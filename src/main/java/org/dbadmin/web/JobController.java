/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.dbadmin.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.dbadmin.model.DBConnectionInfo;
import org.dbadmin.model.DataTable;
import org.dbadmin.model.DbConnection;
import org.dbadmin.model.ExecutionOutput;
import org.dbadmin.model.IngestionInfo;
import org.dbadmin.model.Job;
import org.dbadmin.model.JobTransferInfo;
import org.dbadmin.model.JsonMessage;
import org.dbadmin.model.Role;
import org.dbadmin.model.Task;
import org.dbadmin.model.User;
import org.dbadmin.model.UserJob;
import org.dbadmin.model.ValidationInfo;
import org.dbadmin.model.ValidationStats;
import org.dbadmin.model.JsonMessage.Status;
import org.dbadmin.service.TemplateService;
import org.dbadmin.util.DataIngestionUtils;
import org.dbadmin.util.ErrorCode;
import org.dbadmin.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**  
 *
 */
@Controller
@Api(value="job", description="Operations pertaining to Jobs")
public class JobController {

  private final TemplateService templateService;

  @Autowired
  public JobController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @InitBinder
  public void setAllowedFields(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }

  @RequestMapping(value = "/jobs/new", method = RequestMethod.GET)
  public String initCreationForm(Map<String, Object> model) {
    Job job = new Job();
    model.put("job", job);
    return "jobs/createOrUpdateJobForm";
  }

  @RequestMapping(value = "/jobs/new", method = RequestMethod.POST)
  public String processCreationForm(@Valid Job job, BindingResult result) {
    if (result.hasErrors()) {
      return "jobs/createOrUpdateJobForm";
    } else {
      this.templateService.saveJob(job);
      return "redirect:/jobs/" + job.getId();
    }
  }

  @RequestMapping(value = "/jobs/find", method = RequestMethod.GET)
  public String initFindForm(Map<String, Object> model) {
    model.put("job", new Job());
    return "jobs/findJobs";
  }

  @RequestMapping(value = "/jobs", method = RequestMethod.GET)
  @ApiOperation(value = "Find all the job info.", notes = "Find all the job info related to the session user.")
  public String processFindForm(Job job, BindingResult result, Map<String, Object> model) {

    List<String> user_ADMIN = (List<String>) Arrays.asList("ROLE_ADMIN");
    List<String> authorities = PrincipalUtils.getPrincipalAuthorities();
    List<JobTransferInfo> jobsAssigned = null;
    List<JobTransferInfo> jobsRequested = null;
    List<Role> jobRoles = null;
    String username = PrincipalUtils.getPrincipal();
    boolean isAdmin = false;

    if(authorities.containsAll(user_ADMIN)){
      jobsAssigned = this.templateService.findJobTransferByTransfererName(username, true);
      jobsRequested = this.templateService.findJobTransferByTransfereeName(username);
      isAdmin = true;
    }
    else { // else if user is not Admin, return jobs only which is assigned to the user
      jobsAssigned = this.templateService.findJobTransferByTransfererName(username, false);
      jobsRequested = this.templateService.findJobTransferByTransfereeName(username);
    }
    model.put("isAdmin", isAdmin);

    // Find all the users except the user (username)
    List<List<User>> usersForDropdown = new ArrayList<List<User>>();
    try {
      for(JobTransferInfo jti: jobsAssigned){
        usersForDropdown.add((List<User>) this.templateService.findUsersByJobLevelAndExceptUsername(username, jti.getLevelId(), isAdmin));
      }
    } catch (DataAccessException e) {
        e.printStackTrace();
    } 
    model.put("usersForDropdown", usersForDropdown);  
    
    List<User> users = (List<User>) templateService.findUserByExactUserName(username);
    Integer user_Id = users.get(0).getId();
    List<JobTransferInfo> jobTransferHIstory = this.templateService.findTransferHistoryByUserId(user_Id.intValue(), isAdmin);
    model.put("jobTransferHIstory", jobTransferHIstory);  
    
    if (jobsAssigned.isEmpty() && jobsRequested.isEmpty() && jobTransferHIstory.isEmpty()) {
      // no jobs found
      result.rejectValue("name", "notFound", "not found");
      return "jobs/findJobs";
    } 
    else {
      // multiple were found
      model.put("selections", jobsAssigned);
      if(jobsRequested != null){
        model.put("jobsRequested", jobsRequested);
      }
      jobRoles = (List<Role>) this.templateService.findRoleByRoleName("");
      for(Role jobRole: jobRoles){
        jobRole.setRole_name( jobRole.getRole_name().substring(5));// without "ROLE_"
      }
      model.put("jobRoles", jobRoles);
      return "jobs/jobList";
    }
  }

