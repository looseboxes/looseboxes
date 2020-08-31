<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Buyer Protection</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">buyer protection guarantee assurance</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">free delivery,return item, get refund</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">${siteName} - Buyer Protection</jsp:attribute>  
  <jsp:body> 
    <img class="freeDeliveryImg" src="${contextURL}/images/transparent.gif"
         alt="free delivery"/>    
    <span style="font-size:2em; font-weight:900; color:green; position:relative; bottom:1em">=</span>
    <span class="layout1 myFontSize1 maxFontWeight" style="position:relative; bottom:2.2em; color:navy">
      Free delivery.     
    </span>
    <br/>
    <img class="moneyBackImg" src="${contextURL}/images/transparent.gif" 
           alt="money back guarantee"/>    
    <span style="font-size:2em; font-weight:900; color:green; position:relative; bottom:1em">=</span>
    <span class="layout1 myFontSize1 maxFontWeight" style="position:relative; bottom:2.2em; color:navy">
      If you are not satisfied, return the item and get your money back.     
    </span>
    <br/>
    <strong>Yes! if you are not satisfied, ${siteName} 
    will refund your full purchase price plus original shipping.</strong>
    <br/><br/>
    <%@include file="/info/resources/buyerProtection.xml"%> 
  </jsp:body>
</loose:page1>     
