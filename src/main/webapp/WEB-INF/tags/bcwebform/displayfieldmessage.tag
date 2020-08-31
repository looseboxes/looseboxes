<%@tag trimDirectiveWhitespaces="true" description="Displays the message to be displayed along the specified form field" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<%@attribute name="formHandler" required="true" type="com.bc.web.core.form.Form"%>
<%@attribute name="formField" required="true" type="com.bc.web.core.form.FormField"%>

<c:choose>
  <c:when test="${formField.type == 'select'}">
    <c:set var="len" value="${fn:length(formField.choices)}" scope="page"/>    
    <c:set var="end" value="${len > 2 ? 2 : len}" scope="page"/>  
    <c:forEach begin="0" end="${end}" varStatus="vs" var="entry" items="${formField.choices}">
      ${entry.key}<c:if test="${vs.index < end}">,</c:if>
    </c:forEach>    
    ...etc  
  </c:when>    
  <c:when test="${formField.type == 'file'}">
<%-- FormField.supportedFileTypes returns String[] so we iterate through manually --%>      
    <c:if test="${formHandler.supportedImageTypes != null}">
      <c:forEach var="fileType" items="${formHandler.supportedImageTypes}">${fileType}, </c:forEach>      
    </c:if>  
    <b>${formHandler.maxFileSize/1000000}MB</b> per file.  
  </c:when>    
  <c:when test="${formField.dateType}">
    <tt>${formHandler.datePatterns[0]}</tt>
  </c:when>    
  <c:when test="${formField.timeType}">
    <tt>${formHandler.timePatterns[0]}</tt>
  </c:when>    
  <c:when test="${formField.timestampType}">
    <tt>${formHandler.timestampPatterns[0]}</tt>
  </c:when>    
  <c:when test="${formField.name == 'price' || formField.name == 'discount'}">
    <b>Numbers only</b> e.g 3,000.00 or 3000.        
  </c:when>    
  <c:when test="${formField.name == 'currency'}">
    Select a currency for displaying prices    
  </c:when>    
  <c:when test="${formField.tableName == 'household_items'}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>microwave oven</tt> for type <tt>appliances</tt>        
    </c:if>
    <c:if test="${formField.name == 'brand'}">
      E.g <tt>LG</tt> for subType <tt>microwave oven</tt>        
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'paymentoptions'}">
    <c:if test="${formField.name == 'name'}">
      The name on the card
    </c:if>
    <c:if test="${formField.name == 'number'}">
      Card number
    </c:if>
    <c:if test="${formField.name == 'code'}">
      Code on the card e.g CVV
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'autos'}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>off-road vehicle</tt> for type <tt>suv</tt>        
    </c:if>
    <c:if test="${formField.name == 'brand'}">
      E.g <tt>Honda</tt> for subType <tt>off-road vehicle</tt>        
    </c:if>
    <c:if test="${formField.name == 'model'}">
      E.g <tt>Civic</tt> for brand <tt>Honda</tt>        
    </c:if>
    <c:if test="${formField.name == 'mileage'}">
      Kilometers
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'gadgets'}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>smart-phone</tt> for type <tt>phone</tt>        
    </c:if>
    <c:if test="${formField.name == 'brand'}">
      E.g <tt>Nokia</tt> for subType <tt>smart-phone</tt>        
    </c:if>
    <c:if test="${formField.name == 'model'}">
      E.g <tt>3310</tt> for brand <tt>Nokia</tt>        
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'jobs' && (formHandler.formRecord == null)}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>pilot</tt> for type <tt>aviation</tt>        
    </c:if>
    <c:if test="${formField.name == 'minExperience'}">
      Minimum acceptable experience
    </c:if>
    <c:if test="${formField.name == 'minQualification'}">
      Minimum acceptable qualification
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'jobs' && formHandler.formRecord != null && !formHandler.offer}">
    <c:if test="${formField.name == 'description'}">
      A brief description of yourself
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'classifieds'}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>looking for relationship</tt> for type <tt>personals</tt>        
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'property'}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>bungalow</tt> for type <tt>residential-building</tt>        
    </c:if>
    <c:if test="${formField.name == 'facilitiesAvailable'}">
      E.g elevator, standby power-generator, swimming pool
    </c:if>
    <c:if test="${formField.name == 'servicesAvailable'}">
      Services within the area e.g train-station, library        
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'fashion'}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>handbag</tt> for type <tt>womens accessories</tt>        
    </c:if>
    <c:if test="${formField.name == 'brand'}">
      E.g <tt>Prada</tt> for subType <tt>handbag</tt>        
    </c:if>
    <c:if test="${formField.name == 'model'}">
      E.g <tt>Fall 2014 collection</tt> for brand <tt>Prada</tt>        
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'fashion'}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>handbag</tt> for type <tt>womens accessories</tt>        
    </c:if>
    <c:if test="${formField.name == 'brand'}">
      E.g <tt>Prada</tt> for subType <tt>handbag</tt>        
    </c:if>
    <c:if test="${formField.name == 'model'}">
      E.g <tt>Fall 2014 collection</tt> for brand <tt>Prada</tt>        
    </c:if>
  </c:when>    
  <c:when test="${formField.tableName == 'gifts'}">
    <c:if test="${formField.name == 'subType'}">
      E.g <tt>wristwatch</tt> for type <tt>jewelries and watches</tt>        
    </c:if>
    <c:if test="${formField.name == 'brand'}">
      E.g <tt>Rolex</tt> for subType <tt>wristwatch</tt>        
    </c:if>
    <c:if test="${formField.name == 'model'}">
      E.g <tt>Yatchmaster</tt> for brand <tt>Rolex</tt>        
    </c:if>
  </c:when>    
  <c:when test="${formField.name == 'password' || formField.name == 'confirmPassword'}">
    Minimum of ${formHandler.minimumPasswordLength} characters      
  </c:when>    
  <c:otherwise>
  </c:otherwise>
</c:choose>
