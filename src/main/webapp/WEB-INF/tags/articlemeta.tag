<%@tag trimDirectiveWhitespaces="true" description="HTML for displaying meta data about articles authored by looseBoxes" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="keywords" required="true"%>
<%@attribute name="genre" required="false"%>
<%@attribute name="about" required="false"%>
<%@attribute name="dateCreated" description="Format: yyyy-mm-dd e.g 2010-06-29" required="true"%>
<%@attribute name="dateModified" description="Format: yyyy-mm-dd e.g 2010-06-29" required="false"%>
<%@attribute name="datePublished" description="Format: yyyy-mm-dd e.g 2010-06-29" required="false"%>

<%@attribute name="authorItemtype" required="true" description="http://schema.org/Organization or http://schema.org/Person"%>
<%@attribute name="authorName" required="true"%>
<%@attribute name="authorUrl" required="false"%>
<%@attribute name="authorUrlName" required="false"%>
<%@attribute name="copyrightHolderItemtype" description="http://schema.org/Organization or http://schema.org/Person" required="false"%>
<%@attribute name="copyrightHolderName" required="false"%>
<%@attribute name="copyrightHolderUrl" required="false"%>
<%@attribute name="copyrightYear" required="false"%>

<c:if test="${authorUrl == null && authorName == ''}">
  <c:set var="authorUrl" value="${contextURL}/info/about.jsp"/>    
</c:if>
<c:if test="${authorUrlName == null && authorUrl != null}">
  <c:set var="authorUrlName" value="${authorUrl}"/> \
</c:if>
<c:if test="${copyrightHolderItemtype == null}">
  <c:set var="copyrightHolderItemtype" value="http://schema.org/Organization"/>    
</c:if>
<c:if test="${copyrightHolderName == null}">
  <c:set var="copyrightHolderName" value="${siteName}.com"/>    
</c:if>
<c:if test="${copyrightHolderUrl == null}">
  <c:set var="copyrightHolderUrl" value="${contextURL}/info/about.jsp"/>    
</c:if>
<small itemprop="author" itemscope itemtype="${authorItemtype}">
  <b>Author: </b>  
  <span itemprop="name">${authorName}</span> @    
  <a itemprop="url" href="${authorUrl}">${authorUrlName}</a>    
</small>&emsp;
<small><b>Genre: </b></small>
<small itemprop="genre">${genre==null || genre=='' ? productTable : genre}</small>
<br/><br/>
<small><b>Keywords: </b></small><small itemprop="keywords">${keywords}</small>
<br/><br/>
<small>Published: </small><small itemprop="datePublished">${datePublished==null || datePublished==''?dateCreated:datePublished}</small>
<c:if test="${dateModified != null && dateModified != ''}">
  &emsp;<small>Last modified: </small><small itemprop="dateModified">${dateModified}</small>
</c:if>
<br/><br/>
<small class="mySmaller" itemprop="copyrightHolder" itemscope itemtype="${copyrightHolderItemtype}">
  &copy;<small itemprop="name">${copyrightHolderName}</small>
  <a itemprop="url" href="${copyrightHolderUrl}"></a>
  <c:if test="${copyrightYear != null}">
    &nbsp;<small itemprop="copyrightYear">${copyrightYear}</small>
  </c:if>
</small>
