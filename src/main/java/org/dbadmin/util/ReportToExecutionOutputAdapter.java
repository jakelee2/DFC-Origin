package org.dbadmin.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.dbadmin.model.DqReport;
import org.dbadmin.model.ExecutionOutput;
import org.dbadmin.model.OutputStatus;
import org.dbadmin.model.Report;

/**
 * Utility class-adapter for converting from a {@link Report} interface implementation to {@link ExecutionOutput} instance
 * @author D.K.
 *
 */
public class ReportToExecutionOutputAdapter {

	private ExecutionOutput eo = new ExecutionOutput();

	/**
	 * Convert from a {@link Report} interface implementation to {@link ExecutionOutput} instance
	 */
	public static ExecutionOutput convert(Report report) {
		String className = report.getSimpleClassName();

		switch (className) {
		case "DqReport":
			return convertFromDqReport(report);
		//TODO add other report types here
		}

		return new ExecutionOutput();
	}

	/**
	 * Convert from {@link DqReport} instance to {@link ExecutionOutput} instance
	 * @param report 
	 */
	private static ExecutionOutput convertFromDqReport(Report report) {
		List<Integer> outputInteger = new ArrayList<>();
		List<List<String>> outlist = new ArrayList<>();
		List<String> flatList = new ArrayList<>();
		ExecutionOutput eo = new ExecutionOutput();
		outputInteger = report.getDQReportOutput();
		outlist= report.getDQReportFailedRecords();
		flatList = outlist.stream().flatMap(l -> l.stream()).collect(Collectors.toList());
		try { //sort numerics
			List<Integer> intList = new ArrayList<>();
			for(String s : flatList) intList.add(Integer.valueOf(s));
			Collections.sort(intList);
			flatList.clear();
			for(Integer i : intList) flatList.add(String.valueOf(i));	
			} catch (NumberFormatException e) {} //NOP
		
		eo.setTextOutput(Arrays.toString(outputInteger.toArray()));
		eo.setListOutput(Arrays.toString(flatList.toArray()));

		switch (report.getStatus()) {
		case "OK":
			eo.setOutputStatus(OutputStatus.SUCCESS);
			eo.setError("NO ERRORS");
			break;
		case "NULL_FOUND":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("NULL_FOUND");
			break;
		case "INVALID_LENGTH":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("INVALID_LENGTH");
			break;
		case "INVALID_VALUE":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("INVALID_VALUE");
			break;			
		case "INVALID_DATE":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("INVALID_DATE");
			break;		
		case "INVALID_FORMAT":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("INVALID_FORMAT");
			break;			
		case "OUTLIERS_FOUND":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("OUTLIERS_FOUND");
			break;	
		case "DUP_FOUND":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("DUP_FOUND");
			break;		
		case "NEW_PRIMARY_KEY_SUGGESTION":
			eo.setOutputStatus(OutputStatus.SUCCESS);
			eo.setError("NO ERRORS, NEW_PRIMARY_KEY_SUGGESTION");
			break;	
		case "SINGLE_PRIMARY_KEY_FOUND":
			eo.setOutputStatus(OutputStatus.SUCCESS);
			eo.setError("NO ERRORS, SINGLE_PRIMARY_KEY_FOUND");
			break;	
		case "SMALLER_PRIMARY_KEY_FOUND":
			eo.setOutputStatus(OutputStatus.SUCCESS);
			eo.setError("NO ERRORS, SMALLER_PRIMARY_KEY_FOUND");
			break;		
		case "CLUSTER_FOUND":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("CLUSTER_FOUND");
			break;			
		case "ORPHAN_FOUND":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("ORPHAN_FOUND");	
			break;			
		case "INVALID_PK_FOUND":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("INVALID_PK_FOUND");	
			break;			
		case "INAPPROPRIATE_DATA_TYPE_FOUND":
			eo.setOutputStatus(OutputStatus.PASSED);
			eo.setError("INAPPROPRIATE_DATA_TYPE_FOUND");
			break;	
			
		default:
			eo.setOutputStatus(OutputStatus.NA);
			eo.setError("N/A");
			break;
		}
		return eo;
	}

}
