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

		<b><spring:message code="position.title" /></b>:
		<jstl:out value="${position.title}"/><br/>
	
		<b><spring:message code="position.description" /></b>:
		<jstl:out value="${position.description }"/><br/>	
	
		<b><spring:message code="position.deadline" /></b>:
		<jstl:out value="${position.deadline }"/><br/>
	
		<b><spring:message code="position.profileRequired" /></b>:
		<jstl:out value="${position.profileRequired }"/><br/>
		
		<b><spring:message code="position.skillsRequired" /></b>:
		<br/>
		<jstl:forEach items="${position.skillsRequired}" var="skillRequired" >
			<jstl:if test="${skillRequired != null}">
	        	<jstl:out value="${skillRequired}"/>
	        </jstl:if>
		</jstl:forEach>
		<br/>
		
		<b><spring:message code="position.technologiesRequired" /></b>:
		<br/>
		<jstl:forEach items="${position.technologiesRequired}" var="technologyRequired" >
			<jstl:if test="${technologyRequired != null}">
	        	<jstl:out value="${technologyRequired}"/>
	        </jstl:if>
		</jstl:forEach>
		<br/>
		
		<b><spring:message code="position.salaryOffered" /></b>:
		<jstl:out value="${position.salaryOffered }"/><br/>
		
		<b><spring:message code="position.ticker" /></b>:
		<jstl:out value="${position.ticker }"/><br/>
		
		<jstl:if test="${position.company.userAccount.username == pageContext.request.userPrincipal.name}">
		<b><spring:message code="position.status" /></b>:
		<jstl:out value="${position.status }"/><br/>
		</jstl:if>
		
		<!-- Company -->
		<b><spring:message code="position.company" /></b>:
		<a href="company/display.do?companyId=${position.company.id}">
			<jstl:out value="${position.company.commercialName}"/>
		</a><br/>
		
		<!-- Problems -->
		
		<b><spring:message code="position.problems" /></b>:
		<br/><ul>
		<jstl:forEach items="${position.problems}" var="problem" >
			<jstl:if test="${problem != null}">
	        	<li><jstl:out value="${problem.title}"/></li>
	        </jstl:if>
		</jstl:forEach></ul>

<security:authorize access="hasRole('COMPANY')">
<jstl:if test="${position.company.userAccount.username == pageContext.request.userPrincipal.name}">
<br/>
	<jstl:if test="${position.status == 'DRAFT'}">			
		<a href="position/company/edit.do?positionId=${position.id}"><spring:message code="position.edit"/></a><br/>
	</jstl:if>
<br/>
	<jstl:if test="${position.status == 'FINAL'}">
		<a href="position/company/cancel.do?positionId=${position.id}"><spring:message code="position.saveCancel"/></a><br/>				
	</jstl:if>
<br/>
	<a href="position/company/delete.do?positionId=${position.id}"><spring:message code="position.delete"/></a><br/>
</jstl:if>
</security:authorize>
