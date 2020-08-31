<%@tag trimDirectiveWhitespaces="true" description="prompt for users email, auto generate password for user" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="displayPassword" required="false"  description="default value is 'true'"%> 
<%@attribute name="displayConfirmPassword" required="false"  description="default value is 'true'"%> 
<%@attribute name="displayLearnMore" required="false" 
description="Default value is 'true'. Determines if the 'learn more' link will be displayed. The link will not be displayed in  mobile browsing mode irrespective of the value of this attribute"%> 
<%@attribute name="displayJoinBySocial" required="false"  description="default value is 'true'"%> 
<%@attribute name="submitButtonValue" required="false"%>
<%@attribute name="formClass" required="true"%> 
<%@attribute name="formInputClass" required="true"%> 
<%@attribute name="formAction" required="false" description="The action of the form. default is /join"%>

<%-- Default Values --%>
<c:if test="${formAction == null || formAction == ''}">
  <c:set var="formAction" value="${contextURL}/join"/>
</c:if>
<c:if test="${displayPassword == null || displayPassword == ''}">
  <c:set var="displayPassword" value="true"/>    
</c:if>
<c:if test="${displayConfirmPassword == null || displayConfirmPassword == ''}">
  <c:set var="displayConfirmPassword" value="true"/>      
</c:if>
<c:if test="${displayLearnMore == null || displayLearnMore == ''}">
  <c:set var="displayLearnMore" value="true"/>      
</c:if>
<c:if test="${displayJoinBySocial == null || displayJoinBySocial == ''}">
  <c:set var="displayJoinBySocial" value="true"/>      
</c:if>
<c:if test="${submitButtonValue == null || submitButtonValue == ''}">
  <c:set var="submitButtonValue" value="Join"/>
</c:if>

<form name="joinForm" id="joinFormId" method="post" action="${formAction}" class="${formClass}">

  <table class="${formClass}">
    <tr>
      <td> Email Address: </td>
      <td>
        <%-- @related_48 userType=newUser --%>
        <input class="${formInputClass}"
        type="text" name="emailAddress" id="joinFormEmailAddress" maxlength="50"
        onchange="myFormHandler.validateInput('joinFormEmailAddress', 'joinFormEmailAddressAjaxMessage', 
        '${contextURL}/ajax?type=validate&amp;tableName=siteuser&amp;userType=newUser&amp;', false)"/>
      </td>
    </tr>
    <tr><td colspan="2" id="joinFormEmailAddressAjaxMessage" class="mySmaller"></td></tr>
    <tr>
      <td> Username: </td>
      <td>
        <%-- @related_48 userType=newUser --%>
        <input class="${formInputClass}"
        type="text" name="username" id="joinFormUsername" maxlength="50"
        onchange="myFormHandler.validateInput('joinFormUsername', 'joinFormUsernameAjaxMessage', 
        '${contextURL}/ajax?type=validate&amp;tableName=siteuser&amp;userType=newUser&amp;', false)"/>
      </td>
    </tr>
    <tr><td colspan="2" id="joinFormUsernameAjaxMessage" class="mySmaller"></td></tr>
    <c:if test="${displayPassword == 'true'}">
      <tr>
        <td>Password: </td>
        <td><input class="${formInputClass}" type="password" name="password" id="joinFormPassword" maxlength="50"/>
        </td>
      </tr>
      <tr><td colspan="2" id="joinFormPasswordAjaxMessage" class="mySmaller"></td></tr>
    </c:if>
    <c:if test="${displayConfirmPassword == 'true'}">
      <tr>
        <td>Confirm Password: </td>
        <td><input class="${formInputClass}"
          type="password" name="confirmPassword" id="joinFormConfirmPassword"
          onchange="myFormHandler.matchValues('joinFormPassword', 'joinFormConfirmPassword', 'joinFormPasswordAjaxMessage', 'joinFormConfirmPasswordAjaxMessage')"
          maxlength="50" />
        </td>
      </tr>
      <tr><td colspan="2" id="joinFormConfirmPasswordAjaxMessage" class="mySmaller"></td></tr>
    </c:if>    
    <tr>
      <td></td>
      <td>
        <span style="text-align:left; width:70%"></span>  
        <span style="text-align:right; width:30%">
          <%--@related_25 the submit button ID 'submitId' is used by myFormHandler Scripts --%>  
          <input class="myBtnLayout" type="submit" id="submitId" value="${submitButtonValue}"/>  
        </span>  
      </td>
    </tr>
  </table>
</form>
<c:if test="${displayLearnMore == 'true' && !mobile}">
  <br/><loose:learnmore nodeClass="${formClass}" useDropDownMenu="true"/>          
</c:if>          
<c:if test="${displayJoinBySocial == 'true'}">
  <div class="spaced">
    <div class="dropDownMenu"><div id="progressBar">... ... ... please wait</div></div>
    <br/>
    <loose:oauthFormElement formType="Join" provider="facebook" 
    providerIconClass="facebookImg borderless" providerIconSource="${contextURL}/images/transparent.gif"/>
    
    <c:choose>
      <c:when test="${mobile}"><br/></c:when>
      <c:otherwise>&emsp;</c:otherwise>
    </c:choose>
    
    <loose:oauthFormElement formType="Join" provider="google" 
    providerIconClass="googleImg borderless" providerIconSource="${contextURL}/images/transparent.gif"/>
  </div>    
</c:if>  
  
