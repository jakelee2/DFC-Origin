package org.dbadmin.service;

import java.util.LinkedList;
import java.util.List;

import org.dbadmin.model.Role;
import org.dbadmin.model.User;
import org.dbadmin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by Jake Lee - 4/14/16.
 */

public class UserDetailsServiceImpl implements UserDetailsService {

  private UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /*
   * This is overriding loadUserByUsername(String username) function of UserDetailsService. We can
   * see if a user of the username exists in the system or not.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    List<User> results = (List<User>) this.userRepository.findByExactUsername(username);

    if (results.size() == 0) {
      throw new UsernameNotFoundException("Username Not Found");
    } else {
      List<GrantedAuthority> grantedAuthories = new LinkedList<GrantedAuthority>();
      User user = results.get(0);

      for (Role authority : user.getRoles()) {
        grantedAuthories.add(new SimpleGrantedAuthority(authority.toString()));
      }

      UserDetails userDetails =
          (UserDetails) new org.springframework.security.core.userdetails.User(user.getUsername(),
              user.getPassword(), grantedAuthories);

      return userDetails;
    }
  }
}
