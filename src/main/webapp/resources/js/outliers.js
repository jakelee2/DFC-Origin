// Trigger getDQ function by selecting table from the drop down list
$(function(){
	$(".dropdown-menu li a").click(function(){
		var selectedTable = $(this).text();					// Get the table name from drop down list
		$(".dropdown-menu li").removeClass('selected');		// remove all 'selected'
		$(this).parent().addClass('selected');				// then put a new 'selected'
		$(".dropdown-menu li a").children("i").remove();			// remove all 'check-mark'
		$(this).append("<i class=\"fa fa-check check-mark\"></i>");	// then append a new 'check-mark'
		$("#selectTbl").text(selectedTable);				// display the current table name in the drop down

		if($(this).attr('id') == "defaultDisplay")			// if the user select, "-- -- Select Table -- --", we don't call getDQ()
			clearDisplay();
		else {
            var outTableName = "<h3 class=\"block-title\">Table Name: "+selectedTable+"</h3>";
            document.getElementById("tableNameDisplay").innerHTML = outTableName;
            getOutliers(selectedTable);
        }
	});
});

var getOutliers = function(selectedTable) {
	$.ajax({
		url: 'http://localhost:8080/stats/outliers/' + selectedTable,
		type: 'GET',
		contentType: 'application/json; charset=utf-8',
		success: function (datatable, xhr) {
			if(datatable.length > 0){ // It means the data table exists in db.
				buildHtmlTableFromJson(datatable);
				$('#tableName').val('');
			}
			else {
				clearDisplay();
				document.getElementById("outliersDisplay").innerHTML = "There is no outliers data for this table.";
			}
		},
		error: function (datatable, xhr) {
			clearDisplay();
			document.getElementById("outliersDisplay").innerHTML = "Please select data table.";
		}
	});
};

function buildHtmlTableFromJson(datatable){

    var columns = [];
    var outliersDatas = [];

    $.each(datatable, function(key, value){
        columns.push(value.columnName);
        var outliersData = "";
        $.each(value.outliers, function(key1, value1){
            outliersData += "<tr>"
            outliersData += "<td><div>"+value1.id+"</div></td>";
            outliersData += "<td><div>"+value1.value+"</div></td>";
            outliersData += "</tr>"
        });
        outliersDatas.push(outliersData);
    });

    var outlierDatasOutput = "";

    for (var i = 0; i < columns.length; i++) {
        var columnName = columns[i];
        var outliersData = outliersDatas[i];

        outlierDatasOutput += "<div>";
        outlierDatasOutput += "<div><h3 class=\"block-title\">" + columnName + "</h3></div><br/>";
        outlierDatasOutput += "<table class=\"table tile\">";
        outlierDatasOutput += "<tr><th class=\"tableHeader\">Row Id</th><th class=\"tableHeader\">Row Value</th></tr>";
        outlierDatasOutput += outliersData;
        outlierDatasOutput += "</table>";
        outlierDatasOutput += "</div>";
        outlierDatasOutput += "<br />";
    }

	// Display all the info
    document.getElementById("outliersDisplay").innerHTML = outlierDatasOutput;

}	// End of function buildHtmlTableFromJson(datatable)

function clearDisplay() {
	// Remove all the displaying info
	document.getElementById("tableNameDisplay").innerHTML = "";
    document.getElementById("outliersDisplay").innerHTML = "";
}
