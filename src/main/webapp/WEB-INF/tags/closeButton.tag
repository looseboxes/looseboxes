<%@tag trimDirectiveWhitespaces="true" description="A close button 'x' for use with dropDownMenu scripts" pageEncoding="UTF-8"%>
<%@attribute name="spanClass"%><%@attribute name="buttonClass"%><%@attribute name="elementToClose" required="true"%>
<%-- This will enable us close the dropDownMenu. All div tags within dropDownMenu will not be displayed so we use span --%>
<span class="${spanClass}"><input class="${buttonClass}" type="button" value=" x " onclick="myDropDownMenu.close('${elementToClose}')"/></span>
