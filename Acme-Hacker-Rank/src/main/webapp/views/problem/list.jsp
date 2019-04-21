<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<!-- Listing grid -->

<display:table name="problems" id="row" requestURI="problem/list.do"
	pagesize="5" class="displaytag">
	
	<!-- Display -->
	<display:column>
		<a href="problem/company/display.do?problemId=${row.id}"><spring:message code="problem.display"/></a>
	</display:column>

	<!-- Attributes -->

	<spring:message code="problem.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}"
		sortable="true" />
		
	<spring:message code="problem.isDraft" var="isDraftHeader" />
	<display:column property="isDraft" title="${isDraftHeader}"
		sortable="true" />
		
</display:table>
<br />
<acme:button url="problem/company/create.do" code="problem.create"/>