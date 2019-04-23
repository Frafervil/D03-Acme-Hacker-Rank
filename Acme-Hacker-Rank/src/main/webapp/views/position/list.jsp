<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Buscar rutas por palabra clave -->
<form action="${requestURI }" method="get">
	<spring:message code="position.keyword" var="searchHeader"/>
	<input type="text" name="keyword">
	<input type="submit" value="${searchHeader}">
	<input type="hidden" name="keywordBool" value="true">
</form>

<!-- Listing grid -->

<display:table name="positions" id="row" requestURI="position/list.do"
	pagesize="5" class="displaytag">
	
	<!-- Display -->
	<display:column>
		<a href="position/display.do?positionId=${row.id}"><spring:message code="position.display"/></a>
	</display:column>

	<!-- Attributes -->

	<spring:message code="position.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}"
		sortable="true" />
		
	<spring:message code="position.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		sortable="true" />
		
	<spring:message code="position.deadline" var="deadlineHeader" />
	<display:column property="deadline" title="${deadlineHeader}"
		sortable="true" />
		
	<!-- Company -->
	<spring:message code="position.company" var="companyHeader"/>
	<display:column title="${companyHeader}">
			<a href="company/display.do?companyId=${row.company.id}">
				<jstl:out value="${row.company.commercialName}"/>
			</a>
	</display:column>

</display:table>

<!-- Create position -->
<security:authorize access="hasRole('COMPANY')">
		<acme:button url="position/company/create.do" code="position.create"/>
	
</security:authorize> 
