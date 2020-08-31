<%@tag trimDirectiveWhitespaces="true" description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<%@attribute name="formStage" required="true"%>
<%@attribute name="formStagesForm" type="com.bc.web.core.form.Form" required="true"%>
<%@attribute name="disableOtherStages"%>
<%@attribute name="skipStage"%>

<div id="formStages" style="margin:0.5em">
  <c:forEach var="stageIndex" begin="0" end="${formStagesForm.stageCount}">
    <c:choose>
      <c:when test="${skipStage == stageIndex}"></c:when>  <%-- do nothing --%>
      <c:when test="${(formStage == '' && stageIndex == 0) || formStage == stageIndex}">
        <bwf:formStage  nodeClass="formStage boxShadow" fsForm="${formStagesForm}" formStage="${stageIndex}"/>  
        <c:if test="${stageIndex != formStagesForm.stageCount}">&nbsp;--<img alt="&gt;" class="nextArrowImg" src="${contextURL}/images/transparent.gif"/></c:if>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${disableOtherStages == 'true'}">
            <bwf:formStage  nodeClass="formStage mySmaller" nodeName="span" fsForm="${formStagesForm}" formStage="${stageIndex}"/>    
            <c:if test="${stageIndex != formStagesForm.stageCount}">&nbsp;--<img alt="&gt;" class="nextArrowImg" src="${contextURL}/images/transparent.gif"/></c:if>
          </c:when>
          <c:otherwise>
            <bwf:formStage nodeClass="formStage mySmaller" fsForm="${formStagesForm}" formStage="${stageIndex}"/>      
            <c:if test="${stageIndex != formStagesForm.stageCount}">&nbsp;--<img alt="&gt;" class="nextArrowImg" src="${contextURL}/images/transparent.gif"/></c:if>
          </c:otherwise>
        </c:choose>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</div>
