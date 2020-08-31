<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%--@todo jsp:useBean id="AdminMonitor" class="com.loosebox.components.AdminMonitor" scope="application"/--%>
    
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Master Access</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Get access to hundreds of thousands of users</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">master access</jsp:attribute> 
  <jsp:body> 
      
    <p>
      <b><%--@todo tt>${AdminMonitor.usersCount}</tt--%> Thousands of users</b> are waiting to find out
      about your product(s) / service(s).<br/>
      Please provide an email address and you will be notified of interests. 
      <c:if test="${!mobile}"><br/></c:if>
    </p>  
    <loose:joinform displayPassword="false" 
                    displayConfirmPassword="false" 
                    displayLearnMore="false"
                    displayJoinBySocial="false"
                    submitButtonValue="Notify Me"
                    formClass="form0 width1 background0 myBorder3" 
                    formInputClass="noclass"/>
    
    <ul style="padding-left:0.5em" class="myFontSize0">
      <li>Your email will never be displayed or revealed.</li>    
      <li>${siteName} shall forward enquiries concerning your product(s)/service(s) 
          to you. <i>(with the address of the enquirer indicated)</i>.
      </li>    
      <li>If you wish to be contacted directly you may contact the enquirer.</li>
    </ul>
          
    <h3>If you already have an account - Login Here</h3>      
    <loose:loginform displayForgotPasswordLink="true"
                     displayLoginBySocial="false"
                     displayRemembermeCheckbox="true"
                     formClass="form0 width1 background0 spaced1"
                     formInputClass="noclass"/>

  </jsp:body>
    
</loose:page1>     
