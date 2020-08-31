<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%-- This bean is also used in advancedSearchResults.jspf --%>  
<jsp:useBean id="ConvCurr" class="com.looseboxes.web.servlets.Convcurr" scope="request"/>
<jsp:useBean id="User" class="com.looseboxes.web.components.UserBean" scope="session"/>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Currency Converter - ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Currency Converter</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">Currency Converter</jsp:attribute> 
  <jsp:body> 

    <br/>  
    <c:if test="${currencyConverterResult != null}">
      <div class="handWriting" id="currencyConverterResult">
        1 ${currencyConverterFromCode} = ${currencyConverterResult} ${currencyConverterToCode}
      </div>
    </c:if>  
    
    <loose:menucontent contentClass="contentBox2" contentId="currencyConverter1" display="true" 
    titleClass="header1Layout curvedTop" title="Convert Supported Currencies" titleId="currencyConverterHeader1">
        
      <loose:convcurrform formName="cc1" ajaxResultsElementId="ccAjxRes1" currencyCodes="${ConvCurr.appCurrencyCodes}"/>
        
    </loose:menucontent>  

    <br/><br/>
    
    <loose:menucontent contentClass="contentBox2" contentId="currencyConverter2" display="true" 
    titleClass="header1Layout curvedTop" title="Convert All Currencies" titleId="currencyConverterHeader2">
        
      <loose:convcurrform formName="cc2" ajaxResultsElementId="ccAjxRes2" currencyCodes="${ConvCurr.currencyCodes}"/>
        
    </loose:menucontent>  
    
    <br/>
    <c:if test="${User.productSearchResults != null && not empty User.productSearchResults}">
      <loose:searchresults searchResultsBean="${User.productSearchResults}"/>
    </c:if>    
  </jsp:body>
</loose:page>     
