package org.dbadmin.repository;

import java.util.Collection;
import java.util.List;

import org.dbadmin.model.User;
import org.springframework.dao.DataAccessException;

/**
 * Created by henrynguyen on 3/14/16.
 */
public interface UserRepository {
  Collection<User> findByUsername(String username);

  Collection<User> findByExactUsername(String username);
  
  Collection<User> findAllByExceptUsername(String username);
  
  void delete(User user);

  void save(User user) throws DataAccessException;

  User findById(int id) throws DataAccessException;

  List<User> findByJobLevelAndExceptUsername(String username, int jobLevelId, boolean isAdmin);
}
