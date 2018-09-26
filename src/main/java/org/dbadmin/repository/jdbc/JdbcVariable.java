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
package org.dbadmin.repository.jdbc;

import org.dbadmin.model.Variable;

/**
 * Subclass of Variable that carries temporary id properties which are only relevant for a JDBC
 * implementation of the VariableRepository.
 *
 */
class JdbcVariable extends Variable {

  private int jobId;


  public int getJobId() {
    return this.jobId;
  }

  public void setJobId(int jobId) {
    this.jobId = jobId;
  }

}
