package org.dbadmin.util;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.dbadmin.model.*;

import javax.sql.DataSource;

/**
 * Created by henrynguyen on 6/4/16.
 */
public class DataQualitifiers {

    public static Map<String, Map<String, String>> getOutliersMap(String tableName) {

        Statement selectStatement = null;

        // Column name -> Map (rowid -> value)
        Map<String, Map<String, String>> numericalValues = new HashMap<>();

        DataSource testedDataSource = (DataSource) DataSourceProvider.getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());

        try {
            Connection con = testedDataSource.getConnection();
            selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery("select * from " + tableName);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();


            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                if (DataIngestionUtils.numericTypes.contains(new Integer(resultSetMetaData.getColumnType(i)))) {
                    numericalValues.put(resultSetMetaData.getColumnName(i), new HashMap<>());
                }
            }

            while (resultSet.next()) {
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    String columeName = resultSetMetaData.getColumnName(i);
                    String rowValue = resultSet.getString(i);
                    String rowNumber = resultSet.getRow() + "";
                    if (DataIngestionUtils.numericTypes.contains(new Integer(resultSetMetaData.getColumnType(i)))) {
                        numericalValues.get(columeName).put(rowNumber, rowValue);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<String, Map<String, String>> outliers = new HashMap<>();
        for (String columnName : numericalValues.keySet()) {
            Map<String, String> outlier = StatisticsUtils.getOutliers(numericalValues.get(columnName));
            outliers.put(columnName, outlier);
        }
        return outliers;

    }

    /**
     * @param tableName
     * @return outliers Data. This method perform outliers for all columns of a tables.
     */
    public static List<OutliersData>  getOutliers(String tableName) {
        Map<String, Map<String, String>> map = getOutliersMap(tableName);

        List<OutliersData> results = new ArrayList<>();

        for(String columnName : map.keySet()) {
            Map<String, String> value = map.get(columnName);
            OutliersData outliersData = new OutliersData(columnName);
            List<RowData> rowDatas = value.keySet().stream().map(rowId -> new RowData(rowId, value.get(rowId))).collect(Collectors.toList());
            if (rowDatas.size() > 0) {
                outliersData.setStats(rowDatas);
                results.add(outliersData);
            }
        }

        return results;
    }

    public static Report getOutliersReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {

            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);

            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(String.format("SELECT * FROM %s", tableName));

            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }
            Map<String, String> outlier = StatisticsUtils.getOutliers(rowNum2Values);
            failedRows = outlier.keySet().stream().map(s -> rowNum2PrimaryKeyValues.get(s)).collect(Collectors.toList());

        } catch (NumberFormatException e){ //column cannot be parsed to Number
                return new DqReport(failedRows, DqReport.INVALID_FORMAT);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        if (failedRows.size() > 0) {
            report = new DqReport(failedRows, DqReport.OUTLIERS_FOUND);
        }
        return report;
    }

    public static Report getNullReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s WHERE %s IS NULL", tableName, columnName));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet,
                    primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }
        } catch (Exception e){
            throw new RuntimeException(e); //to populate it to gui
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList(rowNum2PrimaryKeyValues.values()), DqReport.NULL_FOUND);
        }
        return report;
    }

    public static Report getLengthCheckReport(Connection con, String tableName, String columnName, String op, int length) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s WHERE LEN(%s) %s %s", tableName, columnName, op, length));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet,
                    primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList(rowNum2PrimaryKeyValues.values()), DqReport.INVALID_LENGTH);
        }
        return report;
    }

    public static Report getDateCheckReport(Connection con, String tableName, String columnName, String op, Date date) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String strDate = sdf.format(date);

        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s WHERE %s %s '%s'", tableName, columnName, op, strDate));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet,
                    primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList(rowNum2PrimaryKeyValues.values()), DqReport.INVALID_DATE);
        }
        return report;
    }

    public static Report getSetCheckReport(Connection con, String tableName, String columnName, String op, List<String> set) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            String lst = set.stream().map(s -> "'" + s + "'").collect(Collectors.joining(","));
            lst = "(" + lst + ")";
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s WHERE %s %s %s", tableName, columnName, op, lst));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet,
                    primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList<List<String>>(rowNum2PrimaryKeyValues.values()), DqReport.INVALID_VALUE);
        }
        return report;
    }

    public static Report getTelephoneCheckReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s", tableName));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                if(RegexUtils.isPhoneNumber(rowValue)) {
                    List<String> primaryKeysValues =
                        SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                    rowNum2Values.put(rowNumber, rowValue);
                    rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList(rowNum2PrimaryKeyValues.values()), DqReport.INVALID_FORMAT);
        }
        return report;
    }

    public static Report getDateParsableReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s", tableName));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                if(RegexUtils.isParsableDate(rowValue)) {
                    List<String> primaryKeysValues =
                        SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                    rowNum2Values.put(rowNumber, rowValue);
                    rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList(rowNum2PrimaryKeyValues.values()), DqReport.INVALID_FORMAT);
        }
        return report;
    }

    public static Report getAddressCheckReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s", tableName));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                if(RegexUtils.validateAddress(rowValue)) {
                    List<String> primaryKeysValues =
                        SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                    rowNum2Values.put(rowNumber, rowValue);
                    rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList(rowNum2PrimaryKeyValues.values()), DqReport.INVALID_FORMAT);
        }
        return report;
    }

    public static Report getDuplicatedReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {

            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);

            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(String.format("SELECT * FROM %s", tableName));

            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }
            Map<String, String> outlier = StatisticsUtils.getDuplicatedValues(rowNum2Values);
            failedRows = outlier.keySet().stream().map(s -> rowNum2PrimaryKeyValues.get(s)).collect(Collectors.toList());

        } catch (Exception e){
            throw new RuntimeException(e);
        }
        if (failedRows.size() > 0) {
            report = new DqReport(failedRows, DqReport.DUP_FOUND);
        }
        return report;
    }
    
    
	public static Report getCandidateKeyReport(Connection con, String tableName, String columnName) { //List<String> 
		List<String> primaryKeys = new ArrayList<String>();
        CandidateKeyHelper helper = new CandidateKeyHelper();
        List<String> smallestPrimaryKeys = new ArrayList<String>();
        List<List<String>> foundKeys = new ArrayList<>();
        DqReport report = new DqReport(foundKeys, DqReport.OK);
        
        try {
        	String columnIdName = "Column_Name";            
            Statement selectStatement = con.createStatement();
                   
            ResultSet resultSet = selectStatement.executeQuery(String.format("SELECT c.Column_Name "
												                			+ "FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS t "
												                			+ "INNER JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE c "
												                			+ "ON c.Constraint_Name = t.Constraint_Name AND c.Table_Name = t.Table_Name "
												                			+ "AND t.Constraint_Type = 'PRIMARY KEY' "
												                			+ "AND c.Table_Name = '%s' ",tableName));
            
            //Case 1: If no PK present in table
            if(!resultSet.isBeforeFirst()){				//No records returned
            	System.out.println("No primary key found");
            	
            	//Find all columns in tables and check if any column can be made a PK
            	List<String> allColumns = new ArrayList<String>();
            	selectStatement = con.createStatement();                
                resultSet = selectStatement.executeQuery(String.format("SELECT Column_Name FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = N'%s' ",tableName));                
                while (resultSet.next()) {
                	allColumns.add(resultSet.getString(columnIdName));	    			//Populate all columns in list                            
	            }
                
                //Check each primary key combinations possible
                String[] pkCombinations = helper.getPrimaryKeyCombinations(allColumns);
                System.out.println(Arrays.toString(pkCombinations));          
                
                for(int i=0; i < pkCombinations.length; i++) {
              	  	selectStatement = con.createStatement();
              	  	String uniqueSql = String.format("SELECT " + pkCombinations[i] + " FROM %s GROUP BY " + pkCombinations[i] + " HAVING COUNT(*) > 1", tableName);
              	  	resultSet = selectStatement.executeQuery(uniqueSql);
                    
                    if(!resultSet.isBeforeFirst()) {									//No records returned => Smaller PK combination found                  	  
                  	  System.out.println("smallerPk = "+ pkCombinations[i]);
                  	  smallestPrimaryKeys.add(pkCombinations[i]);
                  	  foundKeys.add(smallestPrimaryKeys);
                  	  report = new DqReport(foundKeys, DqReport.NEW_PRIMARY_KEY_SUGGESTION);	
                  	  break;
                    }
                }
            	
            } else {
            	
	            //Case 2: If PK is present, get primary keys present
	            while (resultSet.next()) {
	                String primaryKey = resultSet.getString(columnIdName);
	                System.out.println(primaryKey);
	                primaryKeys.add(primaryKey);	                                
	            }
	            
	            if(primaryKeys.size() == 1){												//Single Primary key found
	            	smallestPrimaryKeys.addAll(primaryKeys);	
	            	foundKeys.add(primaryKeys);
	            	report = new DqReport(foundKeys, DqReport.SINGLE_PRIMARY_KEY_FOUND);	
	            	
	            } else {																	//Multiple keys found: Check and minimum Primary Key
	            	String[] pkCombinations = helper.getPrimaryKeyCombinations(primaryKeys);
	                System.out.println(Arrays.toString(pkCombinations));          
	                
	                for(int i=0; i < pkCombinations.length; i++) {
	              	  	selectStatement = con.createStatement();
	              	  	String uniqueSql = String.format("SELECT " + pkCombinations[i] + " FROM %s GROUP BY " + pkCombinations[i] + " HAVING COUNT(*) > 1 ", tableName);
	              	  	resultSet = selectStatement.executeQuery(uniqueSql);
	                    
	                    if(!resultSet.isBeforeFirst()){										//No records returned => Smaller PK combination found	                  	  
	                  	  System.out.println("smallerPk = "+ pkCombinations[i]);
	                  	  smallestPrimaryKeys.add(pkCombinations[i]);
	                  	  foundKeys.add(smallestPrimaryKeys);
	                  	  report = new DqReport(foundKeys, DqReport.SMALLER_PRIMARY_KEY_FOUND);	
	                  	  break;
	                    }
	                }	                
	            }
            }
            
        } catch (Exception e){
            throw new RuntimeException("getCandidateKeyReport: " +e); //to populate it to gui
        }
        
        return report;		
	}

    public static Report getDuplicatedAbnormalVolumeReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {

            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);

            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(String.format("SELECT * FROM %s", tableName));

            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }
            Map<String, String> outlier = StatisticsUtils.getDuplicatedWithAbnormalValues(rowNum2Values);
            failedRows = outlier.keySet().stream().map(s -> rowNum2PrimaryKeyValues.get(s)).collect(Collectors.toList());

        } catch (Exception e){
            throw new RuntimeException(e);
        }
        if (failedRows.size() > 0) {
            report = new DqReport(failedRows, DqReport.DUP_FOUND);
        }
        return report;
    }

    /*
    * highlight orphan foreign keys with no parent row in parent table
    * */
    public static Report getOrphanDetectReport(Connection con, String tableName, String columnName,
        String referTable, String op) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);

            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(String.format("SELECT * FROM %s WHERE %s %s (SELECT %s FROM %s)", tableName, columnName, op, columnName, referTable));

            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                if (rowValue == null) {
                    continue;
                }
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList<List<String>>(rowNum2PrimaryKeyValues.values()), DqReport.ORPHAN_FOUND);
        }
        return report;
    }

    /**
     *  ensure there are no foreign key constraint violations from constant alters to tables
     */
    public static Report getInvalidPKDetectReport(Connection con, String tableName, String columnName,
        String referTable, String op) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);

            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(String.format("SELECT * FROM %s WHERE %s %s (SELECT %s FROM %s)", tableName, columnName, op, columnName, referTable));

            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                if (rowValue == null) {
                    continue;
                }
                List<String> primaryKeysValues = SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(new ArrayList<List<String>>(rowNum2PrimaryKeyValues.values()), DqReport.INVALID_PK_FOUND);
        }
        return report;
    }


    /*
     * datatypeDetect checks whether the column of varchar can be stored as integer
     * return column names rather than primary keys
     * */
    public static Report getDataTypeDetectReport(Connection con, String tableName) {
        List<List<String>> failedRows = new ArrayList<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        List<String> returnColumns = new ArrayList<>();
        try {
            String dataType = "%char";
            Statement selectStatement = con.createStatement();
            ResultSet stringColumns = selectStatement.executeQuery(String.format("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE DATA_TYPE LIKE '%s' AND TABLE_NAME = '%s'", dataType, tableName)
            );
            while (stringColumns.next()) {
                Statement getValueStatement = con.createStatement();
                String colName = stringColumns.getString("COLUMN_NAME");
                ResultSet resultSet = getValueStatement.executeQuery(
                    String.format("SELECT %s FROM %s", colName, tableName));
                boolean isNumericColumn = true;
                while(resultSet.next()) {
                    String columnValue = resultSet.getString(colName);
                    if (columnValue!=null) columnValue=columnValue.trim();
                    if (!StringUtils.isNumeric(columnValue)) {
                        isNumericColumn = false; break;
                    }
                    System.out.println();
                }
                if (isNumericColumn) {
                    returnColumns.add(colName);
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e); //to populate it to gui
        }
        if (returnColumns.size() > 0) {
            List<List<String>> res = new ArrayList<List<String>>();
            res.add(returnColumns);
            report = new DqReport(res, DqReport.INAPPROPRIATE_DATA_TYPE_FOUND);
        }
        return report;
    }

    /**
     * Compute Clutering using Kmean algorithms
     * @param con
     * @param tableName
     * @param columnName
     * @return
     */
    public static Report getClusterDetectionReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s", tableName));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues =
                    SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        Map<String, String> clusters = StatisticsUtils.getClusterDetection(rowNum2Values);
        List<List<String>> id_cluster_list = clusters.entrySet().stream().map(e -> Arrays.asList(e.getKey(), e.getValue())).collect(Collectors.toList());
        if (rowNum2PrimaryKeyValues.size() > 0) {
            report = new DqReport(id_cluster_list, DqReport.CLUSTER_FOUND);
        }
        return report;
    }

    public static Report getFittingDistributionReport(Connection con, String tableName, String columnName) {
        List<List<String>> failedRows = new ArrayList<>();
        Map<String, String> rowNum2Values = new HashMap<>();
        Map<String, List<String>> rowNum2PrimaryKeyValues = new HashMap<>();
        DqReport report = new DqReport(failedRows, DqReport.OK);
        try {
            Set<String> primaryKeys = SQLUtils.getPrimaryKeysColumnsOrAllColumns(con, tableName);
            Statement selectStatement = con.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(
                String.format("SELECT * FROM %s", tableName));
            while (resultSet.next()) {
                String rowNumber = resultSet.getRow() + "";
                String rowValue = resultSet.getString(columnName);
                List<String> primaryKeysValues =
                    SQLUtils.getValuesForColumns(resultSet, primaryKeys);
                rowNum2Values.put(rowNumber, rowValue);
                rowNum2PrimaryKeyValues.put(rowNumber, primaryKeysValues); 
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        double[] arr = StatisticsUtils.getFittingDistribution(rowNum2Values, 3);
        List<String> clusters = new ArrayList<>();
        for (double d : arr) {
        	clusters.add(String.valueOf((int)Math.round(d)));
        }
        if (rowNum2PrimaryKeyValues.size() > 0) {
        	List<List<String>> cluster_list = new ArrayList<>();
        	cluster_list.add(clusters);
            report = new DqReport(cluster_list, DqReport.CLUSTER_FOUND);
        }
        return report;
    }

}