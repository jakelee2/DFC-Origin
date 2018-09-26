<!DOCTYPE html>

<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<html lang="en">


<body>
<div class="container">
    <!--<h2><fmt:message key="welcome"/></h2>-->
    <spring:url value="/resources/images/dfc.png" htmlEscape="true" var="petsImage" />
    <img src="${petsImage}" />
</div>
</body>

</html>
