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

<div class="content">
	<img src="${hacker.photo}" class="ui mini rounded image" >
</div>

<table class="ui celled table">
	<tbody>
		<tr>
			<td><spring:message code="hacker.name" />
			<td data-label="name"><jstl:out value="${hacker.name}" /></td>
		</tr>
		<tr>
			<td><spring:message code="hacker.surname" />
			<td data-label="surname"><jstl:out value="${hacker.surname}" /></td>
		</tr>
		
<%-- 		<spring:message code="hacker.surname" />  --%>
<%-- 		<jstl:forEach items="${hacker.surname}" var="surname"><img src='<jstl:out value="${surname}"></jstl:out>'> --%>
<!-- 		<br /> -->
<%-- 		</jstl:forEach> --%>
		
		<tr>
			<td><spring:message code="hacker.email" />
			<td data-label="email"><jstl:out value="${hacker.email}" /></td>
		</tr>
		<tr>
			<td><spring:message code="hacker.phone" />
			<td data-label="phone"><jstl:out value="${hacker.phone}" /></td>
		</tr>
		<tr>
			<td><spring:message code="hacker.address" />
			<td data-label="address"><jstl:out value="${hacker.address}" /></td>
		</tr>
		<tr>
			<td><spring:message code="hacker.vatNumber" />
			<td data-label="vatNumber"><jstl:out value="${hacker.vatNumber}" /></td>
		</tr>
	</tbody>
</table>

<jstl:if test="${hacker.userAccount.username == pageContext.request.userPrincipal.name}">
	<security:authorize access="hasRole('HACKER')">
<br/>
<br/>
<input type="button" name="save" class="ui button"
	value="<spring:message code="hacker.edit" />"
	onclick="javascript: relativeRedir('hacker/edit.do');" />
	
</security:authorize>
</jstl:if>
<br/>
<br/>
<jstl:if test="${hacker.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="message/actor/exportData.do" code="actor.exportData"/>
</jstl:if>
	
<br/>
<br/>
<jstl:if test="${hacker.userAccount.username == pageContext.request.userPrincipal.name}">
	<acme:button url="hacker/delete.do" code="actor.delete"/>
</jstl:if>