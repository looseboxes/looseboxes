<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>

<loose:article authorEmail="coolbuyng@gmail.com" 
               dateCreated="2014-04-29" dateModified="2014-04-31" 
               description="The Art of Shopping: Kids Clothes" 
               keywords="article, art of shopping, kids clothes">
    
  <jsp:attribute name="body" trim="true">
    <p>
      Kids breeze through cloths the way one breezes through an icecream, 
      living the parents trailing behind. The resultant cost is one that leaves 
      a gaping hole in their pockets. It is therefore necessary to strategise 
      when shopping for kids cloths. Highlighted below are some cost saving 
      methods in no order of importance.
    </p>    
    <p>
      <b>Buying in Bulk?</b><br/><br/>    
      My candid advice, avoid buying in bulk, rather try buying in piece meal, while this does not particularly save cost, it frees up your funds for other household expenditures. Another plus, you have a new cloth ready for any occasion ( children's parties never end!) saving you from emergency shopping, see it like saving for that rainy day.
    </p>
    <p>
      <b>The Web</b><br/><br/>    
      The web is another gold mine. Get a website that offers you the option of comparing the prices of different websites, it offers you more options for funds and variety. from time to time they go also go on sales, and those cloths you always loved become more affordable, snap up the opportunity and get a few. Be sure to check the percentage discount and decide if is worth it.
    </p>
    Some websites are listed below:<br/>
    <ul>
      <li><a target="_blank" href="${contextURL}/search?pt=f&type=7">${siteName} Kids Fashion</a></li>              
      <li><a target="_blank" href="http://www.hm.com/us/department/KIDS">H &amp; M</a></li>    
      <li><a target="_blank" href="http://www.taafoo.com/pages/searchresults.aspx?departmentId=357922">Taafoo</a></li>                    
      <li><a target="_blank" href="http://www.jumia.com.ng/baby-toys-kids/">Jumia</a></li>          
    </ul>
    <p>
      <b>A Size or Two Bigger?</b><br/><br/>    
      Try buying a size or two bigger than your kid. These way, they grow into the cloths and not out of it, in order words you also buy time! I specially recommend this for occasion cloths, those you tend to spend more on. (Remember, to follow the clothing instructions when washing.)
    </p>
    <p>
      <b>Play Clothes!</b><br/><br/>    
      Day to day cloths or play cloths as they are often called are those that tend to wear out faster and thus gulp more money and often! So my advice? Two really, either spend big on good quality cloths that can withstand the wear and tear of play, (cloths made out of cotton fabric tend to fare better ) or buy affordable cloths which require constant replacement but are more pocket friendly.
    </p>
    <p>
      I would love to hear from you, what saving tips do you have, please leave your comments below so we could all save big!
    </p>
  </jsp:attribute>    
</loose:article>
