package org.dbadmin.model;

import java.util.List;

/**
 * Created by henrynguyen on 8/17/16.
 * Upd. by D.K. on 8/19/2016.
 */
public interface Report {
    String getStatus();

    /**
     * returns class name of the output object
     * @return
     */
    String getClassName();

    /**
     *
     * @return a generic out
     */
    Object getOutput();

    /**
     * @return a list of row that failed for a particular DQ rule
     */
	String getSimpleClassName();

    List<Integer> getDQReportOutput();

    List<List<String>> getDQReportFailedRecords();

}
