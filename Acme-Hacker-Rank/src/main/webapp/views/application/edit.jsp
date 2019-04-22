<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action = "application/hacker/edit.do" modelAttribute="applicationForm">
	<form:hidden path="id"/>
	<div class="ui equal width form">
		<div class = "fields">
		
			<acme:textarea code="application.answer.answerText" path="answerText"/>
			
			<acme:textarea code="application.answer.codeLink" path="codeLink"/>
		
		</div>
	</div>
	
	<input type ="submit" name="edit" id="edit" value="<spring:message code="application.save"/>"/>
	
	<acme:cancel url="welcome/index.do" code="application.cancel"/>
</form:form>