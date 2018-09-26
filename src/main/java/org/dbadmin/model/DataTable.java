package org.dbadmin.model;

import java.util.List;

/**
 * This class model the Data Table and its statistic for
 * both numerical data and non-numerical data.
 */
public class DataTable {

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  String tableName;

  public List<DataModel> getStats() {
    return stats;
  }

  public void setStats(List<DataModel> stats) {
    this.stats = stats;
  }

  List<DataModel> stats;

  public int getNumberColumns() {
    return numberColumns;
  }

  public void setNumberColumns(int numberColumns) {
    this.numberColumns = numberColumns;
  }

  int numberColumns;

  public int getNumberNumericalColumns() {
    return numberNumericalColumns;
  }

  public void setNumberNumericalColumns(int numberNumericalColumns) {
    this.numberNumericalColumns = numberNumericalColumns;
  }

  int numberNumericalColumns;

  public int getNumberNonnumericalColumns() {
    return numberNonnumericalColumns;
  }

  public void setNumberNonnumericalColumns(int numberNonnumericalColumns) {
    this.numberNonnumericalColumns = numberNonnumericalColumns;
  }

  int numberNonnumericalColumns;
}
