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

import org.dbadmin.model.Job;
import org.dbadmin.model.JobTransferInfo;
import org.dbadmin.model.UserJob;
import org.springframework.dao.DataAccessException;

/**
 * Repository class for <code>Job</code> domain objects All method names are compliant with Spring
 * Data naming conventions so this interface can easily be extended for Spring Data See here:
 * http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#
 * jpa.query-methods.query-creation
 *
 */
public interface JobRepository {

  Job findById(int id) throws DataAccessException;

  void save(Job job) throws DataAccessException;

  Collection<Job> findByName(String name) throws DataAccessException;

  Collection<Job> findByUsername(String username) throws DataAccessException;

  List<JobTransferInfo> findTransferByTransfererName(String username, boolean isAdmin) throws DataAccessException;

  List<JobTransferInfo> findJobTransferByTransfereeName(String username) throws DataAccessException;
  
  List<JobTransferInfo> findTransferHistoryByUserId(int userId, boolean isAdmin);

  List<UserJob> findUserJobExistsByJobIdAndUserId(int userId, int jobId) throws DataAccessException;
  
  Number addJobAssignee(int jobId, int transfereeId) throws DataAccessException;
  
  void updateActiveInUserJob(int userJobId, boolean active) throws DataAccessException;
  
  void updateJobAssignee(int jobId, int transfereeId) throws DataAccessException;

  void deactivateTransfer(int userJobId, int transfereeId, String description, boolean isForDefault) throws DataAccessException;

  void transferJob(int transfererId, String transfererUsername, boolean transfererIsAdmin, int jobId, int userJobId, int transfereeId, String transfereeUsername) throws DataAccessException;

  void updateLevel(int jobId, int jobLevel);

  Collection<Job> findByExactName(String name) throws DataAccessException;
  
  Job loadRelatedTasksAndJobs(Job job);

}
