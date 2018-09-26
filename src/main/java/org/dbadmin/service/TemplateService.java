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
package org.dbadmin.service;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.dbadmin.handler.ConfigAttributeImpl;
import org.dbadmin.model.Businessrule;
import org.dbadmin.model.DbConnection;
import org.dbadmin.model.DbTable;
import org.dbadmin.model.DqsBounds;
import org.dbadmin.model.ExecScript;
import org.dbadmin.model.ExecutionOutput;
import org.dbadmin.model.Type;
import org.dbadmin.model.IngestionInfo;
import org.dbadmin.model.Job;
import org.dbadmin.model.JobTransferInfo;
import org.dbadmin.model.Role;
import org.dbadmin.model.Route;
import org.dbadmin.model.ScriptLog;
import org.dbadmin.model.Task;
import org.dbadmin.model.User;
import org.dbadmin.model.UserJob;
import org.dbadmin.model.Variable;
import org.springframework.dao.DataAccessException;

/**
 * Mostly used as a facade so all controllers have a single point of entry
 *
 */
public interface TemplateService {

  Collection<Type> findEtlTypes() throws DataAccessException;

  Businessrule findBusinessruleById(int id) throws DataAccessException;

  Collection<Businessrule> findBusinessruleByName(String name) throws DataAccessException;
  
  List<Businessrule> findBusinessruleByTableId(int tableId) throws DataAccessException;

  Job findJobById(int id) throws DataAccessException;

  Task findTaskById(int id) throws DataAccessException;

  void saveTask(Task task) throws DataAccessException;

  void saveVariable(Variable variable) throws DataAccessException;

  void saveJob(Job job) throws DataAccessException;

  void saveBusinessrule(Businessrule rule) throws DataAccessException;

  Collection<User> findUserByUserName(String username) throws DataAccessException;

  Collection<User> findUserByExactUserName(String username) throws DataAccessException;

  Collection<User> findUserByExceptUsername(String username) throws DataAccessException;

  List<User> findUsersByJobLevelAndExceptUsername(String username, int jobLevelId,
      boolean isAdmin) throws DataAccessException;

  User findUserById(int id) throws DataAccessException;

  void saveUser(User user) throws DataAccessException;

  void deleteUser(User user) throws DataAccessException;

  Collection<Role> findRoleByRoleName(String rolename) throws DataAccessException;

  Collection<Job> findJobByName(String jobName);

  Collection<Job> findJobByUsername(String username) throws DataAccessException;

  List<JobTransferInfo> findJobTransferByTransfererName(String username, boolean isAdmin) throws DataAccessException;

  List<JobTransferInfo> findJobTransferByTransfereeName(String username) throws DataAccessException;
  
  List<UserJob> findUserJobExistsByJobIdAndUserId(int userId, int jobId) throws DataAccessException;

  public Number addJobAssignee(int jobId, int transfereeId) throws DataAccessException;
    
  void updateActiveInUserJob(int userJobId, boolean active) throws DataAccessException;

  void updateJobAssignee(int jobId, int transfereeId) throws DataAccessException;

  void deactivateTransfer(int userJobId, int transfereeId, String description, boolean isForDefault) throws DataAccessException;
  
  List<Role> getUserRolesOfJobTransfer(int jobId) throws DataAccessException;
  
  void transferJob(int transfererId, String transfererUsername, boolean transfererIsAdmin, int jobId, int userJobId, int transfereeId, String transfereeUsername) throws DataAccessException;

  void updateJobLevel(int jobId, int jobLevel) throws DataAccessException;

  public List<JobTransferInfo> findTransferHistoryByUserId(int userId, boolean isAdmin) throws DataAccessException;

  Collection<Task> findTaskByName(String taskName);

  DbTable findDbTableById(int id) throws DataAccessException, SQLException;

  Collection<DbTable> findDbTableByName(String name) throws DataAccessException, SQLException;

  Job createIngestionJob(IngestionInfo ingestionInfo) throws DataAccessException;

  DbConnection findCurrentConnection() throws DataAccessException;

  Collection<DbConnection> findAllConnections() throws DataAccessException;

  List<DbConnection> findAllConnectionsOfExecutionOutput() throws DataAccessException;

  Collection<DbConnection> findFavoriteConnections(int userId) throws DataAccessException;

  Collection<DbConnection> findDbConnectionByName(String name) throws DataAccessException;

  DbConnection findDbConnectionById(int id) throws DataAccessException;

  void saveDbConnection(DbConnection connection) throws DataAccessException;

  void deleteDbConnection(DbConnection connection) throws DataAccessException;

  void add2favoriteDbConnection(int connectionId, int userId);

  Collection<Route> findAllRoutes();

  Route findRouteById(int id) throws DataAccessException;

  Collection<Route> findRouteByUrl(String url) throws DataAccessException;

  void saveRoute(Route route) throws DataAccessException;

  void deleteRoute(Route route) throws DataAccessException;

  void connect(int connId, int authenticatedUserId) throws SQLException;

    Map<String, Role> getRolesMap();

    Map<String, Collection<ConfigAttributeImpl>> getRoutesMap();

  void reloadRoutesMap();

  DqsBounds saveDqsBounds(DqsBounds dqBounds) throws DataAccessException;

  DqsBounds findDqsBoundsByTableAndColumn(String tableName, String columnName)
      throws DataAccessException;

  void disconnect(int connId) throws SQLException;

  Collection<DbConnection> findActiveConnections();

  void saveScript(ExecScript script, User user) throws DataAccessException;

  void deleteScript(String scriptName) throws DataAccessException;

  ExecScript findScriptByName(String name) throws DataAccessException;

  void save(ScriptLog script) throws DataAccessException;

  void save(Role role) throws DataAccessException;

  Role findRoleById(int id) throws DataAccessException;

  void delete(Role role) throws DataAccessException;

  Collection<ScriptLog> findScriptLogByName(String script_name) throws DataAccessException;
  
  List<Job> getJobsQueue(int jobId) throws DataAccessException;

  List<Task> getTasksQueue(int jobId) throws DataAccessException;
  
  void addRelatedJob2Job(int jobId, int relatedJobId, int relatedJobPriority) throws RuntimeException;

  void addRelatedTask2Job(int jobId, int relatedTaskId, int relatedTaskPriority, int taskStartConditionId) throws RuntimeException;
  
  void deleteRelatedJob(int jobId, int relatedJobId) throws RuntimeException;

  void deleteRelatedTask(int jobId, int relatedTaskId) throws RuntimeException;

  void addRelatedBusinessrule2Task(int taskId, int relatedBusinessruleId, int relatedBusinessrulePriority) throws RuntimeException;

  void deleteRelatedBusnessrule(int taskId, int relatedBusnessruleId) throws RuntimeException;

  void saveShortDbTableInfo(DbTable dbtable) throws DataAccessException;

  List<DbTable> findDbTablesByDbConnectionId(int dbconnectionId) throws DataAccessException, SQLException;

  List<ExecutionOutput> findExecutioOutputByTaskId(Integer id);

  List<ExecutionOutput> findExecutioOutputByBusinessruleId(Integer id);

  List<ExecutionOutput> runJob(int jobId) throws DataAccessException;

  List<ExecutionOutput> findTopExecutionOutputByBusinessruleId(Integer tableId, Integer bruleId);

}
