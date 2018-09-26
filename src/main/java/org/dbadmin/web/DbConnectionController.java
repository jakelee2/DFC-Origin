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
package org.dbadmin.web;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.dbadmin.model.DbConnection;
import org.dbadmin.model.DbConnectionJson;
import org.dbadmin.model.JsonMessage;
import org.dbadmin.model.JsonMessage.Status;
import org.dbadmin.model.User;
import org.dbadmin.service.TemplateService;
import org.dbadmin.util.ActiveConnectionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC and REST controller for Database Connections.
 */
@Controller
public class DbConnectionController {

  private final TemplateService templateService;
  private int authenticatedUserId = -1; // not authenticated User
  
  @Autowired
  public DbConnectionController(TemplateService templateService) {
    this.templateService = templateService;

  }

  
  @InitBinder
  public void setAllowedFields(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }

  
  /*************** Web UI (MVC) METHODS: ******************/
  
  @RequestMapping(value = "/dbconnections/new", method = RequestMethod.GET)
  public String webuiCreateConnection(Map<String, Object> model) {
    DbConnection dbconnection = new DbConnection();
    model.put("dbConnection", dbconnection);
    return "dbconnections/createOrUpdateDbconnectionForm";
  }

  

  @RequestMapping(value = "/dbconnections/new", method = RequestMethod.POST)
  public String webuiCreateConnection(@Valid DbConnection dbconnection, BindingResult result,
      Map<String, Object> model) {
    if (result.hasErrors()) {
      return "dbconnections/createOrUpdateDbconnectionForm";
    } else {
      this.templateService.saveDbConnection(dbconnection);
      
      this.rereadAndRefillModel(model);
      
      return "dbconnections/dbconnectionsList";
    }
  }
  
  
  @RequestMapping(value = "/dbconnections/{dbconnectionId}/edit", method = RequestMethod.GET)
  public String webuiUpdateConnection(@PathVariable("dbconnectionId") int connId, Model model) {
    DbConnection conn = this.templateService.findDbConnectionById(connId);
    model.addAttribute(conn);
    return "dbconnections/createOrUpdateDbconnectionForm";
  }

  
  @RequestMapping(value = "/dbconnections/{dbconnectionId}/edit", method = RequestMethod.POST)
  public String webuiUpdateConnection(@Valid DbConnection dbconnection,
      BindingResult result, @PathVariable("dbconnectionId") int connId) {
    if (result.hasErrors()) {
      return "dbconnections/createOrUpdateDbconnectionForm";
    } else {
      dbconnection.setId(connId);
      this.templateService.saveDbConnection(dbconnection);
      
      return "redirect:/dbconnections/{dbconnectionId}";
    }
  }

  
  @RequestMapping(value = "/dbconnections/{dbconnectionId}/delete", method = RequestMethod.GET)
  public String webuiDeleteConnection(@Valid DbConnection conn, BindingResult result,
      @PathVariable("dbconnectionId") int connId, Map<String, Object> model) {
    String msg = "";
    if (result.hasErrors()) {
      return "dbconnections/createOrUpdateDbconnectionForm";
    } else {
      conn.setId(connId);
      try {
        this.templateService.deleteDbConnection(conn);
        msg = "connection deleted successfully";
      } catch (Exception e) {
        msg = "can not delete connection";
      }
      model.put("msg", msg);

      this.rereadAndRefillModel(model);
      
      return "redirect:/dbconnections";
    }
  }

  
  @RequestMapping(value = "/dbconnections/{dbconnectionId}/addfavorite", method = RequestMethod.GET)
  public String webuiAddToFavorite(@PathVariable("dbconnectionId") int connId,
      Map<String, Object> model) {
    this.templateService.add2favoriteDbConnection(connId, getAuthenticatedUserId());
    return "redirect:/dbconnections/{dbconnectionId}";
  }

  
  @RequestMapping(value = "/dbconnections/{dbconnectionId}/connect", method = RequestMethod.GET)
  public String webuiConnect(@PathVariable("dbconnectionId") int connId,
      Map<String, Object> model) {
    String msg = "";
    try {
      this.templateService.connect(connId, getAuthenticatedUserId());
      msg = "Connected successfully";
    } catch (SQLException e) {
      msg = "ERROR: can not connect to database";
    }
    model.put("msg", msg);
    
    this.rereadAndRefillModel(model);
    
    return "dbconnections/dbconnectionsList"; // redirect to list of all connections
  }

  
  @RequestMapping(value = "/dbconnections/{dbconnectionId}/disconnect", method = RequestMethod.GET)
  public String webuiDisconnect(@PathVariable("dbconnectionId") int connId,
      Map<String, Object> model) {
    String msg = "";
    try {
      this.templateService.disconnect(connId); 
      msg = "Disconnected successfully"; 
    } catch (Exception e) {
      msg = "ERROR: "+e.getMessage();
    }
    model.put("msg", msg);
    
    this.rereadAndRefillModel(model);

    return "dbconnections/dbconnectionsList"; // redirect to list of all connections
  }
  
