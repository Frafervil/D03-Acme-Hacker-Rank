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

		<b><spring:message code="problem.title" /></b>:
		<jstl:out value="${problem.title}"/><br/>
	
		<b><spring:message code="problem.statement" /></b>:
		<jstl:out value="${problem.statement }"/><br/>	
	
		<b><spring:message code="problem.hint" /></b>:
		<jstl:out value="${problem.hint }"/><br/>
	
		<b><spring:message code="problem.attachments" /></b>:
		<br/>
		<ul>
		<jstl:forEach items="${problem.attachments}" var="attachment" >
			<jstl:if test="${attachment != null}">
	        	<li><jstl:out value="${attachment}"/></li>
	        </jstl:if>
		</jstl:forEach>
		</ul>
		<br/>
		
		<b><spring:message code="problem.isDraft" /></b>:
		<jstl:out value="${problem.isDraft }"/><br/>
				
		<!-- Company -->
		<b><spring:message code="problem.company" /></b>:
		<a href="company/display.do?companyId=${problem.company.id}">
			<jstl:out value="${problem.company.commercialName}"/>
		</a><br/>

		
<jstl:if test="${problem.company.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
	<jstl:if test="${problem.isDraft == true}">
		<a href="problem/company/edit.do?problemId=${problem.id}"><spring:message code="problem.edit"/></a><br/>
	</jstl:if>
<br/>
	<a href="problem/company/delete.do?problemId=${problem.id}"><spring:message code="problem.delete"/></a><br/>
</jstl:if>

	