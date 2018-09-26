package org.dbadmin.model;

import java.util.List;

/**
 * Created by henrynguyen on 7/28/16.
 */
public class HierachyPrefixRole {
    public String getPrefix() {
        return prefix;
    }

    public List<HierachyRole> getRoles() {
        return roles;
    }

    public String prefix;

    public HierachyPrefixRole(String prefix, List<HierachyRole> roles) {
        this.prefix = prefix;
        this.roles = roles;
    }

    public List<HierachyRole> roles;

}
