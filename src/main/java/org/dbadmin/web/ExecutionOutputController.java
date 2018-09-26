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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.dbadmin.model.ExecutionOutput;
import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @author denys This REST controller works with Execution Output objects.
 */
@Controller
public class ExecutionOutputController {

  private final TemplateService templateService;


  @Autowired
  public ExecutionOutputController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @InitBinder
  public void setAllowedFields(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }


  /** REST Methods */
  
  @RequestMapping(value = "/exoutput/findByTaskId/{taskId}", method = RequestMethod.GET)
  public ResponseEntity<Object> restFindByTaskId(@PathVariable("taskId") String taskId) {
    List<ExecutionOutput> list = this.templateService.findExecutioOutputByTaskId(Integer.valueOf(taskId));
    return new ResponseEntity<Object>(list, HttpStatus.OK);
  }
  
  
  @RequestMapping(value = "/exoutput/findByBusinessruleId/{ruleId}", method = RequestMethod.GET)
  public ResponseEntity<Object> restFindByBusinessruleId(@PathVariable("ruleId") String ruleId) {
    List<ExecutionOutput> list = this.templateService.findExecutioOutputByBusinessruleId(Integer.valueOf(ruleId));
    return new ResponseEntity<Object>(list, HttpStatus.OK);
  }

  @RequestMapping(value = "/exoutput/findTopExecutionOutputByBusinessruleId/{tableId}/{bruleId}", method = RequestMethod.GET)
  @ApiOperation(value = "Find recent top 10 execution_output results.", notes = "Find recent top 10 execution_output results of a business rule of a table.")
  public ResponseEntity<Object> findTopExecutionOutputByBusinessruleId(@PathVariable("tableId") String tableId, @PathVariable("bruleId") String bruleId) {    
    Calendar cal  = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS",Locale.US);
    
    List<ExecutionOutput> list = this.templateService.findTopExecutionOutputByBusinessruleId(Integer.valueOf(tableId), Integer.valueOf(bruleId));
    for(int i = 0; i < list.size(); i++){
      cal.setTime(list.get(i).getCompleteTimestamp());
      list.get(i).setDateForTimestamp(sdf.format(cal.getTime()));
    }
    return new ResponseEntity<Object>(list, HttpStatus.OK);
  }
  
}
