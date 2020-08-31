<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Upload Profile Picture - ${siteName}, ${defaultTitle}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Upload Profile Picture</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">upload profile picture</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Upload Profile Picture</jsp:attribute>  
  <jsp:body> 
    <div class="dropDownMenu"><div id="progressBar">... please wait, uploading</div></div>

    <form onsubmit="myDropDownMenu.open('progressBar', false)" 
          method="post" enctype="multipart/form-data" 
          class="form0 background0 width0"
    name="profilePictureUploadForm" id="profilePictureUploadFormId"
      action="${contextURL}/cpp">

    <table>
      <tr>
        <td>
          <input class="formInput" type="file" name="image1" id="image1Id" 
          accept="image/jpeg,image/jpg,image/png,image/gif"/>
        </td>
        <td class="hideHandheld">
          <div id="image1Message">
            &nbsp;&nbsp;&nbsp;<small>[jpg, jpeg, png, gif] <b>10 megabytes max</b></small>
          </div>
        </td>
      </tr>
      <tr>
        <td>
          <input class="myBtnLayout" type="reset" name="reset" id="resetId" value=" Reset " />
          <input class="myBtnLayout" type="submit" id="submitId" value=" Next " />
        </td>
        <td class="hideHandheld">
        </td>
      </tr>
    </table>

    </form>
  </jsp:body>
</loose:page1>     
