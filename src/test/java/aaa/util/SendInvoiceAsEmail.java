package aaa.util;


/**
 * @(#)SendInvoiceAsEmail.java   13-Jul-2015 16:41:32
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
public class SendInvoiceAsEmail extends AbstractSendMail {

    @Override
    public String getSubject(String email, String password) {
        String subject = "Details of your order on BuzzWears";
        return subject;
    }

    @Override
    public String getMessage(String email, String password) {
        // Salutation (hi, etc) is automatically added
        String msg = 
"<p>You recently placed an order on <a href=\"http://www.buzzwears.com\">BuzzWears.com</a>.</p>\n" +
"<p>Here are the order details.</p>\n" +
"<div style=\"background-color:#EEEEEE;\">\n" +                
"<p><b>Order Summary</b></p>\n" +                
"<table>\n" +
"<tr><th>S/N</th><th>Description</th><th>Amount (NGN)</th></tr>\n" +
"<tr><td>1</td><td>Baby/Kids clothing (27 pcs)</td><td style=\"float:right\">26,797.00</td></tr>\n" +
"<tr><td>2</td><td>Delivery (Gwarimpa - Abuja)</td><td style=\"float:right\"> 2,000.00</td></tr>\n" +
"<tr><td>3</td><td><b>Total</b></td><td style=\"float:right\">28,797.00</td></tr>\n" +
"</table>\n" +
"<p><b>       Addressee:</b> Mrs Onyinye Onianwa</p>\n" +
"<p><b>Delivery Address:</b> 8 Onwuegbuzie Street, behind Konwea Plaza, off Nnebisi Road, Asaba</p>\n" +
"<p><b>   Delivery Time:</b> Before 12 noon 16 September 2015.</p>\n" +
"<p><b>    Payment Type:</b> Bank Deposit.</p>\n" +
"</div>\n" +                
"<p>Scroll down for details of ordered items.</p>\n" +
//////////////////////////// ///////////////////////////////                
"<p style=\"background-color:#EEDDEE; width:100%;\">\n" +
"  For enquiries contact: <tt><a href=\"mailto:support@buzzwears.com\">support@buzzwears.com</a></tt><br/>\n" +
"  BB Pin: <tt>55F156B8</tt>\n" +
//"  BB Pin: <tt>7FDDED4E</tt>\n" +
"</p>";                
        return msg;
    }
    @Override
    public String getProductIds() { 
        return "306,109,202,315,288,289,329,126,131,127,148,351,139,138,300,303,235,234,180,368,302,212,18,344,345,221,307";
    }
}
