<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Join ${siteName} ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Join Form</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">join,register,membership</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Join ${siteName}</jsp:attribute>  
  <jsp:body>

    <loose:joinform 
        displayJoinBySocial="false"
        formClass="form0 width1 background0" 
        formInputClass="noclass"/>
    
  </jsp:body>
</loose:page1>     
