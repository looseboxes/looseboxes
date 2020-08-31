<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<jsp:useBean id="Listings" class="com.looseboxes.web.components.ListingsBean" scope="session"/>
<jsp:setProperty name="Listings" property="productcategory" value="${productcategory}"/>
<jsp:useBean id="User" class="com.looseboxes.web.components.UserBean" scope="session"/>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Find ${productcategory} - ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">
    Search results for ${productcategory} in categories:
    <jsp:useBean id="NameToLabel" class="com.looseboxes.web.components.NameToLabel" scope="page"/>
    <c:forEach items="${Listings.typeKeywords}" step="1" var="typeKeyword">
      <jsp:setProperty name="NameToLabel" property="columnName" value="${typeKeyword}"/>
      ${NameToLabel.value},
    </c:forEach>
  </jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">
    ${productcategory} search, useful ${productcategory} links, 
    <c:forEach items="${Listings.typeKeywords}" step="1" var="typeKeyword">
      <jsp:setProperty name="NameToLabel" property="columnName" value="${typeKeyword}"/>
      ${NameToLabel.value},
    </c:forEach>
  </jsp:attribute> 
  <jsp:body> 
    <c:choose>
      <c:when test="${User.productSearchResults != null}">
        <c:choose>
          <c:when test="${not empty User.productSearchResults.pages}">
            <loose:searchresults searchResultsBean="${User.productSearchResults}"/>
          </c:when>      
          <c:otherwise>
            0 search results
          </c:otherwise>
        </c:choose>  
      </c:when>    
      <c:otherwise>
        <c:redirect url="${contextURL}/search?cat=fashion&availabilityid=1"/>  
        <%--@include file="/WEB-INF/jspf/centeradvert.jspf"--%>  
      </c:otherwise>
    </c:choose>  
  </jsp:body>
</loose:page>     
