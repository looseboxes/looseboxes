<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Promos at ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Promos</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">promos</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">loose yourself!</jsp:attribute>  
  <jsp:body> 
    <div class="handWriting">
      <i>Hi</i>,
      <br/>
      <p>There are no promos currently in progress</p>
    </div>
    <br/>
    Promos are subject to the following <a href="${contextURL}/legal/promos_terms.jsp">terms and conditions</a>
    <br/><br/><br/>
  </jsp:body>
</loose:page1>     
