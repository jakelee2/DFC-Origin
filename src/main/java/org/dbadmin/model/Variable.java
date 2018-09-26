/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.dbadmin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.core.style.ToStringCreator;

/**
 * Domain object representing an additional Variable object for ETL transformation.
 */
@Entity
@Table(name = "variables", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Variable extends BaseEntity {

  // Unique constraint applied above
  @Column(name = "name")
  @NotEmpty
  private String name;

  @Column(name = "body")
  @NotEmpty
  private String body;

  /**
   * Holds value of related Job.
   */
  @ManyToOne
  @JoinColumn(name = "job_id")
  private Job job;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public Job getJob() {
    return job;
  }

  public void setJob(Job job) {
    this.job = job;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)

        .append("id", this.getId()).append("new", this.isNew()).append("name", this.getName())
        .append("body", this.getBody()).toString();
  }

}
