<%@page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Add Shipping to Order</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">add shipping to order</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">cart,shipping details, order</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">${siteName} - Add Shipping to Order</jsp:attribute>  
  <jsp:body> 
      
    <form class="form0 background0" method="post" action="${contextURL}/addshipping">
      <%@include file="/WEB-INF/jspf/insertshippingforcart.jspf"%>  
      <%-- @related express shipping --%>
      <p>Express Shipping: <input type="checkbox" name="expressShipping" value="true"/></p>
      <input type="submit" class="myBtnLayout" value="Add Shipping Details"/>
    </form>

  </jsp:body>
</loose:page1>     
