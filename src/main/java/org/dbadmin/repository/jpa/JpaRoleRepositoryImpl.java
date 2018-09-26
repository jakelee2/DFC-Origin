package org.dbadmin.repository.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dbadmin.model.Role;
import org.dbadmin.repository.RoleRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * Created by henrynguyen on 3/14/16.
 */
@Repository
public class JpaRoleRepositoryImpl implements RoleRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Collection<Role> findByRolename(String role_name) {
    return null;
  }

  @Override
  public void delete(Role role) {

  }

  @Override
  public void save(Role role) {

  }

  @Override public Role findRoleById(int id) {
    return null;
  }

  @Override
  public List<Role> getUserRolesOfJobTransfer(int jobId) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

}
