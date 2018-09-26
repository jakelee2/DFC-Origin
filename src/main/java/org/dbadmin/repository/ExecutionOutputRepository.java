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

import java.util.List;

import org.dbadmin.model.ExecutionOutput;

import org.springframework.dao.DataAccessException;

/**
 * 8/12/2016 Denys:
 * 
 * Repository class for <code>ExecutionOutput</code> domain object.
 * Contains DAO methods for handling Task/Businessrule output after an execution of their internal logic.
 *
 */
public interface ExecutionOutputRepository {

  List<ExecutionOutput> findByTaskId(Integer id) throws DataAccessException;
  
  List<ExecutionOutput> findByBusinessruleId(Integer id) throws DataAccessException;
  
  List<ExecutionOutput> findByJobId(Integer id) throws DataAccessException;

  void save(ExecutionOutput output) throws DataAccessException;

  List<ExecutionOutput> findTopByBusinessruleId(Integer tableId, Integer bruleId) throws DataAccessException;

}
