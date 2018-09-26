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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.style.ToStringCreator;

/**
 * Domain object representing an ETL Task.
 *
 */
public class Task extends BaseEntity implements Comparable<Task>{
  
  private String name;

  private String description;

  private String body;
  
  private String type;

  private int typeId;
  
  private int sourceDbConnId;
  
  private int targetDbConnId;
  
  // Conn. names used for GUI representation
  private String sourceDbConnName;
  
  private String targetDbConnName;
  // end
  
  private int statusId;
  
  private String status ="";
  
  private int startConditionId; // same values, as statusId
  
  private String startCondition="";  
  
  private int hostJobId;
  
  /**
   * PriorityInRelatedJob = -1 is default value. 
   * This value means the Task has no dependency from other Jobs therefore can be executed in parallel stream/thread.
   * Otherwise if this value is not equal to -1, this means the Task has to be executed in logical order 
   * of its priority inside "host" Job.
   */
  private int priorityInRelatedJob = -1; 
  
  // Key = priority of a Businessrule inside this Task in asc order
  private Map<Integer, Businessrule> businessrules = new TreeMap<>();

  private List<Businessrule> businessruleList = new ArrayList<>();
  
  // List of associated Jobs
  private List<Job> associatedJobsList = new ArrayList<>();
  
  private ExecutionOutput executionOutput;
  


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

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public int getTypeId() {
    return typeId;
  }


  public void setTypeId(int type) {
    this.typeId = type;
    this.setType(type);
  }


  public int getSourceDbConnId() {
    return sourceDbConnId;
  }


  public void setSourceDbConnId(int sourceDbConnectionId) {
    this.sourceDbConnId = sourceDbConnectionId;
  }


  public int getTargetDbConnId() {
    return targetDbConnId;
  }


  public void setTargetDbConnId(int targetDbConnectionId) {
    this.targetDbConnId = targetDbConnectionId;
  }

// Status methods
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
  
  public synchronized int getStatusId() {
	    return statusId;
	  }

  public synchronized void setStatusId(int statusId) {
	    this.statusId = statusId;
	    setStatus(statusId);
	  }
  // end
  
  // StartCondition  methods
  
  public String getStartCondition() {
	  return startCondition;
  }

  /**
   * Conditional start of the Task, e.g.:
   * start on previous Task fail, start on previous Task success etc.
   * Status NA means there is no condition for the Task start, except normal tasks flow priorities.
   * @param startConditionId
   */
  private void setStartCondition(int startConditionId) {
	    switch (startConditionId) {
	      case 20: this.startCondition = ExecutionStatus.FAILED.toString(); break;
	      case 100: this.startCondition = ExecutionStatus.SUCCESS.toString(); break;
	      default: this.startCondition = ExecutionStatus.NA.toString(); break;
	    }
	  }
  
  public int getStartConditionId() {
	  return startConditionId;
  }
  
  public void setStartConditionId(int startConditionId) {
	    this.startConditionId = startConditionId;
	    setStartCondition(startConditionId);
	  }
  
  //end StartCondition  methods
  
  public List<Businessrule> getBusinessruleList() {
  return this.businessruleList;
  }
  
  
  public void addBusinessrule(Integer priority, Businessrule brule) {
	  while (true) {
		  if (!this.businessrules.containsKey(priority)) {
			  this.businessrules.put(priority, brule);
			  break;
		  }
		  else priority++; //will be inserted in the next free place
	  }
  }
  

  public Map<Integer, Businessrule> getAssociatedBusinessrulesAndPriorities() {
    return Collections.unmodifiableMap(businessrules);
  }
  
  public int getParentJobId() {
    return hostJobId;
  }

  public void setParentJobId(int relatedJobId) {
    this.hostJobId = relatedJobId;
  }

  public int getPriorityInRelatedJob() {
    return priorityInRelatedJob;
  }

  public void setPriorityInRelatedJob(int priorityInRelatedJob) {
    this.priorityInRelatedJob = priorityInRelatedJob;
  }
  
  public int getHostJobId() {
    return hostJobId;
  }

  public void setHostJobId(int patentJobId) {
    this.hostJobId = patentJobId;
  }

  public void setBusinessrules(Map<Integer, Businessrule> businessrules) {
    this.businessrules = businessrules;
    this.businessruleList = new ArrayList<Businessrule>(businessrules.values());
  }
  
  public void setBusinessruleList(List<Businessrule> businessrules) {
    this.businessruleList = businessrules;
  }

  public List<Job> getAssociatedJobsList() {
    return associatedJobsList;
  }

  public void setAssociatedJobsList(List<Job> associatedJobsList) {
    this.associatedJobsList = associatedJobsList;
  }

  public Map<Integer, Businessrule> getBusinessrules() {
    return businessrules;
  }  
  
  public String getType() {
    return type;
  }

  public void setType(int typeId) {
    switch (typeId) {
      case 1: this.type = Type.SQL.toString(); break;
      case 2: this.type = Type.PYTHON.toString(); break;
      case 3: this.type = Type.INGESTION.toString(); break;
      case 10: this.type = Type.DQ.toString(); break;
      case 100: this.type = Type.OTHER.toString(); break;
      default: this.type = Type.NA.toString(); break;
    }
  }



  public String getSourceDbConnName() {
    return sourceDbConnName;
  }

  public void setSourceDbConnName(String sourceDbConnName) {
    this.sourceDbConnName = sourceDbConnName;
  }

  public String getTargetDbConnName() {
    return targetDbConnName;
  }

  public void setTargetDbConnName(String targetDbConnName) {
    this.targetDbConnName = targetDbConnName;
  }
  
  public ExecutionOutput getExecutionOutput() {
	return executionOutput;
  }

  public void setExecutionOutput(ExecutionOutput executionOutput) {
	this.executionOutput = executionOutput;
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
        .append("startCondition", this.getStartCondition())
        .toString();
  }
  
  @Override
  public int compareTo(Task compareInstance) {
  	int priority = compareInstance.getPriorityInRelatedJob();
  	// return this.getPriorityInRelatedJob() - priority; // could be overflow as we have no limitation for priority numbers
  	return Integer.compare(this.getPriorityInRelatedJob(), priority);
  }


}
