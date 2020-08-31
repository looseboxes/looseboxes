<%@page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8"%>
<%
    /** 
     * Exception occured when we used this.getClass() in the logger
     */
    if(pageContext != null && pageContext.getException() != null) {
        String requestURI = pageContext.getErrorData() == null ? "" : pageContext.getErrorData().getRequestURI();
        com.bc.util.Log.getInstance().log(
                java.util.logging.Level.WARNING, 
                "Exception ocurred in page: "+requestURI, 
                com.looseboxes.web.WebApp.class, 
                pageContext.getException());
    }
%>                
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="robots" content="noindex, nofollow"/>
    <title>Page Compilation Error</title>    
  </head>    
  <body>
    <%@include file="/WEB-INF/jspf/oops.jspf"%>  
  </body>
</html>


