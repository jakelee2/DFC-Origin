package org.dbadmin.web;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dbadmin.model.Role;
import org.dbadmin.model.User;
import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/*
    Handle all Admin route
 */
@Controller
public class AdminController {

  private final TemplateService templateService;

  @Autowired
  public AdminController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @InitBinder
  public void setAllowedFields(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }

  @ModelAttribute("enableTypes")
  public List allEnabledTypes() {
    return Arrays.asList(1, 0);
  }

  @ModelAttribute("allRoles")
  public Collection<Role> allRoles() {
    return templateService.findRoleByRoleName("");
  }

  @ModelAttribute("allRoleNames")
  public Collection<String> allRoleNames() {
    return templateService.findRoleByRoleName("").stream().map(r -> r.getDisplayRole_name())
        .collect(Collectors.toList());
  }

  @RequestMapping(value = "/admin/new", method = RequestMethod.GET)
  public String initCreationForm(Map<String, Object> model) {
    User user = new User();
    model.put("user", user);
    return "admin/createOrUpdateUserForm";
  }

  @RequestMapping(value = "/admin/new", method = RequestMethod.POST)
  public String processCreationForm(@Valid User user, BindingResult result) {
    if (result.hasErrors()) {
      return "admin/createOrUpdateUserForm";
    } else {
      this.templateService.saveUser(user);
      return "redirect:/admin/" + user.getId();
    }
  }

  @RequestMapping(value = "/admin/find", method = RequestMethod.GET)
  public String initFindForm(Map<String, Object> model) {
    model.put("user", new User());
    return "admin/findUsers";
  }

  @RequestMapping(value = "/admin/user", method = RequestMethod.GET)
  public String processFindForm(User user, BindingResult result, Map<String, Object> model) {

    if (user.getUsername() == null) {
      user.setUsername(""); // empty string signifies broadest possible search
    }

    String userName = user.getUsername();
    Collection<User> results = this.templateService.findUserByUserName(userName);
    if (results.isEmpty()) {
      // no rules found
      result.rejectValue("name", "notFound", "not found");
      return "admin/findUsers";
    } else if (results.size() == 1) {
      // 1 rule found
      user = results.iterator().next();
      return "redirect:/admin/" + user.getId();
    } else {
      // multiple rules found
      model.put("selections", results);
      return "admin/usersList";
    }
  }

  @RequestMapping(value = "/admin/{userId}/edit", method = RequestMethod.GET)
  public String initUpdateUserForm(@PathVariable("userId") int userId, Model model) {
    User user = this.templateService.findUserById(userId);
    model.addAttribute(user);
    return "admin/createOrUpdateUserForm";
  }

  @RequestMapping(value = "/admin/{userId}/edit", method = RequestMethod.POST)
  public String processUpdateUserForm(@Valid User user, BindingResult result,
      @PathVariable("userId") int userId) {
    if (result.hasErrors()) {
      return "admin/createOrUpdateUserForm";
    } else {
      user.setId(userId);
      this.templateService.saveUser(user);
      return "redirect:/admin/{userId}";
    }
  }

  @RequestMapping(value = "/admin/{userId}/delete", method = RequestMethod.GET)
  public String processDeleteUserForm(@Valid User user, BindingResult result,
      @PathVariable("userId") int userId) {
    user.setId(userId);
    this.templateService.deleteUser(user);
    return "redirect:/admin/find";
  }


  @RequestMapping("/admin/{userId}")
  public ModelAndView showUser(@PathVariable("userId") int userId) {
    ModelAndView mav = new ModelAndView("admin/userDetails");
    mav.addObject(this.templateService.findUserById(userId));
    return mav;
  }

}

