<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Help Pages Index - ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Help Pages Index. (List of help pages)</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">help pages index, list of help pages</jsp:attribute> 
  <jsp:body>
    <%@include file="/help/resources/index.xml"%>  
  </jsp:body>
</loose:page>     
