<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Send Mail - ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Mail Client - Send Mail</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">send email</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Send Mail</jsp:attribute>  
  <jsp:body>
    <form class="handWriting" method="post" name="sendMail" id="sendMailId" action="${contextURL}/sendmail">

      <c:choose>
        <c:when test="${msn != null}">Hi,</c:when>
        <c:otherwise>Hi <loose:codec1 decode="true" var="${msn}" />,</c:otherwise>
      </c:choose>
      <br/>
      <p>
        Please fill out the form below. Your email password will not be stored.<br/>
        It will only be used to send the requested mail on your behalf. Thanks.
      </p>
      <br/>
      <c:choose>
        <c:when test="${User.loggedIn || mse != null}">
          <c:choose>
            <c:when test="${User.loggedIn}">
              Email address:&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${User.emailAddress}"/><br/><br/>
            </c:when>
            <c:otherwise>
              Email address:&nbsp;&nbsp;&nbsp;&nbsp;<loose:codec1 var="${mse}" decode="true"/><br/><br/>
            </c:otherwise>
          </c:choose>
        </c:when>
        <c:otherwise>
          Email address:&nbsp;&nbsp;&nbsp;<input type="text" name="emailAddress" maxlength="100"/><br/><br/>
        </c:otherwise>
      </c:choose>
      Email password:&nbsp;<input type="password" name="password" maxlength="100"/><br/><br/>
      Enter the email address(es) you wish to send the <b>${mt}</b> to below, then click <em>send</em>
      <br/><br/>
    <%-- The meaning of acronyms 'msn', 'mse', 'mt' on this page is at the target servlet of the form below --%>
      <input type="hidden" name="msn" value="${msn}"/>
      <input type="hidden" name="mse" value="${mse}"/>
      <input type="hidden" name ="mt" value="${mt}"/>
      <input type="hidden" name ="productIds" value="${productIds}"/>
      Recipient 1: <input type="text" name="email0" maxlength="100"/><br/>
      Recipient 2: <input type="text" name="email1" maxlength="100"/><br/>
      Recipient 3: <input type="text" name="email2" maxlength="100"/><br/>
      <input class="myBtnLayout" type="reset" name="reset" id="reset" value=" Reset " />
      <input class="myBtnLayout" type="submit" value=" Send " />
    </form>
  </jsp:body>
</loose:page1>     
