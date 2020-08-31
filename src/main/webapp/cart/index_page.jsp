<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Shopping Cart Pages Index - ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Shopping Cart Pages Index. (List of shopping cart pages)</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">shopping cart pages index list of shopping cart pages</jsp:attribute> 
  <jsp:body>
    <%@include file="/cart/resources/index.xml"%>  
  </jsp:body>
</loose:page>     
