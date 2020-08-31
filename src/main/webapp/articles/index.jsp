<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Articles Index</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Articles Index. (List of articles)</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">articles index, list of articles</jsp:attribute> 
  <jsp:body> 
    <%@include file="/articles/resources/index.xml"%>    
  </jsp:body>
</loose:page>     
