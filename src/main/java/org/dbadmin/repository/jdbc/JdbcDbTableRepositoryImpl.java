package org.dbadmin.repository.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.dbadmin.model.DbColumn;
import org.dbadmin.model.DbConnection;
import org.dbadmin.model.DbTable;
import org.dbadmin.repository.DbTableRepository;
import org.dbadmin.util.ActiveConnectionRegistry;
import org.dbadmin.util.DataSourceProvider;
import org.dbadmin.util.DataTypeJDBCtoString;
import org.dbadmin.util.RegexExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Repository;
/**
 * JDBC-based implementation of the {@link DbTableRepository} interface.
 *
 */
@Repository
public class JdbcDbTableRepositoryImpl implements DbTableRepository{
 
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private DataSource systemDataSource;
    private String tableName = "";
    private List<DbTable> tablesList = new ArrayList<>();
    private DataSource clientDataSource;
	private SimpleJdbcInsert insertDbTable;
    
    @Autowired
    public JdbcDbTableRepositoryImpl(DataSource systemDataSource, DataSource clientDataSource) {
        this.systemDataSource = systemDataSource;
        this.clientDataSource = clientDataSource;
		this.insertDbTable = new SimpleJdbcInsert(systemDataSource).withTableName("dbtables")
				.usingGeneratedKeyColumns("id");
	    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(systemDataSource);

    }

 
    /**
     * Find all information about table(s) by name.
     * If name is null it returns list of all tables for this.clientDataSource
     */
    @SuppressWarnings("unchecked")
	@Override
    public Collection<DbTable> findByName(String name) throws DataAccessException, SQLException {
    	String user   ="";
		String pass   ="";
		String dbUrl  ="";
		String jdbc   ="";
		String vendor ="";
		String schema ="";
		DbConnection dbclatest = ActiveConnectionRegistry.INSTANCE.getLatestActiveConnection();
    	
		if ((dbclatest == null)) {
			clientDataSource = systemDataSource;
		} else {
			user   = dbclatest.getDatabaseUser();
			pass   = dbclatest.getDatabasePasswd();
			dbUrl  = dbclatest.getDatabaseJdbcUrl();
			jdbc   = dbclatest.getDatabaseJdbcDriver();
			vendor = dbclatest.getDatabaseJpaVendorName();
			schema = dbclatest.getDatabaseSchemaPattern();
			if ((user.isEmpty())||(pass.isEmpty())||(dbUrl.isEmpty())||(jdbc.isEmpty())) {
				//will work with system database in any case of emergency
				clientDataSource = systemDataSource; 
			} else {
			    dbclatest.setDataSourceClass(systemDataSource.getClass().getName());
				clientDataSource = (DataSource) DataSourceProvider.getDataSource(dbclatest);
			}
		}
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(clientDataSource);
		
        DatabaseMetaData dbmd=null;
		dbmd = clientDataSource.getConnection().getMetaData();
		String url= RegexExtractor.extractIp(dbmd.getURL().toString());
    	
        List<String> list = new ArrayList<>();
        GetTableNames getTableNames = new GetTableNames();
        try {
        	list = (List<String>) JdbcUtils.extractDatabaseMetaData(clientDataSource, getTableNames);
            Collections.sort(list);
        } catch (MetaDataAccessException e) {
            System.out.println(e);
        }
        List<DbTable> tables = new ArrayList<>();
        
        //this surrogate id key used only for MVC representation
        int id =0; 
         
        //row number estimation using fast DB-oriented techniques
        int rowsNumber = 0;
        String fastRowCounter = "";
        String slowRowCounter = ""; 
        
        // get info for every table
        for(String tab : list)
        {
        	DbTable table = new DbTable();
        	Set<DbColumn> columns = new HashSet<>();
        	tableName = tab;
        	table.setId(id++);
        	table.setName(tableName);
            GetColumns getColumns = new GetColumns();
            slowRowCounter = "select count(*) from "+ tableName.trim();
            switch (vendor) {
            case "SQL_SERVER":
            	fastRowCounter = "SELECT CONVERT(bigint, rows) FROM sysindexes WHERE id = OBJECT_ID('"+tableName.trim()+"') AND indid < 2";
            	break;
            	
            case "ORACLE":
            	fastRowCounter = "SELECT NUM_ROWS FROM ALL_TABLES WHERE TABLE_NAME = '"+tableName.trim().toUpperCase()+"'";
            	break;
            
            case "POSTGRESQL":
            	fastRowCounter = "SELECT reltuples::bigint FROM pg_class WHERE oid = '"+(schema.isEmpty()? "":schema+".")+tableName.trim()+"'::regclass";
            	break;
            	
            default:
            	// slow but most precise
            	fastRowCounter = slowRowCounter;
            }
    
            try {
            	columns = (Set<DbColumn>) JdbcUtils.extractDatabaseMetaData(clientDataSource, getColumns);
            } catch (MetaDataAccessException e) {
                throw new RuntimeException(e);
            }
            table.setDbcolumns(columns);
            table.setColumnNumber(columns.size());
            
            Map<String, Object> paramsForCounter = new HashMap<>();
            paramsForCounter.put("tableName", tableName);
            try {
            	rowsNumber = namedParameterJdbcTemplate.queryForObject(fastRowCounter, paramsForCounter, int.class);
            }
            catch (EmptyResultDataAccessException ex) 
            {
            	rowsNumber = 0;
            }
            table.setRowsNumber(rowsNumber);
            try {
            	// ip address plus port
				table.setDbConnectionURL(dbUrl.isEmpty()? url:dbUrl);
				// primary key
				ResultSet rspk = dbmd.getPrimaryKeys(null, null, tableName);
				if (rspk.next()) {
				    String pk = rspk.getString("COLUMN_NAME");
					table.setPrimaryKey(pk.isEmpty()? "no" : pk);
				    }
			} catch (SQLException e) {
				{
					//NOP - nothing to do here
				}
			};
        	tables.add(table);
        }
        tablesList.clear();
        tablesList.addAll(tables);
        return tables;
	}
        
