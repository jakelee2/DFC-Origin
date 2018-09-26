package org.dbadmin.repository.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.DbConnection;
import org.dbadmin.repository.DbConnectionRepository;
import org.dbadmin.util.ActiveConnectionRegistry;
import org.dbadmin.util.DataSourceProvider;
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
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

/**
 * JDBC-based implementation of the {@link DbConnectionRepository} interface.
 *
 */
@Repository
public class JdbcDbConnectionRepositoryImpl implements DbConnectionRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private DataSource systemDataSource;
	private SimpleJdbcInsert insertConnection;
	private DbConnection lastFoundDbConnection;

	@Autowired
	public JdbcDbConnectionRepositoryImpl(DataSource systemDataSource) throws SQLException {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(systemDataSource);
		this.systemDataSource = systemDataSource;
		this.insertConnection = new SimpleJdbcInsert(systemDataSource).withTableName("dbconnections")
				.usingGeneratedKeyColumns("id");
	}

	/**
	 * Inner utility class
	 *
	 */
	class GetConnectionName implements DatabaseMetaDataCallback {
		/**
		 * Read DB metadata to obtain current Connection name from DataSource
		 * object
		 * 
		 * @param dbmd
		 * @return
		 * @throws SQLException
		 */
		@Override
		public Object processMetaData(DatabaseMetaData dbmd) throws SQLException {
			return dbmd.getUserName() + " on " + RegexExtractor.extractIp(dbmd.getURL().toString());
		}
	}

	/**
	 * Read active current connection of Spring DataSource object. No DB access
	 * is here.
	 */
	@Override
	public DbConnection getSystemConnection() throws DataAccessException {
		// get tables
		String connectionName = "";
		DbConnection dbc = new DbConnection();
		GetConnectionName getConnectionNames = new GetConnectionName();
		try {
			connectionName = (String) JdbcUtils.extractDatabaseMetaData(systemDataSource, getConnectionNames);
			dbc.setConnectionName(connectionName);

		} catch (MetaDataAccessException e) {
			throw new RuntimeException(e);
		}

		return dbc;
	}

	/**
	 * Read connection from DB by name.
	 */
	@Override
	public Collection<DbConnection> findByName(String name) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
		params.put("pname", name.trim());
		List<DbConnection> connections = this.namedParameterJdbcTemplate.query(
				"SELECT " 
						+ "  id" + ", connection_name" + ", database_jdbc_driver" + ", database_jdbc_url"
						+ ", database_table_pattern" + ", database_passwd" + ", database_catalog"
						+ ", database_user" + ", database_schema_pattern" + ", database_type"
						+ ", database_protocol" + ", database_table_type" + ", database_column_pattern"
						+ ", database_jpa_vendor_name" 
						+ " FROM dbconnections" 
						+ " WHERE connection_name = :pname",
				params, BeanPropertyRowMapper.newInstance(DbConnection.class));
		return connections;
	}

	/**
	 * Read connection from DB by id.
	 */
	@Override
	public DbConnection findById(int id) throws DataAccessException {
		DbConnection connection;
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("id", id);
			connection = this.namedParameterJdbcTemplate.queryForObject(
					"SELECT "  
							+ "  id" + ", connection_name" + ", database_jdbc_driver" + ", database_jdbc_url"
							+ ", database_table_pattern" + ", database_passwd" + ", database_catalog"
							+ ", database_user" + ", database_schema_pattern" + ", database_type"
							+ ", database_protocol" + ", database_table_type" + ", database_column_pattern"
							+ ", database_jpa_vendor_name" 
							+ " FROM dbconnections" 
							+ " WHERE id= :id",
					params, BeanPropertyRowMapper.newInstance(DbConnection.class));
			lastFoundDbConnection = connection;
		} catch (EmptyResultDataAccessException ex) {
			throw new ObjectRetrievalFailureException(DbConnection.class, id);
		}
		return connection;
	}

	/**
	 * Read all available connection from DB
	 */
	@Override
	public Collection<DbConnection> findAllConnections() throws DataAccessException {
		List<DbConnection> connections = this.namedParameterJdbcTemplate.query(
				"SELECT " 
						+ "  id" + ", connection_name" + ", database_jdbc_driver" + ", database_jdbc_url"
						+ ", database_table_pattern" + ", database_passwd" + ", database_catalog"
						+ ", database_user" + ", database_schema_pattern" + ", database_type"
						+ ", database_protocol" + ", database_table_type" + ", database_column_pattern"
						+ ", database_jpa_vendor_name" 
						+ " FROM dbconnections",
				BeanPropertyRowMapper.newInstance(DbConnection.class));
		return connections;
	}

    /**
     * Read all the connections which is associated with execution_output table
     */
    @Override
    public List<DbConnection> findAllConnectionsOfExecutionOutput() throws DataAccessException {
      String sql = "SELECT DISTINCT d.* FROM dbconnections d " +
                    "INNER JOIN tasks t ON t.source_db_conn_id = d.id " +
                    "INNER JOIN execution_output o ON o.task_id = t.id ";
      List<DbConnection> connections = this.namedParameterJdbcTemplate.query(sql,
          BeanPropertyRowMapper.newInstance(DbConnection.class));
      return connections;
    }

	/**
	 * Save connection to DB
	 */
	@Override
	public void save(DbConnection connection) throws DataAccessException {
		BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(connection);
		if (connection.isNew()) {
			Number newKey = this.insertConnection.executeAndReturnKey(parameterSource);
			connection.setId(newKey.intValue());
		} else {
		    int result = 0;
			result = this.namedParameterJdbcTemplate.update("UPDATE dbconnections SET "

					+ "connection_name=:connectionName, " + "database_jdbc_driver=:databaseJdbcDriver, "
					+ "database_jdbc_url=:databaseJdbcUrl, " + "database_table_pattern=:databaseTablePattern, "
					+ "database_passwd=:databasePasswd, " + "database_catalog=:databaseCatalog, "
					+ "database_user=:databaseUser, " + "database_schema_pattern=:databaseSchemaPattern, "
					+ "database_type=:databaseType, " + "database_protocol=:databaseProtocol, "
					+ "database_table_type=:databaseTableType, " + "database_column_pattern=:databaseColumnPattern, "
					+ "database_jpa_vendor_name=:databaseJpaVendorName "

					+ "WHERE id=:id", parameterSource);
		    if (result == 0) {
		      throw new IllegalArgumentException("Retured 0 affected rows - can not create connection");
		    }
		}
	}

	/**
	 * Delete connection from DB
	 */
	@Override
	public void delete(DbConnection connection) throws DataAccessException {
		BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(connection);
		int resultLines = 0;
		resultLines = this.namedParameterJdbcTemplate.update("DELETE FROM dbconnections" + " WHERE id=:id", parameterSource);
		this.namedParameterJdbcTemplate.update("DELETE FROM users_connections" + " WHERE connection_id=:id", parameterSource);	
		if (resultLines == 0) {
		  // it is enough to test only main connection table - dbconnections.
		  throw new IllegalArgumentException("Retured 0 affected rows - can not delete connection");
		}
	}

	/**
	 * Read favorite connections for particular User by User Id from DB
	 */
	@Override
	public Collection<DbConnection> findFavoriteConnections(int userId) throws DataAccessException {
		Map<String, Object> namedParameters = new HashMap<>();
		namedParameters.put("userIdParameter", userId);
		List<DbConnection> connections = this.namedParameterJdbcTemplate.query(
				"SELECT " 
						+ "  dbconnections.id" 
						+ ", dbconnections.connection_name" 
						+ ", dbconnections.database_jdbc_driver" 
						+ ", dbconnections.database_jdbc_url"
						+ ", dbconnections.database_table_pattern" 
						+ ", dbconnections.database_passwd" 
						+ ", dbconnections.database_catalog"
						+ ", dbconnections.database_user" 
						+ ", dbconnections.database_schema_pattern" 
						+ ", dbconnections.database_type"
						+ ", dbconnections.database_protocol" 
						+ ", dbconnections.database_table_type" 
						+ ", dbconnections.database_column_pattern"
						+ ", dbconnections.database_jpa_vendor_name" 
						+ " FROM dbconnections, users_connections "
						+ " WHERE dbconnections.id = users_connections.connection_id "
						+ " AND users_connections.user_id = :userIdParameter",
				namedParameters,	
				BeanPropertyRowMapper.newInstance(DbConnection.class));
		return connections;
	}		

	/**
	 * Add connection to the list of favorite connections for particular User and save it to DB
	 */
	@Override
	public void add2favorite(int connectionId, int userId) throws RuntimeException {
		String query = "INSERT INTO users_connections (user_id, connection_id) VALUES (?, ?)";
		if (!hasUserConnectionEntry(connectionId, userId)) {
			try (Connection conn = systemDataSource.getConnection();
				 PreparedStatement ps = conn.prepareStatement(query)) 
			{
				ps.setInt(1, userId);
				ps.setInt(2, connectionId);
				ps.executeUpdate();
				ps.close();
				conn.close(); //will return connection to pool

			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	
	// This is reference implementation for try-with-resources block for JDBC.
	// TODO refactor all other code to this pattern
	// D.K. 9/21/2016
	/**
	 * Tests user_connection DB table for existing of pair connectionId-userId,
	 * therefore avoids duplicates.
	 * 
	 * @param connectionId
	 * @param userId
	 * @return boolean
	 */
	private boolean hasUserConnectionEntry(int connectionId, int userId) {
		try (Connection conn = systemDataSource.getConnection();
			 PreparedStatement ps = createUserConnectionEntryPreparedStatement(conn, userId, connectionId);
			 ResultSet rs = ps.executeQuery()) {
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return true;

	}

	private PreparedStatement createUserConnectionEntryPreparedStatement(Connection conn, int userId, int connectionId)
			throws SQLException {
		final String query = "SELECT id FROM users_connections WHERE user_id = ? AND connection_id = ?";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1, userId);
		ps.setInt(2, connectionId);
		return ps;

	}
	
	
	
	/**
	 * Test, and if ok, then add selected connection to Active Connection Registry
	 * @throws SQLException 
	 * @see ActiveConnectionRegistry
	 */
	@Override
	public void activateDbConnection(int connectionId, int authenticatedUserId) throws SQLException {
		try {
			DbConnection dbConn = findById(connectionId);
			Connection conn = testAndGetConnection(dbConn);
//			conn.close(); // return to the pull?
	        ActiveConnectionRegistry.addActiveConnection(this.lastFoundDbConnection);
	        this.lastFoundDbConnection = dbConn;
		} catch (SQLException ex) {}

	}
	
	

	@Override
	public Connection testAndGetConnection(DbConnection dbConnection) throws SQLException {
		Connection conn;
		dbConnection.setDataSourceClass(systemDataSource.getClass().getName());
		DataSource testedDataSource = (DataSource) DataSourceProvider.getDataSource(dbConnection);
		
		// throws exception here if cannot connect
		conn = testedDataSource.getConnection(); 
		
		return conn;
	}

  @Override
  public void deactivateDbConnection(int connectionId) throws SQLException {
    ActiveConnectionRegistry.INSTANCE.deleteActiveConnection(String.valueOf(connectionId));
  }

  @Override
  public Collection<DbConnection> findActiveConnections() {
    return ActiveConnectionRegistry.INSTANCE.getAllActiveConnections();
  }
}
