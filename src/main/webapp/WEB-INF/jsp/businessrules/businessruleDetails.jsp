<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

      <!-- Dashboard Body -->
      <div class="block-area" id="job-search">
        <h3 class="block-title">Business Rule Information</h3>
        <table class="table tile">
          <tr>
            <th width="25%"><b>NAME</b></th>
            <td><c:out value="${businessrule.name}" /></td>
          </tr>
          <tr>
            <th>DESCRIPTION</th>
            <td><c:out value="${businessrule.description}" /></td>
          </tr>
          <tr>
            <th>BODY</th>
            <td><c:out value="${businessrule.body}" /></td>
          </tr>          
          <tr>
            <th>SOURCE TABLE NAME</th>
            <td><c:out value="${businessrule.sourceTableName}" /></td>
          </tr>
                    <tr>
            <th>SOURCE COLUMN NAME</th>
            <td><c:out value="${businessrule.sourceColumnName}" /></td>
          </tr>
                    <tr>
            <th>TARGET TABLE NAME</th>
            <td><c:out value="${businessrule.targetTableName}" /></td>
          </tr>
                    <tr>
            <th>TARGET COLUMN NAME</th>
            <td><c:out value="${businessrule.targetColumnName}" /></td>
          </tr>
                    <tr>
            <th>CONNECTION TO REST API</th>
            <td><c:out value="${businessrule.restconnection}" /></td>
          </tr>
          <tr>
            <td>
              <spring:url value="{businessruleId}/edit.html" var="editUrl">
                <spring:param name="businessruleId" value="${businessrule.id}" />
              </spring:url> <a href="${fn:escapeXml(editUrl)}" class="btn btn-alt btn-xs m-r-5">Edit</a> 
             
              <spring:url value="{businessruleId}/delete.html" var="deleteUrl">
                <spring:param name="businessruleId" value="${businessrule.id}" />
              </spring:url> 
              <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-alt btn-xs m-r-5">Delete</a></td>
          </tr>
        </table>
      </div>
      
      	<!-- Associated Tasks -->
	
      <div class="block-area" id="ass-tasks">
        <h3 class="block-title">Associated Tasks</h3>
        <div id="atasksid" class="collapse">
          <datatables:table id="atasks" data="${taskList}" row="taskList" cssClass="table tile" cssStyle="true"
            pageable="false" info="false" displayLength="20">
 
            <datatables:column title="Task name" display="html">
       		<input type="hidden" value="${taskList.name}" />
            <spring:url value="/tasks/{taskId}.html" var="taskUrl">
            <spring:param name="taskId" value="${taskList.id}" />
            </spring:url>
            <a href="${fn:escapeXml(taskUrl)}">        
            <c:out value="${taskList.name}" /></a>
            </datatables:column>
            
            <datatables:column title="Description" property="description" />
            <datatables:column title="Type" property="type" />                
            <datatables:column title="Body" property="body" />
             	
            <datatables:column title="Source id" display="html">
       		<input type="hidden" value="${taskList.sourceDbConnId}" />
            <spring:url value="/dbconnections/{dbconnectionId}" var="connSUrl">
            <spring:param name="dbconnectionId" value="${taskList.sourceDbConnId}" />
            </spring:url>
            <a href="${fn:escapeXml(connSUrl)}">        
            <c:out value="${taskList.sourceDbConnId}" /></a>
            </datatables:column>
            
            <datatables:column title="Target id" display="html">
       		<input type="hidden" value="${taskList.targetDbConnId}" />
            <spring:url value="/dbconnections/{dbconnectionId}" var="connTUrl">
            <spring:param name="dbconnectionId" value="${taskList.targetDbConnId}" />
            </spring:url>
            <a href="${fn:escapeXml(connTUrl)}">        
            <c:out value="${taskList.targetDbConnId}" /></a>
            </datatables:column>
                         	         
          </datatables:table>
        </div>
      </div>
      
      
      <!-- End of Dashboard Body -->
    </section>
  </section>
  <jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>