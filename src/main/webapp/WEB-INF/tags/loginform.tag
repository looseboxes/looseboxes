<%@tag trimDirectiveWhitespaces="true" description="login form" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="displayRemembermeCheckbox" required="false" description="default value is 'false'"%> 
<%@attribute name="displayForgotPasswordLink" required="false" description="default value is 'false'"%>
<%@attribute name="displayLoginBySocial" required="false" description="default value is 'true'"%>
<%@attribute name="submitButtonValue" required="false"%>
<%@attribute name="formClass" required="true"%>
<%@attribute name="formInputClass" required="true"%> 
<%@attribute name="formAction" required="false" description="The action of the form. default is /login"%>
<%-- @related_48 --%>
<%@attribute name="userType" required="false" description="Possible values are guest,newUser,existingUser. Default is existingUser"%>
<%-- @related_51 --%>
<%@attribute name="auxSubmit" required="false" description="The value of an auxillary submit button"%>

<%-- Default Values --%>
<c:if test="${formAction == null || formAction == ''}">
  <c:set var="formAction" value="${contextURL}/login"/>
</c:if>
<c:if test="${userType == null || userType == ''}">
  <c:set var="userType" value="existingUser"/>
</c:if>
<c:if test="${displayLoginBySocial == null || displayLoginBySocial == ''}">
  <c:set var="displayLoginBySocial" value="true"/>
</c:if>
<c:if test="${submitButtonValue == null || submitButtonValue == ''}">
  <c:set var="submitButtonValue" value="Login"/>
</c:if>

<form name="loginForm" id="loginFormId" method="post" action="${formAction}" class="${formClass}">
    
  <%-- @related_24 --%>
  <c:if test="${targetPage != null}">
    <c:set var="myTargetPage" value="${targetPage}"/>
  </c:if> 
  <c:if test="${param.targetPage != null}">
    <c:set var="myTargetPage" value="${param.targetPage}"/>
  </c:if> 
  <c:if test="${myTargetPage != null}">
    <input type="hidden" name="targetPage" value="${myTargetPage}" scope="request"/>
  </c:if>
  
  <table class="${formClass}">
    <tr>
      <td colspan="2">
        <c:choose>
          <c:when test="${auxSubmit != null}">
            <%-- No input validation via ajax in this case --%>  
            <input type="text" class="${formInputClass}" name="emailAddress" id="loginFormEmailAddress"
            maxlength="50" value="${User.emailAddress == null ? 'email address' : User.emailAddress}" 
            onfocus="myFormHandler.clearField('loginFormEmailAddress')"/>
            <br/>
          </c:when>
          <c:otherwise>
            <%-- @related_48 userType --%>
            <input type="text" class="${formInputClass}" name="emailAddress" id="loginFormEmailAddress"
            maxlength="50" value="${User.emailAddress == null ? 'email address' : User.emailAddress}" 
            onfocus="myFormHandler.clearField('loginFormEmailAddress')"
            onchange="myFormHandler.validateInput('loginFormEmailAddress', 'loginFormEmailAddressAjaxMessage', 
            '${contextURL}/ajax?type=validate&amp;tableName=siteuser&amp;userType=${userType}&amp;', false)"/>
            <br/>
          </c:otherwise>
        </c:choose>  
      </td>    
    </tr>    
    <tr>
      <td colspan="2" id="loginFormEmailAddressAjaxMessage" class="mySmaller"></td>    
    </tr>    
    <tr>
      <td colspan="2">
        <input type="password" class="${formInputClass}" name="password" id="loginFormPassword"
        value="password" maxlength="50" onfocus="myFormHandler.clearField('loginFormPassword')"/>
      </td>    
    </tr>    
    <tr>
      <td colspan="2" style="text-align:left">
      <c:if test="${displayRemembermeCheckbox == 'true'}">
        <input style="float:left" type="checkbox" name="remembermeCheckbox" id="remembermeCheckboxId" value="true"/> 
        <span style="float:left">&nbsp;Remember Me</span>
      </c:if>
      <%--@related_25 the submit button ID 'submitId' is used by myFormHandler Scripts --%>    
      &emsp;<input class="myBtnLayout" type="submit" id="submitId" value="${submitButtonValue}"/>  
      <c:if test="${auxSubmit != null}">
        <%-- @related_51 --%>  
        &emsp;or&emsp;<input class="myBtnLayout" type="submit"id="submitId1" value="${auxSubmit}"/>            
      </c:if>
      </td>
    </tr>    
    <c:if test="${displayForgotPasswordLink == 'true'}">
    <tr>
      <td colspan="2" style="text-align:center; width:100%">
        <a href="${contextURL}/user/requestPassword.jsp">I forgot my password</a>
      </td>
    </tr>
    </c:if>
    <c:if test="${displayLoginBySocial == 'true'}">
      <tr>
        <td colspan="2" style="text-align:center; width:100%">
        <div class="spaced">
          <div class="dropDownMenu"><div id="progressBar">... ... ... please wait</div></div>
          <br/>
          <loose:oauthFormElement formType="Login" provider="facebook" useScript="false"  
          providerIconClass="facebookImg borderless" providerIconSource="${contextURL}/images/transparent.gif"/>
          
          <c:choose>
            <c:when test="${mobile}"><br/></c:when>
            <c:otherwise>&emsp;</c:otherwise>
          </c:choose>
          
          <loose:oauthFormElement formType="Login" provider="google" useScript="false" 
          providerIconClass="googleImg borderless" providerIconSource="${contextURL}/images/transparent.gif"/>
        </div>
        </td>
      </tr>
    </c:if>    
  </table>
</form>
      
