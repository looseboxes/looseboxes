<%@tag trimDirectiveWhitespaces="true" description="Root page template" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="pageTitle" description="used as the page title" required="true"%>
<%@attribute name="pageDescription" description="used to create meta description tag" required="true"%>
<%@attribute name="pageKeywords" description="used to create meta keywords tag" required="true"%>
<%@attribute name="pageHeadInclude" fragment="true"%>
<%@attribute name="pageItemtype" required="false" description="default is 'http://schema.org/WebPage'"%>
<%@attribute name="pageHeading"%>
<%@attribute name="pageAfterBodyInclude" fragment="true"%>
<%@attribute name="displayPageBottomSponsoredItems" required="false" description="default is 'true'"%>

<c:if test="${pageItemtype == null}">
  <c:set var="pageItemtype" value="http://schema.org/WebPage"/>  
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
    <jsp:invoke fragment="pageHeadInclude"/>
  </head>
  <body class="borderless" itemscope itemtype="${pageItemtype}">
  <%--script type="text/javascript" src="${contextURL}/resources/facebook/fbsdksetup.js"></script--%>  
    <div id="wrapper">  
      <div id="header">  
        <%@include file="/WEB-INF/jspf/topbanner.jspf"%>  
        <%@include file="/WEB-INF/jspf/topcenter.jspf"%>  
      </div>
      <div id="content">
        <%--  
        <div id="topBox" class="fullWidth">
          <%todo @include file="/WEB-INF/jspf/topadverts.jspf"%>
        </div>  
        --%>
        <c:if test="${!mobile}">
          <div id="leftBox">
            <loose:leftColumn/>
          </div>
        </c:if>
            
        <div id="centerBox">

          <div id="centerBoxTop" class="fullWidth">
              
              <c:if test="${pageHeading != null && pageHeading != ''}">
                <div class="header1Layout">${pageHeading}</div>  
              </c:if>
              
<%-- centeradvert was here --%>                                

<%-- search box was here --%>                

          </div>  
          <div id="centerBoxBottom" class="fullWidth">
<%-- 
DO NOT put any other content in this enclosing DIV Tag with id=pageData 
The jsp body is extracted by Response copiers which expect only the actual body
--%>
            <div id="pageData">

              <jsp:doBody/> 
              
            </div>
            <c:if test="${displayPageBottomSponsoredItems}">
              <br/>
              <loose:displayadverts/>    
            </c:if>
            <c:if test="${mobile}">
              <div class="listViewX">
<%-- @literal columnname --%>      
<%-- I removed productstatusid --%>
                <loose:listings 
                listingNames="availabilityid,productsubcategoryid" maxLength="28" 
                hideHeadings="false" headingClass="sideContentBox_header"/>  
              </div>    
            </c:if>
          </div>  
        </div>
              
      </div>
      <div class="mySmaller"> 
        <%@include file="/WEB-INF/jspf/bottombanner.jspf"%>            
        <%@include file="/WEB-INF/jspf/bottomscripts.jspf"%>            
        <jsp:invoke fragment="pageAfterBodyInclude"/>
      </div>    
    </div>   
  </body>
</html>
