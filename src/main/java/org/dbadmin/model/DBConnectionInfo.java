package org.dbadmin.model;

/**
 * Temporary class for development stage. Consider use of {@link DBConnection} class which contains
 * additionally general DB connection properties like: database Table Pattern, database Catalog,
 * database Schema Pattern, database Type, database Protocol, database Dsn, database Table Type,
 * database Column Pattern etc.
 *
 */
public class DBConnectionInfo extends BaseEntity {
  private String status;
  private String username;
  private String password;
  private String ip;
  private String database;

  private String connectionName;



  // TODO: add support to other dbs
  private String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  public String getDriver() {
    return driver;
  }

  public void setDriver(String driver) {
    this.driver = driver;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getConnectionName() {
    return connectionName;
  }

  public void setConnectionName(String connectionName) {
    this.connectionName = connectionName;
  }

}
