<%@tag trimDirectiveWhitespaces="true" description="Turing Number and Agree To Terms check box" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="hideCaptcha"%>
<%@attribute name="hideAgreeToTerms"%>
  <table>
  <%-- turingNumber and agreeToTerms --%>
    <%-- 'true' implies these inputs will be displayed, any 
    other value implies otherwise --%>  
    <c:if test="${hideCaptcha != 'true'}">
      <c:set var="hideCaptcha" value="false"/>  
    </c:if>
    <c:if test="${hideCaptcha == 'false'}">
      <tr>
        <td class="formColumn1">Captcha Challenge<font color="#FF0000"> * </font></td>
        <td class="formColumn2">
          <table>
            <tr>
              <td rowspan="2">
                <img name="captchaImage" id="captchaImageId" src="${contextURL}/captcha" alt="captcha image" class="captchaImage borderless"/>
              </td>
              <td>
                <input type="text" name="userCaptchaInput" class="captchaInput" size="5" maxlength="7"/>
              </td>
            </tr>
            <tr>
              <td>
                <input type="button" class="myBtnLayout fullWidth" style="text-align:center" 
                onclick="myFormHandler.changeTuring('captchaImageId', 'src', '${contextURL}/captcha', 250, 75)" value="change"/>
              </td>
            </tr>
          </table>
        </td>
        <td class="hideHandheld formColumn3">
          <div id="turingNumberMessage" class="mySmaller">&nbsp;&nbsp;&nbsp;Type in the letters and figures you see on the left</div>
        </td>
      </tr>
    </c:if>
    <%-- 'true' implies these inputs will be displayed, any 
    other value implies otherwise --%>  
    <c:if test="${hideAgreeToTerms != 'true'}">
      <c:set var="hideAgreeToTerms" value="false"/>  
    </c:if>
    <c:if test="${hideAgreeToTerms == 'false'}">
      <tr>
        <td class="formColumn1">Agree to Terms<font color="#FF0000"> * </font></td>
        <td class="formColumn2">
          <input type="checkbox" name="agreeToTerms" id="agreeToTermsId" value="true" onclick="myFormHandler.validateConsent()" />
        </td>
        <td class="hideHandheld formColumn3">
          <div id="agreeToTermsMessage">&nbsp;<small>Click <a href="${contextURL}/legal/privacy_policy.jsp">here</a> to view Privacy Policy</small></div>
        </td>
      </tr>
    </c:if>
  </table>
