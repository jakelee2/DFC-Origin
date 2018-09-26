package org.dbadmin.model;

/**
 * This class contains general connection properties, some of them can be empty depending on
 * particular configuration and database in use.
 * 
 * No JPA properties are here.
 */
public class DbConnection extends BaseEntity {

  private String connectionName = "";
  private String databaseJdbcDriver = "";
  private String databaseJdbcUrl = "";
  private String databaseTablePattern = "";
  private String databasePasswd = "";
  private String databaseCatalog = "";
  private String databaseUser = "";
  private String databaseSchemaPattern = "";
  private String databaseType = "";
  private String databaseProtocol = "";
  private String databaseTableType = "";
  private String databaseColumnPattern = "";
  private String databaseJpaVendorName = "";
  private String dataSourceClass = "";
  private String status = "";

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

  public String getDatabaseTablePattern() {
    return databaseTablePattern;
  }

  public void setDatabaseTablePattern(String databaseTablepattern) {
    this.databaseTablePattern = databaseTablepattern;
  }

  public String getDatabasePasswd() {
    return databasePasswd;
  }

  public void setDatabasePasswd(String databasePasswd) {
    this.databasePasswd = databasePasswd;
  }

  public String getDatabaseCatalog() {
    return databaseCatalog;
  }

  public void setDatabaseCatalog(String databaseCatalog) {
    this.databaseCatalog = databaseCatalog;
  }

  public String getDatabaseUser() {
    return databaseUser;
  }

  public void setDatabaseUser(String databaseUser) {
    this.databaseUser = databaseUser;
  }

  public String getDatabaseSchemaPattern() {
    return databaseSchemaPattern;
  }

  public void setDatabaseSchemaPattern(String databaseSchemapattern) {
    this.databaseSchemaPattern = databaseSchemapattern;
  }

  public String getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(String databaseType) {
    this.databaseType = databaseType;
  }

  public String getDatabaseProtocol() {
    return databaseProtocol;
  }

  public void setDatabaseProtocol(String databaseProtocol) {
    this.databaseProtocol = databaseProtocol;
  }

  public String getDatabaseTableType() {
    return databaseTableType;
  }

  public void setDatabaseTableType(String databaseTabletype) {
    this.databaseTableType = databaseTabletype;
  }

  public String getDatabaseColumnPattern() {
    return databaseColumnPattern;
  }

  public void setDatabaseColumnPattern(String databaseColumnpattern) {
    this.databaseColumnPattern = databaseColumnpattern;
  }

  public String getDatabaseJpaVendorName() {
    return databaseJpaVendorName;
  }

  public void setDatabaseJpaVendorName(String databaseJpaVendorName) {
    this.databaseJpaVendorName = databaseJpaVendorName;
  }

  public String getDataSourceClass() {
    return dataSourceClass;
  }

