<script type="text/javascript">
var cookieEnabled=(navigator.cookieEnabled)? true : false
//if not IE4+ nor NS6+
if (typeof navigator.cookieEnabled=="undefined" && !cookieEnabled){ 
document.cookie="testcookie"
cookieEnabled=(document.cookie.indexOf("testcookie")!=-1)? true : false
}
if (!cookieEnabled) {
  document.write("<h3>Cookies are not currently enabled on your browser!<br/>Cookies are required to keep your session valid!</h3>");    
}
</script>

<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:errorpage 
    pageTitle="${siteName} - Expired Session" 
    pageDescription="expired session error page">   
    
    Oops your session is no longer valid!<br/>
    Keep browsing though, a new session was created for you!
    
</loose:errorpage>

