package org.dbadmin.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.Businessrule;
import org.dbadmin.model.DQConfig;
import org.dbadmin.model.DQExecutor;
import org.dbadmin.model.DbConnection;
import org.dbadmin.model.ExecutionOutput;
import org.dbadmin.model.Job;
import org.dbadmin.model.Report;
import org.dbadmin.model.Task;
import org.dbadmin.repository.DbConnectionRepository;
import org.dbadmin.repository.DqConfigRepository;
import org.dbadmin.repository.ExecutionOutputRepository;
import org.dbadmin.repository.JobRepository;
import org.dbadmin.repository.JobsTasksLogic;
import org.dbadmin.util.ReportToExecutionOutputAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Utility class which holds current Jobs/Tasks Queue status / execution details 
 * According to business logic, a Job can contain:
 * 1) other Jobs (including their own Tasks)
 * 2) Tasks
 * 
 * Consumer can run both a Job and a Task independently if needed.
 * 
 * @author Denys K.
 *
 */

@Repository
public class JobsTasksLogicComposer implements JobsTasksLogic{

	private DataSource dataSource;
	private JobRepository jobRepository;
	private DbConnectionRepository dbConnectionRepository;
	private JdbcTemplate jdbcTemplate;
	private ExecutionOutputRepository executionOutputRepository;
	private DqConfigRepository dqConfigRepository;
	
	@Autowired
	public JobsTasksLogicComposer(DataSource dataSource, 
		    JobRepository jobRepository,
		    ExecutionOutputRepository executionOutputRepository,
		    DbConnectionRepository dbConnectionRepository,
		    DqConfigRepository dqConfigRepository
			) {
		
		this.dataSource = dataSource;
		this.jobRepository = jobRepository;
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	    this.dbConnectionRepository = dbConnectionRepository;
	    this.executionOutputRepository = executionOutputRepository;
	    this.dqConfigRepository = dqConfigRepository;
	}


	/**
	 * Method retrieves whole Job by id and its dependent Jobs and Tasks
	 */
	private Job getJobInternalLogic(Job k) {
		Job job = new Job();
		job = jobRepository.findById(k.getId());
		job.setHostJobId(k.getHostJobId());
		job.setPriorityInRelatedJob(k.getPriorityInRelatedJob());
		return job;
	}

	/**
	 * Get all relations between jobs from job2job table.
	 * If a Job has a related Job, job2job table should contains this record as a pair "job_id - related_job_id".
	 * Jobs which have no entries in job2job table as job_id, considered  as jobs with no dependencies.
	 * @return
	 */
	private List<Job> getAllRelatedJobsList () {
	    // get all related jobs to this job
	    String jobQuery = 
	        "SELECT DISTINCT "
	        + "jobs.*, job2job_link.job_id as host_job_id, job2job_link.related_job_priority  "
	        + "FROM jobs, job2job_link "
	        + "WHERE jobs.id IN ("
	        + "		SELECT " 
	        + "		job2job_link.related_job_id "
	        + "		FROM jobs, job2job_link "
	        + "		WHERE jobs.id = job2job_link.job_id) "
	        + "AND jobs.id = job2job_link.related_job_id ";
	  
	    List<Job> relatedJobsList = this.jdbcTemplate.query(jobQuery, new JdbcRelatedJobRowMapper());
		return relatedJobsList;
		}


	@Override
	public List<Job> getJobsQueue(int jobId) {
		List<Job> allRelatedJobsList = new ArrayList<>();
		allRelatedJobsList = this.getAllRelatedJobsList();
		Job hostJob = new Job();
		hostJob.setId(jobId);
		hostJob = this.getJobInternalLogic(hostJob);

		List<Job> jobsQueue = new LinkedList<>();
		Job latestJob = new Job();
		jobsQueue.add(hostJob);

		// Fill list with a queue of jobs.
		for (int i=0; i<allRelatedJobsList.size(); i++) {
			latestJob = jobsQueue.get(jobsQueue.size() - 1);
			for (Job k : allRelatedJobsList) {
				if (latestJob.getId().equals(k.getHostJobId())) {
					jobsQueue.add(getJobInternalLogic(k));
				}
			}
		}
		
	
		// sort Jobs related to the same host Job by priorities 
		// see: org.dbadmin.model.Job.compareTo(Job)
		Map<Integer, List<Job>> jobMap = new LinkedHashMap<>();
		List<Job> list = null;
		for (Job job : jobsQueue) {
			if (!jobMap.containsKey(job.getHostJobId())) {
				list = new LinkedList<>();

			} 				
			list.add(job);
			Collections.sort(list);
			jobMap.put(job.getHostJobId(), list);
		}

		jobsQueue.clear();

		// iterating the Map to recreate final sorted list of Jobs, with still keeps initial logical order		
		for (Map.Entry<Integer, List<Job>> pair : jobMap.entrySet()) {
			jobsQueue.addAll(pair.getValue());
		}
		return jobsQueue;
	}
	
	
	@Override
	public List<Task> getTasksQueue(int jobId) {
		List<Task> tasksQueue = new LinkedList<>();
		List<Job> jobsLink = this.getJobsQueue(jobId);
		List<Task> tasksTemp = new ArrayList<>();
		for (Job job : jobsLink) {
			tasksTemp = job.getTasklist();
			Collections.sort(tasksTemp); //see: org.dbadmin.model.Task.compareTo(Job)
			tasksQueue.addAll(tasksTemp);
		}
		return tasksQueue;	
	}
	
