<%--
 * register.jsp
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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${formURI}" modelAttribute="administratorForm">
		
		<form:hidden path="idAdministrator" />
		
		<fieldset>
    	<legend><spring:message code="administrator.fieldset.personalInformation"/></legend>
		<acme:textbox code="administrator.name" path="name" placeholder="Homer"/>
		<acme:textbox code="administrator.surname" path="surname" placeholder="Simpson"/>
		<acme:textbox code="administrator.photo" path="photo" placeholder="https://www.jazzguitar.be/images/bio/homer-simpson.jpg"/>
		<acme:textbox code="administrator.phone" path="phone" placeholder="+34 600 1234"/>
		<acme:textbox code="administrator.address" path="address" placeholder="123 Main St Anytown, Australia"/>
		<acme:textbox code="administrator.email" path="email" placeholder="homerjsimpson@"/>
		<acme:textbox code="administrator.vatNumber" path="vatNumber" placeholder="2.0"/>
		</fieldset>
		<br/>
		
		<fieldset>
    	<legend><spring:message code="creditCard"/></legend>
		<acme:textbox code="creditCard.holderName" path="holderName" placeholder="Homer Simpson"/>
		<acme:textbox code="creditCard.brandName" path="brandName" placeholder="Mastercard"/>
		<acme:textbox code="creditCard.number" path="number"/>
		<acme:textbox code="creditCard.expirationMonth" path="expirationMonth"/>
		<acme:textbox code="creditCard.expirationYear" path="expirationYear"/>
		<acme:textbox code="creditCard.CVV" path="CVV" placeholder="123"/>
		
		</fieldset>
		
		<fieldset>
    	<legend><spring:message code="administrator.fieldset.userAccount"/></legend>
		<acme:textbox code="administrator.username" path="username" placeholder="HomerS"/>
		
		<acme:password code="administrator.password" path="password"/>
		<acme:password code="administrator.passwordChecker" path="passwordChecker"/>
		
		</fieldset>
		<br/>
		
		<acme:checkbox code="administrator.confirmTerms" path="checkBox"/>
		
			<jstl:if test="${cookie['language'].getValue()=='en'}">
			<a href="terms/englishTerms.do"><spring:message code="administrator.terms"/></a>
			<br/>
			<br/>	
		</jstl:if>
		<jstl:if test="${cookie['language'].getValue()=='es'}">
			<a href="terms/terms.do"><spring:message code="administrator.terms"/></a>
		</jstl:if>	
				
		<input type="submit" name="register" id="register"
		value="<spring:message code="administrator.save" />" >&nbsp;

		<acme:cancel url="welcome/index.do" code="administrator.cancel"/>
	</form:form>
	
	<script type="text/javascript">
	$("#register").on("click",function(){validatePhone("<spring:message code='admin.confirmationPhone'/>","${countryCode}");}); 
</script>
