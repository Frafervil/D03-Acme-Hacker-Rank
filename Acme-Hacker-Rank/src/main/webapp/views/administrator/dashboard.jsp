<%--
 * dashboard.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->

<security:authorize access="hasRole('ADMIN')">
	
	<h3><spring:message code="administrator.statistics" /></h3>
	
	<table class="displayStyle">
		<tr>
			<th colspan="5"><spring:message code="administrator.statistics" /></th>
		</tr>
		
		<tr>
			<th><spring:message code="administrator.metrics" /></th>
			<th><spring:message code="administrator.average" /></th>
			<th><spring:message code="administrator.minimum" /></th>
			<th><spring:message code="administrator.maximum" /></th>
			<th><spring:message code="administrator.std" /></th>
		</tr>
		
		<tr>
			<td><spring:message code="administrator.positionsPerCompany" /></td>
			<td><jstl:out value="${avgPositionsPerCompany }" /></td>
			<td><jstl:out value="${minPositionsPerCompany }" /></td>
			<td><jstl:out value="${maxPositionsPerCompany }" /></td>
			<td><jstl:out value="${stddevPositionsPerCompany }" /></td>
		</tr>
		
		<tr>
			<td><spring:message code="administrator.applicationsPerHacker" /></td>
			<td><jstl:out value="${avgApplicationsPerHacker }" /></td>
			<td><jstl:out value="${minApplicationsPerHacker }" /></td>
			<td><jstl:out value="${maxApplicationsPerHacker }" /></td>
			<td><jstl:out value="${stddevApplicationsPerHacker }" /></td>
		</tr>
	</table>
	
	<h3><spring:message code="administrator.companiesWithMorePositions" /></h3>
	<display:table pagesize="10" class="displaytag" 
	name="companiesWithMorePositions" requestURI="dashboard/administrator/display.do" id="company">
		
	<spring:message code="administrator.company.commercialName" var="commercialName" />
	<display:column property="commercialName" title="${commercialName }" sortable="true"/>
	</display:table>
	
	<h3><spring:message code="administrator.hackersWithMoreApplications" /></h3>
	<display:table pagesize="10" class="displaytag" 
	name="hackersWithMoreApplications" requestURI="dashboard/administrator/display.do" id="hacker">
		
	<spring:message code="administrator.name" var="name" />
	<display:column property="name" title="${name }" sortable="true"/>

	<spring:message code="administrator.surname" var="surname" />
	<display:column property="surname" title="${surname }" sortable="true"/>
	</display:table>
	
	<h3><spring:message code="administrator.statistics" /></h3>
	
	<table class="displayStyle">
		<tr>
			<th colspan="5"><spring:message code="administrator.statistics" /></th>
		</tr>
		
		<tr>
			<th><spring:message code="administrator.metrics" /></th>
			<th><spring:message code="administrator.average" /></th>
			<th><spring:message code="administrator.minimum" /></th>
			<th><spring:message code="administrator.maximum" /></th>
			<th><spring:message code="administrator.std" /></th>
		</tr>
		
		<tr>
			<td><spring:message code="administrator.salariesOffered" /></td>
			<td><jstl:out value="${avgSalariesOffered }" /></td>
			<td><jstl:out value="${minSalariesOffered }" /></td>
			<td><jstl:out value="${maxSalariesOffered }" /></td>
			<td><jstl:out value="${stddevSalariesOffered }" /></td>
		</tr>
	</table>
	
	<h3><spring:message code="administrator.bestSalaryPosition" /></h3>
	<jstl:out value="${bestSalaryPosition.title}"></jstl:out>
	
	<h3><spring:message code="administrator.worstSalaryPosition" /></h3>
	<jstl:out value="${worstSalaryPosition.title}"></jstl:out>
	
</security:authorize>