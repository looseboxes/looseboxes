<%@tag trimDirectiveWhitespaces="true" description="display a specific form field" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>

<%@attribute name="dfForm" required="true" type="com.bc.web.core.form.Form"%>
<%@attribute name="formField" required="true" type="com.bc.web.core.form.FormField"%>

<%-- For moible devices the input size should not be larger than the screen width --%>    
<c:set var="formFieldSize" value="${mobile?formField.size/2:formField.size}"/>    
<c:set var="optionsMaxLen" value="${mobile?18:36}"/>

<c:choose>
  <c:when test="${formField.type == 'select'}">

  <tr>
    <td class="formColumn1">
      ${formField.label}<c:if test="${!formField.optional}"><font color="red"> <b> * </b></font></c:if>
    </td>
    <td class="formColumn2">
<%--@debug            
      <c:forEach var="pair" items="${formField.choices}">
        ${pair.key}    =    ${pair.value}    ,    ${formField.value}<br/>
      </c:forEach>
--%>
<%--@debug
<div style="color:red; font-size:0.75em">    
FormField.name: ${formField.name}, FormField.value: ${formField.value}, FormField.choices:<br/>
  <c:forEach var="pair" items="${formField.choices}">
    Choice.key: ${pair.key}, Choice.value: ${pair.value}
    <br/>
  </c:forEach>    
</div>
--%>
    
    <select name="${formField.name}" id="${formField.id}"
    class="formInput" size="1">

      <option disabled>
        <c:choose>
          <c:when test="${mobile}">
            Select <bwf:truncate ellipsis=".." maxLength="${optionsMaxLen-4}" target="${formField.label}"/>
          </c:when>
          <c:otherwise>Select ${formField.label}</c:otherwise>
        </c:choose>  
      </option>

      <c:forEach var="pair" items="${formField.choices}">
        <c:choose>
          <c:when test="${pair.key.toString().equalsIgnoreCase(formField.value)}">
            <option value="${pair.value}" selected>
              <bwf:truncate ellipsis=".." maxLength="${optionsMaxLen}" target="${pair.key}"/>  
            </option>
          </c:when>
          <c:otherwise>
            <option value="${pair.value}">
              <bwf:truncate ellipsis=".." maxLength="${optionsMaxLen}" target="${pair.key}"/>  
            </option>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </select>
    </td>
    <td class="hideHandheld formColumn3">
      <div id="${formField.name}Message">
        &nbsp;&nbsp;&nbsp;
        <small><bwf:displayfieldmessage formHandler="${dfForm}" formField="${formField}"/></small>
      </div>
    </td>
  </tr>

  </c:when>  
  <c:otherwise>

  <tr>
    <td class="formColumn1">
      ${formField.label}<c:if test="${formField.optional == 'false'}"><font color="red"> <b> * </b></font></c:if>
    </td>
    <td class="formColumn2">
      <c:choose>
        <c:when test="${formField.name == 'password' || formField.type == 'password'}">

          <input type="hidden" name="password" id="hiddenPasswordId" value="${formField.value}" />

          <input type="${formField.type}" 
            name="${formField.name}" id="${formField.id}"
            size="${formFieldSize}" maxlength="${formField.maxLength}"
            value="${formField.value}"
            onchange="myFormHandler.updateAttribute('${formField.id}', 'hiddenPasswordId', 'value')"/>
        </c:when>
        <c:when test="${formField.maxLength <= 255}">

          <input type="${formField.type}" class="formInput" name="${formField.name}" id="${formField.id}"
            size="${formFieldSize}" maxlength="${formField.maxLength}" value="${formField.value}"
            onchange="myFormHandler.validateInput('${formField.id}', '${formField.name}AjaxMessage', 
            '${contextURL}/ajax?type=validate&amp;tableName=${dfForm.tableName}&amp;', '${disableSubmitOnChange}')"/>

          <%-- If its a date/time/timestamp, display a date chooser --%>          
          <c:if test="${FormField.dateType || FormField.timeType || FormField.timestampType}">
            <a href="javascript:show_calendar('document.${dfForm.tableName}form.${formField.name}', document.${dfForm.tableName}form.${formField.name}.value);">
              <img class="calendarImg" src="${contextURL}/images/transparent.gif" border="0" alt="Click to select date/time"/>
            </a>    
          </c:if>
        </c:when>
        <c:otherwise>
          <textarea rows="4" class="formInput" name="${formField.name}" id="${formField.id}"
            onchange="myFormHandler.validateInput('${formField.id}', '${formField.name}AjaxMessage', 
            '${contextURL}/ajax?type=validate&amp;tableName=${dfForm.tableName}&amp;', '${disableSubmitOnChange}')">
            ${formField.value}
          </textarea>
        </c:otherwise>
      </c:choose>
    </td>
    <td class="hideHandheld formColumn3">
      <div id="${formField.name}Message">&nbsp;&nbsp;&nbsp;
        <small><bwf:displayfieldmessage formHandler="${dfForm}" formField="${formField}"/></small>
      </div>
    </td>
  </tr>
  <tr>
    <td colspan="3" id="${formField.name}AjaxMessage"></td>
  </tr>
    <c:if test="${formField.name == 'password' || formField.type == 'password'}">
<%-- Add another row for cofirmPassword just after password --%>
      <tr>
        <td class="formColumn1">
          Confirm Password <font color="red"> <b> * </b></font>
        </td>
        <td class="formColumn2">
          <input type="${formField.type}"
            name="confirmPassword" id="confirmPasswordId"
            size="${formFieldSize}" maxlength="${formField.maxLength}"
            value="${formField.value}"
            onchange="myFormHandler.matchValues('hiddenPasswordId', 'confirmPasswordId', '${formField.name}AjaxMessage', 'confirmPasswordAjaxMessage')"
          />
        </td>
        <td class="hideHandheld formColumn3">
          <div id="confirmPasswordMessage">&nbsp;&nbsp;&nbsp;<small>Re-enter your password</small></div>
        </td>
      </tr>
      <tr>
        <td colspan="3" id="confirmPasswordAjaxMessage"></td>
      </tr>
    </c:if>

  </c:otherwise>  
</c:choose>
