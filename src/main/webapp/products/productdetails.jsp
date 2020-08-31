<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="/WEB-INF/tlds/bcsocial" prefix="social"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:if test="${User.selectedItem == null}">
  <jsp:forward page="${contextURL}/products/searchresults.jsp"/>
</c:if>

<c:set var="productdetailsTitle" value="${User.selectedItem.record.productsubcategoryid.productsubcategory} @${siteName}"/>
<c:set var="productdetailsDescription" value="${User.selectedItem.record.productsubcategoryid.productsubcategory} - ${User.selectedItem.record.productName}"/>

<loose:page pageItemtype="http://schema.org/Product">
  <jsp:attribute trim="true" name="pageTitle">${productdetailsTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">${productdetailsDescription}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">${User.selectedItem.metaKeywords}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeadInclude">
  
    <social:socialmetatags smItem="${User.selectedItem.record}" 
                           smItemVariant="${User.selectedItem.selectedVariant}"/>
      
    <c:if test="${User.selectedItem != null && User.selectedItem.canBeLocated}">
      <link href="http://code.google.com/apis/maps/documentation/javascript/examples/default.css" rel="stylesheet" type="text/css"/>
    </c:if>
  </jsp:attribute>    
  <jsp:attribute trim="true" name="pageAfterBodyInclude">
    <c:if test="${User.selectedItem != null}">
      <c:if test="${User.selectedItem.canBeLocated}">
        <script type="text/javascript"> 
    <%-- Had to separate these too load events. More effective this way --%>  
          looseboxes.addLoadEvent(
            googlemap.init('messagenode','addressnode','map_canvas','latitudeId','longitudeId')
          );
          looseboxes.addLoadEvent(
            googlemap.loadscript()
          );
        </script>
      </c:if>     
    </c:if>
    <c:if test="${User.selectedItem.images != null}">
    <script type="text/javascript">
      <c:forEach varStatus="vs" var="image" items="${User.selectedItem.images}">
        <c:choose>
          <c:when test="${fn:startsWith(image.value, 'http://')}">
            <c:set var="imagePath" value="${image.value}"/>  
          </c:when>  
          <c:otherwise>
            <c:set var="imagePath" value="$${localURL}${image.value}"/>  
          </c:otherwise>    
        </c:choose>  
<%-- Load each image --%>            
        looseboxes.loadImage("${imagePath}", 435, 435);    
<%-- Display the first image --%>            
        <c:if test="${vs.index == 0}">
<%-- @related_ids initialImage and initialImageLink --%>            
          looseboxes.loadAndDisplayImage(null, null, 'initialImage', '${imagePath}', 'initialImageLink', '${contextURL}/viewimg?vid=${User.selectedItem.selectedVariant.productvariantid}&image=${image.key}');
        </c:if>    
      </c:forEach>
    </script>  
    </c:if>
  </jsp:attribute>      
  <jsp:body>
    <div id="productJspDataBody">  
        <c:if test="${!mobile}">
          <c:set var="mTitle" value="Click any of the buttons below for more"/>    
          <loose:navbar previousLink="${contextURL}/products/searchresults.jsp" title="${mTitle}"/>
          <br/>
        </c:if>
        <c:choose>
          <c:when test="${User.selectedItem == null}">
            This page is for viewing selected items. No item is currently selected for viewing. 
            <a href="${contextURL}/products/searchresults.jsp">Click here</a> to select an item for viewing     
            <br/>
          </c:when>
          <c:otherwise>
              
            <%@include file="/WEB-INF/jspf/selectedProductDetails.jspf"%>  
            <%--@todo c:choose>
              <c:when test="${mobile}"><%@include file="/WEB-INF/jspf/m/selectedProductDetails.jspf"%></c:when>      
              <c:otherwise><%@include file="/WEB-INF/jspf/selectedProductDetails.jspf"%></c:otherwise>
            </c:choose --%>  
            
<%-- I REMOVED A LOT FROM HERE, ESPECIALLY HAVING TO DO WITH COMMENTS --%>           
          </c:otherwise>    
        </c:choose> 
    </div>
  </jsp:body>
</loose:page>     

