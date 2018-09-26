package org.dbadmin.web;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.dbadmin.model.*;
import org.dbadmin.service.TemplateService;
import org.dbadmin.util.*;
/**
 * Created by henrynguyen on 6/9/16.
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StatsController {
    private final TemplateService templateService;

    @Autowired
    public StatsController(TemplateService templateService) {
        this.templateService = templateService;
    }

    // http://localhost:8080/stats/outliers/routes
    @RequestMapping(value = "/stats/outliers/{tableName}", method = RequestMethod.GET)
    public ResponseEntity<List<OutliersData>> outliers(@PathVariable("tableName") String tableName) {
        // use current template
        List<OutliersData> map =
            DataQualitifiers.getOutliers(tableName);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/stats/getOutliers/{tableNames}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, List<OutliersData>>> getOutliers(@PathVariable("tableNames") String tableNames) {

        String[] tables = tableNames.split(",");
        Map<String, List<OutliersData>> map = new HashMap<>();
        for (String tableName : tables) {
            map.put(tableName, DataQualitifiers.getOutliers(tableName));
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping(value="/outliers", method = RequestMethod.GET)
    public String dqstatsPage (HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        Collection<DbTable> DbTables = null;
        try {
            DbTables = this.templateService.findDbTableByName("");
        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        model.addAttribute("DbTables", DbTables);
        return "outliers";
    }

    // For API Testing only!
    @RequestMapping(value = "/stats/null/{tableName}/{columnName}", method = RequestMethod.GET)
    public ResponseEntity<List<Integer>> getNullCheck(@PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName) {

        DataSource testedDataSource = (DataSource) DataSourceProvider
            .getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());

        List<Integer> nullRows = new ArrayList<>();

        try {

            DQConfig config = new DQConfigBuilder(testedDataSource.getConnection())
                .tableName(tableName)
                .columnName(columnName)
                .rule(DQRule.NULL_CHECK)
                .build();

            Report report = DQExecutor.run(config);
            nullRows = report.getDQReportOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(nullRows, HttpStatus.OK);
    }

    // http://localhost:8080/stats/outlier/routes/priority
    @RequestMapping(value = "/stats/outlier/{tableName}/{columnName}", method = RequestMethod.GET)
    public ResponseEntity<List<List<String>>> getOutliersCheck(@PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName) {

        DataSource testedDataSource = (DataSource) DataSourceProvider
            .getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());

        List<List<String>> nullRows = new ArrayList<>();

        try {

            DQConfig config = new DQConfigBuilder(testedDataSource.getConnection())
                .tableName(tableName)
                .columnName(columnName)
                .rule(DQRule.OUTLIER)
                .build();

            Report report = DQExecutor.run(config);
            nullRows = report.getDQReportFailedRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(nullRows, HttpStatus.OK);
    }

    // For API Testing only!
    @RequestMapping(value = "/stats/length/{tableName}/{columnName}/{length}", method = RequestMethod.GET)
    public ResponseEntity<List<List<String>>> getLengthCheck(@PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName, @PathVariable("length") int length) {

        DataSource testedDataSource = (DataSource) DataSourceProvider
            .getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());

        String op = DqOps.GREATER_OR_EQUAL;

        List<List<String>> failedRows = new ArrayList<>();
        try {

            DQConfig config = new DQConfigBuilder(testedDataSource.getConnection())
                .tableName(tableName)
                .columnName(columnName)
                .rule(DQRule.LENGTH_CHECK)
                .withOps(op)
                .withLength(length)
                .build();

            Report report = DQExecutor.run(config);
            failedRows = report.getDQReportFailedRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(failedRows, HttpStatus.OK);
    }

    // For API Testing only!
    @RequestMapping(value = "/stats/date/{tableName}/{columnName}", method = RequestMethod.GET)
    public ResponseEntity<List<List<String>>> getDateCheck(@PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName) {

        DataSource testedDataSource = (DataSource) DataSourceProvider
            .getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());

        String op = DqOps.GREATER_OR_EQUAL;

        SimpleDateFormat sdf = new SimpleDateFormat(DQConfig.DATE_FORMAT);
        Date date = null;
        try {
            date = sdf.parse("2016-07-21");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<List<String>> failedRows = new ArrayList<>();
        try {

            DQConfig config = new DQConfigBuilder(testedDataSource.getConnection())
                .tableName(tableName)
                .columnName(columnName)
                .rule(DQRule.DATE_CHECK)
                .withOps(op)
                .withDate(date)
                .build();

            Report report = DQExecutor.run(config);

            failedRows = report.getDQReportFailedRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(failedRows, HttpStatus.OK);
    }

    // For API Testing only!
    @RequestMapping(value = "/stats/set/{tableName}/{columnName}", method = RequestMethod.GET)
    public ResponseEntity<List<List<String>>> getSetCheck(@PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName) {

        DataSource testedDataSource = (DataSource) DataSourceProvider
            .getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());

        String op = DqOps.IN;
        List<String> list = Arrays.asList("henry", "fred");

        List<List<String>> failedRows = new ArrayList<>();
        try {

            DQConfig config = new DQConfigBuilder(testedDataSource.getConnection())
                .tableName(tableName)
                .columnName(columnName)
                .rule(DQRule.SET_CHECK)
                .withOps(op)
                .withSet(list)
                .build();

            Report report = DQExecutor.run(config);
            failedRows = report.getDQReportFailedRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(failedRows, HttpStatus.OK);
    }

    // For API Testing only!
    /*
     * highlight orphan foreign keys with no parent row in parent table
     * */
    @RequestMapping(value = "/stats/orphan/{tableName}/{columnName}/{referTable}", method = RequestMethod.GET)
    public ResponseEntity<List<List<String>>> getOrphanDetect(@PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName, @PathVariable("referTable") String referTable) {

        DataSource testedDataSource = (DataSource) DataSourceProvider
            .getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());

        String op = DqOps.NOT_IN;
        
        List<List<String>> failedRows = new ArrayList<>();
        try {

            DQConfig config = new DQConfigBuilder(testedDataSource.getConnection())
                .tableName(tableName)
                .columnName(columnName)
                .referTable(referTable)
                .rule(DQRule.ORPHAN_DETECT)
                .withOps(op)
                .build();
            Report report = DQExecutor.run(config);
            failedRows = report.getDQReportFailedRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(failedRows, HttpStatus.OK);
    }
    
    // For API Testing only!
    /**
     *  ensure there are no foreign key constraint violations from constant alters to tables
     */
    @RequestMapping(value = "/stats/pk/{tableName}/{columnName}/{referTable}", method = RequestMethod.GET)
    public ResponseEntity<List<List<String>>> getInvalidPKDetect(@PathVariable("tableName") String tableName, @PathVariable("columnName") String columnName, @PathVariable("referTable") String referTable) {

        DataSource testedDataSource = (DataSource) DataSourceProvider
            .getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());

        String op = DqOps.NOT_IN;
        
        List<List<String>> failedRows = new ArrayList<>();
        try {

            DQConfig config = new DQConfigBuilder(testedDataSource.getConnection())
                .tableName(tableName)
                .columnName(columnName)
                .referTable(referTable)
                .rule(DQRule.INVALID_PK_DETECT)
                .withOps(op)
                .build();
            Report report = DQExecutor.run(config);
            failedRows = report.getDQReportFailedRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(failedRows, HttpStatus.OK);
    }
    
    // For API Testing only!
    /*
     * datatypeDetect checks whether the column of varchar can be stored as integer
     * return column names rather than primary keys
     * */
    @RequestMapping(value = "/stats/datatype/{tableName}", method = RequestMethod.GET)
    public ResponseEntity<List<List<String>>> getInvalidPKDetect(@PathVariable("tableName") String tableName) {

        DataSource testedDataSource = (DataSource) DataSourceProvider
            .getDataSource(ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection());
        
        List<List<String>> failedRows = new ArrayList<>();
        try {

            DQConfig config = new DQConfigBuilder(testedDataSource.getConnection())
                .tableName(tableName)
                .rule(DQRule.DATA_TYPE_DETECT)
                .build();
            Report report = DQExecutor.run(config);
            failedRows = report.getDQReportFailedRecords();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(failedRows, HttpStatus.OK);
    }
}

