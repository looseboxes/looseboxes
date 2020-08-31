<%@page isErrorPage="true" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<loose:errorpage 
    pageTitle="${siteName} - Error Processing Request" 
    pageDescription="An error occured while processing the request" 
    displayTopBanner="true">   
    
    <c:choose>
      <c:when test="${pageContext.errorData != null && pageContext.errorData.throwable.localizedMessage != null}">
        ${pageContext.errorData.throwable.localizedMessage}  
      </c:when>    
      <c:otherwise>
        <%@include file="/WEB-INF/jspf/oops.jspf"%>  
      </c:otherwise>
    </c:choose>
    
</loose:errorpage>




