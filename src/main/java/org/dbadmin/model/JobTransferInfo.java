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

import org.springframework.core.style.ToStringCreator;

/**
 * Domain object representing an Job.
 *
 */
public class JobTransferInfo extends BaseEntity {

  private Integer jobId;
  private Integer userJobId;
  private String jobName;
  private String transferer;
  private String transferee;
  private String description;
  private Integer levelId;
  private String levelName;
  private String lastUpdated;
  
  public Integer getJobId() {
    return jobId;
  }

  public void setJobId(Integer jobId) {
    this.jobId = jobId;
  }

  public Integer getUserJobId() {
    return userJobId;
  }

  public void setUserJobId(Integer userJobId) {
    this.userJobId = userJobId;
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getTransferer() {
    return transferer;
  }

  public void setTransferer(String transferer) {
    this.transferer = transferer;
  }

  public String getTransferee() {
    return transferee;
  }

  public void setTransferee(String transferee) {
    this.transferee = transferee;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getLevelId() {
    return levelId;
  }

  public void setLevelId(Integer levelId) {
    this.levelId = levelId;
  }

  public String getLevelName() {
    return levelName;
  }

  public void setLevelName(String levelName) {
    this.levelName = levelName;
  }
  
  @Override
  public String toString() {
    return new ToStringCreator(this)
        .append("new", this.isNew())
        .append("userJobId", this.getUserJobId())
        .append("jobName", this.getJobName())
        .append("transferer", this.getTransferer())
        .append("transferee", this.getTransferee())
        .toString();
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }
}
