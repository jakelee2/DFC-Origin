package org.dbadmin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrynguyen on 8/11/16.
 */
public class DqReport implements Report {

    public static final String OK = "OK";
    public static final String NULL_FOUND = "NULL_FOUND";
    public static final String INVALID_LENGTH = "INVALID_LENGTH";
    public static final String INVALID_DATE = "INVALID_DATE";
    public static final String INVALID_VALUE = "INVALID_VALUE";
    public static final String OUTLIERS_FOUND = "OUTLIERS_FOUND";
    public static final String INVALID_FORMAT = "INVALID_FORMAT";
    public static final String CLUSTER_FOUND = "CLUSTER_FOUND";
    public static final String DUP_FOUND = "DUP_FOUND";
	public static final String SINGLE_PRIMARY_KEY_FOUND = "SINGLE_PRIMARY_KEY_FOUND";
	public static final String NEW_PRIMARY_KEY_SUGGESTION = "NEW_PRIMARY_KEY_SUGGESTION";
	public static final String SMALLER_PRIMARY_KEY_FOUND = "SMALLER_PRIMARY_KEY_FOUND";
    public static final String ORPHAN_FOUND = "ORPHAN_FOUND";
    public static final String INVALID_PK_FOUND = "INVALID_PK_FOUND";
    public static final String INAPPROPRIATE_DATA_TYPE_FOUND = "INAPPROPRIATE_DATA_TYPE_FOUND";

    public String status;
    public List<Integer> output;
    public List<List<String>> failedRecords;

    @Deprecated
    public DqReport(String status,  List<Integer> output) {
        this.status = status;
        this.output = output;
        this.failedRecords = new ArrayList<>();
    }

    public DqReport(List<List<String>> failedRecords, String status) {
        this.status = status;
        this.output = new ArrayList<>();
        this.failedRecords = failedRecords;
    }

    @Override public String getStatus() {
        return status;
    }

    @Override public String getClassName() {
        return this.getClassName();
    }

    @Override public String getSimpleClassName() {
        return this.getClass().getSimpleName();
    }

    @Override public Object getOutput() {
        return output;
    }

    @Override public List<Integer> getDQReportOutput() {
        return output;
    }

    @Override public List<List<String>> getDQReportFailedRecords() {
        return failedRecords;
    }

}
