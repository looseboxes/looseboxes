<%@tag trimDirectiveWhitespaces="true" description="Root page with default values for some of the fragment attributes" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="pageTitle" description="used as the page title" required="true"%>
<%@attribute name="pageDescription" description="used to create meta description tag" required="true"%>
<%@attribute name="pageKeywords" description="used to create meta keywords tag" required="true"%>
<%@attribute name="pageHeadInclude" fragment="true"%>
<%@attribute name="pageItemtype" required="false" description="default is 'http://schema.org/WebPage'"%>
<%@attribute name="pageHeading" fragment="true"%>
<%@attribute name="pageAfterBodyInclude" fragment="true"%>

<loose:rootpage pageTitle="${pageTitle}" pageDescription="${pageDescription}" 
                pageKeywords="${pageKeywords}" pageItemtype="${pageItemtype}" pageHeading="${pageHeading}"
                pageHeadInclude="${pageHeadInclude}"
                pageAfterBodyInclude="${pageAfterBodyInclude}">
  <jsp:body>
    <jsp:doBody/>  
  </jsp:body>    
</loose:rootpage>    
