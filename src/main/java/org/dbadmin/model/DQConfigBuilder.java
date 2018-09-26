package org.dbadmin.model;

import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henrynguyen on 8/31/16.
 */
public class DQConfigBuilder {
    Connection connection;
    String tableName;
    String columnName;
    DQRule rule;
    Map<String, String> params;

    public DQConfigBuilder(Connection connection) {
        if (connection == null) throw new IllegalArgumentException("Connection can not be null");
        this.connection = connection;
        params = new HashMap<String, String>();
        tableName = null;
        columnName = null;
        rule = null;
    }

    public DQConfigBuilder tableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public DQConfigBuilder columnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public DQConfigBuilder rule(DQRule rule) {
        this.rule = rule;
        return this;
    }

    public DQConfigBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public DQConfigBuilder withOps(String dqOps) {
        this.params.put(DQConfig.DQOP, dqOps);
        return this;
    }

    public DQConfigBuilder withLength(int length) {
        this.params.put(DQConfig.DQLENGTH, String.valueOf(length));
        return this;
    }

    public DQConfigBuilder withDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DQConfig.DATE_FORMAT);
        this.params.put(DQConfig.DQDATE, sdf.format(date));
        return this;
    }

    public DQConfigBuilder withSet(List<String> set) {
        this.params.put(DQConfig.DQSET, StringUtils.join(set, DQConfig.DELIM));
        return this;
    }
    
    public DQConfigBuilder referTable(String referTable) {
    	this.params.put(DQConfig.DQ_REFER_TABLE, referTable);
        return this;
	}

    public DQConfig build(){
        return new DQConfig(connection, tableName, columnName, rule, params);
    }

}