  @RequestMapping(value = "/jobs/findUsersForJob", method = RequestMethod.POST)
  @ApiOperation(value = "Find users for a job", notes = "Find users who are eligible to do the job based on the given level and job id")
  public ResponseEntity<List<User>> findUsersForJob(@RequestParam String jobId, @RequestParam String selectedLevel) { 
    
    Integer jobLevel = Integer.parseInt(selectedLevel);    
    String username = PrincipalUtils.getPrincipal();
    List<User> users = (List<User>) this.templateService.findUsersByJobLevelAndExceptUsername(username, jobLevel.intValue(), true);
    return new ResponseEntity<>(users, HttpStatus.OK);
  }
  
  @RequestMapping(value = "/jobs/accept/{jobId}/{userJobId}", method = RequestMethod.GET)
  @ApiOperation(value = "Accept job transfer", notes = "Accept a job transfer request assigned to a Transferee")
  public String acceptJobTransfer(@PathVariable("jobId") int jobId, @PathVariable("userJobId") int userJobId, Model model) {
    String transfereeUsername = PrincipalUtils.getPrincipal();
    List<User> users = (List<User>) templateService.findUserByExactUserName(transfereeUsername);
    Integer transferee_Id = users.get(0).getId();

    // check if the job request is still active in the job_transfer_queue
    List<JobTransferInfo> jtiList = this.templateService.findJobTransferByTransfereeName(transfereeUsername);
    if(jtiList == null || jtiList.size() == 0) {// if not active, do nothing and return to jobs page
      return "redirect:/jobs";
    } // else continue
    
    // to accept, 
    // 1. add a new row or activate an existing row in users_jobs table
    // if the job (job_id) and transferee(user_id) combination is Not in the users_jobs table, we add the job for the user.
    List<UserJob> userJobs =  this.templateService.findUserJobExistsByJobIdAndUserId(transferee_Id, jobId);    
    if(userJobs == null || userJobs.size() == 0)
      this.templateService.addJobAssignee(jobId, transferee_Id.intValue());
    else if(!userJobs.get(0).isActive()) // if existing one is not active
    {
      // activate users_jobs row
      this.templateService.updateActiveInUserJob(userJobs.get(0).getId(), true);
    }
    
    // 2. deactivate the row in job_transfer_queue table
    this.templateService.deactivateTransfer(userJobId, transferee_Id.intValue(), "accepted", false);
    return "redirect:/jobs";
  }
  
  @RequestMapping(value = "/jobs/decline/{jobId}/{userJobId}", method = RequestMethod.GET)
  @ApiOperation(value = "Decline job transfer", notes = "Decline a job transfer requested to a transferee")
  public String declineJobTransfer(@PathVariable("jobId") int jobId, @PathVariable("userJobId") int userJobId, Model model) {
    String transfereeUsername = PrincipalUtils.getPrincipal();
    List<User> users = (List<User>) templateService.findUserByExactUserName(transfereeUsername);
    Integer transferee_Id = users.get(0).getId();

    // to decline, 
    // 1. don't need to touch users_jobs table
    // 2. need to deactivate the row in job_transfer_queue table    
    this.templateService.deactivateTransfer(userJobId, transferee_Id.intValue(), "declined", false);
    return "redirect:/jobs";
  }

  @RequestMapping(value = "/jobs/discardJob/{jobId}/{userJobId}", method = RequestMethod.GET)
  @ApiOperation(value = "Discard Job", notes = "Discard a Job from user's own Job list")
  public String discardJobInList(@PathVariable("jobId") int jobId, @PathVariable("userJobId") int userJobId, Model model) {
    // deactivate users_jobs row
    this.templateService.updateActiveInUserJob(userJobId, false);
    return "redirect:/jobs";
  }

