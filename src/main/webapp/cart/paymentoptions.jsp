<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="mEmailAddress" value="${emailAddress==null?param.emailAddress:emailAddress}"/>  

<c:choose>
  <c:when test="${User.emailAddress == mEmailAddress}">
    <c:set var="mUser" value="${User}"/>    
  </c:when>
  <c:otherwise>
    <jsp:useBean id="Userselector" class="com.looseboxes.web.components.SelectorBean<com.looseboxes.pu.entities.Siteuser>" scope="request">
      <jsp:setProperty name="Paymentmethodselector" property="tableName" value="siteuser"/>    
      <jsp:setProperty name="Paymentmethodselector" property="columnName" value="emailAddress"/>    
      <jsp:setProperty name="Paymentmethodselector" property="columnValue" value="${mEmailAddress}"/>    
    </jsp:useBean>
    <c:set var="mUser" value="${Userselector.singleResult}"/>    
  </c:otherwise>
</c:choose>

<jsp:useBean id="Paymentmethodselector" class="com.looseboxes.web.components.SelectorBean<com.looseboxes.pu.entities.Userpaymentmethod>" scope="request">
  <jsp:setProperty name="Paymentmethodselector" property="tableName" value="userpaymentmethod"/>    
  <jsp:setProperty name="Paymentmethodselector" property="columnName" value="paymentmethoduser"/>    
  <jsp:setProperty name="Paymentmethodselector" property="columnValue" value="${mUser}"/>    
</jsp:useBean>
<c:set var="Paymentoptions" value="${Paymentmethodselector.resultList}"/>

<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">User Payment Options</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">user payment options</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">user payment options</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">User Payment Options</jsp:attribute>  
  <jsp:body>
    <c:choose>
      <c:when test="${mEmailAddress == null || mEmailAddress == ''}">
        <p class="handWriting">
          Please specify an <tt>email address</tt> for which payment options will be displayed
        </p>
      </c:when>  
      <c:otherwise>
        <ol>
          <c:forEach var="Paymentoption" items="${Paymentoptions}">
            <li>${Paymentoption.paymentmethodid.paymentmethod}</li>  
          </c:forEach>  
        </ol>
      </c:otherwise>
    </c:choose>  
  </jsp:body>
</loose:page1>     
