<%@page isErrorPage="true" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:errorpage 
    pageTitle="${siteName} - Error Processing Request" 
    pageDescription="An error occured while processing the request" 
    displayTopBanner="true">   
    
    <%@include file="/WEB-INF/jspf/oops.jspf"%>  
    
</loose:errorpage>




