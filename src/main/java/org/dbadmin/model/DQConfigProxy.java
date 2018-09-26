package org.dbadmin.model;

/**
 * class -helper for conversion output object to DQConfig
 * @author D.K.
 *
 */
public class DQConfigProxy extends BaseEntity {
	private int parametersMapId;
	private String dqRule;
	private String comments;
	
	public int getParametersMapId() {
		return parametersMapId;
	}
	public void setParametersMapId(int parametersMapId) {
		this.parametersMapId = parametersMapId;
	}
	public String getDqRule() {
		return dqRule;
	}
	public void setDqRule(String dqRule) {
		this.dqRule = dqRule;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

}