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

import org.dbadmin.model.Businessrule;
import org.dbadmin.model.Job;
import org.dbadmin.model.Task;
import org.dbadmin.model.Type;
import org.dbadmin.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

/**
 * A simple JDBC-based implementation of the {@link TaskRepository} interface.
 *
 */
@Repository
public class JdbcTaskRepositoryImpl implements TaskRepository {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private SimpleJdbcInsert insertTask;

  private JdbcTemplate jdbcTemplate;


  @Autowired
  public JdbcTaskRepositoryImpl(DataSource dataSource) {
    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    this.jdbcTemplate = new JdbcTemplate(dataSource);

    this.insertTask =
        new SimpleJdbcInsert(dataSource).withTableName("tasks").usingGeneratedKeyColumns("id");

  }

  @Override
  public List<Type> findTaskTypes() throws DataAccessException {
    Map<String, Object> params = new HashMap<>();
    return this.namedParameterJdbcTemplate.query("SELECT type_id, name FROM types ORDER BY type_id", params,
        BeanPropertyRowMapper.newInstance(Type.class));
  }


  @Override
  public void save(Task task) throws DataAccessException {
    if (task.isNew()) {
      Number newKey = this.insertTask.executeAndReturnKey(createTaskParameterSource(task));
      task.setId(newKey.intValue());
      task.setStatusId(1); //CREATED
    } else {
      this.namedParameterJdbcTemplate.update(
          "UPDATE tasks "
          + "SET name=:name, "
          + "description=:description, "
          + "body =:body, "
          + "type_id=:type_id, "
          + "source_db_conn_id=:source_db_conn_id, "
          + "target_db_conn_id=:target_db_conn_id, "
          + "status =:status "         
          + "WHERE id=:id",
          createTaskParameterSource(task));
    }
  }

  
  @Override
  public void updateTaskStatus(Task task, int statusId) throws DataAccessException {
    
    Map<String, Object> params = new HashMap<>();
    params.put("id", task.getId());
    params.put("status", statusId);
    
    if (task.isNew()) {
      Number newKey = this.insertTask.executeAndReturnKey(createTaskParameterSource(task));
      task.setId(newKey.intValue());
      task.setStatusId(statusId);
    } else {
      this.namedParameterJdbcTemplate.update(
          "UPDATE tasks "
          + "SET "
          + "status =:status "         
          + "WHERE id=:id",
          createTaskParameterSource(task));
    }
  }

  /**
   * Creates a {@link MapSqlParameterSource} based on data values {@link Task} instance.
   */
  private MapSqlParameterSource createTaskParameterSource(Task task) {
    return new MapSqlParameterSource()
        .addValue("id", task.getId())
        .addValue("name", task.getName())
        .addValue("description", task.getDescription())
        .addValue("body", task.getBody())
        .addValue("type_id", task.getTypeId())
        .addValue("source_db_conn_id", task.getSourceDbConnId())    
        .addValue("target_db_conn_id", task.getTargetDbConnId())
        .addValue("status", task.getStatusId());
  }

  @Override
  public List<Task> findByJobId(Integer jobId) {
    String query = 
        "SELECT " 
        + "jobs.id as job_id, "
        + "tasks.id as task_id, " 
        + "tasks.name as name, "
        + "tasks.description as description, " 
        + "tasks.body as body, "
        + "tasks.type_id as type_id, "
        + "tasks.source_db_conn_id as source_db_conn_id, " 
        + "tasks.target_db_conn_id as target_db_conn_id, "     
        + "tasks.status as status, " 
        + "task2job_link.related_task_priority as priority_in_related_job "
        + "FROM jobs, tasks, task2job_link "
        + "WHERE job_id=? " 
        + "AND jobs.id = task2job_link.job_id "
        + "AND tasks.id = task2job_link.related_task_id";
    
    List<Task> list = this.jdbcTemplate.query(query, new JdbcJob2TaskRowMapper(), jobId);
    System.out.println(query + jobId);

    return list;
  }

  @Override
  public Collection<Task> findByName(String name) throws DataAccessException {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name + "%");
    List<Task> tasks = this.namedParameterJdbcTemplate.query(
        "SELECT id, name, description, body, type_id, source_db_conn_id, target_db_conn_id, status FROM tasks WHERE name like :name",
        params, BeanPropertyRowMapper.newInstance(Task.class));
    return tasks;
  }

  @Override
  public Task findById(int id) throws DataAccessException {
    Task task = new Task();
    try {
      Map<String, Object> params = new HashMap<>();
      params.put("id", id); //
      task = this.namedParameterJdbcTemplate.queryForObject(
          "SELECT id, name, description, body, type_id, source_db_conn_id, target_db_conn_id, status as status_id FROM tasks WHERE id=:id",
          params, BeanPropertyRowMapper.newInstance(Task.class));
      List<Businessrule> relatedBusinessrules = this.findRelatedBusinessrules(id);
      task.setBusinessruleList(relatedBusinessrules);
      List<Job> associatedJobsList = this.findAccotiatedJobs(id);
      task.setAssociatedJobsList(associatedJobsList);
      
    } catch (EmptyResultDataAccessException ex) {
      throw new ObjectRetrievalFailureException(Task.class, id);
    }
    return task;
  }

  @Override
  public List<Job> findAccotiatedJobs(Integer taskId) {
    String query = 
        "SELECT " 
        + "jobs.id, "
        + "jobs.name, "
        + "jobs.description, " 
        + "jobs.status, "
        + "jobs.level_id, "
        + "jobs.id as related_job_priority " // TODO consider logic here 
        + "FROM jobs, task2job_link "
        + "WHERE task2job_link.related_task_id = ? " 
        + "AND task2job_link.job_id = jobs.id";
    
    List<Job> list = this.jdbcTemplate.query(query, new JdbcJobRowMapper(), taskId);

    return list;
  }
  
  
  @Override
  public List<Businessrule> findRelatedBusinessrules(Integer taskId) {
    String query = 
        "SELECT " 
        + "businessrules.id, "
        + "businessrules.name, "
        + "businessrules.description, "
        + "businessrules.body, "
        + "businessrules.restconnection, "
        + "businessrules.config_id, "        
        + "brule2task_link.related_brule_priority "
        + "FROM businessrules, brule2task_link "
        + "WHERE brule2task_link.task_id=? "
        + "AND brule2task_link.related_brule_id = businessrules.id";
    
    List<Businessrule> list = this.jdbcTemplate.query(query, new JdbcBusinessruleRowMapper(), taskId);

    return list;
  }
}
