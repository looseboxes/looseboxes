<%@tag trimDirectiveWhitespaces="true" description="displays a from navigation " pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<%@attribute name="formStage"%>
<%@attribute name="dbAction" type="com.bc.web.core.form.Form" required="true"%>
<%@attribute name="menuClass" required="true"%>
<%@attribute name="submitValue" required="true"%>
<%@attribute name="resetValue"%>
<%@attribute name="firstLink"%>
<%@attribute name="previousLink"%>
<%@attribute name="nextLink"%>
<%@attribute name="lastLink"%>
<%@attribute name="cancel"%>
<%@attribute name="skipStage"%>
<c:if test="${cancel == 'true'}">
  <bwf:formStage fsForm="${dbAction}" formStage="-1" nodeValue="Cancel" nodeClass="${menuClass}"/>&nbsp;&nbsp;  
</c:if>   
<c:if test="${resetValue != null && resetValue != ''}">
  <input class="${menuClass}" type="reset" value="${resetValue}"/>&nbsp;&nbsp;
</c:if>   
<c:if test="${firstLink == 'true' && skipStage != '0'}">
  <bwf:formStage fsForm="${dbAction}" formStage="0" nodeValue="To Start" nodeClass="${menuClass}"/>&nbsp;&nbsp;
</c:if>   
<c:if test="${previousLink == 'true' && skipStage != formStage-1}">
  <bwf:formStage fsForm="${dbAction}" formStage="${formStage-1}" nodeValue="Go Back" nodeClass="${menuClass}"/>&nbsp;&nbsp;
</c:if>   
<c:if test="${nextLink == 'true' && skipStage != formStage+1}">
  <bwf:formStage fsForm="${dbAction}" formStage="${formStage+1}" nodeValue="Skip This" nodeClass="${menuClass}"/>&nbsp;&nbsp;
</c:if>   
<%--@related_25 the submit button ID 'submitId' is used by myFormHandlerScripts --%>  
<input class="${menuClass}" type="submit" id="submitId" value="${submitValue}"/>
<c:if test="${lastLink == 'true' && skipStage != '3'}">
  <bwf:formStage fsForm="${dbAction}" formStage="3" nodeValue="To End" nodeClass="${menuClass}"/>&nbsp;&nbsp;
</c:if>   
