<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="datatables" uri="http://github.com/dandelion/datatables"%>

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
            <datatables:table id="scripts" data="${logs}" row="script"
                              cssClass="table tile" cssStyle="true" pageable="false" info="false"
                              export="csv">
                <datatables:column title="Script Name" property="script_name" />
                <datatables:column title="Output" property="output" />
                <datatables:column title="Started at" property="started_at" />
                <datatables:column title="Completed at" property="completed_at" />
                <datatables:column title="Started by" property="started_by" />
                <datatables:export type="csv" cssClass="btn btn-alt btn-xs m-r-5" />
            </datatables:table>
            <br />
            <a href="<c:url value='/scripts' />">Scripts List</a>
        </div>



    </section>
</section>
<jsp:include page="../fragments/footStatics.jsp" />
</body>
</html>
