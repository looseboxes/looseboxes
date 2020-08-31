<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} Comment Form</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Post a Comment</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">post comment</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Post a Comment</jsp:attribute>  
  <jsp:body>
    <jsp:useBean id="InsertProductcomment" class="com.looseboxes.web.components.forms.InsertProductcommentForm" scope="session">
      <jsp:setProperty name="InsertProductcomment" property="request" value="<%=request%>"/>  
    </jsp:useBean> 
    
    <c:if test="${!InsertProductcomment.hasImageFields}">
      <c:set var="skipStage" value="2"/>
    </c:if>  
    <form method="post" class="form0 background0 fullWidth" 
          name="${InsertProductcomment.table}Form" id="${InsertProductcomment.table}FormId"
    action="${contextURL}/postcomment" onsubmit="return myFormHandler.validate()">
      <input type="hidden" name="productId" value="${param.productId}"/>  
      <input type="hidden" name="repliedto" value="${param.repliedto}"/>  
      
      <bwf:form formBean="${InsertProductcomment}"/>  
      
      <bwf:formverification hideAgreeToTerms="true" hideCaptcha="true"/>  
      
      <div style="margin-top:0.5em">
        <bwf:defaultformNav skipStage="${skipStage}" dbAction="${InsertProductcomment}" 
                         formStage="${InsertProductcomment.stage}" submitValue="Next"/>
      </div>    
    </form>    
    <br/>
    <%-- to do --%>
    <%--loose:displaycomments tableName="${InsertProductcomment.table}" 
    idColumn="productId" idValue="${param.productId}"/--%>
  </jsp:body>
</loose:page1>     
