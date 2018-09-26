package org.dbadmin.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * Created by Jake Lee - 4/13/16.
 * 
 * This handler is used to distinguish what was the reason of login failure.
 */

public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  /*
   * Overriding the onAuthenticationFailure function. We display error message at the login page
   * depending to the Exception type.
   */
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    if (exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
      response.sendRedirect("login?error=notfound");
    } else if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
      response.sendRedirect("login?error=badcredential");
    } else {
      response.sendRedirect("login?error=true");
    }
  }
}
