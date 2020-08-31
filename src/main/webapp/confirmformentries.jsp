<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>

<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Confirm Entries Page - ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">confirm form entries</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">confirm form entries</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    <c:choose>
      <c:when test="${mobile}">Confirm Details</c:when>    
      <c:otherwise>Confirm&nbsp;${FormToConfirm.type.label}&nbsp;Details</c:otherwise>
    </c:choose> 
  </jsp:attribute>  
  <jsp:body> 
      
    <form name="${FormToConfirm.table}form" id="${FormToConfirm.table}formId"  
    method="post" action="${contextURL}${FormToConfirm.action}">
      
<%-- Move to the next stage --%>        
      <input type="hidden" name="stage" value="${FormToConfirm.stage + 1}"/>
        
      <bwf:confirmationform dbActionBean="${FormToConfirm}"/>
    
    </form>
      
  </jsp:body> 
</loose:page1>
