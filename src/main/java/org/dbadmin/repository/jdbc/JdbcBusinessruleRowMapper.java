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

import org.dbadmin.model.Businessrule;
import org.springframework.jdbc.core.RowMapper;

/**
 * {@link RowMapper} implementation mapping data from a {@link ResultSet} to the corresponding
 * properties of the {@link JdbcJob} class.
 */
class JdbcBusinessruleRowMapper implements RowMapper<Businessrule> {

  @Override
  public Businessrule mapRow(ResultSet rs, int rownum) throws SQLException {
    Businessrule rule = new Businessrule();
    rule.setId(rs.getInt("id"));
    rule.setName(rs.getString("name"));
    rule.setDescription(rs.getString("description"));
    rule.setBody(rs.getString("body"));
    rule.setRestconnection(rs.getString("restconnection"));
    rule.setConfigId(rs.getInt("config_id"));
    rule.setPriorityInHostTask(rs.getInt("related_brule_priority"));
    return rule;
  }
}
