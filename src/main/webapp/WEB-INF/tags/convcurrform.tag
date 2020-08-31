<%@tag trimDirectiveWhitespaces="true" description="form for converting between currencies" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="formName" required="true"%> 
<%@attribute name="currencyCodes" required="true" type="java.util.Collection"%> 
<%@attribute name="ajaxResultsElementId" required="true"%> 
    
  <form class="form0" name="${formName}" method="post" action="${contextURL}/convcurr">
    From: 
    <select name="fromCurrency" size="1">
<%-- @related_32 empty string will be reflected as null --%>  
      <option selected disabled value="">Select Currency</option>
      <c:forEach var="currencyCode" items="${currencyCodes}">
        <option onclick="myAjax.get('${formName}', '${ajaxResultsElementId}', '${contextURL}/ajax?type=convcurr&amp;')"
                value="${currencyCode}">${currencyCode}</option>
      </c:forEach>
    </select>  
    &emsp;
    To: 
    <select name="toCurrency" size="1">
<%-- @related_32 empty string will be reflected as null --%>  
      <option selected disabled value="">Select Currency</option>
      <c:forEach var="currencyCode" items="${currencyCodes}">
        <option onselect="myAjax.get('${formName}', '${ajaxResultsElementId}', '${contextURL}/ajax?type=convcurr&amp;')"
                value="${currencyCode}">${currencyCode}</option>
      </c:forEach>
    </select>  
    &emsp;  
    <input type="submit" class="myBtnLayout" value="go"/>  
    <br/>
    <div id="${ajaxResultsElementId}"></div>
  </form>    
        
