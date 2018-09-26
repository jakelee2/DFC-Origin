package org.dbadmin.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by henrynguyen on 5/6/16.
 */

@Entity
@Table(name = "routes")
public class Route extends BaseEntity {

  @Column(name = "url")
  private String url;

  @Column(name = "access")
  private String access;

  @Column(name = "priority")
  @NotNull
  private Integer priority;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getAccess() {
    return access;
  }

  public void setAccess(String access) {
    this.access = access;
  }

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

}
