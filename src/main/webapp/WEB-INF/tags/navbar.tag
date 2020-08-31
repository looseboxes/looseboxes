<%@tag trimDirectiveWhitespaces="true" description="Tag fragment for a simple menubar with back and home buttons to the left and a title to the right" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="title" required="false"%> 
<%@attribute name="previousLink" required="false"%> 
<%@attribute name="nextLink" required="false"%> 
<div class="menubar">
  <c:if test="${previousLink != null && previousLink != ''}">
    <a href="${previousLink}"> 
      <img alt=" < " class="previousArrowImg" src="${contextURL}/images/transparent.gif"/>&nbsp;Back 
    </a>&emsp;
  </c:if>    
  <a href="${contextURL}/index.jsp">Home</a>&emsp;
  <c:if test="${title != null && title != ''}">
    ${title}&emsp;
  </c:if>    
  <c:if test="${nextLink != null && nextLink != ''}">
    <a href="${nextLink}"> 
      Next&nbsp;<img alt=" > " class="nextArrowImg" src="${contextURL}/images/transparent.gif"/> 
    </a>
  </c:if>    
</div>
