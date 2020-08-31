<%@tag trimDirectiveWhitespaces="true" description="displays a list of html pages in the cached_pages folder of the current category" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@attribute name="tableName" required="false"%>
<%@attribute name="offset" required="false"%>
<%@attribute name="size" required="false"%>
<%@attribute name="mobile" required="false"%>

<c:if test="${tableName == null || tableName == ''}">
  <c:set var="tableName" value="${productTable}"/>    
</c:if>

<loose:listpages itemtype="http://schema.org/WebPage" 
fileTypes="html,jsp" exclude="index.html,index.jsp"
dir="/cat/${mobile=='true'?tableName+'/m':tableName}/pages" 
type="${mobile=='true'?'product pages (mobile-platform)':'product pages'}" 
offset="${offset}" size="${size}" productcategory="${productcategory}"/>
