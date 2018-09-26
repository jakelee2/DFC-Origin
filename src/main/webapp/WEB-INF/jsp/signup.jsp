<!DOCTYPE html>

<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<meta charset="UTF-8">

<title>Data Analytics Signup Template</title>
<link href="<spring:url value="/resources/css/error.css" htmlEscape="true" />" rel="stylesheet">

<style>
@import url(http://fonts.googleapis.com/css?family=Exo:100,200,400);

@import
	url(http://fonts.googleapis.com/css?family=Source+Sans+Pro:700,400,300)
	;

body {
	margin: 0;
	padding: 0;
	background: #fff;
	color: #fff;
	font-family: Arial;
	font-size: 14px;
}

.body {
	position: absolute;
	top: -20px;
	left: -20px;
	right: -40px;
	bottom: -40px;
	width: auto;
	height: auto;
	/* change to spring format */
	background-image: url("<spring:url value="/ resources/ images/ login.png " htmlEscape="
		true " />");
	background-size: cover;
	-webkit-filter: blur(5px);
	z-index: 0;
}

.grad {
	position: absolute;
	top: -20px;
	left: -20px;
	right: -40px;
	bottom: -40px;
	width: auto;
	height: auto;
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, rgba(0, 0
		, 0, 0)), color-stop(100%, rgba(0, 0, 0, 0.65)));
	/* Chrome,Safari4+ */
	z-index: 1;
	opacity: 0.7;
}

.header {
	position: absolute;
	top: calc(50% - 35px);
	left: calc(50% - 475px);
	z-index: 2;
}

.header div {
	float: left;
	color: #fff;
	font-family: 'Exo', sans-serif;
	font-size: 35px;
	font-weight: 200;
}

.header div span {
	color: #e32636 !important;
}

.signup {
	position: absolute;
	top: calc(50% - 75px);
	left: calc(50% - 50px);
	height: 150px;
	width: 350px;
	padding: 10px;
	z-index: 2;
}

.signup input[type=text] {
	width: 250px;
	height: 30px;
	background: transparent;
	border: 1px solid rgba(255, 255, 255, 0.6);
	border-radius: 2px;
	color: #fff;
	font-family: 'Exo', sans-serif;
	font-size: 16px;
	font-weight: 400;
	padding: 4px;
}

.signup input[type=password] {
	width: 250px;
	height: 30px;
	background: transparent;
	border: 1px solid rgba(255, 255, 255, 0.6);
	border-radius: 2px;
	color: #fff;
	font-family: 'Exo', sans-serif;
	font-size: 16px;
	font-weight: 400;
	padding: 4px;
	margin-top: 10px;
}

.signup input[type=button] {
	width: 260px;
	height: 35px;
	background: #fff;
	border: 1px solid #fff;
	cursor: pointer;
	border-radius: 2px;
	color: #a18d6c;
	font-family: 'Exo', sans-serif;
	font-size: 16px;
	font-weight: 400;
	padding: 6px;
	margin-top: 10px;
}

.signup input[type=button]:hover {
	opacity: 0.8;
}

.signup input[type=button]:active {
	opacity: 0.6;
}

.signup input[type=submit] {
	width: 260px;
	height: 35px;
	background: #fff;
	border: 1px solid #fff;
	cursor: pointer;
	border-radius: 2px;
	color: #a18d6c;
	font-family: 'Exo', sans-serif;
	font-size: 16px;
	font-weight: 400;
	padding: 6px;
	margin-top: 10px;
}

.signup input[type=submit]:hover {
	opacity: 0.8;
}

.signup input[type=submit]:active {
	opacity: 0.6;
}

.signup input[type=text]:focus {
	outline: none;
	border: 1px solid rgba(255, 255, 255, 0.9);
}

.signup input[type=password]:focus {
	outline: none;
	border: 1px solid rgba(255, 255, 255, 0.9);
}

.signup input[type=button]:focus {
	outline: none;
}

.signup_error_msg {
	position: absolute;
	top: calc(50% - 175px);
	left: calc(50% - 60px);
	height: 150px;
	width: 350px;
	padding: 10px;
	z-index: 2;
}

.label {
	display: inline;
	padding: .2em .6em .3em;
	font-size: 20px;
	font-weight: bold;
	line-height: 1;
	color: #fff;
	text-align: center;
	white-space: nowrap;
	vertical-align: baseline;
	border-radius: .25em;
}

