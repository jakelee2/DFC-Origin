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
package org.dbadmin.repository.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dbadmin.model.Job;
import org.dbadmin.model.JobTransferInfo;
import org.dbadmin.model.UserJob;
import org.dbadmin.repository.JobRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of the {@link JobRepository} interface.
 *
 */
@Repository
public class JpaJobRepositoryImpl implements JobRepository {

  @PersistenceContext
  private EntityManager em;

  @Override
  public Job findById(int id) {
    return this.em.find(Job.class, id);
  }

  @Override
  public void save(Job job) {
    if (job.getId() == null) {
      this.em.persist(job);
    } else {
      this.em.merge(job);
    }
  }

  @Override
  public Collection<Job> findByName(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Job> findByUsername(String username) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JobTransferInfo> findTransferByTransfererName(String username, boolean isAdmin)
      throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<JobTransferInfo> findJobTransferByTransfereeName(String username) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<UserJob> findUserJobExistsByJobIdAndUserId(int userId, int jobId)
      throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Number addJobAssignee(int jobId, int transfereeId) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
    
  }

  @Override
  public void updateActiveInUserJob(int userJobId, boolean active) throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void updateJobAssignee(int jobId, int transfereeId) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deactivateTransfer(int jobId, int transfereeId, String description, boolean isForDefault) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void transferJob(int transfererId, String transfererUsername, boolean transfererIsAdmin, int jobId, int userJobId, int transfereeId, String transfereeUsername) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public List<JobTransferInfo> findTransferHistoryByUserId(int userId, boolean isAdmin) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateLevel(int jobId, int jobLevel) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Collection<Job> findByExactName(String name) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

@Override
public Job loadRelatedTasksAndJobs(Job job) {
	// TODO Auto-generated method stub
	return null;
}


}
