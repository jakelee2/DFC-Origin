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

import org.dbadmin.model.Type;
import org.dbadmin.model.Businessrule;
import org.dbadmin.model.Job;
import org.dbadmin.model.Task;
import org.springframework.dao.DataAccessException;

/**
 * Repository class for <code>Task</code> domain object.
 *
 */
public interface TaskRepository {

  List<Type> findTaskTypes() throws DataAccessException;

  List<Task> findByJobId(Integer id) throws DataAccessException;

  void save(Task task) throws DataAccessException;

  Collection<Task> findByName(String name) throws DataAccessException;

  Task findById(int id) throws DataAccessException;

  List<Job> findAccotiatedJobs(Integer taskId);

  List<Businessrule> findRelatedBusinessrules(Integer taskId);

  void updateTaskStatus(Task task, int statusId) throws DataAccessException;

}
