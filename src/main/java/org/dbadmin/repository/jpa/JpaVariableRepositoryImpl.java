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

import org.dbadmin.model.Task;
import org.dbadmin.model.Variable;
import org.dbadmin.repository.VariableRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation using EntityManager.
 * <p/>
 * <p>
 * TODO The mappings are defined in "orm.xml" located in the META-INF directory????
 *
 */
@Repository
public class JpaVariableRepositoryImpl implements VariableRepository {

  @PersistenceContext
  private EntityManager em;


  @Override
  public void save(Variable variable) {
    if (variable.getId() == null) {
      this.em.persist(variable);
    } else {
      this.em.merge(variable);
    }
  }


  @Override
  @SuppressWarnings("unchecked")
  public List<Variable> findByJobId(Integer jobId) {
    Query query = this.em.createQuery("SELECT variable FROM Variable v where v.jobs.id= :id");
    query.setParameter("id", jobId);
    return query.getResultList();
  }


  @Override
  public Collection<Task> findByName(String name) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }

}
