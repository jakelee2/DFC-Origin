// when refreshing the page, we keep the selected table name in window.sessionStorage and use it for display. 
// sessionStorage is similar to Window.localStorage, the only difference is while data stored in localStorage has no expiration set, 
// data stored in sessionStorage gets cleared when the page session ends.
window.onbeforeunload = function() {
	DATA_OUTPUT.saveTableNameInStorage();
}
	
// some web browsers (like Opera) do not acknowledge 'onbeforeunload', 
// so it is more safe to have same function in 'onunload' too
window.onunload = function() {
	DATA_OUTPUT.saveTableNameInStorage();
};

// when loading the page, we check if we have selected table in the sessionStorage
// if we have any selected table, we display info about the table
window.onload = function() {
	DATA_OUTPUT.getTableNameInStorage();
};


var DATA_OUTPUT = (function(){

	var dataHashMapForChart = {};
	
	// Gets tables of the selected dbConnection.
	var getTblOfDbConn = function(selectedDbConn, selectedDbConnName) {
		$.ajax({
			url: 'http://localhost:8080/dqoutput/' + selectedDbConn,
			type: 'GET',
			contentType: 'application/json; charset=utf-8',
			success: function (tableList, xhr) {
				clearDisplay();
				if(tableList.length > 0){ // It means the data table exists in db. 
					displayTablesFromList(tableList, selectedDbConnName);
					$('#tableName').val('');
				}
				else {
					document.getElementById("errorMessageDisplay").innerHTML = "Connection does not have proper table.";
				}
			},
			error: function (tableList, xhr) {
				clearDisplay();
				document.getElementById("errorMessageDisplay").innerHTML = "There was problem with retrieving connections.";
			}
		});
	};

	// Gets execution_output of the selected table.
	var getBusinessruleByTblId = function(tableId, tableName) {
		$.ajax({
			url: 'http://localhost:8080/exoutput/findByTableId/' + tableId,
			type: 'GET',
			contentType: 'application/json; charset=utf-8',
			success: function (businessrules, xhr) {
				clearResultDisplay();
				if(businessrules.length > 0){ // It means the data table exists in db. 					
					var i;
					for(i = 0; i < businessrules.length; i++){
						getExecutionOutpuByTblAndBruleId(tableId, businessrules[i].id, businessrules[i].name);
					}					
					displayOutput(businessrules, tableName);
				}
				else {

					document.getElementById("errorMessageDisplay").innerHTML = "The table doesn not have any relevant rule.";
				}
			},
			error: function (businessrules, xhr) {
				clearResultDisplay();
				document.getElementById("errorMessageDisplay").innerHTML = "There was error with retrieving relevant rule.";
			}
		});
	};
	
	var getExecutionOutpuByTblAndBruleId = function(tableId, bruleId, bruleName){
		$.ajax({
			url: 'http://localhost:8080/exoutput/findTopExecutionOutputByBusinessruleId/' +tableId + '/' + bruleId,
			type: 'GET',
			contentType: 'application/json; charset=utf-8',
			success: function (outputs, xhr) {
				if(outputs.length > 0){ // It means the data table exists in db. 
					var i;
					var dataStr = [];
					var countOfTextOutput;
					var str;
					for(i=0; i < outputs.length; i++) {
						countOfTextOutput = 0
						if (outputs[i].listOutput.length > 2){ // not '[]' in list_output field in execution_output table
							countOfTextOutput = outputs[i].listOutput.split(",").length;
							str = JSON.parse("{\"date\":\"" + 			// eg. 'cat_' + '2' ==> 'cat_2'					
									outputs[i].dateForTimestamp + 					
									"\", \"number of errors\":" + countOfTextOutput + "}");
							dataStr.push(str);

						}
					}
					dataHashMapForChart[bruleName] = dataStr;
					
				}
				else {
					document.getElementById("execOutputDisplay").innerHTML = "The businessrule doesn not have any relevant error.";
				}
			},
			error: function (outputs, xhr) {
				document.getElementById("execOutputDisplay").innerHTML = "There was error with retrieving relevant error.";
			}
		});
	};
	
	function displayTablesFromList(tableList, selectedDbConnName){
		
		var tableForTableList = "";
		var i; 
		for(i = 0; i < tableList.length; i++){
			tableForTableList += "<tr><th width=\"25%\"><div class=\"value\"><h5>" + tableList[i].id +"</h5></div></th>" + 
									 "<td>" +
										 "<div class=\"value\">" +
										 	"<a class=\"btn btn-sm\" onClick=\"DATA_OUTPUT.getExecOutputOfTable('"+tableList[i].id+"', '"+tableList[i].name+"');\">" + 
										 		tableList[i].name + 
										 	"</a>" + 
										 "</div>" + 
									"</td>" + 
								 "</tr>";
		}

	
		// Display all the info
		document.getElementById("connNameDisplay").innerHTML = "<h3 class=\"block-title\">TABLES IN CONNECTION : "+selectedDbConnName+"</h3>";
		document.getElementById("tableListOfConn").innerHTML = tableForTableList;
	
	}	// End of function displayTablesFromList(tableList, selectedDbConnName)

	
	function displayOutput(businessrules, tableName){
		var tableForOutputList = "";
		var i; 
		var tableForOutputBarChart = "";

		tableForOutputList += "<tr>" +
								"<td>" + 
									"<table class=\"tile\" id=\"businessRuleList\">" + 
											"<tr>" +
												"<th class=\"tableHeader\">" +
													"<div><h5>Rule ID</h5></div>" +
												"</th>" +
												"<th class=\"tableHeader\">" +
													"<div><h5>Rule Name</h5></div>" +
												"</th>" +
											"</tr>" ;
		for(i = 0; i < businessrules.length; i++){
			tableForOutputList += 
											"<tr>" +
											  	"<th width=\"25%\">" +
											  		"<div class=\"value\" onClick=\"DATA_OUTPUT.showBarChart(\'"+businessrules[i].name+"\')\">" +
											  			"<a class=\"businessruleName\"><h5>" + businessrules[i].id + "</h5></a>" +
											  		"</div>" +
											  	"</th>" + 
											  	"<td>" +
											  		"<div class=\"value\" onClick=\"DATA_OUTPUT.showBarChart(\'"+businessrules[i].name+"\')\">" +
											  			"<a class=\"btn btn-sm businessruleName\" >" + businessrules[i].name +"</a>" +
											  		"</div>" +
											  	"</td>" +
									  		"</tr>" ;
		}
		
		tableForOutputList +=   	"</table>" +
								"</td>" +
							  "</tr>";

		tableForOutputBarChart += 
							  "<tr>" +
								"<td>" +
									"<div class=\"vizBarChart\" id=\"vizBarChart\">" +
										"<a class=\"btn btn-sm\">Please click any [Rule ID] or [Rule Name] above to display Bar Chart.</a>" +
									"</div><div id=\"bruleName\"></div>" +
								"</td>" +
							  "</tr>";
			
		document.getElementById("execOutputList").innerHTML = "<h3 class=\"block-title\">Business Rules Of : [ "+tableName+" ] Table</h3>";
	    document.getElementById("execOutputDisplay").innerHTML = tableForOutputList;
	    document.getElementById("execOutputBarChart").innerHTML = tableForOutputBarChart;
	}
	
	function clearDisplay() {
		// Remove all the displaying info
		document.getElementById("connNameDisplay").innerHTML = "";
		document.getElementById("tableListOfConn").innerHTML = "";
		document.getElementById("execOutputList").innerHTML = "";
	    document.getElementById("execOutputDisplay").innerHTML = "";
	    document.getElementById("execOutputBarChart").innerHTML = "";
	    document.getElementById("errorMessageDisplay").innerHTML = "";
	    
	}

	function clearResultDisplay() {
		// Remove all the displaying info
		document.getElementById("execOutputList").innerHTML = "";
	    document.getElementById("execOutputDisplay").innerHTML = "";
	    document.getElementById("execOutputBarChart").innerHTML = "";
	    document.getElementById("errorMessageDisplay").innerHTML = "";
	    
	}


	// Return Block: Namespace for public function which can be called from outside of DATA_OUTPUT function
	// All the private functions should be located before this Return Block.
	return {

		//self-invoking function: selecting table from the drop down list
		getSelectedTable: (function(){
			$(".dropdown-menu li a").click(function(){
				var selectedDbConn = $(this).attr('id');							// Get the table name(id) from drop down list
				DATA_OUTPUT.dropDownAction(selectedDbConn);
			});
		})(),

		dropDownAction: function(selectedDbConn){
			$(".dropdown-menu li").removeClass('selected');					// remove all 'selected' first
			$("#"+selectedDbConn).parent().addClass('selected');				// then put a new 'selected' in the <li> tag
			$(".dropdown-menu li a").children("i").remove();				// remove all 'check-mark'
			$("#"+selectedDbConn).append("<i class=\"fa fa-check check-mark\"></i>");	// then append a new 'check-mark'
			$("#selectTbl").children("a").text($("#"+selectedDbConn).text());		// display the current connection name in the drop down
			$("#selectTbl").children("a").attr('id', selectedDbConn);				// put current connection id in the drop down
		
			if($("#"+selectedDbConn).attr('id') == "defaultDisplay")			// if the user select "-- -- Select Connection -- --", we don't call getTblOfDbConn(selectedDbConn)
				clearDisplay();
			else
				getTblOfDbConn(selectedDbConn, $("#"+selectedDbConn).text());
		},

		getExecOutputOfTable: function(tableId, tableName){
			getBusinessruleByTblId(tableId, tableName);
		},

		showBarChart: function(bruleName) {
			document.getElementById("vizBarChart").innerHTML = "";
			document.getElementById("bruleName").innerHTML = "<h5>"+bruleName+"</h5>";
			
			// Dynamic Bar Chart div height and width size change
			document.getElementById("vizBarChart").style.minHeight = 400 + "px";
			document.getElementById("vizBarChart").style.maxWidth  = (dataHashMapForChart[bruleName].length*100 + 140) + "px";
			
			d3plus.viz()
			.container("#vizBarChart")
			.data(dataHashMapForChart[bruleName])
			.type("bar")
			.id("date")
			.x("date")
			.y("number of errors")
			.text("number of errors")
			.width(dataHashMapForChart[bruleName].length*100 + 120)
			.order({"value": "date", "sort": "desc"}) // for descending order of "date" in bar chart
			.draw();
		},
		
		saveTableNameInStorage: function() {
			window.sessionStorage.setItem("currentDqsTableName", $("#selectTbl").children('a').attr('id'));
		},

		getTableNameInStorage: function() {
			var tableNameSaved = window.sessionStorage.getItem("currentDqsTableName");
			if( !(tableNameSaved == null || tableNameSaved == "" || tableNameSaved == "-- -- Select Connection -- --") ){
				DATA_OUTPUT.dropDownAction(tableNameSaved);
			}
		},

		clearDqTableInSessionStorage: function(){
			// Need to clear the #selectTbl text first to prevent:
			// the table's name being saved again into sessionStorage in the dqstats.js just before logout
			$("#selectTbl").children('a').text("");
			window.sessionStorage.removeItem("currentDqsTableName");
		}

	};	// End of Return Block

})();
