<%@tag trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="cbTitle" required="false"%>
<%@attribute name="cbTitleId" required="false"%>
<%@attribute name="cbContentId" required="true"%>
<%@attribute name="cbToEmail" required="false" description="either 'cbToEmail', 'cbToUsername' or 'cbToUser' must be specified"%>
<%@attribute name="cbToUsername" required="false" description="either 'cbToEmail', 'cbToUsername' or 'cbToUser' must be specified"%>
<%@attribute name="cbToUser" required="false" description="either 'cbToEmail', 'cbToUsername' or 'cbToUser' must be specified"%>
<%@attribute name="cbChatTextId" required="true"%>
<%@attribute name="cbMessagesId" required="true"%>
<%@attribute name="cbMessagesContent" required="false" description="Initial contents of the messages div" fragment="true"%>

<c:if test="${cbTitle != null}">
  <div id="${cbTitleId}" class="header1Layout curvedTop">${cbTitle}</div>    
</c:if>
<div id="${cbContentId}" class="contentBox2">

  <c:url value="${contextURL}/ajax" var="cbAddUrl">
    <c:param name="type" value="chat" />
    <c:param name="action" value="add"/>
    <c:if test="${toEmail != null}">
      <c:param name="toEmail" value="${toEmail}" />
    </c:if>
    <c:if test="${toUsername != null}">
      <c:param name="toUsername" value="${toUsername}"/>    
    </c:if>
    <c:if test="${toUser != null}">
      <c:param name="toUser" value="${toUser}"/>    
    </c:if>
  </c:url>

  <c:url value="${contextURL}/ajax" var="cbRefreshUrl">
    <c:param name="type" value="chat" />
    <c:param name="action" value="refresh"/>
    <c:if test="${toEmail != null}">
      <c:param name="toEmail" value="${toEmail}" />
    </c:if>
    <c:if test="${toUsername != null}">
      <c:param name="toUsername" value="${toUsername}"/>    
    </c:if>
    <c:if test="${toUser != null}">
      <c:param name="toUser" value="${toUser}"/>    
    </c:if>
  </c:url>
    
  <textarea rows="4" class="formInput" name="chatText" id="${cbChatTextId}"
    maxlength="250" onfocus="myFormHandler.clearField('${cbChatTextId}'); return false;" 
    onkeypress="myChat.enterkeyPress('${cbChatTextId}', '${cbChatTextId}', 
    '${cbMessagesId}', '${cbAddUrl}', '${cbRefreshUrl}'); return false;"/>
  </textarea>
    

  <input type="button" value="Send" class="myBtnLayout" 
    onclick="myChat.getChat('${cbChatTextId}', '${cbMessagesId}', '${cbAddUrl}', '${cbRefreshUrl}'); 
    return false;"/>
<%--    window.location.reload(true); return false;"/> --%>
  
  <input type="button" value="End Chat" class="myBtnLayout floatRight" 
         onclick="myChat.endChat('${cbChatTextId}'); 
         myFormHandler.clearField('${cbMessagesId}');    
         myFormHandler.clearField('${cbChatTextId}'); return false;"/>

  <div id="${cbMessagesId}" class="myBorder mySmaller"></div>  
</div>