  @RequestMapping(value = "/jobs/dectivateTransfer", method = RequestMethod.POST)
  @ApiOperation(value = "Deactivate a job transfer", notes = "Deactivate a job transfer based on job id")
  public String deactivateJobTransfer(@RequestParam String jobId, @RequestParam String userJobId, @RequestParam String jobOwner, Model model) {
    String transfererUsername = PrincipalUtils.getPrincipal();
    int userJob_Id = Integer.parseInt(userJobId);
    boolean transfererIsAdmin = false;
    List<String> user_ADMIN = (List<String>) Arrays.asList("ROLE_ADMIN");
    List<String> authorities = PrincipalUtils.getPrincipalAuthorities();
    if(authorities.containsAll(user_ADMIN)){
      transfererIsAdmin = true;
    }
    
    boolean jobInvolvedWithAdmin = false;
    
    // check what user (Admin or other) created the job transfer 
    List<Role> roles = this.templateService.getUserRolesOfJobTransfer(userJob_Id);
    for(Role role: roles){
      if("ROLE_ADMIN".equals(role.getRole_name())){
        jobInvolvedWithAdmin = true;
        break;
      }
    }
    
    if(transfererIsAdmin || !jobInvolvedWithAdmin)
      this.templateService.deactivateTransfer(userJob_Id, 0, "declined by " + transfererUsername, true);
    return "redirect:/jobs";
  }

  @RequestMapping(value = "/jobs/transfer", method = RequestMethod.POST)
  @ApiOperation(value = "Generate a job transfer request", notes = "Generate a job transfer request to transferee based on job id and transferee id")
  public String transferJob(@RequestParam String jobId,  @RequestParam String userJobId, @RequestParam String transfereeId) { 
    
    String transfererUsername = PrincipalUtils.getPrincipal();
    boolean transfererIsAdmin = false;
    List<String> user_ADMIN = (List<String>) Arrays.asList("ROLE_ADMIN");
    List<String> authorities = PrincipalUtils.getPrincipalAuthorities();
    if(authorities.containsAll(user_ADMIN)){
      transfererIsAdmin = true;
    }
    List<User> users = (List<User>) templateService.findUserByExactUserName(transfererUsername);
    Integer transferer_Id = users.get(0).getId();
    Integer job_Id = Integer.parseInt(jobId);
    
    // if there is no relevant users_jobs table row, we create new row for this transfer with [job_id and transferer_Id]
    Integer userJob_Id = null;
    if(StringUtils.isEmpty(userJobId)){
      userJob_Id = (Integer) templateService.addJobAssignee(job_Id, transferer_Id);
    } else{
      userJob_Id = Integer.parseInt(userJobId);
    }
    
    Integer transferee_Id = Integer.parseInt(transfereeId);
    String transfereeUsername = templateService.findUserById(transferee_Id).getUsername();
    
    // need to create a row in the job_transfer_queue table
    this.templateService.transferJob(transferer_Id.intValue(), transfererUsername, transfererIsAdmin, job_Id.intValue(), userJob_Id.intValue(),transferee_Id.intValue(), transfereeUsername);
    return "redirect:/jobs";
  }

  @RequestMapping(value = "/jobs/changeJobLevel", method = RequestMethod.POST)
  @ApiOperation(value = "Change job level", notes = "Change a job's security level with given security level based on job id")
  public String changeJobLevel(@RequestParam String jobId, @RequestParam String selectedLevel, @RequestParam String selectedLevelName) { 
    
    Integer job_Id = Integer.parseInt(jobId);
    Integer jobLevel = Integer.parseInt(selectedLevel);    
    this.templateService.updateJobLevel(job_Id.intValue(), jobLevel.intValue());
    return "redirect:/jobs";
  }

  @RequestMapping(value = "/jobs/{jobId}/edit", method = RequestMethod.GET)
  public String initUpdateJobForm(@PathVariable("jobId") int jobId, Model model) {
    Job job = this.templateService.findJobById(jobId);
    model.addAttribute(job);
    return "jobs/createOrUpdateJobForm";
  }

