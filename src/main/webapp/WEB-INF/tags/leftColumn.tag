<%@tag trimDirectiveWhitespaces="true" description="left column view with options" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%--@todo loose:sidecontent display="false" title="Advanced Search" 
                   titleId="mHeader" contentId="mContent">
  <%@include file="/WEB-INF/jspf/advancedSearchBox.jspf"%>
  
</loose:sidecontent --%>

<div class="spaced"></div>

<div class="listViewX contentBox sideContentWidth mySmaller">
  <%-- @literal columnname --%>     
  <%-- I removed productstatusid --%>
  <loose:listings 
    listingNames="availabilityid,productsubcategoryid" maxLength="28" 
    hideHeadings="false" headingClass="sideContentBox_header"/>  
</div>    
  
