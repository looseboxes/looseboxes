<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:errorpage 
    pageTitle="${siteName} - Service Unavailable (503 Error)" 
    pageDescription="carrying out maintenance">   
    
    We are currently carrying out some maintenance on <i>${siteName}</i>.<br/>
    By this we hope to provide an improved browsing experience.
    
</loose:errorpage>
