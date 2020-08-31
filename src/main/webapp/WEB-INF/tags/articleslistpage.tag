<%@tag trimDirectiveWhitespaces="true" description="displays a list of pages in the specified folder" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:page>
    
  <jsp:attribute trim="true" name="pageTitle">${tableName} - Articles List </jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">list of ${tableName} articles</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">${tableName} articles list</jsp:attribute> 
  <jsp:body> 
      
    <loose:articleslist/>   
        
  </jsp:body>
</loose:page>     
