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

    <div id="newChatTitle" class="header1Layout curvedTop">Start new Chat</div>
    <form name="newChatForm" id="newChatFormId" class="contentBox2"
            method="post" action="${contextURL}/chat">

      <input type="hidden" name="action" value="load"/>
          
      <label>
        Enter email/username of the user to chat with:
      
<%-- @related_toUser 'toUser' field indicates either 'toEmail' or 'toUsername' --%>        
        <input type="text" name="toUser"  
               title="type the email/username of the user you want to chat with here"/>
      </label>

        
      <input class="myBtnLayout" type="submit" value="Send"/>
        
    </form>      
      
    <c:set var="userChats" value="${User.chats}"/>  

<%-- Map<String, List<Entity>> --%>    
    <c:forEach varStatus="vs" var="pair" items="${userChats}">

      <c:set var="toEmail" value="${pair.key}"/>
      
      <br/>
      
      <loose:chatbox cbContentId="chatContentId${vs.index}" 
                     cbTitle="Chat with ${toEmail}" 
                     cbTitleId="chatTitle${vs.index}" 
                     cbToEmail="${toEmail}" 
                     cbChatTextId="chatTextId${vs.index}"
                     cbMessagesId="chatAjaxResults${vs.index}">
          
        <jsp:attribute name="cbMessagesContent" trim="true">
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
        </jsp:attribute>    
          
      </loose:chatbox>    
    </c:forEach>  
  </jsp:body>
</loose:page2>     
