<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<c:set var="mEmailAddress" value="${emailAddress==null?param.emailAddress:emailAddress}"/>

<loose:page>
  <jsp:attribute trim="true" name="pageTitle">User Orders - ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">User Orders</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">User Orders</jsp:attribute> 
  <jsp:body> 
    <c:choose>
      <c:when test="${!User.loggedIn}">
        <p class="handWriting">
          You must be logged-in to view the requested resource.
        </p>  
        <%@include file="/WEB-INF/jspf/loginForm.jspf"%>          
      </c:when>    
      <c:when test="${mEmailAddress != User.emailAddress && !User.admin}">
        <p class="handWriting">
          You are not authorized to perform the requested operation
        </p>  
      </c:when>    
      <c:otherwise>
        <br/>
        <jsp:useBean id="UserOrders" scope="request" class="com.looseboxes.web.components.UserOrders">
          <jsp:setProperty name="UserOrders" property="emailAddress" value="${mEmailAddress}"/>  
        </jsp:useBean>  
        <c:choose>
          <c:when test="${UserOrders.records != null && not empty UserOrders.records}">
            <div class="centeredBlock">These are the orders you have placed</div>    
            <loose:myorders ordersBean="${UserOrders}"/>
          </c:when>  
          <c:otherwise>
            <div class="centeredBlock">You have not placed any orders</div>    
          </c:otherwise>
        </c:choose>
        <br/><br/>
        <jsp:useBean id="SellerOrders" scope="request" class="com.looseboxes.web.components.SellerOrders">
          <jsp:setProperty name="SellerOrders" property="emailAddress" value="${mEmailAddress}"/>  
        </jsp:useBean>  
        <c:if test="${SellerOrders.records != null && not empty SellerOrders.records}">
          <div class="centeredBlock">These are the orders others placed for your items</div>    
          <loose:myorders ordersBean="${SellerOrders}"/>
        </c:if>
      </c:otherwise>
    </c:choose>  
  </jsp:body>
</loose:page>     