  @RequestMapping(value = "/jobs/{jobId}/edit", method = RequestMethod.POST)
  public String processUpdateJobForm(@Valid Job job, BindingResult result,
      @PathVariable("jobId") int jobId) {
    if (result.hasErrors()) {
      return "jobs/createOrUpdateJobForm";
    } else {
      job.setId(jobId);
      this.templateService.saveJob(job);
      return "redirect:/jobs/{jobId}";
    }
  }

  @RequestMapping("/jobs/{jobId}")
  @ApiOperation(value = "Show job", notes = "Show details of a job")
  public ModelAndView showJob(@PathVariable("jobId") int jobId) {
    ModelAndView mav = new ModelAndView("jobs/jobDetails");
    Job job = this.templateService.findJobById(jobId);
    mav.addObject(job);
    mav.addObject(job.getTasklist()); //taskList 
    mav.addObject(job.getJoblist()); //jobList
    return mav;
  }

 
  //REST methods here:
  
  @RequestMapping("/jobs/{jobId}/run")
  public ResponseEntity<IngestionInfo> runJob(@PathVariable("jobId") int jobId) {
    Job job = templateService.findJobById(jobId);
    IngestionInfo ingestionInfo = new IngestionInfo(job);
    ErrorCode err = DataIngestionUtils.copyDataFromSrcTableToTargetTable(ingestionInfo);
    String status = "";
    // TODO: Do it correctly with enumerations
    if (err == ErrorCode.OK) {
      status = "success";
    } else {
      status = "failure";
    }
    ingestionInfo.setStatus(status);

    return new ResponseEntity<IngestionInfo>(ingestionInfo, HttpStatus.OK);
  }

  @RequestMapping(value = "/jobs/ingestion", method = RequestMethod.POST)
  public ResponseEntity<IngestionInfo> ingest(@RequestBody IngestionInfo ingestionInfo) {
    templateService.createIngestionJob(ingestionInfo);
    ErrorCode err = DataIngestionUtils.copyDataFromSrcTableToTargetTable(ingestionInfo);
    String status = "";
    // TODO: Do it correctly with enumerations
    if (err == ErrorCode.OK) {
      status = "success";
    } else {
      status = "failure";
    }
    ingestionInfo.setStatus(status);

    return new ResponseEntity<IngestionInfo>(ingestionInfo, HttpStatus.OK);
  }

  
  // http://localhost:8080/jobs/dq/jobs
  @RequestMapping(value = "/jobs/dq/{tableName}", method = RequestMethod.GET)
  @ApiOperation(value = "Get data profile of a table", notes = "Get detailed numeric information based on table")
  public ResponseEntity<DataTable> dq(@PathVariable("tableName") String tableName) {
    DataTable data = DataIngestionUtils.getNumericDataFromDataSource(tableName);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }

  @RequestMapping(value = "/jobs/save", method = RequestMethod.POST)
  public ResponseEntity<Job> saveIngestionJob(@RequestBody IngestionInfo ingestionInfo) {
    Job job = templateService.createIngestionJob(ingestionInfo);
    return new ResponseEntity<Job>(job, HttpStatus.OK);
  }

  // TODO: Move this to appropriate controller
  @RequestMapping(value = "/jobs/connectivity", method = RequestMethod.POST)
  public ResponseEntity<DBConnectionInfo> conCheck(@RequestBody DBConnectionInfo db) {
    ErrorCode err = DataIngestionUtils.connectivityCheck(db);
    String status = (err == ErrorCode.OK) ? "success" : "failure";
    db.setStatus(status);

    return new ResponseEntity<DBConnectionInfo>(db, HttpStatus.OK);
  }

  @RequestMapping(value = "/jobs/connectivityds", method = RequestMethod.POST)
  public ResponseEntity<DbConnection> conCheckUsingDataSource(@RequestBody DbConnection db) {
    ErrorCode err = DataIngestionUtils.connectivityCheckUsingDataSource(db);
    String status = (err == ErrorCode.OK) ? "success" : "failure";
    db.setStatus(status);
    return new ResponseEntity<DbConnection>(db, HttpStatus.OK);
  }

