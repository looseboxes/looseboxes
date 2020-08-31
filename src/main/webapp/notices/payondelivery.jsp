<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Pay on Delivery</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Pay on delivery, notification</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">pay on delivery, notification</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">${siteName} - Pay on Delivery Information</jsp:attribute>  
  <jsp:body> 
    <div class="handWriting">
      Hi,<br/><br/>
      <p>Your order was processed successfully.</p>
      <p>You have selected to hold payment pending delivery of the order item.</p>
      <%@include file="/WEB-INF/jspf/postpaymentnotice.jspf"%>
    </div>  
  </jsp:body>
</loose:page1>     
