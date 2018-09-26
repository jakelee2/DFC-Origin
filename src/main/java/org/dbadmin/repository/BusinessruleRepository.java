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
package org.dbadmin.repository;

import java.util.Collection;
import java.util.List;

import org.dbadmin.model.BaseEntity;
import org.dbadmin.model.Businessrule;
import org.springframework.dao.DataAccessException;

/**
 * Repository class for <code>Businessrule</code> domain objects All method names are compliant with
 * Spring Data naming conventions so this interface can easily be extended for Spring Data See here:
 * http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#
 * jpa.query-methods.query-creation
 *
 */
public interface BusinessruleRepository {

  /**
   * Retrieve <code>Businessrule</code>s from the data store by last name, returning all
   * Businessrule.
   *
   * @param name Value to search for
   * @return a <code>Collection</code> of matching <code>Businessrule</code>s (or an empty
   *         <code>Collection</code> if none found)
   */
  Collection<Businessrule> findByName(String name) throws DataAccessException;

  /**
   * Retrieve an <code>Businessrule</code> from the data store by id.
   *
   * @param id the id to search for
   * @return the <code>Businessrule</code> if found
   * @throws org.springframework.dao.DataRetrievalFailureException if not found
   */
  Businessrule findById(int id) throws DataAccessException;


  /**
   * Save an <code>Businessrule</code> to the data store, either inserting or updating it.
   *
   * @param rule the <code>Businessrule</code> to save
   * @see BaseEntity#isNew
   */
  void save(Businessrule rule) throws DataAccessException;

  List<Businessrule> findByTableId(Integer tableId) throws DataAccessException;


}
