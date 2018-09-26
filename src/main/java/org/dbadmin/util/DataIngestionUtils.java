package org.dbadmin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.DBConnectionInfo;
import org.dbadmin.model.DataModel;
import org.dbadmin.model.DataTable;
import org.dbadmin.model.DbConnection;
import org.dbadmin.model.IngestionInfo;
import org.dbadmin.model.ValidationInfo;
import org.dbadmin.model.ValidationStats;

/**
 * Created by henrynguyen on 2/20/16.
 */
public class DataIngestionUtils {
  private static String jdbcUserName = Configuration.getProperty("jdbc.username");
  private static String jdbcPassword = Configuration.getProperty("jdbc.password");
  private static String jdbcUrl = Configuration.getProperty("jdbc.url");
  private static String jdbcDriverClassName = Configuration.getProperty("jdbc.driverClassName");

  public static List<Integer> numericTypes =
      Arrays.asList(new Integer[] {-6, -5, 2, 3, 4, 5, 6, 7, 8});


  // TODO: move to own class
  public static ErrorCode connectivityCheck(DBConnectionInfo dbConInfo) {
    // jdbc:sqlserver://127.0.0.1:1433;databaseName={DBNAME}
    String url =
        "jdbc:sqlserver://" + dbConInfo.getIp() + ";databaseName=" + dbConInfo.getDatabase() + ";";
    try {
      Connection con =
          DriverManager.getConnection(url, dbConInfo.getUsername(), dbConInfo.getPassword());
      boolean validCon = con.isValid(5);
      return validCon ? ErrorCode.OK : ErrorCode.ERROR;
    } catch (SQLException e) {
      e.printStackTrace();
      return ErrorCode.ERROR;
    }
  }
  
  public static ErrorCode connectivityCheckUsingDataSource(DbConnection dbConnection) {
    DataSource testedDataSource = (DataSource) DataSourceProvider.getDataSource(dbConnection);
    try (Connection con = testedDataSource.getConnection())
    {
      boolean validCon = con.isValid(5);
      return validCon ? ErrorCode.OK : ErrorCode.ERROR;
    } catch (SQLException e) {
      e.printStackTrace();
      return ErrorCode.ERROR;
    }
  }
  
/* We don't need it anymore - see getNumericDataFromDataSource method below
 * Denys K. 6/7/2016
 * 
  public static DataTable getNumericData(String tableName) {
    Map<String, ArrayList<String>> numericalValues = new HashMap<>();
    Map<String, ArrayList<String>> nonNumericalValues = new HashMap<>();
    int numColumns = 0;
    try {
      Class.forName(jdbcDriverClassName);
      Connection con = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);

      Statement selectStatement = con.createStatement();
      ResultSet resultSet = selectStatement.executeQuery("select * from " + tableName);
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      numColumns = resultSetMetaData.getColumnCount();

      for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
        if (numericTypes.contains(new Integer(resultSetMetaData.getColumnType(i)))) {
          numericalValues.put(resultSetMetaData.getColumnName(i), new ArrayList<>());
        } else {
          nonNumericalValues.put(resultSetMetaData.getColumnName(i), new ArrayList<>());
        }
      }

      while (resultSet.next()) {
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
          String columeName = resultSetMetaData.getColumnName(i);
          String rowValue = resultSet.getString(i);
          if (numericTypes.contains(new Integer(resultSetMetaData.getColumnType(i)))) {
            numericalValues.get(columeName).add(rowValue);
          } else {
            nonNumericalValues.get(columeName).add(rowValue);
          }
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<DataModel> dataModels = new ArrayList<>();

    // Add Numerical Values
    for (String key : numericalValues.keySet()) {
      List<String> l = numericalValues.get(key);
      DataModel dataModel = StatisticsUtils.getNumericalDataModel(l);
      dataModel.columnName = key;
      dataModels.add(dataModel);
    }

    // Add NonNumerical Values
    for (String key : nonNumericalValues.keySet()) {
      List<String> l = nonNumericalValues.get(key);
      DataModel dataModel = StatisticsUtils.getNonNumericalDataModel(l);
      dataModel.columnName = key;
      dataModels.add(dataModel);
    }

    DataTable dataTable = new DataTable();
    dataTable.setTableName(tableName);
    dataTable.setStats(dataModels);
    dataTable.setNumberColumns(numColumns);
    dataTable.setNumberNumericalColumns(numericalValues.keySet().size());
    dataTable.setNumberNonnumericalColumns(nonNumericalValues.keySet().size());

    return dataTable;
  }
*/

 /**
 * Use latest selected from UI connection through DataSource interface
 * @param tableName
 * @return
 */
  public static DataTable getNumericDataFromDataSource(String tableName) {
    Map<String, ArrayList<String>> numericalValues = new HashMap<>();
    Map<String, ArrayList<String>> nonNumericalValues = new HashMap<>();
    int numColumns = 0;
    DataSource testedDataSource = (DataSource) DataSourceProvider.getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());
    try (Connection con = testedDataSource.getConnection()) {

    Statement selectStatement = con.createStatement();
      ResultSet resultSet = selectStatement.executeQuery("select * from " + tableName);
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      numColumns = resultSetMetaData.getColumnCount();

      for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
        if (numericTypes.contains(new Integer(resultSetMetaData.getColumnType(i)))) {
          numericalValues.put(resultSetMetaData.getColumnName(i), new ArrayList<>());
        } else {
          nonNumericalValues.put(resultSetMetaData.getColumnName(i), new ArrayList<>());
        }
      }

