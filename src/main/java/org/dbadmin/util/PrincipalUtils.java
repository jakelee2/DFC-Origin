package org.dbadmin.util;

import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * This Util Class is for Principal info
 */
public class PrincipalUtils {


  public static String getPrincipal() {
    String userName = null;
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof UserDetails) {
      userName = ((UserDetails) principal).getUsername();
    } else {
      userName = principal.toString();
    }
    return userName;
  }

  /*
   * Inside the SecurityContextHolder we store details of the principal currently interacting with
   * the application. Type conversion from Collection<? extends GrantedAuthority> to List<String>
   * Returns all the authorities of a user.
   */
  public static List<String> getPrincipalAuthorities() {
    List<String> authorities = new ArrayList<String>();
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (principal instanceof UserDetails) {
      for (GrantedAuthority each : ((UserDetails) principal).getAuthorities())
        authorities.add(each.toString());
    }
    return authorities;
  }
}
