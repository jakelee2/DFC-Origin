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

import org.dbadmin.model.DbTable;
import org.dbadmin.model.JsonMessage;
import org.dbadmin.model.JsonMessage.Status;
import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @author denys This MVC and REST controller works with database tables.
 */
// @RequestMapping("/dbtables")
@Controller
public class DbTableController {

  private final TemplateService templateService;


  @Autowired
  public DbTableController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @InitBinder
  public void setAllowedFields(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }


  @RequestMapping(value = "/dbtables", method = RequestMethod.GET)
  public String processFindForm(DbTable table, Map<String, Object> model) {

    String name = table.getName();
    Collection<DbTable> results = null;
    String error = "";
    try {
      results = this.templateService.findDbTableByName(name);
    } catch (Exception e) {
      // error = e.getMessage();
      // TODO consider move to message properties file
      error = "ERROR: can not find or access data";
    }
    model.put("selections", results);
    model.put("error", error);
    return "dbtables/dbtablesList";
  }

  /**
   * Custom handler for displaying an Table.
   *
   * @param tableId the ID of the DbTable to display
   * @return a ModelMap with the model attributes for the view
   * @throws SQLException
   * @throws DataAccessException
   */
  @RequestMapping("/dbtables/{dbtableId}")
  public ModelAndView showDbtable(@PathVariable("dbtableId") int tableId)
      throws DataAccessException, SQLException {
    ModelAndView mav = new ModelAndView("dbtables/dbtableDetails");
    mav.addObject(this.templateService.findDbTableById(tableId));
    return mav;
  }

  
  
  /** REST Methods */
  
	@RequestMapping(value = "/dbtables/savetableinfo/{name},{dbconnectionId},{comments}", method = RequestMethod.GET)
	public ResponseEntity<Object> saveShortDbTableInfo(@PathVariable String name,
			@PathVariable String dbconnectionId, @PathVariable String comments) {
		JsonMessage msg = new JsonMessage();
		msg.setStatus(Status.ERROR);
		msg.setMessage("ERROR: can not add table info");
		DbTable dbtable;
		try {
			dbtable = new DbTable();
			dbtable.setName(name);
			dbtable.setDbconnectionId(Integer.parseInt(dbconnectionId));
			dbtable.setComments(comments);
			this.templateService.saveShortDbTableInfo(dbtable);
	        msg.setStatus(Status.SUCCESS);
	        msg.setMessage("Table "+name+" was successfully saved");
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
		}
		finally {
			dbtable = null;
		}
	}

	@RequestMapping(value="/dqoutput/{dbconnectionId}", method = RequestMethod.GET)
	@ApiOperation(value = "Find all the tables in db connection.", notes = "Find all the tables in db connection.")
	public ResponseEntity<List<DbTable>> showDbtablesByDbConnectionId(@PathVariable("dbconnectionId") int dbconnectionId)throws DataAccessException, SQLException {
	    List<DbTable> dbTables = this.templateService.findDbTablesByDbConnectionId(dbconnectionId);
	    return new ResponseEntity<>(dbTables, HttpStatus.OK);
	  }

}
