<!DOCTYPE html>

<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta charset="UTF-8">

<title>Data Analytics Admin Panel</title>

<link href="<spring:url value="/resources/css/login.css" htmlEscape="true" />" rel="stylesheet">
<link href="<spring:url value="/resources/css/error.css" htmlEscape="true" />" rel="stylesheet">
<script src="<spring:url value="/resources/js/prefixfree.min.js" htmlEscape="true" />"></script>
<script src="<spring:url value="/resources/js/particles.min.js" htmlEscape="true" />"></script>

</head>

<body>

  <!--  particles on log in screen only, div loads it -->
  <div id="particles-js"></div>
  <script src="<spring:url value="/resources/js/particles_config.js" htmlEscape="true" />"></script>

  <div class="body"></div>
  <div class="grad"></div>
  <div class="header">
    <div>
      DataAnalytics<span>Admin</span>
    </div>
  </div>
  <br />
  <script type="text/javascript">
    <!-- change to spring page transition -->
      var myFunc = function() {
        var url = "./dashboard";
        document.location.href = url;
      }
    </script>
  <!-- change to hit db with authentication info once REST API is up and running -->
  <div class="error_msg">
    <c:if test="${param.error != null}">
      <div class="a-box a-alert-error a-spacing-base">
        <div class="a-box-inner a-alert-container">
          <h4 class="a-alert-heading">Authentication Error!</h4>
          <i class="a-icon-alert"></i>
          <div class="a-alert-content">
            <ul class="a-nostyle">
              <c:choose>
                <c:when test="${param.error == 'notfound'}">
                  <li><span class="a-list-item">User does not exist.</span></li>
                </c:when>
                <c:when test="${param.error == 'badcredential'}">
                  <li><span class="a-list-item">Incorrect password.</span></li>
                </c:when>
                <c:when test="${param.error == 'true'}">
                  <li><span class="a-list-item">Invalid Login Credentials</span></li>
                </c:when>
              </c:choose>
            </ul>
          </div>

        </div>
      </div>

    </c:if>
  </div>

  <div class="logout_success_msg">
    <c:if test="${param.logout != null}">
      <div class="alert-success">You have been logged out successfully</div>
    </c:if>
  </div>
  <div class="login">
    <c:url var="loginUrl" value="/login" />
    <form action="${loginUrl}" method="post" class="form-horizontal">
      <div class="input-group input-sm">
        <label class="input-group-addon" for="username"><i class="fa fa-user"></i></label> <input type="text"
          class="form-control" id="username" name="ssoId" placeholder="Enter Username" required>
      </div>
      <div class="input-group input-sm">
        <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label> <input type="password"
          class="form-control" id="password" name="password" placeholder="Enter Password" required>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

      <div class="form-actions">
        <input type="submit" class="btn btn-block btn-primary btn-default" value="Log in">
      </div>
    </form>

    <c:url var="signupUrl" value="/signup" />
    <form action="${signupUrl}" method="get" class="form-horizontal">
      <c:choose>
        <c:when test="${param.error == 'notfound'}">
          <input type="submit" value="Not registered? Sign Up Now!" style="color: #FF6600" />
        </c:when>
        <c:otherwise>
          <input type="submit" value="Not registered? Sign Up Now!" style="color: #a18d6c" />
        </c:otherwise>
      </c:choose>
    </form>

  </div>
</body>
</html>
