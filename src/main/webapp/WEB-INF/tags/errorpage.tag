<%@tag trimDirectiveWhitespaces="true" description="default error page script" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="displayTopBanner"%>
<%@attribute name="pageTitle" required="true"%>
<%@attribute name="pageDescription" required="true"%>

<loose:page2 pageTitle="${pageTitle}" 
             pageKeywords="${pageKeywords}" 
             pageDescription="${pageDescription}" 
             displayTopBanner="${displayTopBanner}"
             displayPageBottomSponsoredItems="true">
    
  <jsp:attribute trim="true" name="pageHeadInclude">
    <meta name="robots" content="noindex, nofollow"/>
  </jsp:attribute>  
    
  <jsp:body> 
    <div class="handWriting centeredContent">
        
      <jsp:doBody/>    
      <br/>
      <p>
        Click <a href="${contextURL}/products/searchresults.jsp">here</a>
        to return to <i><a href="${contextURL}/index.jsp">${siteName}</a></i>.
      </p>
      <i>Thanks<br/>
      ${siteName}</i>  
    </div>
  </jsp:body>
</loose:page2>     
