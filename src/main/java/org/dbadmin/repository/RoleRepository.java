package org.dbadmin.repository;

import java.util.Collection;
import java.util.List;

import org.dbadmin.model.Role;
import org.springframework.dao.DataAccessException;

/**
 * Created by henrynguyen on 3/14/16.
 */
public interface RoleRepository {
  Collection<Role> findByRolename(String rolename);

  void delete(Role role);

  void save(Role role);

  Role findRoleById(int id);

  List<Role> getUserRolesOfJobTransfer(int jobId) throws DataAccessException;
}