  @RequestMapping(value = "/dbconnections", method = RequestMethod.GET)
  public String webuiProcessFind(DbConnection connection, Map<String, Object> model) {

    this.rereadAndRefillModel(model);
    
    return "dbconnections/dbconnectionsList";
  }


  @RequestMapping("/dbconnections/{dbconnectionId}")
  public ModelAndView webuiShowConnection(@PathVariable("dbconnectionId") int connectionId) {
    ModelAndView mav = new ModelAndView("dbconnections/dbconnectionDetails");
    DbConnection dbc = this.templateService.findDbConnectionById(connectionId);
    mav.addObject(dbc);
    return mav;
  }


  private void rereadAndRefillModel (Map<String, Object> model) {

    Collection<DbConnection> activeConnections = this.templateService.findActiveConnections();
    model.put("actives", activeConnections);
    
    Collection<DbConnection> allConnections = this.templateService.findAllConnections();
    model.put("selections", allConnections);

    Collection<DbConnection> favoriteConnections = this.templateService.findFavoriteConnections(getAuthenticatedUserId());
    model.put("favorits", favoriteConnections);
    
  }
  
  /*************** REST METHODS: ******************/
  
  
  // 1)  all REST methods below work with ActiveConnectionRegistry. No persistence is here.
  

  @RequestMapping(value = "/dbconnections/getactive/{connId}", method = RequestMethod.GET)
  public ResponseEntity<Object> restFindActiveConnectionById(@PathVariable("connId") String id) {
    DbConnection data = ActiveConnectionRegistry.INSTANCE.getActiveConnectionById(id);
    JsonMessage msg = new JsonMessage();
    if (data.getConnectionName().isEmpty()) {
      msg.setStatus(Status.FAIL);
      msg.setMessage("CANNOT FIND connection id = " + id + " among ACTIVE connections");
      return new ResponseEntity<Object>(msg, HttpStatus.BAD_REQUEST);
    } else
      return new ResponseEntity<Object>(data, HttpStatus.OK);
  }

  
  @RequestMapping(value = "/dbconnections/disconnect/{connId}", method = RequestMethod.GET)
  public ResponseEntity<JsonMessage> restDeleteConnectionFromActiveList(@PathVariable("connId") String id) {
    JsonMessage msg = new JsonMessage();    
    try {
    ActiveConnectionRegistry.INSTANCE.deleteActiveConnection(id);
    msg.setStatus(Status.SUCCESS);
    msg.setMessage("Active connection id =" + id + " has been disconnected successfully.");
    return new ResponseEntity<JsonMessage>(msg, HttpStatus.OK);
    } catch (Exception e) {
      msg.setStatus(Status.FAIL);
      msg.setMessage(e.getLocalizedMessage());
      return new ResponseEntity<JsonMessage>(msg, HttpStatus.BAD_REQUEST);
    }
  }
  

  @RequestMapping(value = "/dbconnections/getactive/all", method = RequestMethod.GET)
  public ResponseEntity<Object> restGetAllActiveConnections() {
    List<DbConnection> list = ActiveConnectionRegistry.INSTANCE.getAllActiveConnections();
    JsonMessage msg = new JsonMessage();
    if (list.isEmpty()) {
      msg.setStatus(Status.FAIL);
      msg.setMessage("CANNOT find any ACTIVE connections");
      return new ResponseEntity<Object>(msg, HttpStatus.BAD_REQUEST);
    } else
      return new ResponseEntity<Object>(list, HttpStatus.OK);
  }
  
  // 2) all REST methods below work through DAO layer. Persistent.
  
