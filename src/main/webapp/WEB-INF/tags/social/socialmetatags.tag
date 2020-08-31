<%-- 
    Document   : twittercard
    Created on : Jul 22, 2016, 11:05:25 AM
    Author     : Josh
--%>
<%@tag trimDirectiveWhitespaces="true" 
description="Series of meta tags used as facebook open graph meta tags @see https://developers.facebook.com/docs/sharing/webmasters#markup, twitter cards @see https://dev.twitter.com/cards/overview" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="smItem" type="com.looseboxes.pu.entities.Product" required="true"%>
<%@attribute name="smItemVariant" type="com.looseboxes.pu.entities.Productvariant" required="false"%>

<fmt:formatNumber type="currency" currencyCode="${User.currency.currencyCode}" 
                  var="smDisplayPrice" maxFractionDigits="2"   
                  value="${smItem.discount == null? smItem.price : (smItem.price - smItem.discount)}"/> 

<c:if test="${smItemVariant != null}">
  <%--c:url var="smItemImageUrl" value="${contextURL}/viewimg?vid=${smItemVariant.productvariantid}&image=image1"/--%>
  <c:url var="smItemImageUrl" value="${localURL}${smItemVariant.image1}"/>
</c:if>

<%-- Facebook Open Graph--%>

<%--meta property="og:app_id" content="1705255026405225"/--%>
<meta property="fb:app_id" content="1705255026405225"/>

<meta property="og:site_name" content="${siteName}"/>
<meta property="og:type" content="product"/>
<meta property="og:locale" content="en_US"/>
<meta property="og:url" content="${contextURL}/prouducts/${smItem.productid}.jsp"/>
<meta property="og:title" content="${smItem.productsubcategoryid.productsubcategory} @${siteName}"/>
<meta property="og:description" content="${smItem.productsubcategoryid.productsubcategory} - ${smItem.productName} - ${smDisplayPrice}"/>

<c:if test="${smItemVariant != null}">
  <meta property="og:image" content="${smItemImageUrl}"/>
</c:if>

<meta property="product:price:amount" content="${smItem.price}"/> 
<meta property="product:price:currency" content="${User.currency.currencyCode}"/> 
<c:if test="${smItem.discount != null}">
  <meta property="product:sale_price:amount" content="${smItem.price - smItem.discount}"/> 
  <meta property="product:sale_price:currency" content="${User.currency.currencyCode}"/>
</c:if>
  
<%-- Twitter Card --%>

<meta name="twitter:card" content="product"/>
<meta name="twitter:site" content="@${twitterUsername}"/>
<meta name="twitter:title" content="${smItem.productsubcategoryid.productsubcategory} @${siteName}"/>
<meta name="twitter:description" content="@${twitterUsername} #${smItem.productsubcategoryid.productsubcategory} - ${smItem.productName} - ${smDisplayPrice} - ${contextURL}/prouducts/${smItem.productid}.jsp"/>
<c:if test="${smItemVariant != null}">
  <meta name="twitter:image" content="${smItemImageUrl}"/>
  <meta name="twitter:image:alt" content="${smItem.productName}"/>
</c:if>
  
<meta name="twitter:label1" content="Price">  
<meta name="twitter:data1" content="${smDisplayPrice}">
<c:if test="${smItemVariant != null}">
  <meta name="twitter:label2" content="Color">
  <meta name="twitter:data2" content="${smItemVariant.color}">
</c:if>


