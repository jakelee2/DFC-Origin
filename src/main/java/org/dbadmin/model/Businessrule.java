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
import java.util.List;

import org.springframework.core.style.ToStringCreator;

/**
 * Domain object representing an Businessrule container.
 *
 */
public class Businessrule extends BaseEntity implements Comparable<Businessrule> {

	private String name;
	private String description;
	private String body;
	private String restconnection;
	private int priorityInHostTask = -1;
	private List<Task> associatedTasks = new ArrayList<>();
	private int sourceTableId = -1;
	private int targetTableId = -1;
	private int sourceTableColumnId= -1;
	private int targetTableColumnId= -1;
	private int sourceDbconnectionId = -1;
	private int targetDbconnectionId = -1;	
	private String sourceTableName;
	private String targetTableName;	
	private String sourceColumnName;
	private String targetColumnName;		
	private ExecutionOutput executionOutput;
	private int configId;
	
	
	public int getPriorityInHostTask() {
		return priorityInHostTask;
	}

	public void setPriorityInHostTask(int priorityInHostTask) {
		this.priorityInHostTask = priorityInHostTask;
	}

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

	public String getRestconnection() {
		return restconnection;
	}

	public void setRestconnection(String restconnection) {
		this.restconnection = restconnection;
	}

	public List<Task> getAssociatedTasklist() {
		return associatedTasks;
	}

	public void setAssociatedTasklist(List<Task> associatedTasklist) {
		this.associatedTasks = associatedTasklist;
	}
	
	public ExecutionOutput getExecutionOutput() {
		return executionOutput;
	}

	public void setExecutionOutput(ExecutionOutput executionOutput) {
		this.executionOutput = executionOutput;
	}

	public int getSourceTableId() {
		return sourceTableId;
	}

	public void setSourceTableId(int sourceTableId) {
		this.sourceTableId = sourceTableId;
	}

	public int getTargetTableId() {
		return targetTableId;
	}

	public void setTargetTableId(int targetTableId) {
		this.targetTableId = targetTableId;
	}

	public int getSourceTableColumnId() {
		return sourceTableColumnId;
	}

	public void setSourceTableColumnId(int sourceTableColumnId) {
		this.sourceTableColumnId = sourceTableColumnId;
	}

	public int getTargetTableColumnId() {
		return targetTableColumnId;
	}

	public void setTargetTableColumnId(int targetTableColumnId) {
		this.targetTableColumnId = targetTableColumnId;
	}
	
	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", this.getId())
				.append("new", this.isNew())
				.append("name", this.name)
				.append("description", this.description)
				.append("body", this.body)
				.append("restconnection", this.restconnection)
				.append("priorityInHostTask", this.priorityInHostTask)
				.toString();
	}

	public int getSourceDbconnectionId() {
		return sourceDbconnectionId;
	}

	public void setSourceDbconnectionId(int sourceDbconnectionId) {
		this.sourceDbconnectionId = sourceDbconnectionId;
	}

	public int getTargetDbconnectionId() {
		return targetDbconnectionId;
	}

	public void setTargetDbconnectionId(int targetDbconnectionId) {
		this.targetDbconnectionId = targetDbconnectionId;
	}

	public String getSourceTableName() {
		return sourceTableName;
	}

	public void setSourceTableName(String sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public String getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(String targetTableName) {
		this.targetTableName = targetTableName;
	}

	public String getSourceColumnName() {
		return sourceColumnName;
	}

	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}

	public String getTargetColumnName() {
		return targetColumnName;
	}

	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
	}	
	
	@Override
	public int compareTo(Businessrule compareInstance) {
		int priority = compareInstance.getPriorityInHostTask();
		return Integer.compare(this.getPriorityInHostTask(), priority);
	}

	public void setConfigId(int id) {
		this.configId = id;
		
	}

	public int getConfigId() {
		return configId;
	}

}
