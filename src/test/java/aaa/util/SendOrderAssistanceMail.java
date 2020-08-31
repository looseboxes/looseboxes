package aaa.util;


/**
 * @(#)SendOrderAssistanceMail.java   30-Jun-2015 23:52:46
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
public class SendOrderAssistanceMail extends AbstractSendMail {

    @Override
    public String getSubject(String email, String password) {
        String subject = "BuzzWears careline - Help us assist you";
        return subject;
    }

    @Override
    public String getMessage(String email, String password) {
        // Salutation (hi, etc) is automatically added
        String msg = 
"<p>We noticed you had problems using BuzzWears shopping cart.</p>\n" +
"<p>Appologies. Kindly, follow these steps to help us assist you.</p>\n" +
"<ul>\n" +
"<li>Browse to <a href=\"http://www.buzzwears.com\">BuzzWears.com</a> <b>using the same device</b> you used to place the order.</li>\n" +
"<li>Go to your shopping cart</li>\n" +
"<li><b>Login</b> if you are not logged in.</li>\n" +
"<li>Then ping admin @ BBPIN: 55F156B8</li>\n" +
"</ul>\n" +
"<p>This will enable us view your order, generate and send you an invoice for payment.</p>\n" +
                
//////////////////////////// ///////////////////////////////                
"<p style=\"background-color:#EEDDEE; width:100%;\">\n" +
"  For enquiries contact: <tt><a href=\"mailto:support@buzzwears.com\">support@buzzwears.com</a></tt><br/>\n" +
"  BB Pin: <tt>55F156B8</tt>\n" +
"</p>";                
        return msg;
    }
}
/**
 * 
<p>We noticed you had problems using BuzzWears shopping cart.</p>

<p>Appologies. Kindly, follow these steps to help us assist you.</p>

<ul>
<li>Browse to buzzwears.com using the same device you used.</li>

<li>Go to your shopping cart</li>

<li><b>Login</b> if you are not logged in.</li>

<li>Then ping admin @ BBPIN 55F156B8</li>
</ul>

<p>This will enable us view your order and send you an invoice for payment.</p>
 * 
 */