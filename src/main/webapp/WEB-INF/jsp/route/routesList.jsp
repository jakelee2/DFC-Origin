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
				<h3 class="block-title">Routes</h3>
				<datatables:table id="routes" data="${selections}" row="route"
					cssClass="table tile" cssStyle="true" pageable="false" info="false"
					export="csv">
					<datatables:column title="Name" display="html">
						<spring:url value="/admin/route/{routeId}.html" var="routeUrl">
							<spring:param name="routeId" value="${route.id}" />
						</spring:url>
						<a href="${fn:escapeXml(routeUrl)}"><c:out
								value="${route.url}" /></a>
					</datatables:column>
					<datatables:column title="URL" display="pdf">
						<c:out value="${route.url}" />
					</datatables:column>
					<datatables:column title="Access" property="access" />
					<datatables:column title="Priority" property="priority" />
					<datatables:export type="csv" cssClass="btn btn-alt btn-xs m-r-5" />
				</datatables:table>
			</div>
			<!-- End of Dashboard Body -->
		</section>
	</section>
	<jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>
