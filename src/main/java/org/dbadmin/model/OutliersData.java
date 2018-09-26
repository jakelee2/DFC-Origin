package org.dbadmin.model;

import java.util.List;

/**
 * Created by henrynguyen on 6/9/16.
 */
public class OutliersData {
    public OutliersData(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    String columnName;

    public List<RowData> getOutliers() {
        return outliers;
    }

    public void setStats(List<RowData> outliers) {
        this.outliers = outliers;
    }

    List<RowData> outliers;

}
