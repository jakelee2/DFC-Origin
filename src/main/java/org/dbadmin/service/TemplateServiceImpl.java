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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dbadmin.handler.ConfigAttributeImpl;
import org.dbadmin.model.Businessrule;
import org.dbadmin.model.DbConnection;
import org.dbadmin.model.DbTable;
import org.dbadmin.model.DqsBounds;
import org.dbadmin.model.ExecScript;
import org.dbadmin.model.ExecutionOutput;
import org.dbadmin.model.IngestionInfo;
import org.dbadmin.model.Job;
import org.dbadmin.model.JobTransferInfo;
import org.dbadmin.model.Role;
import org.dbadmin.model.Route;
import org.dbadmin.model.ScriptLog;
import org.dbadmin.model.Task;
import org.dbadmin.model.Type;
import org.dbadmin.model.User;
import org.dbadmin.model.UserJob;
import org.dbadmin.model.Variable;
import org.dbadmin.repository.BusinessruleRepository;
import org.dbadmin.repository.DbConnectionRepository;
import org.dbadmin.repository.DbTableRepository;
import org.dbadmin.repository.DqsRepository;
import org.dbadmin.repository.ExecutionOutputRepository;
import org.dbadmin.repository.JobRepository;
import org.dbadmin.repository.JobsTasksLogic;
import org.dbadmin.repository.RoleRepository;
import org.dbadmin.repository.RouteRepository;
import org.dbadmin.repository.ScriptRepository;
import org.dbadmin.repository.TaskRepository;
import org.dbadmin.repository.UserRepository;
import org.dbadmin.repository.VariableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Template controllers Also a placeholder for @Transactional
 * and @Cacheable annotations
 *
 */
@Service
public class TemplateServiceImpl implements TemplateService {

  private JobRepository jobRepository;
  private BusinessruleRepository businessruleRepository;
  private TaskRepository taskRepository;
  private VariableRepository variableRepository;
  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private DbTableRepository dbTableRepository;
  private DbConnectionRepository dbConnectionRepository;
  private RouteRepository routeRepository;
  private DqsRepository dqsRepository;
  private ScriptRepository scriptRepository;
  private Map<String, Collection<ConfigAttributeImpl>> routesMap = null;
  private JobsTasksLogic jobsTasksLogic;
  private ExecutionOutputRepository executionOutputRepository;
    private Map<String, Role> rolesMap = null;


    @Autowired
  public TemplateServiceImpl(BusinessruleRepository businessruleRepository,
      JobRepository jobRepository, TaskRepository taskRepository,
      VariableRepository variableRepository, UserRepository userRepository,
      RoleRepository roleRepository, DbTableRepository dbTableRepository,
      DbConnectionRepository dbConnectionRepository, RouteRepository routeRepository,
      DqsRepository dqsRepository, ScriptRepository scriptRepository
      , JobsTasksLogic jobsTasksLogic, ExecutionOutputRepository executionOutputRepository
      ) {
    this.businessruleRepository = businessruleRepository;
    this.jobRepository = jobRepository;
    this.taskRepository = taskRepository;
    this.variableRepository = variableRepository;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.dbTableRepository = dbTableRepository;
    this.dbConnectionRepository = dbConnectionRepository;
    this.routeRepository = routeRepository;
    this.dqsRepository = dqsRepository;
    this.scriptRepository = scriptRepository;
    this.jobsTasksLogic = jobsTasksLogic;
    this.executionOutputRepository = executionOutputRepository;
  }


  @Override
  @Transactional(readOnly = true)
  public Collection<Type> findEtlTypes() throws DataAccessException {
    return taskRepository.findTaskTypes();
  }


  @Override
  @Transactional(readOnly = true)
  public Businessrule findBusinessruleById(int id) throws DataAccessException {
    return businessruleRepository.findById(id);
  }


