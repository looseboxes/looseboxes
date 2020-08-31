<%@tag trimDirectiveWhitespaces="true" description="a customizable drop down menu" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="nodeClass"%> 
<%@attribute name="nodeLink"%> 
<%@attribute name="nodeText"%> 
<%@attribute name="useDropDownMenu"%>
<%@attribute name="dropDownMenuId"%>
<%@attribute name="dropDownMenuClass"%>
<div class="${nodeClass}" style="padding:0.5em 0 0.5em 0">
  <c:choose>
    <c:when test="${useDropDownMenu == 'true'}">
      <b><a href="${contextURL}${nodeLink}" onmouseover="myDropDownMenu.open('${dropDownMenuId}', false)">${nodeText}</a></b>
    </c:when>  
    <c:otherwise>
      <b><a href="${contextURL}${nodeLink}">${nodeText}</a></b>
    </c:otherwise>
  </c:choose>      
</div>
<c:if test="${useDropDownMenu == 'true'}">
  <div class="dropDownMenu">
    <div id="${dropDownMenuId}" class="${dropDownMenuClass}">
      <loose:myCloseButton elementToClose="${dropDownMenuId}"/>
      <br/>
      <jsp:doBody/>
    </div>
  </div>
</c:if>  
