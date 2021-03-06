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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbadmin.model.Job;
import org.springframework.jdbc.core.RowMapper;

/**
 * {@link RowMapper} implementation mapping data from a {@link ResultSet} to the corresponding
 * properties of the {@link JdbcJob} class.
 */
class JdbcRelatedJobRowMapper implements RowMapper<Job> {

  @Override
  public Job mapRow(ResultSet rs, int rownum) throws SQLException {
    Job job = new Job();
    job.setId(rs.getInt("id"));
    job.setName(rs.getString("name"));
    job.setDescription(rs.getString("description"));
    job.setStatusId(rs.getInt("status"));

    // is it correct???
    job.setPriorityInRelatedJob(rs.getInt("related_job_priority"));

    job.setLevelId(rs.getInt("level_id"));
    job.setHostJobId(rs.getInt("host_job_id"));

    return job;
  }
}
