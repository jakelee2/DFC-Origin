package org.dbadmin.repository.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dbadmin.model.User;
import org.dbadmin.repository.UserRepository;

/**
 * Created by henrynguyen on 3/14/16.
 */

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;


@Repository
public class JpaUserRepositoryImpl implements UserRepository {


  @PersistenceContext
  private EntityManager em;


  @Override
  public Collection<User> findByUsername(String username) {
    return null;
  }

  @Override
  public Collection<User> findByExactUsername(String username) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void delete(User user) {

  }

  @Override
  public void save(User user) throws DataAccessException {
    if (user.getId() == null) {
      this.em.persist(user);
    } else {
      this.em.merge(user);
    }
  }

  @Override
  public User findById(int id) throws DataAccessException {
    return null;
  }

  @Override
  public Collection<User> findAllByExceptUsername(String username) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<User> findByJobLevelAndExceptUsername(String username, int jobLevelId, boolean isAdmin) {
    // TODO Auto-generated method stub
    return null;
  }
}
