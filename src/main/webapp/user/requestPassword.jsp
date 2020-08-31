<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Request Password. ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Forgot Password, Request Password</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">forgot password, request password</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Request for Password</jsp:attribute>  
  <jsp:body> 
    <form class="form0 background0 width1" name="requestPasswordForm" method="post"
      action="${contextURL}/reqpwd">
      <div id="requestPasswordFormMessage" class="mySmaller"></div>
      <br/>
      <div>Enter your <b>email address</b><br/> 
      Your password will be mailed to you</div>
      <table class="width1">
        <tr>
          <td>
            <input class="formInput" type="text" name="emailAddress" id="emailAddressId" maxlength="50"
            onchange="myFormHandler.validateInput('emailAddressId', 'requestPasswordFormMessage', 
            '${contextURL}/ajax?type=validate&amp;tableName=siteuser&amp;', false)"/>              
          </td>
        </tr>
        <tr>
          <td>
            <input class="myBtnLayout" type="submit" id="submit" value=" Submit " />              
          </td>
        </tr>
      </table>
    </form>
  </jsp:body>
</loose:page1>     