      while (resultSet.next()) {
        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
          String columeName = resultSetMetaData.getColumnName(i);
          String rowValue = resultSet.getString(i);
          if (numericTypes.contains(new Integer(resultSetMetaData.getColumnType(i)))) {
            numericalValues.get(columeName).add(rowValue);
          } else {
            nonNumericalValues.get(columeName).add(rowValue);
          }
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<DataModel> dataModels = new ArrayList<>();

    // Add Numerical Values
    for (String key : numericalValues.keySet()) {
      List<String> l = numericalValues.get(key);
      DataModel dataModel = StatisticsUtils.getNumericalDataModel(l);
      dataModel.columnName = key;
      dataModels.add(dataModel);
    }

    // Add NonNumerical Values
    for (String key : nonNumericalValues.keySet()) {
      List<String> l = nonNumericalValues.get(key);
      DataModel dataModel = StatisticsUtils.getNonNumericalDataModel(l);
      dataModel.columnName = key;
      dataModels.add(dataModel);
    }

    DataTable dataTable = new DataTable();
    dataTable.setTableName(tableName);
    dataTable.setStats(dataModels);
    dataTable.setNumberColumns(numColumns);
    dataTable.setNumberNumericalColumns(numericalValues.keySet().size());
    dataTable.setNumberNonnumericalColumns(nonNumericalValues.keySet().size());

    return dataTable;
  }
  
  
  public static ErrorCode copyDataFromSrcTableToTargetTable(IngestionInfo dbConInfo) {
    String url =
        "jdbc:sqlserver://" + dbConInfo.getIp() + ";databaseName=" + dbConInfo.getDatabase() + ";";

    try {
      Class.forName(jdbcDriverClassName);
      Connection sourceCon =
          DriverManager.getConnection(url, dbConInfo.getUsername(), dbConInfo.getPassword());
      Connection targetCon = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);

      Statement selectStatement = sourceCon.createStatement();

      ResultSet resultSet =
          selectStatement.executeQuery("select * from " + dbConInfo.getSourceTable());
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

      String delim = ",";
      String headerStr = resultSetMetaData.getColumnName(1);
      String valuesStr = "?";

      String columnString = resultSetMetaData.getColumnName(1) + " "
          + DataTypeJDBCtoString.getDataTypeString(resultSetMetaData.getColumnType(1)) + " null, ";

      for (int i = 2; i <= resultSetMetaData.getColumnCount(); i++) {
        headerStr = headerStr + delim + resultSetMetaData.getColumnName(i);
        valuesStr = valuesStr + delim + "?";
        columnString += resultSetMetaData.getColumnName(i) + " "
            + DataTypeJDBCtoString.getDataTypeString(resultSetMetaData.getColumnType(i))
            + " null, ";
      }

      String insertStatement = "INSERT INTO " + dbConInfo.getTargetTable() + "(" + headerStr
          + ") VALUES" + "(" + valuesStr + ")";

      // If the table does not exist in the target location, create it first
      String createStatement = "if not exists (select * from sysobjects where name='"
          + dbConInfo.getTargetTable() + "' and xtype='U') create table "
          + dbConInfo.getTargetTable() + " ( " + columnString + ")";

      targetCon.prepareStatement(createStatement).execute();

      while (resultSet.next()) {

        String row = resultSet.getString(1);
        for (int i = 2; i <= resultSetMetaData.getColumnCount(); i++) {
          row = row + delim + resultSet.getString(i);
        }

        PreparedStatement preparedStatement = targetCon.prepareStatement(insertStatement);

        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
          if (resultSetMetaData.getColumnType(i) == -6) {
            preparedStatement.setInt(i, resultSet.getInt(i));
          } else if (resultSetMetaData.getColumnType(i) == -5) {
            preparedStatement.setInt(i, resultSet.getInt(i));
          } else if (resultSetMetaData.getColumnType(i) == -1) {
            preparedStatement.setInt(i, resultSet.getInt(i));
          } else if (resultSetMetaData.getColumnType(i) == 0) {
            preparedStatement.setNull(i, 0);
          } else if (resultSetMetaData.getColumnType(i) == 1) {
            preparedStatement.setString(i, resultSet.getString(i));
          } else if (resultSetMetaData.getColumnType(i) == 2) {
            preparedStatement.setInt(i, resultSet.getInt(i));
          } else if (resultSetMetaData.getColumnType(i) == 3) {
            preparedStatement.setBigDecimal(i, resultSet.getBigDecimal(i));
          } else if (resultSetMetaData.getColumnType(i) == 4) {
            preparedStatement.setInt(i, resultSet.getInt(i));
          } else if (resultSetMetaData.getColumnType(i) == 5) {
            preparedStatement.setInt(i, resultSet.getInt(i));
          } else if (resultSetMetaData.getColumnType(i) == 6) {
            preparedStatement.setFloat(i, resultSet.getFloat(i));
          } else if (resultSetMetaData.getColumnType(i) == 8) {
            preparedStatement.setDouble(i, resultSet.getDouble(i));
          } else if (resultSetMetaData.getColumnType(i) == 12) {
            preparedStatement.setString(i, resultSet.getString(i));
          } else if (resultSetMetaData.getColumnType(i) == 91) {
            preparedStatement.setDate(i, resultSet.getDate(i));
          } else if (resultSetMetaData.getColumnType(i) == 92) {
            preparedStatement.setTime(i, resultSet.getTime(i));
          } else if (resultSetMetaData.getColumnType(i) == 93) {
            preparedStatement.setTimestamp(i, resultSet.getTimestamp(i));
          } else {
            preparedStatement.setString(i, resultSet.getString(i));
          }
        }

        // execute insert SQL stetement
        preparedStatement.executeUpdate();
        preparedStatement.close();
      }
      return ErrorCode.OK;
    } catch (Exception e) {
      e.printStackTrace();
      return ErrorCode.ERROR;
    }
  }

  public static ValidationStats genStats(ValidationInfo dbConInfo) {
    String url =
        "jdbc:sqlserver://" + dbConInfo.getIp() + ";databaseName=" + dbConInfo.getDatabase() + ";";
    ValidationStats output = new ValidationStats();

    try {
      Class.forName(jdbcDriverClassName);
      Connection sourceCon =
          DriverManager.getConnection(url, dbConInfo.getUsername(), dbConInfo.getPassword());

      Statement selectStatement = sourceCon.createStatement();

      // row count
      ResultSet sourceResultSet =
          selectStatement.executeQuery("select count(*) as x from " + dbConInfo.getSourceTable());
      while (sourceResultSet.next()) {
        output.setSourceRowCount(sourceResultSet.getInt("x"));
      }

      // avg
      sourceResultSet = selectStatement.executeQuery(
          "select avg(" + dbConInfo.getColumnName() + ") as x from " + dbConInfo.getSourceTable());
      while (sourceResultSet.next()) {
        output.setSourceFiftyPercentile(sourceResultSet.getDouble("x"));
      }

      // num nulls
      sourceResultSet = selectStatement.executeQuery("select count(*) - count("
          + dbConInfo.getColumnName() + ") as x from " + dbConInfo.getSourceTable());
      while (sourceResultSet.next()) {
        output.setSourceNumNulls(sourceResultSet.getInt("x"));
      }

      // min nulls
      sourceResultSet = selectStatement.executeQuery(
          "select min(" + dbConInfo.getColumnName() + ") as x from " + dbConInfo.getSourceTable());
      while (sourceResultSet.next()) {
        output.setSourceMin(sourceResultSet.getDouble("x"));
      }

      // max nulls
      sourceResultSet = selectStatement.executeQuery(
          "select max(" + dbConInfo.getColumnName() + ") as x from " + dbConInfo.getSourceTable());
      while (sourceResultSet.next()) {
        output.setSourceMax(sourceResultSet.getDouble("x"));
      }

      output.setStatus("OK");
      sourceCon.close();

      Connection targetCon = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
      selectStatement = null;
      selectStatement = targetCon.createStatement();
      // row count
      ResultSet targetResultSet =
          selectStatement.executeQuery("select count(*) as x from " + dbConInfo.getTargetTable());
      while (targetResultSet.next()) {
        output.setTargetRowCount(targetResultSet.getInt("x"));
      }

      // avg
      targetResultSet = selectStatement.executeQuery(
          "select avg(" + dbConInfo.getColumnName() + ") as x from " + dbConInfo.getTargetTable());
      while (targetResultSet.next()) {
        output.setTargetFiftyPercentile(targetResultSet.getDouble("x"));
      }

      // num nulls
      targetResultSet = selectStatement.executeQuery("select count(*) - count("
          + dbConInfo.getColumnName() + ") as x from " + dbConInfo.getTargetTable());
      while (targetResultSet.next()) {
        output.setTargetNumNulls(targetResultSet.getInt("x"));
      }

      // min nulls
      targetResultSet = selectStatement.executeQuery(
          "select min(" + dbConInfo.getColumnName() + ") as x from " + dbConInfo.getTargetTable());
      while (targetResultSet.next()) {
        output.setTargetMin(targetResultSet.getDouble("x"));
      }

      // max nulls
      targetResultSet = selectStatement.executeQuery(
          "select max(" + dbConInfo.getColumnName() + ") as x from " + dbConInfo.getTargetTable());
      while (targetResultSet.next()) {
        output.setTargetMax(targetResultSet.getDouble("x"));
      }
      targetCon.close();
      return output;
    } catch (Exception e) {
      e.printStackTrace();
      output.setStatus("ERROR");
      return output;
    }
  }
}
