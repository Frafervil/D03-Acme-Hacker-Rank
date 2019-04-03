

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags" %>


<form:form action="hacker/edit.do" modelAttribute="hacker">
	<form:hidden path="id"/>
	<form:hidden path="version" />
	
	<acme:textbox code="hacker.name" path="name"/>
	
	<acme:textbox code="hacker.surname" path="surname"/>
	
	<acme:textbox code="hacker.email" path="email"/>
	
	<spring:message code = "hacker.phone.placeholder" var="phonePlaceholder"/>
	<acme:textarea code="hacker.phone" path="phone" placeholder="${phonePlaceholder }"/>
	
	<acme:textbox code="hacker.address" path="address"/>
	
	<acme:textbox code="hacker.photo" path="photo"/>
	
	<acme:textbox code="hacker.vatNumber" path="vatNumber"/>
	<br />
	<br />
	
<jstl:if test="${hacker.userAccount.username == pageContext.request.userPrincipal.name}">
	<input type="submit" name="save" id="save"
		value="<spring:message code="hacker.save" />" />
</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="hacker.cancel" />"
		onclick="javascript: relativeRedir('${redirectURI}');" />
	<br />

</form:form>

<jstl:if test="${hacker.userAccount.username == pageContext.request.userPrincipal.name}">
<script type="text/javascript">
$("#save").on("click",function(){validatePhone("<spring:message code='hacker.confirmationPhone'/>","${countryCode}");});
</script>
</jstl:if>