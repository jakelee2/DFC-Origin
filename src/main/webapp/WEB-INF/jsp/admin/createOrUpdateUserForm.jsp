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
				<h3 class="block-title">
					<c:if test="${user['new']}">New </c:if>
					User
				</h3>
				<form:form modelAttribute="user" class="form-horizontal"
					id="add-user-form">
					<template:horizInputField label="User Name" name="username" />
					<template:horizInputField label="Password" name="rawPassword" />
					<template:horizSelectField label="Enabled" name="enabled"
						names="${enableTypes}" size="1" />


					<!-- TODO: Add to tags -->
					<div class="control-group">
						<div class="col-md-2">
							<label class="control-label">User Roles</label>
						</div>

						<div class="col-md-10">
							<div class="controls">
								<form:select path="roleIds" cssClass="form-control input-sm"
									multiple="true" items="${allRoleNames}" />
							</div>
						</div>
					</div>

					<div class="form-actions">
						<c:choose>
							<c:when test="${user['new']}">
								<button class="btn btn-file btn-sm btn-alt" type="submit">Add
									User</button>
							</c:when>
							<c:otherwise>
								<button class="btn btn-file btn-sm btn-alt" type="submit">Update
									User</button>
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