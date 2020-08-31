package aaa.util;

import com.bc.util.Log;
import com.looseboxes.TestWebApp;
import com.bc.html.HtmlGen;
import com.bc.security.Encryption;
import com.bc.security.SecurityProvider;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @(#)SendMail.java   13-Jun-2015 09:15:35
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
public class SendMail {
    
    public static void main(String [] args) {
        try{
            if(!TestWebApp.isInitialized()) {
                TestWebApp.init();
            }
            int offset = 2000;
            int max = 1000;
            int batchSize = 20;
            long batchInterval = 60000;
            SendMail sendMail = new SendMail();
            sendMail.sendMailToFailedSubscribers2();
            sendMail.sendMailToManyNewsMinute(offset, max, batchSize, batchInterval);
//            sendMail.sendMailToManyBuzzwears(offset, max, batchSize, batchInterval);
        }catch(Exception e) {
            Log.getInstance().log(Level.WARNING, "Error sending mail", SendMail.class, e);
        }finally{
            try{
                TestWebApp.destroy();
            }catch(Exception e) {
                Log.getInstance().log(Level.WARNING, "Error destroying "+TestWebApp.class.getName(), SendMail.class, e);
            }
        }
    }

    public void sendMailToManyNewsMinute(int offset, int max, int batchSize, long batchInterval) {
//        MassMailBean instance = new MassMailBean(){
//            @Override
//            public MessageFormatter getMessageFormatter() throws ValidationException {
//                return null;
//            }
//            public String getProductCategory() {
//                return null;
//            }
//        };
//        instance.setRemote(true);
//        instance.setMobile(true);
        
//        instance.setSenderEmail("mail.newsminute@gmail.com");
//        instance.setSenderPassword("rAmC-1p5");
//        instance.setSenderEmail("looseboxes@gmail.com");
//        instance.setSenderPassword("m4ScSe-Vu");
//        instance.setBatchInterval(batchInterval);
//        instance.setBatchSize(batchSize);
//        instance.setMax(max);
//        instance.setOffset(offset); 
//        instance.setOnlyVerifiedRecipients(true);

//        instance.setRecipients("posh.bc@gmail.com,looseboxes@gmail.com,chinomsoikwuagwu@yahoo.com,coolbuyng@gmail.com");
        
//        instance.setSelectedProductIds(this.getProductIds());
        
//        instance.setSubject("Breaking news - Ramadan Starts Today");
        
        StringBuilder text = new StringBuilder();
        HtmlGen htmlGen = new HtmlGen();
        
// Opening div        
        htmlGen.tagStart("div", "style", "font-size:1.5em", text);
        
        text.append("<p>Muslims in Nigeria will tomorrow (today) join their counterparts around the world to commence this year’s Ramadan fast, following the declaration of moon-sighting by the President-General...<br/>");
        HtmlGen.AHREF("http://www.looseboxes.com/idisc/feed/197632.jsp", "Read more", text);
        text.append("</p>");

        text.append("<p>President Muhammadu Buhari has admitted that his age would limit his performance as the President of the country...<br/>");
        HtmlGen.AHREF("http://www.looseboxes.com/idisc/feed/195835.jsp", "Read more", text);
        text.append("</p>");
        
        text.append("<p>");
        HtmlGen.AHREF("http://www.looseboxes.com/downloads/newsminute.apk", "Download the <b>News Minute</b> android app", text);
        text.append(" to always get latest news ");
        HtmlGen.AHREF("http://www.looseboxes.com/downloads/newsminute.apk", "<b>here</b>", text);
        text.append("</p>");

        text.append(this.getWhyNewsMinute());
        
// Closing div        
        text.append("</div>");
        
//        instance.setText(text.toString());
//        instance.setLogLevel(Level.INFO);
//        instance.setUseBcc(true);
//        instance.start();
    }
    
    public void sendMailToManyBuzzwears(int offset, int max, int batchSize, long batchInterval) {
//looseboxes@gmail.com
//m4ScSe-Vu
        
//chinomsoikwuagwu@yahoo.com
//1CingOnli
        
//        MailConfig config = WebApp.getInstance().getMailConfig();
//        config.setDebug(true);
//        MassMailBean instance = new MassMailBean(){
//            public String getProductCategory() {
//                return null;
//            }
//        };
//        instance.setRemote(true);
//        instance.setMobile(true);
        
//        instance.setSenderEmail("BuzzWears@buzzwears.com");
//        instance.setSenderPassword("xM-nuPe3");
//        instance.setBatchInterval(batchInterval);
//        instance.setBatchSize(batchSize);
//        instance.setMax(max);
//        instance.setOffset(offset); // 8660 + 266,   
//        instance.setOnlyVerifiedRecipients(true);
//        instance.setRecipients("posh.bc@gmail.com,looseboxes@gmail.com,chinomsoikwuagwu@yahoo.com,coolbuyng@gmail.com");
        
//        instance.setSelectedProductIds(this.getProductIds());
        
//        instance.setSubject("You can't get this quality of baby/kids fashion cheaper elsewhere");
//        instance.setSubject("Brand new, europe imported baby/kids clothes starting at N699 plus");
//        instance.setSubject("Must see fashion at half the price only at BuzzWears.com");
        
        StringBuilder text = new StringBuilder();
        HtmlGen htmlGen = new HtmlGen();
        htmlGen.tagStart("div", "style", "font-size:1.5em", text);
        htmlGen.enclosingTag("p", "Seriously, you can't get brand new US/EU imported baby/kids fashion at this price anywhere else.", text);
        text.append("<p>And there's some ");
        final String linkText = "ladies fashion";
        try{
            Map params = new HashMap();
            params.put(Product_.productcategoryid.getName(), References.productcategory.Fashion);
            params.put(Product_.productsubcategoryid.getName(), References.productsubcategory.WomensClothing);
            HtmlGen.AHREF(ServletUtil.getURL(com.looseboxes.web.servlets.Search.class, params), linkText, text);
        }catch(MalformedURLException | RuntimeException e) {
            e.printStackTrace();
            text.append(linkText);
        }
        text.append(" too.</p>");
        htmlGen.enclosingTag("p", "Tell us what you think. <b>BB PIN: 55F156B8</b>", text);
        text.append("<p>");
        HtmlGen.AHREF(WebApp.getInstance().getBaseURL(), "Find out more", text);
        text.append("</p>");
        text.append("</div>");
//        instance.setText(text.toString());
//        instance.setLogLevel(Level.INFO);
//        instance.setUseBcc(true);
//        instance.start();
    }
    
    public void sendMailToFailedSubscribers() {
        
        Map<String, String> details = new HashMap<>();
//        details.put("safurahjimoh@gmail.com", "safuajose");
//        details.put("omotayaare@gmail.com", "buzzwears");
//        details.put("meetejiro247@gmail.com", "protection");
//        details.put("fauzeeyaah@gmail.com", "khalidaisha");
//        details.put("flakyde@yahoo.com", "folake");
//        details.put("flakyde@gmail.com", "folake");
        details.put("posh.bc@gmail.com", "test-password");
        details.put("chinomsoikwuagwu@yahoo.com", "test-password");
        
        for(String key:details.keySet()) {
            
            this.sendMailToFailedSubscriber(key, details.get(key));
        }
    }
        
    public void sendMailToFailedSubscribers2() throws GeneralSecurityException {
        
        Map<String, String> details = new HashMap<>();
        details.put("nelotache@yahoo.com", "a43376d2aca168720d6a8070dbb0b4ec");
        details.put("ikusimoabiola@gmail.com", "85afcfefedcacea94470525b5975e1fb");
        details.put("oluyemisi69@gmail.com", "fbf08cb40ad8f09f3302da36ba63f9d6");
        details.put("tyna4christ@yahoo.co.uk", "50b2a086ac02231b97ada6785e6688cc");
        details.put("ammyfn@yahoo.com", "64b7f1883c1e7c8f8df510fac62a53ce");
        details.put("maxiflexi@yahoo.com", "7235bc3a3580874aeeabdb0e3e1efacd");
        details.put("posh.bc@gmail.com", "a43376d2aca168720d6a8070dbb0b4ec");
        details.put("chinomsoikwuagwu@yahoo.com", "a43376d2aca168720d6a8070dbb0b4ec");
        
        
        Encryption encryption = SecurityProvider.DEFAULT.getEncryption("AES", "AcIcvwW2MU4sJkvBx103m6gKsePm");
        
        for(String key:details.keySet()) {

            String decrypted = new String(encryption.decrypt(details.get(key)));
            
            this.sendMailToFailedSubscriber(key, decrypted);
        }
    }
        
    public void sendMailToFailedSubscriber(String email, String password) {    
        
        if(email == null || password == null) {
            throw new UnsupportedOperationException();
        }
        
        String subject = "BuzzWears has validated your profile.";
        String msg = "<p>We noticed that your recent attempt to create a new profile on <a href=\"http://www.buzzwears.com\">buzzwears.com</a> was \n" +
"unsuccessful.</p>\n" +
"\n" +
"<p>Our appologies. We have rectified the error, created and validated your profile.</p>\n" +
"\n" +
"<p>\n" +
"  <p>You can may login with these details:</p>\n" +
"  <tt>Email address:</tt> <b>"+email+"</b><br/>\n" +
"  <tt>password:</tt> <b>"+password+"</b>\n" +
"</p>" + 
"<p><em><i><a href=\"http://www.buzzwears.com\">BuzzWears</a> - You can't get our quality of fashion cheaper elsewhere!</i></em></p>\n" +
"\n" +
"<p style=\"background-color:#EEDDEE; width:100%;\">\n" +
"  For enquiries contact: <tt><a href=\"mailto:support@buzzwears.com\">support@buzzwears.com</a></tt><br/>\n" +
"  BB Pin: <tt>55F156B8</tt>\n" +
"</p>";                
        this.sendMailToFailedSubscriber(email, subject, msg);
    }
    
    private void sendMailToFailedSubscriber(String recipient, String subject, String msg) {
        
//        MassMailBean instance = new MassMailBean(){
//            public String getProductCategory() {
//                return null;
//            }
//        };
//        instance.setRemote(true);
//        instance.setMobile(true);
        
//        instance.setSenderEmail("BuzzWears@buzzwears.com");
//        instance.setSenderPassword("xM-nuPe3");
//        instance.setRecipients(recipient);
        
//        instance.setSelectedProductIds(this.getProductIds());
        
//        instance.setSubject(subject);
        
//        instance.setText(msg);
//        instance.setLogLevel(Level.FINE);
//        instance.start();
    }
    
    public String getProductIds() {
// Products on local testing server
//        instance.setSelectedProductIds("7,10,13,16,19,22,25,28,31,33,36,39,42,45");
        
// Products on live server        
        return "111,129,126,139,145,152,150,169,99,106,109,110,118,130,200,195";
//        return "130,118,110,109,106,99,169,150,152,145,139,126,129,111";
//        return "111,129,126,139,145,152,150,169";
//        return "99,106,109,110,118,129,130,137";
//        return "142,153,154,161,165,167,174,176";
    }
    
    public String getWhyNewsMinute() {
        String why = "<p>Asked why they love the news minute app...\n" +
            "</p>\n" +
            "<ul>\n" +
            "    <li>\n" +
            "      I am always the first to know when breaking news occurs. This has been quite useful, for example when the Nyanya Bomb Blast occurred in Abuja, even though I was in Lagos at the time, I quickly called relatives to avoid the area.\n" +
            "      <br/><small>Banke, fashion designer - Abuja</small>\n" +
            "    </li>\n" +
            "    <li>\n" +
            "      The app is actually quite well organized for a Nigerian app. I also get news on sports and politics in one place.\n" +
            "      <br/><small>Obi, banker - Kano</small>\n" +
            "    </li>\n" +
            "    <li>\n" +
            "      I set the app to notify me anytime my Boss's name is mentioned in the news media. This makes my job very easy.\n" +
            "      <br/><small>Joy, publicist - Lagos</small>\n" +
            "    </li>\n" +
            "    <li>\n" +
            "      I think it's because it loads very fast and it doesn't consume much data.\n" +
            "      <br/><small>Moses, student - PH</small>\n" +
            "    </li>\n" +
            "    <li>\n" +
            "      As a reporter I need to be up to date with what's happening. News minute gives me quick and easy access to all thats happening in the news media including gossip and blogs.\n" +
            "      <br/><small>Alice, reporter - Lagos</small>\n" +
            "    </li>\n" +
            "    <li>\n" +
            "      The app is fast. I like that. I didn't know I needed the app until I tried it.\n" +
            "      <br/><small>Amina, business woman - Lagos</small>\n" +
            "    </li></ul>";        
        return why;
    }
}
