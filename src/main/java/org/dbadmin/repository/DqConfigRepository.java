package org.dbadmin.repository;

import org.dbadmin.model.DQConfig;
import org.springframework.dao.DataAccessException;

public interface DqConfigRepository {

	DQConfig findById(int id) throws DataAccessException;

	void save(DQConfig config) throws DataAccessException;

	void delete(DQConfig config) throws DataAccessException;

}
