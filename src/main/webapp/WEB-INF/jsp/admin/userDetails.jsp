<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
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
				<h3 class="block-title">User Information</h3>
				<table class="table tile">
					<tr>
						<th width="25%"><b>USERNAME</b></th>
						<td><c:out value="${user.username}" /></td>
					</tr>
					<tr>
						<th>ENABLED</th>
						<td><c:out value="${user.enabled}" /></td>
					</tr>
					<tr>
						<th>ROLES</th>
						<td><c:forEach var="role" items="${user.getRoles()}"
								varStatus="stat">
								<c:out
									value="${stat.first ? '' :','} ${role.getDisplayRole_name()}" />
							</c:forEach></td>
					</tr>
					<tr>
						<td><spring:url value="{userId}/edit.html" var="editUrl">
								<spring:param name="userId" value="${user.id}" />
							</spring:url> <a href="${fn:escapeXml(editUrl)}"
							class="btn btn-alt btn-xs m-r-5">Edit</a> <spring:url
								value="{userId}/delete.html" var="deleteUrl">
								<spring:param name="userId" value="${user.id}" />
							</spring:url> <a href="${fn:escapeXml(deleteUrl)}"
							class="btn btn-alt btn-xs m-r-5">Delete</a></td>
					</tr>
				</table>
			</div>
			<!-- End of Dashboard Body -->
		</section>
	</section>
	<jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>