<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
  <c:when test="${pageContext==null || pageContext.errorData==null}">
    <c:set var="reqRes" value=""/>      
  </c:when>  
  <c:otherwise>
    <c:set var="reqRes" value="${pageContext.errorData.requestURI}"/>    
  </c:otherwise>
</c:choose>
<loose:errorpage 
    pageTitle="${siteName} - Response Timeout (408 Error)" 
    pageDescription="response timeout, taking too long">   
    
    Sorry, there seems to be a problem at the moment. Its
    taking too long for a response from the server for:
    <p class="mySmaller">${reqRes}</p>
    
</loose:errorpage>
