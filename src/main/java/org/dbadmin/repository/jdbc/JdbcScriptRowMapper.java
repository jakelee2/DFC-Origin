package org.dbadmin.repository.jdbc;

import org.dbadmin.model.ExecScript;
import org.dbadmin.model.Variable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by henrynguyen on 6/28/16.
 */
public class JdbcScriptRowMapper implements RowMapper<ExecScript> {
    @Override public ExecScript mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExecScript script = new ExecScript(rs.getInt("id"), rs.getString("name"));
        return script;
    }
}
