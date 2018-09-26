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
 * properties of the {@link Businessrule} class.
 */
class JdbcBusinessruleTabColMapper implements RowMapper<Businessrule> {

  @Override
  public Businessrule mapRow(ResultSet rs, int rownum) throws SQLException {
    Businessrule brule = new Businessrule();
//    brule.setId(rs.getInt("id"));    
    brule.setSourceTableId(rs.getInt("source_table_id"));
    brule.setSourceTableColumnId(rs.getInt("source_table_column_id"));
    brule.setTargetTableId(rs.getInt("target_table_id"));
    brule.setTargetTableColumnId(rs.getInt("target_table_column_id"));
    brule.setSourceTableName(rs.getString("source_table_name"));
    brule.setTargetTableName(rs.getString("target_table_name"));
    brule.setSourceColumnName(rs.getString("source_column_name")); 
    brule.setTargetColumnName(rs.getString("target_column_name"));
    brule.setSourceDbconnectionId(rs.getInt("source_dbconnection_id"));
    brule.setTargetDbconnectionId(rs.getInt("target_dbconnection_id"));
    return brule;
  }
}
