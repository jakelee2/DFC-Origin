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
package org.dbadmin.repository;

import java.util.List;

import org.dbadmin.model.ExecutionOutput;
import org.dbadmin.model.Job;
import org.dbadmin.model.Task;

public interface JobsTasksLogic {

	/**
	 * Return ordered List of all related Jobs to this Job.jobId
	 * 
	 * @param jobId
	 * @return List<Job>
	 */
	List<Job> getJobsQueue(int jobId);

	
	/**
	 * Return ordered List of all Task starting from this Job.jobId
	 * 
	 * @param jobId
	 * @return List<Task>
	 */
	List<Task> getTasksQueue(int jobId);

	
	/**
	 * Add a Job as dependent(related) to another Job (host Job) and set their
	 * priority in the host Job
	 * 
	 * @param jobId
	 *            - Host Job
	 * @param relatedJobId
	 *            - Dependent Job
	 * @param relatedJobPriority
	 * @throws RuntimeException
	 */
	void addRelatedJob2Job(int jobId, int relatedJobId, int relatedJobPriority) throws RuntimeException;

	/**
	 * 
	 * Add a Task as dependent(related) to another Job (host Job) and set their
	 * priority in the host Job and Start Condition for the Task
	 * @param jobId
	 * @param relatedTaskId
	 * @param relatedTaskPriority
	 * @param taskStartConditionId
	 */
	void addRelatedTask2Job(int jobId, int relatedTaskId, int relatedTaskPriority, int taskStartConditionId);

	/**
	 * Delete related Job from host Job
	 * @param jobId   - host Job
	 * @param relatedJobId - related Job
	 * @throws RuntimeException
	 */
	void deleteRelatedJob(int jobId, int relatedJobId) throws RuntimeException;
	
	/**
	 * Delete related Task from host Job
	 * @param jobId   - host Job
	 * @param relatedTaskId - related Task
	 * @throws RuntimeException
	 */
	void deleteRelatedTask(int jobId, int relatedTaskId) throws RuntimeException;


	void addRelatedBusinessrule2Task(int taskId, int relatedBusinessruleId, int relatedBusinessrulePriority) throws RuntimeException;


	void deleteRelatedBusinessrule(int taskId, int relatedBusinessruleId) throws RuntimeException;


	List<ExecutionOutput> runJob(int jobId);

}
