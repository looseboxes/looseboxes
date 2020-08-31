<%@tag trimDirectiveWhitespaces="true" description="A customizable message form. Sender and recipient details are already known. The sender only needs to enter the message subject and text" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="messageType" required="true"%>
<%@attribute name="fromEmail" required="false"%>
<%@attribute name="fromUsername" required="false"%>
<%@attribute name="toEmail" required="false"%>
<%@attribute name="toUsername" required="false"%>
<%@attribute name="targetProductId" required="false"%>
<%@attribute name="chatWithRecipient" required="false"%>

<c:choose>
  <c:when test="${mobile}">
    <c:set var="subjectClass" value="fullWidth formInput tone0"/>        
    <c:set var="textClass" value="fullWidth formInput tone0"/>        
    <c:set var="textRows" value="3"/>        
    <c:set var="textCols" value="30"/>        
  </c:when>  
  <c:otherwise>
    <c:set var="subjectClass" value="formInput tone0"/>        
    <c:set var="textClass" value="formInput tone0"/>        
    <c:set var="textRows" value="4"/>        
    <c:set var="textCols" value="100"/>        
  </c:otherwise>  
</c:choose>
<c:if test="${chatWithRecipient == 'true'}">
  <form class="form0" name="chatWithSeller" method="post" action="${contextURL}/chat?ax=load">
    <b>Seller is online</b>&emsp;
    <input type="hidden" name="toEmail" value="${toEmail}"/>     
    <input class="myBtnLayout" type="submit" value="Chat Now"/>
  </form>
  <br/>  
</c:if>  
<form class="form0" name="contactSeller" method="post" action="${contextURL}/contactux?t=${messageType}">
    
  <input type="hidden" name="fromEmail" value="${fromEmail}"/>     
  <input type="hidden" name="fromUsername" value="${fromUsername}"/>     
  <input type="hidden" name="toEmail" value="${toEmail}"/>     
  <input type="hidden" name="toUsername" value="${toUsername}"/>     
  <input type="hidden" name="productid" value="${targetProductId}"/>     
  
  <input class="${subjectClass}" type="text" name="messageSubject" id="messageSubjectId"
  value="Subject:" onclick="myFormHandler.clearField('messageSubjectId')"/>     
  <br/><br/>
  
  <textarea rows="${textRows}" cols="${textCols}" class="${textClass}" name="messageText" id="messageTextId"
  onclick="myFormHandler.clearField('messageTextId')">Enter your message here</textarea>
  <br/>
  
  <input class="myBtnLayout" type="submit" value="Send"/>
  
</form>

