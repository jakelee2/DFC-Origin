<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- This jsp file is for access denied page.
 Depending on the 'role' value, different javascript function and UI messages are displayed. 
 -->
<html>
<head>
<jsp:include page="./fragments/headStatics.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>AccessDenied page</title>
</head>
<script type="text/javascript">
  window.onload = function() {
    <c:choose>
    <c:when test="${role == 'ROLE_NA'}">
      setTimeout(function() {
        window.location.href = '/logout';
      }, 10000);
    </c:when>
    <c:otherwise>
      setTimeout(function() {
        window.location.href = '/';
      }, 10000);
    </c:otherwise>
    </c:choose>
  };
</script>

<body id="skin-blur-ocean">
  <header id="header" class="media">

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
    <!-- Content -->
    <section id="content" class="container">
      <jsp:include page="./fragments/header.jsp" />
      <h4 class="page-title" style="font-size: 25px; color: rgba(255, 255, 0, 0.8)">Access Denied</h4>
      <div class="block-area">
        <div class="row">
          <div class="col-md-12" style="font-size: 20px">
            Dear <strong>${user}</strong>, You are not authorized to access this page. <br /> Please wait for an Admin to
            approve your access. <br /> <br />
            <c:choose>
              <c:when test="${role == 'ROLE_NA'}">
							You may click the Logout button or you will be redirected to the login page in 10 seconds.
							<a href="<c:url value="/logout" />" style="color: rgba(255, 255, 0, 1); font-weight: bold">Logout</a>
                <div id="targetPage" style="visibility: hidden">logout</div>
              </c:when>
              <c:otherwise>
							You may click Home button or you will be redirected to the home page in 10 seconds.
							<a href="<c:url value="/" />" style="color: rgba(255, 255, 0, 1); font-weight: bold">Home</a>
                <div id="targetPage" style="visibility: hidden">home</div>
              </c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>

      <!-- End of Dashboard Body -->
    </section>
  </section>
  <jsp:include page="./fragments/footStatics.jsp" />
</body>
</html>