<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="datatables"
	uri="http://github.com/dandelion/datatables"%>
<%@ page import="org.dbadmin.util.*"%>

<!--[if IE 9 ]>
<html class="ie9">
<![endif]-->
<html>
<head>
<jsp:include page="../fragments/headStatics.jsp" />
<link href="<spring:url value="/resources/css/joblist.css" htmlEscape="true" />" rel="stylesheet">
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
			
			
				<c:if test="${fn:length(selections) gt 0}">			
					<h3 class="block-title">Jobs</h3>
					<datatables:table id="jobs" data="${selections}" row="job"
						cssClass="table tile" cssStyle="true" pageable="true" info="false"
						export="csv">
						<datatables:column title="Name" cssStyle="width: 150px;"
							display="html">
							<spring:url value="/jobs/{jobId}.html" var="jobUrl">
								<spring:param name="jobId" value="${job.jobId}" />
							</spring:url>
							<a href="${fn:escapeXml(jobUrl)}"><c:out value="${job.jobName}" /></a>
						</datatables:column>

						<datatables:column title="Level" cssStyle="width: 150px;" display="html">	
							<div class="level_drop_down">
								<c:if test="${isAdmin == true}">
									<a data-toggle="modal" class="btn btn-sm" ><c:out value="CHANGE LEVEL : " /></a>
								</c:if>
													
								<div class="btn-group"  id="level">
									<c:choose>
										<c:when test="${job.levelName != null}">
							        <button type="button" class="btn btn-sm btn-alt selectLevel_${job.jobId}" id="selectLevel_${job.jobId}">${job.levelName}</button>
						            	</c:when>
		    							<c:otherwise>
						            <button type="button" class="btn btn-sm btn-alt selectLevel_${job.jobId}" id="selectLevel_${job.jobId}">-- -- Select Level -- --</button>
		    							</c:otherwise>
		    						</c:choose>
						            <button type="button" class="btn btn-sm btn-alt dropdown-toggle" data-toggle="dropdown">
						              <span class="caret"></span>
						            </button>

									<c:if test="${isAdmin == true}">
						            <ul class="dropdown-menu animated fadeIn">
						              	<li id="${job.jobId}"><a href="#" id="defaultLevelDisplay" class="${job.transferer}"><span class="text">-- -- Select Level -- --</span></a></li>
						              	<c:forEach var="role" items="${jobRoles}">
						              		<c:if test="${role.role_name == 'ADMIN' || role.role_name == 'USER'}"> 
							    	        	<li id="${job.jobId}"><a href="#" id="${role.id}" class="${job.transferer}"><span class="text">${role.role_name}</span></a></li>
											</c:if>
						            	</c:forEach>
						        	</ul>
									</c:if>
						        </div>
					        </div>

						</datatables:column>
							
						<c:if test="${isAdmin == true}">
							<datatables:column title="Owner" cssStyle="width: 150px;" display="html">
								<a><c:out value="${job.transferer}" /></a>
							</datatables:column>
						</c:if>
	
						<datatables:column title="Transfer" cssStyle="width: 150px;" display="html">
							<a data-toggle="modal" class="btn btn-sm" ><c:out value="TRANSFER TO : " /></a>
	
							<div class="btn-group" id="transfer">
								<c:choose>
									<c:when test="${job.transferee != null}">
						        <button type="button" class="btn btn-sm btn-alt" id="selectUser_${job.userJobId}">${job.transferee}</button>
					            	</c:when>
	    							<c:otherwise>
					            <button type="button" class="btn btn-sm btn-alt" id="selectUser_${job.userJobId}">-- -- Select User -- --</button>
	    							</c:otherwise>
	    						</c:choose>
					            <button type="button" class="btn btn-sm btn-alt dropdown-toggle" data-toggle="dropdown">
					              <span class="caret"></span>
					            </button>
					            
					            <ul class="dropdown-menu animated fadeIn" id="${job.jobId}">
					              	<li id="${job.userJobId}" class="${job.jobId}"><a href="#" id="defaultUserDisplay" class="${job.transferer}"><span class="text">-- -- Select User -- --</span></a></li>
					              	<c:forEach var="user" items="${usersForDropdown[job_rowIndex-1]}">
						            	<c:if test="${job.transferer != user.username}"><!-- OWNER should not be in the TRANSFER Drow Down List -->
					    	        		<li id="${job.userJobId}" class="${job.jobId}"><a href="#" id="${user.id}" class="${job.transferer}"><span class="text">${user.username}</span></a></li>
										</c:if>
					            	</c:forEach>
					        	</ul>
					        </div>
						</datatables:column>
						
						<datatables:column title="Relinquish Job" cssStyle="width: 150px;" display="html">
							<spring:url value="/jobs/discardJob/{jobId}/{userJobId}.html" var="jobUrl">
								<spring:param name="jobId" value="${job.jobId}" />
								<spring:param name="userJobId" value="${job.userJobId}" />
							</spring:url>
							<!-- DISCARD button should show up only when its owner exists -->
							<c:if test="${job.transferer != null && job.transferer != '' }">	
								<a href="${fn:escapeXml(jobUrl)}" data-toggle="modal" class="btn btn-sm" ><c:out value="DISCARD" /></a>
							</c:if>
	
						</datatables:column>
						
						<datatables:export type="csv" cssClass="btn btn-alt btn-xs m-r-5" />
					</datatables:table>
				</c:if>
				
				
				<c:if test="${fn:length(jobsRequested) gt 0}">
					<h3 class="block-title">Jobs transfer requests</h3>
					<datatables:table id="jobsForMe" data="${jobsRequested}" row="job_request"
						cssClass="table tile" cssStyle="true" pageable="true" info="false"
						export="csv">
						<datatables:column title="Job Name" cssStyle="width: 150px;" display="html">
							<spring:url value="/jobs/{jobId}.html" var="jobUrl">
								<spring:param name="jobId" value="${job_request.jobId}" />
							</spring:url>
							<a href="${fn:escapeXml(jobUrl)}"><c:out value="${job_request.jobName}" /></a>
						</datatables:column>

						<datatables:column title="Requested By" cssStyle="width: 150px;" display="html">
							<a><c:out value="${job_request.transferer}" /></a>
						</datatables:column>

						<datatables:column title="Accept" cssStyle="width: 150px;" display="html">
							<spring:url value="/jobs/accept/{jobId}/{userJobId}.html" var="jobUrl">
								<spring:param name="jobId" value="${job_request.jobId}" />
								<spring:param name="userJobId" value="${job_request.userJobId}" />
							</spring:url>
							<a href="${fn:escapeXml(jobUrl)}"><c:out value="Accept" /></a>
						</datatables:column>
						
						<datatables:column title="Decline" cssStyle="width: 150px;" display="html">
							<spring:url value="/jobs/decline/{jobId}/{userJobId}.html" var="jobUrl">
								<spring:param name="jobId" value="${job_request.jobId}" />
								<spring:param name="userJobId" value="${job_request.userJobId}" />
							</spring:url>
							<a href="${fn:escapeXml(jobUrl)}"><c:out value="Decline" /></a>
						</datatables:column>

						<datatables:export type="csv" cssClass="btn btn-alt btn-xs m-r-5" />
					</datatables:table>
				</c:if>
				
				
				<c:if test="${fn:length(jobTransferHIstory) gt 0}">
					<h3 class="block-title">Transfer Log</h3>
					<datatables:table id="jobTransferHistory" data="${jobTransferHIstory}" row="job_history"
						cssClass="table tile" cssStyle="true" pageable="true" info="false" export="csv">
	
						<datatables:column title="Transfer ID" cssStyle="width: 150px;" display="html">
							<a><c:out value="${job_history.jobId}" /></a>
						</datatables:column>
						
						<datatables:column title="Transferer" cssStyle="width: 150px;" display="html">
							<a><c:out value="${job_history.transferer}" /></a>
						</datatables:column>
	
						<datatables:column title="Job Name" cssStyle="width: 150px;" display="html">
							<a><c:out value="${job_history.jobName}" /></a>
						</datatables:column>
	
						<datatables:column title="Transferee" cssStyle="width: 150px;" display="html">
							<a><c:out value="${job_history.transferee}" /></a>
						</datatables:column>

						<datatables:column title="Description" cssStyle="width: 150px;" display="html">
							<a><c:out value="${job_history.description}" /></a>
						</datatables:column>
	
						<datatables:column title="Date" cssStyle="width: 150px;" display="html" sortInitDirection="desc">
							<a><c:out value="${job_history.lastUpdated}" /></a>
						</datatables:column>
	
						<datatables:export type="csv" cssClass="btn btn-alt btn-xs m-r-5" />
					</datatables:table>
				</c:if>
				
			</div>
			<!-- End of Dashboard Body -->
		</section>
	</section>
	<jsp:include page="../fragments/footStatics.jsp" />
	<script src="<spring:url value="/resources/js/joblist.js" htmlEscape="true" />"></script>
</body>
</html>