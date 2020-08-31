<%@tag trimDirectiveWhitespaces="true" description="displays a list of articles in the articles folder of the current category" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@attribute name="tableName" required="false"%>
<%@attribute name="offset" required="false"%>
<%@attribute name="size" required="false"%>

<c:if test="${tableName == null || tableName == ''}">
  <c:set var="tableName" value="${productTable}"/>    
</c:if>

<loose:listpages itemtype="http://schema.org/Article" 
fileTypes="jsp" exclude="index.jsp" 
dir="/cat/${tableName}/articles" type="articles" 
offset="${offset}" size="${size}" productcategory="${productcategory}"/>

