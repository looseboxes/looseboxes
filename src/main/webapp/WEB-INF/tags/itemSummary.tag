<%@tag trimDirectiveWhitespaces="true" description="displays specified product details in a custom format, NOTE: ends with the specified separator" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="isSeparator"%>
<%@attribute name="isAvailability"%>
<%@attribute name="isProductName"%>
<%@attribute name="isPrice"%>
<%@attribute name="isDiscount"%>

<%-- use the user's preferred locale, over the request's preferred locale --%>  
<fmt:setLocale value="${User.locale}" scope="request"/>  

<c:if test="${isAvailability != null}">
  <small>${isAvailability}</small>${isSeparator}
</c:if>
<c:if test="${isProductName != null}">
  <span class="wrapped"><b>${isProductName}</b></span>${isSeparator}  
</c:if>
<c:if test="${isPrice != null}">
  <c:choose>
    <c:when test="${isDiscount == null}">
        <fmt:formatNumber maxFractionDigits="2" currencyCode="${User.currency.currencyCode}" value="${isPrice}" type="currency"/>
    </c:when>  
    <c:otherwise>
      <span class="strike"><fmt:formatNumber maxFractionDigits="2" currencyCode="${User.currency.currencyCode}" value="${isPrice}" type="currency"/></span>&nbsp;
      <span class="boldRed"><fmt:formatNumber maxFractionDigits="2" currencyCode="${User.currency.currencyCode}" value="${isPrice - isDiscount}" type="currency"/></span>
    </c:otherwise>  
  </c:choose>
  ${isSeparator}  
</c:if>  
