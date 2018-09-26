<!DOCTYPE html>

<%@ page session="false"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en">

<body>
  <div class="container">

    <h2>Something happened...</h2>

    <p>${exception.message}</p>

    ${exception.message}.
    <c:forEach items="${exception.stackTrace}" var="stackTrace"> 
				${stackTrace} 
			</c:forEach>
  </div>
</body>

</html>