  @RequestMapping(value = "/dbconnections/connectbyid/{connId}", method = RequestMethod.GET)
  public ResponseEntity<JsonMessage> restConnectById(@PathVariable("connId") String id) {
    JsonMessage msg = new JsonMessage();
    try {
      this.templateService.connect(Integer.valueOf(id), getAuthenticatedUserId());
      msg.setStatus(Status.SUCCESS);
      msg.setMessage("Connected successfully to id= " + id);
      return new ResponseEntity<JsonMessage>(msg, HttpStatus.OK);
    } catch (Exception e) {
      msg.setStatus(Status.ERROR);
      msg.setMessage("ERROR: can not connect to database");
      return new ResponseEntity<JsonMessage>(msg, HttpStatus.FORBIDDEN);
    }
  }

  
  @RequestMapping(value = "/dbconnections/connectbyname/{connName}", method = RequestMethod.GET)
  public ResponseEntity<Object> restConnectByName(@PathVariable("connName") String name) {
    JsonMessage msg = new JsonMessage();
    msg.setStatus(Status.ERROR);
    msg.setMessage("ERROR: can not connect to database");
    try {
      List<DbConnection> li = (List<DbConnection>) this.templateService.findDbConnectionByName(name);
      if (!li.isEmpty() && li.size() > 0) {
        this.templateService.connect(li.get(0).getId(), getAuthenticatedUserId());
        String fullname = li.get(0).getConnectionName();
        String server = li.get(0).getDatabaseJdbcUrl();
        msg.setStatus(Status.SUCCESS);
        msg.setMessage("Connected successfully to: " + fullname + " at server: " + server);
        return new ResponseEntity<Object>(msg, HttpStatus.OK);
      } else
        return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
    } catch (Exception e) {
      return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
    }
  }

  
  @RequestMapping(value = "/dbconnections/deletebyid/{connId}", method = RequestMethod.GET)
  public ResponseEntity<Object> restDeleteConnectionById(@PathVariable("connId") String id) {
    JsonMessage msg = new JsonMessage();
    try {
      DbConnection conn = new DbConnection();
      conn.setId(Integer.valueOf(id));
      this.templateService.deleteDbConnection(conn);
      msg.setStatus(Status.SUCCESS);
      msg.setMessage("Connection id= "+id+" has been deleted successfully.");
      return new ResponseEntity<Object>(msg, HttpStatus.OK);
    } catch (Exception e) {
      msg.setStatus(Status.FAIL);
      msg.setMessage("ERROR: can not delete connection id= "+id);
      return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
    }
  }

  
  @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/dbconnections/create", method = RequestMethod.POST)
  public ResponseEntity<JsonMessage> restCreateConnection(@RequestBody DbConnectionJson jsonRequest) {
    JsonMessage msg = new JsonMessage();
    try {
      String connectionName = jsonRequest.getConnectionName();
      String databaseJdbcUrl = jsonRequest.getDatabaseJdbcUrl();
      String databaseJdbcDriver = jsonRequest.getDatabaseJdbcDriver();
      String databaseUser = jsonRequest.getDatabaseUser();
      String databasePasswd = jsonRequest.getDatabasePasswd();
      if (
          !connectionName.isEmpty() &&
          !databaseJdbcUrl.isEmpty() &&
          !databaseJdbcDriver.isEmpty() &&
          !databaseUser.isEmpty() &&
          !databasePasswd.isEmpty()
          ) {
            DbConnection conn = new DbConnection();
            conn.setConnectionName(connectionName);
            conn.setDatabaseJdbcUrl(databaseJdbcUrl);
            conn.setDatabaseJdbcDriver(databaseJdbcDriver);
            conn.setDatabaseUser(databaseUser);
            conn.setDatabasePasswd(databasePasswd);
            List<DbConnection> li = (List<DbConnection>) this.templateService.findDbConnectionByName(connectionName);
            if (li!=null && !li.isEmpty()) {
              this.templateService.saveDbConnection(conn);
              msg.setStatus(Status.ERROR);
              msg.setMessage("Connection with name "+connectionName+" already exists.");            
              return new ResponseEntity<JsonMessage>(msg, HttpStatus.FORBIDDEN);
            }
            else
            {
              this.templateService.saveDbConnection(conn);
              msg.setStatus(Status.SUCCESS);
              msg.setMessage("Connection "+connectionName+" created.");            
              return new ResponseEntity<JsonMessage>(msg, HttpStatus.OK);              
            }
      } else {
        msg.setStatus(Status.FAIL);
        msg.setMessage("Can not create connection due to lack of mandatory arguments.");          
        return new ResponseEntity<JsonMessage>(msg, HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      msg.setStatus(Status.FAIL);
      msg.setMessage(e.getLocalizedMessage());       
      return new ResponseEntity<JsonMessage>(msg, HttpStatus.FORBIDDEN);
    }
  }
  
  /*************** Utility METHODS: ******************/
  
  /**
   * Retrieve current Principal (Authenticated) User id
   * 
   * @return authenticated User id
   */
  private int getAuthenticatedUserId() {
    if (this.authenticatedUserId == -1) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String currentPrincipalName = authentication.getName();
      List<User> users = (List<User>) this.templateService.findUserByUserName(currentPrincipalName);
      // assume User name is unique.
      if (users != null && !users.isEmpty()) {
        this.authenticatedUserId = users.get(0).getId();
      }
    }
    return this.authenticatedUserId;
  }
  
}
