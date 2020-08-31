<%@tag trimDirectiveWhitespaces="true" description="template for content which could be closed or minimized" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="title" required="true"%>
<%@attribute name="titleId"%>
<%@attribute name="titleClass"%>
<%@attribute name="contentId"%>
<%@attribute name="contentClass"%>
<%@attribute name="display"%>
<c:choose>
  <c:when test="${display == 'true'}">
    <c:set var="buttonText" value="-"/>
    <c:set var="contentStyle" value="text-align:justify"/>
  </c:when>  
  <c:otherwise>
    <c:set var="buttonText" value="+"/>
    <c:set var="contentStyle" value="text-align:justify; display:none"/>
  </c:otherwise>  
</c:choose>  
<table class="${titleClass}">
  <tr>
    <td>&nbsp;</td>    
    <td id="${titleId}" style="width:100%; text-align:center">${title}</td>    
    <td>
      <input type="button" value="${buttonText}" class="boldRed curvedTop toggleBtnLayout borderless" id="${titleId}control" onclick="myDropDownMenu.toggle('${contentId}', '${titleId}control', true);"/>
    </td>    
  </tr> 
</table>  
<div id="${contentId}" class="${contentClass}" style="${contentStyle}">
  <jsp:doBody/>
</div>    

