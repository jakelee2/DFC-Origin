package org.dbadmin.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "users_roles")
public class Users_Roles extends BaseEntity {

  @Column(name = "user_id")
  @NotEmpty
  private Integer user_id;

  public Integer getUser_id() {
    return this.user_id;
  }

  public void setUser_id(Integer user_id) {
    this.user_id = user_id;
  }

  @Column(name = "role_id")
  private Integer role_id;

  public Integer getRole_id() {
    return this.role_id;
  }

  public void setRole_id(Integer role_id) {
    this.role_id = role_id;
  }

  public Users_Roles(Integer role_id, Integer user_id) {
    this.role_id = role_id;
    this.user_id = user_id;
  }
}
