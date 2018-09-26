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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.ExecutionOutput;
import org.dbadmin.repository.ExecutionOutputRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * A simple JDBC-based implementation of the {@link ExecutionOutputRepository}
 * interface.
 *
 */
@Repository
public class JdbcExecutionOutputRepository implements ExecutionOutputRepository {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcExecutionOutputRepository(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public List<ExecutionOutput> findByJobId(Integer id) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
		params.put("pid", id);
		List<ExecutionOutput> outputs = this.namedParameterJdbcTemplate.query(
				"SELECT * FROM execution_output WHERE job_id = :pid", params,
				BeanPropertyRowMapper.newInstance(ExecutionOutput.class));
		return outputs;
	}	
	
	@Override
	public List<ExecutionOutput> findByTaskId(Integer id) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
		params.put("pid", id);
		List<ExecutionOutput> outputs = this.namedParameterJdbcTemplate.query(
				"SELECT * FROM execution_output WHERE task_id = :pid", params,
				BeanPropertyRowMapper.newInstance(ExecutionOutput.class));
		return outputs;
	}

	@Override
	public List<ExecutionOutput> findByBusinessruleId(Integer id) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
		params.put("pid", id);
		List<ExecutionOutput> outputs = this.namedParameterJdbcTemplate.query(
				"SELECT * FROM execution_output WHERE businessrule_id = :pid", params,
				BeanPropertyRowMapper.newInstance(ExecutionOutput.class));
		return outputs;
	}

    @Override
    public List<ExecutionOutput> findTopByBusinessruleId(Integer tableId, Integer bruleId) throws DataAccessException {
        Map<String, Object> params = new HashMap<>();
        params.put("bruleId", bruleId);
        params.put("tableId", tableId);
        String sql = "SELECT TOP 10 o.text_output, o.list_output, o.error, o.complete_timestamp " +         
                      "FROM execution_output o " +
                      "INNER JOIN brule2tabs_columns_link btcl ON btcl.brule_id = o.businessrule_id " +
                      "INNER JOIN dbtables dt ON dt.id = btcl.source_table_id " +
                      "WHERE o.output_status = 'PASSED' AND o.businessrule_id = :bruleId  AND dt.id = :tableId " +
                      "ORDER BY o.complete_timestamp DESC ";
        List<ExecutionOutput> outputs = this.namedParameterJdbcTemplate.query(
                sql, params,
                BeanPropertyRowMapper.newInstance(ExecutionOutput.class));
        return outputs;
    }

	/**
	 * Method use auto-generated values for id and complete_timestamp fields of execution_output table
	 */
	@Override
	public void save(ExecutionOutput output) throws DataAccessException {	
		Map<String, Object> params = new HashMap<>();
	    params.put("jobId", output.getJobId());
	    params.put("businessruleId", output.getBusinessruleId());	
	    params.put("taskId", output.getTaskId());	
	    params.put("textOutput", output.getTextOutput());
	    params.put("listOutput", output.getListOutput());
	    params.put("error", output.getError());	 
	    params.put("outputStatus", output.getOutputStatus().toString());
	    params.put("comments", output.getComments());
	    String query = "INSERT INTO execution_output (job_id, businessrule_id, task_id, text_output, list_output, error, output_status, comments) VALUES (:jobId, :businessruleId, :taskId, :textOutput, :listOutput, :error, :outputStatus, :comments)";
	    this.namedParameterJdbcTemplate.update(query, params);
	}
	
}
