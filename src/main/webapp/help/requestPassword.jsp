<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Forwarding to request password page</title>
    </head>
    <body>
      <h3>... Please wait. Forwarding your request.</h3>  
      <jsp:forward page="${pageContext.servletContext.contextPath}/user/requestPassword.jsp"/>
    </body>
</html>
