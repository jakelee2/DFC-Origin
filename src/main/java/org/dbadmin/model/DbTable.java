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

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.style.ToStringCreator;

/**
 * Domain object representing an DbTable. No JPA properties are here. No
 * database table for this entity, only "on-fly" loading.
 * 
 * TODO 8/10/2016 -add persistence as we need to save Businessrules which is table-dependable.
 *
 */
public class DbTable extends BaseEntity {

	private String name;
	private Integer rowsNumber;
	private Integer columnNumber; 
	private String primaryKey;
	private String dbConnectionURL; // for GUI
	private Set<DbColumn> dbcolumns;
	private String databaseJpaVendorName;
	private DbConnection dbConnection;
	private int dbconnectionId;

	private String comments;

	public DbConnection getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(DbConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<DbColumn> getDbcolumns() {
		if (this.dbcolumns == null) {
			this.dbcolumns = new HashSet<>();
		}
		return this.dbcolumns;
	}

	public void setDbcolumns(Set<DbColumn> dbcolumns) {
		this.dbcolumns = dbcolumns;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Integer getRowsNumber() {
		return rowsNumber;
	}

	public void setRowsNumber(Integer rowsNumber) {
		this.rowsNumber = rowsNumber;
	}

	public Integer getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String getDbConnectionURL() {
		return dbConnectionURL;
	}

	public void setDbConnectionURL(String dbConnectionURL) {
		this.dbConnectionURL = dbConnectionURL;
	}

	public String getDatabaseJpaVendorName() {
		return databaseJpaVendorName;
	}

	public void setDatabaseJpaVendorName(String databaseJpaVendorName) {
		this.databaseJpaVendorName = databaseJpaVendorName;
	}
	
	public int getDbconnectionId() {
		return dbconnectionId;
	}

	public void setDbconnectionId(int dbConnectionId) {
		this.dbconnectionId = dbConnectionId;
	}

	public String getComment() {
		return comments;
	}

	public void setComments(String comment) {
		this.comments = comment;
	}


	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId()).append("new", this.isNew())
				.append("name", this.getName()).append("rowsNumber", this.getRowsNumber())
				.append("columnNumber", this.getColumnNumber()).append("primaryKey", this.getPrimaryKey())
				.append("dbConnectionURL", this.getDbConnectionURL())
				.append("dbConnectionId", this.getDbconnectionId())
				.append("databaseJpaVendorName", this.getDatabaseJpaVendorName()).toString();
	}

}
