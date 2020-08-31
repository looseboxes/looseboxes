<%@tag trimDirectiveWhitespaces="true" description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="description" required="true"%>
<%@attribute name="keywords" required="true"%>
<%@attribute name="genre" required="false"%>
<%@attribute name="about" required="false"%>
<%@attribute name="authorEmail" required="false"%>

<%@attribute name="dateCreated" description="Format: yyyy-mm-dd e.g 2010-06-29" required="true"%>
<%@attribute name="dateModified" description="Format: yyyy-mm-dd e.g 2010-06-29" required="false"%>
<%@attribute name="datePublished" description="Format: yyyy-mm-dd e.g 2010-06-29" required="false"%>
<%@attribute name="headline" required="false"%>
<%@attribute name="body" fragment="true" required="true"%>

<%-- These attributes are inherited from page.tag --%>
<%@attribute name="pageHeadInclude" fragment="true"%>
<%@attribute name="pageAfterBodyInclude" fragment="true"%>

<c:set var="hasAuthor" value="${authorEmail != null && authorEmail != ''}"/>

<c:if test="${hasAuthor}">
  <jsp:useBean id="AuthorBean" class="com.looseboxes.web.components.UserBean" scope="request">      
    <jsp:setProperty name="AuthorBean" property="emailAddress" value="${authorEmail}"/>    
  </jsp:useBean>   
</c:if>

<loose:page pageItemtype="http://schema.org/Article">
  <jsp:attribute trim="true" name="pageTitle">${siteName} - ${headline==null || headline==''?description:headline}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">${description}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">${keywords}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeadInclude">${pageHeadInclude}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageAfterBodyInclude">${pageAfterBodyInclude}</jsp:attribute> 
  <jsp:body>

    <div>
        
      <c:if test="${AuthorBean != null}">
        <c:if test="${AuthorBean.record.image1 != null && AuthorBean.record.image1 != ''}">
          <div>
              
            <loose:getimagesrc imagePath="${AuthorBean.record.image1}"/>  

            <img id="authorImage" 
               itemprop="image" class="${imageClass}" 
               src="${getimagesrc_imageSrc}" 
               alt="Author's image" 
               title="Author's image"/> 
          </div>
        </c:if>                
      </c:if>  
        
      <h3 itemprop="headline">${headline==null || headline==''?description:headline}</h3>
      <div class="handWriting" itemprop="articleBody">
        <jsp:invoke fragment="body"/>     
      </div>  
      <br/>
      
      <c:if test="${AuthorBean != null}">
        <c:choose>
          <c:when test="${AuthorBean.record.admin}">
            <c:set var="authorItemtype" value="http://schema.org/Organization"/>                  
          </c:when>  
          <c:otherwise>
            <c:set var="authorItemtype" value="http://schema.org/Person"/>                  
          </c:otherwise>
        </c:choose>  
        <c:set var="authorName" value="${AuthorBean.record.username}"/>    
        <c:if test="${AuthorBean.record.admin}">
          <c:set var="authorUrl" value="${contextURL}/info/about.jsp"/>    
          <c:set var="authorUrlName" value="about ${siteName}"/>    
        </c:if>
      </c:if>
      
      <loose:articlemeta about="${about}" genre="${genre}" keywords="${keywords}"  
      authorItemtype="${authorItemtype}" authorName="${authorName}"  
      authorUrl="${authorUrl}" authorUrlName="${authorUrlName}"  
      dateCreated="${dateCreated}" dateModified="${dateModified}" datePublished="${datePublished}"/>
      
    </div>    
  </jsp:body>
</loose:page>     
