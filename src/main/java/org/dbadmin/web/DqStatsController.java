package org.dbadmin.web;

import org.dbadmin.model.DqsBounds;
import org.dbadmin.service.TemplateService;

/**
 * Created by Jake Lee 
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DqStatsController {
  private final TemplateService templateService;

  @Autowired
  public DqStatsController(TemplateService templateService) {
    this.templateService = templateService;
  }

  @RequestMapping(value = "/saveBounds", method = RequestMethod.POST)
  public ResponseEntity<DqsBounds> saveDqsBounds(@RequestParam String tableName, 
      @RequestParam String columnName, @RequestParam String minVal, @RequestParam String maxVal) {  	
    Double minValue = StringUtils.isEmpty(minVal) ? null : Double.parseDouble(minVal);
    Double maxValue = StringUtils.isEmpty(maxVal) ? null : Double.parseDouble(maxVal);

  	DqsBounds dqsBounds = new DqsBounds();
  	dqsBounds.setTableName(tableName);
  	dqsBounds.setColumnName(columnName);
  	dqsBounds.setMinVal(minValue);
  	dqsBounds.setMaxVal(maxValue);
  	this.templateService.saveDqsBounds(dqsBounds);
    return new ResponseEntity<>(dqsBounds, HttpStatus.OK);
  }

  @RequestMapping(value = "/findDqsBounds", method = RequestMethod.POST)
  public ResponseEntity<DqsBounds> findDqsBoundsByTableAndColumn(@RequestParam String tableName, 
      @RequestParam String columnName) throws DataAccessException {
    DqsBounds dqsBounds = this.templateService.findDqsBoundsByTableAndColumn(tableName, columnName);
    return new ResponseEntity<>(dqsBounds, HttpStatus.OK);
  }      
}
