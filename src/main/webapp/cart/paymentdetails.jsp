<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="mOrderId" value="${orderId==null?param.orderId:orderId}"/>  

<jsp:useBean id="Paymentselector" class="com.looseboxes.web.components.SelectorBean<com.looseboxes.pu.entities.Payment>" scope="request">
  <jsp:setProperty name="Paymentselector" property="tableName" value="payment"/>    
  <jsp:setProperty name="Paymentselector" property="columnName" value="productorderid"/>    
  <jsp:setProperty name="Paymentselector" property="columnValue" value="${mOrderId}"/>    
</jsp:useBean>
<%-- c:set var="mOrder" value="${Paymentselector.singleResult}"/ --%>

<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Order no: ${mOrderId} - payment details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">order, payment details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">order, payment details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">
    <c:choose>
      <c:when test="${mobile}">Payment Details</c:when>    
      <c:otherwise>Order no: ${mOrderId} - Payment Details</c:otherwise>
    </c:choose>  
  </jsp:attribute>  
  <jsp:body>
    <c:choose>
      <c:when test="${mOrderId == null || mOrderId == ''}">
        <p class="handWriting">
          Please specify an <tt>order ID</tt> for which payment details will be displayed
        </p>
      </c:when>  
      <c:otherwise>
        <jsp:useBean id="Payment" class="com.looseboxes.pu.entities.Payment" scope="request"/>
        <c:if test="${Payment.paymentStatus != null}">
          <c:set var="pdPaymentStatus" value="${Payment.paymentstatusid.paymentstatus}"/>  
        </c:if>
        <ul>
          <li>Order id: ${Payment.productorderid.productorderid}</li>    
          <li>Payment status: ${pdPaymentStatus}</li>    
        </ul>
      </c:otherwise>
    </c:choose>  
  </jsp:body>
</loose:page1>     
