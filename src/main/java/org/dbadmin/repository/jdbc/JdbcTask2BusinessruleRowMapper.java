/*
 * Copyright 2002-2015 the original author or authors.
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


import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbadmin.model.Task;
import org.springframework.jdbc.core.RowMapper;

/**
 * {@link RowMapper} implementation mapping data from a {@link ResultSet} to the corresponding
 * properties of the {@link Task} class.
 */
class JdbcTask2BusinessruleRowMapper implements RowMapper<Task> {

  @Override
  public Task mapRow(ResultSet rs, int row) throws SQLException {
    Task task = new Task();
    task.setId(rs.getInt("task_id"));
    task.setName(rs.getString("name"));
    task.setDescription(rs.getString("description"));
    task.setBody(rs.getString("body"));
    task.setTypeId(rs.getInt("type_id"));
    task.setSourceDbConnId(rs.getInt("source_db_conn_id"));
    task.setTargetDbConnId(rs.getInt("target_db_conn_id")); 
    task.setStatusId(rs.getInt("status"));
//    task.setParentJobId(rs.getInt("job_id"));
//    task.setPriorityInRelatedJob(rs.getInt("priority_in_related_job"));

    return task;
  }
}
