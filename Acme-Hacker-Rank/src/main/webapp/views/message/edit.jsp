<%--
 * edit.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%-- Formulario para la creación de mensajes nuevos, (escritura de un mensaje a un actor) --%>
<jstl:if test="${mensaje.id==0}">
<form:form action="${requestURI}" modelAttribute="mensaje">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="sender" />
	
	
	<jstl:if test="${broadcast}">
	<form:hidden path="recipients" />
	
	</jstl:if>
	
	<form:label path="subject">
		<spring:message code="message.subject" />:
	</form:label>
	<form:input path="subject" />
	<form:errors cssClass="error" path="subject" />
	<br>
	<br>
	
	<form:label path="body">
		<spring:message code="message.body" />:
	</form:label>
	<form:textarea path="body" />
	<form:errors cssClass="error" path="body" />
	<br>
	<br>
	
	<jstl:if test="${mensaje.id==0}">
	<input type="submit" name="save"
		value="<spring:message code="message.send" />" />&nbsp; 
	</jstl:if>
	
	<input type="button" name="cancel"
		value="<spring:message code="message.cancel" />"
		onclick="javascript: relativeRedir('/message/actor/list.do');" />
	<br />

</form:form>
</jstl:if>

<jstl:if test="${!permission }">
<h3><spring:message code="message.nopermission" /></h3>
</jstl:if>