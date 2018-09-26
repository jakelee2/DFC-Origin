<!DOCTYPE html>
<%@ page session="false"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--[if IE 9 ]>
<html class="ie9">
<![endif]-->
<html>
<head>
    <jsp:include page="../fragments/headStatics.jsp" />
    <link href="<spring:url value="/resources/css/dqstats.css" htmlEscape="true" />" rel="stylesheet">
</head>
<body id="skin-blur-ocean">
<header id="header" class="media">
    <a href="javascript:;" id="menu-toggle"></a> <a class="logo pull-left" href="/admin/find.html">Data Quality
    Statistics</a>
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
            <h3 class="block-title">Scripts</h3>


            <form:form method="POST" modelAttribute="fileBucket" enctype="multipart/form-data" class="form-horizontal">

                <div class="row">
                    <div class="form-group col-md-12">
                        <label class="col-md-3 control-lable" for="file">Upload a file</label>
                        <div class="col-md-7">
                            <form:input type="file" path="file" id="file" class="form-control input-sm"/>
                            <div class="has-error">
                                <form:errors path="file" class="help-inline"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="form-actions floatRight">
                        <input type="submit" value="Upload" class="btn btn-primary btn-sm">
                    </div>
                </div>
            </form:form>
            <a href="<c:url value='/scripts' />">Scripts List</a>

        </div>
        <!-- End of Dashboard Body -->
    </section>
</section>
<jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>
