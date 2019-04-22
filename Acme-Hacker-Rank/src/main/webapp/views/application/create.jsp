<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	<form:form action ="application/hacker/create.do" modelAttribute ="applicationForm">
		
		<form:hidden path="id"/>
		
		<div>
			<form:label path="position">
		<spring:message code="application.position.title" />
	</form:label>	
	<form:select id="position" path="position">
		<form:options items="${positions}" itemLabel="title" />
	</form:select>
	<form:errors path="position" cssClass="error" />
		</div>
		
		
		<input type="submit" name="save" id="save"
		value="<spring:message code="application.save" />" >&nbsp; 
		
		<acme:cancel url="welcome/index.do" code="application.cancel"/>
	</form:form>




