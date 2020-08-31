<%@tag trimDirectiveWhitespaces="true" description="The link of the specified form stage" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="formStage"%>
<%@attribute name="fsForm" type="com.bc.web.core.form.Form" required="true"%>
<%@attribute name="nodeName"%>
<%@attribute name="nodeValue"%>
<%@attribute name="nodeClass"%>
<%-- // @related formStage action  we add parameter action=0 to tell the servlet don't stress yourself --%>
<c:choose>
  <c:when test="${formStage == '-1'}">
    <c:set var="linkText" scope="page" value="Cancel"/>  
  </c:when>
  <c:when test="${formStage == '0'}">
    <c:set var="linkText" scope="page" value="Enter Details"/>  
  </c:when>
  <c:when test="${formStage == '1'}">
    <c:set var="linkText" scope="page" value="Confirm Entries"/>  
  </c:when>
  <c:otherwise>
    <c:set var="linkText" scope="page" value="Form Stage ${formStage}"/>  
  </c:otherwise>  
</c:choose>
<c:set var="link" scope="page" value="${contextURL}${fsForm.action}?action=0&stage=${formStage}"/>  
<c:choose>
  <c:when test="${nodeName == 'span'}"><span class="${nodeClass}">${nodeValue != null ? nodeValue : linkText}</span></c:when>  
  <c:when test="${nodeName == 'button'}"><input class="${nodeClass}" onclick="window.location='${link}'" type="button" value="${nodeValue != null ? nodeValue : linkText}"/></c:when>  
  <c:otherwise><a class="${nodeClass}" href="${link}">${nodeValue != null ? nodeValue : linkText}</a></c:otherwise>  
</c:choose>

