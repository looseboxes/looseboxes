<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<jsp:useBean id="User" class="com.looseboxes.web.components.UserBean" scope="session"/>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} Add Shipping  to New Payment Method</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Add shipping to new payment method</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">Form,add shipping to payment method</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">${siteName}&nbsp;- Add Shipping  to New Payment Method</jsp:attribute>  
  <jsp:body>
    <c:choose>
      <c:when test="${User.shoppingCart == null || empty User.shoppingCart.items}">
        <div class="handWriting">
        There are currently no items in your shopping cart.
        <br/><br/>
        Use the <b>Add to Cart</b> or <b>Buy Now</b> buttons to add items to your Shopping Cart.
        </div>
        <a href="${contextURL}/products/productdetails.jsp">&emsp;Back</a>
      </c:when>  
      <c:otherwise>
<%-- 
A DbAction which will be accessed via its Id, should have a unique name and thus
would be unique across a session. Hence we give this a session scope.
--%>
        <jsp:useBean id="InsertShippingdetails" class="com.looseboxes.web.components.forms.InsertShippingdetailsForm" scope="session">
          <jsp:setProperty name="InsertShippingdetails" property="stage" value="1"/>
          <jsp:setProperty name="InsertShippingdetails" property="request" value="<%=request%>"/>    
        </jsp:useBean>
        
        <jsp:useBean id="InsertUserpaymentmethod" class="com.looseboxes.web.components.forms.InsertUserpaymentmethodForm" scope="session"/>  

        <bwf:formStages disableOtherStages="true" skipStage="2"  
        formStagesForm="${InsertShippingdetails}" formStage="${InsertShippingdetails.stage}"/>  
        
        <form method="post" class="form0 background0 fullWidth" name="${InsertShippingdetails.tableName}form" id="${InsertShippingdetails.tableName}formId"
        action="${contextURL}${InsertShippingdetails.stage}" onsubmit="return myFormHandler.validate()">

          <bwf:form formBean="${InsertShippingdetails}"/>  
          
          <%-- @related express shipping --%>
          <p>Express Shipping: <input type="checkbox" name="expressShipping" value="true"/></p>
          
          <bwf:formverification hideAgreeToTerms="true" hideCaptcha="true"/>  
      
          <div style="margin-top:0.5em">
            <bwf:defaultformNav skipStage="${skipStage}" dbAction="${InsertShippingdetails}" 
                             formStage="${InsertShippingdetails.stage}" submitValue="Next"/>
          </div>    
        </form>    
        
      </c:otherwise>
    </c:choose>  
  </jsp:body>
</loose:page1>     
