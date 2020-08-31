<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Why use ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Why use ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">why use ${siteName},dedicated to FREE information FREE OF CHARGE, contact product owner directly, largest online record of goods, services</jsp:attribute> 
  <jsp:body>
    <br/>  
    <loose:menucontent contentClass="contentBox2" contentId="aboutLooseBoxes" display="true" 
    titleClass="header1Layout curvedTop" title="Why ${siteName}" titleId="aboutLooseBoxesHeader">
      <div style="margin:0.5em">
        <%@include file="/info/resources/why_us.xml"%>  
      </div>    
    </loose:menucontent>  
  </jsp:body>
</loose:page>     

    