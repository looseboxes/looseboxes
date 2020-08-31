<%@page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} Delivery Rates</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">delivery rates</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">delivery rates</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">${siteName} Delivery Rates</jsp:attribute>  
  <jsp:body> 
      
    <%@include file="/WEB-INF/jspf/deliveryrates.jspf"%>  

  </jsp:body>
</loose:page1>     
