<%@tag trimDirectiveWhitespaces="true" description="Select the first available image for a specified Product and sets it to a specified variable" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="spiProduct" type="com.looseboxes.pu.entities.Product" required="true"%>
<%@attribute name="spiProductvariantId" type="java.lang.Integer" required="false"%>
<%@attribute name="spiIncludeLogo" required="true"%>

<%-- Note the variable is exported via the request scope --%>
<%-- Hence we set any previous value within the request to null by removing this --%>
<c:remove scope="request" var="selectedImage"/>

<c:if test="${spiIncludeLogo && spiProduct.logo != null && spiProduct.logo != ''}">
  <c:set scope="request" var="selectedImage" value="${spiProduct.logo}"/>  
</c:if>

<c:if test="${selectedImage == null || selectedImage == ''}">
  <c:forEach var="productunit" items="${spiProduct.productvariantList}">
      
    <c:if test="${spiProductvariantId == null || spiProductvariantId == productunit.productvariantid}">

        <c:if test="${selectedImage == null || selectedImage == ''}">
          <c:set scope="request" var="selectedImage" value="${productunit.image1}"/>          
        </c:if>    
        <c:if test="${selectedImage == null || selectedImage == ''}">
          <c:set scope="request" var="selectedImage" value="${productunit.image2}"/>          
        </c:if>    
        <c:if test="${selectedImage == null || selectedImage == ''}">
          <c:set scope="request" var="selectedImage" value="${productunit.image3}"/>          
        </c:if>    
        <c:if test="${selectedImage == null || selectedImage == ''}">
          <c:set scope="request" var="selectedImage" value="${productunit.image4}"/>          
        </c:if>    
        <c:if test="${selectedImage == null || selectedImage == ''}">
          <c:set scope="request" var="selectedImage" value="${productunit.image5}"/>          
        </c:if>    
        <c:if test="${selectedImage == null || selectedImage == ''}">
          <c:set scope="request" var="selectedImage" value="${productunit.image6}"/>          
        </c:if>    
        <c:if test="${selectedImage == null || selectedImage == ''}">
          <c:set scope="request" var="selectedImage" value="${productunit.image7}"/>          
        </c:if>    
        
    </c:if>  
      
  </c:forEach>
</c:if>
