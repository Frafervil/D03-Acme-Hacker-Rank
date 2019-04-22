<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<security:authorize access="hasRole('HACKER')">

<form action="application/hacker/list.do" method="get">
	
	<input type="radio" name="applicationStatus" value="0" checked> <spring:message code="application.status.all" />
	<input type="radio" name="applicationStatus" value="1"> <spring:message code="application.status.accepted" />
	<input type="radio" name="applicationStatus" value="2">  <spring:message code="application.status.pending" />
	<input type="radio" name="applicationStatus" value="3">  <spring:message code="application.status.rejected" />
	<input type="radio" name="applicationStatus" value="4">  <spring:message code="application.status.submitted" />
	<br />
	<spring:message code="application.status.choose" var="choose"/>
	<input type="submit" value="${choose}">
</form>
	
</security:authorize>

<security:authorize access="hasRole('COMPANY')">
	
	<form action="application/company/list.do" method="get">
	
	<input type="radio" name="applicationStatus" value="0" checked> <spring:message code="application.status.all" />
	<input type="radio" name="applicationStatus" value="1"> <spring:message code="application.status.accepted" />
	<input type="radio" name="applicationStatus" value="2">  <spring:message code="application.status.pending" />
	<input type="radio" name="applicationStatus" value="3">  <spring:message code="application.status.rejected" />
	<input type="radio" name="applicationStatus" value="4">  <spring:message code="application.status.submitted" />
	<br />
	<spring:message code="application.status.choose" var="choose"/>
	<input type="submit" value="${choose}">
	</form>
	
	
	
</security:authorize>



<!-- Listing grid -->

<display:table name="applications" id="row" requestURI="application/list.do"
	pagesize="5" class="displaytag">
	
	<!-- Display -->
	<security:authorize access="hasRole('HACKER')">
		<display:column>
			<a href="application/hacker/display.do?applicationId=${row.id}"><spring:message code="application.display"/></a>
		</display:column>
	</security:authorize>
	<security:authorize access="hasRole('COMPANY')">
		<display:column>
			<a href="application/company/display.do?applicationId=${row.id}"><spring:message code="application.display"/></a>
		</display:column>
	</security:authorize>
	<!-- Attributes -->
	
	<spring:message code="application.moment" var="applicationMoment" />
	<display:column property="moment" title="${applicationMoment}"
		sortable="true" />
	
	<spring:message code="application.problem.title" var="problemTitle" />
	<display:column property="problem.title" title="${problemTitle}"
		sortable="true" />
		
	<spring:message code="application.problem.statement" var="problemStatement" />
	<display:column property="problem.statement" title="${problemStatement}"
		sortable="true" />
	
	<spring:message code="application.answer.answerText" var="answerText" />
	<display:column property="answer.answerText" title="${answerText}"
		sortable="true" />

	<spring:message code="application.answer.codeLink" var="answerLink" />
	<display:column property="answer.codeLink" title="${answerLink}"
		sortable="true" />
	
	<spring:message code="application.answer.moment" var="answerMoment" />
	<display:column property="answer.moment" title="${answerMoment}"
		sortable="true" />
	
	<spring:message code="application.status" var="applicationStatus" />
	<display:column property="status" title="${applicationStatus}"
		sortable="true" />	
	
	<spring:message code="application.position.title" var="positionTitle" />
	<display:column property="position.title" title="${positionTitle}"
		sortable="true" />
	
	<spring:message code="application.hacker.name" var="hackerName" />
	<display:column property="hacker.name" title="${hackerName}"
		sortable="true" />

	
	<!-- Action links -->
	<security:authorize access="hasRole('HACKER')">
			<jstl:if test="${row.status == 'PENDING'}">
				<display:column>
					<a href="application/hacker/edit.do?applicationId=${row.id }"> <spring:message code="application.answer" /></a>
				</display:column>
			</jstl:if>
	</security:authorize>
</display:table>

	<security:authorize access="hasRole('HACKER')">
	<div>
		<a href="application/hacker/create.do"><spring:message code="application.create" /></a>
	</div>
</security:authorize> 