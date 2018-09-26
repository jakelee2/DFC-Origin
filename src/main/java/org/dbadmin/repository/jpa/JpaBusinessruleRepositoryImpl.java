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

import org.dbadmin.model.Businessrule;
import org.dbadmin.repository.BusinessruleRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

/**
 * JPA implementation of the {@link BusinessruleRepository} interface.
 *
 */
@Repository
public class JpaBusinessruleRepositoryImpl implements BusinessruleRepository {

  @PersistenceContext
  private EntityManager em;


  @SuppressWarnings("unchecked")
  public Collection<Businessrule> findByName(String name) {
    // using 'join fetch' because a single query should load both owners and pets
    // using 'left join fetch' because it might happen that an owner does not have pets yet
    Query query = this.em.createQuery(
        "SELECT DISTINCT businessrule FROM Businessrule businessrule left join fetch businessrule.jobs WHERE businessrule.name LIKE :name");
    query.setParameter("name", name + "%");

    return query.getResultList();
  }

  @Override
  public Businessrule findById(int id) {
    // using 'join fetch' because a single query should load both owners and pets
    // using 'left join fetch' because it might happen that an owner does not have pets yet
    Query query = this.em.createQuery(
        "SELECT businessrule FROM Businessrule businessrule left join fetch businessrule.jobs WHERE businessrule.id =:id");
    query.setParameter("id", id);
    Businessrule br = (Businessrule) query.getSingleResult();
    return br;
  }


  @Override
  public void save(Businessrule businessrule) {
    if (businessrule.getId() == null) {
      this.em.persist(businessrule);
    } else {
      this.em.merge(businessrule);
    }

  }

  @Override
  public List<Businessrule> findByTableId(Integer tableId) throws DataAccessException {
    // TODO Auto-generated method stub
    return null;
  }


}
