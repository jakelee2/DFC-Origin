<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
    <a href="javascript:;" id="menu-toggle"></a> <a class="logo pull-left" href="admin/find.html">Admin Panel</a>
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
      <div class="block-area" id="dbtable-source">
        <h3 class="block-title">Database Tables</h3>
        <div id="tablist" class="collapse">
          <datatables:table id="dbtables" data="${selections}" row="dbtable" cssClass="table tile" cssStyle="true"
            pageable="false" info="false" displayLength="20" export="csv" cdn="false">
            <datatables:column title="Name" display="html">
              <datatables:columnHead>
                <input type="checkbox" onclick="$('#dbtables').find(':checkbox').attr('checked', this.checked);" />
              </datatables:columnHead>
              <input type="checkbox" value="${dbtable.name}" />
              <spring:url value="/dbtables/{dbtableId}.html" var="dbtableUrl">
                <spring:param name="dbtableId" value="${dbtable.id}" />
              </spring:url>
              <a href="${fn:escapeXml(dbtableUrl)}"><c:out value="${dbtable.name}" /></a>
            </datatables:column>
            <datatables:column title="Name" display="pdf">
              <c:out value="${dbtable.name}" />
            </datatables:column>
            <datatables:column title="Columns" property="columnNumber" />
            <datatables:column title="Rows" property="rowsNumber" />
            <datatables:export type="csv" cssClass="btn btn-alt btn-xs m-r-5" />
          </datatables:table>
        </div>
      </div>

      <div class="block-area" id="sql_error">
        <h5>
          <c:out value="${error}" />
        </h5>
      </div>
      <!-- End of Dashboard Body -->
    </section>
  </section>
  <jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>
