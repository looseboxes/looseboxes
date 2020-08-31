<%@tag trimDirectiveWhitespaces="true" description="displays a from navigation " pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://forms.looseboxes.com/jsp/jstl/bcwebform" prefix="bwf"%>
<%@attribute name="formStage"%>
<%@attribute name="dbAction" type="com.bc.web.core.form.Form" required="true"%>
<%@attribute name="firstLink"%>
<%@attribute name="previousLink"%>
<%@attribute name="nextLink"%>
<%@attribute name="lastLink"%>
<%@attribute name="submitValue" required="true"%>
<%@attribute name="skipStage"%>
<bwf:formNav dbAction="${dbAction}" formStage="${formStage}" 
firstLink="${firstLink}" lastLink="${lastLink}" 
nextLink="${nextLink}" previousLink="${previousLink}" 
menuClass="myBtnLayout formNav mySmaller" 
resetValue="Reset" submitValue="${submitValue}"/>   
