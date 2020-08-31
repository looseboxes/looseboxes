<%@tag trimDirectiveWhitespaces="true" description="same as page.tag, but has a page heading, has no searchBox, left and right column" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="pageTitle" description="used as the page title" required="true"%>
<%@attribute name="pageDescription" description="used to create meta description tag" required="true"%>
<%@attribute name="pageKeywords" description="used to create meta keywords tag" required="true"%>
<%@attribute name="pageHeadInclude" fragment="true"%>
<%@attribute name="pageHeading"%>
<%@attribute name="pageAfterBodyInclude" fragment="true"%>
<%@attribute name="displayPageBottomSponsoredItems" required="false" description="default is 'true'"%>

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
  <jsp:invoke fragment="pageHeadInclude"/>
</head>

<body class="borderless">
    
  <%--script type="text/javascript" src="${contextURL}/resources/facebook/fbsdksetup.js"></script--%>  
    
  <div id="wrapper">
    <div id="header">  
      <%@include file="/WEB-INF/jspf/topbanner.jspf"%>
    </div>
    <div id="content">

      <%@include file="/WEB-INF/jspf/topadverts.jspf"%>

      <c:if test="${pageHeading != null && pageHeading != ''}">
        <div class="header1Layout">${pageHeading}</div>  
      </c:if>

      <div class="padded borderless justifiedContent">

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
      <%@include file="/WEB-INF/jspf/advertScripts.jspf"%>          
      <jsp:invoke fragment="pageAfterBodyInclude"/>
    </div>    
  </div>
</body>
</html>
