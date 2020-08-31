<%@tag trimDirectiveWhitespaces="true" description="displays a list of html pages in the cached_pages folder of the current category" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:page>
    
  <jsp:attribute trim="true" name="pageTitle">${tableName} - Pages List </jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">list of ${tableName} pages</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">${tableName} pages list</jsp:attribute> 
  <jsp:body> 
      
    <loose:pageslist/>   
        
  </jsp:body>
</loose:page>     
