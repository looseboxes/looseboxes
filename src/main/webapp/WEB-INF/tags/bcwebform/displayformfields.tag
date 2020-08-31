<%@tag trimDirectiveWhitespaces="true" description="displays the options and inputs specified" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>

<%@attribute name="dfsForm" required="true" type="com.bc.web.core.form.Form"%>
<%-- @related HTML Form fieldTypes file,text,password,select,hidden,aux --%>
<%@attribute name="fieldTypes" required="true" description="comma separated list of field types to display. All field types are: file,text,password,select,hidden"%>
<%@attribute name="nullables" type="java.lang.Boolean" description="If value is true then all nullable fields are displayed"%>
<%@attribute name="disableSubmitOnChange" required="true"%>

<table class="fullWidth">
   
<jsp:useBean id="formField" scope="page" class="com.bc.web.core.form.FormFieldBean">    
  <jsp:setProperty name="formField" property="parentForm" value="${dfsForm}"/>    
</jsp:useBean>    

<c:forEach var="entry" items="${dfsForm.formRecord}">
  <jsp:setProperty name="formField" property="columnName" value="${entry.key}"/>  
  <jsp:setProperty name="formField" property="columnValue" value="${entry.value}"/>  
  <c:if test="${formField.optional == nullables && fn:contains(fieldTypes, formField.type)}">
    <bwf:displayformfield dfForm="${dfsForm}" formField="${formField}"/>    
  </c:if>
</c:forEach>
    
</table>
