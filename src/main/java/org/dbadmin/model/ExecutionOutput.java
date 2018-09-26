package org.dbadmin.model;

import java.sql.Timestamp;

public class ExecutionOutput extends BaseEntity{

	private int jobId;
	private int taskId;	
	private int businessruleId;
    private String textOutput;
    private String listOutput;
	private String error;
    private OutputStatus outputStatus;
	private String comments;
    private Timestamp completeTimestamp;
    private String dateForTimestamp;

   
	public int getBusinessruleId() {
		return businessruleId;
	}
	public void setBusinessruleId(int businessruleId) {
		this.businessruleId = businessruleId;
	}
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public OutputStatus getOutputStatus() {
		return outputStatus;
	}
	public void setOutputStatus(OutputStatus outputStatus) {
		this.outputStatus = outputStatus;
	}

	public Timestamp getCompleteTimestamp() {
		return completeTimestamp;
	}
	public void setCompleteTimestamp(Timestamp completeTimestamp) {
		this.completeTimestamp = completeTimestamp;
	}
    
    public String getTextOutput() {
		return textOutput;
	}
	public void setTextOutput(String textOutput) {
		this.textOutput = textOutput;
	}
	public int getJobId() {
		return jobId;
	}
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
    public String getListOutput() {
		return listOutput;
	}
	public void setListOutput(String listOutput) {
		this.listOutput = listOutput;
	}
  public String getDateForTimestamp() {
    return dateForTimestamp;
  }
  public void setDateForTimestamp(String dateForTimestamp) {
    this.dateForTimestamp = dateForTimestamp;
  }
 
}
