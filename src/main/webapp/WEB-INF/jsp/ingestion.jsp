<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!--[if IE 9 ]>
<html class="ie9">
<![endif]-->
<html>
<head>
<jsp:include page="./fragments/headStatics.jsp" />
</head>
<body id="skin-blur-ocean">
  <header id="header" class="media">
    <a href="javascript:;" id="menu-toggle"></a> <a class="logo pull-left" href="javascript:;">Admin Panel</a>
    <div class="media-body">
      <div class="media" id="top-menu">
        <div class="pull-left tm-icon">
          <a data-drawer="messages" class="drawer-toggle" href="javascript:;"> <i class="sa-top-message"></i> <i
            class="n-count animated">5</i> <span>Alerts</span>
          </a>
        </div>
        <div class="pull-left tm-icon">
          <a data-drawer="notifications" class="drawer-toggle" href="javascript:;"> <i class="sa-top-updates"></i> <i
            class="n-count animated">9</i> <span>History</span>
          </a>
        </div>
        <div id="time" class="pull-right">
          <span id="hours"></span> : <span id="min"></span> : <span id="sec"></span>
        </div>
      </div>
    </div>
  </header>
  <div class="clearfix"></div>
  <section id="main" class="p-relative" role="main">
    <jsp:include page="./fragments/sidebar.jsp" />

    <!-- Content -->
    <section id="content" class="container">
      <jsp:include page="./fragments/header.jsp" />

      <!-- Breadcrumb -->
      <ol class="breadcrumb hidden-xs">
        <li><a href="#">Home</a></li>
        <li class="active">Dashboard</li>
      </ol>
      <h4 class="page-title">DASHBOARD</h4>

      <!-- Dashboard Body -->
      <!-- Ingestion Progress Labels -->
      <div class="col-md-12">
        <h1>
          <span style="display: none;" class="label label-default" id="workingLabel">Working... Please wait...</span>
        </h1>
        <h1>
          <span style="display: none;" class="label label-success" id="connSuccessLabel">Connection Successful!</span>
        </h1>
        <h1>
          <span style="display: none;" class="label label-danger" id="connFailLabel">Connection Failed, Please
            Re-Check Connection Settings!</span>
        </h1>
        <h1>
          <span style="display: none;" class="label label-success" id="ingestSuccessLabel">Data Ingested
            Successfully!</span>
        </h1>
        <h1>
          <span style="display: none;" class="label label-danger" id="ingestFailLabel">Could Not Ingest Data!</span>
        </h1>
        <h1>
          <span style="display: none;" class="label label-success" id="saveSuccessLabel">Data Saved Successfully!</span>
        </h1>
      </div>

      <%-- Move to external js file --%>
      <script type="text/javascript">
              var hideAllLabels = function() {
                // put all labels in array
                var labels = [];
                labels.push(document.getElementById('workingLabel'));
                labels.push(document.getElementById('connSuccessLabel'));
                labels.push(document.getElementById('connFailLabel'));
                labels.push(document.getElementById('ingestSuccessLabel'));
                labels.push(document.getElementById('ingestFailLabel'));
                labels.push(document.getElementById('saveSuccessLabel'));
                
                for ( var label in labels) {
                  labels[label].style.display = "none";
                }
              };
            </script>

      <!-- TODO: Do this the correct way with forms -->
      <script type="text/javascript">
              var postConCheck = function() {
                var client = new XMLHttpRequest();
                
                client.open("post", "http://localhost:8080/jobs/connectivity",
                    true);
                
                client.setRequestHeader('Accept', 'application/json');
                client.setRequestHeader('Content-Type', 'application/json');
                
                var ip = document.getElementById("ip").value;
                var database = document.getElementById("database").value;
                var username = document.getElementById("username").value;
                var password = document.getElementById("password").value;
                
                var requestBody = '{"ip": "' + ip + '", "database": "'
                    + database + '", "username": "' + username
                    + '", "password": "' + password + '"}';
                
                hideAllLabels();
                document.getElementById('workingLabel').style.display = "block";
                
                client.onload = function() {
                  if (client.readyState == 4 && client.status == 200) {
                    hideAllLabels();
                    var response = JSON.parse(client.responseText);
                    if (response.status === "success") {
                      document.getElementById('connSuccessLabel').style.display = "block";
                    } else {
                      document.getElementById('connFailLabel').style.display = "block";
                    }
                  }
                };
                
                client.send(requestBody);
              };
            </script>
      <!-- Check Connection -->
      <div class="block-area" id="ingestion">
        <h3 class="block-title">Connectivity</h3>
        <div class="tile p-15">
          <form role="form">
            <div class="form-group">
              <input type="text" class="form-control input-sm" id="ip" placeholder="IP">
            </div>
            <div class="form-group">
              <input type="text" class="form-control input-sm" id="database" placeholder="Database">
            </div>
            <div class="form-group">
              <input type="text" class="form-control input-sm" id="username" placeholder="Username">
            </div>
            <div class="form-group">
              <input type="Password" class="form-control input-sm" id="password" placeholder="Password">
            </div>
            <button class="btn btn-sm btn-alt m-r-5" onClick="postConCheck();return false;">Check</button>
          </form>
        </div>
      </div>

      <hr class="whiter m-t-20">

      <!-- Ingest Data -->
      <!-- TODO: Do this the correct way with forms -->
      <script type="text/javascript">
              var generateRequestBody = function() {
                
                var ingestJobName = document.getElementById("ingestJobName").value;
                var sourceTable = document.getElementById("ingestSrcTbl").value;
                var targetTable = document.getElementById("ingestTrgTbl").value;
                var ip = document.getElementById("ip").value;
                var database = document.getElementById("database").value;
                var username = document.getElementById("username").value;
                var password = document.getElementById("password").value;
                
                var requestBody = '{"sourceTable": "' + sourceTable
                    + '", "targetTable": "' + targetTable
                    + '", "ingestJobName": "' + ingestJobName + '", "ip": "'
                    + ip + '", "database": "' + database + '", "username": "'
                    + username + '", "password": "' + password + '"}';
                
                return requestBody;
                
              };
              
              var postIngestion = function() {
                var client = new XMLHttpRequest();
                
                client.open("post", "http://localhost:8080/jobs/ingestion",
                    true);
                
                client.setRequestHeader('Accept', 'application/json');
                client.setRequestHeader('Content-Type', 'application/json');
                
                var requestBody = generateRequestBody();
                
                hideAllLabels();
                document.getElementById('workingLabel').style.display = "block";
                
                client.onload = function() {
                  if (client.readyState == 4 && client.status == 200) {
                    hideAllLabels();
                    var response = JSON.parse(client.responseText);
                    
                    if (response.status === "success") {
                      document.getElementById('ingestSuccessLabel').style.display = "block";
                    } else {
                      document.getElementById('ingestFailLabel').style.display = "block";
                    }
                  }
                };
                
                client.send(requestBody);
              };
              
              var saveIngestionJob = function() {
                var client = new XMLHttpRequest();
                
                client.open("post", "http://localhost:8080/jobs/save", true);
                
                client.setRequestHeader('Accept', 'application/json');
                client.setRequestHeader('Content-Type', 'application/json');
                
                var requestBody = generateRequestBody();
                
                hideAllLabels();
                document.getElementById('workingLabel').style.display = "block";
                
                client.onload = function() {
                  if (client.readyState == 4 && client.status == 200) {
                    hideAllLabels();
                    var response = JSON.parse(client.responseText);
                    document.getElementById('saveSuccessLabel').style.display = "block";
                  }
                };
                
                client.send(requestBody);
              };
            </script>
      <div class="block-area" id="ingestion">
        <h3 class="block-title">Ingestion</h3>
        <div class="tile p-15">
          <form role="form">
            <div class="form-group">
              <label for="exampleInputEmail1">Job Name</label> <input type="text" class="form-control input-sm"
                id="ingestJobName" placeholder="Ingestion Job Name">
            </div>
            <div class="form-group">
              <label for="exampleInputEmail1">Source Table</label> <input type="text" class="form-control input-sm"
                id="ingestSrcTbl" placeholder="Source Table">
            </div>
            <div class="form-group">
              <label for="exampleInputPassword1">Target Table</label> <input type="text" class="form-control input-sm"
                id="ingestTrgTbl" placeholder="Target Table">
            </div>
            <button class="btn btn-sm btn-alt m-r-5" onClick="saveIngestionJob();return false;">Save</button>
            <button class="btn btn-sm btn-alt m-r-5" onClick="postIngestion();return false;">Save and Run</button>
          </form>
        </div>
      </div>

      <script type="text/javascript">
              var generateValidationRequestBody = function() {
                
                var ingestJobName = document.getElementById("ingestJobName").value;
                var sourceTable = document.getElementById("ingestSrcTbl").value;
                var targetTable = document.getElementById("ingestTrgTbl").value;
                var ip = document.getElementById("ip").value;
                var database = document.getElementById("database").value;
                var username = document.getElementById("username").value;
                var password = document.getElementById("password").value;
                var columnName = document.getElementById("columnName").value;
                
                var requestBody = '{"sourceTable": "' + sourceTable
                    + '", "targetTable": "' + targetTable
                    + '", "ingestJobName": "' + ingestJobName + '", "ip": "'
                    + ip + '", "database": "' + database + '", "username": "'
                    + username + '", "password": "' + password
                    + '", "columnName": "' + columnName + '"}';
                
                return requestBody;
              };
              
              var validateStats = function() {
                var client = new XMLHttpRequest();
                
                client.open("post", "http://localhost:8080/jobs/validation",
                    true);
                
                client.setRequestHeader('Accept', 'application/json');
                client.setRequestHeader('Content-Type', 'application/json');
                
                var requestBody = generateValidationRequestBody();
                
                hideAllLabels();
                document.getElementById('workingLabel').style.display = "block";
                
                client.onload = function() {
                  if (client.readyState == 4 && client.status == 200) {
                    hideAllLabels();
                    var response = JSON.parse(client.responseText);
                    var outputString = "Source Row Count: "
                        + response.sourceRowCount + ", " + "Target Row Count: "
                        + response.targetRowCount + ", " + "Source Num Nulls: "
                        + response.sourceNumNulls + ", " + "Target Num Nulls: "
                        + response.targetNumNulls + ", " + "Source Min: "
                        + response.sourceMin + ", " + "Target Min: "
                        + response.targetMin + ", " + "Source Max: "
                        + response.sourceMax + ", " + "Target Max: "
                        + response.targetMax + ", " + "Source 50%: "
                        + response.sourceFiftyPercentile + ", "
                        + "Target 50%: " + response.targetFiftyPercentile + ".";
                    alert(outputString);
                    if (response.status === "OK") {
                      // add new labels
                    } else {
                      // add new labels
                    }
                  }
                };
                
                client.send(requestBody);
              };
            </script>
      <div class="block-area" id="validation">
        <h3 class="block-title">Validation</h3>
        <div class="tile p-15">
          <form role="form">
            <div class="form-group">
              <label for="exampleInputEmail1">Column Name</label> <input type="text" class="form-control input-sm"
                id="columnName" placeholder="Column Name">
            </div>
            <button class="btn btn-sm btn-alt m-r-5" onClick="validateStats();return false;">Validate</button>
          </form>
        </div>
      </div>
      <!-- End of Dashboard Body -->
    </section>
  </section>
  <jsp:include page="./fragments/footStatics.jsp" />
  <script type="text/javascript">
      function openPage(pageURL) {
        window.location.href = pageURL;
      }
    </script>
</body>
</html>
