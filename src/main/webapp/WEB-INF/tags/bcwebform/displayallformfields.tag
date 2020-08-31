<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="dafTargetForm" type="com.bc.web.core.form.Form"%>

<c:if test="${dafTargetForm.mandatoryFields != null && not empty dafTargetForm.mandatoryFields}">
  <div>Asterixed (<font style="color:red; font-weight:bold">*</font>) fields are mandatory.</div>
  <br/> 
  <bwf:displayformfields fieldTypes="select,text,hidden" nullables="false" 
                         disableSubmitOnChange="true" dfsForm="${dafTargetForm}"/>
  <br/>
</c:if>
<c:if test="${dafTargetForm.optionalFields != null && not empty dafTargetForm.optionalFields}">
  
<%-- We use the hasImageFields property from the main form and all its children --%>    
  <c:set var="fHasImageFields" value="false"/>        
  <c:choose>
    <c:when test="${dafTargetForm.hasImageFields}">
      <c:set var="fHasImageFields" value="true"/>        
    </c:when>
    <c:otherwise>
      <c:forEach var="fReferenceForm" items="${dafTargetForm.referencedForms}">
        <c:if test="${fReferenceForm.hasImageFields}">
          <c:set var="fHasImageFields" value="true"/>            
        </c:if>  
      </c:forEach>   
    </c:otherwise>
  </c:choose>
  
  <c:if test="${fHasImageFields}">
      <bwf:displayformfields fieldTypes="file" nullables="true" 
                             disableSubmitOnChange="true" dfsForm="${dafTargetForm}"/>
  </c:if>
    
  <bwf:displayformfields fieldTypes="select,text,hidden" nullables="true" 
                         disableSubmitOnChange="true" dfsForm="${dafTargetForm}"/>
</c:if>
