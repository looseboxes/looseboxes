<%@tag trimDirectiveWhitespaces="true" description="put the tag description here" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<%@attribute name="formType" description="Possible values are: login,join,getAccessToken (case insensitive)" required="true"%>
<%@attribute name="useScript" description="If this is set to true then the authURL will be loaded via a script" required="false"%>

<div class="dropDownMenu"><div id="progressBar">... ... ... please wait</div></div>
<br/>
<div class="tview width1">
  <div>
    <span>
        <loose:oauthFormElement formType="${formType}" provider="facebook" useScript="${useScript}"  
        providerIconClass="facebookImg borderless" providerIconSource="${contextURL}/images/transparent.gif"/><br/>
        <loose:oauthFormElement formType="${formType}" provider="yahoo" useScript="${useScript}" 
        providerIconClass="yahooImg borderless" providerIconSource="${contextURL}/images/transparent.gif"/>
    </span>    
    <span>
        <loose:oauthFormElement formType="${formType}" provider="twitter" useScript="${useScript}" 
        providerIconClass="twitterImg borderless" providerIconSource="${contextURL}/images/transparent.gif"/><br/>
        <loose:oauthFormElement formType="${formType}" provider="google" useScript="${useScript}" 
        providerIconClass="googleImg borderless" providerIconSource="${contextURL}/images/transparent.gif"/>
    </span>    
  </div>  
</div>    

