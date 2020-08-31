<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Promos Terms and Conditions - ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Promos Terms and Conditions</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">promos terms and conditions, posts subject to verification, maximum payable, right to disqualify, right to remove, may make ammendments, may terminate</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">${siteName} Promos - Terms and Conditions</jsp:attribute>  
  <jsp:body>
    <ul class="spacedList">
      <li>All posts (uploads) are subject to verification by the <i>${siteName}</i> team before any payment will be made.</li>
      <li>A maximum of NGN 100,000 is payable per month no matter how many items are uploaded by a user.</li>
      <li><b>${siteName}</b> reserves the right to disqualify any user from partaking in this promo/offer</li>
      <li><b>${siteName}</b> reserves the right to remove any posts (uploads) from this promo/offer.</li>
      <li><b>${siteName}</b> may make ammendments to these terms and conditions at any time and without prior
      notice to users. The only requirement being that such ammendments must be displayed on this page.</li>
      <li><b>${siteName}</b> may terminate this promo/offer at any time and without prior notice to all users but not
      before clearing all outstanding payments.</li>
    </ul>
  </jsp:body>
</loose:page1>     
