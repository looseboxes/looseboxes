<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>

<jsp:useBean id="InsertShippingdetails" class="com.looseboxes.web.components.forms.InsertShippingdetailsForm" scope="session"/>

<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Confirm Entries Page - ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">confirm shipping details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">confirm shipping details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    <c:choose>
      <c:when test="${mobile}">Confirm Details</c:when>    
      <c:otherwise>Confirm&nbsp;Shipping&nbsp;Details</c:otherwise>
    </c:choose> 
  </jsp:attribute>  
  <jsp:body> 
      
    <strong>Shipping Details</strong><br/> 
    
    <form class="form0" name="confirmShippingForm" id="confirmShippingFormId"  
    method="post" action="${contextURL}/ioafc">
        
      <bwf:confirmationform dbActionBean="${InsertShippingdetails}"/>
      
      <c:forEach var="payment" items="${User.paymentoptions}">
        <c:forEach var="pair" items="${payment}">
          <c:if test="${pair.key == 'paymentoptionId' && pair.value == User.shoppingCart.selectedPaymentoptionId}">
            <c:set var="selectedPayment" value="${payment}"/>    
          </c:if>      
        </c:forEach>    
      </c:forEach>

      <c:if test="${selectedPayment != null}">
        
        <br/>
        
        <strong>Payment Details</strong><br/> 
    
        <jsp:useBean id="selectedPayment" class="com.looseboxes.pu.entities.Userpaymentmethod" scope="page"/>
        
        <table style="text-align:left">
          <thead>
            <th colspan="2">Payment Details</th>
          </thead>  
          <tbody>
            <tr><td>Payment Method</td><td>${selectedPayment.paymentmethodid.paymentmethod}</td></tr>  
            <tr><td>User Name</td><td>${selectedPayment.paymentMethodUsername}</td></tr>  
            <tr><td>Expiry Date</td><td>${selectedPayment.expiryDate}</td></tr>  
            <tr><td>Billing Address</td><td>${selectedPayment.billingAddress}</td></tr>  
          </tbody>
        </table>
        
      </c:if>
      
    </form>
      
  </jsp:body> 
</loose:page1>
