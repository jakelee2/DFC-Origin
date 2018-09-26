package org.dbadmin.util;

/**
 * Utility class for JDBC type to ANSI SQL type conversion.
 *
 */
public class DataTypeJDBCtoString {

  /**
   * Convert integer JDBC type to string ANSI SQL type.
   * 
   * @param jdbcCode
   * @return SQL type
   */
  public static String getDataTypeString(int jdbcCode) {
    String out = "";
    switch (jdbcCode) {
      case -7:
        out = "BIT";
        break;
      case -6:
        out = "TINYINT";
        break;
      case -5:
        out = "BIGINT";
        break;
      case -4:
        out = "IMAGE";
        break;
      case -3:
        out = "UDT";
        break;
      case -2:
        out = "BINARY";
        break;
      case -1:
        out = "TEXT";
        break;
      case 0:
        out = "VARCHAR(MAX)"; // null
        break;
      case 1:
        out = "CHAR";
        break;
      case 2:
        out = "NUMERIC";
        break;
      case 3:
        out = "DECIMAL";
        break;
      case 4:
        out = "INT";
        break;
      case 5:
        out = "SMALLINT";
        break;
      case 6:
        out = "FLOAT";
        break;
      case 7:
        out = "REAL";
        break;
      case 8:
        out = "FLOAT";
        break;
      case 12:
        out = "VARCHAR";
        break;
      case 91:
        out = "DATE";
        break;
      case 92:
        out = "TIME";
        break;
      case 93:
        out = "TIMESTAMP";
        break;
      default:
        out = "VARCHAR(MAX)";
        break;
    }
    return out;
  }
}
