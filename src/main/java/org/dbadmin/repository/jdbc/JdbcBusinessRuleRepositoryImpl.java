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
import org.dbadmin.repository.BusinessruleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

/**
 * JDBC-based implementation of the {@link BusinessRuleRepository} interface.
 *
 */
@Repository
public class JdbcBusinessRuleRepositoryImpl implements BusinessruleRepository {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private SimpleJdbcInsert insertRule;

  @Autowired
  public JdbcBusinessRuleRepositoryImpl(DataSource dataSource,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

    this.insertRule = new SimpleJdbcInsert(dataSource).withTableName("businessrules")
        .usingGeneratedKeyColumns("id");

    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

  }


  /**
   * Loads {@link BusinessRule BusinessRules} from the data store by name, returning all rules whose
   * names <i>contains</i> the given name; also loads the {@link Job Jobs} for the corresponding
   * rules, if not already loaded.
   */
  @Override
  public Collection<Businessrule> findByName(String name) throws DataAccessException {
    Map<String, Object> params = new HashMap<>();
    params.put("name", name + "%");
    List<Businessrule> rules = this.namedParameterJdbcTemplate.query(
        "SELECT id, name, description, body, restconnection FROM businessrules WHERE name like :name",
        params, BeanPropertyRowMapper.newInstance(Businessrule.class));
//    loadAllJobsAndEtlTasks(rules);
    return rules;
  }

  /**
   * Loads the {@link Businessrule} with the supplied <code>id</code>; also loads the {@link Job}
   * and {@link Task} for the corresponding Businessrule, if not already loaded.
   */
  @Override
  public Businessrule findById(int id) throws DataAccessException {
    Businessrule rule;
    try {
      Map<String, Object> params = new HashMap<>();
      params.put("id", id);
      rule = this.namedParameterJdbcTemplate.queryForObject(
          "SELECT "
    		  +"b.id, " 
    		  +"b.name, " 
    		  +"b.description, " 
    		  +"b.body, " 
    		  +"b.restconnection, " 
    		  +"l.source_table_id, " 
    		  +"l.source_table_column_id, " 
    		  +"t.name as source_table_name, " 
    		  +"c.name as source_column_name, "
    		  +"tt.name as target_table_name, "
    		  +"ct.name as target_column_name "
    	  +"FROM " 
    		  +"businessrules b, " 
    		  +"brule2tabs_columns_link l, " 
    		  +"dbtables t, " 
    		  +"dbcolumns c, "  
    		  +"dbtables tt, " 
    		  +"dbcolumns ct "
          +"WHERE b.id= :id "
          +"AND b.id = l.brule_id "
          +"AND l.source_table_id = t.id "
          +"AND l.source_table_column_id = c.id "    
          +"AND l.target_table_id = tt.id "
          +"AND l.target_table_column_id = ct.id ", 
          params,
          BeanPropertyRowMapper.newInstance(Businessrule.class));
    } catch (EmptyResultDataAccessException ex) {
      throw new ObjectRetrievalFailureException(Businessrule.class, id);
    }
    loadAssociatedTasks(rule); 
    return rule;
  }

  public void loadAssociatedTasks(Businessrule rule) {
    Map<String, Object> params = new HashMap<>();
    params.put("id", rule.getId());
    List<Task> associatedTasks = this.namedParameterJdbcTemplate.query(
            "SELECT " 
                + "brule2task_link.task_id, "
                + "brule2task_link.related_brule_priority as priority_in_the_task, "
                + "tasks.name, "
                + "tasks.description, "
                + "tasks.body, "
                + "tasks.type_id, "
                + "tasks.source_db_conn_id, "
                + "tasks.target_db_conn_id, "
                + "tasks.status "
                + "FROM "
                + "brule2task_link, tasks "
                + "WHERE brule2task_link.related_brule_id = :id "
                + "AND brule2task_link.task_id = tasks.id",
            params, new JdbcTask2BusinessruleRowMapper());
    rule.setAssociatedTasklist(associatedTasks);
  }

  @Override
  public void save(Businessrule rule) throws DataAccessException {
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(rule);
    if (rule.isNew()) {
      Number newKey = this.insertRule.executeAndReturnKey(parameterSource);
      rule.setId(newKey.intValue());
    } else {
      this.namedParameterJdbcTemplate.update(
          "UPDATE businessrules SET name=:name, description=:description, body=:body, restconnection=:restconnection WHERE id=:id",
          parameterSource);
    }
  }
  
  @Override
  public List<Businessrule> findByTableId(Integer tableId) throws DataAccessException {
      Map<String, Object> params = new HashMap<>();
      params.put("tableId", tableId);
      String sql = "SELECT DISTINCT br.id, br.name, br.description " +         
                    "FROM businessrules br " +
                    "INNER JOIN execution_output o ON o.businessrule_id = br.id " +
                    "INNER JOIN brule2tabs_columns_link btcl ON btcl.brule_id = o.businessrule_id " +
                    "INNER JOIN dbtables dt ON dt.id = btcl.source_table_id " +
                    "WHERE o.output_status = 'PASSED' AND dt.id = :tableId ";
      List<Businessrule> outputs = this.namedParameterJdbcTemplate.query(
              sql, params,
              BeanPropertyRowMapper.newInstance(Businessrule.class));
      return outputs;
  }


}
