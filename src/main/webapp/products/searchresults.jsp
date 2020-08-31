<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<c:if test="${searchText != null}">
  <c:set var="srSearchKey" value="${searchText}"/>  
</c:if>
<c:choose>
  <c:when test="${User.productSearchResults == null && empty User.productSearchResults.pages}">
    <c:set var="srSearchSize" value="0"/>      
  </c:when>  
  <c:otherwise>
    <c:set var="srSearchSize" value="${fn:length(User.productSearchResults.pages)}"/>        
  </c:otherwise>
</c:choose>

<jsp:useBean id="Listings" class="com.looseboxes.web.components.ListingsBean" scope="session"/>
<jsp:setProperty name="Listings" property="productcategory" value="${productcategory}"/>
<jsp:useBean id="User" class="com.looseboxes.web.components.UserBean" scope="session"/>

<loose:page>
  <jsp:attribute trim="true" name="pageTitle">
    <c:choose>
      <c:when test="${srSearchKey == null}">${siteName} - ${defaultTitle}</c:when>  
      <c:otherwise>${siteName} - search results for ${srSearchKey}</c:otherwise>
    </c:choose>    
  </jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">
    <c:choose>
      <c:when test="${srSearchKey == null}">search for ${productcategory}</c:when>  
      <c:otherwise>search results for ${srSearchKey}</c:otherwise>
    </c:choose>    
  </jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">
    ${productcategory} search, useful ${productcategory} links 
    <jsp:useBean id="NameToLabel" class="com.looseboxes.web.components.NameToLabel" scope="page"/>
    <c:forEach items="${Listings.typeKeywords}" step="1" var="typeKeyword">
      <jsp:setProperty name="NameToLabel" property="columnName" value="${typeKeyword}"/>
      ,${NameToLabel.value}
    </c:forEach>
  </jsp:attribute> 
  <jsp:body> 
    <c:choose>
      <c:when test="${srSearchSize != 0}">
        
        <loose:searchresults searchResultsBean="${User.productSearchResults}"/>
            
      </c:when>    
      <c:otherwise>
          
        <p>${srSearchSize} search results</p>
        
        <%@include file="/WEB-INF/jspf/centeradvert.jspf"%>  
        
      </c:otherwise>
    </c:choose>  
  </jsp:body>
</loose:page>     
