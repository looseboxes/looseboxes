<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Welcome to ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Welcome Page</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">welcome page</jsp:attribute> 
  <jsp:body>
    <%@include file="/resources/welcome.xml"%>    
  </jsp:body>
</loose:page>     
