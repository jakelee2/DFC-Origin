package org.dbadmin.repository.jdbc;

import org.dbadmin.model.*;
import org.dbadmin.repository.ScriptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by henrynguyen on 6/28/16.
 */
@Repository
public class JdbcScriptRepository implements ScriptRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertScript;
    private SimpleJdbcInsert insertScriptLog;
    private SimpleJdbcInsert insertUsersScripts;


    @Autowired
    public JdbcScriptRepository(DataSource dataSource,
        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

        this.insertScript =
            new SimpleJdbcInsert(dataSource).withTableName("scripts").usingGeneratedKeyColumns("id");

        this.insertScriptLog =
            new SimpleJdbcInsert(dataSource).withTableName("scripts_logs").usingGeneratedKeyColumns("id");

        this.insertUsersScripts = new SimpleJdbcInsert(dataSource).withTableName("users_scripts")
            .usingGeneratedKeyColumns("id");

        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

    }

    @Override public void save(ExecScript script, User user) throws DataAccessException {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(script);
        Number newKey = this.insertScript.executeAndReturnKey(parameterSource);
        script.setId(newKey.intValue());

        Users_Scripts ur = new Users_Scripts(script.getId(), user.getId());
        BeanPropertySqlParameterSource urParameterSource = new BeanPropertySqlParameterSource(ur);
        this.insertUsersScripts.executeAndReturnKey(urParameterSource);
    }

    @Override public void delete(String scriptName) throws DataAccessException {
        ExecScript script = findScriptByName(scriptName);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(script);
        this.namedParameterJdbcTemplate.update("DELETE FROM users_scripts" + " WHERE script_id=:id", parameterSource);
        this.namedParameterJdbcTemplate.update("DELETE FROM scripts" + " WHERE id=:id", parameterSource);
    }

    public void loadJoinColumns(ExecScript script) {
        Map<String, Object> params = new HashMap<>();
        params.put("script_id", script.getId());
        final List<User> users = this.namedParameterJdbcTemplate
            .query("SELECT " + "users.id, " + "users.username " + "FROM users, users_scripts "
                    + "WHERE users_scripts.user_id=:user_id " + "AND users_scripts.script_id=scripts.id ",

                params, BeanPropertyRowMapper.newInstance(User.class));

    }

    @Override public ExecScript findScriptByName(String name) throws DataAccessException {
        ExecScript script;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            script = this.namedParameterJdbcTemplate.queryForObject(
                "SELECT id, name FROM scripts WHERE name= :name", params,
                new JdbcScriptRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(ExecScript.class, name);
        }
        return script;
    }

    @Override public void save(ScriptLog script) throws DataAccessException {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(script);
        Number newKey = this.insertScriptLog.executeAndReturnKey(parameterSource);
        script.setId(newKey.intValue());
    }

    @Override public Collection<ScriptLog> findScriptLogByName(String script_name) throws DataAccessException {
        Map<String, Object> params = new HashMap<>();
        params.put("script_name", script_name);
        List<ScriptLog> scriptLogs = this.namedParameterJdbcTemplate.query(
            "SELECT id, script_name, output, started_at, completed_at, started_by FROM scripts_logs WHERE script_name <> :script_name", params,
            BeanPropertyRowMapper.newInstance(ScriptLog.class));
        return scriptLogs;
    }
}
