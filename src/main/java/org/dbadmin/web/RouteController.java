package org.dbadmin.web;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dbadmin.handler.AccessDecisionManagerImpl;
import org.dbadmin.model.HierachyPrefixRole;
import org.dbadmin.model.HierachyRole;
import org.dbadmin.model.Role;
import org.dbadmin.model.Route;
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

/**
 * Created by henrynguyen on 5/9/16.
 */

@Controller
public class RouteController {

  private final TemplateService templateService;

  @Autowired
  public RouteController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @InitBinder
  public void setAllowedFields(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }

    private List<HierachyPrefixRole> getHierachyRoutes(Collection<Route> routes){

        // Get all Roles
        Collection<Role> allRoles = templateService.findRoleByRoleName("");

        Map<String, List<HierachyRole>> prefixMap = new HashMap<>();
        Role permitAllRole = HierachyRole.createPermitAllRole();

        for(Route route : routes) {
            String role_name = route.getAccess();

            // Permit All Case
            if (role_name.compareToIgnoreCase(AccessDecisionManagerImpl.PERMIT_ALL) == 0) {
                HierachyRole hierachyRole = new HierachyRole(AccessDecisionManagerImpl.PERMIT_ALL, route, permitAllRole);
                HierachyRole.addRoleToMap(prefixMap, hierachyRole);
            } else { // get other roles and prefix
                Collection<Role> roles = allRoles.stream()
                    .filter(r -> r.getRole_name().compareToIgnoreCase(role_name) == 0)
                    .collect(Collectors.toList());
                for (Role role : roles) {
                    if (role.getRole_name().compareToIgnoreCase(role_name) == 0) {
                        String prefix = role.getRole_prefix();
                        HierachyRole hierachyRole = new HierachyRole(prefix, route, role);
                        HierachyRole.addRoleToMap(prefixMap, hierachyRole);
                    }
                }
            }
        }

        return prefixMap.keySet().stream().map(k -> new HierachyPrefixRole(k, prefixMap.get(k))).collect(Collectors.toList());
    }

  @ModelAttribute("allRoleNames")
  public Collection<String> allRoleNames() {
    List roles = templateService.findRoleByRoleName("").stream().map(r -> r.getRole_name())
        .collect(Collectors.toList());
    roles.add(AccessDecisionManagerImpl.PERMIT_ALL);
    return roles;
  }

  @RequestMapping(value = "/admin/route/new", method = RequestMethod.GET)
  public String initCreationForm(Map<String, Object> model) {
    Route route = new Route();
    model.put("route", route);
    return "route/createOrUpdateRouteForm";
  }

  @RequestMapping(value = "/admin/route/new", method = RequestMethod.POST)
  public String processCreationForm(@Valid Route route, BindingResult result) {
    if (result.hasErrors()) {
      return "route/createOrUpdateRouteForm";
    } else {
      this.templateService.saveRoute(route);
      return "redirect:/admin/route/" + route.getId();
    }
  }

  @RequestMapping(value = "/admin/route/find", method = RequestMethod.GET)
  public String initFindForm(Map<String, Object> model) {
    model.put("route", new Route());
    return "route/findRoutes";
  }

  @RequestMapping(value = "/admin/route", method = RequestMethod.GET)
  public String processFindForm(Route route, BindingResult result, Map<String, Object> model) {

    if (route.getUrl() == null) {
      route.setUrl(""); // empty string signifies broadest possible search
    }

    Collection<Route> results = this.templateService.findRouteByUrl(route.getUrl());
    if (results.isEmpty()) {
      // no rules found
      result.rejectValue("route", "notFound", "not found");
      return "route/findRoutes";
    } else if (results.size() == 1) {
      // 1 rule found
      route = results.iterator().next();
      return "redirect:/admin/route" + route.getId();
    } else {
      // multiple rules found
      model.put("selections", results);
      return "route/routesList";
    }
  }


    @RequestMapping(value = "/admin/route/hierachy", method = RequestMethod.GET)
    public String hierachyRoute(Route route, BindingResult result, Map<String, Object> model) {
        Collection<Route> allRoutes = this.templateService.findRouteByUrl("");
        Collection<HierachyPrefixRole> results = getHierachyRoutes(allRoutes);
        model.put("selections", results);
        return "route/hierachyRoutes";

    }

  @RequestMapping(value = "/admin/route/{routeId}/edit", method = RequestMethod.GET)
  public String initUpdateRouteForm(@PathVariable("routeId") int routeId, Model model) {
    Route route = this.templateService.findRouteById(routeId);
    model.addAttribute(route);
    return "route/createOrUpdateRouteForm";
  }

  @RequestMapping(value = "/admin/route/{routeId}/edit", method = RequestMethod.POST)
  public String processUpdateRouteForm(@Valid Route route, BindingResult result,
      @PathVariable("routeId") int routeId) {
    if (result.hasErrors()) {
      return "route/createOrUpdateRouteForm";
    } else {
      route.setId(routeId);
      this.templateService.saveRoute(route);
      return "redirect:/admin/route/{routeId}";
    }
  }

  @RequestMapping(value = "/admin/route/{routeId}/delete", method = RequestMethod.GET)
  public String processDeleteRouteForm(@Valid Route route, BindingResult result,
      @PathVariable("routeId") int routeId) {
    route.setId(routeId);
    this.templateService.deleteRoute(route);
    return "redirect:/admin/find";
  }


  @RequestMapping("/admin/route/{routeId}")
  public ModelAndView showRoute(@PathVariable("routeId") int routeId) {
    ModelAndView mav = new ModelAndView("route/routeDetails");
    mav.addObject(this.templateService.findRouteById(routeId));
    return mav;
  }

}

