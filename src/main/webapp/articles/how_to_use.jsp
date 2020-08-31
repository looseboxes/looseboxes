<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:article authorEmail="looseboxes@gmail.com" 
               dateCreated="2012-04-12" dateModified="2014-04-22" 
               description="How to Use ${siteName}" genre="help articles" 
               keywords="how to use ${siteName}">
  <jsp:attribute trim="true" name="pageAfterBodyInclude"> 
    <script type="text/javascript">
      looseboxes.loadImage("${contextURL}/articles/howto_use/center_showing_links.gif", 440, 304);
      looseboxes.loadImage("${contextURL}/articles/howto_use/center_showing_jobs.gif", 430, 194);
      looseboxes.loadImage("${contextURL}/articles/howto_use/center_showing_searchbox.gif", 430, 194);
    </script>
  </jsp:attribute> 
  <jsp:attribute name="body" trim="true">
    <h4>First - 3 Tips</h4>
    <ol class="spacedList">
      <li>
        Make use of the <b>links</b> provided at the right and left side of most 
        <i>${siteName}</i> web pages. For example if you are an IT specialist looking 
        for a job, simply click on the <em>Information Technology</em> link on
        the right. The links are shown in the image below<br/><br/>
        <img alt="image show useful links" width="380" height="270"
             src="${contextURL}/articles/howto_use/center_showing_links.gif"/>
      </li>  
      <li>
        When searching ${siteName}, avoid using common words like <em>national</em>, 
        <em>limited</em> etc. For example: use <em>linkserve</em> rather than 
        <em>linkserve limited</em>. Also, use <em>railway corporation</em> rather than 
        <em>nigerian railway corporation</em>.
      </li>
      <li>
        Try using accronyms (e.g nnpc, undp) if the full name does not yield the 
        desirable result. For example: Rather than United Nations Development Program 
        use <em>undp</em>. However note that accronyms with 3 or less letters are not effective.
      </li>
    </ol>
    <h4>Then - 2 Links</h4>
    <ol class="spacedList">
      <li><a href="${contextURL}/help/index.jsp"><b>${siteName}</b> help files</a></li>
      <li><a href="${contextURL}/info/sitemap.jsp"><b>${siteName}</b> site map</a></li>
    </ol>
    <h4>Finally - 1 Example</h4>
    <ol class="spacedList">
      <li>
        <b>To search for a job with title: <em>CCTV, Solar and Inverter installation Engineers</em></b>
        <br/><br/>
        <ul class="spacedList">
          <li>
            First make sure you are browsing under the jobs category. The category you are currently browsing 
            on is highlighted in the top banner. The image below shows the various categories with the <em>jobs</em>
            category highlighted.<br/><br/>
            <img alt="image showing categories" width="380" height="180"
                 src="${contextURL}/articles/howto_use/center_showing_jobs.gif"/>
          </li>
          <li>
            Then enter the search term in the search box and click the <i>search</i> button or hit the enter key on your computer.      
            The search box is shown in the image below:<br/><br/>
            <img alt="image showing searchbox"  width="380" height="180"
                 src="${contextURL}/articles/howto_use/center_showing_searchbox.gif"/>
          </li>
          <li>
            Use the search term '<em>CCTV, Solar and Inverter installation</em>'. Leave out the word
            '<em>Engineers</em>' as it is a common word and could lead to multiple false results. 
          </li>
          <li>
            If this search term doesn't yield the desired, enter <em>Solar and Inverter installation</em> and  subsequently
            <em>CCTV</em> if necessary.
          </li>
        </ul>
      </li>    
    </ol>
  </jsp:attribute>
</loose:article>    
