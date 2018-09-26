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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.dbadmin.model.Businessrule;
import org.dbadmin.model.JsonMessage;
import org.dbadmin.model.JsonMessage.Status;
import org.dbadmin.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.wordnik.swagger.annotations.ApiOperation;

/**
 * Spring MVC controller for BusinessRule Domain Object
 */
@Controller
public class BusinessruleController {

  private final TemplateService templateService;


  @Autowired
  public BusinessruleController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @InitBinder
  public void setAllowedFields(WebDataBinder dataBinder) {
    dataBinder.setDisallowedFields("id");
  }

  @RequestMapping(value = "/businessrules/new", method = RequestMethod.GET)
  public String initCreationForm(Map<String, Object> model) {
    Businessrule businessrule = new Businessrule();
    model.put("businessrule", businessrule);
    return "businessrules/createOrUpdateBusinessruleForm";
  }

  @RequestMapping(value = "/businessrules/new", method = RequestMethod.POST)
  public String processCreationForm(@Valid Businessrule rule, BindingResult result) {
    if (result.hasErrors()) {
      return "businessrules/createOrUpdateBusinessruleForm";
    } else {
      this.templateService.saveBusinessrule(rule);
      return "redirect:/businessrules/" + rule.getId();
    }
  }

  @RequestMapping(value = "/businessrules/find", method = RequestMethod.GET)
  public String initFindForm(Map<String, Object> model) {
    model.put("businessrule", new Businessrule());
    return "businessrules/findBusinessrules";
  }

  @RequestMapping(value = "/businessrules", method = RequestMethod.GET)
  public String processFindForm(Businessrule businessrule, BindingResult result,
      Map<String, Object> model) {

    // allow parameterless GET request for /rules to return all records
    if (businessrule.getName() == null) {
      businessrule.setName(""); // empty string signifies broadest possible search
    }
    {
    }

    // find by name
    String ruleName = businessrule.getName();
    Collection<Businessrule> results = this.templateService.findBusinessruleByName(ruleName);
    if (results.isEmpty()) {
      // no rules found
      result.rejectValue("name", "notFound", "not found");
      return "businessrules/findBusinessrules";
    } else if (results.size() == 1) {
      // 1 was found
      businessrule = results.iterator().next();
      return "redirect:/businessrules/" + businessrule.getId();
    } else {
      // multiple were found
      model.put("selections", results);
      return "businessrules/businessrulesList";
    }
  }

  @RequestMapping(value = "/businessrules/{businessruleId}/edit", method = RequestMethod.GET)
  public String initUpdateBusinessruleForm(@PathVariable("businessruleId") int ruleId,
      Model model) {
    Businessrule rule = this.templateService.findBusinessruleById(ruleId);
    model.addAttribute(rule);
    return "businessrules/createOrUpdateBusinessruleForm";
  }

  @RequestMapping(value = "/businessrules/{businessruleId}/edit", method = RequestMethod.POST)
  public String processUpdateBusinessruleForm(@Valid Businessrule rule, BindingResult result,
      @PathVariable("businessruleId") int ruleId) {
    if (result.hasErrors()) {
      return "businessrules/createOrUpdateBusinessruleForm";
    } else {
      rule.setId(ruleId);
      this.templateService.saveBusinessrule(rule);
      return "redirect:/businessrules/{businessruleId}";
    }
  }

  /**
   * Custom handler for displaying an Rule.
   *
   * @param ruleId the ID of the Rule to display
   * @return a ModelMap with the model attributes for the view
   */
  @RequestMapping("/businessrules/{businessruleId}")
  public ModelAndView showBusinessrule(@PathVariable("businessruleId") int ruleId) {
    ModelAndView mav = new ModelAndView("businessrules/businessruleDetails");
    Businessrule brule = this.templateService.findBusinessruleById(ruleId);
    mav.addObject(brule);
    mav.addObject(brule.getAssociatedTasklist());
    return mav;
  }
  
  
  /*** REST methods *****/
  
 
    /**
     * Add a Businessrule as dependent to a host Task and set its priority
     * Usage example: http://localhost:8080/businessrules/addrelatedrule/1,2,33 
     * 
     * @param taskIdParam
     * @param relatedBusinessruleIdParam
     * @param relatedBusinessrulePriorityParam
     * @return
     */
	@RequestMapping(value = "/businessrules/addrelatedrule/{taskIdParam},{relatedBusinessruleIdParam},{relatedBusinessrulePriorityParam}", method = RequestMethod.GET)
	public ResponseEntity<Object> addRelatedBusinessrule2Task(@PathVariable String taskIdParam,
			@PathVariable String relatedBusinessruleIdParam, @PathVariable String relatedBusinessrulePriorityParam) {
		JsonMessage msg = new JsonMessage();
		msg.setStatus(Status.ERROR);
		msg.setMessage("ERROR: can not add businessrule");
		try {
			int taskId = Integer.parseInt(taskIdParam);
			int relatedBusinessruleId = Integer.parseInt(relatedBusinessruleIdParam);
			int relatedBusinessrulePriority = Integer.parseInt(relatedBusinessrulePriorityParam);
			this.templateService.addRelatedBusinessrule2Task(taskId, relatedBusinessruleId, relatedBusinessrulePriority);
	        msg.setStatus(Status.SUCCESS);
	        msg.setMessage("Businessrle id="+relatedBusinessruleId+" was successfully added to Task: id="+taskId+", priority="+relatedBusinessrulePriority);
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
		}
	}
	
	/**
	 * Usage example: http://localhost:8080/businessrules/deleterelatedrule/1,2
	 * @param taskIdParam
	 * @param relatedBusnessruleIdParam
	 * @return JSON object
	 */
	@RequestMapping(value = "/businessrules/deleterelatedrule/{taskIdParam},{relatedBusnessruleIdParam}", method = RequestMethod.GET)
	public ResponseEntity<Object> deleteRelatedBusinessrule(@PathVariable String taskIdParam, @PathVariable String relatedBusnessruleIdParam) {
		JsonMessage msg = new JsonMessage();
		msg.setStatus(Status.ERROR);
		msg.setMessage("ERROR: can not delete related task");
		try {
			int taskId = Integer.parseInt(taskIdParam);
			int relatedBusnessruleId = Integer.parseInt(relatedBusnessruleIdParam);
			this.templateService.deleteRelatedBusnessrule(taskId, relatedBusnessruleId);
	        msg.setStatus(Status.SUCCESS);
	        msg.setMessage("Related Businessrule id="+relatedBusnessruleId+" was successfully deleted from host Task id="+taskIdParam);
			return new ResponseEntity<Object>(msg, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(msg, HttpStatus.FORBIDDEN);
		}
	}  
  

	  @RequestMapping(value = "/exoutput/findByTableId/{tableId}", method = RequestMethod.GET)
	  @ApiOperation(value = "Find business rules of table.", notes = "Find a table's all the business rules which is in execution_output table.")
	  public ResponseEntity<Object> restFindByTableId(@PathVariable("tableId") String tableId) {
	    List<Businessrule> list = this.templateService.findBusinessruleByTableId(Integer.valueOf(tableId));
	    return new ResponseEntity<Object>(list, HttpStatus.OK);
	  }


}
