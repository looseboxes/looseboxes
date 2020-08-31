<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">View Product Variants</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">View the variants of selected product </jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">view product variants</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">View Product Variants</jsp:attribute>  
  <jsp:body> 
      
    <jsp:useBean id="pvVariants" class="com.looseboxes.web.components.Productvariants"/>
    
    <c:set var="pvPid" value="${param.productid != null ? param.productid : productid}"/>
      
    <jsp:setProperty name="pvVariants" property="productid" value="${pvPid}"/>
    
    <form class="form0 background0" method="post" action="${contextURL}/products/productvariants.jsp">
      <p><label>Enter a product ID to view its variants:</label> <input type="text" name="productid" value="${pvPid}"/></p>
      <p><input type="submit" value="View Variants"/></p>
    </form>
    
    <form class="form0 background0" method="post" action="${contextURL}/edit">
        <p><b>Product ID: ${pvVariants.product.productid}</b></p>
        <input type="hidden" name="productid" value="${pvVariants.product.productid}"/>
        <p><label>Name:</label> <input type="text" name="productName" value="${pvVariants.product.productName}"/></p>
        <p><label>Price:</label> <input type="text" name="price" value="${pvVariants.product.price}"/></p>
        <p><label>Discount:</label> <input type="text" name="discount" value="${pvVariants.product.discount}"/></p>
        <p><label style="color:red">Discounted Price: ${pvVariants.product.price-pvVariants.product.discount}<small>price - discount</small></label></p>
        <p><label>Manufacture Date:</label> <input type="text" name="dateOfManufacture" value="${pvVariants.product.dateOfManufacture}"/></p>
        <p><label>Min Order Qty:</label> <input type="text" name="minimumOrderQuantity" value="${pvVariants.product.minimumOrderQuantity}"/></p>
        <p><label>Keywords:</label> <input type="text" name="keywords" value="${pvVariants.product.keywords}"/></p>
        <p><label>Description:</label> <input type="text" name="description" value="${pvVariants.product.description}"/></p>
        <p><input type="submit" value="Submit"/></p>
    </form>
    <br/>
    <b>Number of variants: ${fn:length(pvVariants.productvariants)}</b>
    <c:forEach var="Productvariant" items="${pvVariants.productvariants}">
      <form class="form0 background0" method="post" action="${contextURL}/editvariant">
        <p>
          <b>
            Product ID: ${pvVariants.product.productid}&emsp;
            Unit/Variant ID: ${Productvariant.productvariantid}
          </b>
        </p>
        <input type="hidden" name="productid" value="${Productvariant.productid.productid}"/>
        <input type="hidden" name="productvariantid" value="${Productvariant.productvariantid}"/>
        <p><label>Name:</label> <input type="text" name="productName" value="${Productvariant.productid.productName}"/></p>
        <p><label>In Stock:</label> <input type="text" name="quantityInStock" value="${Productvariant.quantityInStock}"/></p>
        <p><label>Size:</label> <input type="text" name="productSize" value="${Productvariant.productSize}"/></p>
        <p><label>Color:</label> <input type="text" name="color" value="${Productvariant.color}"/></p>
        <p><label>Image 1:</label> <input type="text" name="image1" value="${Productvariant.image1}"/></p>
        <p><label>Weight:</label> <input type="text" name="weight" value="${Productvariant.weight}"/></p>
        <p><input type="submit" value="Submit"/></p>
      </form>
    </c:forEach>
      
  </jsp:body>
</loose:page1>     
