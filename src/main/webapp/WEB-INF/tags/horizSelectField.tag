<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of corresponding property in bean object"%>
<%@ attribute name="label" required="true" rtexprvalue="true"
	description="Label appears in red color if input is considered as invalid after submission"%>
<%@ attribute name="names" required="true" rtexprvalue="true"
	type="java.util.List" description="Names in the list"%>
<%@ attribute name="size" required="true" rtexprvalue="true"
	description="Size of Select"%>

<spring:bind path="${name}">
	<c:set var="cssGroup"
		value="control-group ${status.error ? 'error' : '' }" />
	<div class="${cssGroup}">
		<div class="col-md-2">
			<label class="control-label">${label}</label>
		</div>

		<div class="col-md-10">
			<div class="controls">
				<form:select cssClass="form-control input-sm" path="${name}"
					items="${names}" size="${size}" />
				<span class="help-inline">${status.errorMessage}</span>
			</div>
		</div>
	</div>
</spring:bind>