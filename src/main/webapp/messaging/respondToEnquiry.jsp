<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="Rte" value="${ResponseToProductEnquiryEmail}" scope="page"/> 
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Respond to Enquiry</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Respond to equiry</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">respond to equiry by ${Rte.fromUsername}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Respond to enquiry by ${Rte.fromUsername}</jsp:attribute>  
  <jsp:body> 
    <div class="background0 width4">
      <loose:contactuser messageType="res" 
      targetProductId="${Rte.targetProductId}" chatWithRecipient="false"                  
      fromEmail="${Rte.fromEmail}" fromUsername="${Rte.fromUsername}"
      toEmail="${Rte.toEmail}" toUsername="${Rte.toUsername}"/>
    </div>    
  </jsp:body> 
</loose:page1>
