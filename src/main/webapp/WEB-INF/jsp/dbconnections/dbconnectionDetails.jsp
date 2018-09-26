<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

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

      <!-- Dashboard Body -->
      <div class="block-area" id="connection-info">
        <h3 class="block-title">Connection Information</h3>
        <div id="conninfo" class="collapse">
          <table class="table tile">
            <tr>
              <th width="25%"><b>Connection Name</b></th>
              <td><c:out value="${dbConnection.connectionName}" /></td>
            </tr>
            <tr>
              <th>JDBC Driver</th>
              <td><c:out value="${dbConnection.databaseJdbcDriver}" /></td>
            </tr>
            <tr>
              <th>URL</th>
              <td><c:out value="${dbConnection.databaseJdbcUrl}" /></td>
            </tr>
            <tr>
              <th>User</th>
              <td><c:out value="${dbConnection.databaseUser}" /></td>
            </tr>
            <tr>
              <th>Password</th>
              <td><c:out value="${dbConnection.databasePasswd}" /></td>
            </tr>
            <tr>
              <th>Table Pattern</th>
              <td><c:out value="${dbConnection.databaseTablePattern}" /></td>
            </tr>
            <tr>
              <th>Catalog</th>
              <td><c:out value="${dbConnection.databaseCatalog}" /></td>
            </tr>
            <tr>
              <th>Schema Pattern</th>
              <td><c:out value="${dbConnection.databaseSchemaPattern}" /></td>
            </tr>
            <tr>
              <th>Database Type</th>
              <td><c:out value="${dbConnection.databaseType}" /></td>
            </tr>
            <tr>
              <th>Table Type</th>
              <td><c:out value="${dbConnection.databaseTableType}" /></td>
            </tr>
            <tr>
              <th>Database Protocol</th>
              <td><c:out value="${dbConnection.databaseProtocol}" /></td>
            </tr>
            <tr>
              <th>Column Pattern</th>
              <td><c:out value="${dbConnection.databaseColumnPattern}" /></td>
            </tr>
            <tr>
              <th>Vendor Name</th>
              <td><c:out value="${dbConnection.databaseJpaVendorName}" /></td>
            </tr>
            <tr>
              <td>
              <spring:url value="{dbconnectionId}/addfavorite.html" var="favoriteUrl">
                  <spring:param name="dbconnectionId" value="${dbConnection.id}" />
                </spring:url> 
                <a href="${fn:escapeXml(favoriteUrl)}" class="btn btn-alt btn-xs m-r-5">Add to Favorite</a> 
                
                <spring:url value="{dbconnectionId}/edit.html" var="editUrl">
                  <spring:param name="dbconnectionId" value="${dbConnection.id}" />
                </spring:url> 
                <a href="${fn:escapeXml(editUrl)}" class="btn btn-alt btn-xs m-r-5">Edit</a> 
                
                <spring:url value="{dbconnectionId}/delete.html" var="deleteUrl">
                  <spring:param name="dbconnectionId" value="${dbConnection.id}" />
                </spring:url> 
                <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-alt btn-xs m-r-5">Delete</a>
              </td>
            </tr>
          </table>
          <p></p>

          <spring:url value="{dbconnectionId}/connect.html" var="connectUrl">
            <spring:param name="dbconnectionId" value="${dbConnection.id}" />
          </spring:url>
          <a href="${fn:escapeXml(connectUrl)}" class="btn btn-alt btn-sm m-r-5"><b>Connect</b></a>
          
          <spring:url value="{dbconnectionId}/disconnect.html" var="disconnectUrl">
            <spring:param name="dbconnectionId" value="${dbConnection.id}" />
          </spring:url>
          <a href="${fn:escapeXml(disconnectUrl)}" class="btn btn-alt btn-sm m-r-5"><b>Disconnect</b></a>
        </div>
        <div>
          <h5>
            <c:out value="${msg}" />
          </h5>
        </div>
      </div>
      <!-- End of Dashboard Body -->

    </section>
  </section>
  <jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>