<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="mOrderId" value="${orderId==null?param.orderId:orderId}"/>  

<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Shopping Cart - Order no: ${mOrderId}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">shopping cart, view order</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">shopping cart, view order</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    <c:choose>
      <c:when test="${mobile}">Order no: ${mOrderId}</c:when>    
      <c:otherwise>Shopping Cart - Order no: ${mOrderId}</c:otherwise>
    </c:choose>  
  </jsp:attribute>  
  <jsp:body>
    <c:choose>
      <c:when test="${mOrderId == null || mOrderId == ''}">
        <p class="handWriting">
          Please specify an order ID for the cart you would like to view
        </p>
      </c:when>  
      <c:otherwise>
          <jsp:forward page="${pageContext.servletContext.contextPath}/cart/index.jsp?orderId=${mOrderId}"/>
      </c:otherwise>
    </c:choose>  
  </jsp:body>
</loose:page1>     
