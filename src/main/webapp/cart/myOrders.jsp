<%@page trimDirectiveWhitespaces="true" contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:redirect url="${contextURL}/cart/orders.jsp?emailAddress=${User.emailAddress}"/>
