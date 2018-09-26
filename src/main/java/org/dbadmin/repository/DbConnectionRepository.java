/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dbadmin.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.dbadmin.model.DbConnection;
import org.springframework.dao.DataAccessException;

/**
 * Repository class for <code>DbConnection</code> domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data See here: http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 */
public interface DbConnectionRepository {

	DbConnection getSystemConnection() throws DataAccessException;

	Collection<DbConnection> findAllConnections() throws DataAccessException;

	List<DbConnection> findAllConnectionsOfExecutionOutput() throws DataAccessException;

	Collection<DbConnection> findByName(String name) throws DataAccessException;

	DbConnection findById(int id) throws DataAccessException;

	void save(DbConnection connection) throws DataAccessException;

	void delete(DbConnection connection) throws DataAccessException;

	void add2favorite(int connectionId, int userId) throws DataAccessException, RuntimeException;

	Collection<DbConnection> findFavoriteConnections(int userId) throws DataAccessException;

	void activateDbConnection(int connectionId, int authenticatedUserId) throws SQLException;

	/**
	 * Tests database Connection and return the object with particular properties found in DbConnection object.
	 * @param dbConnection
	 * @return SQL Connection
	 * @throws SQLException - if cannot connect
	 */
	Connection testAndGetConnection(DbConnection dbConnection) throws SQLException;

    void deactivateDbConnection(int connectionId) throws SQLException;

    Collection<DbConnection> findActiveConnections();
	
}