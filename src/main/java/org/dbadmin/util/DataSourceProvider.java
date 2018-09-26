package org.dbadmin.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.dbadmin.model.DbConnection;

/**
 * Provide creating or retrieving (if exists) DataSources objects
 * Contains logic for different types of DataSources: Tomcat / JBoss/ WebSphere etc.
 */
public class DataSourceProvider {

	private static Map<String, Object> dataSourceMap = new ConcurrentHashMap<String, Object>();
	private static String dataSourceKey   = "";
	private static final String TOMCAT    = "org.apache.tomcat.jdbc.pool.DataSource";
	private static final String JBOSS     = "todo: add jboss jdbc class";
	private static final String WEBSPHERE = "todo: add websphere jdbc class";
	private static final String ORACLE    = "todo: add Oracle jdbc class";

	/**
	 * @return username+databaseUrl as a joined string
	 */
	public static String getDataSourceKey() {
		return dataSourceKey;
	}


	/**
	 * Search for existing or create new DataSource object. One per unique connection.
	 * @param username
	 * @param password
	 * @param databaseUrl
	 * @param databaseJdbc e.g."com.microsoft.sqlserver.jdbc.SQLServerDriver" etc.
	 * @param clazz
	 * @return
	 */
	public static Object getDataSource(DbConnection dbConnection) {
		Object dataSource = null;
		dataSourceKey = dbConnection.toString();
		dataSource = dataSourceMap.get(dataSourceKey);

		if (dataSource == null) {
		    String className = dbConnection.getDataSourceClass();
			if (className!= null)  {
				try {
					Class<?> clazz = Class.forName(className);
					dataSource = clazz.newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					//populate it higher
					throw new RuntimeException(e); 
				} 
				switch (className) {

				case TOMCAT:
					((DataSourceProxy) dataSource).setUsername(dbConnection.getDatabaseUser());
					((DataSourceProxy) dataSource).setPassword(dbConnection.getDatabasePasswd());
					((DataSourceProxy) dataSource).setUrl(dbConnection.getDatabaseJdbcUrl());
					((DataSourceProxy) dataSource).setDriverClassName(dbConnection.getDatabaseJdbcDriver());
					break;

				case JBOSS:
				    {}
					break;

				case WEBSPHERE:
				    {}
					break;
					
                case ORACLE:
                    {}
                    break;	

				}
			}
			dataSourceMap.put(dataSourceKey, dataSource);
		}
		return dataSource;
	}

}
