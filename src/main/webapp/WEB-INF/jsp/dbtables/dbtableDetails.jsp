<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>
<%@ page import="org.dbadmin.util.*"%>

<!--[if IE 9 ]>
<html class="ie9">
<![endif]-->
<html>
<head>
<jsp:include page="../fragments/headStatics.jsp" />
</head>
<body id="skin-blur-ocean">
  <header id="header" class="media">
    <a href="javascript:;" id="menu-toggle"></a> <a class="logo pull-left" href="/admin/find.html">Admin Panel</a>
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
    <jsp:include page="../fragments/sidebar.jsp" />

    <!-- Content -->
    <section id="content" class="container">

      <!-- Breadcrumb -->
      <ol class="breadcrumb hidden-xs">
        <li><a href="#">Home</a></li>
        <li class="active">Dashboard</li>
      </ol>
      <h4 class="page-title">DASHBOARD</h4>
      <jsp:include page="../fragments/header.jsp" />

      <div class="block-area" id="table-select">
        <h3 class="block-title">Table Information</h3>
        <div id="tabinfo" class="collapse">
          <table class="table tile">
            <tr>
              <th width="25%"><b>Connection</b></th>
              <td><c:out value="${dbTable.dbConnectionURL}" /></td>
            </tr>
            <tr>
              <th width="25%"><b>Table Name</b></th>
              <td><c:out value="${dbTable.name}" /></td>
            </tr>
            <tr>
              <th>Primary Key</th>
              <td><c:out value="${dbTable.primaryKey}" /></td>
            </tr>
            <tr>
              <th>Rows</th>
              <td><c:out value="${dbTable.rowsNumber}" /></td>
            </tr>
            <tr>
              <th>Columns</th>
              <td><c:out value="${dbTable.columnNumber}" /></td>
            </tr>
          </table>
        </div>
      </div>

      <div class="block-area" id="columns-info">
        <h3 class="block-title">Columns</h3>
        <div id="columninfo" class="collapse">
          <datatables:table id="columns" data="${dbTable.dbcolumns}" row="dbtable" cssClass="table tile" cssStyle="true"
            pageable="false" info="true" displayLength="20" export="csv">
            <datatables:column title="Name" property="name" />
            <datatables:column title="Size" property="size" />
            <datatables:column title="SQL Type" property="sqlType" />
            <datatables:column title="Type Name" property="sqlTypeString" />
            <datatables:column title="Decimal Digits" property="decimalDigit" />
            <datatables:column title="Nullable" property="isnullable" />
          </datatables:table>
        </div>
      </div>
      <!-- End of Dashboard Body -->
    </section>
  </section>
  <jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>
