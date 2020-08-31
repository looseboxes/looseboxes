<%@tag trimDirectiveWhitespaces="true" description="Tag for rendering search results" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="searchResultsBean" required="true" type="com.bc.jpa.search.SearchResults"%>
<%@attribute name="displayMessage" required="false" description="default value is true"%>
<%@attribute name="displayPageLinks" required="false" description="default value is true"%>
<%@attribute name="displayMailAll" required="false" description="default value is true"%>

<c:if test="${displayMessage == null}">
  <c:set var="displayMessage" value="true"/>
</c:if>
<c:if test="${displayPageLinks == null}">
  <c:set var="displayPageLinks" value="true"/>
</c:if>
<c:if test="${displayMailAll == null}">
  <c:set var="displayMailAll" value="true"/>
</c:if>

<%-- BEGIN SEARCH RESULTS Edits here should be also done in class SearchRecordHtmlParser --%>

<%-- @related_searchresults div id is referenced in WebApp --%>
<div id="searchresults" class="fullWidth borderless justifiedBlock"
itemprop="mainContentOfPage" itemscope itemtype="http://schema.org/WebPageElement">
  <div class="fullWidth">    

    <c:if test="${'true' == displayMessage}">
        <loose:searchresultsMessage searchResultsBean="${searchResultsBean}" 
                                    searchServletPath="${contextURL}/search"/>      
    </c:if>  
      
    <%--@todo form class="mySmaller" style="color:gray" action="${contextURL}/search?ax=sort" 
    name="sortSearchresults" id="selectSearchresultsId" method="post">
      <c:if test="${fn:length(searchResultsBean.currentPage) > 1}">
        <c:if test="${!mobile}">&emsp;Sort by&nbsp;</c:if>  
        <loose:sort inputClass="tone0" searchResults="${ProductSearch}"/>  
        <input class="myBtnLayout" type="submit" value="Sort"/>
      </c:if>
    </form --%>  
  </div>
  <%--br/--%><%-- This break is important, some browsers go crazy without it --%>           
  <c:if test="${mobile}"><br/></c:if>
  
<%-- May be required by our fragments below --%>        
  <c:set var="searchResultsBean" value="${searchResultsBean}" scope="request"/>  
  
<%-- This is a Tag, so we have to call doBody --%>  
<%-- Also avoid putting comments within --%>  

  <loose:displayrows columnCount="${mobile?1:4}"  
                   items="${searchResultsBean.currentPage}" 
                   rowClass="srRow myFontSize0 spaced2"
                   cellClass="srCell myBorder5">
    <jsp:attribute name="cellFragment" trim="true">
      <%@include file="/WEB-INF/jspf/searchresultsCell.jspf"%>  
    </jsp:attribute> 
      
    <jsp:attribute name="bannerAfterFirstRow" trim="true">
      <%@include file="/WEB-INF/jspf/centeradvert.jspf"%>    
    </jsp:attribute>            
    
    <jsp:body>
      <jsp:doBody/>  
    </jsp:body> 
      
  </loose:displayrows>

  <c:if test="${displayPageLinks && searchResultsBean.pageCount > 1}">
    <loose:searchresultsPagelinks searchResultsBean="${searchResultsBean}" 
                                  searchServletPath="${contextURL}/search"/>      
  </c:if>  
</div>
<%-- END SEARCH RESULTS --%>
