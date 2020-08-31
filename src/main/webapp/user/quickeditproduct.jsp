<%@page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Change Product Category</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">change product category</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">change product category</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Change Product Category</jsp:attribute>  
  <jsp:body> 
      
    <loose:quickeditproduct formAction="${contextURL}/user/quickeditproduct.jsp"/>

  </jsp:body>
</loose:page1>     
