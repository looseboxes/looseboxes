<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<jsp:useBean id="InsertUserpaymentmethod" class="com.looseboxes.web.components.forms.InsertUserpaymentmethodForm" scope="session"/>
<jsp:setProperty name="InsertUserpaymentmethod" property="stage" value="1"/>

<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Add Payment to Order</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Add Payment to Order</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">add payment, check out</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    <c:choose>
      <c:when test="${mobile}">Add Payment Form</c:when>    
      <c:otherwise>
        ${siteName}&nbsp;Add Payment Form - Basic Details
      </c:otherwise>
    </c:choose>  
  </jsp:attribute>  
  <jsp:attribute name="pageAfterBodyInclude">
    <script type="text/javascript" src="${contextURL}/resources/datetimeselector.js"></script>
    <c:if test="${!InsertUserpaymentmethod.hideCaptcha}">
      <script type="text/javascript">
        looseboxes.loadImage("${contextURL}/captcha", 250, 75);  
      </script>
    </c:if>
  </jsp:attribute>      
  <jsp:body>

    <c:if test="${!InsertUserpaymentmethod.hasImageFields}">
      <c:set var="skipStage" value="2"/>
    </c:if>  
      
    <bwf:formStages disableOtherStages="true" skipStage="${skipStage}" 
    formStagesForm="${InsertUserpaymentmethod}" formStage="${InsertUserpaymentmethod.stage}"/>  
    
    <form method="post" class="form0 background0 fullWidth" 
    name="${InsertUserpaymentmethod.table}form" id="${InsertUserpaymentmethod.table}formId"
    action="${contextURL}${InsertUserpaymentmethod.action}" onsubmit="return myFormHandler.validate()">
        
      <bwf:form formBean="${InsertUserpaymentmethod}"/>  
      
      <br/><input type="checkbox" name="otherDeliveryAddress" value="true"/>
      <span class="mySmaller"><b>I want to use a different delivery address</b></span>
      <br/><br/>
      
      <bwf:formverification hideAgreeToTerms="false" hideCaptcha="${InsertUserpaymentmethod.hideCaptcha}"/>  
      
      <div style="margin-top:0.5em">
        <bwf:defaultformNav skipStage="${skipStage}" dbAction="${InsertUserpaymentmethod}" 
                 formStage="${InsertUserpaymentmethod.stage}" submitValue="Next"/>
      </div>    
      
    </form>    
    
  </jsp:body>
  
</loose:page1>  
