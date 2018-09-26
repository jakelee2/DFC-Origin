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
<jsp:include page="./fragments/headStatics.jsp" />
</head>
<body id="skin-blur-ocean">
  <header id="header" class="media">
    <a href="javascript:;" id="menu-toggle"></a> <a class="logo pull-left" href="javascript:;">Admin Panel</a>
    <div class="media-body">
      <div class="media" id="top-menu">
        <div id="time" class="pull-right">
          <span id="hours"></span> : <span id="min"></span> : <span id="sec"></span>
        </div>
      </div>
    </div>
  </header>
  <div class="clearfix"></div>
  <section id="main" class="p-relative" role="main">
    <jsp:include page="./fragments/sidebar.jsp" />
    <!-- Content -->
    <section id="content" class="container">
      <!-- Breadcrumb -->
      <ol class="breadcrumb hidden-xs">
        <li><a href="#">Home</a></li>
        <li class="active">Dashboard</li>
      </ol>
      <h4 class="page-title">DASHBOARD</h4>

      <!-- Dashboard Body -->
      <div class="block-area">
        <!-- Row Container -->
        <div class="row m-container">

          <!-- Management Drop Panel -->
          <div class="accordion tile">
            <div class="panel-group block" id="accordion">
              <div class="panel panel-default">
                <div class="panel-heading">
                  <h3 class="panel-title">
                    <a class="accordion-toggle collapsed active" data-toggle="collapse" data-parent="#accordion"
                      href="#collapseOne">Management</a>
                  </h3>
                </div>
                <div id="collapseOne" class="panel-collapse collapsed active" style="height: 0px;">
                  <div class="panel-body">
                    <a class="shortcut tile" href="javascript:;"> <img
                      src="<spring:url value="/resources/images/shortcuts/reports.png" htmlEscape="true" />" alt="">
                      <small class="t-overflow">Reporting</small>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Categories -->
          <div class="col-md-2">
          
            <div class="tile">
              <h2 class="tile-title">Favorite Reports</h2>
              <div class="tile-config dropdown">
                <a data-toggle="dropdown" href="" class="tooltips tile-menu" title="" data-original-title="Options"></a>
                <ul class="dropdown-menu pull-right text-right">
                  <li id="todo-add"><a href="">Add New</a></li>
                  <li id="todo-refresh"><a href="">Refresh</a></li>
                  <li id="todo-clear"><a href="">Clear All</a></li>
                </ul>
              </div>

              <div class="listview todo-list sortable ui-sortable">

                <div class="media" style="position: relative; left: 0px; top: 0px;">
                  <label class="t-overflow" style="top: -4px;"> Collections </label>
                </div>

                <div class="media" style="position: relative; left: 0px; top: 0px;">
                  <label class="t-overflow" style="top: -2px;"> Find Solicit Acquire </label>
                </div>
              </div>

              <h2 class="tile-title">All Reports</h2>

              <div class="listview todo-list sortable ui-sortable">

                <div class="media" style="position: relative; left: 0px; top: 0px;">
                  <label class="t-overflow" style="top: -2px;"> Fav Database </label>
                </div>
              </div>
            </div>
          
          </div>
          <!-- End of Connections and Tables -->


          <!-- Tiled Body -->
          <div class="col-md-10">
            <div class="tab-container tile">
              <ul class="nav tab nav-tabs">
                <li class="active"><a href="#actual">Actual Delinquency</a></li>
                <li><a href="#checkcashing">Check Cashing Delinquency</a></li>
              </ul>

              <div class="tab-content">
                <div class="tab-pane active" id="actual">
                  <img src="<spring:url value="/resources/images/001ActualDelin.jpg" htmlEscape="true"/>" width="100%">
                </div>

                <div class="tab-pane" id="checkcashing">
                  <img src="<spring:url value="/resources/images/001CheckDelin.jpg" htmlEscape="true"/>" width="100%">
                </div>
              </div>

            </div>
          </div>
          <!-- End of tiled body -->

        </div>
        <!-- End of row container -->

      </div>
      <!-- End of Dashboard Body -->
    </section>
  </section>
  <jsp:include page="./fragments/footStatics.jsp" />
</body>
</html>