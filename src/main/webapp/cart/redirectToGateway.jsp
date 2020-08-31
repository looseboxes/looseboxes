<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="robots" content="noindex, nofollow"/>
    <title>Preparing Payment</title>
  </head>
  <body onload="document.forms[0].submit()">
    <h3>... Please wait preparing payment</h3>
    <noscript>
      <h3>Please click the Continue button to proceed.</h3>
    </noscript>
    <form name="payForItem" method="post" action="${User.shoppingCart.paymentRequest.endpoint}">

      <c:forEach var="pair" items="${User.shoppingCart.paymentRequest.parameters}">
        <input type="hidden" name="${pair.key}" value="${pair.value}"/>    
      </c:forEach>  

      <noscript>
        <input type="submit" value="Continue"/>
      </noscript>  

    </form>  
  </body>
</html>
