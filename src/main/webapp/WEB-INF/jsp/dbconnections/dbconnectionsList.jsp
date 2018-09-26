<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="../fragments/headStatics.jsp" />

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
    <jsp:include page="../fragments/sidebar.jsp" />

    <!-- Content -->
    <section id="content" class="container">
      <jsp:include page="../fragments/header.jsp" />

      <!-- Breadcrumb -->
      <ol class="breadcrumb hidden-xs">
        <li><a href="#">Home</a></li>
        <li class="active">Dashboard</li>
      </ol>
      <h4 class="page-title">DASHBOARD</h4>
      
      <div class="block-area" id="dbtable-connection-active">
        <h3 class="block-title">Active Connections</h3>
        <div id="connfav" class="collapse">
          <datatables:table id="dbactive" data="${actives}" row="dbconnections" cssClass="table tile" cssStyle="true"
            pageable="false" info="false" displayLength="20">
            <datatables:column title="Active Connection name" display="html">
               <c:out value="${dbconnections.connectionName}" /></a>
            </datatables:column>
          </datatables:table>
        </div>
      </div>
      
      
      <div class="block-area" id="dbtable-connection-favorite">
        <h3 class="block-title">Favorite Connections</h3>
        <div id="connfav" class="collapse">
          <datatables:table id="dbfav" data="${favorits}" row="dbconnections" cssClass="table tile" cssStyle="true"
            pageable="false" info="false" displayLength="20">
            <datatables:column title="Favorite Connection name" display="html">
              <datatables:columnHead>
                <input type="checkbox" onclick="$('#dbfav').find(':checkbox').attr('checked', this.checked);" />
              </datatables:columnHead>
              <input type="checkbox" value="${dbconnections.connectionName}" />
              <spring:url value="/dbconnections/{dbconnectionId}.html" var="dbconnUrl">
                <spring:param name="dbconnectionId" value="${dbconnections.id}" />
              </spring:url>
              <a href="${fn:escapeXml(dbconnUrl)}">
              <c:out value="${dbconnections.connectionName}" /></a>

            </datatables:column>
            <datatables:column title="URL" property="databaseJdbcUrl" />
            <datatables:column title="User" property="databaseUser" />
            <datatables:column title="Type" property="databaseJpaVendorName" />
            <datatables:column title="Id" property="id" />
          </datatables:table>
        </div>
      </div>

      <div class="block-area" id="dbtable-connection-source">
        <h3 class="block-title">All Connections</h3>
        <div id="connlist" class="collapse">
          <datatables:table id="dbs" data="${selections}" row="dbconnections" cssClass="table tile" cssStyle="true"
            pageable="false" info="false" displayLength="20">
            <datatables:column title="Connection name" display="html">
              <datatables:columnHead>
                <input type="checkbox" onclick="$('#dbs').find(':checkbox').attr('checked', this.checked);" />
              </datatables:columnHead>
              <input type="checkbox" value="${dbconnections.connectionName}" />
              <spring:url value="/dbconnections/{dbconnectionId}.html" var="dbconnUrl">
                <spring:param name="dbconnectionId" value="${dbconnections.id}" />
              </spring:url>
              <a href="${fn:escapeXml(dbconnUrl)}"><c:out value="${dbconnections.connectionName}" /></a>
            </datatables:column>
            <datatables:column title="URL" property="databaseJdbcUrl" />
            <datatables:column title="User" property="databaseUser" />
            <datatables:column title="Type" property="databaseJpaVendorName" />
            <datatables:column title="Id" property="id" />
          </datatables:table>
        </div>
      </div>
      <div class="block-area" id="dbtable-add-connection">
        <a href="<spring:url value="/dbconnections/new" htmlEscape="true"/>" class="btn btn-sm btn-alt m-r-5" role="button">New
          Connection</a>
      </div>

      <div class="block-area" id="sql_error">
        <h5>
          <c:out value="${msg}" />
        </h5>
      </div>
    </section>

  </section>

  <jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>