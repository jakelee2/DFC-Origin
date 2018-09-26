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
import javax.persistence.Query;

import org.dbadmin.model.Type;
import org.dbadmin.model.Businessrule;
import org.dbadmin.model.Job;
import org.dbadmin.model.Task;
import org.dbadmin.repository.TaskRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of the TemplateService interface using EntityManager.
 * <p/>
 * <p>
 * The mappings are defined in "orm.xml" located in the META-INF directory.
 *
 */
@Repository
public class JpaEtlTaskRepositoryImpl implements TaskRepository {

  @PersistenceContext
  private EntityManager em;


  @Override
  public void save(Task task) {
    if (task.getId() == null) {
      this.em.persist(task);
    } else {
      this.em.merge(task);
    }
  }


  @Override
  @SuppressWarnings("unchecked")
  public List<Task> findByJobId(Integer jobId) {
    Query query = this.em.createQuery("SELECT etltask FROM ETLTask v where v.jobs.id= :id");
    query.setParameter("id", jobId);
    return query.getResultList();
  }


  @Override
  @SuppressWarnings("unchecked")
  public List<Type> findTaskTypes() throws DataAccessException {
    return this.em.createQuery("SELECT ptype FROM ETLType ptype ORDER BY ptype.name")
        .getResultList();
  }


  @Override
  public Collection<Task> findByName(String name) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public Task findById(int id) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public List<Job> findAccotiatedJobs(Integer taskId) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public List<Businessrule> findRelatedBusinessrules(Integer taskId) {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void updateTaskStatus(Task task, int statusId) throws DataAccessException {
    // TODO Auto-generated method stub
    
  }

}
