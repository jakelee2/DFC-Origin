package org.dbadmin.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by henrynguyen on 9/1/16.
 */
public class SQLUtils {
    public static Set<String> getPrimaryKeyColumnsForTable(Connection connection, String tableName) throws SQLException {
        try (ResultSet pkColumns = connection.getMetaData().getPrimaryKeys(null, null, tableName)) {
            Set<String> pkColumnSet = new HashSet<>();
            while (pkColumns.next()) {
                String pkColumnName = pkColumns.getString("COLUMN_NAME");
                pkColumnSet.add(pkColumnName);
            }
            return pkColumnSet;
        }
    }

    public static Set<String> getAllColumnsForTable(Connection connection, String tableName) throws SQLException {
        Set<String> columns = new HashSet<>();
        ResultSet resultSet = connection.getMetaData().getColumns(null, null, tableName, null);
        while (resultSet.next()) {
            String name = resultSet.getString("COLUMN_NAME");
            columns.add(name);
        }
        return columns;
    }

    public static Set<String> getPrimaryKeysColumnsOrAllColumns(Connection connection, String tableName) throws SQLException {
        Set<String> primaryKeys = getPrimaryKeyColumnsForTable(connection, tableName);
        return primaryKeys.isEmpty() ? getAllColumnsForTable(connection, tableName) : primaryKeys;
    }

    public static List<String> getValuesForColumns(ResultSet resultSet, Set<String> columns) {
        List<String> keyValues = columns.stream().map(k -> {
            try {
                return resultSet.getString(k);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "";
        }).collect(Collectors.toList());
        return keyValues;
    }

    public static Set<String> getAllColumnNames(ResultSetMetaData metaData) {
        Set<String> columns = new HashSet<String>();
        try {
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                columns.add(metaData.getColumnName(i));
            }
        } catch (Exception e) {

        }
        return columns;
    }
}
