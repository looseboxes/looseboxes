<%@page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Sign in to ${siteName} ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Login Form</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">login,enter,signin</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Sign in to ${siteName}</jsp:attribute>  
  <jsp:body> 
      
    <%@include file="/WEB-INF/jspf/loginForm.jspf"%>  

  </jsp:body>
</loose:page1>     
