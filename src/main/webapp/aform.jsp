<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>

<jsp:useBean id="CurrentUpdateForm" class="com.looseboxes.web.components.forms.InsertProductForm"/>
<c:set var="mDesc" value="${CurrentUpdateForm.table} ${CurrentUpdateForm.type.label} Form - Basic Details"/>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} Form</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">${mDesc}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">form,basic details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    <c:choose>
      <c:when test="${mobile}">${CurrentUpdateForm.type.label}&nbsp;Form</c:when>    
      <c:otherwise>
        ${siteName}&nbsp;${mDesc}
      </c:otherwise>
    </c:choose>  
  </jsp:attribute>  
  <jsp:attribute name="pageAfterBodyInclude">
    <script type="text/javascript" src="${contextURL}/resources/datetimeselector.js"></script>
    <c:if test="${!CurrentUpdateForm.hideCaptcha}">
      <script type="text/javascript">
        looseboxes.loadImage("${contextURL}/captcha", 250, 75);  
      </script>
    </c:if>
  </jsp:attribute>      
  <jsp:body>

    <%--@todo @include file="/WEB-INF/jspf/uploadOrRequest.jspf"--%>  
      
    <form method="post" class="form0 background0 fullWidth" 
          enctype="multipart/form-data" 
          name="${CurrentUpdateForm.table}form" id="${CurrentUpdateForm.table}formId"
          action="${contextURL}${CurrentUpdateForm.action}" onsubmit="return myFormHandler.validate()">

      <bwf:defaultform mfTargetForm="${CurrentUpdateForm}"/>    
      
    </form>    
      
  </jsp:body>
  
</loose:page1>  
