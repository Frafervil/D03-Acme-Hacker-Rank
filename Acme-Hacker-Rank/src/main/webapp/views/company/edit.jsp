

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


<form:form action="company/edit.do" modelAttribute="company">
	<form:hidden path="id"/>
	<form:hidden path="version" />
	
	<acme:textbox code="company.name" path="name"/>
	
	<acme:textbox code="company.surname" path="surname"/>
	
	<acme:textbox code="company.email" path="email"/>
	
	<acme:textbox code="company.phone" path="phone"/>
	
	<acme:textbox code="company.address" path="address"/>
	
	<acme:textbox code="company.photo" path="photo"/>
	
	<acme:textbox code="company.vatNumber" path="vatNumber"/>
	<br />
	<br />
	
	<h3><spring:message code="company.company" /></h3>
	<acme:textbox code="company.commercialName" path="commercialName"/>
	<br />
	<br />
	
<jstl:if test="${company.userAccount.username == pageContext.request.userPrincipal.name}">
	<input type="submit" name="save" id="save"
		value="<spring:message code="company.save" />" />
</jstl:if>
	<input type="button" name="cancel"
		value="<spring:message code="company.cancel" />"
		onclick="javascript: relativeRedir('${redirectURI}');" />
	<br />

</form:form>

<jstl:if test="${company.userAccount.username == pageContext.request.userPrincipal.name}">
<script type="text/javascript">
$("#save").on("click",function(){validatePhone("<spring:message code='company.confirmationPhone'/>","${countryCode}");});
</script>
</jstl:if>