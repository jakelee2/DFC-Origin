package org.dbadmin.repository.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.dbadmin.model.Businessrule;
import org.dbadmin.model.Job;
import org.dbadmin.model.JobTransferInfo;
import org.dbadmin.model.Task;
import org.dbadmin.model.UserJob;
import org.dbadmin.repository.JobRepository;
import org.dbadmin.repository.TaskRepository;
import org.dbadmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcJobRepositoryImpl implements JobRepository {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private UserRepository userRepository;

  private SimpleJdbcInsert insertJob;
  
  private JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcJobRepositoryImpl(DataSource dataSource, TaskRepository taskRepository,
      UserRepository userRepository) {

    this.insertJob = new SimpleJdbcInsert(dataSource).withTableName("jobs").usingGeneratedKeyColumns("id");

    this.userRepository = userRepository;
    
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  /**
   * use LIKE SQL predicate for searching
   */
  @Override
  public Collection<Job> findByName(String name) throws DataAccessException {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name + "%");
    List<Job> jobs = this.namedParameterJdbcTemplate.query(
        "SELECT id, name, description, status FROM jobs WHERE name like :name", params,
        BeanPropertyRowMapper.newInstance(Job.class));
    return jobs;
  }

  /**
   * use equal SQL predicate for searching
   */
  @Override
  public Collection<Job> findByExactName(String name) throws DataAccessException {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name + "%");
    List<Job> jobs = this.namedParameterJdbcTemplate.query(
        "SELECT id, name, description, status FROM jobs WHERE name =:name", params,
        BeanPropertyRowMapper.newInstance(Job.class));
    jobs = loadAllRelatedTasksAndJobs(jobs);
    return jobs;
  }
  
  // Find Jobs assigned to a user by username
  @Override
  public Collection<Job> findByUsername(String username) throws DataAccessException {    
    
    Map<String, Object> params = new HashMap<>();
    params.put("username", username);
    List<Job> jobs = this.namedParameterJdbcTemplate.query(
        "SELECT j.id, j.name " + 
        "FROM jobs j " + 
        "INNER JOIN users_jobs uj ON uj.job_id  = j.id " + 
        "INNER JOIN users u       ON uj.user_id = u.id " + 
        "WHERE u.username = :username ", params,
        BeanPropertyRowMapper.newInstance(Job.class));
    return jobs;
  }
  
  // Find Jobs transfer request by her transferer username
  @Override
  public List<JobTransferInfo> findTransferByTransfererName(String username, boolean isAdmin) throws DataAccessException {    
    
    Map<String, Object> params = new HashMap<>();
    params.put("username", username);
    // uj.id is unique value
    String sql = "SELECT j.id AS jobId, uj.id AS userJobId, j.name AS jobName, u.username AS transferer, u2.username AS transferee, t.description, j.level_id, SUBSTRING(r.role_name, 6, LEN(r.role_name)) AS level_name " + 
        "FROM jobs j " + 
        "INNER JOIN roles r ON r.id = j.level_id " + 
        (isAdmin ? "LEFT OUTER ": "INNER ") + "JOIN users_jobs uj ON uj.active=1 AND uj.job_id = j.id " + 
        (isAdmin ? "LEFT OUTER ": "INNER ") + "JOIN users u ON uj.user_id = u.id " + 
        "LEFT OUTER JOIN job_transfer_queue t ON t.active=1 AND t.user_job_id = uj.id " + 
        "LEFT OUTER JOIN users u2 ON t.transferee = u2.id " +
        (isAdmin ? "" : "WHERE u.username = :username ") ;

    List<JobTransferInfo> jobTransferInfo = this.namedParameterJdbcTemplate.query(
        sql, params, BeanPropertyRowMapper.newInstance(JobTransferInfo.class));
    return jobTransferInfo;
  }

  // Find Jobs transfer requestby her transferee username
  @Override
  public List<JobTransferInfo> findJobTransferByTransfereeName(String username) throws DataAccessException {    
    
    Map<String, Object> params = new HashMap<>();
    params.put("username", username);
    String sql = "SELECT j.id AS jobId, uj.id AS userJobId, j.name AS jobName, u.username AS transferer, u1.username AS transferee, t.description, j.level_id, SUBSTRING(r.role_name, 6, LEN(r.role_name)) AS level_name " + 
        "FROM jobs j " + 
        "INNER JOIN users_jobs uj ON uj.job_id = j.id " + 
        "INNER JOIN job_transfer_queue t ON t.user_job_id = uj.id " + 
        "INNER JOIN users u ON t.transferer = u.id " + 
        "INNER JOIN users u1 ON t.transferee = u1.id " + 
        "INNER JOIN roles r ON r.id = j.level_id " + 
        "WHERE t.active = 1 AND u1.username = :username ";
    
    List<JobTransferInfo> jobTransferInfo = this.namedParameterJdbcTemplate.query(
        sql, params, BeanPropertyRowMapper.newInstance(JobTransferInfo.class));
    return jobTransferInfo;
  }

  public List<UserJob> findUserJobExistsByJobIdAndUserId(int userId, int jobId){
    Map<String, Object> params = new HashMap<>();
    params.put("userId", userId);
    params.put("jobId", jobId);
    
    String sql = "SELECT id, user_id, job_id, active FROM users_jobs WHERE user_id = :userId AND job_id = :jobId ";
    List<UserJob> userJob = this.namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(UserJob.class));
    return userJob;
  }

  // transferer requests 'job transfer' to transferee(userId)
  // if a transfer of the job is Not existing in job_transfer_queue table, we create (INSERT INTO) a new transfer
  // if a transfer of the job is already existing in job_transfer_queue table, we just update the transfer.
  @Override
  public void transferJob(int transfererId, String transfererUsername, boolean transfererIsAdmin, int jobId, int userJobId, int transfereeId, String transfereeUsername) throws DataAccessException{
    Map<String, Object> params = new HashMap<>();
    params.put("transferer", transfererId);
    params.put("jobId", jobId);
    params.put("userJobId", userJobId);
    params.put("transferee", transfereeId);
    
    String sql = "SELECT j.id AS jobId, uj.id AS userJobId, j.name AS jobName, t.transferer AS transferer, t.transferee AS transferee, t.description " +
        "FROM job_transfer_queue t " +
        "INNER JOIN users_jobs uj ON uj.id = t.user_job_id " + 
        "INNER JOIN jobs j ON j.id = uj.job_id " +
        "WHERE uj.job_id = :jobId AND uj.id = :userJobId AND t.active = 1 ";
    
    List<JobTransferInfo> jobTransferInfo = this.namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(JobTransferInfo.class));
    if(jobTransferInfo == null || jobTransferInfo.size() < 1) { // if the transfer data not exist, create one.
      sql = "INSERT INTO job_transfer_queue(job_id, user_job_id, transferer, transferee, description, active) VALUES (:jobId, :userJobId, :transferer, :transferee, 'waiting', 1) ";
    } else {
      if(jobTransferInfo.get(0).getTransferer().equals(String.valueOf(transfererId))){ // When a user(not admin) who owns the job tries to transfer
        sql = "UPDATE job_transfer_queue SET transferee = :transferee WHERE user_job_id = :userJobId AND active = 1 ";
      }
      else {
        // if a user(not admin) who doesn't own the job tries to transfer, this request has to be ignored. 
        if(transfererIsAdmin == false)
          return;
        
        // When ADMIN tries to transfer job to any person,
        // i. remove existing job_transfer_queue data
        deactivateTransfer(userJobId, Integer.valueOf(jobTransferInfo.get(0).getTransferee()).intValue(), "reassigned to " + transfereeUsername + " by " + transfererUsername, false);
        // ii. insert new row which has admin and transferee
        sql = "INSERT INTO job_transfer_queue(job_id, user_job_id, transferer, transferee, description, active) VALUES (:jobId, :userJobId, :transferer, :transferee, 'waiting', 1) ";
      }
    }
    this.namedParameterJdbcTemplate.update(sql, params);

  }

  // make inactive (active = 0) job_transfer_queue table with new transfereeId
  // we don't delete data.  need to keep all the data for history
  @Override
  public void deactivateTransfer(int userJobId, int transfereeId, String description, boolean isForDefault) throws DataAccessException{
    Map<String, Object> params = new HashMap<>();
    params.put("user_job_id", userJobId);
    params.put("transferee", transfereeId);    // it's not used when isForDefault is true
    String sql = "UPDATE job_transfer_queue " + 
                 "SET description = '" + description + "', active = 0 " +
                 "WHERE active = 1 AND user_job_id = :user_job_id " + 
                 (isForDefault ? "" : "AND transferee = :transferee ");
    this.namedParameterJdbcTemplate.update(sql, params);
  }
  
  @Override
  public Number addJobAssignee(int jobId, int transfereeId) throws DataAccessException{
    Map<String, Object> params = new HashMap<>();
    params.put("job_id", jobId);
    params.put("user_id", transfereeId);

    String sql = "INSERT INTO users_jobs (user_id, job_id, active) VALUES (:user_id, :job_id, 1) ";
    this.namedParameterJdbcTemplate.update(sql, params);
    sql = "SELECT id FROM users_jobs WHERE user_id = :user_id AND job_id = :job_id AND active = 1 ";
    Number id = (Integer) this.namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    return id;
  }
  
  @Override
  public void updateActiveInUserJob(int userJobId, boolean active) throws DataAccessException{
    Map<String, Object> params = new HashMap<>();
    params.put("userJobId", userJobId);
    params.put("active", active);
    
    String sql =  "UPDATE users_jobs SET active = :active WHERE id = :userJobId ";
    this.namedParameterJdbcTemplate.update(sql, params);
  }
  
  // update users_jobs table with new transfereeId
  // It's updating job owner
  @Override
  public void updateJobAssignee(int jobId, int transfereeId) throws DataAccessException{
    Map<String, Object> params = new HashMap<>();
    params.put("job_id", jobId);
    params.put("user_id", transfereeId);
    
    String sql =  "UPDATE users_jobs SET user_id = :user_id WHERE job_id = :job_id ";
    this.namedParameterJdbcTemplate.update(sql, params);
  }

  // it's updating job security levels (ADMIN, USER)
  @Override
  public void updateLevel(int jobId, int jobLevel) throws DataAccessException{
    Map<String, Object> params = new HashMap<>();
    params.put("job_id", jobId);
    params.put("jobLevel", jobLevel);
    
    String sql =  "UPDATE jobs SET level_id = :jobLevel WHERE id = :job_id ";
    this.namedParameterJdbcTemplate.update(sql, params);
  }

  public List<JobTransferInfo> findTransferHistoryByUserId (int userId, boolean isAdmin){
    Map<String, Object> params = new HashMap<>();
    params.put("user_id", userId);
    String sql = "SELECT t.id AS jobId, uj.id AS userJobId, j.name AS jobName, u.username AS transferer, u1.username AS transferee, t.description, j.level_id, SUBSTRING(r.role_name, 6, LEN(r.role_name)) AS level_name, t.complete_timestamp AS lastUpdated " +
        "FROM job_transfer_queue t " +
        "INNER JOIN users_jobs uj ON uj.id = t.user_job_id " + 
        "INNER JOIN jobs j ON uj.job_id = j.id " +
        "INNER JOIN users u ON u.id = t.transferer " +
        "INNER JOIN users u1 ON u1.id = t.transferee " +
        "INNER JOIN roles r ON r.id = j.level_id " +
        "WHERE t.active = 0 " + (isAdmin ? "" : "AND (t.transferer = :user_id OR t.transferee = :user_id) ") ;
    
    List<JobTransferInfo> jobTransferInfo = this.namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(JobTransferInfo.class));
    return jobTransferInfo;
  }

  @Override
  public Job findById(int id) throws DataAccessException {
    Job job = new Job();
    try {
      Map<String, Object> params = new HashMap<>();
      params.put("id", id); //
      job = this.namedParameterJdbcTemplate.queryForObject("SELECT id, name, description, status as status_id FROM jobs WHERE id=:id",
          params, BeanPropertyRowMapper.newInstance(Job.class));
      job = this.loadRelatedTasksAndJobs(job);
      
    } catch (EmptyResultDataAccessException ex) {
      throw new ObjectRetrievalFailureException(Job.class, id);
    }
    return job;
  }

  @Override
  public void save(Job job) throws DataAccessException {
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(job);

    if (job.isNew()) {
      Number newKey = this.insertJob.executeAndReturnKey(parameterSource);
      job.setId(newKey.intValue());
      job.setStatusId(1); // Status.CREATED
    } else {
      this.namedParameterJdbcTemplate.update("UPDATE jobs SET name=:name, description=:description WHERE id=:id",
          parameterSource);
    }
  }

  /**
   * For a list of Jobs
   * @param jobs
   * @return
   */
  public List<Job> loadAllRelatedTasksAndJobs(List<Job> jobs) {
    List<Job> filledJobsList = new ArrayList<>();
    for (Job job : jobs) {
      filledJobsList.add(this.loadRelatedTasksAndJobs(job));
    }
    return filledJobsList;
  }

  
  /**
   * The main method which keeps Job-Tasks-Jobs logic dependency
   * @param job
   */
  public Job loadRelatedTasksAndJobs(Job job) {
    Integer jobId = job.getId();
    Job filledJob = new Job();
    
    filledJob.setId(jobId);
    filledJob.setName(job.getName());
    filledJob.setDescription(job.getDescription());
    filledJob.setStatusId(job.getStatusId());
    
    // get all related tasks
      String taskQuery = 
          "SELECT " 
          + "jobs.id as job_id, "
          + "tasks.id as task_id, " 
          + "tasks.name as name, "
          + "tasks.description as description, " 
          + "tasks.body as body, "
          + "tasks.type_id as type_id, "
          + "tasks.source_db_conn_id as source_db_conn_id, " 
          + "tasks.target_db_conn_id as target_db_conn_id, "     
          + "tasks.status as status, " 
          + "task2job_link.related_task_priority as priority_in_related_job, "
          + "task2job_link.task_start_condition_id as task_start_condition_id "          
          + "FROM jobs, tasks, task2job_link "
          + "WHERE job_id=? " 
          + "AND jobs.id = task2job_link.job_id "
          + "AND tasks.id = task2job_link.related_task_id";
      
      List<Task> taskList = this.jdbcTemplate.query(taskQuery, new JdbcJob2TaskRowMapper(), jobId);

      
		// creating Map of prioritized Tasks and add related Businessrules to each of them
		Map<Integer, Task> taskMap = new TreeMap<>();
		int taskPriority;
		for (Task t : taskList) {
			taskPriority = t.getPriorityInRelatedJob();
			while (taskMap.containsKey(taskPriority)) {
				// Tasks within the Job have the same priority caused by business logic or user problems.
				// So, we need key (priority) as close as possible to the original value increasing key by 1
				taskPriority++;				
			}
			taskMap.put(taskPriority, this.loadRelatedBusinessrules(t));
		}
		filledJob.setTasks(taskMap);
      
      
      // get all related jobs to this job
      String jobQuery = 
          "SELECT DISTINCT "
          + "jobs.*, job2job_link.related_job_priority "
          + "FROM jobs, job2job_link "
          + "WHERE jobs.id IN ("
          + "	SELECT " 
          + "	job2job_link.related_job_id "
          + "	FROM jobs, job2job_link "
          + "	WHERE jobs.id= ? "
          + "	AND jobs.id = job2job_link.job_id) "
          + "AND jobs.id = job2job_link.related_job_id "
          + "AND job2job_link.job_id = ?";

      List<Job> relatedJobsList = this.jdbcTemplate.query(jobQuery, new JdbcJobRowMapper(), jobId, jobId);
      Map<Integer, Job> jobMap = new TreeMap<>();
      int internalJobPriority;
      for (Job j : relatedJobsList) {
    	  internalJobPriority = j.getPriorityInRelatedJob();
    	  while (jobMap.containsKey(internalJobPriority)) {
				// Jobs within the Job have the same priority caused by business logic or user problems.
				// So, we need key (priority) as close as possible to the original value increasing key by 1
    		  internalJobPriority++;				
    	  }
    	  jobMap.put(internalJobPriority, j);
      }
      filledJob.setJobs(jobMap);

      return filledJob;
  
  }
  

  /**
   * Add related prioritized Businessrules to the Task.
   * NB: modifies input parameter
   * @param task
   * @return
   */
  private Task loadRelatedBusinessrules(Task task) {
	  Integer taskId = task.getId();
      String query = 
    		  "SELECT * "
    		  +"FROM businessrules b, brule2task_link l "
    		  +"WHERE b.id IN ( "
    		          +"SELECT related_brule_id FROM brule2task_link "
    		          +"WHERE task_id=?) "
    		  +"AND l.task_id=? "
    		  +"AND b.id = l.related_brule_id";
      
      List<Businessrule> ruleList = this.jdbcTemplate.query(query, new JdbcBusinessruleRowMapper(), taskId, taskId);
      Map<Integer, Businessrule> ruleMap = new TreeMap<>();
      for (Businessrule br : ruleList) {
    	  br = this.loadTablesAndColumns4Businessrule(br);
    	  ruleMap.put(br.getPriorityInHostTask(), br);
      }
      task.setBusinessrules(ruleMap);
      return task;
  
  }
  
  /**
   * Loads Businessrule object with all information, related to tables/columns/connection
   * form DB tables: brule2tabs_columns_link, dbtables, dbcolumns.
   * 
   * NB: modifies input object.
   * 
   * @param Businessrule 
   * @return Businessrule brule
   */
  public Businessrule loadTablesAndColumns4Businessrule (Businessrule brule) {
	  Integer bruleId = brule.getId();
      String query;
     
      query =  "SELECT "+
    		  	"l.source_table_id, "+
    		  	"l.source_table_column_id, "+ 
    		  	"l.target_table_id, "+ 
    		  	"l.target_table_column_id, "+
    		  	"t.name as source_table_name, "+
    		  	"c.name as source_column_name, "+
    		  	"t1.name as target_table_name, "+
    		  	"c1.name as target_column_name, "+
    		  	"t.dbconnection_id as source_dbconnection_id, "+
    		  	"t1.dbconnection_id as target_dbconnection_id "+
    		  	"FROM brule2tabs_columns_link l, dbtables t, dbcolumns c, dbtables t1, dbcolumns c1 "+
    		  	"WHERE l.brule_id=? "+ 
    		  	"AND t.id = l.source_table_id "+
    		  	"AND c.id = l.source_table_column_id "+
    		  	"AND t1.id = l.target_table_id "+
    		  	"AND c1.id = l.target_table_column_id";
      try {
//NB! Spring throws an EmptyResultDataAccessException, instead of returning a null, etc. when record not found.
      Businessrule bruleTabCol = this.jdbcTemplate.queryForObject(query, new JdbcBusinessruleTabColMapper(), bruleId);	  
      brule.setSourceTableId(bruleTabCol.getSourceTableId());
      brule.setSourceTableColumnId(bruleTabCol.getSourceTableColumnId());
      brule.setTargetTableId(bruleTabCol.getTargetTableId());
      brule.setTargetTableColumnId(bruleTabCol.getTargetTableColumnId()); 
      brule.setSourceTableName(bruleTabCol.getSourceTableName());
      brule.setTargetTableName(bruleTabCol.getTargetTableName());
      brule.setSourceColumnName(bruleTabCol.getSourceColumnName()); 
      brule.setTargetColumnName(bruleTabCol.getTargetColumnName());
      brule.setSourceDbconnectionId(bruleTabCol.getSourceDbconnectionId());
      brule.setTargetDbconnectionId(bruleTabCol.getTargetDbconnectionId());
      bruleTabCol = null; 
      } catch (EmptyResultDataAccessException e) 
      		{/** No data for this Businessrule was found in brule2tabs_columns_link**/};
      
	  return brule;
  }

}
