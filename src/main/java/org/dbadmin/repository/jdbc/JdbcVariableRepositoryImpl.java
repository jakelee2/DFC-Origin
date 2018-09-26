/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.dbadmin.repository.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.Task;
import org.dbadmin.model.Variable;
import org.dbadmin.repository.VariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * JDBC-based implementation of the {@link VariableRepository} interface.
 *
 */
@Repository
public class JdbcVariableRepositoryImpl implements VariableRepository {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private SimpleJdbcInsert insertTask;

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcVariableRepositoryImpl(DataSource dataSource) {
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

    this.insertTask =
        new SimpleJdbcInsert(dataSource).withTableName("variables").usingGeneratedKeyColumns("id");

  }

  @Override
  public Collection<Task> findByName(String name) throws DataAccessException {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name + "%");
    List<Task> tasks = this.namedParameterJdbcTemplate.query(
        "SELECT id, name, description, body FROM tasks WHERE name like :name", params,
        BeanPropertyRowMapper.newInstance(Task.class));
    return tasks;
  }

  @Override
  public void save(Variable variable) throws DataAccessException {
    if (variable.isNew()) {
      Number newKey = this.insertTask.executeAndReturnKey(createVariableParameterSource(variable));
      variable.setId(newKey.intValue());
    } else {
      this.namedParameterJdbcTemplate.update(
          "UPDATE variables SET name=:name, body =:body, " + "job_id=:job_id WHERE id=:id",
          createVariableParameterSource(variable));
    }
  }


  /**
   * Creates a {@link MapSqlParameterSource} based on data values {@link Task} instance.
   */
  private MapSqlParameterSource createVariableParameterSource(Variable variable) {
    return new MapSqlParameterSource().addValue("id", variable.getId())
        .addValue("name", variable.getName()).addValue("body", variable.getBody())
        .addValue("job_id", variable.getJob().getId());
  }

  @Override
  public List<Variable> findByJobId(Integer jobId) {
    return this.jdbcTemplate.query(
        "SELECT id as variable_id, name as variable_name, body as variable_body, job_id as variable_job_id FROM variables WHERE job_id=?",
        new JdbcVariableRowMapper(), jobId);
  }
}
