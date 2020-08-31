<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Contact NUROX Ltd</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Contact NUROX Ltd</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">contact details phone number email address local address postal address</jsp:attribute> 
  <jsp:body>
    <br/>  
    <loose:menucontent contentClass="contentBox2" contentId="aboutLooseBoxes" display="true" 
    titleClass="header1Layout curvedTop" title="Contact NUROX Ltd" titleId="aboutLooseBoxesHeader">
      <div class="spaced1">
        <%@include file="/info/resources/contact_nuroxltd.xml"%>  
      </div>    
    </loose:menucontent>  
  </jsp:body>
</loose:page>     

    