package org.dbadmin.web;

import org.dbadmin.model.Role;
import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

/**
 * Created by henrynguyen on 7/14/16.
 */

@Controller
public class RoleController {
    private final TemplateService templateService;

    @Autowired
    public RoleController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @RequestMapping(value = "/admin/roles", method = RequestMethod.GET)
    public String processFindForm(Map<String, Object> model) {
        Collection<Role> results = this.templateService.findRoleByRoleName("");
        model.put("selections", results);
        return "roles/rolesList";
    }

    @RequestMapping(value = "/admin/roles/new", method = RequestMethod.GET)
    public String initCreationForm(Map<String, Object> model) {
        Role role = new Role();
        model.put("role", role);
        return "roles/createOrUpdateRoleForm";
    }

    @RequestMapping(value = "/admin/roles/new", method = RequestMethod.POST)
    public String processCreationForm(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            return "roles/createOrUpdateRoleForm";
        } else {
            this.templateService.save(role);
            return "redirect:/admin/roles/" + role.getId();
        }
    }

    @RequestMapping(value = "/admin/roles/{roleId}/edit", method = RequestMethod.GET)
    public String initUpdateRoleForm(@PathVariable("roleId") int roleId, Model model) {
        Role role = this.templateService.findRoleById(roleId);
        model.addAttribute(role);
        return "roles/createOrUpdateRoleForm";
    }

    @RequestMapping(value = "/admin/roles/{roleId}/edit", method = RequestMethod.POST)
    public String processUpdateRoleForm(@Valid Role role, BindingResult result,
        @PathVariable("roleId") int roleId) {
        if (result.hasErrors()) {
            return "roles/createOrUpdateRoleForm";
        } else {
            role.setId(roleId);
            this.templateService.save(role);
            return "redirect:/admin/roles/{roleId}";
        }
    }

    @RequestMapping(value = "/admin/roles/{roleId}/delete", method = RequestMethod.GET)
    public String processDeleteRoleForm(@Valid Role role, BindingResult result,
        @PathVariable("roleId") int roleId) {
        role.setId(roleId);
        this.templateService.delete(role);
        return "redirect:/admin/roles";
    }

    @RequestMapping("/admin/roles/{roleId}")
    public ModelAndView showRole(@PathVariable("roleId") int roleId) {
        ModelAndView mav = new ModelAndView("roles/roleDetails");
        mav.addObject(this.templateService.findRoleById(roleId));
        return mav;
    }



}
