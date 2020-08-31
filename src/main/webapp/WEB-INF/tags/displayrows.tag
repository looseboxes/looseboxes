<%@tag trimDirectiveWhitespaces="true" description="Displays the supplied elements in a table" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="rowClass" required="false"%>
<%@attribute name="cellClass" required="false"%>
<%@attribute name="cellWidthPercent" required="false"%>
<%@attribute name="columnCount" required="false"%>
<%@attribute name="cellFragment" fragment="true" required="false"%>

<%@attribute name="items" required="true" type="java.util.Collection"%>
<%@attribute name="begin" description="iteration will begin at this index" required="false"%>
<%@attribute name="end" description="iteration will end at this index" required="false"%>
<%@attribute name="step" description="iteration will be done in these steps" required="false"%>

<%@attribute name="bannerAfterFirstRow" description="Banner to display after the first row" fragment="true" required="false"%>

<%-- We do this in case there are fewer results than there are columns in the table --%>
<c:set var="itemCount" value="${fn:length(items)}"/>
<c:choose>
  <c:when test="${cellWidthPercent != null}">
    <c:choose>
      <c:when test="${itemCount < columnCount}">        
          <c:set var="tableStyle" value="width:${cellWidthPercent * itemCount}%;" scope="page"/>              
      </c:when>
      <c:otherwise>
        <c:set var="tableStyle" value="width:auto;" scope="page"/>              
      </c:otherwise>      
    </c:choose>  
    <c:set var="cellStyle" value="width:${cellWidthPercent}%;" scope="page"/>              
  </c:when>    
  <c:otherwise>
    <c:set var="tableStyle" value="width:auto;" scope="page"/>                
    <c:set var="cellStyle" value="width:auto;" scope="page"/>              
  </c:otherwise>  
</c:choose>
<%-- Default values --%>
<c:if test="${begin == null || begin == ''}"><c:set var="begin" value="0" scope="page"/></c:if>
<c:if test="${end == null || end == ''}"><c:set var="end" value="${itemCount}" scope="page"/></c:if>
<c:if test="${step == null || step == ''}"><c:set var="step" value="1" scope="page"/></c:if>

<c:set var="colIndexInRow" value="0"/> 
<c:set var="rowIndex" value="0"/> 

<table style="${tableStyle}">
    
  <c:forEach var="item" varStatus="vs" begin="${begin}" end="${end}" step="${step}" items="${items}">

    <c:if test="${colIndexInRow == 0}">
      <tr class="${rowClass}">
    </c:if>    
    
      <td class="${cellClass}" style="${cellStyle}">
          
          <%--@currentRowItem Export this variable for use by fragments below --%>    
          <c:set var="currentRowItem" value="${item}" scope="request"/>  

          <c:choose>
            <c:when test="${cellFragment == null}">
              ${item}   
            </c:when>  
            <c:otherwise>
              <jsp:invoke fragment="cellFragment"/> 
            </c:otherwise>
          </c:choose>  

      </td>
      
    <c:set var="reachedEndOfCurrRow" value="${colIndexInRow == columnCount-1}"/>      

    <c:if test="${reachedEndOfCurrRow || vs.index == end}"></tr></c:if>  

<%-- If specified add the banner after the first row --%>
    <c:if test="${rowIndex == 0 && reachedEndOfCurrRow && bannerAfterFirstRow != null}">
      <tr>
        <td colspan="${columnCount}">
          <jsp:invoke fragment="bannerAfterFirstRow"/>        
        </td>    
      </tr>
    </c:if>  

    <c:choose> 
      <c:when test="${reachedEndOfCurrRow}">
        <c:if test="${!mobile}">
          <c:set var="colIndexInRow" value="0"/>    
          <c:set var="rowIndex" value="${rowIndex+1}"/>    
        </c:if>  
      </c:when>  
      <c:otherwise>
        <c:set var="colIndexInRow" value="${colIndexInRow+1}"/>      
      </c:otherwise>
    </c:choose>    

  </c:forEach>   
      
</table>

