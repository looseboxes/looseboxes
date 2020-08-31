<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - User Pages Index</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">User Pages Index. (List of user pages)</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">user pages index, list of user pages</jsp:attribute> 
  <jsp:body>
    <%@include file="/user/resources/index.xml"%>  
  </jsp:body>
</loose:page>   
