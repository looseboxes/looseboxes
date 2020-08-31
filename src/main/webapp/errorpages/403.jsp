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
    pageTitle="${siteName} - Forbidden Resource Error (403 Error)" 
    pageDescription="forbidden resource error">   
    
    Access to the requested resource is forbidden:
    <p class="mySmaller">${reqRes}</p>
    
</loose:errorpage>
