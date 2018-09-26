package org.dbadmin.model;

import org.dbadmin.handler.AccessDecisionManagerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by henrynguyen on 7/28/16.
 */
public class HierachyRole {

    public HierachyRole(String prefix, Route route, Role role) {
        this.prefix = prefix;
        this.route = route;
        this.role = role;
    }

    public String prefix;
    public Route route;
    public Role role;

    public String getRouteUrl(){
        return route.getUrl();
    }

    public String getRoleLevel(){
        return role.getRole_level().toString();
    }

    public String getRoleName(){
        return role.getDisplayRole_name();
    }

    public String getRouteId(){
        return route.getId().toString();
    }

    public static void addRoleToMap(Map<String, List<HierachyRole>> prefixMap, HierachyRole hierachyRole){
        List<HierachyRole> hierachyRoles = prefixMap.containsKey(hierachyRole.prefix) ? prefixMap.get(hierachyRole.prefix) : new ArrayList<>();
        hierachyRoles.add(hierachyRole);
        prefixMap.put(hierachyRole.prefix, hierachyRoles);
    }

    public static Role createPermitAllRole(){
        Role permitAllRole = new Role();
        permitAllRole.setRole_name(AccessDecisionManagerImpl.PERMIT_ALL);
        permitAllRole.setRole_prefix(AccessDecisionManagerImpl.PERMIT_ALL);
        permitAllRole.setRole_level(0);
        return permitAllRole;
    }
}
