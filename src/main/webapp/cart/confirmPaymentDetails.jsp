<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>

<jsp:useBean id="InsertUserpaymentmethod" class="com.looseboxes.web.components.forms.InsertUserpaymentmethodForm" scope="session"/>

<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Confirm Entries Page - ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">confirm payment details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">confirm payment details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    <c:choose>
      <c:when test="${mobile}">Confirm Details</c:when>    
      <c:otherwise>Confirm Payment and Shipping Details</c:otherwise>
    </c:choose> 
  </jsp:attribute>  
  <jsp:body> 

    <form class="form0" name="paymentAndShippingform" id="paymentAndShippingFormId"  
    method="post" action="${contextURL}${InsertUserpaymentmethod.action}">
        
      <strong>Payment Details</strong><br/> 
        
      <bwf:confirmationform dbActionBean="${InsertUserpaymentmethod}"/>  
      
      <c:if test="${InsertShippingdetails != null}">
          
        <br/>
        <strong>Shipping Details</strong><br/> 
        
        <table style="text-align:left">
          <c:forEach items="${InsertShippingdetails.details}" var="pair">
            <tr><td class="tableDT">${pair.key}</td><td class="tableDD">${pair.value}</td></tr>
          </c:forEach>
        </table>
        
      </c:if>
      
    </form>
      
  </jsp:body> 
</loose:page1>
