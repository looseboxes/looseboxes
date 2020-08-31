<%@tag trimDirectiveWhitespaces="true" description="Renders the specified bean's details in table format" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="ordersBean" type="com.looseboxes.web.components.UserOrders" required="true"%>
<c:if test="${ordersBean.records != null}">
  <table class="table0">
    <thead>
      <tr>
        <th>Order ID</th><th>Amount</th><th>Order Date</th>        
        <th>Status</th><th>Actions</th>        
      </tr>    
    </thead>  
    <tbody>
        
    <jsp:useBean id="PostOrderHandler" scope="request" class="com.looseboxes.web.servlets.PostOrderHandler"/>
        
    <c:forEach var="moOrder" items="${ordersBean.records}">
        
      <jsp:useBean id="moOrder" class="com.looseboxes.pu.entities.Productorder" scope="page"/>  
        
      <tr>
        <td>${moOrder.productorderid}</td>
        <td>${moOrder.payment.paymentAmount}</td>
        <td>${moOrder.orderDate}</td>  
        <td>${moOrder.orderstatusid.orderstatus}</td>
        <td class="mySmaller">
          <form class="form0" method="post" action="${contextURL}/poh">
            <input type="hidden" name="orderId" value="${moOrder.productorderid}"/>  
            <select name="axt">
              <option selected disabled>select an action</option>
              <c:forEach var="actionType" items="${PostOrderHandler.actionTypes}">
                <option value="${actionType.key}">${actionType.label}</option>
              </c:forEach>    
            </select> 
            <input type="submit" value="go"/>  
          </form>  
        </td>  
      </tr>
    </c:forEach>    
    </tbody>
  </table>  
</c:if>
