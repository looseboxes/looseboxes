<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Updating Shipping Details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Updating Shipping Details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">why update shipping details, how to update shipping details</jsp:attribute> 
  <jsp:body>
    <br/>  
    <loose:menucontent contentClass="contentBox2" contentId="updateShippingdetails" display="true" 
    titleClass="header1Layout curvedTop" title="Updating Shipping Details" titleId="updateShippingdetailsHeader">
      <strong>Why update shiping details</strong>        
      <%@include file="/info/resources/seller_why_updateshipping.xml"%>
      <br/>
      <strong>How to update shiping details</strong>        
      <%@include file="/info/resources/seller_howto_updateshipping.xml"%>
      <br/>
      <a href="${contextURL}/cart/myOrders.jsp">Click here to view the <tt>orders</tt> table</a>
    </loose:menucontent>  
  </jsp:body>
</loose:page>     
