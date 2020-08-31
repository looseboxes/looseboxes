<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Information Pages Index - ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Information Pages Index. (List of information pages)</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">information pages index list of information pages</jsp:attribute> 
  <jsp:body>
    <%@include file="/info/resources/index.xml"%>  
  </jsp:body>
</loose:page>     
