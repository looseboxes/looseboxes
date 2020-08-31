<%@tag trimDirectiveWhitespaces="true" description="builds an image source and exports it to a specified variable" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="imagePath" required="true"%>

<c:choose>
  <c:when test="${fn:startsWith(imagePath, 'http://')}">
    <c:set var="mLocalFolder" value=""/>    
  </c:when>  
  <c:otherwise>
    <c:set var="mLocalFolder" value="${localURL}"/>      
  </c:otherwise>
</c:choose>  

<c:set scope="request" var="getimagesrc_imageSrc" value="${mLocalFolder}${imagePath}"/>