	@Override
	public void addRelatedJob2Job(int jobId, int relatedJobId, int relatedJobPriority) throws RuntimeException {
		String query = "INSERT INTO job2job_link (job_id, related_job_id, related_job_priority) VALUES (?, ?, ?)";
			try (Connection conn = dataSource.getConnection();
				 PreparedStatement ps = conn.prepareStatement(query)) 
			{
				ps.setInt(1, jobId);
				ps.setInt(2, relatedJobId);
				ps.setInt(3, relatedJobPriority);				
				ps.executeUpdate();
				ps.close();
				conn.close(); //will return connection to pool

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	
	@Override
	public void addRelatedTask2Job(int jobId, int relatedTaskId, int relatedTaskPriority, int taskStartConditionId) {
		String query = "INSERT INTO task2job_link (job_id, related_task_id, related_task_priority, task_start_condition_id) VALUES (?, ?, ?, ?)";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(query)) 
		{
			ps.setInt(1, jobId);
			ps.setInt(2, relatedTaskId);
			ps.setInt(3, relatedTaskPriority);	
			ps.setInt(4, taskStartConditionId);				
			ps.executeUpdate();
			ps.close();
			conn.close(); //will return connection to pool

		} catch (SQLException e) { 
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public void deleteRelatedJob(int jobId, int relatedJobId) throws RuntimeException {
		String delete = "DELETE FROM job2job_link WHERE job_id =? AND related_job_id = ?";
			try (Connection conn = dataSource.getConnection();
				 PreparedStatement ps = conn.prepareStatement(delete)) 
			{
				ps.setInt(1, jobId);				
				ps.setInt(2, relatedJobId);
				ps.executeUpdate();
				ps.close();
				conn.close(); //will return connection to pool

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

	@Override
	public void deleteRelatedTask(int jobId, int relatedTaskId) throws RuntimeException {
		String delete = "DELETE FROM task2job_link WHERE job_id =? AND related_task_id = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(delete)) 
		{
			ps.setInt(1, jobId);				
			ps.setInt(2, relatedTaskId);
			ps.executeUpdate();
			ps.close();
			conn.close(); //will return connection to pool

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addRelatedBusinessrule2Task(int taskId, int relatedBusinessruleId, int relatedBusinessrulePriority)
			throws RuntimeException {
		String query = "INSERT INTO brule2task_link (task_id, related_brule_id, related_brule_priority) VALUES (?, ?, ?)";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(query)) 
		{
			ps.setInt(1, taskId);
			ps.setInt(2, relatedBusinessruleId);
			ps.setInt(3, relatedBusinessrulePriority);	
			ps.executeUpdate();
			ps.close();
			conn.close(); //will return connection to pool

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	@Override
	public void deleteRelatedBusinessrule(int taskId, int relatedBusinessruleId) throws RuntimeException {
		String delete = "DELETE FROM brule2task_link WHERE task_id =? AND related_brule_id = ?";
		try (Connection conn = dataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(delete)) 
		{
			ps.setInt(1, taskId);				
			ps.setInt(2, relatedBusinessruleId);
			ps.executeUpdate();
			ps.close();
			conn.close(); //will return connection to pool

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
		
	@Override
	public List<ExecutionOutput> runJob (int jobId) {
		List<ExecutionOutput> eo = new LinkedList<>();
		List<Task> taskList = new LinkedList<>();
		for (Job job : getJobsQueue(jobId)) {
			taskList = this.getTasksQueue(job.getId());
			for (Task task : taskList) {
				switch (task.getType()) {
				
				case "DQ": {
					eo.addAll(this.runDqTask(task, job));
				}
				case "XX": {//here to add handling for other types
					}
				}
			}
		}
		return eo;
	}

	/**
	 * This is main method to run the Task associated with the Job
	 * @param task
	 * @param job
	 * @return List<ExecutionOutput>
	 */
	private List<ExecutionOutput> runDqTask(Task task, Job job) {
		List<ExecutionOutput> leo = new ArrayList<>();

		int connId = task.getSourceDbConnId();

		try {
			Connection conn;
			DbConnection dbConnection = dbConnectionRepository.findById(connId);
			conn = dbConnectionRepository.testAndGetConnection(dbConnection);
			List<Businessrule> brList = new LinkedList<>();
			brList.addAll(task.getBusinessruleList());
			String sourceTableName;
			String sourceColumnName;

			for (Businessrule br : brList) {
				sourceTableName = br.getSourceTableName();
				sourceColumnName = br.getSourceColumnName();
				if (conn != null) {
					DQConfig dqConfig = new DQConfig();
					int configId = br.getConfigId();
					if (configId > 0)	{// businessrule has a Config
							dqConfig = dqConfigRepository.findById(configId);
						}
					dqConfig.setConnection(conn);
					dqConfig.setTableName(sourceTableName);
					dqConfig.setColumnName(sourceColumnName);		
					
					Report report = DQExecutor.run(dqConfig);					
					
					ExecutionOutput eox = new ExecutionOutput();
					eox = ReportToExecutionOutputAdapter.convert(report);
					eox.setJobId(job.getId());
					eox.setTaskId(task.getId());
					eox.setBusinessruleId(br.getId());
					eox.setComments(job.getName()+": "+task.getName()+": "+br.getName()+" on table "+sourceTableName+", column "+sourceColumnName);
					leo.add(eox);
				} else  {
					throw new IllegalArgumentException("Lack of mandatory parameters (connection/sourceTableName/sourceColumnName) for Businessrule id ="+br.getId());
				}
			}
		} catch (Exception e) {throw new RuntimeException(e); } //populate it to gui
		try {
			for (ExecutionOutput eo : leo) {
				executionOutputRepository.save(eo);
			}
			
		} catch (Exception e) {
			System.err.println("Cannot save Execution Output for Task id="+task.getId());
			throw new RuntimeException(e);
		}
		
		return leo;
	}
}
