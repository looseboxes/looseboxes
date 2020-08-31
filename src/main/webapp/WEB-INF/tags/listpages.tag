<%@tag trimDirectiveWhitespaces="true" description="displays a list of pages in the specified folder" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="type" description="String. feeds,articles etc identifying this list type" required="true"%>
<%@attribute name="dir" description="the directory containing the list of pages to display" required="true"%>
<%@attribute name="itemtype" description="HTML 5 itemtype property for the itemscope see schema.org" required="true"%>

<%@attribute name="fileTypes" description="comma separated list of fileTypes to be listed. E.g html,jsp,xml" required="false"%>
<%@attribute name="exclude" description="comma separated list of filenames to exclude" required="false"%>

<%@attribute name="productcategory" required="false"%>
<%@attribute name="offset" required="false"%>
<%@attribute name="size" required="false"%>

<jsp:useBean id="PageList" class="com.looseboxes.web.components.PageList">  
  <jsp:setProperty name="PageList" property="fileTypes" value="${fileTypes}"/>    
  <jsp:setProperty name="PageList" property="exclude" value="${exclude}"/>    
  <jsp:setProperty name="PageList" property="dir" value="${dir}"/>    
</jsp:useBean>    

<c:if test="${offset == null || offset == ''}">
  <c:set var="offset" value="0"/>    
</c:if>

<c:set var="pageListLen" value="${fn:length(PageList)}"/>

<c:if test="${size == null || size == ''}">
  <c:set var="size" value="${pageListLen-offset}"/>    
</c:if>

<c:set var="end" value="${offset+size}"/>

<c:if test="${end > pageListLen}">
  <c:set var="end" value="${pageListLen}"/>    
</c:if>

<c:choose>
  <c:when test="${not empty PageList}">
    <ul class="listView0">
      <c:forEach begin="${offset}" end="${end}" var="PageMetaData" items="${PageList}">
        <li itemscope itemtype="${itemtype}"><a itemprop="url" href="${PageMetaData.link}">${PageMetaData}</a></li>        
      </c:forEach>
    </ul>
  </c:when>      
  <c:otherwise>
    <b>There are currently no ${type} available <c:if test="${productcategory != null}"> for the category ${productcategory}</c:if></b>    
    <br/>  
  </c:otherwise>
</c:choose>

