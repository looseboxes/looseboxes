<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Legal Pages Index</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Legal Pages Index. (List of legal pages)</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">legal pages index, list of legal pages</jsp:attribute> 
  <jsp:body>
    <%@include file="/legal/resources/index.xml"%>  
  </jsp:body>
</loose:page>     
