<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%--@related ShippingRecord attribute --%>        
<jsp:useBean id="ShippingRecord" class="com.looseboxes.pu.entities.Shippingdetails" scope="request"/>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Shipping Details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Shipping Details</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">shipping details street address, county, city, state,region,country</jsp:attribute> 
  <jsp:body>
    <div class="handWriting">
      <c:choose>
        <c:when test="${ShippingRecord != null && 
                        ShippingRecord.deliveryAddress != null}">
                
          <p>Country: ${ShippingRecord.deliveryAddress.countryid.country}</p>
          <p>Region: ${ShippingRecord.deliveryAddress.regionid.region}</p>
          <p>City: ${ShippingRecord.deliveryAddress.city}</p>
          <p>County: ${ShippingRecord.deliveryAddress.county}</p>
          <p>Street Address: ${ShippingRecord.deliveryAddress.streetAddress}</p>
          <p>Post Code: ${ShippingRecord.deliveryAddress.postalCode}</p>
          
<%--@related shippingdetails.jsp shippingdetailsId request attribute --%>        
          <p>
            <a href="${contextURL}/editshipping?orderId=${ShippingRecord.productorderid.productorderid}&shippingdetailsid=${ShippingRecord.shippingdetailsid}">Click here</a> to edit shipping details.        
          </p>
        </c:when>
        <c:otherwise>
            
          Shipping details not available for the selected order.
          
<%-- //@related shippingdetails.jsp orderId request attribute --%>        
          <p>
            <a href="${contextURL}/insertShipping?orderId=${orderId}">Click here</a> to add shipping details.
          </p>
          
        </c:otherwise>
      </c:choose>
    </div>    
  </jsp:body>
</loose:page>     

