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
		<a href="javascript:;" id="menu-toggle"></a> <a class="logo pull-left"
			href="/admin/find.html">Admin Panel</a>
		<div class="media-body">
			<div class="media" id="top-menu">
				<div class="pull-left tm-icon">
					<a data-drawer="messages" class="drawer-toggle" href="javascript:;">
						<i class="sa-top-message"></i> <i class="n-count animated">5</i> <span>Alerts</span>
					</a>
				</div>
				<div class="pull-left tm-icon">
					<a data-drawer="notifications" class="drawer-toggle"
						href="javascript:;"> <i class="sa-top-updates"></i> <i
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
				<h3 class="block-title">Task Information</h3>
				<table class="table tile">
					<tr>
						<th width="25%"><b>NAME</b></th>
						<td><c:out value="${task.name}" /></td>
					</tr>
					<tr>
						<th>DESCRIPTION</th>
						<td><c:out value="${task.description}" /></td>
					</tr>
					<tr>
						<th>BODY</th>
						<td><c:out value="${task.body}" /></td>
					</tr>
					<tr>
						<th>TASK Type</th>
						<td><c:out value="${task.type}" /></td>
					</tr>
					<tr>
						<th>Source Database CONNECTION id</th>
						<td><c:out value="${task.sourceDbConnId}" /></td>
					</tr>					
					<tr>
						<th>Target Database CONNECTION id</th>
						<td><c:out value="${task.targetDbConnId}" /></td>
					</tr>					
					<tr>
						<th>TASK execution STATUS</th>
						<td><c:out value="${task.status}" /></td>
					</tr>					
					
					<tr>
						<td>

							<spring:url value="{taskId}/edit.html" var="editUrl">
								<spring:param name="taskId" value="${task.id}" />
							</spring:url> <a href="${fn:escapeXml(editUrl)}" class="btn btn-alt btn-xs m-r-5">Edit </a> 
							
							<spring:url	value="{taskId}/delete.html" var="deleteUrl">
								<spring:param name="taskId" value="${task.id}" />
							</spring:url> <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-alt btn-xs m-r-5">Delete </a>
							
			              	<spring:url value="{taskId}/run.html" var="runUrl">
                				<spring:param name="taskId" value="${task.id}" />
              			  	</spring:url> <a href="${fn:escapeXml(runUrl)}" class="btn btn-alt btn-xs m-r-5">Run</a> 

			              	<spring:url value="/businessrules.html" var="addbusinessruleUrl">
                				<spring:param name="taskId" value="${task.id}" />
              			  	</spring:url> <a href="${fn:escapeXml(addbusinessruleUrl)}" class="btn btn-alt btn-xs m-r-5">Add Businessrule</a> 

              			</td>
						<td>
					</tr>
				</table>
			</div>

	<!-- Contains Businessrules -->
	
      <div class="block-area" id="ass-br">
        <h3 class="block-title">Contains Businessrules</h3>
        <div id="abrid" class="collapse">
          <datatables:table id="abr" data="${businessruleList}" row="businessruleList" cssClass="table tile" cssStyle="true"
            pageable="false" info="false" displayLength="20">
            <datatables:column title="Businessrule name" display="html">
 
       		<input type="hidden" value="${businessruleList.name}" />
            <spring:url value="/businessrules/{businessruleId}.html" var="businessruleUrl">
            <spring:param name="businessruleId" value="${businessruleList.id}" />
            </spring:url>
            <a href="${fn:escapeXml(businessruleUrl)}">           
            <c:out value="${businessruleList.name}" /></a>
            
            </datatables:column>
              <datatables:column title="Description" property="description" />
              <datatables:column title="Body" property="body" />              
          </datatables:table>
        </div>
      </div>

			
	<!-- Associated Jobs -->
	
      <div class="block-area" id="ass-jobs">
        <h3 class="block-title">Associated Jobs</h3>
        <div id="ajobsid" class="collapse">
          <datatables:table id="ajobs" data="${jobList}" row="jobList" cssClass="table tile" cssStyle="true"
            pageable="false" info="false" displayLength="20">
            <datatables:column title="Job name" display="html">
 
       		<input type="hidden" value="${jobList.name}" />
            <spring:url value="/jobs/{jobId}.html" var="jobUrl">
            <spring:param name="jobId" value="${jobList.id}" />
            </spring:url>
            <a href="${fn:escapeXml(jobUrl)}">           
            <c:out value="${jobList.name}" /></a>
            
            </datatables:column>
<%--            <datatables:column title="Priority" property="priorityInRelatedJob" />      --%>         
              <datatables:column title="Description" property="description" />
              <datatables:column title="Status" property="status" />
          </datatables:table>
        </div>
      </div>

			<!-- End of Dashboard Body -->
		</section>
	</section>
	<jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>