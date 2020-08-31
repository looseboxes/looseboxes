<%@tag trimDirectiveWhitespaces="true" description="tag fragment for option to learn more. This is often displayed below the login form" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="useDropDownMenu"%>
<%@attribute name="nodeClass"%> 

<loose:dropdownmenu nodeLink="/info/why_us.jsp" 
dropDownMenuClass="background0 width4" nodeClass="${nodeClass}"
dropDownMenuId="learnmoreDropDownMenuId"  nodeText="Learn More" useDropDownMenu="${useDropDownMenu}">
    
  <%@include file="/info/resources/why_us.xml"%>
  <br/><br/>
  Here's 2 useful links:
  <ul>
    <li><a href="${contextURL}/help/index.jsp"><b>${siteName}</b> help files</a></li>
    <li><a href="${contextURL}/info/sitemap.jsp"><b>${siteName}</b> site map</a></li>
  </ul>
    
</loose:dropdownmenu>    
