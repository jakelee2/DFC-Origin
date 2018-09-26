package org.dbadmin.model;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DQConfig extends BaseEntity { //D.K. 08/31/2016

    public static final String DQOP = "op";
    public static final String DQSET = "set";
    public static final String DQLENGTH = "length";
    public static final String DQDATE = "date";
    public static final String DELIM = "|";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DQ_REFER_TABLE = "referTable";

	Connection connection;
    String tableName;
    String columnName;
    DQRule rule;
    Map<String, String> params;

    public DQConfig(Connection connection, String tableName, String columnName, DQRule rule,
        Map<String, String> params) {
        this.connection = connection;
        this.tableName = tableName;
        this.columnName = columnName;
        this.rule = rule;
        this.params = params;
    }

    public DQConfig() {
		// used for recreation object while reading from DB 
    	// D.K. 9/1/2016
	}

	public String getDQOp() {
        return params.get(DQOP);
    }

    public int getLength() {
        return Integer.parseInt(params.get(DQLENGTH));
    }

    public Date getDate() {
        String dateStr = params.get(DQDATE);
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public List<String> getSet(){
        String setStr = params.get(DQSET);
        return Arrays.asList(setStr.split(Pattern.quote(DELIM)));
    }
    
    // used for partial object creation. D.K. 9/1/2016
    public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getReferTable() {
		return params.get(DQ_REFER_TABLE);
	}
	@Override
	public String toString() {
		return "DQConfig [connection=" + connection + ", tableName=" + tableName + ", columnName=" + columnName
				+ ", rule=" + rule + ", params=" + params + "]";
	}
}
