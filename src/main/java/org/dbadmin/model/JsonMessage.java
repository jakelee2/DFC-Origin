package org.dbadmin.model;

import java.io.Serializable;

public class JsonMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	public enum Status {SUCCESS, FAIL, ERROR};
	
	private Status status;
    private String message;	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
}
