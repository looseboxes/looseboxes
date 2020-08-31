<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Sitemap</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Sitemap</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">sitemap, list of pages, info pages, help pages, articles, texts, feeds, user pages, legal pages</jsp:attribute> 
  <jsp:body>
    <br/>  
    <loose:menucontent contentClass="contentBox2" contentId="aboutLooseBoxes" display="true" 
    titleClass="header1Layout curvedTop" title="${siteName} Site Map" titleId="aboutLooseBoxesHeader">
        <div class="listView0">
          <ul>
            <li><a href="${contextURL}/index.jsp">Home page</a></li>
            <li><a href="${contextURL}/welcome.jsp">Welcome page</a></li>
            <li><a href="${contextURL}/products/searchresults.jsp">Search page</a></li>
          </ul>
        </div>
        <%@include file="/info/resources/index.xml"%><br/>
        <%@include file="/help/resources/index.xml"%><br/> 
        <%@include file="/cart/resources/index.xml"%>
        <%@include file="/articles/resources/index.xml"%><br/>
        <%@include file="/user/resources/index.xml"%><br/>
        <%@include file="/legal/resources/index.xml"%>
        <div class="listView0">
          <c:choose>
            <c:when test="${mobile}">
              <c:set var="pagesHref" value="${contextURL}/cat/${productcategory}/m/pages/index.jsp"/>
            </c:when>
            <c:otherwise>
              <c:set var="pagesHref" value="${contextURL}/cat/${productcategory}/pages/index.jsp"/>
            </c:otherwise>
          </c:choose>  
          <b><a href="${pagesHref}">Product Pages (${productcategory})</a></b><br/>  
        </div>
    </loose:menucontent>    
  </jsp:body>
</loose:page>    
