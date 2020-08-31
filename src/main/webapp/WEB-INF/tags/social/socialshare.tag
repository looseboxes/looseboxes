<%@tag trimDirectiveWhitespaces="true" description="holds icons for sharing on fb, twitter etc" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="ssItemRecord" required="true" type="com.looseboxes.pu.entities.Product"%>
<%@attribute name="ssOwnerEmail"%>
<%@attribute name="ssLargeIcons"%>

<%-- use the user's preferred locale, over the request's preferred locale --%>  
<fmt:setLocale value="${User.locale}" scope="page"/>  

<c:set var="ssProductId" value="${ssItemRecord.productid}"/>

<c:set var="mailIcon" value="${ssLargeIcons?'mailImg':'mailImg2'}"/>
<c:set var="fbIcon" value="${ssLargeIcons?'facebookImg':'facebookImg2'}"/>
<c:set var="twtIcon" value="${ssLargeIcons?'twitterImg':'twitterImg2'}"/>
<c:set var="editIcon" value="${ssLargeIcons?'editImg':'editImg2'}"/>

<%--@todo a title="mail this item to a friend" class="borderless"
href="${contextURL}/tosendmail?mt=notification&amp;ids=${ssProductId}">
  <img class="${mailIcon}" alt="mail" src="${contextURL}/images/transparent.gif"/>
</a --%>
  
<c:url value="http://www.facebook.com/sharer.php" var="fbShareUrl">
  <c:param name="u" value="${contextURL}/products/${ssProductId}.jsp"/>    
</c:url>
<a title="share this item on facebook" class="borderless" 
  href="${fbShareUrl}" rel="nofollow" target="_blank">
  <img class="${fbIcon}" alt="facebook" src="${contextURL}/images/transparent.gif"/>
</a>

<c:if test="${ssItemRecord.price != null}">
  <c:choose>
    <c:when test="${ssItemRecord.discount == null}">
      <fmt:formatNumber maxFractionDigits="2" currencyCode="${User.currency.currencyCode}" var="fmtPrice" value="${ssItemRecord.price}" type="currency"/>    
    </c:when>  
    <c:otherwise>
      <fmt:formatNumber maxFractionDigits="2" currencyCode="${User.currency.currencyCode}" var="fmtPrice" value="${ssItemRecord.price - ssItemRecord.discount}" type="currency"/>
    </c:otherwise>  
  </c:choose>
</c:if>

<c:set var="twtStatusUrl" value="${contextURL}/products/${ssItemRecord.productid}_${ssItemRecord.productName}.jsp"/>


<c:url value="http://twitter.com/home" var="twtShareUrl">
    <c:choose>
        <c:when test="${twitterUsername != null}">
            <c:param name="status" value="@${twitterUsername} ${ssItemRecord.productName} ${fmtPrice} ${twtStatusUrl}"/>    
        </c:when>
        <c:otherwise>
            <c:param name="status" value="${ssItemRecord.productName} ${fmtPrice} ${twtStatusUrl}"/>    
        </c:otherwise>
    </c:choose>  
</c:url>
<a title="tweet this item" class="borderless" 
  href="${twtShareUrl}" rel="nofollow" target="_blank">
  <img class="${twtIcon}" alt="twitter" src="${contextURL}/images/transparent.gif"/>
</a>

<%--
<a title="chat with us" class="borderless" 
   href="${contextURL}/quickchat?type=cs" target="_blank" >
  <img class="liveChat3Img" alt="chat" src="${contextURL}/images/transparent.gif"/> 
</a>
--%>

<c:if test="${User.admin || User.details.emailAddress == ssOwnerEmail}">
  <a  title="edit this item" class="borderless"
  href="${contextURL}/edit?id=${ssProductId}">
    <img class="${editIcon}" alt="edit" src="${contextURL}/images/transparent.gif"/>
  </a>
  <a  title="delete this item" class="borderless"
  href="${contextURL}/delete?id=${ssProductId}">
    <c:choose>
      <c:when test="${ssLargeIcons == 'true'}"><font class="boldRed myFontSize3">x</font></c:when>  
      <c:otherwise><font class="boldRed">X</font></c:otherwise>
    </c:choose>  
  </a>
  <a  title="change category/type" class="borderless"
  href="${contextURL}/user/quickeditproduct.jsp?productId=${ssProductId}">
    <img class="${editIcon}" alt="edit" src="${contextURL}/images/transparent.gif"/>
  </a>
</c:if>
