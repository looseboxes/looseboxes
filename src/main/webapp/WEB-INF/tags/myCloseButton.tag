<%@tag trimDirectiveWhitespaces="true" description="The default close button for use with dropDownMenu scripts" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<%@attribute name="elementToClose" required="true"%>
<loose:closeButton  spanClass="fullWidth" buttonClass="boldRed floatRight" elementToClose="${elementToClose}"/>