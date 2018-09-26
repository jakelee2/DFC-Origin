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

import org.dbadmin.model.Variable;
import org.springframework.jdbc.core.RowMapper;

/**
 * {@link RowMapper} implementation mapping data from a {@link ResultSet} to the corresponding
 * properties of the {@link Variable} class.
 */
class JdbcVariableRowMapper implements RowMapper<Variable> {

  @Override
  public JdbcVariable mapRow(ResultSet rs, int row) throws SQLException {
    JdbcVariable variable = new JdbcVariable();
    variable.setId(rs.getInt("variable_id"));
    variable.setName(rs.getString("variable_name"));
    variable.setBody(rs.getString("variable_body"));
    variable.setJobId(rs.getInt("variable_job_id")); // foreign key

    return variable;
  }
}
