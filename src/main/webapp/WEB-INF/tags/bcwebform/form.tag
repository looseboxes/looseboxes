<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="formBean" required="true" type="com.bc.web.core.form.Form"%>

<bwf:displayallformfields dafTargetForm="${formBean}"/>
    
<jsp:useBean id="formField" class="com.bc.web.core.form.FormFieldBean" scope="page">       
  <jsp:setProperty name="formField" property="parentForm" value="${formBean}"/>
</jsp:useBean>    
<c:forEach var="fieldName" items="${formBean.fieldNames}">
  <jsp:setProperty name="formField" property="columnName" value="${fieldName}"/>        
<%--  @debug Column: ${fieldName}, Form: ${formField.form}<br/>--%>
  <c:if test="${formField.form != null}">
    <b>Add ${formField.label}</b><br/>
    <bwf:displayallformfields dafTargetForm="${formField.form}"/>
  </c:if>
</c:forEach>  
