<!DOCTYPE html>
<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!--[if IE 9 ]>
<html class="ie9">
<![endif]-->
<html>
<head>
<jsp:include page="../fragments/headStatics.jsp" />
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
    <jsp:include page="../fragments/sidebar.jsp" />

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
                      <small class="t-overflow">Manage Jobs</small>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Connections Bar -->
          <!-- Connections and Tables -->
          <div class="col-md-2">

            <!-- Job List -->
            <div class="tile">
              <h2 class="tile-title">Jobs</h2>
              <div class="tile-config dropdown">
                <a data-toggle="dropdown" href="" class="tooltips tile-menu" title="" data-original-title="Options"></a>
                <ul class="dropdown-menu pull-right text-right">
                  <li><a href="">Edit</a></li>
                  <li><a href="">Delete</a></li>
                </ul>
              </div>

              <div class="listview icon-list">
                <div class="media">
                  <div class="media-body">Job #1</div>
                </div>

                <div class="media">
                  <div class="media-body">Job #2</div>
                </div>

                <div class="media">
                  <div class="media-body">Job #3</div>
                </div>

                <div class="media">
                  <div class="media-body">Job #4</div>
                </div>
              </div>
            </div>
            <!-- End of Job List -->

          </div>
          <div class="col-md-2">

            <!-- Task List -->
            <div class="tile">
              <h2 class="tile-title">Tasks</h2>
              <div class="tile-config dropdown">
                <a data-toggle="dropdown" href="" class="tooltips tile-menu" title="" data-original-title="Options"></a>
                <ul class="dropdown-menu pull-right text-right">
                  <li><a href="">Edit</a></li>
                  <li><a href="">Delete</a></li>
                </ul>
              </div>

              <div class="listview icon-list">
                <div class="media">
                  <div class="media-body">Ingestion Task</div>
                </div>

                <div class="media">
                  <div class="media-body">Python Tasks</div>
                </div>
              </div>
            </div>
            <!-- End of Task List -->

          </div>
          <!-- End of Jobs and Tasks -->

          <!-- Graphs and Stuff -->
          <div class="col-md-8">
            <div class="tab-container tile">
              <ul class="nav tab nav-tabs">
                <li class="active"><a href="#metadata">Run Task</a></li>
                <li><a href="#dataquality">Execution Log</a></li>
              </ul>

              <div class="tab-content">
                <div class="tab-pane active" id="metadata">
                  Screen for running tasks and seeing their output.
                </div>

                <div class="tab-pane" id="dataquality">
                    Previous ran tasks.
                </div>

              </div>
            </div>
          </div>
          <!-- End of Graphs and Stuff -->
        </div>
      </div>
      <!-- End of Dashboard Body -->
    </section>
  </section>
  <jsp:include page="../fragments/footStatics.jsp" />
  <script type="text/javascript">
      function openPage(pageURL) {
        window.location.href = pageURL;
      }
    </script>
</body>
</html>
