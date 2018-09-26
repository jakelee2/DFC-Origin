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

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Simple JavaBean domain object with an id property. Used as a base class for objects needing this
 * property.
 *
 * For Hibernate ORM: Hibernate offers various generation strategies, let's explore the most common
 * ones first that happens to be standardized by JPA: IDENTITY: supports identity columns in DB2,
 * MySQL, MS SQL Server, Sybase and HypersonicSQL. The returned identifier is of type long, short or
 * int. SEQUENCE uses a sequence in DB2, PostgreSQL, Oracle, SAP DB, McKoi or a generator in
 * Interbase. The returned identifier is of type long, short or int. TABLE (called
 * MultipleHiLoPerTableGenerator in Hibernate) : uses a hi/lo algorithm to efficiently generate
 * identifiers of type long, short or int, given a table and column as a source of hi values. The
 * hi/lo algorithm generates identifiers that are unique only for a particular database. AUTO:
 * selects IDENTITY, SEQUENCE or TABLE depending upon the capabilities of the underlying database.
 */
@MappedSuperclass
public class BaseEntity {
  @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public boolean isNew() {
    return (this.id == null);
  }

}
