package aaa.util;


/**
 * @(#)SendShoppingCartTipsMail.java   30-Jun-2015 23:38:02
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public class SendShoppingCartTipsMail extends AbstractSendMail {

    @Override
    public String getSubject(String email, String password) {
        String subject = "BuzzWears careline - Some useful tips";
        return subject;
    }

    @Override
    public String getMessage(String email, String password) {
        // Salutation (hi, etc) is automatically added
        // http://www.buzzwears.com/user/join.jsp
        String msg = 
//"<p>We noticed you had problems using <a href=\"http://www.buzzwears.com\">BuzzWears</a> shopping cart.</p>\n" +
//"<b>Steps to Paying for Items</b>\n" +                
"<b>Here are a few tips for using <a href=\"http://www.buzzwears.com\">BuzzWears</a> shopping cart.</b>\n" +
                
"<ul>\n" +
"  <li>Create an account <a href=\"http://www.buzzwears.com/user/join.jsp\">here</a>, if you don't have one." +
"    <small>(Activate your account by following instructions sent to your email inbox/spam folder)</small>"   +             
"  </li>\n" +
"  <li><a href=\"http://www.buzzwears.com/user/login.jsp\">Login to BuzzWears</a> before placing any order.</li>\n" +
"</ul>\n" +
"<ul>\n" +
"  <li>Add items to your cart</li>\n" +
"  <li>Go to your cart by clicking the cart image</li>\n" +
"  <li>Enter your address details (for delivery) and click <em>add address/use this address</em></li>\n" +
"  <li>Select payment method and click <em>pay for item</em></li>\n" +
"</ul>\n" +
"<b>\n" +
"  IMPORTANT NOTES:\n" +
"  <p>Make sure you enter a street address.</p>\n" +
"  <p>If you are not logged-in, you will be able to add to your cart. However, to pay for the items in your cart, you must be logged-in.</p>\n" +
"</b>" +
                
//////////////////////////// ///////////////////////////////                
"<p style=\"background-color:#EEDDEE; width:100%;\">\n" +
"  For more information <a href=\"http://www.buzzwears.com/help/FAQs.jsp#buy\">click here</a><br/>\n" +                
"  For enquiries contact: <tt><a href=\"mailto:support@buzzwears.com\">support@buzzwears.com</a></tt><br/>\n" +
"  Call: <tt>08092925682</tt>\n" +
//"  BB Pin: <tt>55F156B8</tt>\n" +
"</p>";                
        return msg;
    }
}
/**
 * 
<p>We noticed you had problems using <a href=\"http://www.buzzwears.com\">BuzzWears</a> shopping cart.</p>

<p>Here are a few tips:</p>

<b>Steps to Paying for Items</b>

<ul>

  <li>Add items to your cart</li>

  <li>Go to your cart</li>

  <li>Enter your address details (for delivery) and click <em>add address/use this address</em></li>

  <li>Select payment method and click <em>pay for item</em></li>
</ul>

<b>
  Notes:

  <p>Make sure you enter a street address.</p>

  <p>If you are not logged-in, you will be able to add to your cart. However, to pay for the items in your cart, you must be logged-in.</p>
</b>
 * 
 */
