package aaa.util;


/**
 * @(#)SendOrderNoticeEmail.java   02-Jul-2015 10:15:35
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
public class SendOrderNoticeEmail extends AbstractSendMail {

    @Override
    public String getSubject(String email, String password) {
        String subject = "BuzzWears careline - Your order details 04";
        return subject;
    }

    @Override
    public String getMessage(String email, String password) {
        // Salutation (hi, etc) is automatically added
        String msg = 
"<p>Thanks for your patience and understanding.</p>\n" +
//"<p>We noticed you made 3 different sets of orders and will like you to choose the particular one for which an invoice is to be generated.</p>\n" +
//"<p>Details of the First order will be outlined below.</p>\n" +
//"<p>Two more emails will be sent to you, each outlining details of each of the other 2 orders. Once you have noted the particular order you want to be billed for, please ping admin. BBPin: <tt>55F156B8</tt></p>\n" +
//"<p><b>Details of First Order. (10 items total less shipping: N7,880)</b></p>\n";
//"<p><b>Details of Second Order. (10 items total less shipping: N6,930)</b></p>\n";
//"<p><b>Details of Third Order. (12 items total less shipping: N8,076)</b></p>\n";
"<p><b>Details of Fourth Order. (13 items total less shipping: N9,174)</b></p>\n";
        return msg;
    }

    @Override
    public String getProductIds() {
// First Order        
//        return "283,295,294,288,287,180,369,266,317,268";
// Second Order        
//        return "355,298,354,367,287,297,282,145,171,266";
// Third Order        
//        return "338,246,299,298,369,354,355,316,297,337,215,284";
// Possible Fourth Order        
        return "355,298,354,367,287,297,282,145,171,266,338,246,299";
    }
}
