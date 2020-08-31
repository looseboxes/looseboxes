<%@tag trimDirectiveWhitespaces="true" description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@attribute name="dbActionBean" type="com.bc.web.core.form.Form" required="true"%>
<%@attribute name="displayFormStages" type="java.lang.Boolean" required="false"%>
<%@attribute name="displayFormNav" type="java.lang.Boolean" required="false"%>

<c:if test="${displayFormStages == null || displayFormStages == ''}"><c:set var="displayFormStages" value="true"/></c:if>
<c:if test="${displayFormNave == null || displayFormStages == ''}"><c:set var="displayFormNav" value="true"/></c:if>
    
<c:if test="${!dbActionBean.hasImageFields}">
  <c:set var="skipStage" value="2"/>
</c:if>  

<c:if test="${displayFormStages}">
  <bwf:formStages skipStage="${skipStage}" formStagesForm="${dbActionBean}" formStage="${dbActionBean.stage}"/>    
</c:if>

<%-- Put a submit button at the top of the form --%>  
<c:choose>
<c:when test="${displayFormNav}">
  <bwf:defaultformNav skipStage="${skipStage}" dbAction="${dbActionBean}" 
  previousLink="true" formStage="${dbActionBean.stage}" submitValue="Submit"/>
</c:when>  
<c:otherwise>
  <input type="submit" value="Submit" class="myBtnLayout formNav"/>    
</c:otherwise>
</c:choose>

<fmt:setLocale value="${User.locale}" scope="request"/>

<jsp:useBean id="cfFormField" class="com.bc.web.core.form.FormFieldBean" scope="page">
  <jsp:setProperty name="cfFormField" property="parentForm" value="${dbActionBean}"/>    
</jsp:useBean>   

<table class="table0">
<c:forEach items="${dbActionBean.formDetails}" var="pair">
  <jsp:setProperty name="cfFormField" property="columnName" value="${pair.key}"/>      
  <jsp:setProperty name="cfFormField" property="columnValue" value="${pair.value}"/>      
  <c:choose>
    <c:when test="${pair.key == 'price' || pair.key == 'discount'}">
      <fmt:formatNumber var="cfValue" currencyCode="${User.currency.currencyCode}" 
                        maxFractionDigits="2" value="${pair.value}" type="currency"/>  
    </c:when>
    <c:when test="${cfFormField.dateType}">
      <fmt:formatDate var="cfValue" pattern="${dbActionBean.datePatterns[0]}" value="${pair.value}"/>   
    </c:when>    
    <c:when test="${cfFormField.timeType}">
      <fmt:formatDate var="cfValue" pattern="${dbActionBean.timePatterns[0]}" value="${pair.value}"/>   
    </c:when>    
    <c:when test="${cfFormField.timestampType}">
      <fmt:formatDate var="cfValue" pattern="${dbActionBean.timestampPatterns[0]}" value="${pair.value}"/>   
    </c:when>    
    <c:when test="${!cfFormField.multiChoice}">
      <c:set var="cfValue" value="${pair.value}"/>    
    </c:when>
    <c:otherwise>
<%--@todo Use cfFormField.tableName, name and value to fetch the actual value and display here --%>      
      <c:set var="cfValue" value="${pair.value}"/>        
    </c:otherwise>
  </c:choose>  
  <tr style="border-bottom:2px solid #EEEEEE; margin-bottom:0.5em;">
    <td class="tableDT">${cfFormField.label}</td><td class="tableDD">${cfValue}</td>
  </tr>
</c:forEach>
</table>

<%-- Put another submit button at the bottom of the form if there are more than 7 entries --%>  
<c:if test="${fn:length(dbActionBean.record) > 7}">
<c:choose>
  <c:when test="${displayFormNav}">
    <div style="margin-top:0.5em">
      <bwf:defaultformNav skipStage="${skipStage}" dbAction="${dbActionBean}" 
      previousLink="true" formStage="${dbActionBean.stage}" submitValue="Submit"/>
    </div> 
  </c:when>  
  <c:otherwise>
    <input type="submit" value="Submit" class="myBtnLayout formNav"/>    
  </c:otherwise>
</c:choose>
</c:if>

