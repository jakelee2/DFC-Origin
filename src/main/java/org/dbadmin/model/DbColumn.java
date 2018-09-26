package org.dbadmin.model;

import org.springframework.core.style.ToStringCreator;

/**
 * @author denys. Domain object representing an DbTable. No JPA properties at the moment.
 *
 */
public class DbColumn extends BaseEntity {

  private String name;

  // SQL type from java.sql.Types
  private Integer sqlType;

  // SQL type from java.sql.Types converted by SQLServerDataTypeJDBCtoString.getDataTypeString into
  // String
  private String sqlTypeString;

  private Integer size;

  // the number of fractional digits.
  // Null is returned for data types where DECIMAL_DIGITS is not applicable.
  private Integer decimalDigit;

  // ISO rules are used to determine the nullability for a column:
  // YES --- if the column can include NULLs
  // NO --- if the column cannot include NULLs
  // empty string --- if the nullability for the column is unknown
  private String isnullable;

  public String getIsnullable() {
    return isnullable;
  }

  public void setIsnullable(String isnullable) {
    this.isnullable = isnullable;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Integer getSqlType() {
    return sqlType;
  }

  public void setSqlType(Integer sqlType) {
    this.sqlType = sqlType;
  }

  public Integer getDecimalDigit() {
    return decimalDigit;
  }

  public void setDecimalDigit(Integer decimalDigit) {
    this.decimalDigit = decimalDigit;
  }

  public String getSqlTypeString() {
    return sqlTypeString;
  }

  public void setSqlTypeString(String sqlTypeString) {
    this.sqlTypeString = sqlTypeString;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this).append("id", this.getId()).append("new", this.isNew())
        .append("name", this.getName()).append("sqlType", this.getSqlType())
        .append("sqlTypeString", this.getSqlTypeString()).append("size", this.getSize())
        .append("isnullable", this.getIsnullable()).toString();
  }

}
