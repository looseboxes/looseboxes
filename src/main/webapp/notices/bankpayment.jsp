<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Post Bank Payment Notice</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Bank account details, skype contact details, phone details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">bank account details, skype contact details, phone details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">${siteName} - Bank Payment Information</jsp:attribute>  
  <jsp:body> 
    <div class="handWriting">
      Hi,<br/><br/>
      Please use the information provided below to complete your payment:
      <br/><br/>
      <em>Account Name:</em>&emsp;NUROX LTD
      <br/><em>Account Number:</em>&emsp;0025295097
      <br/><em>Bank Branch:</em>&emsp;Garki Abuja 
      <br/><em>Country:</em>&emsp;Nigeria
      <%@include file="/WEB-INF/jspf/postpaymentnotice.jspf"%>
    </div>  
  </jsp:body>
</loose:page1>     
