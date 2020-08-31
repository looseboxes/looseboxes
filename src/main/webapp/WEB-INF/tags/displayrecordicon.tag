<%@tag trimDirectiveWhitespaces="true" description="displays the an icon for the specified Product" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="product" type="com.looseboxes.pu.entities.Product" 
required="true" fragment="false" description="The Product whose icon is to be displayed"%>

<%@attribute name="displayImageInLink" required="false" fragment="false"%>

<%@attribute name="imageClass" required="false" fragment="false"%>
<%@attribute name="noImageClass" required="false" fragment="false"%>

<loose:selectproductimage spiIncludeLogo="true" spiProduct="${product}"/>
<c:set var="imageSrc" value="${selectedImage}"/>          

<c:set var="miStyle" value=""/>

<c:choose>
  <c:when test="${imageSrc != null && imageSrc != ''}">
      
    <c:choose>
      <c:when test="${displayImageInLink == 'true'}">
        <a style="${miStyle}" 
           href="${contextURL}/products/${product.productid}_<%=java.net.URLEncoder.encode(product.getProductName(), "utf-8")%>.jsp">
          <loose:getimagesrc imagePath="${imageSrc}"/>  

          <img id="image${product.productid}" 
             itemprop="image" class="${imageClass}" 
             src="${getimagesrc_imageSrc}" 
             alt="${product.productName}" 
             title="${product.productName}"/> 
        </a>
      </c:when>  
      <c:otherwise>
        <loose:getimagesrc imagePath="${imageSrc}"/>  

        <img style="${miStyle}" id="image${product.productid}" 
           itemprop="image" class="${imageClass}" 
           src="${getimagesrc_imageSrc}" 
           alt="${product.productName}" 
           title="${product.productName}"/> 
      </c:otherwise>
    </c:choose>
      
  </c:when> 
  <c:otherwise>
      
    <c:choose>
      <c:when test="${displayImageInLink == 'true'}">
        <a style="${miStyle}" href="${contextURL}/products/${product.productid}_<%=java.net.URLEncoder.encode(product.getProductName(), "utf-8")%>.jsp">
          <span class="${noImageClass}">
            <span class="layout1 myFontSize2">${siteName}</span>  
            <br/><br/><span class="myFontSize1">no image</span>
          </span>
        </a>    
      </c:when>
      <c:otherwise>
        <span style="${miStyle}" class="${noImageClass}">
          <span class="layout1 myFontSize2">${siteName}</span>  
          <br/><br/><span class="myFontSize1">no image</span>
        </span>
      </c:otherwise>
    </c:choose>  
      
  </c:otherwise>
</c:choose>  
