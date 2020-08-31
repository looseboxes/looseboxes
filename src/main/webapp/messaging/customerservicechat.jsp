<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<c:if test="${!User.loggedIn}">
  <c:redirect url="${contextURL}/user/login.jsp"/>      
</c:if>
<loose:page2>
  <jsp:attribute trim="true" name="pageTitle">Chat Live with Customer Service - ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Chat live with Customer Service</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">customer service chat</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeadInclude">
<%-- Refresh the page every X seconds, needed for chat --%>      
<%--
  <meta http-equiv="Cache-Control" content="no-cache"/>
  <meta http-equiv="refresh" content="15"/>      
--%>
  </jsp:attribute>
  <jsp:body> 
      
    <c:if test="${!User.customerServiceLoggedInToChat}">
      <p class="handWriting myBorder">
        All our Customer Service assistants are currently engaged. <b>Please leave
        a message.</b> You will be attended to within 24 hours.
      </p>        
    </c:if>  
    <p class="mySmaller">
      &nbsp;<span class="boldRed">*</span> Chat is only available Mon - Fri (9am - 5pm).    
    </p>
    
    <loose:chatbox cbChatTextId="chatTextId" 
                   cbContentId="csChatId" 
                   cbMessagesId="csChatAjaxResults" 
                   cbTitle="Chat Live with Customer Service" 
                   cbTitleId="csChatHeader" 
                   cbToEmail="looseboxes@gmail.com"/>
  </jsp:body>
</loose:page2>     
