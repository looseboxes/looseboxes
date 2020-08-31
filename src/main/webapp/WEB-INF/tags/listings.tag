<%@tag trimDirectiveWhitespaces="true" description="displays various listings under a category in a list" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="tagName" required="false" description="Supported values: 'select', 'ul', 'ol', default is 'ul'"%>
<%@attribute name="listingNames" required="true" description="E.g 'category,employmentType,offerType,type'"%>
<%@attribute name="maxLength" required="true"%>
<%@attribute name="listId"%>
<%@attribute name="listClass"%>
<%@attribute name="listItemClass"%>
<%@attribute name="hideHeadings"%>
<%@attribute name="headingClass"%>
<%@attribute name="hideLinks"%>
<%@attribute name="hideCounts"%>

<jsp:useBean id="Listings" class="com.looseboxes.web.components.ListingsBean" scope="session"/>
<jsp:setProperty name="Listings" property="productcategory" value="${productcategory}"/>    

<jsp:useBean id="NameToLabel" class="com.looseboxes.web.components.NameToLabel" scope="page"/>
<jsp:setProperty name="NameToLabel" property="tableName" value="product"/>

<c:if test="${listId == null || listId == ''}">
  <c:set var="listId" value="generatedListId_${listingNames}_${maxLength}"/>    
</c:if>

<div id="${listId}" class="${listClass}">
    
  <c:forEach var="listing" items="${Listings.listings.values}">
      
    <c:if test="${fn:contains(listingNames, listing.key)}">  
          
        <c:if test="${hideHeadings != 'true'}">
          <jsp:setProperty name="NameToLabel" property="columnName" value="${listing.key}"/>
          <div class="${headingClass}">${NameToLabel.value}</div>    
        </c:if>    

        <c:choose>
          <c:when test="${tagName == 'select'}">
            <select name="${listing.key}" class="spaced2">
          </c:when>  
          <c:when test="${tagName == 'ol'}">
            <ol class="spaced2">
          </c:when>
          <c:otherwise>
            <ul class="spaced2">
          </c:otherwise>    
        </c:choose>  
        
        <c:forEach var="pair" items="${listing.value}">
            
          <c:if test="${hideLinks != 'true'}">
            <c:choose>
              <c:when test="${listing.key == 'productsubcategoryid'}">
                <c:set var="listingURL" value="${contextURL}/cat/${productcategory}/${pair.key.label}.jsp"/>    
              </c:when>
              <c:otherwise>
                <c:set var="listingURL" value="${contextURL}/search?cat=${productcategory}&amp;${listing.key}=${pair.key.label}"/>
              </c:otherwise>
            </c:choose>
          </c:if>
        
          <c:if test="${hideCounts != 'true'}">
            <c:set var="listingCount" value="${pair.value}"/>    
          </c:if>
          
          <c:choose>
            <c:when test="${tagName == 'select'}">
                
              <option value="${keyOfValue}" class="${listItemClass}" onclick="${listingURL}">
                <c:choose>
                  <c:when test="${mobile}">
                    <loose:truncate ellipsis=".." maxLength="16" target="${pair.key.label}"/>&nbsp;(${listingCount})
                  </c:when>
                  <c:otherwise>${pair.key.label}&nbsp;(${listingCount})</c:otherwise>
                </c:choose>  
              </option>  
              
            </c:when>    
            <c:otherwise>
                
              <li class="${listItemClass}">
                <a href="${listingURL}">
                <c:choose>
                  <c:when test="${mobile}">
                    <loose:truncate ellipsis=".." maxLength="18" target="${pair.key.label}"/>&nbsp;(${listingCount})
                  </c:when>
                  <c:otherwise>${pair.key.label}&nbsp;(${listingCount})</c:otherwise>
                </c:choose>  
                </a>
              </li>  
            </c:otherwise>
          </c:choose>
            
        </c:forEach>

        <c:choose>
          <c:when test="${tagName == 'select'}">
            </select>
          </c:when>  
          <c:when test="${tagName == 'ol'}">
            </ol>
          </c:when>
          <c:otherwise>
            </ul>
          </c:otherwise>    
        </c:choose>  
          
    </c:if>
          
  </c:forEach>
</div>
