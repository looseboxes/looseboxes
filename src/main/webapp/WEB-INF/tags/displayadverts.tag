<%@tag trimDirectiveWhitespaces="true" description="displays some adverts" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%-- May be required by our fragments below --%>        
<c:set var="searchResultsBean" value="${advertSearchResults}" scope="request"/>  

<div class="myFontSize0">&nbsp;sponsored</div>
<div class="myBorder5">

<%-- This is a Tag, so we have to call doBody --%>  
<%-- Also avoid putting comments within --%>  

  <loose:displayrows begin="0" end="${mobile?1:3}" columnCount="${mobile?2:4}" 
                   items="${advertSearchResults.currentPage}" 
                   rowClass="srRow myFontSize0 spaced2"
                   cellClass="srCell myBorder5">

    <jsp:attribute name="cellFragment" trim="true">
      <%@include file="/WEB-INF/jspf/searchresultsCell.jspf"%>  
    </jsp:attribute> 

    <jsp:body>
      <jsp:doBody/>  
    </jsp:body> 

  </loose:displayrows>
</div>   