  @Override
  @Transactional(readOnly = true)
  public Collection<Businessrule> findBusinessruleByName(String name) throws DataAccessException {
    return businessruleRepository.findByName(name);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Businessrule> findBusinessruleByTableId(int tableId) throws DataAccessException {
    return businessruleRepository.findByTableId(tableId);
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<Job> findJobByName(String name) throws DataAccessException {
    return jobRepository.findByName(name);
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<Job> findJobByUsername(String username) throws DataAccessException {
    return jobRepository.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public List<JobTransferInfo> findJobTransferByTransfererName(String username, boolean isAdmin)
      throws DataAccessException {
    return jobRepository.findTransferByTransfererName(username, isAdmin);
  }

  @Override
  @Transactional(readOnly = true)
  public List<JobTransferInfo> findJobTransferByTransfereeName(String username)
      throws DataAccessException {
    return jobRepository.findJobTransferByTransfereeName(username);
  }

  public List<UserJob> findUserJobExistsByJobIdAndUserId(int userId, int jobId) throws DataAccessException {
    return jobRepository.findUserJobExistsByJobIdAndUserId(userId, jobId);
  }
  
  @Transactional
  public Number addJobAssignee(int jobId, int transfereeId) throws DataAccessException {
    Number id = (Number) jobRepository.addJobAssignee(jobId, transfereeId);
    return id;
  }

  @Override
  public void updateActiveInUserJob(int userJobId, boolean active) throws DataAccessException {
    jobRepository.updateActiveInUserJob(userJobId, active);
  }

  @Transactional
  public void updateJobAssignee(int jobId, int transfereeId) throws DataAccessException {
    jobRepository.updateJobAssignee(jobId, transfereeId);
  }

  @Transactional
  public void deactivateTransfer(int userJobId, int transfereeId, String description, boolean isForDefault)
      throws DataAccessException {
    jobRepository.deactivateTransfer(userJobId, transfereeId, description, isForDefault);
  }

  @Override
  public List<Role> getUserRolesOfJobTransfer(int jobId) throws DataAccessException{
    List<Role> roles = roleRepository.getUserRolesOfJobTransfer(jobId);
    return roles;
  }

  @Transactional
  public void transferJob(int transfererId, String transfererUsername, boolean transfererIsAdmin, int jobId, int userJobId, int transfereeId,
      String transfereeUsername) throws DataAccessException {
    jobRepository.transferJob(transfererId, transfererUsername, transfererIsAdmin, jobId, userJobId, transfereeId,
        transfereeUsername);
  }

  @Override
  public void updateJobLevel(int jobId, int jobLevel) {
    jobRepository.updateLevel(jobId, jobLevel);
  }

  @Transactional
  public List<JobTransferInfo> findTransferHistoryByUserId(int userId, boolean isAdmin)
      throws DataAccessException {
    return jobRepository.findTransferHistoryByUserId(userId, isAdmin);
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<Task> findTaskByName(String name) throws DataAccessException {
    return taskRepository.findByName(name);
  }

  @Override
  public Job createIngestionJob(IngestionInfo ingestionInfo) throws DataAccessException {
    Job job = new Job();
    job.setName(ingestionInfo.getIngestJobName());
    // job.setBusinessrule_id(1); // TODO: this is hard code for ingestion job for now
    jobRepository.save(job);

    Task task = new Task();

    task.setName(ingestionInfo.getIngestJobName());
    task.setDescription(ingestionInfo.getIngestJobName());
    task.setBody(ingestionInfo.toJson());
    // task.setPriority(1);

    task.setTypeId(3); // Ingestion
    // task.setJob(job);

    taskRepository.save(task);
    return job;

  }

  @Override
  @Transactional
  public void saveBusinessrule(Businessrule rule) throws DataAccessException {
    businessruleRepository.save(rule);
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<User> findUserByUserName(String username) throws DataAccessException {
    return userRepository.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<User> findUserByExactUserName(String username) throws DataAccessException {
    return userRepository.findByExactUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<User> findUserByExceptUsername(String username) throws DataAccessException {
    return userRepository.findAllByExceptUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findUsersByJobLevelAndExceptUsername(String username, int jobLevelId,
      boolean isAdmin) throws DataAccessException {
    return userRepository.findByJobLevelAndExceptUsername(username, jobLevelId, isAdmin);
  }

  @Override
  @Transactional(readOnly = true)
  public User findUserById(int id) throws DataAccessException {
    return userRepository.findById(id);
  }

  @Override
  @Transactional
  public void saveUser(User user) throws DataAccessException {
    // update role here
    List<Role> roles = new ArrayList<>();
    user.getRoleIds().stream()
        .forEach(rolename -> roles.addAll(roleRepository.findByRolename("ROLE_" + rolename)));
    user.setRoles(roles);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void deleteUser(User user) throws DataAccessException {
    userRepository.delete(user);
  }

  @Override
  public Collection<Role> findRoleByRoleName(String rolename) throws DataAccessException {
    return roleRepository.findByRolename(rolename);
  }


  @Override
  @Transactional(readOnly = true)
  public Job findJobById(int id) throws DataAccessException {
    return jobRepository.findById(id);
  }

  @Override
  @Transactional
  public void saveJob(Job job) throws DataAccessException {
    jobRepository.save(job);
  }


  @Override
  @Transactional
  public void saveTask(Task task) throws DataAccessException {
    taskRepository.save(task);

  }

  @Override
  @Transactional
  public void saveVariable(Variable variable) throws DataAccessException {
    variableRepository.save(variable);

  }


  @Override
  @Transactional(readOnly = true)
  public Task findTaskById(int id) throws DataAccessException {
    return taskRepository.findById(id);
  }


  @Override
  @Transactional(readOnly = true)
  public DbTable findDbTableById(int id) throws DataAccessException, SQLException {
    return dbTableRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<DbTable> findDbTableByName(String name)
      throws DataAccessException, SQLException {
    return dbTableRepository.findByName(name);
  }


  @Override
  @Transactional(readOnly = true)
  public DbConnection findCurrentConnection() throws DataAccessException {
    return dbConnectionRepository.getSystemConnection();
  }


  @Override
  @Transactional(readOnly = true)
  public Collection<DbConnection> findAllConnections() throws DataAccessException {
    return dbConnectionRepository.findAllConnections();
  }

  @Override
  @Transactional(readOnly = true)
  public List<DbConnection> findAllConnectionsOfExecutionOutput() throws DataAccessException {
    return dbConnectionRepository.findAllConnectionsOfExecutionOutput();
  }

  @Override
  @Transactional(readOnly = true)
  public DbConnection findDbConnectionById(int id) throws DataAccessException {
    return dbConnectionRepository.findById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<DbConnection> findDbConnectionByName(String name) throws DataAccessException {
    return dbConnectionRepository.findByName(name);
  }


  @Override
  @Transactional
  public void saveDbConnection(DbConnection connection) throws DataAccessException {
    dbConnectionRepository.save(connection);
  }


  @Override
  @Transactional
  public void deleteDbConnection(DbConnection connection) throws DataAccessException {
    dbConnectionRepository.delete(connection);
  }


  @Override
  @Transactional
  public void add2favoriteDbConnection(int connectionId, int userId) {
    dbConnectionRepository.add2favorite(connectionId, userId);
  }

  @Override
  public Collection<Route> findAllRoutes() {
    return routeRepository.findAllRoutes();
  }

  @Override
  public Route findRouteById(int id) throws DataAccessException {
    return routeRepository.findRouteById(id);
  }

  @Override
  public Collection<Route> findRouteByUrl(String url) throws DataAccessException {
    return routeRepository.findRouteByUrl(url);
  }

  @Override
  public void saveRoute(Route route) throws DataAccessException {
    routeRepository.saveRoute(route);
    reloadRoutesMap();
  }

  @Override
  public void deleteRoute(Route route) throws DataAccessException {
    routeRepository.deleteRoute(route);
    reloadRoutesMap();
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<DbConnection> findFavoriteConnections(int userId) throws DataAccessException {
    return dbConnectionRepository.findFavoriteConnections(userId);
  }

  @Override
  public Collection<DbConnection> findActiveConnections() {
    return dbConnectionRepository.findActiveConnections();
  }

  @Override
  public void saveScript(ExecScript script, User user) throws DataAccessException {
    scriptRepository.save(script, user);
  }

  @Override
  public void deleteScript(String scriptName) throws DataAccessException {
    scriptRepository.delete(scriptName);
  }

  @Override
  public ExecScript findScriptByName(String name) throws DataAccessException {
    return scriptRepository.findScriptByName(name);
  }

  @Override
  public void save(ScriptLog script) throws DataAccessException {
    scriptRepository.save(script);
  }

    @Override
    public void save(Role role) throws DataAccessException {
        roleRepository.save(role);
        reloadRolesMap();
    }

    @Override
    public void delete(Role role) throws DataAccessException {
        roleRepository.delete(role);
        reloadRolesMap();
    }

    @Override
    public Role findRoleById(int id) throws DataAccessException {
        return roleRepository.findRoleById(id);
    }

  @Override
  public Collection<ScriptLog> findScriptLogByName(String script_name) throws DataAccessException {
    return scriptRepository.findScriptLogByName(script_name);
  }

  @Override
  public void connect(int connectionId, int authenticatedUserId) throws SQLException {
    dbConnectionRepository.activateDbConnection(connectionId, authenticatedUserId);

  }

  @Override
  public void disconnect(int connectionId) throws SQLException {
    dbConnectionRepository.deactivateDbConnection(connectionId);
  }

    /**
     * get Role Map from data base and cached it.
     *
     * @return
     */
    @Override
    public Map<String, Role> getRolesMap() {
        if (rolesMap == null) {
            reloadRolesMap();
        }
        return rolesMap;
    }

    /**
     * Reload Routes Map if there is a changes on the route admin.
     */
    private void reloadRolesMap() {
        rolesMap = new HashMap<>();
        List<Role> roles = findRoleByRoleName("").stream().collect(Collectors.toList());
        rolesMap = roles.stream().collect(
            Collectors.toMap(x -> x.getRole_name(), x -> x));
    }


  /**
   * get Route Map from data base and cached it.
   *
   * @return
   */
  @Override
  public Map<String, Collection<ConfigAttributeImpl>> getRoutesMap() {
    if (routesMap == null) {
      reloadRoutesMap();
    }
    return routesMap;
  }

  /**
   * Reload Routes Map if there is a changes on the route admin.
   */
  @Override
  public void reloadRoutesMap() {
    routesMap = new HashMap<>();
    List<Route> routes = findAllRoutes().stream().collect(Collectors.toList());
    Collections.sort(routes, (r1, r2) -> r1.getPriority().compareTo(r2.getPriority()));

    for (Route route : routes) {
      ConfigAttributeImpl attr = new ConfigAttributeImpl(route.getAccess(), route.getPriority());
      if (!routesMap.containsKey(route.getUrl())) {
        routesMap.put(route.getUrl(), new ArrayList<>());
      }
      routesMap.get(route.getUrl()).add(attr);
    }
  }

  @Override
  @Transactional
  public DqsBounds saveDqsBounds(DqsBounds dqBounds) throws DataAccessException {
    dqsRepository.save(dqBounds);
    return dqBounds;
  }

  @Override
  @Transactional(readOnly = true)
  public DqsBounds findDqsBoundsByTableAndColumn(String tableName, String columnName)
      throws DataAccessException {
    return dqsRepository.findByTableAndColumn(tableName, columnName);
  }


@Override
@Transactional(readOnly = true)
public List<Job> getJobsQueue(int jodId) throws DataAccessException {
	return jobsTasksLogic.getJobsQueue(jodId);

}


@Override
@Transactional(readOnly = true)
public List<Task> getTasksQueue(int jobId) throws DataAccessException {
	return jobsTasksLogic.getTasksQueue(jobId);
}


@Override
@Transactional //not only read-only!
public List<ExecutionOutput> runJob(int jobId) throws DataAccessException {
	return jobsTasksLogic.runJob(jobId);
}

@Override
@Transactional
public void addRelatedJob2Job(int jobId, int relatedJobId, int relatedJobPriority) throws RuntimeException {
	jobsTasksLogic.addRelatedJob2Job(jobId, relatedJobId, relatedJobPriority);
}


@Override
@Transactional
public void addRelatedTask2Job(int jobId, int relatedTaskId, int relatedTaskPriority, int taskStartConditionId)
		throws RuntimeException {
	jobsTasksLogic.addRelatedTask2Job(jobId, relatedTaskId, relatedTaskPriority, taskStartConditionId);

}


@Override
@Transactional
public void deleteRelatedJob(int jobId, int relatedJobId) throws RuntimeException {
	jobsTasksLogic.deleteRelatedJob(jobId, relatedJobId);

}


@Override
@Transactional
public void deleteRelatedTask(int jobId, int relatedTaskId) throws RuntimeException {
	jobsTasksLogic.deleteRelatedTask(jobId, relatedTaskId);

}


@Override
@Transactional
public void addRelatedBusinessrule2Task(int taskId, int relatedBusinessruleId, int relatedBusinessrulePriority)
		throws RuntimeException {
		jobsTasksLogic.addRelatedBusinessrule2Task(taskId, relatedBusinessruleId, relatedBusinessrulePriority);

}


@Override
@Transactional
public void deleteRelatedBusnessrule(int taskId, int relatedBusnessruleId) throws RuntimeException {
		jobsTasksLogic.deleteRelatedBusinessrule(taskId, relatedBusnessruleId);
}

@Override
@Transactional(readOnly = true)
public void saveShortDbTableInfo(DbTable dbtable) throws DataAccessException {
	dbTableRepository.saveShortDbTableInfo(dbtable);
}

@Override
@Transactional(readOnly = true)
public List<DbTable> findDbTablesByDbConnectionId(int dbconnectionId) throws DataAccessException, SQLException {
    return dbTableRepository.findByDbConnectionId(dbconnectionId);
}

@Override
public List<ExecutionOutput> findExecutioOutputByTaskId(Integer id) {
	return executionOutputRepository.findByTaskId(id);
}

@Override
public List<ExecutionOutput> findExecutioOutputByBusinessruleId(Integer id) {
	return executionOutputRepository.findByBusinessruleId(id);
}

  @Override
  public List<ExecutionOutput> findTopExecutionOutputByBusinessruleId(Integer tableId, Integer bruleId) {
      return executionOutputRepository.findTopByBusinessruleId(tableId, bruleId);
  }

}
