<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">${siteName} - Frequently Asked Questions</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Frequently Asked Questions (FAQs)</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">FAQ, frequently asked questions answers, information, how do i join, how do i, buy, how do i sell, how do i pay</jsp:attribute> 
  <jsp:body>
    <br/>  
    <loose:menucontent contentClass="contentBox2" contentId="aboutLooseBoxes" display="true" 
    titleClass="header1Layout curvedTop" title="Frequently Asked Questions" titleId="aboutLooseBoxesHeader">
        <strong>Contents</strong><br />
        <a href="#join">How do I join ${siteName}?</a><br/>
        <a href="#paybymobile">How do I use mobile banking to instantly pay for item(s) purchased</a><br/>
        <a href="#editprofile">How do I edit my profile?</a><br/>
        <a href="#sell">How do I sell?</a><br/>
        <a href="#search">How do I search for an item?</a><br/>
        <a href="#viewdetails">How do I view an item's details?</a><br/>
        <a href="#contactseller">How do I contact the seller of an item?</a><br/>
        <a href="#buy">How do I buy?</a><br/>
        <a href="#howlong_delivery">How long does it take for an item to get delivered?</a><br/>
        <a href="#request">How do I submit a request for an item?</a><br/>
        <br/><br/>
        <a name="join"><strong>How do I join ${siteName}?</strong></a>
        <%@include file="/help/resources/howto_join.xml"%>
        <br/>
        <a name="paybymobile"><strong>How do I use mobile banking to instantly pay for item(s) purchased?</strong></a>
        <%@include file="/help/resources/howto_paybymobile.xml"%>
        <br/>
        <a name="editprofile"><strong>How do I edit my profile?</strong></a>
        <%@include file="/help/resources/howto_editprofile.xml"%>
        <br/>
        <a name="sell"><strong>How do I sell?</strong></a>
        <%@include file="/help/resources/howto_sell.xml"%>
        <br/>
        <a name="search"><strong>How do I search for an item?</strong></a>
        <%@include file="/help/resources/howto_search.xml"%>
        <br/>
        <a name="viewdetails"><strong>How do I view an item's details?</strong></a>
        <%@include file="/help/resources/howto_viewdetails.xml"%>
        <br/>
        <a name="contactseller"><strong>How do I contact the seller of an item?</strong></a><br/>
        <%@include file="/help/resources/howto_contactseller.xml"%>
        <br/>
        <a name="buy"><strong>How do I buy?</strong></a>
        <%@include file="/help/resources/howto_buy.xml"%>
        <br/>
        <a name="howlong_delivery"><strong>How long does it take for an item to get delivered?</strong></a>
        <%@include file="/help/resources/howlong_delivery.xml"%>
    </loose:menucontent>  
  </jsp:body>
</loose:page>     
