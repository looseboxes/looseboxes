<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">About NUROX Ltd</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">About NUROX Ltd</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">about nurox ltd</jsp:attribute> 
  <jsp:body>
    <br/>  
    <loose:menucontent contentClass="contentBox2" contentId="aboutNuroxLtd" display="true" 
    titleClass="header1Layout curvedTop" title="About NUROX Ltd" titleId="aboutNuroxLtdHeader">
      <%@include file="/info/resources/about_nuroxltd.xml"%>  
      <br/>
      <%@include file="/info/resources/vision.xml"%>
      <br/><br/>
      <%@include file="/info/resources/mission.xml"%>
      <br/><br/><br/>  
      For further questions you may wish to <a href="${contextURL}/info/contact_nuroxltd.jsp">contact NUROX Ltd</a>
    </loose:menucontent>  
  </jsp:body>
</loose:page>     
