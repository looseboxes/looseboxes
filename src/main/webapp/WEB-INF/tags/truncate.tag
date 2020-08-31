<%@tag trimDirectiveWhitespaces="true" description="Truncates a string if it is longer than a specified length" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="target" required="true" description="The string to truncate if length is greater than maxLength" type="java.lang.String"%>
<%@attribute name="maxLength" required="true" description="The max length allowed for the target"%>
<%@attribute name="ellipsis" required="false" description="The ellipsis to append to the target if truncated" type="java.lang.String"%>
<%@attribute name="escapeXml" required="false" description="Determines if output will be xml escaped"%>

<c:if test="${escapeXml == null || escapeXml == ''}"><c:set var="escapeXml" value="false"/></c:if>
    
<c:choose>
  <c:when test="${fn:length(target) > maxLength}">
    <c:out escapeXml="${escapeXml}" value="${fn:substring(target, 0, maxLength)}${ellipsis}"/>        
  </c:when>
  <c:otherwise>
    <c:out escapeXml="${escapeXml}" value="${target}"/>            
  </c:otherwise>
</c:choose>    
