<%@tag trimDirectiveWhitespaces="true" description="displays a form details in a customized manner" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>

<%@attribute name="mfTargetForm" required="true" type="com.bc.web.core.form.Form"%>

<c:if test="${!mfTargetForm.hasImageFields}">
  <c:set var="skipStage" value="2"/>
</c:if>  

<bwf:formStages disableOtherStages="true" skipStage="${skipStage}" 
                  formStagesForm="${mfTargetForm}" formStage="${mfTargetForm.stage}"/>  

<input type="hidden" name="stage" value="${mfTargetForm.stage + 1}"/> 

<%--bwf:form fTargetForm="${mfTargetForm}"/--%> 
<%--@todo Above was replaced by below code --%>
<%-- BEGIN REPLACEMENT --%>
<bwf:displayallformfields dafTargetForm="${mfTargetForm}"/>
    
<jsp:useBean id="formField" class="com.bc.web.core.form.FormFieldBean" scope="page">       
  <jsp:setProperty name="formField" property="parentForm" value="${mfTargetForm}"/>
</jsp:useBean>    
<c:forEach var="fieldName" items="${mfTargetForm.fieldNames}">
  <jsp:setProperty name="formField" property="columnName" value="${fieldName}"/>        
<%--  @debug Column: ${fieldName}, Form: ${formField.form}<br/>--%>
  <c:if test="${formField.form != null}">
    <b>Add ${formField.label}</b><br/>
    <bwf:displayallformfields dafTargetForm="${formField.form}"/>
  </c:if>
</c:forEach>  
<%-- END REPLACEMENT --%>

<bwf:formverification hideAgreeToTerms="false" hideCaptcha="${mfTargetForm.hideCaptcha}"/>  

<div style="margin-top:0.5em">
  <bwf:defaultformNav skipStage="${skipStage}" dbAction="${mfTargetForm}" 
                   formStage="${mfTargetForm.stage}" submitValue="Next"/>
</div> 


