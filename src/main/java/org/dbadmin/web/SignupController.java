package org.dbadmin.web;

import java.util.Collection;
import java.util.List;

import org.dbadmin.model.Role;
import org.dbadmin.model.User;
import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wordnik.swagger.annotations.ApiOperation;

@Controller
public class SignupController {

  private final TemplateService templateService;
  private final String DEFAULT_USER_ROLE = "ROLE_NA";

  @Autowired
  public SignupController(TemplateService templateService) {
    this.templateService = templateService;
  }

  // For ajax request from signup.jsp file.
  @RequestMapping(value = "/registration", method = RequestMethod.POST)
  @ApiOperation(value = "Sign up process", notes = "Sign up with username and password")
  @ResponseBody
  public String signupPage(@RequestParam String username, @RequestParam String password,
      @RequestParam String matchingPassword) {

    Collection<User> results = this.templateService.findUserByExactUserName(username);
    if (results.size() >= 1) {
      return "userNameExisting";
    } else if (password.length() < 3 || password.length() > 20) { // password length should be 3 ~ 20
      return "passWordLength";
    } else if (!password.equals(matchingPassword)) {
      return "passWordMismatch";
    } else {
      // need to create a new account
      User user = new User();
      user.setUsername(username);
      user.setRawPassword(password);
      List<Role> roles = (List<Role>) this.templateService.findRoleByRoleName(DEFAULT_USER_ROLE);
      user.setRoles(roles);
      user.setEnabled(1);
      this.templateService.saveUser(user);
      return "newUserCreated";
    }
  }
}
