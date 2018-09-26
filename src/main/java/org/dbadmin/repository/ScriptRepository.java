package org.dbadmin.repository;

import org.dbadmin.model.ExecScript;
import org.dbadmin.model.ScriptLog;
import org.dbadmin.model.User;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

/**
 * Created by henrynguyen on 6/28/16.
 */
public interface ScriptRepository {

    void save(ExecScript script, User user) throws DataAccessException;
    void delete(String scriptName) throws DataAccessException;

    ExecScript findScriptByName(String name) throws DataAccessException;

    void save(ScriptLog script) throws DataAccessException;
    Collection<ScriptLog> findScriptLogByName(String script_name) throws DataAccessException;
}