    /**
     * Loads the {@link DbTable} with the supplied <code>id</code> from the class field (no DAO operations here); 
     * also loads the {@link DbColumn} for the corresponding DbTable.
     * @throws SQLException 
     */	
    @Override
    public DbTable findById(int id) throws DataAccessException, SQLException {
    	DbTable table = new DbTable();
    	if (!(tablesList==null) && !(tablesList.isEmpty()))
    	{
    		table = tablesList.get(id);
    	}
    	else
    	{
    		findByName("");
    		table = tablesList.get(id);
    	}
        return table;
    }

    
	/**
	 * Inner utility class
	 *
	 */
    class GetTableNames implements DatabaseMetaDataCallback {
		/**
		 * Read DB metadata to obtain table names
		 * 
		 * @param dbmd
		 * @return List of Tables
		 * @throws SQLException
		 */
		@Override
    	public Object processMetaData(DatabaseMetaData dbmd) throws SQLException {
			String   catalog          = null;
			String   schemaPattern    = null;
			String   tableNamePattern = "%";
			String[] types            = { "TABLE" };
			ResultSet rs = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);
			List<String> listOfTables = new ArrayList<>();
			while (rs.next()) {
				listOfTables.add(rs.getString("TABLE_NAME"));
			}
			return listOfTables;
		}
	}
    
    
	/**
	 * Inner utility class
	 *
	 */
    class GetColumns implements DatabaseMetaDataCallback {
		/**
		 * Read DB metadata to obtain columns names and types of the table
		 * 
		 * @param dbmd
		 * @return Set of Columns
		 * @throws SQLException
		 */
		@Override
    	public Object processMetaData(DatabaseMetaData dbmd) throws SQLException {
			String   catalog           = null;
			String   schemaPattern     = null;
			String   tableNamePattern  = tableName;
			String   columnNamePattern = null;
			ResultSet rs = dbmd.getColumns(catalog, schemaPattern,  tableNamePattern, columnNamePattern);
			Set<DbColumn> setOfColumns = new HashSet<>();
			while (rs.next()) {
				DbColumn dbcolumn = new DbColumn();
				dbcolumn.setName(rs.getString("COLUMN_NAME"));
				dbcolumn.setSqlType(rs.getInt("DATA_TYPE")); 
				dbcolumn.setSqlTypeString(DataTypeJDBCtoString.getDataTypeString(rs.getInt("DATA_TYPE")));
				dbcolumn.setSize(rs.getInt("COLUMN_SIZE"));
				dbcolumn.setDecimalDigit(rs.getInt("DECIMAL_DIGITS"));
				dbcolumn.setIsnullable(rs.getString("IS_NULLABLE"));
				setOfColumns.add(dbcolumn);
			}

			return setOfColumns;
		}
	}
    
	/**
	 * Save four fields of DB Table to dbtable.
	 * This information intend to be used by Task-Businessrule-DbTable-DbColumn logic link
	 * 
	 * Use: e.g. 
	 */
	@Override
	public void saveShortDbTableInfo(DbTable dbtable) throws DataAccessException {
		BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(dbtable);
		if (dbtable.isNew()) {
			Number newKey = this.insertDbTable.executeAndReturnKey(parameterSource);
			dbtable.setId(newKey.intValue());
		} else {
			this.namedParameterJdbcTemplate.update("UPDATE dbtable SET "
					+ "name=:name, " 
					+ "dbconnection_id=:dbconnectionId, "
					+ "commnets=:comments "
					+ "WHERE id=:id", parameterSource);
		}
	} 
	
    /**
     * Loads the {@link DbTable} with the dbconnectionId ; 
     * @throws SQLException 
     */ 
    @Override
    public List<DbTable> findByDbConnectionId(int dbconnectionId) throws DataAccessException, SQLException {
      Map<String, Object> params = new HashMap<>();
      params.put("dbconnection_id", String.valueOf(dbconnectionId));
      String sql = "SELECT DISTINCT dt.id, dt.name, dt.comments, dt.dbconnection_id FROM dbtables dt " +
                    "INNER JOIN dbconnections dc ON dc.id = dt.dbconnection_id " +
                    "INNER JOIN tasks t ON t.source_db_conn_id = dc.id " +
                    "INNER JOIN execution_output o ON o.task_id = t.id " +
                    "WHERE dc.id = :dbconnection_id ";

      List<DbTable> dbTables = this.namedParameterJdbcTemplate.query(sql, params, BeanPropertyRowMapper.newInstance(DbTable.class));
      return dbTables;
    }

}