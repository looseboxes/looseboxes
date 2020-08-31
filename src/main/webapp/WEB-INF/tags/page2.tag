<%@tag trimDirectiveWhitespaces="true" description="has only topbanner, myMessage and centerColumn " pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="pageTitle" description="used as the page title" required="true"%>
<%@attribute name="pageDescription" description="used to create meta description tag" required="true"%>
<%@attribute name="pageKeywords" description="used to create meta keywords tag" required="true"%>
<%@attribute name="pageHeadInclude" fragment="true"%>
<%@attribute name="pageAfterBodyInclude" fragment="true"%>
<%@attribute name="displayTopBanner"%>
<%@attribute name="displayPageBottomSponsoredItems" required="false" description="default is 'true'"%>

<c:if test="${displayTopBanner == null}">
  <c:set var="displayTopBanner" value="true"/>
</c:if>

<c:if test="${displayPageBottomSponsoredItems == null}">
  <c:set var="displayPageBottomSponsoredItems" value="true"/>
</c:if>

<%@include file="/WEB-INF/jspf/doctype.jspf"%>      
<html>
<head>
  <%@include file="/WEB-INF/jspf/defaultHeadContents.jspf"%>    
  <title>${pageTitle}</title>
  <meta name="description" content="${pageDescription}"/>
  <meta name="keywords" content="${pageKeywords}"/>
  <c:if test="${pageHeadInclude != null}">
    <jsp:invoke fragment="pageHeadInclude"/>
  </c:if>
</head>

<body class="borderless">
  <%--script type="text/javascript" src="${contextURL}/resources/facebook/fbsdksetup.js"></script--%>  
  <div id="wrapper">
    <c:if test="${displayTopBanner}">
      <div id="header">  
        <%@include file="/WEB-INF/jspf/topbanner.jspf"%>
      </div>  
    </c:if>  
    <div id="content" class="borderless">

      <div class="padded borderless justifiedContent">

        <br/>
        <jsp:doBody/>
        
        <c:if test="${displayPageBottomSponsoredItems}">
          <br/>
          <loose:displayadverts/>    
        </c:if>
        
      </div>  

    </div>
    <div class="mySmaller">    
      <%@include file="/WEB-INF/jspf/bottombanner.jspf"%>            
      <%@include file="/WEB-INF/jspf/bottomscripts.jspf"%>            
      <c:if test="${pageAfterBodyInclude != null}">
        <jsp:invoke fragment="pageAfterBodyInclude"/>  
      </c:if>
    </div>    
  </div>    
</body>
</html>
