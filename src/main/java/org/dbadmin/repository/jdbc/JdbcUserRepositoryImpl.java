package org.dbadmin.repository.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.*;
import org.dbadmin.repository.UserRepository;
import org.dbadmin.util.PasswordEncoderGenerator;
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

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {


  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private SimpleJdbcInsert insertUser;
  private SimpleJdbcInsert insertUsersRoles;

  @Autowired
  public JdbcUserRepositoryImpl(DataSource dataSource,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

    this.insertUser =
        new SimpleJdbcInsert(dataSource).withTableName("users").usingGeneratedKeyColumns("id");

    this.insertUsersRoles = new SimpleJdbcInsert(dataSource).withTableName("users_roles")
        .usingGeneratedKeyColumns("id");

    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

  }

  @Override
  public Collection<User> findByUsername(String username) {
    Map<String, Object> params = new HashMap<>();
    params.put("username", username + "%");
    List<User> users = this.namedParameterJdbcTemplate.query(
        "SELECT id, username, password, enabled FROM users WHERE username like :username", params,
        BeanPropertyRowMapper.newInstance(User.class));
    loadJoinColumns(users);
    return users;
  }

  @Override
  public Collection<User> findByExactUsername(String username) {
    Map<String, Object> params = new HashMap<>();
    params.put("username", username);
    List<User> users = this.namedParameterJdbcTemplate.query(
        "SELECT id, username, password, enabled FROM users WHERE username = :username", params,
        BeanPropertyRowMapper.newInstance(User.class));
    loadJoinColumns(users);
    return users;
  }

  @Override
  public List<User> findByJobLevelAndExceptUsername(String username, int jobLevelId, boolean isAdmin) {
    Map<String, Object> params = new HashMap<>();
    params.put("username", username);
    params.put("jobLevelId", jobLevelId);
    String sql = "SELECT DISTINCT u.id, u.username, u.password, u.enabled " +
                 "FROM users u " +
                 "INNER JOIN users_roles ur ON ur.user_id = u.id " +
                 "WHERE ur.role_id <= :jobLevelId " + 
                 (isAdmin ? "":"AND u.username <> :username "); // if current user is Admin, include the name on the drop-down list
    
    List<User> users = this.namedParameterJdbcTemplate.query(sql, params,
        BeanPropertyRowMapper.newInstance(User.class));
    loadJoinColumns(users);
    return users;
  }

  @Override
  public Collection<User> findAllByExceptUsername(String username) {
    Map<String, Object> params = new HashMap<>();
    params.put("username", username);
    List<User> users = this.namedParameterJdbcTemplate.query(
        "SELECT id, username, password, enabled FROM users WHERE username <> :username", params,
        BeanPropertyRowMapper.newInstance(User.class));
    loadJoinColumns(users);
    return users;
  }

  @Override
  public void delete(User user) {
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
    this.namedParameterJdbcTemplate.update("DELETE FROM users" + " WHERE id=:id", parameterSource);
  }

  @Override
  public void save(User user) throws DataAccessException {
    user.setPassword(PasswordEncoderGenerator.encode(user.getRawPassword()));
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

    if (user.isNew()) {
      Number newKey = this.insertUser.executeAndReturnKey(parameterSource);
      user.setId(newKey.intValue());
    } else {
      if (user.getRawPassword().isEmpty()) {
        this.namedParameterJdbcTemplate.update(
            "UPDATE users SET username=:username, enabled=:enabled " + " WHERE id=:id",
            parameterSource);
      } else {
        this.namedParameterJdbcTemplate
            .update("UPDATE users SET username=:username, password=:password, enabled=:enabled "
                + " WHERE id=:id", parameterSource);
      }
    }

    this.namedParameterJdbcTemplate.update("DELETE FROM users_roles" + " WHERE user_id=:id",
        parameterSource);

    for (Role role : user.getRoles()) {
      Users_Roles ur = new Users_Roles(role.getId(), user.getId());
      BeanPropertySqlParameterSource urParameterSource = new BeanPropertySqlParameterSource(ur);
      this.insertUsersRoles.executeAndReturnKey(urParameterSource);
    }

  }

  @Override
  public User findById(int id) throws DataAccessException {
    User user;
    try {
      Map<String, Object> params = new HashMap<>();
      params.put("id", id);
      user = this.namedParameterJdbcTemplate.queryForObject(
          "SELECT id, username, password, enabled FROM users WHERE id= :id", params,
          BeanPropertyRowMapper.newInstance(User.class));
    } catch (EmptyResultDataAccessException ex) {
      throw new ObjectRetrievalFailureException(Businessrule.class, id);
    }
    loadJoinColumns(user);
    return user;
  }

  private void loadJoinColumns(List<User> users) {
    for (User user : users) {
      loadJoinColumns(user);
    }
  }


  public void loadJoinColumns(User user) {
    Map<String, Object> params = new HashMap<>();
    params.put("user_id", user.getId());
    final List<Role> roles = this.namedParameterJdbcTemplate
        .query("SELECT " + "roles.id, " + "roles.role_name " + "FROM roles, users_roles "
            + "WHERE users_roles.user_id=:user_id " + "AND users_roles.role_id=roles.id ",

            params, BeanPropertyRowMapper.newInstance(Role.class));
    user.setRoles(roles);

      final List<ExecScript> scripts = this.namedParameterJdbcTemplate
          .query("SELECT " + "scripts.id, " + "scripts.name " + "FROM scripts, users_scripts "
                  + "WHERE users_scripts.user_id=:user_id " + "AND users_scripts.script_id=scripts.id ",

              params,  new JdbcScriptRowMapper());

      user.setScripts(scripts);

  }
}
