package org.dbadmin.model;

/**
 * This class model the Data Statistic of each column from database table.
 */
public class DataModel {
  public String columnName;
  public double non_missing;
  public double missing;
  public double missing_percent;
  public double unique;
}
