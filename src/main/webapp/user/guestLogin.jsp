<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Login page for guests</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Login page for guests</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">guest login,confirm email address</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Confirm Email Address</jsp:attribute>  
  <jsp:body> 
    <div class="handWriting">
        OOps, the email address you entered: <b><c:out value="${QuickregisterData.emailAddress}"/></b>
        is yet to be taken.
        <br/>
        <form class="form0" name="joinForm" method="post" action="${contextURL}/reggx">
          If the email address is correct and you are a new user click
          <input class="myBtnLayout" type="submit" value="here" />
        </form>
        <br/>
        If you are already registered with ${siteName} then the email
        address above is incorrect.<br/>
        Please log in with the correct email address below.
    </div>
    <br/>
    <%@include  file="/WEB-INF/jspf/loginForm.jspf"%>
  </jsp:body>
</loose:page1>     
