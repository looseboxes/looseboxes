<%@tag trimDirectiveWhitespaces="true" description="template for content which could be closed or minimized" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="title" required="true"%>
<%@attribute name="display" required="false"%>
<%@attribute name="titleId"%>
<%@attribute name="contentId"%>
<c:if test="${display == null || display == ''}">
  <c:set var="display" value="true"/>    
</c:if>
<loose:menucontent title="${title}" titleId="${titleId}" titleClass="header4Layout curvedTop fullWidth" 
                   contentId="${contentId}" contentClass="contentBox sideContentWidth" display="${display}">
  <jsp:doBody/>  
</loose:menucontent>
