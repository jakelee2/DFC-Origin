package org.dbadmin.model;

/**
 * class -helper for using with standard Spring mapper
 * @author D.K.
 *
 */
public class DQParams extends BaseEntity {
	
	private int mapId; //for a DQConfig object witch has more then one parameters
	private String mapKey;
	private String mapValue;
	private String comments;
	
	public String getMapKey() {
		return mapKey;
	}
	public void setMapKey(String mapKey) {
		this.mapKey = mapKey;
	}
	public String getMapValue() {
		return mapValue;
	}
	public void setMapValue(String mapValue) {
		this.mapValue = mapValue;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getMapId() {
		return mapId;
	}
	public void setMapId(int mapId) {
		this.mapId = mapId;
	}
}