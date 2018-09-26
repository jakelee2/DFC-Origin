package org.dbadmin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

  @Column(name = "username")
  @NotEmpty
  private String username;

  @Column(name = "password")
  private String password;

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Column(name = "enabled")
  @NotNull
  private Integer enabled;

  public Integer getEnabled() {
    return this.enabled;
  }

  public void setEnabled(Integer enabled) {
    this.enabled = enabled;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  private String rawPassword;

  public String getRawPassword() {
    return this.rawPassword;
  }

  public void setRawPassword(String rawPassword) {
    this.rawPassword = rawPassword;
  }


  @ManyToMany
  @JoinTable(name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id") ,
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") )
  private List<Role> roles;

  public List<Role> getRoles() {
    return this.roles;
  }

    @ManyToMany
    @JoinTable(name = "users_scripts",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id") ,
        inverseJoinColumns = @JoinColumn(name = "script_id", referencedColumnName = "id") )
    private List<ExecScript> scripts;

    public List<ExecScript> getScripts() {
        return this.scripts;
    }

    public void setScripts(List<ExecScript> scripts) {
        this.scripts = scripts;
    }

  List<String> roleIds = new ArrayList<>();

  public List<String> getRoleIds() {
    if (roleIds.isEmpty()) {
      roleIds = this.roles.stream().map(r -> r.getDisplayRole_name()).collect(Collectors.toList());
    }

    return roleIds;
  }

  public void setRoleIds(List<String> r) {
    this.roleIds = r;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this).append("username", this.getUsername())
        .append("password", this.getPassword()).append("enabled", this.getEnabled()).toString();
  }

}
