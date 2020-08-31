<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Submit Complain</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Submit complain / Report inappropriate buying/selling</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">submit complain, report inappropriate buying/selling</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Report Inappropriate Activities</jsp:attribute>  
  <jsp:body> 
  
    <div><strong>Enter your complaint</strong></div>  
    <form class="form0 background0 width4" name="complaintForm" method="post" target="${contextURL}/submitComplain">

      <input type="hidden" name="emailAddress" value="${User.emailAddress}"/>
      <label>Subject</label>
      <input class="formInput" type="text" name="subject" value=""/>    
      <label>Text</label>
      <textarea rows="4" class="formInput" name="message">
      </textarea>
      <br/><br/>
      <input class="myBtnLayout" type="submit" value="Submit"/>
      
    </form>    
    
  </jsp:body> 
</loose:page1>
