<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Electronic Payment Notice</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">post payment notice, electronic payment</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">post payment notice, electronic payment</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Post Payment Notice</jsp:attribute>  
  <jsp:body> 
    <div class="handWriting">
      Hi,<br/><br/>
      <p>Your payment was processed successfully.</p>
      <%@include file="/WEB-INF/jspf/postpaymentnotice.jspf"%>
    </div>  
  </jsp:body>
</loose:page1>     