  @RequestMapping(value = "/jobs/validation", method = RequestMethod.POST)
  public ResponseEntity<ValidationStats> validateColumns(@RequestBody ValidationInfo dbInfo) {
    ValidationStats out = DataIngestionUtils.genStats(dbInfo);

    return new ResponseEntity<ValidationStats>(out, HttpStatus.OK);
  }
  
  /**
   * REST: create Job queue starting from particular Job with this jobId
   * @param jobId
   * @return
   */
  @RequestMapping(value = "/jobs/getjobsqueue/{jobId}", method = RequestMethod.GET)
  public ResponseEntity<Object> restGetJobQueue(@PathVariable("jobId") String jobId) {
    List<Job> list = this.templateService.getJobsQueue(Integer.valueOf(jobId));
    return new ResponseEntity<Object>(list, HttpStatus.OK);
  }
  
  /**
   * REST: create Task queue starting from particular Job with this jobId
   * @param jobId
   * @return
   */
  @RequestMapping(value = "/jobs/gettasksqueue/{jobId}", method = RequestMethod.GET)
  public ResponseEntity<Object> restGetTaskQueue(@PathVariable("jobId") String jobId) {
    List<Task> list = this.templateService.getTasksQueue(Integer.valueOf(jobId));
    return new ResponseEntity<Object>(list, HttpStatus.OK);
  }
  

  /**
   * 
   * Add one Job as dependent to another Job (host Job) and set their priority in the host Job
   * @param jobIdParam
   * @param relatedJobIdParam
   * @param relatedJobPriorityParam
   * @return Json msg
   * 
   * Usage example: http://localhost:8080/jobs/addrelatedjob/1,2,3
   */
	@RequestMapping(value = "/jobs/addrelatedjob/{jobIdParam},{relatedJobIdParam},{relatedJobPriorityParam}", method = RequestMethod.GET)
	public ResponseEntity<Object> restAddRelatedJob2Job(@PathVariable String jobIdParam,
			@PathVariable String relatedJobIdParam, @PathVariable String relatedJobPriorityParam) {
		JsonMessage msg = new JsonMessage();
		msg.setStatus(Status.ERROR);
		msg.setMessage("ERROR: can not add related job");
		try {
			int jobId = Integer.parseInt(jobIdParam);
			int relatedJobId = Integer.parseInt(relatedJobIdParam);
			int relatedJobPriority = Integer.parseInt(relatedJobPriorityParam);
			this.templateService.addRelatedJob2Job(jobId, relatedJobId, relatedJobPriority);
	        msg.setStatus(Status.SUCCESS);
	        msg.setMessage("Job id="+relatedJobIdParam+" was successfully added to host Job id="+jobIdParam+" as dependent with priority "+relatedJobPriorityParam);
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
		}
	}
	
	/**
	 * Usage example: http://localhost:8080/jobs/deleterelatedjob/1,2
	 * @param jobIdParam
	 * @param relatedJobIdParam
	 * @return
	 */
	@RequestMapping(value = "/jobs/deleterelatedjob/{jobIdParam},{relatedJobIdParam}", method = RequestMethod.GET)
	public ResponseEntity<Object> restDeleteRelatedTask(@PathVariable String jobIdParam,
			@PathVariable String relatedJobIdParam) {
		JsonMessage msg = new JsonMessage();
		msg.setStatus(Status.ERROR);
		msg.setMessage("ERROR: can not delete related job");
		try {
			int jobId = Integer.parseInt(jobIdParam);
			int relatedJobId = Integer.parseInt(relatedJobIdParam);
			this.templateService.deleteRelatedJob(jobId, relatedJobId);
	        msg.setStatus(Status.SUCCESS);
	        msg.setMessage("Related Job id="+relatedJobId+" was successfully deleted from host Job id="+jobIdParam);
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
		}
	}
	
	  /**
	   * REST: run Job by jobId
	   * @param jobId
	   * @return
	   */
	  @RequestMapping(value = "/jobs/runjobbyid/{jobId}", method = RequestMethod.GET)
	  public ResponseEntity<Object> restRunJobById(@PathVariable("jobId") String jobId) {
	    List<ExecutionOutput> list = this.templateService.runJob(Integer.valueOf(jobId));
	    return new ResponseEntity<Object>(list, HttpStatus.OK);
	  }
  
}
