<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<!-- Listing grid -->

<display:table name="companies" id="row" requestURI="company/list.do"
	pagesize="5" class="displaytag">
	
	<!-- Display -->
	<display:column>
		<a href="company/display.do?companyId=${row.id}"><spring:message code="company.display"/></a>
	</display:column>

	<!-- Attributes -->

	<spring:message code="company.commercialName" var="commercialNameHeader" />
	<display:column property="commercialName" title="${commercialNameHeader}"
		sortable="true" />

	<!-- Action links -->

	<display:column>
	<a href="position/listCompanyId.do?companyId=${row.id }"> <spring:message code="company.positions" /></a>
	</display:column>

</display:table>