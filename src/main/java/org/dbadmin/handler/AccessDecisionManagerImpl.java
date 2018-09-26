package org.dbadmin.handler;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.dbadmin.model.Role;
import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 *  Created by henrynguyen on 5/5/16.
 *  This class to decide whether an URL is allowed by a user and its role.
 */
public class AccessDecisionManagerImpl implements AccessDecisionManager {

  public static final String PERMIT_ALL = "permitAll";

    private final TemplateService templateService;

    @Autowired
    public AccessDecisionManagerImpl(TemplateService templateService) {
        this.templateService = templateService;
    }

  // In this method, need to compare authentication with configAttributes.
  // 1, A object is a URL, a filter was find permission configuration by this URL, and pass to here.
  // 2, Check authentication has attribute in permission configuration (configAttributes)
  // 3, If not match corresponding authentication, throw a AccessDeniedException.
  public void decide(Authentication authentication, Object object,
      Collection<ConfigAttribute> configAttributes)
          throws AccessDeniedException, InsufficientAuthenticationException {
    if (configAttributes == null) {
      return;
    }
    Iterator<ConfigAttribute> ite = configAttributes.iterator();
    while (ite.hasNext()) {
      ConfigAttribute ca = ite.next();
      String needRole = ca.getAttribute();
      for (GrantedAuthority ga : authentication.getAuthorities()) {
        if (isAllow(needRole, ga.getAuthority())) {
          return;
        }
      }
    }
    throw new AccessDeniedException("no right");
  }

    private boolean isAllow(String needRole, String hasRole) {
        if (needRole.compareToIgnoreCase(PERMIT_ALL) == 0 || needRole.equals(hasRole))
            return true;
        Map<String, Role> map = templateService.getRolesMap();
        if (!map.containsKey(needRole) || !map.containsKey(hasRole))
            return false;
        Role need = map.get(needRole);
        Role has = map.get(hasRole);
        try {
            if (need.getRole_prefix().matches(has.getRole_prefix()) && need.getRole_level() <= has.getRole_level()) {
                return true;
            }
        } catch (Exception e){

        }
        return false;
    }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }


}
