<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.dbadmin.util.*"%>

<!-- Sidebar -->
<aside id="sidebar">

  <!-- Sidbar Widgets -->
  <div class="side-widgets overflow">
    <!-- Profile Menu -->
    <div class="text-center s-widget m-b-25 dropdown" id="profile-menu">
      <a href="javascript:;" data-toggle="dropdown"> <img class="profile-pic animated"
        src="<spring:url value="/resources/images/profile-pic.jpg" htmlEscape="true" />" alt="">
      </a>
      <ul class="dropdown-menu profile-menu">
        <li><a href="/admin/user?username=">User Management</a> <i class="icon left">&#61903;</i><i
          class="icon right">&#61815;</i></li>
        <li><a href="/admin/route.html">Site Management</a> <i class="icon left">&#61903;</i><i class="icon right">&#61815;</i></li>
        <li><a href="/logout">Sign Out</a> <i class="icon left">&#61903;</i><i class="icon right">&#61815;</i></li>
      </ul>
      <h4 class="m-0">Super User</h4>
      Application User
    </div>

    <!-- Projects -->
    <div class="s-widget m-b-25">
      <h2 class="tile-title">Connection</h2>

      <div class="s-widget-body">
        <div class="side-border">
          <small><%= ActiveConnectionRegistry.INSTANCE.getLatestActiveConnectionIp() %></small>
        </div>
      </div>
      <div class="s-widget-body">
        <div class="side-border">
          <small><%= ActiveConnectionRegistry.INSTANCE.getLatestActiveConnectionDbName() %></small>
        </div>
      </div>
    </div>
  </div>
  

  <!-- Side Menu -->
  <ul class="list-unstyled side-menu">
    <li><a class="sa-side-home" href="/dashboard"> <span class="menu-item">Home</span>
    </a></li>
    <li><a class="sa-side-chart" href="/ui"> <span class="menu-item">Business Intelligence</span>
    </a></li>
    <li><a class="sa-side-updates" href="/dbconnections"> <span class="menu-item">Connections</span>
    </a></li>
    
    <li><a class="sa-side-connection" href="/data"> <span class="menu-item">Data</span>
    </a></li>
    <li><a class="sa-side-download" href="/outliers"> <span class="menu-item">Data Quality</span>
    </a></li>
    <li><a class="sa-side-note" href="/dqoutput"> <span class="menu-item">Data Output</span>
    </a></li>
    <li><a class="sa-side-widget" href="/dbtables"> <span class="menu-item">Tables</span>
    </a></li>
    <li><a class="sa-side-analysis" href="/dqstats"> <span class="menu-item">Data Profile</span>
    </a></li>
    <li><a class="sa-side-note" href="/businessrules.html"> <span class="menu-item">Business Rules</span>
    </a></li>
    <li><a class="sa-side-widget" href="/jobboard"> <span class="menu-item">Jobs</span>
    </a></li>
    <li><a class="sa-side-time" href="/jobs.html"> <span class="menu-item">Job Management</span>
    </a></li>
<!--     
    <li><a class="sa-side-typography" href="/etltasks.html"> <span class="menu-item">Tasks</span>
    </a></li>
-->    
    <li><a class="sa-side-exit" href="/logout" onclick="DATA_PROFILE.clearDqTableInSessionStorage();"> <span
        class="menu-item">Log Out</span>
    </a></li>
  </ul>
</aside>