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
package org.dbadmin.repository.jdbc;

/**
 * Created by Jake Lee 
 */

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.DqsBounds;
import org.dbadmin.repository.DqsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * A simple JDBC-based implementation of the {@link VisitRepository} interface.
 *
 */
@Repository
public class JdbcDqsRepositoryImpl implements DqsRepository {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private SimpleJdbcInsert insertDqsBound;
    
  @Autowired
  public JdbcDqsRepositoryImpl(DataSource dataSource) {
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    
    this.insertDqsBound = new SimpleJdbcInsert(dataSource)
      .withTableName("dqs_bounds")
      .usingGeneratedKeyColumns("id");
  }

  @Override
  public void save(DqsBounds dqBounds) throws DataAccessException {
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(dqBounds);
    
    if(findByTableAndColumn(dqBounds.getTableName(), dqBounds.getColumnName()) == null){ // INSERT
      Number newKey = this.insertDqsBound.executeAndReturnKey(parameterSource);
      dqBounds.setId(newKey.intValue());
    }
    else{	//UPDATE
      this.namedParameterJdbcTemplate.update(
        "UPDATE dqs_bounds SET min_val = :minVal, max_val = :maxVal " + 
        "WHERE table_name = :tableName AND column_name = :columnName ",
    	parameterSource);
   	}
  }

  @Override
  public DqsBounds findByTableAndColumn(String tableName, String columnName) throws DataAccessException {
	DqsBounds dqsb = null;
  	try {
  	  Map<String, Object> params = new HashMap<>();
  	  params.put("tableName", tableName);
  	  params.put("columnName", columnName);
  	  dqsb = this.namedParameterJdbcTemplate.queryForObject(
  	    "SELECT id, table_name, column_name, min_val, max_val " +
  	    "FROM dqs_bounds " +
		"WHERE table_name = :tableName and column_name = :columnName",
	    params,
	    BeanPropertyRowMapper.newInstance(DqsBounds.class)
	  );
    } catch (Exception ex) {
      return dqsb;
    }
    return dqsb;
  }

}
