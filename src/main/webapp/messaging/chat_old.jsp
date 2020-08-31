<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${!User.loggedIn}">
  <c:redirect url="${contextURL}/user/login.jsp"/>      
</c:if>

<loose:page2>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Live Chat</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Live Chat</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">live chat</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeadInclude">
<%-- Refresh the page every X seconds, needed for chat --%>      
<%--
  <meta http-equiv="Cache-Control" content="no-cache"/>
  <meta http-equiv="refresh" content="15"/>      
--%>
  </jsp:attribute>
  <jsp:body> 
   
    <input class="myBtnLayout" type="button" value="Refresh"
    onclick="window.location='${contextURL}/messaging/chat.jsp'"/>
    <c:choose>
      <c:when test="${mobile}"><br/></c:when>
      <c:otherwise><span class="mySmaller">Status: </span></c:otherwise>
    </c:choose>
    <c:choose>
      <c:when test="${User.loggedInToChat}">
        Online&nbsp;
        <input class="myBtnLayout mySmaller" type="button" value="Hide Me"
        onclick="window.location='${contextURL}/chat?ax=logout'"/>
      </c:when>
      <c:otherwise>
        Offline&nbsp;
        <input class="myBtnLayout mySmaller" type="button" value="Let others see me"
        onclick="window.location='${contextURL}/chat?ax=login'"/>
      </c:otherwise>
    </c:choose>
    <br/><br/>
      
    <div id="newChatMenuHeader" class="header1Layout curvedTop">Start a new Chat</div>
    <div id="newChatMenuId" class="contentBox2">
      <form name="newChatForm" id="newChatFormId" method="post" 
            action="${contextURL}/chat?styp=new">
          
        <label>Enter email/username of the user to chat with</label>
        <br/>
<%-- @related_toUser 'toUser' field indicates either 'toEmail' or 'toUsername' --%>        
        <input class="formInput" type="text" name="toUser"  
               title="type the email of the user you want to chat with here"/>
        
        <textarea rows="4" class="formInput" name="chatText" id="newChatTextId"
        maxlength="250" onfocus="myFormHandler.clearField('newChatTextId'); return false;" 
        onkeypress="myChat.enterkeyPress('newChatTextId', 'newChatFormId', 
        'newChatAjaxResults', '${contextURL}/ajax?type=chat&amp;styp=new&amp;ax=add&amp;', 
        '${contextURL}/ajax?type=chat&amp;styp=new&amp;ax=refresh&amp;'); return false;"/>
        </textarea>
        
        <input class="myBtnLayout" type="submit" value="Send"
        onclick="myChat.getChat('newChatFormId', 
        'newChatAjaxResults', '${contextURL}/ajax?type=chat&amp;styp=new&amp;ax=add&amp;', 
        '${contextURL}/ajax?type=chat&amp;styp=new&amp;ax=refresh&amp;'); return false;"/>
        
        <input type="button" value="End Chat" class="myBtnLayout floatRight" 
               onclick="myChat.endChat('newChatFormId'); 
               myFormHandler.clearField('newChatAjaxResults');    
               myFormHandler.clearField('newChatTextId')"/>
        
      </form>      
      <br/>
      <div id="newChatAjaxResults" class="myBorder boxShadow"></div>  
    </div>
      
    <c:set var="userChats" value="${User.chats}"/>  

<%-- Map<String, List<Entity>> --%>    
    <c:forEach varStatus="vs" var="pair" items="${userChats}">

      <c:set var="toEmail" value="${pair.key}"/>
      
      <br/>
      
      <div id="userChatMenuHeader${vs.index}" class="header1Layout curvedTop">Chat with ${toEmail}</div>
      <div id="userChatMenuId${vs.index}" class="contentBox2">
        <form name="userChatForm${vs.index}" id="userChatFormId${vs.index}" 
        method="post" action="${contextURL}/chat">
            
          <input type="hidden" name="toEmail" value="${toEmail}"/>
          
          <textarea rows="4" class="formInput" name="chatText" id="chatTextId${vs.index}"
          maxlength="250" onfocus="myFormHandler.clearField('chatTextId${vs.index}'); return false;"/>
          </textarea>
          
          <input class="myBtnLayout" type="submit" value="Send"
          onclick="myChat.getChat('userChatFormId${vs.index}', 
          'userChatAjaxResults${vs.index}', '${contextURL}/ajax?type=chat&amp;styp=new&amp;ax=add&amp;', 
          '${contextURL}/ajax?type=chat&amp;styp=new&amp;ax=refresh&amp;'); return false;"/>
          
          <input type="button" value="End Chat" class="myBtnLayout floatRight" 
                 onclick="myChat.endChat('userChatFormId${vs.index}'); 
                 myFormHandler.clearField('userChatAjaxResults${vs.index}');    
                 myFormHandler.clearField('userChatForm${vs.index}')"/>
          
        </form>  
        <br/>
        <div id="userChatAjaxResults${vs.index}" class="myBorder boxShadow">
          
          <c:set var="endIndex" value="${fn:length(pair.value)-1}"/>  
          <c:if test="${endIndex < 0}">
            <c:set var="endIndex" value="0"/>    
          </c:if>    
          <c:choose>
            <c:when test="${endIndex-7 < 0}">
              <c:set var="startIndex" value="0"/>    
            </c:when>    
            <c:otherwise>
              <c:set var="startIndex" value="${endIndex-7}"/>        
            </c:otherwise>
          </c:choose>
          
          <jsp:useBean id="ChatmessageXml" class="com.looseboxes.web.components.ChatmessageXml"/>
          <%-- last 5 chat messages for each chat --%>  
          <table>
          <c:forEach begin="${startIndex}" end="${endIndex}" var="chatMessage" items="${pair.value}">
              
            <jsp:setProperty name="ChatmessageXml" property="chatmessage" value="${chatMessage}"/>  
            <jsp:setProperty name="ChatmessageXml" property="ownMessage" value="${chatMessage.fromEmail eq User.emailAddress}"/>    
            <tr>
              <td><jsp:getProperty name="ChatmessageXml" property="xml"/></td>
            </tr>
          </c:forEach>    
          </table>
        </div>  
      </div>
      
    </c:forEach>  
  </jsp:body>
</loose:page2>     
