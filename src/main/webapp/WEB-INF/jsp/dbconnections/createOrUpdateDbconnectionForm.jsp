<!DOCTYPE html>

<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags"%>

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
      <div class="block-area" id="conn-new">
        <h3 class="block-title">
          <c:if test="${dbConnection['new']}">New </c:if>
          Connection
        </h3>
        <form:form modelAttribute="dbConnection" id="add-dbConnection-form">
          <template:horizInputField label="Connection Name*" name="connectionName" />
          <template:horizInputField label="JDBC Driver*" name="databaseJdbcDriver" />
          <template:horizInputField label="URL*" name="databaseJdbcUrl" />
          <template:horizInputField label="User*" name="databaseUser" />
          <template:horizInputField label="Password*" name="databasePasswd" />
          <template:horizInputField label="Table Pattern" name="databaseTablePattern" />
          <template:horizInputField label="Catalog" name="databaseCatalog" />
          <template:horizInputField label="Schema Pattern" name="databaseSchemaPattern" />
          <template:horizInputField label="Database Type" name="databaseType" />
          <template:horizInputField label="Database Protocol" name="databaseProtocol" />
          <template:horizInputField label="Table Type" name="databaseTableType" />
          <template:horizInputField label="Column Pattern" name="databaseColumnPattern" />
          <template:horizInputField label="Vendor Name" name="databaseJpaVendorName" />
          <div class="form-actions">
            <c:choose>
              <c:when test="${dbConnection['new']}">
                <button class="btn btn-file btn-sm btn-alt" type="submit">Add Connection</button>
              </c:when>
              <c:otherwise>
                <button class="btn btn-file btn-sm btn-alt" type="submit">Update Connection</button>
              </c:otherwise>
            </c:choose>
          </div>
        </form:form>
      </div>
      <!-- End of Dashboard Body -->
    </section>
  </section>
  <jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>