<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Orders search results</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">This page displays the results of searching for orders</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">orders search results</jsp:attribute> 
  <jsp:body> 
    <c:choose>
      <c:when test="${ordersresults != null && not empty ordersresults.pages}">
        
        <loose:searchresultsMessage searchResultsBean="${ordersresults}" 
                                    searchServletPath="${contextURL}/searchorders"/>      

        <table class="table0">
          <thead>
            <tr>
              <th>Order id</th><th>User</th><th>Status</th>
              <th>Order Date</th><th>Required Date</th><th>Options</th>
            </tr>  
          </thead>   
          <tbody>
            <c:forEach var="order" items="${ordersresults.currentPage}">

            <jsp:useBean id="order" class="com.looseboxes.pu.entities.Productorder" scope="page"/>          

            <tr>
              <td><a href="${contextURL}/cart/viewOrder.jsp?orderId=${order.productorderid}">${order.productorderid}</a></td>            
              <td><a href="${contextURL}/cart/orders.jsp?emailAddress=${order.buyer.emailAddress}">${order.buyer.emailAddress}</a></td>            
              <td>${order.orderstatusid.orderstatus}</td>            
              <td>${order.orderDate}</td>            
              <td>${order.requiredDate}</td>            
              <td> 
                <select>
                  <option disabled selected>Select an action</option>      
                  <option onclick="window.location='${contextURL}/cart/viewOrder.jsp?orderId=${order.productorderid}';">view order details</option>      
                  <option onclick="window.location='${contextURL}/cart/orders.jsp?emailAddress=${order.buyer.emailAddress}';">view all user orders</option>      
                  <option onclick="window.location='${contextURL}/cart/paymentdetails.jsp?orderId=${order.productorderid}';">view user payment details</option>      
                  <option onclick="window.location='${contextURL}/cart/paymentoptions.jsp?emailAddress=${order.buyer.emailAddress}';">view user payment options</option>      
                </select>    
              </td>
            </tr>    
          </c:forEach>  
          </tbody>
        </table>
        
        <c:if test="${ordersresults.pageCount > 1}">
          <loose:searchresultsPagelinks searchResultsBean="${ordersresults}" 
                                        searchServletPath="${contextURL}/searchorders"/>      
        </c:if>  
            
      </c:when>    
      <c:otherwise>
        
        <form class="form0" method="post" action="${contextURL}/searchorders">
          <p><input type="submit" value="Search Orders"/></p>      
        </form>
        
      </c:otherwise>
    </c:choose>  
  </jsp:body>
</loose:page>     