.label-danger {
	background-color: #d9534f;
	display: block;
	width: 256px;
	word-wrap: break-word;
	white-space: pre-wrap;
	text-align: justify;
}

::-webkit-input-placeholder {
	color: rgba(255, 255, 255, 0.6);
}

::-moz-input-placeholder {
	color: rgba(255, 255, 255, 0.6);
}
</style>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script>
  $(document).ready(function() {
    $("#signup_form").submit(function() {
      var dataToBeSent = {
        username : $("#username").val(),
        password : $("#password").val(),
        matchingPassword : $("#matchingPassword").val()
      };
      
      $.ajax({
        url : '/registration',
        data : dataToBeSent,
        type : 'post',
        success : function(msg) {
          $('#msgBox').css('visibility', 'hidden');
          setTimeout(function() {
            initMsgBox();
            renderMsg(msg);
          }, 1000); // get 1 second interval
        }
      });
      clearInputFields();
      return false;
    });
    
    function initMsgBox() {
      $('#msgBox').css('visibility', 'visible');
      $('#msgIcon').css('background-position', '-283px -35px');
      $('#msgBox').css('border-color', '#c40000');
      $('#msgHeader').css('color', '#c40000');
      $("#goBackToLogin").css('color', '#a18d6c');
    }
    
    function renderMsg(msg) {
      switch (msg) {
      case 'passWordMismatch':
        $("#msgHeader").text('There was a problem');
        $("#msgText").text('Passwords do not match');
        break;
      case 'passWordLength':
        $("#msgHeader").text('There was a problem');
        $("#msgText").text('Password length should be 3~20');
        break;
      case 'userNameExisting':
        $("#msgHeader").text('Username already exists');
        $("#msgText").text('Please try to login');
        $("#goBackToLogin").css('color', '#FF6600');
        break;
      case 'newUserCreated':
        $('#msgIcon').css('background-position', '-315px -35px');
        $('#msgBox').css('border-color', '#00CC33');
        $('#msgHeader').css('color', '#00CC33');
        $("#msgHeader").text('User account has been created');
        $("#msgText").text('Please wait for admin to approve your account');
        break;
      default:
        $('#msgBox').css('visibility', 'hidden');
      }
    }
    
    function clearInputFields() {
      // Clear forms here
      $('#username').val('');
      $('#password').val('');
      $('#matchingPassword').val('');
    }
  });
</script>
</head>

<body>

  <div class="body"></div>
  <div class="grad"></div>
  <div class="header">
    <div>
      DataAnalytics <span>Template</span>
    </div>
    <br />
    <div>Sign Up Here!</div>
  </div>
  <br>

  <div class="error_msg">
    <div class="a-box a-alert a-alert-error a-spacing-base" id="msgBox">
      <div class="a-box-inner a-alert-container">
        <h4 class="a-alert-heading" id="msgHeader">There was a problem</h4>
        <i class="a-icon a-icon-alert" id="msgIcon"></i>
        <div class="a-alert-content">
          <ul class="a-nostyle">
            <li><span id="msgText"></span></li>
          </ul>
        </div>
      </div>
    </div>
  </div>

  <!-- change to hit db with authentication info once REST API is up and running -->
  <div class="signup">
    <form method="post" class="form-horizontal" id="signup_form">
      <div class="input-group input-sm">
        <label class="input-group-addon" for="username"><i class="fa fa-user"></i></label> <input type="text"
          class="form-control" id="username" name="username" placeholder="Enter New Username" required>
      </div>
      <div class="input-group input-sm">
        <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label> <input type="password"
          class="form-control" id="password" name="password" placeholder="Enter New Password" required>
      </div>
      <div class="input-group input-sm">
        <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label> <input type="password"
          class="form-control" id="matchingPassword" name="matchingPassword" placeholder="Enter New Password Again"
          required>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

      <div class="form-actions">
        <input type="submit" class="btn btn-block btn-primary btn-default" value="Sign Up">
      </div>
    </form>

    <c:url var="loginUrl" value="/login" />
    <form action="${loginUrl}" method="get" class="form-horizontal">
      <input type="submit" value="Go Back to Login!" id="goBackToLogin" />
    </form>

  </div>
</body>
</html>
