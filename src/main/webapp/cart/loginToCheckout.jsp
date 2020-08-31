<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Login to Pay for Items in Cart</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Login to pay for items in shopping cart</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">login,pay for items</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    Login to Pay for Items <c:if test="${!mobile}">in Shopping Cart</c:if>  
  </jsp:attribute>  
  <jsp:body>
      
    <div class="myLarger"><b>New User</b></div>

    <loose:joinform displayConfirmPassword="true" 
                    displayJoinBySocial="false" 
                    displayLearnMore="false" 
                    formAction="${contextURL}/joinToCheckout" 
                    formClass="form0 width1 background0" 
                    formInputClass="noclass" 
                    submitButtonValue="Pay For Item"/>
    
    <br/>
    
    <div class="myLarger"><b>Existing User</b></div>
    <loose:loginform displayForgotPasswordLink="true" 
                     displayLoginBySocial="false" 
                     displayRemembermeCheckbox="true" 
                     formAction="${contextURL}/loginToCheckout" 
                     formClass="form0 width1 background0" 
                     formInputClass="noclass" 
                     submitButtonValue="Pay For Item" 
                     userType="existingUser"/>
    
  </jsp:body>
</loose:page1>     
