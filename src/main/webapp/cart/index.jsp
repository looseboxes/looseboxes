<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="User" class="com.looseboxes.web.components.UserBean" scope="session"/>
<c:choose>
  <c:when test="${User.name == null || User.name == ''}">
    <c:set var="iName" value="My Shopping Cart"/>
  </c:when>  
  <c:otherwise>
    <c:set var="iName" value="${User.name}'s Shopping Cart"/>  
  </c:otherwise>
</c:choose>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle"><c:out value="${iName}"/></jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription"><c:out value="${iName}"/></jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords"><c:out value="${iName}"/></jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    <c:choose>
      <c:when test="${mobile}">Shopping Cart</c:when>    
      <c:otherwise><c:out value="${iName}"/></c:otherwise>
    </c:choose>  
  </jsp:attribute>  
  <jsp:body>
    <c:choose>
      <c:when test="${User.shoppingCart == null || empty User.shoppingCart.items}">
        <p class="handWriting">
          There are currently no items in your shopping cart.
          <br/><br/>
          Use the <tt>Add to Cart</tt> or <tt>Buy Now</tt> buttons to add items 
          to your Shopping Cart.
        </p>
        <br/>
        <a href="${contextURL}/products/searchresults.jsp">&emsp;Back</a>
      </c:when>  
      <c:otherwise>
        <%@include file="/WEB-INF/jspf/shoppingcart.jspf"%> 
      </c:otherwise>
    </c:choose>  
        
  </jsp:body>
</loose:page1>     
