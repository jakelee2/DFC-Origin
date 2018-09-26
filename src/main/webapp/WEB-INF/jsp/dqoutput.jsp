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
<link href="<spring:url value="/resources/css/dqoutput.css" htmlEscape="true" />" rel="stylesheet">
</head>
<body id="skin-blur-ocean">
  <header id="header" class="media">
    <a href="javascript:;" id="menu-toggle"></a> <a class="logo pull-left" href="/admin/find.html">Task Execution Result</a>
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
        <h3 class="block-title">Task Execution Result</h3>
        <div class="form-group">
          <label for="tableName">Search Connection</label>
          <div class="btn-group">
            <button type="button" class="btn btn-sm btn-alt" id="selectTbl"><a>-- -- Select Connection -- --</a></button>
            <button type="button" class="btn btn-sm btn-alt dropdown-toggle" data-toggle="dropdown">
              <span class="caret"></span>
            </button>
            <ul class="dropdown-menu animated fadeIn">
              <li><a href="#" id="defaultDisplay"><span class="text">-- -- Select Connection -- --</span></a></li>
              <c:forEach var="conn" items="${Connections}">
                <li><a href="#" id="${conn.id}"><span class="text">${conn.connectionName}</span></a></li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </div>

      <div class="block-area" id="job-search">
        <div id="connNameDisplay"></div>
        <table class="table tile" id="tableListOfConn"></table>
        <br />

        <div id="messageDisplay"></div>
        <table class="table tile" id="errorMessageDisplay"></table>
        <br />
        
        <div id="execOutputList"></div>
        <table class="table tile" id="execOutputDisplay"></table>
        <table class="table tile" id="execOutputBarChart"></table>
        <br />

      </div>
      <!-- End of Dashboard Body -->
    </section>
  </section>
<jsp:include page="./fragments/footStatics.jsp" />
<script src="<spring:url value="/resources/js/dqoutput.js" htmlEscape="true" />"></script>
</body>
</html>