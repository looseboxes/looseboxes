<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:errorpage 
    pageTitle="${siteName} - Internal Server Error (500 Error)" 
    pageDescription="error in application, contact webmaster">   
    
    An error has occured in this application. All information about<br/>
    this error has been recorded and site engineers have been notified.<br/>
    We appologize for any incovenience. If the problem persists, please<br/>
    contact the webmaster at: <a href="mailto:support@looseboxes.com">support@looseboxes.com</a>.
    
</loose:errorpage>
