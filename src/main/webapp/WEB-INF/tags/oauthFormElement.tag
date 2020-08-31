<%@tag trimDirectiveWhitespaces="true" description="A form element for oauthForm.tag" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@attribute name="formType" description="Possible values are: login,join,getAccessToken (case insensitive)" required="true"%>
<%@attribute name="provider" description="Keyword for oauth provider, possible values: facebook,twitter,google,yahoo" required="true"%>
<%@attribute name="useScript" description="If this is set to true then the authURL will be loaded via a script" required="false"%>
<%@attribute name="providerIconSource" description="Source for the icon of the provider" required="true"%>
<%@attribute name="providerIconClass" description="Class for the icon of the provider, for images loaded by css"%>

<c:set var="linkURL" value="${contextURL}/oauth?provider=${provider}&amp;type=${formType}&amp;stage=requestToken&amp;noscript=1" scope="page"/>

<c:choose>
  <c:when test="${useScript == 'true'}">
    <c:set var="getValueURL" value="${contextURL}/ajax?provider=${provider}&amp;type=${formType}&amp;stage=requestToken" scope="page"/>    
    <c:set var="onclickScripts" value="myDropDownMenu.open('progressBar', false); looseboxes.loadURL(null, '${getValueURL}', '${formType}_${provider}', '${contextURL}/errorpages/default.jsp'); return false;"/>      
    <a href="${linkURL}" rel="nofollow" target="_blank" class="spaced mySmaller" onclick="${onclickScripts}">
      <img class="${providerIconClass}" title="${formType} with ${provider}" alt="*" 
           src="${providerIconSource}" width="16px" height="16px" /> 
      ${formType} with ${provider}
    </a>
  </c:when>  
  <c:otherwise>
    <a href="${linkURL}" target="_blank" class="spaced mySmaller">
      <img class="${providerIconClass}" title="${formType} with ${provider}" alt="*" 
           src="${providerIconSource}" width="16px" height="16px" /> 
      ${formType} with ${provider}
    </a>
  </c:otherwise>
</c:choose>

