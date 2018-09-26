package org.dbadmin.web;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dbadmin.model.DbConnection;
import org.dbadmin.model.DbTable;
import org.dbadmin.service.TemplateService;
import org.dbadmin.util.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wordnik.swagger.annotations.ApiOperation;

@Controller
public class HomeController {

	private final TemplateService templateService;
	
    @Autowired
    public HomeController(TemplateService templateService) {
        this.templateService = templateService;
    }
    
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  @ApiOperation(value = "Login page", notes = "Open login page")
  public String loginPage() {
    return "login";
  }

  /*
   * When redirected to accessDenied page, get username and its authorities. If the user's role is
   * only ROLE_NA, we set the model's "role" value to "ROLE_NA".
   */
  @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
  @ApiOperation(value = "Redirect to access denied page", notes = "Redirect to access denied page when the user is not qualified to access a page")
  public String accessDeniedPage(HttpServletRequest request, HttpServletResponse response,
      ModelMap model) {
    List<String> user_NA = (List<String>) Arrays.asList("ROLE_NA");
    String username = PrincipalUtils.getPrincipal();
    List<String> authorities = PrincipalUtils.getPrincipalAuthorities();
    model.addAttribute("user", username);
    if (authorities.size() == 1 && authorities.containsAll(user_NA)) { // when the user role is only "NA"
      model.addAttribute("role", "ROLE_NA"); // log out and go back to login page
    }
    return "accessDenied";
  }

  /*
   * Currently we are not using this logoutPage() mapping. When we call "/logout" url, spring
   * security calls "/j_spring_security_logout" automatically and logout is executed.
   */
  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  @ApiOperation(value = "Redirect to login page for logout", notes = "Redirect to login page after a user logs out")
  public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    return "redirect:/login?logout";
  }

  @RequestMapping(value="/dqstats", method = RequestMethod.GET)
  @ApiOperation(value = "Get DB tables' info", notes = "Get all the metadata about all the DB tables")
  public String dqstatsPage (HttpServletRequest request, HttpServletResponse response, ModelMap model) {
  	Collection<DbTable> DbTables = null;
   	try {
   	  DbTables = this.templateService.findDbTableByName("");
	} catch (DataAccessException e) {
	  e.printStackTrace();
	} catch (SQLException e) {
	  e.printStackTrace();
	}
   	model.addAttribute("DbTables", DbTables);    	
   	return "dqstats";
  }
  
  @RequestMapping(value="/dqoutput", method = RequestMethod.GET)
  @ApiOperation(value = "Get all the DB connections in execution_output", notes = "Get all the DB connections involved with the execution_output table")
  public String dqOutputDisplay (HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    List<DbConnection> connections = this.templateService.findAllConnectionsOfExecutionOutput();
    model.addAttribute("Connections", connections);       
    return "dqoutput";
  }
  
}
