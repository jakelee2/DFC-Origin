package org.dbadmin.model;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ValidationInfo {
  private String status;
  private String sourceTable;
  private String targetTable;
  private String username;
  private String password;
  private String ip;
  private String database;
  private String ingestJobName;
  private String columnName;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getIngestJobName() {
    return ingestJobName;
  }

  public void setIngestJobName(String ingestJobName) {
    this.ingestJobName = ingestJobName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  public ValidationInfo() {}

  public ValidationInfo(Job job) {
    Task task = job.getTasks().size() > 0 ? job.getTasks().get(0) : null;
    JSONParser parser = new JSONParser();

    try {
      Object obj = parser.parse(task.getBody());
      JSONObject jsonObject = (JSONObject) obj;
      password = (String) jsonObject.get("password");
      sourceTable = (String) jsonObject.get("sourceTable");
      targetTable = (String) jsonObject.get("targetTable");
      username = (String) jsonObject.get("username");
      ingestJobName = (String) jsonObject.get("ingestJobName");
      database = (String) jsonObject.get("database");
      ip = (String) jsonObject.get("ip");
    } catch (ParseException e) {
      e.printStackTrace();
    }

  }

  public String getSourceTable() {
    return sourceTable;
  }

  public void setSourceTable(String sourceTable) {
    this.sourceTable = sourceTable;
  }

  public String getTargetTable() {
    return targetTable;
  }

  public void setTargetTable(String targetTable) {
    this.targetTable = targetTable;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String toJson() {
    return "{" + "\"password\":\"" + password + "\"" + ",\"sourceTable\":\"" + sourceTable + "\""
        + ",\"targetTable\":\"" + targetTable + "\"" + ",\"username\":\"" + username + "\""
        + ",\"ingestJobName\":\"" + ingestJobName + "\"" + ",\"ip\":\"" + ip + "\""
        + ",\"database\":\"" + database + "\"" + "}";
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
}
