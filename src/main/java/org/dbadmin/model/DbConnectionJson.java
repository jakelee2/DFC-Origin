package org.dbadmin.model;

/**
 * This class contains short list of mandatory connection properties for REST JSON request.
 */
public class DbConnectionJson {

  private String connectionName = "";
  private String databaseJdbcDriver = "";
  private String databaseJdbcUrl = "";
  private String databasePasswd = "";
  private String databaseUser = "";
  
  public String getConnectionName() {
    return connectionName;
  }

  public void setConnectionName(String connectionName) {
    this.connectionName = connectionName;
  }

  public String getDatabaseJdbcDriver() {
    return databaseJdbcDriver;
  }

  public void setDatabaseJdbcDriver(String databaseJdbcDriver) {
    this.databaseJdbcDriver = databaseJdbcDriver;
  }

  public String getDatabaseJdbcUrl() {
    return databaseJdbcUrl;
  }

  public void setDatabaseJdbcUrl(String databaseJdbcUrl) {
    this.databaseJdbcUrl = databaseJdbcUrl;
  }

  public String getDatabasePasswd() {
    return databasePasswd;
  }

  public void setDatabasePasswd(String databasePasswd) {
    this.databasePasswd = databasePasswd;
  }

  public String getDatabaseUser() {
    return databaseUser;
  }

  public void setDatabaseUser(String databaseUser) {
    this.databaseUser = databaseUser;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
      sb.append("connectionName "+ this.getConnectionName())
        .append(",databaseJdbcUrl "+ this.getDatabaseJdbcUrl())
        .append(",databaseJdbcDriver "+ this.getDatabaseJdbcDriver())        
        .append(",databasePasswd "+ this.getDatabasePasswd())
        .append(",databaseUser "+ this.getDatabaseUser());
      return sb.toString();
  }

}