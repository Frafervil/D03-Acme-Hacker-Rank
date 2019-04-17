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
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>
		

	<form:form action="hacker/register.do" modelAttribute="hackerForm">
		
		<form:hidden path="idHacker" />
		
		<fieldset>
    	<legend><spring:message code="hacker.fieldset.personalInformation"/></legend>
		<acme:textbox code="hacker.name" path="name" placeholder="Homer"/>
		<acme:textbox code="hacker.surname" path="surname" placeholder="Simpson"/>
		<acme:textbox code="hacker.photo" path="photo" placeholder="https://www.jazzguitar.be/images/bio/homer-simpson.jpg"/>
		<acme:textbox code="hacker.email" path="email" placeholder="homerjsimpson@email.com"/>
		<acme:textbox code="hacker.phone" path="phone" placeholder="+34 600 1234"/>
		<acme:textbox code="hacker.address" path="address" placeholder="123 Main St Anytown, Australia"/>
		<acme:textbox code="hacker.vatNumber" path="vatNumber" placeholder="4"/>
		
		</fieldset>
		
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
    	<legend><spring:message code="hacker.fieldset.userAccount"/></legend>
		<acme:textbox code="hacker.username" path="username" placeholder="HomerS"/>
		
		<acme:password code="hacker.password" path="password"/>
		<acme:password code="hacker.passwordChecker" path="passwordChecker"/>
		
		</fieldset>
		<br/>
		
		<acme:checkbox code="hacker.confirmTerms" path="checkBox"/>
		
		<jstl:if test="${cookie['language'].getValue()=='en'}">
			<a href="terms/englishTerms.do"><spring:message code="hacker.terms"/></a>
			<br/>
			<br/>	
		</jstl:if>
		<jstl:if test="${cookie['language'].getValue()=='es'}">
			<a href="terms/terms.do"><spring:message code="hacker.terms"/></a>
		</jstl:if>
		
		<input type="submit" name="register" id="register"
		value="<spring:message code="hacker.save" />" >&nbsp; 
		
		<acme:cancel url="welcome/index.do" code="hacker.cancel"/>
	</form:form>
	
	
	<script type="text/javascript">
	$("#register").on("click",function(){validatePhone("<spring:message code='hacker.confirmationPhone'/>","${countryCode}");}); 
</script>