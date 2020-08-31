<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Mail Shopping Cart</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Mail shopping cart</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">mail shopping cart</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Mail Shopping Cart</jsp:attribute>  
  <jsp:body>
      
    <c:choose>
      <c:when test="${orderId != null}">
        <c:set var="mcOrderid" value="${orderId}"/>    
      </c:when>    
      <c:when test="${param.orderId != null}">
        <c:set var="mcOrderid" value="${param.orderId}"/>    
      </c:when>    
      <c:otherwise>
        <c:set var="mcOrderid" value="${User.shoppingCart.orderId}"/>    
      </c:otherwise>
    </c:choose>  

    <p>Asterixed (<span class="boldRed">*</span>) fields are required</p>
    <form id="mailcartFormId" class="form0 background0" method="post" action="${contextURL}/mailCart">
      <table>
        <tr>
          <td><p><label>To: </label><span class="boldRed">*</span></p></td>  
          <td>
            <p><input size="32" maxlength="64" type="text" name="recipient" value="support@buzzwears.com"/></p>    
          </td>      
        </tr>    
        <tr>
          <td><p><label>Order ID: </label></p></td>  
          <td>
            <p><input size="32" maxlength="32" type="text" name="orderId" value="${mcOrderid}"/></p>
          </td>      
        </tr>    
        <tr>
          <td>
            <input type="submit" value="Send Mail"/>
          </td>
          <td></td>
        </tr>    
      </table>    
    </form>
    
  </jsp:body>
</loose:page1>     
