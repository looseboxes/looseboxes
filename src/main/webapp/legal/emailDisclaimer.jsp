<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Email Disclaimer - ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Email disclaimer, confidentiality note</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">email disclaimer, confidentiality note</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Email Disclaimer</jsp:attribute>  
  <jsp:body> 
  <p>
    Any email sent by ${siteName}, its attachments and any rights attaching
    hereto are, unless the content clearly indicates otherwise, the property of
    ${siteName}. It is confidential, private and intended for only the addressee.
    <br/><br/>
    Should you not be the addressee and receive any email from ${siteName}, kindly
    notify the sender, and delete the email immediately. Donot disclose or use it
    in any way.
    <br/><br/>
    Views and opinions expressed in any email sent by ${siteName} are those of the
    sender unless clearly stated as those of ${siteName}.
    <br/><br/>
    ${siteName} accepts no liablility for any loss or damages howsoever incurred,
    or suffered, resulting or arising, from the use of its email or attachements.
    <br/><br/>
    ${siteName} does not warrant the integrity of its emails sent by it nor that
    emails are free of errors, viruses, interception or interference, and thus
    accepts no liability for any damaage or loss caused by any issuues arising
    out of the transmission of its email.
  </p>
  </jsp:body> 
</loose:page1>
