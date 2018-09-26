package org.dbadmin.model;

import org.springframework.core.style.ToStringCreator;

/**
 * Created by Jake Lee - 8/17/16.
 * 
 * This class is equivalent to the users_jobs table in db.
 */
public class UserJob extends BaseEntity {

  private Integer userId;
  private Integer jobId;
  private boolean active;
  

  public Integer getJobId() {
    return jobId;
  }

  public void setJobId(Integer jobId) {
    this.jobId = jobId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)
        .append("new", this.isNew())
        .append("jobId", this.getJobId())
        .append("userId", this.getUserId())
        .append("active", this.isActive())
        .toString();
  }
}
