// when refreshing the page, we keep the selected table name in window.sessionStorage and use it for display. 
// sessionStorage is similar to Window.localStorage, the only difference is while data stored in localStorage has no expiration set, 
// data stored in sessionStorage gets cleared when the page session ends.
window.onbeforeunload = function() {
	DATA_PROFILE.saveTableNameInStorage();
}
	
// some web browsers (like Opera) do not acknowledge 'onbeforeunload', 
// so it is more safe to have same function in 'onunload' too
window.onunload = function() {
	DATA_PROFILE.saveTableNameInStorage();
};

// when loading the page, we check if we have selected table in the sessionStorage
// if we have any selected table, we display info about the table
window.onload = function() {
	DATA_PROFILE.getTableNameInStorage();
};


var DATA_PROFILE = (function(){

	// Gets data of the selected table from back end.  The data type is JSON.
	var getDQ = function(selectedTable) {
		$.ajax({
			url: 'http://localhost:8080/jobs/dq/' + selectedTable,
			type: 'GET',
			contentType: 'application/json; charset=utf-8',
			success: function (datatable, xhr) {
				if(datatable.numberColumns > 0){ // It means the data table exists in db. 
					buildHtmlTableFromJson(datatable);
					$('#tableName').val('');
				}
				else {
					clearDisplay();
					document.getElementById("numericStatsDisplay").innerHTML = "Table does not have proper data or table does not exist.";
				}
			},
			error: function (datatable, xhr) {
				clearDisplay();
				document.getElementById("numericStatsDisplay").innerHTML = "Please select data table.";
			}
		});
	};
	
	// Data structure objects to save info
	var dataHashMapForNumeric = {};
	var dataHashMapForCategoric = {};
	//Variables for showGraph()
	var hashMapForGraph = {};
	var keysOfHashMapForGraph = [];
	
	function initializeDataObj(){
		// initialize all the global data structure objects
		dataHashMapForNumeric = {};		// ** Empty the HashMap object
		dataHashMapForCategoric = {};	// ** Empty the HashMap object
		// For showGraph() function
		hashMapForGraph = {};
		keysOfHashMapForGraph = [];	
	}
	
	function buildHtmlTableFromJson(datatable){
	
		initializeDataObj();		// Empty all the data structure
	
		var tableName = "";
		var outTableName = "";
		var outNumericHeader = "";
		var outCategoricalHeader = "";
		var outNumericStatsData = "";
		var outCategoricalStatsData = "";
		var outTableInfo = "";
	
		$.each(datatable, function(key, value){
			if(key == 'stats'){
				var numericHeaderNames = [];
				var categoricalHeaderNames = [];
				$.each(value, function(key1, value1){
					// For NUMERICAL STATS Header
					if(numericHeaderNames.length == 0 && value1.hasOwnProperty('mean')){
						numericHeaderNames = Object.keys(value1);	// get all the keys of value1
						var i; 
						outNumericHeader = "<tr>";
						for(i = 0; i < numericHeaderNames.length; i++) {
							if(numericHeaderNames[i].indexOf("_") > -1){
								outNumericHeader += "<th class=\"tableHeader\"><div>"+numericHeaderNames[i].substring(0,numericHeaderNames[i].indexOf("_")+1)+"<br>"+ 
													numericHeaderNames[i].substring(numericHeaderNames[i].indexOf("_")+1)+"</div></th>";							
							}
							else
								outNumericHeader += "<th class=\"tableHeader\"><div>"+numericHeaderNames[i]+"</div></th>";
						}
						outNumericHeader += "<th class=\"tableHeader\"><div>Bounds</div></th></tr>";
					}
					
					// For CATEGORICAL STATS Header
					if(categoricalHeaderNames.length == 0 && !value1.hasOwnProperty('mean')){
						categoricalHeaderNames = Object.keys(value1);	// get all the keys of value1
						var i; 
						outCategoricalHeader = "<tr>";
						for(i = 0; i < categoricalHeaderNames.length; i++) {
							if(categoricalHeaderNames[i] == 'cat_6') {
								return false;
							}	// We display up to cat_5 in the UI
	
							if(categoricalHeaderNames[i].indexOf("_") > -1 && ( categoricalHeaderNames[i].indexOf('non') === 0 || categoricalHeaderNames[i].indexOf('missing') === 0 )){
								outCategoricalHeader += "<th class=\"tableHeader\"><div>"+categoricalHeaderNames[i].substring(0,categoricalHeaderNames[i].indexOf("_")+1)+"<br>"+ 
															categoricalHeaderNames[i].substring(categoricalHeaderNames[i].indexOf("_")+1)+"</div></th>";
							}
							else
								outCategoricalHeader += "<th class=\"tableHeader\"><div>"+categoricalHeaderNames[i]+"</div></th>";						
						}
						outCategoricalHeader += "</tr>";
					}
	
					if(numericHeaderNames.length != 0 && categoricalHeaderNames.length != 0 )
						return false;
				});
	
				$.each(value, function(key1, value1){
					var dataStr;
					var str;
					var keyOfHashMap = "";	// Actually this is 'columnName'
					if(value1.hasOwnProperty('mean')){				// for NUMERICAL STATS
						outNumericStatsData += "<tr>";
						dataStr = [];
						var prevValue2;
						$.each(value1, function(key2, value2){		// for each row of table
							if(typeof value2 == 'number' && value2 % 1 != 0)	// if the value2 is decimal number
								value2 = truncateDecimal(value2);
	
							// id for each element: table name(tableName), column name(), and key2 
							if(key2 == 'min' || key2 == 'max'){
								outNumericStatsData += "<td><div id=\""+tableName+"_"+keyOfHashMap+"_"+key2+"\">"+value2+"</div></td>";
							}
							else{
								outNumericStatsData += "<td><div>"+value2+"</div></td>";
							}
	
							// DATA COLLECTION for NUMERICAL STATS GRAPH
							if(key2 == 'columnName'){	// <== 'columnName' must be assigned to 'keyOfHashMap' FIRST !!!
								keyOfHashMap = value2;
							}
							else if (key2.indexOf('p') === 0){
								if(prevValue2 == value2) 
									dataStr.splice(-1,1);	// ** remove previous duplicate value2 from the data list for correct graph display
	
								str = JSON.parse("{\"count\":\""+value2+"\", \"percentile\":"+key2.substring(1)+", \"distribution\":"+0+"}");
								dataStr.push(str);
								
								prevValue2 = value2;
							}
							// End of DATA COLLECTION for NUMERICAL STATS GRAPH
						});
						outNumericStatsData += "<td><div class=\"m-b-20\">"+
	                            "<a data-toggle=\"modal\" href=\"#modalNarrower\" class=\"btn btn-sm\" onClick=\"DATA_PROFILE.getBoundsInfoForCol('"+tableName+"', '"+keyOfHashMap+"', 'modal');\">Set Bounds</a>"+
								"</div></td></tr>";
						
						// Data for NUMERICAL STATS graph is saved in dataHashMapForNumeric
						if(dataStr.length > 1)	// We will display only when there is change in NUMERICAL STATS values
							dataHashMapForNumeric[keyOfHashMap] = dataStr;
					}
					else {											// for CATEGORICAL STATS
						var catArrayMap = {};						// used for combination of 'cat_' and 'freq_' when generating 'str'
						outCategoricalStatsData += "<tr>";
						dataStr = [];
	
						$.each(value1, function(key2, value2){		// for each row of table
							if(key2 == 'cat_6') { return false; }	// We display up to cat_5 in the UI
							if(typeof value2 == 'number' && value2 % 1 != 0)	// if the value2 is decimal number
								value2 = truncateDecimal(value2);
							
							outCategoricalStatsData += "<td><div>"+value2+"</div></td>";
	
							// Data collection for CATEGORICAL STATS Bar Chart
							if(key2 == 'columnName'){
								keyOfHashMap = value2;
							}
							else if(key2.indexOf('cat') === 0){
								catArrayMap[key2.substring(4)] = value2;	// save category name in the catArrayMap with the key number eg. 'cat_2' ==> '2'
							}
							else if (key2.indexOf('freq') === 0 && catArrayMap[key2.substring(5)] != null){	// if the category name is not null
								str = JSON.parse("{\"category\":\"cat_"+key2.substring(5)+": " + 			// eg. 'cat_' + '2' ==> 'cat_2'
										catArrayMap[key2.substring(5)].substring(0, 20) + 					// limit the length of category name up to 20
										"\", \"frequency\":" + value2 + "}");
								dataStr.push(str);
							}
							// End of data collection for CATEGORICAL STATS Bar Chart
						});
						outCategoricalStatsData += "</tr>";
						if(dataStr.length > 0)	// We do not store data into HashMap if dataStr has nothing
							dataHashMapForCategoric[keyOfHashMap] = dataStr;
					}
				});
				outNumericHeader += outNumericStatsData;
				outCategoricalHeader += outCategoricalStatsData;
			}
			else if(key == 'tableName') {
				tableName = value;
				outTableName = "<h3 class=\"block-title\">Table Name: "+tableName+"</h3>";
			}
			else {
				outTableInfo += "<tr><th width=\"25%\"><div>" + key +"</div></th>" + "<td><div class=\"value\">" + value +"</div></td></tr>";
			}	
		});
	
		var colNames = Object.keys(dataHashMapForNumeric);
	
		// for NUMERICAL TABLE MIN MAX COLOR CHANGE
		for (var i = 0; i < colNames.length; i++) {
			getBoundsInfo(tableName, colNames[i], 'initialization');
		}
		
		this.returnDataHashMapForNumeric = function(){
			return dataHashMapForNumeric;
		}
	
		// for NUMERICAL STATS GRAPH
		var outColNames =	"<tr><td class=\"tableNamesForGraph\">";
		for (var i = 0; i < colNames.length; i++) {
				outColNames += 	"<table id=\"numericalGraphDisplay\">" +
									"<tr><th class=\"tableHeader\"><div class=\"columnName\">" +
										"<button class=\"btn btn-sm btn-alt\" id=\"getGraphBtn\" onClick=\"DATA_PROFILE.showGraph(\'"+colNames[i]+"\')\">" + 
											colNames[i] +
										"</button></div></th>" +
									"</tr>" +
								"</table>";
		}
		outColNames +=			"</td>" +
								"<td class=\"currentColumns\">" +
									"<table id=\"columnsInGraph\">" +
										"<tr><td><div id=\"columnInGraph\"></div></td>" +
										"</tr>" +
									"</table>" +
								"</td>" +
								"<td>" +
									"<div class=\"notifyMaxInGraph\" id=\"notifyMaxInGraph\"></div>" +
									"<div  class=\"vizBox\" id=\"vizBox\">Please click any button on the left to display graph</div>" +
								"</td>" +
							"</tr>";
	
		// for CATEGORICAL STATS BAR CHART
		var catgoryNames = Object.keys(dataHashMapForCategoric);
		var outCatNames =	"<tr><td class=\"tableNamesForGraph\">";
		for (var i = 0; i < catgoryNames.length; i++) {
			outCatNames += 		"<table id=\"numericalBarChartDisplay\">" +
									"<tr><th class=\"tableHeader\"><div class=\"columnName\">" +
										"<button class=\"btn btn-sm btn-alt\" id=\"getBarChartBtn\" onClick=\"DATA_PROFILE.showBarChart(\'"+catgoryNames[i]+"\')\">" + 
											catgoryNames[i] + 
										"</button></div></th>" +
									"</tr>" +
								"</table>";
		}
		outCatNames += 			"</td>" +
								"<td><div class=\"vizBarChart\" id=\"vizBarChart\">Please click any button on the left to display Bar Chart</div><div id=\"categoryName\"></div>" +
								"</td>" +
							"</tr>";
	
		// Display all the info
		document.getElementById("tableNameDisplay").innerHTML = outTableName;
		document.getElementById("tableInfoDisplay").innerHTML = outTableInfo;
		document.getElementById("numericalStatsTitle").innerHTML = "<h3 class=\"block-title\">Numerical Stats</h3>";
	    document.getElementById("numericStatsDisplay").innerHTML = outNumericHeader;
		document.getElementById("numericalGraphTitle").innerHTML = "<h3 class=\"block-title\">Distribution Graph</h3>";
		document.getElementById("numericGraphTable").innerHTML = outColNames;
	
		// Display categorical data only when there is categorical data existing
	    if(outCategoricalHeader != "") {
	    	document.getElementById("categoricalStatsTitle").innerHTML = "<h3 class=\"block-title\">Categorical Stats</h3>";
	        document.getElementById("categoricalStatsDisplay").innerHTML = outCategoricalHeader;
	    	document.getElementById("categoricalBarChartTitle").innerHTML = "<h3 class=\"block-title\">Categorical Stats Bar Chart</h3>";
	    	document.getElementById("categoricalBarChartTable").innerHTML = outCatNames;
	    }
	    else {
	    	document.getElementById("categoricalStatsTitle").innerHTML = "<h3 class=\"block-title\">There are no categorical data in the table</h3>";
		    document.getElementById("categoricalStatsDisplay").innerHTML = "";
			document.getElementById("categoricalBarChartTitle").innerHTML = "";
			document.getElementById("categoricalBarChartTable").innerHTML = "";
	    }
	}	// End of function buildHtmlTableFromJson(datatable)
	
	function clearDisplay() {
		// Remove all the displaying info
		document.getElementById("tableNameDisplay").innerHTML = "";
		document.getElementById("tableInfoDisplay").innerHTML = "";
		document.getElementById("numericalStatsTitle").innerHTML = "";
	    document.getElementById("numericStatsDisplay").innerHTML = "";
		document.getElementById("numericalGraphTitle").innerHTML = "";
		document.getElementById("numericGraphTable").innerHTML = "";
		document.getElementById("categoricalStatsTitle").innerHTML = "";
	    document.getElementById("categoricalStatsDisplay").innerHTML = "";
		document.getElementById("categoricalBarChartTitle").innerHTML = "";
		document.getElementById("categoricalBarChartTable").innerHTML = "";
	}
	
	var getBoundsInfo = function (tableName, columnName, option){
		var dataToBeSent = 'tableName='+tableName+'&columnName='+columnName;
	
		$.ajax({
			url: '/findDqsBounds',
			data: dataToBeSent,
			type: 'post',
			success: function(dqsBounds, xhr){
				if(option == 'modal'){
					if(dqsBounds == null || dqsBounds == ""){
						document.getElementById("tableName").value = tableName;
						document.getElementById("columnName").value = columnName;
						document.getElementById("minVal").value = "";
						document.getElementById("maxVal").value = "";
					}
					else {
						document.getElementById("tableName").value = dqsBounds.tableName;
						document.getElementById("columnName").value = dqsBounds.columnName;
						document.getElementById("minVal").value = dqsBounds.minVal;
						document.getElementById("maxVal").value = dqsBounds.maxVal;
					}
					document.getElementById("modalMsg").innerHTML = "<p>Please set MIN and MAX values for <b>"+columnName+"</b> column.</p>";	
				}
				else if(option == 'initialization'){
					if(dqsBounds != null && dqsBounds != ""){
						changeMinMaxColor(dqsBounds.tableName, dqsBounds.columnName, dqsBounds.minVal, dqsBounds.maxVal);
					}
				}
			},
			error: function (dqsBounds, xhr){
				document.getElementById("modalMsg").innerHTML = "<p>There was a problem with retrieving data.</p>";	
			}		
		});
	}	
	
	var truncateDecimal = function(num){
		return Number(num).toFixed(2);
	}

	function displayWithSetTimeout(id, content){
		document.getElementById(id).innerHTML = "";
		setTimeout(function(){document.getElementById(id).innerHTML = content;}, 1000);
	}
	
	function changeMinMaxColor(tableName, columnName, minVal, maxVal){
		
		if(minVal == null || minVal == "")
			document.getElementById(tableName+"_"+columnName+"_min").style.color = "white";
		else
			document.getElementById(tableName+"_"+columnName+"_min").style.color = 
				(parseFloat(minVal) > parseFloat(document.getElementById(tableName+"_"+columnName+"_min").innerHTML)) ? "red" : "white";
	
		if(maxVal == null || maxVal == "")
			document.getElementById(tableName+"_"+columnName+"_max").style.color = "white";
		else
			document.getElementById(tableName+"_"+columnName+"_max").style.color = 
				(parseFloat(maxVal) < parseFloat(document.getElementById(tableName+"_"+columnName+"_max").innerHTML)) ? "red" : "white";
	}


	// Return Block: Namespace for public function which can be called from outside of DATA_PROFILE function
	// All the private functions should be located before this Return Block.
	return {
		
		//self-invoking function: selecting table from the drop down list
		getSelectedTable: (function(){
			$(".dropdown-menu li a").click(function(){
				var selectedTable = $(this).attr('id');							// Get the table name(id) from drop down list
				DATA_PROFILE.dropDownAction(selectedTable);
			});
		})(),

		dropDownAction: function(selectedTable){
			$(".dropdown-menu li").removeClass('selected');					// remove all 'selected' first
			$("#"+selectedTable).parent().addClass('selected');				// then put a new 'selected' in the <li> tag
			$(".dropdown-menu li a").children("i").remove();				// remove all 'check-mark'
			$("#"+selectedTable).append("<i class=\"fa fa-check check-mark\"></i>");	// then append a new 'check-mark'
			$("#selectTbl").text($("#"+selectedTable).text());				// display the current table name in the drop down
		
			if($("#"+selectedTable).attr('id') == "defaultDisplay")			// if the user select "-- -- Select Table -- --", we don't call getDQ(selectedTable)
				clearDisplay();
			else
				getDQ(selectedTable);
		},

		getBoundsInfoForCol: function(tableName, columnName, option){
			getBoundsInfo(tableName, columnName, option);
		},

		saveBounds: function (){
			var dataToBeSent = {
				tableName : $("#tableName").val(),
				columnName: $("#columnName").val(),
				minVal: $("#minVal").val(),
				maxVal: $("#maxVal").val()
		    };
			
			// Input check conditions
			// 1. if input is not number
			if(isNaN(dataToBeSent.minVal) || isNaN(dataToBeSent.maxVal)){
				displayWithSetTimeout("modalMsg", "<p>The inputs have to be numeric numbers. Please correct the input.</p>");
				return false;
			}
			// 2. if MIN is not less than MAX
			if (parseFloat(minVal.value) >= parseFloat(maxVal.value)){
				displayWithSetTimeout("modalMsg", "<p>MIN value should be lower than MAX value. Please correct the input.</p>");
				return false;
			}
			
			$.ajax({
				url: '/saveBounds',
				data: dataToBeSent,
				type: 'post',
				success: function(dqsbounds, xhr){
					displayWithSetTimeout("modalMsg", "<p>Bounds value of "+dqsbounds.columnName+" was saved.</p>");
					changeMinMaxColor(dqsbounds.tableName, dqsbounds.columnName, dqsbounds.minVal, dqsbounds.maxVal);
				},
				error: function (dqsbounds, xhr) {
					displayWithSetTimeout("modalMsg", "<p>Saving bounds value of "+dataToBeSent.columnName+" failed.</p>");
				}
			});
		},

		showGraph: function(columnName) {
			var MAX_VALUE = 5;	// Max number of columns to be displayed in the graph
			document.getElementById("vizBox").innerHTML = "";
			document.getElementById("notifyMaxInGraph").innerHTML = "";
		
			if(columnName in hashMapForGraph){		// If the column data is already in the hasMap, we delete it. 
				delete hashMapForGraph[columnName];
			}
			else {
				if(Object.keys(hashMapForGraph).length < MAX_VALUE){	// Length of keys is hashMap's size
					hashMapForGraph[columnName] = dataHashMapForNumeric[columnName];
				}
				else{
					document.getElementById("notifyMaxInGraph").innerHTML = "You can compare up to 5 columns !!!";
				}
			}
		
			keysOfHashMapForGraph = Object.keys(hashMapForGraph);
		
			if(keysOfHashMapForGraph.length == 0) {	// if there is no data to display
				document.getElementById("vizBox").innerHTML = "";
				document.getElementById("columnInGraph").innerHTML = "";
				document.getElementById("vizBox").style.minHeight = "0px";	// If there is no data selected for graph, it should become 0 size.
			}
			else {		
				var strForCurrentCol = "";
				var values = null;
				var dataForGraph = [];
				var format = d3.time.format("%y");// year without century as a decimal number [00,99].
		
				for (var i = keysOfHashMapForGraph.length-1; i >=0 ; i--) { 
					strForCurrentCol += "<div class=\"columnIndex\">"+keysOfHashMapForGraph[i]+": "+("0"+(i+1))+"</div>";
					values = hashMapForGraph[keysOfHashMapForGraph[i]];
					for(var j = 0; j < values.length; j++) {
						values[j].distribution = (i+1001);
						dataForGraph.push(values[j]);
					}
				}
		
				document.getElementById("columnInGraph").innerHTML = strForCurrentCol;
				// Dynamic Graph div height size change
				document.getElementById("vizBox").style.minHeight = (keysOfHashMapForGraph.length*50 + 220) + "px";
				d3plus.viz()
				.container("#vizBox")
				.data(dataForGraph)
				.type("box")
				.id("count")
				.x("percentile")
				.time({"value": "distribution", "format": format})
				.y("distribution")
				.ui([{
				    "label": "Visualization Type",
				    "method": "type",
				    "value": ["scatter","box"]
				  }])
				.draw();
			}
		},

		showBarChart: function(categoryName) {
			document.getElementById("vizBarChart").innerHTML = "";
			document.getElementById("categoryName").innerHTML = categoryName;
			
			// Dynamic Bar Chart div height and width size change
			document.getElementById("vizBarChart").style.minHeight = 400 + "px";
			document.getElementById("vizBarChart").style.maxWidth  = (dataHashMapForCategoric[categoryName].length*135 + 120) + "px";
			
			d3plus.viz()
			.container("#vizBarChart")
			.data(dataHashMapForCategoric[categoryName])
			.type("bar")
			.id("category")
			.x("category")
			.y("frequency")
			.text("frequency")
			.draw();
		},
		
		saveTableNameInStorage: function() {
			window.sessionStorage.setItem("currentDqsTableName", $("#selectTbl").text());
		},
		
		getTableNameInStorage: function() {
			var tableNameSaved = window.sessionStorage.getItem("currentDqsTableName");
			if( !(tableNameSaved == null || tableNameSaved == "" || tableNameSaved == "-- -- Select Table -- --") ){
				DATA_PROFILE.dropDownAction(tableNameSaved);
			}
		},
		
		clearDqTableInSessionStorage: function(){
			// Need to clear the #selectTbl text first to prevent:
			// the table's name being saved again into sessionStorage in the dqstats.js just before logout
			$("#selectTbl").text("");
			window.sessionStorage.removeItem("currentDqsTableName");
		}		
	};
	// End of Return Block

})();