  public void setDataSourceClass(String dataSourceClass) {
    this.dataSourceClass = dataSourceClass;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  /*
   * It is very important not to use here standard Spring's ToStringCreator class.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("DbConnection: id " + this.getId())
        .append(",connectionName " + this.getConnectionName())
        .append(",databaseJdbcDriver " + this.getDatabaseJdbcDriver())
        .append(",databaseJdbcUrl " + this.getDatabaseJdbcUrl())
        .append(",databaseTablepattern " + this.getDatabaseTablePattern())
        .append(",databasePasswd " + this.getDatabasePasswd())
        .append(",databaseCatalog " + this.getDatabaseCatalog())
        .append(",databaseUser " + this.getDatabaseUser())
        .append(",databaseSchemaPattern " + this.getDatabaseSchemaPattern())
        .append(",databaseType " + this.getDatabaseType())
        .append(",databaseProtocol " + this.getDatabaseProtocol())
        .append(",databaseTableType " + this.getDatabaseTableType())
        .append(",databaseColumnPattern " + this.getDatabaseColumnPattern())
        .append(",databaseJpaVendorName " + this.getDatabaseColumnPattern())
        .append(",dataSourceClass " + this.getDataSourceClass());
    return sb.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((connectionName == null) ? 0 : connectionName.hashCode());
    result = prime * result + ((dataSourceClass == null) ? 0 : dataSourceClass.hashCode());
    result = prime * result + ((databaseCatalog == null) ? 0 : databaseCatalog.hashCode());
    result = prime * result + ((databaseColumnPattern == null) ? 0 : databaseColumnPattern.hashCode());
    result = prime * result + ((databaseJdbcDriver == null) ? 0 : databaseJdbcDriver.hashCode());
    result = prime * result + ((databaseJdbcUrl == null) ? 0 : databaseJdbcUrl.hashCode());
    result = prime * result + ((databaseJpaVendorName == null) ? 0 : databaseJpaVendorName.hashCode());
    result = prime * result + ((databasePasswd == null) ? 0 : databasePasswd.hashCode());
    result = prime * result + ((databaseProtocol == null) ? 0 : databaseProtocol.hashCode());
    result = prime * result + ((databaseSchemaPattern == null) ? 0 : databaseSchemaPattern.hashCode());
    result = prime * result + ((databaseTablePattern == null) ? 0 : databaseTablePattern.hashCode());
    result = prime * result + ((databaseTableType == null) ? 0 : databaseTableType.hashCode());
    result = prime * result + ((databaseType == null) ? 0 : databaseType.hashCode());
    result = prime * result + ((databaseUser == null) ? 0 : databaseUser.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DbConnection other = (DbConnection) obj;
    if (connectionName == null) {
      if (other.connectionName != null)
        return false;
    } else if (!connectionName.equals(other.connectionName))
      return false;
    if (dataSourceClass == null) {
      if (other.dataSourceClass != null)
        return false;
    } else if (!dataSourceClass.equals(other.dataSourceClass))
      return false;
    if (databaseCatalog == null) {
      if (other.databaseCatalog != null)
        return false;
    } else if (!databaseCatalog.equals(other.databaseCatalog))
      return false;
    if (databaseColumnPattern == null) {
      if (other.databaseColumnPattern != null)
        return false;
    } else if (!databaseColumnPattern.equals(other.databaseColumnPattern))
      return false;
    if (databaseJdbcDriver == null) {
      if (other.databaseJdbcDriver != null)
        return false;
    } else if (!databaseJdbcDriver.equals(other.databaseJdbcDriver))
      return false;
    if (databaseJdbcUrl == null) {
      if (other.databaseJdbcUrl != null)
        return false;
    } else if (!databaseJdbcUrl.equals(other.databaseJdbcUrl))
      return false;
    if (databaseJpaVendorName == null) {
      if (other.databaseJpaVendorName != null)
        return false;
    } else if (!databaseJpaVendorName.equals(other.databaseJpaVendorName))
      return false;
    if (databasePasswd == null) {
      if (other.databasePasswd != null)
        return false;
    } else if (!databasePasswd.equals(other.databasePasswd))
      return false;
    if (databaseProtocol == null) {
      if (other.databaseProtocol != null)
        return false;
    } else if (!databaseProtocol.equals(other.databaseProtocol))
      return false;
    if (databaseSchemaPattern == null) {
      if (other.databaseSchemaPattern != null)
        return false;
    } else if (!databaseSchemaPattern.equals(other.databaseSchemaPattern))
      return false;
    if (databaseTablePattern == null) {
      if (other.databaseTablePattern != null)
        return false;
    } else if (!databaseTablePattern.equals(other.databaseTablePattern))
      return false;
    if (databaseTableType == null) {
      if (other.databaseTableType != null)
        return false;
    } else if (!databaseTableType.equals(other.databaseTableType))
      return false;
    if (databaseType == null) {
      if (other.databaseType != null)
        return false;
    } else if (!databaseType.equals(other.databaseType))
      return false;
    if (databaseUser == null) {
      if (other.databaseUser != null)
        return false;
    } else if (!databaseUser.equals(other.databaseUser))
      return false;
    return true;
  }

}
