package org.dbadmin.repository.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.Role;
import org.dbadmin.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRoleRepositoryImpl implements RoleRepository {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private SimpleJdbcInsert insertRole;

  @Autowired
  public JdbcRoleRepositoryImpl(DataSource dataSource,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {

    this.insertRole =
        new SimpleJdbcInsert(dataSource).withTableName("roles").usingGeneratedKeyColumns("id");

    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

  }

    /**
     * Find Role by the roleName
     * @param role_name
     * @return list of roles that matches the role name
     */
  @Override
  public Collection<Role> findByRolename(String role_name) {
    Map<String, Object> params = new HashMap<>();
    params.put("role_name", role_name + "%");
    List<Role> roles = this.namedParameterJdbcTemplate.query(
        "SELECT id, role_name, role_prefix, role_level FROM roles WHERE role_name like :role_name", params,
        BeanPropertyRowMapper.newInstance(Role.class));
    return roles;
  }

  @Override
  public List<Role> getUserRolesOfJobTransfer(int userJobId) throws DataAccessException{
    Map<String, Object> params = new HashMap<>();
    params.put("user_job_id", userJobId);
    String sql = "SELECT DISTINCT r.* " + 
        "FROM job_transfer_queue t " + 
        "INNER JOIN users u ON u.id = t.transferer " + 
        "INNER JOIN users_roles ur ON ur.user_id = u.id " + 
        "INNER JOIN roles r ON r.id = ur.role_id " + 
        "WHERE t.active = 1 AND t.user_job_id = :user_job_id ";
    List<Role> roles = this.namedParameterJdbcTemplate.query(sql, params,
        BeanPropertyRowMapper.newInstance(Role.class));
    return roles;
  }
  
  /**
   * Delete a certain role
   * @param role
   */
  @Override
  public void delete(Role role) {
      BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(role);
      this.namedParameterJdbcTemplate.update("DELETE FROM roles" + " WHERE id=:id", parameterSource);
  }

    /**
     * Insert or Update a Role
     * @param role
     */
    @Override
    public void save(Role role) {

        if (!role.getRole_name().startsWith("ROLE_")){
            String newRoleName = "ROLE_" + role.getRole_name().toUpperCase();
            role.setRole_name(newRoleName);
        }
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(role);

        if (role.isNew()) {
            Number newKey = this.insertRole.executeAndReturnKey(parameterSource);
            role.setId(newKey.intValue());
        } else {
            this.namedParameterJdbcTemplate.update(
                "UPDATE roles SET role_name=:role_name, role_prefix=:role_prefix, role_level=:role_level " + " WHERE id=:id",
                parameterSource);

        }
    }

    /**
     * Find Role by its id
     * @param id
     * @return
     */
    @Override
    public Role findRoleById(int id){
        Role role;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            role = this.namedParameterJdbcTemplate.queryForObject(
                "SELECT id, role_name, role_prefix, role_level FROM roles WHERE id= :id", params,
                BeanPropertyRowMapper.newInstance(Role.class));
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(Role.class, id);
        }
        return role;
    }

}
