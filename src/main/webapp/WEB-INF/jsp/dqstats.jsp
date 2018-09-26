<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--[if IE 9 ]>
<html class="ie9">
<![endif]-->
<html>
<head>
<jsp:include page="./fragments/headStatics.jsp" />
<link href="<spring:url value="/resources/css/dqstats.css" htmlEscape="true" />" rel="stylesheet">
</head>
<body id="skin-blur-ocean">
  <header id="header" class="media">
    <a href="javascript:;" id="menu-toggle"></a> <a class="logo pull-left" href="/admin/find.html">Data Quality
      Statistics</a>
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

      <!-- Breadcrumb -->
      <ol class="breadcrumb hidden-xs">
        <li><a href="#">Home</a></li>
        <li class="active">Dashboard</li>
      </ol>
      <h4 class="page-title">DASHBOARD</h4>
      <jsp:include page="./fragments/header.jsp" />
      <div class="block-area" id="job-search">
        <h3 class="block-title">Data Quality Statistics</h3>
        <div class="form-group">
          <label for="tableName">Search Table</label>
          <div class="btn-group">
            <button type="button" class="btn btn-sm btn-alt" id="selectTbl">-- -- Select Table -- --</button>
            <button type="button" class="btn btn-sm btn-alt dropdown-toggle" data-toggle="dropdown">
              <span class="caret"></span>
            </button>
            <ul class="dropdown-menu animated fadeIn">
              <li><a href="#" id="defaultDisplay"><span class="text">-- -- Select Table -- --</span></a></li>
              <c:forEach var="item" items="${DbTables}">
                <li><a href="#" id="${item.name}"><span class="text">${item.name}</span></a></li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </div>

      <div class="block-area" id="job-search">
        <div id="tableNameDisplay"></div>
        <table class="table tile" id="tableInfoDisplay"></table>
        <br />
        <div id="numericalStatsTitle"></div>
        <table class="table tile" id="numericStatsDisplay"></table>
        <br />

		<!-- Modal -->	
		<div class="modal fade" id="modalNarrower" tabindex="-1" role="dialog" aria-hidden="true">
		    <div class="modal-dialog modal-sm">
		        <div class="modal-content">
		            <div class="modal-header">
		                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                <h4 class="modal-title">Set bounds</h4>
		            </div>
		            <div class="modal-body" id="modalMsg">
		                <p>Please set the MIN and MAX bounds here.</p>
		            </div>

		            <div class="input-group input-sm">
			            <label class="input-group-addon" for="tableName"><i class="fa fa-search"></i></label>
			            <label class="input-group-btn" for="tableName">TABLE</label>
			            <input type="text" class="form-control" id="tableName" name="tableName" readonly>	<!-- used for parameter in saveBounds() function -->
			        </div>
		            <div class="input-group input-sm">
			            <label class="input-group-addon" for="columnName"><i class="fa fa-th-list"></i></label>
			            <label class="input-group-btn" for="columnName">COLUMN</label>
			            <input type="text" class="form-control" id="columnName" name="columnName" readonly>	<!-- used for parameter in saveBounds() function -->
			        </div>
		            <div class="input-group input-sm">
			            <label class="input-group-addon" for="minVal"><i class="fa fa-signal"></i></label>
			            <label class="input-group-btn" for="minVal">MIN</label>
			            <input type="text" class="form-control" id="minVal" name="minVal" placeholder="Enter Min Value" required>
			        </div>
			        <div class="input-group input-sm">
			            <label class="input-group-addon" for="maxVal"><i class="fa fa-bar-chart-o"></i></label>
			            <label class="input-group-btn" for="minVal">MAX</label>
			            <input type="text" class="form-control" id="maxVal" name="maxVal" placeholder="Enter Max Value" required>
			        </div>
			        <input type="hidden" name="${_csrf.parameterName}" 	value="${_csrf.token}" />
			        <div class="modal-footer">
			            <button type="submit" class="btn btn-sm" onclick="DATA_PROFILE.saveBounds()">Save changes</button>
			            <button type="button" class="btn btn-sm" data-dismiss="modal">Close</button>
					</div>
		        </div>
		    </div>
		</div>        

        <div id="numericalGraphTitle"></div>
        <table class="table tile" id="numericGraphTable"></table>
        <br />
        <div id="categoricalStatsTitle"></div>
        <table class="table tile" id="categoricalStatsDisplay"></table>
        <br />
        <div id="categoricalBarChartTitle"></div>
        <table class="table tile" id="categoricalBarChartTable"></table>
        <br />
      </div>
      <!-- End of Dashboard Body -->
    </section>
  </section>
<jsp:include page="./fragments/footStatics.jsp" />
<script src="<spring:url value="/resources/js/dqstats.js" htmlEscape="true" />"></script>
</body>
</html>