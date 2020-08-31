<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">About ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">About ${siteName}. Who we are. Vision. Mission Statement</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">about ${siteName}. Who we are. Vision. Mission Statement</jsp:attribute> 
  <jsp:body>
    <br/>  
    <loose:menucontent contentClass="contentBox2" contentId="aboutLooseBoxes" display="true" 
    titleClass="header1Layout curvedTop" title="About ${siteName}" titleId="aboutLooseBoxesHeader">
        <%@include file="/info/resources/who_we_are.xml"%>
    </loose:menucontent>  
  </jsp:body>
</loose:page>     
