<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="/errorpages/jsperrorpage.jsp"%>
<%@taglib uri="/WEB-INF/tlds/looseboxes" prefix="loose"%>
<loose:page1>
  <jsp:attribute trim="true" name="pageTitle">Return Policy - ${siteName}</jsp:attribute> 
  <jsp:attribute trim="true" name="pageDescription">Return Policy</jsp:attribute> 
  <jsp:attribute trim="true" name="pageKeywords">return policy</jsp:attribute> 
  <jsp:attribute trim="true" name="pageHeading">Return Policy</jsp:attribute>  
  <jsp:body> 
  <pre class="mySmaller">
    <b>(1) Introduction</b>

    We understand that from time to time you may wish to return a product to us.
    We have created this 30-day returns policy to enable you to return products 
    to us in appropriate circumstances.

    <b>(2) Returns</b>

    You are entitled to return a product to us where:

    (a)	the seller receives the returned product within 30 days following the 
        date of delivery of the product;	

    (b)	the returned product is unused, in its original unopened packaging (with 
        any seal or shrink-wrap intact), with any labels still attached, and 
        otherwise in a condition enabling the seller to sell the product as new;

    <b>(3) Returns procedure</b>

    In order to take advantage of your rights under this returns policy, you must: 

    (a)	Send the product to be returned to the seller. The sellers address is 
        available:

        (i) Online when you view the product details:
        (ii)Via email when you contact support@buzzwears.com

    (b)	Inform ${siteName} of the return via email support@buzzwears.com

    <b>(4) Exclusions</b>

    The following kinds of products may not be returned under this policy:

    (a)	food, drink and any other products liable to deteriorate within the 
        period set out in Section 1 and 2 above;

    (b)	clothing items sewn specifically to size, this includes wedding gowns 
        and other custom made clothing items.

    (c)	any other item that cannot meet the requirements in 2 (b) above.

    <b>(5) Refunds</b>

    We will give you a refund for the full price of any product properly returned 
    by you in accordance with the terms of this returns policy excluding the 
    original delivery charges and excluding the costs of returning the product 
    to us.

    <b>(6) Improper returns</b>

    Where you return a product in contravention of this policy:

    (a)	we will not refund or exchange the product;

    (b)	we may retain the returned product until you pay to us such additional 
        amount as we may charge for re-delivery of the returned product; and

    <b>(7) Questions</b>

    If you have questions or enquires please <a href="${contextURL}/info/contact_us.jsp">contact us</a>.
  </pre>        
  </jsp:body> 
</loose:page1>
