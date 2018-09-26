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

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.dbadmin.model.JsonMessage;
import org.dbadmin.model.Task;
import org.dbadmin.model.JsonMessage.Status;
import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 */
@Controller
public class TaskController {

  private final TemplateService templateService;

  @Autowired
  public TaskController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @InitBinder
  public void setAllowedFields(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }

  @RequestMapping(value = "/tasks/new", method = RequestMethod.GET)
  public String initCreationForm(Map<String, Object> model) {
    Task task = new Task();
    model.put("task", task);
    return "tasks/createOrUpdateETLTaskForm";
  }

  @RequestMapping(value = "/tasks/new", method = RequestMethod.POST)
  public String processCreationForm(@Valid Task task, BindingResult result) {
    if (result.hasErrors()) {
      return "tasks/createOrUpdateETLTaskForm";
    } else {
      this.templateService.saveTask(task);
      return "redirect:/tasks/" + task.getId();
    }
  }

  @RequestMapping(value = "/tasks/find", method = RequestMethod.GET)
  public String initFindForm(Map<String, Object> model) {
    model.put("task", new Task());
    return "tasks/findETLTasks";
  }

  @RequestMapping(value = "/tasks", method = RequestMethod.GET)
  public String processFindForm(Task task, BindingResult result, Map<String, Object> model) {

    if (task.getName() == null) {
      task.setName(""); // empty string signifies broadest possible search
    }
    {
    }

    // find by name
    String taskName = task.getName();
    Collection<Task> results = this.templateService.findTaskByName(taskName);
    if (results.isEmpty()) {
      result.rejectValue("name", "notFound", "not found");
      return "tasks/findETLTasks";
    } else if (results.size() == 1) {
      // 1 was found
      task = results.iterator().next();
      return "redirect:/tasks/" + task.getId();
    } else {
      // multiple were found
      model.put("selections", results);
      return "tasks/taskList";
    }
  }

  @RequestMapping(value = "/tasks/{taskId}/edit", method = RequestMethod.GET)
  public String initUpdateETLTaskForm(@PathVariable("taskId") int taskId, Model model) {
    Task task = this.templateService.findTaskById(taskId);
    model.addAttribute(task);
    return "tasks/createOrUpdateETLTaskForm";
  }

  @RequestMapping(value = "/tasks/{taskId}/edit", method = RequestMethod.POST)
  public String processUpdateETLTaskForm(@Valid Task task, BindingResult result,
      @PathVariable("taskId") int taskId) {
    if (result.hasErrors()) {
      return "tasks/createOrUpdateETLTaskForm";
    } else {
      task.setId(taskId);
      this.templateService.saveTask(task);
      return "redirect:/tasks/{taskId}";
    }
  }

  @RequestMapping("/tasks/{taskId}")
  public ModelAndView showETLTask(@PathVariable("taskId") int taskId) {
    ModelAndView mav = new ModelAndView("tasks/taskDetails");
    Task task = this.templateService.findTaskById(taskId);
    mav.addObject(task);
    mav.addObject(task.getAssociatedJobsList()); //jobList
    mav.addObject(task.getBusinessruleList());   //businesruleList
    return mav;
  }

  
  /** REST Methods
  
  /**
   * 
   * Add a Task as dependent to a Job (host Job) and set their priority in the host Job
   * and Start Condition (see Task domain object for the details)
   * @param jobIdParam
   * @param relatedTaskIdParam
   * @param relatedTaskPriorityParam
   * @param taskStartConditionIdParam 
   * @return Json msg
   * 
   * Usage example: http://localhost:8080/tasks/addrelatedtask/1,2,3,100
   */
	@RequestMapping(value = "/tasks/addrelatedtask/{jobIdParam},{relatedTaskIdParam},{relatedTaskPriorityParam},{taskStartConditionIdParam}", method = RequestMethod.GET)
	public ResponseEntity<Object> addRelatedTask2Job(@PathVariable String jobIdParam,
			@PathVariable String relatedTaskIdParam, @PathVariable String relatedTaskPriorityParam, @PathVariable String taskStartConditionIdParam) {
		JsonMessage msg = new JsonMessage();
		msg.setStatus(Status.ERROR);
		msg.setMessage("ERROR: can not add related job");
		try {
			int jobId = Integer.parseInt(jobIdParam);
			int relatedTaskId = Integer.parseInt(relatedTaskIdParam);
			int relatedTaskPriority = Integer.parseInt(relatedTaskPriorityParam);
			int taskStartConditionId = Integer.parseInt(taskStartConditionIdParam);			
			this.templateService.addRelatedTask2Job(jobId, relatedTaskId, relatedTaskPriority, taskStartConditionId);
	        msg.setStatus(Status.SUCCESS);
	        msg.setMessage("Task id="+relatedTaskId+" was successfully added to host Job id="+jobIdParam+", priority="+relatedTaskPriority+ ", start condition id="+taskStartConditionId);
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
		}
	}
	
	/**
	 * Usage example: http://localhost:8080/tasks/deleterelatedtask/1,2
	 * @param jobIdParam
	 * @param relatedTaskIdParam
	 * @return
	 */
	@RequestMapping(value = "/tasks/deleterelatedtask/{jobIdParam},{relatedTaskIdParam}", method = RequestMethod.GET)
	public ResponseEntity<Object> deleteRelatedTask(@PathVariable String jobIdParam,
			@PathVariable String relatedTaskIdParam) {
		JsonMessage msg = new JsonMessage();
		msg.setStatus(Status.ERROR);
		msg.setMessage("ERROR: can not delete related task");
		try {
			int jobId = Integer.parseInt(jobIdParam);
			int relatedTaskId = Integer.parseInt(relatedTaskIdParam);
			this.templateService.deleteRelatedTask(jobId, relatedTaskId);
	        msg.setStatus(Status.SUCCESS);
	        msg.setMessage("Related task id="+relatedTaskId+" was successfully deleted from host Job id="+jobIdParam);
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
		}
	}

}
