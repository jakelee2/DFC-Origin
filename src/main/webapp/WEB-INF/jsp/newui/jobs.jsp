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
                      <small class="t-overflow">Add Con.</small>
                    </a>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Connections Bar -->
          <!-- Connections and Tables -->
          <div class="col-md-2">

            <!-- Active Connections -->
            <div class="tile">
              <h2 class="tile-title">Active Connections</h2>
              <div class="tile-config dropdown">
                <a data-toggle="dropdown" href="" class="tooltips tile-menu" title="" data-original-title="Options"></a>
                <ul class="dropdown-menu pull-right text-right">
                  <li><a href="">Edit</a></li>
                  <li><a href="">Delete</a></li>
                </ul>
              </div>

              <div class="listview icon-list" id="activeConnections" onClick="displayTables()">
                <!--
                <div class="media p-5 text-center l-100">
                  <a href=""><small>More...</small></a>
                </div>
                -->
              </div>
            </div>
            <!-- End of Active Connections -->
            
            <script type="text/javascript">
            var addActiveConnection = function(param) {
              var template = '<div class="media"> <div class="media-body"><a href="javascript:displayTables()">' + param + '</a></div> </div>';
              $( template ).appendTo("#activeConnections");
            }

            var displayTables = function() {
              var tables = ["brule2tas_link", "businessrules", "dbconnections", "dqs_bounds", "job_transfer_queue", "job2job_link", "jobs", "roles", "routes", "scripts", "scripts_logs", "task2job_link", "tasks", "users"];
			  for (i in tables) {
                 var template = '<div class="media"> <div class="media-body">' + tables[i] + '</div> </div>';
                $( template ).appendTo("#tables");
			  }
            }
            </script>

            <!-- Active Connections -->
            <div class="tile">
              <h2 class="tile-title">Connections</h2>
              <div class="tile-config dropdown">
                <a data-toggle="dropdown" href="" class="tooltips tile-menu" title="" data-original-title="Options"></a>
                <ul class="dropdown-menu pull-right text-right">
                  <li><a href="">Edit</a></li>
                  <li><a href="">Delete</a></li>
                </ul>
              </div>

              <div class="listview icon-list">
                <div class="media">
                  <div class="media-body"><a href="javascript:addActiveConnection('Application Database');">Application Database</a></div>
                </div>

                <div class="media">
                  <div class="media-body"><a href="javascript:addActiveConnection('Data Database');">Data Database</a></div>
                </div>

                <div class="media">
                  <div class="media-body"><a href="javascript:addActiveConnection('Fake Database');">Fake Database</a></div>
                </div>

                <div class="media">
                  <div class="media-body"><a href="javascript:addActiveConnection('Local DB');">Local DB</a></div>
                </div>

                <div class="media p-5 text-center l-100">
                  <a href=""><small>More...</small></a>
                </div>
              </div>
            </div>
            <!-- End of Active Connections -->

          </div>
          <!-- End of Connections and Tables -->
          
          <!-- Tables -->
          <div class="col-md-2">

            <script type="text/javascript">
            var addActiveConnection = function(param) {
              var template = '<div class="media"> <div class="media-body">' + param + '</div> </div>';
              $( template ).appendTo("#activeConnections");
            }
            </script>

            <!-- Active Connections -->
            <div class="tile">
              <h2 class="tile-title">Tables</h2>
              <div class="tile-config dropdown">
                <a data-toggle="dropdown" href="" class="tooltips tile-menu" title="" data-original-title="Options"></a>
                <ul class="dropdown-menu pull-right text-right">
                  <li><a href="">Edit</a></li>
                  <li><a href="">Delete</a></li>
                </ul>
              </div>

              <div class="listview icon-list" id="tables">
              </div>
            </div>
            <!-- End of Active Connections -->

          </div>
          <!-- End of Tables -->

          <!-- Graphs and Stuff -->
          <div class="col-md-8">
            <div class="tab-container tile">
              <ul class="nav tab nav-tabs">
                <li class="active"><a href="#metadata">Metadata</a></li>
                <li><a href="#dataquality">Data Quality</a></li>
                <li><a href="#dataprofile">Data Profile</a></li>
                <li><a href="#info">Connection Information</a></li>
              </ul>

              <div class="tab-content">
                <div class="tab-pane active" id="metadata">
                  <table class="table tile">
                    <tbody>
                      <tr>
                        <th width="25%"><b>Connection</b></th>
                        <td>jdbc:sqlserver://127.0.0.1:1433;databaseName=development</td>
                      </tr>
                      <tr>
                        <th width="25%"><b>Table Name</b></th>
                        <td>jobs</td>
                      </tr>
                      <tr>
                        <th>Primary Key</th>
                        <td>id</td>
                      </tr>
                      <tr>
                        <th>Rows</th>
                        <td>5</td>
                      </tr>
                      <tr>
                        <th>Columns</th>
                        <td>4</td>
                      </tr>
                    </tbody>
                  </table>
                  <table id="columns" class="table tile dataTable no-footer" style="" role="grid"
                    aria-describedby="columns_info">
                    <thead>
                      <tr role="row">
                        <th class="sorting_asc" tabindex="0" aria-controls="columns" rowspan="1" colspan="1"
                          aria-sort="ascending" aria-label="Name: activate to sort column descending"
                          style="width: 339px;">Name</th>
                        <th class="sorting" tabindex="0" aria-controls="columns" rowspan="1" colspan="1"
                          aria-label="Size: activate to sort column ascending" style="width: 126px;">Size</th>
                        <th class="sorting" tabindex="0" aria-controls="columns" rowspan="1" colspan="1"
                          aria-label="SQL Type: activate to sort column ascending" style="width: 225px;">SQL Type</th>
                        <th class="sorting" tabindex="0" aria-controls="columns" rowspan="1" colspan="1"
                          aria-label="Type Name: activate to sort column ascending" style="width: 265px;">Type Name</th>
                        <th class="sorting" tabindex="0" aria-controls="columns" rowspan="1" colspan="1"
                          aria-label="Decimal Digits: activate to sort column ascending" style="width: 358px;">Decimal
                          Digits</th>
                        <th class="sorting" tabindex="0" aria-controls="columns" rowspan="1" colspan="1"
                          aria-label="Nullable: activate to sort column ascending" style="width: 248px;">Nullable</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr role="row" class="odd">
                        <td class="sorting_1">businessrule_id</td>
                        <td>10</td>
                        <td>4</td>
                        <td>INT</td>
                        <td>0</td>
                        <td>NO</td>
                      </tr>
                      <tr role="row" class="even">
                        <td class="sorting_1">id</td>
                        <td>10</td>
                        <td>4</td>
                        <td>INT</td>
                        <td>0</td>
                        <td>NO</td>
                      </tr>
                      <tr role="row" class="odd">
                        <td class="sorting_1">name</td>
                        <td>30</td>
                        <td>12</td>
                        <td>VARCHAR</td>
                        <td>0</td>
                        <td>YES</td>
                      </tr>
                      <tr role="row" class="even">
                        <td class="sorting_1">priority</td>
                        <td>10</td>
                        <td>4</td>
                        <td>INT</td>
                        <td>0</td>
                        <td>YES</td>
                      </tr>
                    </tbody>
                  </table>
                </div>

                <div class="tab-pane" id="dataquality">
                  <h3 class="block-title">Outliers</h3>
                  <table class="table tile">
                    <tbody>
                      <tr>
                        <th class="tableHeader">Row Id</th>
                        <th class="tableHeader">Row Value</th>
                      </tr>
                      <tr>
                        <td><div>709</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>273</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>791</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>430</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>475</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>432</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>399</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>873</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>279</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>752</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>875</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>435</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>876</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>514</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>756</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>636</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>637</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>879</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>714</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>956</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>759</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>99</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>13</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>281</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>681</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>560</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>120</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>881</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>122</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>685</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>762</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>564</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>642</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>884</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>521</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>127</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>800</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>404</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>966</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>406</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>527</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>803</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>407</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>968</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>809</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>25</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>26</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>692</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>373</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>693</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>771</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>498</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>411</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>774</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>378</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>852</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>413</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>699</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>854</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>931</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>811</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>415</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>812</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>813</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>934</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>816</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>817</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>818</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>32</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>819</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>33</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>420</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>863</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>423</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>500</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>820</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>821</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>865</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>701</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>547</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>789</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>823</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>427</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>669</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>825</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>506</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>826</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>705</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>749</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>827</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>948</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                      <tr>
                        <td><div>828</div></td>
                        <td><div>10.0</div></td>
                      </tr>
                      <tr>
                        <td><div>707</div></td>
                        <td><div>13.0</div></td>
                      </tr>
                      <tr>
                        <td><div>949</div></td>
                        <td><div>12.0</div></td>
                      </tr>
                    </tbody>
                  </table>
                </div>

                <div class="tab-pane" id="dataprofile">
                  <table class="table tile" id="numericGraphTable">
                    <tbody>
                      <tr>
                        <td class="tableNamesForGraph"><table id="numericalGraphDisplay">
                            <tbody>
                              <tr>
                                <th class="tableHeader"><div class="columnName">
                                    <button class="btn btn-sm btn-alt" id="getGraphBtn"
                                      onclick="DATA_PROFILE.showGraph('type_id')">type_id</button>
                                  </div></th>
                              </tr>
                            </tbody>
                          </table>
                          <table id="numericalGraphDisplay">
                            <tbody>
                              <tr>
                                <th class="tableHeader"><div class="columnName">
                                    <button class="btn btn-sm btn-alt" id="getGraphBtn"
                                      onclick="DATA_PROFILE.showGraph('fee_type_id')">fee_type_id</button>
                                  </div></th>
                              </tr>
                            </tbody>
                          </table>
                          <table id="numericalGraphDisplay">
                            <tbody>
                              <tr>
                                <th class="tableHeader"><div class="columnName">
                                    <button class="btn btn-sm btn-alt" id="getGraphBtn"
                                      onclick="DATA_PROFILE.showGraph('new_amt')">new_amt</button>
                                  </div></th>
                              </tr>
                            </tbody>
                          </table>
                          <table id="numericalGraphDisplay">
                            <tbody>
                              <tr>
                                <th class="tableHeader"><div class="columnName">
                                    <button class="btn btn-sm btn-alt" id="getGraphBtn"
                                      onclick="DATA_PROFILE.showGraph('was_amt')">was_amt</button>
                                  </div></th>
                              </tr>
                            </tbody>
                          </table></td>
                        <td class="currentColumns"><table id="columnsInGraph">
                            <tbody>
                              <tr>
                                <td><div id="columnInGraph">
                                    <div class="columnIndex">was_amt: 04</div>
                                    <div class="columnIndex">new_amt: 03</div>
                                    <div class="columnIndex">fee_type_id: 02</div>
                                    <div class="columnIndex">type_id: 01</div>
                                  </div></td>
                              </tr>
                            </tbody>
                          </table></td>
                        <td><div class="notifyMaxInGraph" id="notifyMaxInGraph"></div>
                          <div class="vizBox" id="vizBox" style="min-height: 420px;">
                            <div style="position: relative; width: 900px; height: 420px;">
                              <svg id="d3plus" width="897" height="420" xmlns="http://www.w3.org/2000/svg"
                                xmlns:xlink="http://www.w3.org/1999/xlink">
                                <rect id="bg" fill="#ffffff" stroke="none" width="900" height="420"></rect>
                                <g id="timeline" transform="translate(0,352)">
                                <rect class="d3plus_timeline_play" shape-rendering="crispEdges" opacity="0.3" width="23"
                                  height="23" fill="#ffffff" stroke="#ffffff" stroke-width="1" x="388" y="5"></rect>
                                <text class="d3plus_timeline_playIcon" fill="#444444" stroke="none" font-weight="200"
                                  font-family="Helvetica Neue" font-size="11px" text-anchor="middle" x="400.5" y="17"
                                  dy="0.5ex" opacity="0.3" style="font-family: FontAwesome; pointer-events: none;"></text>
                                <rect class="d3plus_timeline_background" shape-rendering="crispEdges" width="94"
                                  height="24" fill="#ffffff" x="415" y="5"></rect>
                                <g id="ticks" transform="translate(416,6)">
                                <g class="tick" transform="translate(0,0)" style="opacity: 1;">
                                <line y2="22" stroke="#cccccc" shape-rendering="crispEdges" stroke-width="1" x2="0"></line>
                                <text dy="0em" y="0" x="0" style="text-anchor: middle;"></text></g>
                                <g class="tick" transform="translate(23,0)" style="opacity: 1;">
                                <line y2="22" stroke="#cccccc" shape-rendering="crispEdges" stroke-width="1" x2="0"></line>
                                <text dy="0em" y="0" x="0" style="text-anchor: middle;"></text></g>
                                <g class="tick" transform="translate(46,0)" style="opacity: 1;">
                                <line y2="22" stroke="#cccccc" shape-rendering="crispEdges" stroke-width="1" x2="0"></line>
                                <text dy="0em" y="0" x="0" style="text-anchor: middle;"></text></g>
                                <g class="tick" transform="translate(69,0)" style="opacity: 1;">
                                <line y2="22" stroke="#cccccc" shape-rendering="crispEdges" stroke-width="1" x2="0"></line>
                                <text dy="0em" y="0" x="0" style="text-anchor: middle;"></text></g>
                                <g class="tick" transform="translate(92,0)" style="opacity: 1;">
                                <line y2="22" stroke="#eaeaea" shape-rendering="crispEdges" stroke-width="1" x2="0"></line>
                                <text dy="0em" y="0" x="0" style="text-anchor: middle;"></text></g>
                                <path class="domain" fill="none" d="M0,22V0H92V22"></path></g>
                                <g id="brush" transform="translate(416,6)" opacity="1"
                                  style="pointer-events: all; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);">
                                <rect class="background" x="0" width="92" fill="none" height="22"
                                  shape-rendering="crispEdges" style="visibility: visible; cursor: crosshair;"></rect>
                                <rect class="extent" x="0" width="92" opacity="0.75" height="22" fill="#c3c3c3"
                                  shape-rendering="crispEdges" style="cursor: move;"></rect>
                                <g class="resize e" transform="translate(92,0)" style="cursor: ew-resize;">
                                <rect class="d3plus_handle" fill="#666" transform="translate(-3,0)" width="3"
                                  shape-rendering="crispEdges" opacity="1" height="22" style="visibility: visible;"></rect>
                                <rect x="-3" width="6" height="22" style="visibility: hidden;"></rect></g>
                                <g class="resize w" transform="translate(0,0)" style="cursor: ew-resize;">
                                <rect class="d3plus_handle" fill="#666" transform="translate(0,0)" width="3"
                                  shape-rendering="crispEdges" opacity="1" height="22" style="visibility: visible;"></rect>
                                <rect x="-3" width="6" height="22" style="visibility: hidden;"></rect></g></g>
                                <g id="labels">
                                <text stroke="none" y="0" dy="0.5ex" x="0" font-weight="200"
                                  font-family="Helvetica Neue" font-size="11px" text-anchor="middle" opacity="1"
                                  fill="rgba(68,68,68,1)" transform="translate(428,17)" pointer-events="none">01</text>
                                <text stroke="none" y="0" dy="0.5ex" x="0" font-weight="200"
                                  font-family="Helvetica Neue" font-size="11px" text-anchor="middle" opacity="1"
                                  fill="rgba(68,68,68,1)" transform="translate(451,17)" pointer-events="none">02</text>
                                <text stroke="none" y="0" dy="0.5ex" x="0" font-weight="200"
                                  font-family="Helvetica Neue" font-size="11px" text-anchor="middle" opacity="1"
                                  fill="rgba(68,68,68,1)" transform="translate(474,17)" pointer-events="none">03</text>
                                <text stroke="none" y="0" dy="0.5ex" x="0" font-weight="200"
                                  font-family="Helvetica Neue" font-size="11px" text-anchor="middle" opacity="1"
                                  fill="rgba(68,68,68,1)" transform="translate(497,17)" pointer-events="none">04</text></g></g>
                                <g id="key" transform="translate(0,420)"></g>
                                <g id="footer" transform="translate(0,420)"></g>
                                <clipPath id="clipping_d3plus_1466797442116">
                                <rect width="897" height="349"></rect></clipPath>
                                <g id="container" clip-path="url(#clipping_d3plus_1466797442116)"
                                  transform="translate(0,0)">
                                <g id="zoom">
                                <g id="d3plus_viz" transform="">
                                <rect id="d3plus_overlay" width="897" height="349" opacity="0" x="0" y="0"
                                  style="cursor: auto;"></rect>
                                <g id="app">
                                <g id="box" opacity="1">
                                <g id="d3plus_graph_plane" transform="translate(49,10)">
                                <rect id="d3plus_graph_background" x="0" y="0" width="838" height="291" fill="#fafafa"
                                  stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></rect>
                                <path id="d3plus_graph_mirror" fill="#000" fill-opacity="0.03" stroke-width="1"
                                  stroke="#ccc" stroke-dasharray="10,10" opacity="0" d="M 838 291 L 0 291 L 838 0 Z"></path>
                                <g id="d3plus_graph_xticks" transform="translate(0,291)">
                                <g class="tick" transform="translate(0,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#444" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200" text-anchor="middle"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">0</tspan></text></g>
                                <g class="tick" transform="translate(40.327237728585175,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">5</tspan></text></g>
                                <g class="tick" transform="translate(80.65447545717035,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">10</tspan></text></g>
                                <g class="tick" transform="translate(120.98171318575554,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">15</tspan></text></g>
                                <g class="tick" transform="translate(161.3089509143407,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">20</tspan></text></g>
                                <g class="tick" transform="translate(201.63618864292587,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">25</tspan></text></g>
                                <g class="tick" transform="translate(241.96342637151108,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">30</tspan></text></g>
                                <g class="tick" transform="translate(282.29066410009625,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">35</tspan></text></g>
                                <g class="tick" transform="translate(322.6179018286814,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">40</tspan></text></g>
                                <g class="tick" transform="translate(362.9451395572666,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">45</tspan></text></g>
                                <g class="tick" transform="translate(403.27237728585175,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">50</tspan></text></g>
                                <g class="tick" transform="translate(443.5996150144369,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">55</tspan></text></g>
                                <g class="tick" transform="translate(483.92685274302215,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">60</tspan></text></g>
                                <g class="tick" transform="translate(524.2540904716072,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">65</tspan></text></g>
                                <g class="tick" transform="translate(564.5813282001925,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">70</tspan></text></g>
                                <g class="tick" transform="translate(604.9085659287776,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">75</tspan></text></g>
                                <g class="tick" transform="translate(645.2358036573628,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">80</tspan></text></g>
                                <g class="tick" transform="translate(685.563041385948,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">85</tspan></text></g>
                                <g class="tick" transform="translate(725.8902791145332,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">90</tspan></text></g>
                                <g class="tick" transform="translate(766.2175168431183,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">95</tspan></text></g>
                                <g class="tick" transform="translate(806.5447545717035,0)" style="opacity: 1;">
                                <line y2="10" x2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy="0px" y="15" x="-8.5" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px; font-size: 10px;"
                                  text-anchor="middle" transform="rotate(0, -3, 20.5)translate(0,-2.2)">
                                <tspan x="0px" dy="11px" dominant-baseline="alphabetic" style="baseline-shift: 0%;">100</tspan></text></g>
                                <path class="domain" d="M0,10V0H838V10" fill="none" stroke="none"></path></g>
                                <g id="d3plus_graph_yticks" transform="translate(0, 0)">
                                <g class="tick" transform="translate(0,254.625)" style="opacity: 1;">
                                <line x2="-10" y2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy=".32em" x="-15" y="0" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: end; text-transform: none; letter-spacing: 0px;">01</text></g>
                                <g class="tick" transform="translate(0,181.875)" style="opacity: 1;">
                                <line x2="-10" y2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy=".32em" x="-15" y="0" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: end; text-transform: none; letter-spacing: 0px;">02</text></g>
                                <g class="tick" transform="translate(0,109.125)" style="opacity: 1;">
                                <line x2="-10" y2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy=".32em" x="-15" y="0" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: end; text-transform: none; letter-spacing: 0px;">03</text></g>
                                <g class="tick" transform="translate(0,36.375)" style="opacity: 1;">
                                <line x2="-10" y2="0" stroke="#ccc" stroke-width="1" shape-rendering="crispEdges"></line>
                                <text dy=".32em" x="-15" y="0" font-size="10px" stroke="none" fill="#666"
                                  font-family="Helvetica Neue" font-weight="200"
                                  style="text-anchor: end; text-transform: none; letter-spacing: 0px;">04</text></g>
                                <path class="domain" d="M-10,0H0V291H-10" fill="none" stroke="none"></path></g>
                                <g id="d3plus_graph_xgrid">
                                <line x1="0" x2="0" y1="0" y2="291" stroke="#444" stroke-width="1"
                                  shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="40.327237728585175" x2="40.327237728585175" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="80.65447545717035" x2="80.65447545717035" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="120.98171318575554" x2="120.98171318575554" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="161.3089509143407" x2="161.3089509143407" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="201.63618864292587" x2="201.63618864292587" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="241.96342637151108" x2="241.96342637151108" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="282.29066410009625" x2="282.29066410009625" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="322.6179018286814" x2="322.6179018286814" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="362.9451395572666" x2="362.9451395572666" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="403.27237728585175" x2="403.27237728585175" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="443.5996150144369" x2="443.5996150144369" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="483.92685274302215" x2="483.92685274302215" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="524.2540904716072" x2="524.2540904716072" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="564.5813282001925" x2="564.5813282001925" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="604.9085659287776" x2="604.9085659287776" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="645.2358036573628" x2="645.2358036573628" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="685.563041385948" x2="685.563041385948" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="725.8902791145332" x2="725.8902791145332" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="766.2175168431183" x2="766.2175168431183" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="806.5447545717035" x2="806.5447545717035" y1="0" y2="291" stroke="#ccc"
                                  stroke-width="1" shape-rendering="crispEdges" style="opacity: 1;"></line></g>
                                <g id="d3plus_graph_ygrid">
                                <line x1="0" x2="838" y1="254.625" y2="254.625" stroke="#ccc" stroke-width="1"
                                  shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="0" x2="838" y1="181.875" y2="181.875" stroke="#ccc" stroke-width="1"
                                  shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="0" x2="838" y1="109.125" y2="109.125" stroke="#ccc" stroke-width="1"
                                  shape-rendering="crispEdges" style="opacity: 1;"></line>
                                <line x1="0" x2="838" y1="36.375" y2="36.375" stroke="#ccc" stroke-width="1"
                                  shape-rendering="crispEdges" style="opacity: 1;"></line></g>
                                <g id="d3plus_graph_x_userlines"></g>
                                <g id="d3plus_graph_y_userlines"></g></g>
                                <text stroke="none" id="d3plus_graph_xlabel" x="448.5" y="339"
                                  font-family="Helvetica Neue" font-weight="200" font-size="12px" fill="#444"
                                  dominant-baseline="central"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px;">Percentile</text>
                                <text stroke="none" id="d3plus_graph_ylabel" x="-155.5" y="10" transform="rotate(-90)"
                                  font-family="Helvetica Neue" font-weight="200" font-size="12px" fill="#444"
                                  dominant-baseline="central"
                                  style="text-anchor: middle; text-transform: none; letter-spacing: 0px;">Distribution</text></g></g>
                                <g id="edges" opacity="1"></g>
                                <g id="focus"></g>
                                <g id="edge_hover" opacity="0"></g>
                                <g id="data" opacity="1">
                                <g opacity="1" transform="translate(468.4032723772858,264.625)scale(1)"
                                  class="d3plus_rect" style="cursor: auto;">
                                <rect class="d3plus_data" x="-189.53801732435034" y="-29.375" width="379.07603464870067"
                                  height="58.75" fill="white" opacity="0.9" vector-effect="non-scaling-stroke"
                                  style="stroke: rgb(68, 68, 68); stroke-width: 1;" shape-rendering="crispEdges"
                                  transform="" ry="0" rx="0"></rect></g>
                                <g opacity="1" transform="translate(649.8758421559191,191.875)scale(1)"
                                  class="d3plus_rect">
                                <rect class="d3plus_data" x="-98.80173243503373" y="-29.375" width="197.60346487006746"
                                  height="58.75" fill="white" opacity="0.9" vector-effect="non-scaling-stroke"
                                  style="stroke: rgb(68, 68, 68); stroke-width: 1;" shape-rendering="crispEdges"
                                  transform="" ry="0" rx="0"></rect></g>
                                <g opacity="1" transform="translate(573.2540904716072,119.125)scale(1)"
                                  class="d3plus_rect">
                                <rect class="d3plus_data" x="-221.79980750721845" y="-29.375" width="443.5996150144369"
                                  height="58.75" fill="white" opacity="0.9" vector-effect="non-scaling-stroke"
                                  style="stroke: rgb(68, 68, 68); stroke-width: 1;" shape-rendering="crispEdges"
                                  transform="" ry="0" rx="0"></rect></g>
                                <g opacity="1" transform="translate(497.64051973051005,46.375)scale(1)"
                                  class="d3plus_rect">
                                <rect class="d3plus_data" x="-287.33156881616935" y="-29.375" width="574.6631376323387"
                                  height="58.75" fill="white" opacity="0.9" vector-effect="non-scaling-stroke"
                                  style="stroke: rgb(68, 68, 68); stroke-width: 1;" shape-rendering="crispEdges"
                                  transform="" ry="0" rx="0"></rect></g>
                                <g opacity="1" transform="translate(468.4032723772858,264.625)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges" x1="0" x2="0" y1="0"
                                  y2="0" style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line>
                                <rect id="d3plus_label_bg_median_line_-30578659200000" class="d3plus_label_bg"
                                  opacity="1" fill="#fff" height="19" width="19.21875" x="-9.609375" y="-22.7656250"
                                  transform="rotate(0,0,0)scale(1)translate(0,0)rotate(0, -160.1630173243503, 0)"></rect>
                                <text font-size="11px" id="d3plus_label_median_line_-30578659200000"
                                  class="d3plus_label" opacity="1" font-weight="200" font-family="Helvetica Neue"
                                  stroke="none" pointer-events="none" fill="#64615f" text-anchor="middle"
                                  x="-181.5380173243503" y="-21.375" style="font-size: 11px;"
                                  transform="rotate(0,0,0)scale(1)translate(0,0)rotate(0, -160.1630173243503, 0)translate(0,12.905)">
                                <tspan x="0px" dy="12.100000000000001px" dominant-baseline="alphabetic"
                                  style="baseline-shift: 0%;">52</tspan></text></g>
                                <g opacity="1" transform="translate(468.4032723772858,264.625)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges"
                                  x1="-379.07603464870067" x2="-189.53801732435034" y1="0" y2="0"
                                  style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line></g>
                                <g opacity="1" transform="translate(468.4032723772858,264.625)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges"
                                  x1="379.07603464870067" x2="189.53801732435034" y1="0" y2="0"
                                  style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line></g>
                                <g opacity="1" transform="translate(649.8758421559191,191.875)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges" x1="0" x2="0" y1="0"
                                  y2="0" style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line>
                                <rect id="d3plus_label_bg_median_line_-30547123200000" class="d3plus_label_bg"
                                  opacity="1" fill="#fff" height="19" width="28.390625" x="-14.1875" y="-22.7656250"
                                  transform="rotate(0,0,0)scale(1)translate(0,0)rotate(0, -69.42673243503373, 0)"></rect>
                                <text font-size="11px" id="d3plus_label_median_line_-30547123200000"
                                  class="d3plus_label" opacity="1" font-weight="200" font-family="Helvetica Neue"
                                  stroke="none" pointer-events="none" fill="#5f5f5f" text-anchor="middle"
                                  x="-90.80173243503373" y="-21.375" style="font-size: 11px;"
                                  transform="rotate(0,0,0)scale(1)translate(0,0)rotate(0, -69.42673243503373, 0)translate(0,12.905)">
                                <tspan x="0px" dy="12.100000000000001px" dominant-baseline="alphabetic"
                                  style="baseline-shift: 0%;">74.5</tspan></text></g>
                                <g opacity="1" transform="translate(649.8758421559191,191.875)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges"
                                  x1="-197.6034648700674" x2="-98.80173243503373" y1="0" y2="0"
                                  style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line></g>
                                <g opacity="1" transform="translate(649.8758421559191,191.875)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges"
                                  x1="197.60346487006734" x2="98.80173243503373" y1="0" y2="0"
                                  style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line></g>
                                <g opacity="1" transform="translate(653.9085659287776,119.125)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges" x1="0" x2="0" y1="0"
                                  y2="0" style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line>
                                <rect id="d3plus_label_bg_median_line_-30515587200000" class="d3plus_label_bg"
                                  opacity="1" fill="#fff" height="19" width="19.21875" x="-9.609375" y="-22.7656250"
                                  transform="rotate(0,0,0)scale(1)translate(0,0)rotate(0, -111.77033205004807, 0)"></rect>
                                <text font-size="11px" id="d3plus_label_median_line_-30515587200000"
                                  class="d3plus_label" opacity="1" font-weight="200" font-family="Helvetica Neue"
                                  stroke="none" pointer-events="none" fill="#616161" text-anchor="middle"
                                  x="-133.14533205004807" y="-21.375" style="font-size: 11px;"
                                  transform="rotate(0,0,0)scale(1)translate(0,0)rotate(0, -111.77033205004807, 0)translate(0,12.905)">
                                <tspan x="0px" dy="12.100000000000001px" dominant-baseline="alphabetic"
                                  style="baseline-shift: 0%;">75</tspan></text></g>
                                <g opacity="1" transform="translate(573.2540904716072,119.125)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges"
                                  x1="-443.59961501443695" x2="-221.79980750721845" y1="0" y2="0"
                                  style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line></g>
                                <g opacity="1" transform="translate(573.2540904716072,119.125)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges"
                                  x1="274.22521655437924" x2="221.79980750721845" y1="0" y2="0"
                                  style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line></g>
                                <g opacity="1" transform="translate(553.0904716073147,46.375)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges" x1="0" x2="0" y1="0"
                                  y2="0" style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line>
                                <rect id="d3plus_label_bg_median_line_-30484051200000" class="d3plus_label_bg"
                                  opacity="1" fill="#fff" height="19" width="28.390625" x="-14.1875" y="-22.7656250"
                                  transform="rotate(0,0,0)scale(1)translate(0,0)rotate(0, -202.50661693936468, 0)"></rect>
                                <text font-size="11px" id="d3plus_label_median_line_-30484051200000"
                                  class="d3plus_label" opacity="1" font-weight="200" font-family="Helvetica Neue"
                                  stroke="none" pointer-events="none" fill="#626262" text-anchor="middle"
                                  x="-223.88161693936468" y="-21.375" style="font-size: 11px;"
                                  transform="rotate(0,0,0)scale(1)translate(0,0)rotate(0, -202.50661693936468, 0)translate(0,12.905)">
                                <tspan x="0px" dy="12.100000000000001px" dominant-baseline="alphabetic"
                                  style="baseline-shift: 0%;">62.5</tspan></text></g>
                                <g opacity="1" transform="translate(497.64051973051005,46.375)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges"
                                  x1="-440.57507218479304" x2="-287.33156881616935" y1="0" y2="0"
                                  style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line></g>
                                <g opacity="1" transform="translate(497.64051973051005,46.375)scale(1)"
                                  class="d3plus_whisker">
                                <line class="d3plus_data" fill="none" shape-rendering="crispEdges"
                                  x1="349.83878729547644" x2="287.33156881616935" y1="0" y2="0"
                                  style="stroke-width: 1; stroke: rgb(68, 68, 68);"
                                  marker-start="url(#d3plus_whisker_marker)"></line></g></g>
                                <g id="data_focus"></g>
                                <g id="d3plus_labels"></g></g></g></g>
                                <defs>
                                <marker id="d3plus_whisker_marker" markerUnits="userSpaceOnUse"
                                  style="overflow: visible;">
                                <line x1="0" x2="0" y1="-29.375" y2="29.375" fill="none" shape-rendering="crispEdges"
                                  style="stroke-width: 2; stroke: rgb(68, 68, 68);"></line></marker></defs></svg>
                              <div id="d3plus_message" opacity="0"
                                style="display: none; color: rgb(68, 68, 68); font-family: &amp; amp; quot; Helvetica Neue&amp;amp; quot;; font-weight: 200; font-size: 16px; padding: 5px; position: absolute; text-align: center; left: 50%; width: auto; margin-left: 0px; top: 50%; bottom: auto; margin-top: 0px; opacity: 0; background-color: rgb(255, 255, 255);">
                                <div class="d3plus_message_text" style="display: block;">Drawing Visualization</div>
                              </div>
                              <div id="d3plus_drawer"
                                style="text-align: center; position: absolute; width: 887px; height: auto; top: auto; right: auto; bottom: 0px; left: auto;">
                                <div class="d3plus_drawer_ui" style="display: inline-block; padding: 5px;">
                                  <div id="d3plus_toggle_visualization_type"
                                    style="position: relative; overflow: visible; vertical-align: top; display: inline-block; margin: 0px;">
                                    <div class="d3plus_title"
                                      style="display: inline-block; color: rgb(68, 68, 68); font-family: &amp; amp; quot; Helvetica Neue&amp;amp; quot;; font-size: 11px; font-weight: 200; padding: 5px; border: 1px solid transparent;">Visualization
                                      Type</div>
                                    <div class="d3plus_toggle" style="display: inline-block; vertical-align: top;">
                                      <div id="d3plus_button_default1"
                                        style="position: relative; overflow: visible; vertical-align: top; display: inline-block; margin: 0px;">
                                        <div class="d3plus_node"
                                          style="color: rgb(68, 68, 68); border: 1px solid rgb(195, 195, 195); position: relative; margin: 0px; display: inline-block; font-family: &amp; amp; quot; Helvetica Neue&amp;amp; quot;; font-size: 11px; font-weight: 200; letter-spacing: 0px; background-color: rgb(255, 255, 255);">
                                          <div class="d3plus_button_element d3plus_button_label"
                                            style="display: block; text-align: left; position: static; width: auto; height: auto; margin-top: 0px; top: auto; left: auto; right: auto; transition: none; transform: none; opacity: 1; padding: 5px; background-image: none; background-color: transparent; background-size: 100%;">Scatter</div>
                                        </div>
                                      </div>
                                    </div>
                                    <div class="d3plus_toggle" style="display: inline-block; vertical-align: top;">
                                      <div id="d3plus_button_default1"
                                        style="position: relative; overflow: visible; vertical-align: top; display: inline-block; margin: 0px;">
                                        <div class="d3plus_node d3plus_button_active"
                                          style="color: rgba(68, 68, 68, 0.74902); border: 1px solid rgb(195, 195, 195); position: relative; margin: 0px; display: inline-block; font-family: &amp; amp; quot; Helvetica Neue&amp;amp; quot;; font-size: 11px; font-weight: 200; letter-spacing: 0px; background-color: rgb(195, 195, 195);">
                                          <div class="d3plus_button_element d3plus_button_label"
                                            style="display: block; text-align: left; position: static; width: auto; height: auto; margin-top: 0px; top: auto; left: auto; right: auto; transition: none; transform: none; opacity: 1; padding: 5px; background-image: none; background-color: transparent; background-size: 100%;">Box</div>
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div></td>
                      </tr>
                    </tbody>
                  </table>
                </div>

                <div class="tab-pane" id="info">
                  <table class="table tile">
                    <tbody>
                      <tr>
                        <th width="25%"><b>Connection Name</b></th>
                        <td>Data DB</td>
                      </tr>
                      <tr>
                        <th>JDBC Driver</th>
                        <td>com.microsoft.sqlserver.jdbc.SQLServerDriver</td>
                      </tr>
                      <tr>
                        <th>URL</th>
                        <td>jdbc:sqlserver://127.0.0.1:1433;databaseName=demo_dummy_data</td>
                      </tr>
                      <tr>
                        <th>User</th>
                        <td>dev</td>
                      </tr>
                      <tr>
                        <th>Password</th>
                        <td>Pass12345</td>
                      </tr>
                      <tr>
                        <th>Table Pattern</th>
                        <td></td>
                      </tr>
                      <tr>
                        <th>Catalog</th>
                        <td></td>
                      </tr>
                      <tr>
                        <th>Schema Pattern</th>
                        <td></td>
                      </tr>
                      <tr>
                        <th>Database Type</th>
                        <td></td>
                      </tr>
                      <tr>
                        <th>Table Type</th>
                        <td></td>
                      </tr>
                      <tr>
                        <th>Database Protocol</th>
                        <td></td>
                      </tr>
                      <tr>
                        <th>Column Pattern</th>
                        <td></td>
                      </tr>
                      <tr>
                        <th>Vendor Name</th>
                        <td>SQL_SERVER</td>
                      </tr>
                      <tr>
                        <td><a href="3/addfavorite.html" class="btn btn-alt btn-xs m-r-5">Add to Favorite</a> <a
                          href="3/edit.html" class="btn btn-alt btn-xs m-r-5">Edit</a> <a href="3/delete.html"
                          class="btn btn-alt btn-xs m-r-5">Delete</a></td>
                      </tr>
                    </tbody>
                  </table>
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
