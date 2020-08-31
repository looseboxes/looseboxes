<%@tag pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="formAction" required="true" type="java.lang.String"%>
<%@attribute name="showIdRangeOption" required="false"%>
<%@attribute name="isEditable" required="false"%>

<c:choose>
  <c:when test="${param.targetTable != null}">
    <c:set var="targetTable" value="${param.targetTable}"/>  
  </c:when>
  <c:otherwise>
    <c:set var="targetTable" value="${productTable}"/>  
  </c:otherwise>  
</c:choose>

<jsp:useBean id="QuickEdit" class="com.looseboxes.web.components.admin.QuickEditProduct" scope="session"/>

<c:if test="${param.submit == 'Edit'}">
  <h3>...Transfering</h3>    
  <jsp:setProperty name="QuickEdit" property="*"/>    
  <jsp:setProperty name="QuickEdit" property="start" value="true"/>    
</c:if>

<p><b>${myMessage}</b></p>

<c:set var="mId" value="${productId !=null ? productId : (param.productId != null ? param.productId : '')}"/>  

<form class="form0" method="post" action="${formAction}">

  <p class="fullWidth">
    <label class="fullWidth justifiedContent">
      From Table: 
      <select name="fromTable" size="1">
        <option value="${productTable}">${productTable}</option>
        <c:if test="${isEditable == 'true'}">
          <c:forEach var="table" items="${Db1.productTableNames}">
            <c:if test="${table != productTable}">
              <option value="${table}">${table}</option>
            </c:if>
          </c:forEach>
        </c:if>  
      </select>
    </label>
  </p>

  <p class="fullWidth">
    <label class="fullWidth justifiedContent">
      To Table: 
      <select name="toTable" size="1">
        <option value="${targetTable}">${targetTable}</option>
        <c:forEach var="table" items="${Db1.productTableNames}">
          <c:if test="${table != targetTable}">
<%-- We dont use short names for tableName or productId here--%>  
            <option onclick="window.location='${formAction}?targetTable=${table}&amp;productId=${mId}'"
                    value="${table}">${table}</option>
          </c:if>
        </c:forEach>
      </select>
    </label>
  </p>
  
  <p class="fullWidth">
    <label class="fullWidth justifiedContent">
      <c:choose>
        <c:when test="${isEditable == 'true'}">
          <span class="tone0">Enter comma separated list of product IDs to transfer e.g <tt>23,88,101</tt></span><br/>
          Product Ids: <input type="text" name="productIds" value="${mId}"/>
        </c:when>  
        <c:otherwise>
          <input type="hidden" name="productIds" value="${mId}"/>
          Product Id: ${mId}<br/>
          <jsp:useBean id="RecordLoader" class="com.loosebox.components.RecordLoader">
            <jsp:setProperty name="RecordLoader" property="tableName" value="${productTable}"/>    
            <jsp:setProperty name="RecordLoader" property="productId" value="${mId}"/>    
          </jsp:useBean>
          ${RecordLoader.record.highlights}
        </c:otherwise>  
      </c:choose>
    </label>
  </p>

  <c:if test="${showIdRangeOption == 'true'}">
      <span class="tone0">Or enter begin and end IDs below</span><br/>
      <p class="fullWidth">
        <label class="fullWidth justifiedContent">
          Start Id: <input type="text" name="startId" value="-1"/>
        </label>
      </p>
      
      <p class="fullWidth">
        <label class="fullWidth justifiedContent">
          End Id: <input type="text" name="endId" endId="-1"/>
        </label>
      </p>
  </c:if>

  <c:if test="${targetTable != null}">
    <p class="fullWidth">
      <label class="fullWidth justifiedContent">
        Select type: 
        <loose:listings listingNames="type" tagName="select" maxLength="50"
                        hideHeadings="true" hideCounts="true" hideLinks="true"/>
      </label>
    </p>
  </c:if>
      
  <input name="submit" type="submit" value="Edit"/>

</form>


