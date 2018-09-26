
package org.dbadmin.repository;

import org.dbadmin.model.DqsBounds;

/**
 * Created by Jake Lee
 */

import org.springframework.dao.DataAccessException;

/**
 * Repository class for <code>DQS</code> domain objects 
 *
 */
public interface DqsRepository {
	
  DqsBounds findByTableAndColumn(String columnName, String tableName) throws DataAccessException;

  void save(DqsBounds dqBounds) throws DataAccessException;
}


