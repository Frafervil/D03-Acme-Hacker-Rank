<%--
 * edit.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
 
 <%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="position/company/edit.do" modelAttribute="position">
		<form:hidden path="id"/>
		<form:hidden path="version"/>
		
		
		<acme:textbox code="position.title" path="title" placeholder="Postion title"/>
		<br />
		<acme:textarea code="position.description" path="description"/>
		<br />		
		<acme:datebox code="position.deadline" path="deadline" placeholder="dd/MM/yyyy HH:mm" />
		<br />
		<acme:textbox code="position.profileRequired" path="profileRequired" />		<br />
		<br />
		<acme:textarea code="position.skillsRequired" path="skillsRequired"/>
		<br />
		<acme:textarea code="position.technologiesRequired" path="technologiesRequired"/>
		<br />
		<acme:textbox code="position.salaryOffered" path="salaryOffered" />
		<br />
		<form:label path="problems">
		<spring:message code="position.problems" />:
		</form:label>
		<form:select multiple="true" path="problems" >
			<form:options items="${problems}" itemValue="id" itemLabel="title" />
		</form:select>
		<form:errors cssClass="error" path="problems" />	
		<br />	
		<br />		
		<br />
		<acme:submit name="saveDraft" code="position.saveDraft"/>
		<acme:submit name="saveFinal" code="position.saveFinal"/>

		
		<acme:cancel url="welcome/index.do" code="position.cancel"/>
		
</form:form>
