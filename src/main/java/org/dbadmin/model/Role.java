package org.dbadmin.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by henrynguyen on 3/14/16.
 */
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

  @Column(name = "role_name")
  @NotEmpty
  private String role_name;

    public String getRole_prefix() {
        return role_prefix;
    }

    public void setRole_prefix(String role_prefix) {
        this.role_prefix = role_prefix;
    }

    public Integer getRole_level() {
        return role_level;
    }

    public void setRole_level(Integer role_level) {
        this.role_level = role_level;
    }

    @Column(name = "role_prefix")
    private String role_prefix;

    @Column(name = "role_level")
    private Integer role_level;

  public String getRole_name() {
    return this.role_name;
  }

  public String getDisplayRole_name() {
    return this.role_name.replace("ROLE_", "");
  }

  public void setRole_name(String rolename) {
    this.role_name = rolename;
  }

  @ManyToMany(mappedBy = "scripts")
  private Collection<User> users;

  public Collection<User> getUsers() {
    return users;
  }

  public void setUsers(final Collection<User> users) {
    this.users = users;
  }

}
