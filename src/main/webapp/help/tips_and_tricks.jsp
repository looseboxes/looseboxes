<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page>
  <jsp:attribute trim="true" name="pageTitle">Tips and Tricks - ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Tips and Tricks</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">tips and tricks information, posting a product</jsp:attribute> 
  <jsp:body>
    <br/>  
    <loose:menucontent contentClass="contentBox2" contentId="aboutLooseBoxes" display="true" 
    titleClass="header1Layout curvedTop" title="Tips & Tricks" titleId="aboutLooseBoxesHeader">
      <strong>Contents</strong><br />
      <a href="#posting">Posting a product</a>
      <br/><br/>
      <a name="posting"></a><strong> Posting a product. </strong><br/>
      <ul>
        <li>When posting a product options are given for keywords this is used by both 
          ${siteName} and other search engines. To make your product &lsquo;search friendly&rsquo;;
          use common words as keywords.
        </li>
        <li>When posting a product, in the keyword/highlight option, you do not need to 
          enter the category, type or offer type etc of the product for example if the
          category is <em>new</em>, do not enter <em>used honda civic</em> or <em>used</em>
          as a keywords, rather consider keywords like <em>barely used</em> etc.
        </li>
      </ul>
    </loose:menucontent>  
  </jsp:body>
</loose:page>     

