<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<table class="ui celled table">
	<tbody>
		<tr>
			<td><spring:message code="application.moment" />
			<td data-label="moment"><jstl:out value="${application.moment}" /></td>
		</tr>
		<tr>
			<td><spring:message code="application.problem.title" />
			<td data-label="problem.title"><jstl:out value="${application.problem.title}" /></td>
		</tr>
		<tr>
			<td><spring:message code="application.answer.moment" />
			<td data-label="answer.moment"><jstl:out value="${application.answer.moment}" /></td>
		</tr>
		<tr>
			<td><spring:message code="application.answer.answerText" />
			<td data-label="answer.answerText"><jstl:out value="${application.answer.answerText}" /></td>
		</tr>
		<tr>
			<td><spring:message code="application.answer.codeLink" />
			<td data-label="answer.codeLink"><jstl:out value="${application.answer.codeLink}" /></td>
		</tr>
		<tr>
			<td><spring:message code="application.status" />
			<td data-label="status"><jstl:out value="${application.status}" /></td>
		</tr>
	</tbody>
</table>

<security:authorize access="hasRole('COMPANY')">
	<jstl:if test="${application.status == 'SUBMITTED'}">
	<br/>
		<a href="application/company/reject.do?applicationId=${application.id}" ><spring:message code="application.reject" /></a><br/>			
		<a href="application/company/approve.do?applicationId=${application.id}" ><spring:message code="application.approve" /></a>								
		
	</jstl:if>
</security:authorize>