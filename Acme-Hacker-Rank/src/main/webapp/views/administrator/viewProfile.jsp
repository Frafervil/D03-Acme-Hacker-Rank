<%--
 * viewProfile.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>

<table class="ui celled table">
	<thead>
		<tr>
			<acme:image src="https://cdn1.iconfinder.com/data/icons/instagram-ui-glyph/48/Sed-09-128.png"/>
		</tr>
	</thead>
	<tbody>
		<tr>
			<acme:displayText code="administrator.name" dataLabel="name" path="${actor.name}"/>
		</tr>
		<tr>
			<acme:displayText dataLabel="surname" code="administrator.surname" path="${actor.surname}"/>
		</tr>
		<tr>
			<acme:displayText dataLabel="phone" code="administrator.phone" path="${actor.phone}"/>
		</tr>
		<tr>
			<acme:displayText dataLabel="address" code="administrator.address" path="${actor.address}"/>
		</tr>
		<tr>
			<acme:displayText dataLabel="vatNumber" code="administrator.vatNumber" path="${actor.vatNumber}"/>
		</tr>
		<tr>
			<acme:displayText dataLabel="email" code="administrator.email" path="${actor.email}"/>
		</tr>
	</tbody>
</table>

<acme:button url="administrator/edit.do" code="administrator.edit"/>

<jstl:if test="${actor.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="message/actor/exportData.do" code="actor.exportData"/>
</jstl:if>

<jstl:if test="${actor.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
<br/>
	<acme:button url="administrator/delete.do" code="actor.delete"/>
</jstl:if>

</body>
</html>