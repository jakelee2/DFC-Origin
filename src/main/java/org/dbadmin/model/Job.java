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
package org.dbadmin.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.core.style.ToStringCreator;

/**
 * Domain object representing an Job.
 *
 */
public class Job extends BaseEntity implements Comparable<Job> {

  private String name;
  private String description;
  private int statusId;
  private String status ="";

  /**
   * PriorityInRelatedJob = -1 is default value.
   * This value means the Job has no dependency from other Jobs therefore can be executed in parallel stream/thread.
   * Otherwise if this value is not equal to -1, this means the Job has to be executed in order
   * of its priority inside "host" Job (lower priority number means higher priority to execute).
   */
  private int priorityInRelatedJob = -1;

  /**
   * Used if this Job involved in jobs queue as dependent job. -1 means there is no host job.
   */
  private int hostJobId = -1;

  private int level_id;

  // Key = priority of a Job / Task inside this Job in asc order
  private Map<Integer, Task> tasks = new TreeMap<>();
  private Map<Integer, Job> jobs = new TreeMap<>();

  private List<Task> tasklist = new ArrayList<>();
  private List<Job>  joblist = new ArrayList<>();



  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public synchronized String getStatus() {
    return status;
  }

  private synchronized void setStatus(int statusId) {
    switch (statusId) {
      case 1: this.status = ExecutionStatus.CREATED.toString(); break;
      case 5: this.status = ExecutionStatus.RUNNING.toString(); break;
      case 10: this.status = ExecutionStatus.WAITING.toString(); break;
      case 20: this.status = ExecutionStatus.FAILED.toString(); break;
      case 100: this.status = ExecutionStatus.SUCCESS.toString(); break;
      default: this.status = ExecutionStatus.NA.toString(); break;
    }

  }

  public int getLevelId() {
    return level_id;
  }

  public void setLevelId(int level_id) {
    this.level_id = level_id;
  }

  public void setTasks(Map<Integer, Task> tasks) {
    this.tasks = tasks;
    this.tasklist = this.getTasks();
  }

  public void setJobs(Map<Integer, Job> jobs) {
    this.jobs = jobs;
    this.joblist = this.getJobs();
  }


  /**
   * Add Task to the Job, set Priority of the Task inside this Job
   * @param priority - Task priority inside this Job
   * @param task
   */
  public void addRelatedTask(int priority, Task task) {
	  while (true) {
		  if (!this.tasks.containsKey(priority)) {
			  this.tasks.put(priority, task);
			  break;
		  }
		  else priority++; //will be inserted in the next free place
	  }
  }

  /**
   * Delete (all) Related Task(s) by Task id
   * @param jobId
   */
  public void deleteRelatedTasks(int taskId) {
	  this.tasks.entrySet().removeIf(e-> e.getValue().id==taskId);
  }

  public void addRelatedJob(int priority, Job job) {
		  while (true) {
			  if (!this.jobs.containsKey(priority)) {
				  this.jobs.put(priority, job);
				  break;
			  }
			  else priority++; //will be inserted in the next free place
		  }
	  }

  public void changeJobPriority(int oldPriority, int newPriority, int jobId) {
	  //TODO if needed
  }

  /**
   * Delete (all) Related Job(s) by Job id
   * @param jobId
   */
  public void deleteRelatedJobs(int jobId) {
	  this.jobs.entrySet().removeIf(e-> e.getValue().id==jobId);
  }

  /**
   * One Job can be used many times inside the host Job therefore has many priorities
   * @param jobId
   * @return Set of priorities
   */
  public Set<Integer> getJobPriorities(int jobId) {
	Set<Integer> priority = new HashSet<>();
	this.jobs.forEach((k,v) -> {if (v.id==jobId)  priority.add(k);});
	return priority;

  }

  /**
   * One Task can be used many times inside the host Job therefore has many priorities
   * @param taskId
   * @return Set of priorities
   */
  public Set<Integer> getTaskPriorities(int taskId) {
	Set<Integer> priority = new HashSet<>();
	this.tasks.forEach((k,v) -> {if (v.id==taskId)  priority.add(k);});
	return priority;

  }

  public Map<Integer, Task> getAssociatedTasksAndPriorities() {
    return Collections.unmodifiableMap(tasks);
  }

  public List<Task> getTasks() {
    this.tasklist = new ArrayList<Task>(tasks.values());
    return this.tasklist;
  }

  public List<Job> getJobs() {
    this.joblist = new ArrayList<Job>(jobs.values());;
    return this.joblist;
  }


  public Map<Integer, Job> getAssociatedJobsAndPriorities() {
    return Collections.unmodifiableMap(jobs);
  }

  public int getPriorityInRelatedJob() {
    return priorityInRelatedJob;
  }

  public void setPriorityInRelatedJob(int priorityInRelatedJob) {
    this.priorityInRelatedJob = priorityInRelatedJob;
  }



  public List<Task> getTasklist() {
    return getTasks();
  }

  public void setTasklist(List<Task> tasklist) {
    this.tasklist = tasklist;
  }

  public List<Job> getJoblist() {
    return getJobs();
  }

  public void setJoblist(List<Job> joblist) {
    this.joblist = joblist;
  }

  public synchronized int getStatusId() {
    return statusId;
  }

  public synchronized void setStatusId(int statusId) {
    this.statusId = statusId;
    setStatus(statusId);
  }

public int getHostJobId() {
	return hostJobId;
}

public void setHostJobId(int hostJobId) {
	this.hostJobId = hostJobId;
}


@Override
public String toString() {
  return new ToStringCreator(this)

      .append("id", this.getId())
      .append("hostJobId", this.getHostJobId())
      .append("priorityInRelatedJob", this.getPriorityInRelatedJob())
      .append("new", this.isNew())
      .append("name", this.getName())
      .append("description", this.getDescription())
      .append("status", this.getStatus())
      .toString();
}

@Override
public int compareTo(Job compareInstance) {
	int priority = compareInstance.getPriorityInRelatedJob();
	// return this.getPriorityInRelatedJob() - priority; // could be overflow as we have no limitation for priority numbers
	return Integer.compare(this.getPriorityInRelatedJob(), priority);
}



}
