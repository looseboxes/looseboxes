package aaa.util;


/**
 * @(#)SendMailBase.java   30-Jun-2015 23:46:05
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
public abstract class AbstractSendMail {
    
    public abstract String getSubject(String email, String password);
    
    public abstract String getMessage(String email, String password);

    /**
     * @param recipients String containing comma separated list of recipients
     * @param password 
     */
    public void sendMail(String recipients, String password) {    
        
        if(recipients == null) {
            throw new UnsupportedOperationException();
        }
        
        String subject = this.getSubject(recipients, password);
        
        String msg = this.getMessage(recipients, password);
        
        this.sendMail(recipients, subject, msg);
    }
    
    /**
     * @param recipients String containing comma separated list of recipients
     * @param subject
     * @param msg 
     */
    public void sendMail(String recipients, String subject, String msg) {
        
//        instance.setRemote(true);
//        instance.setMobile(true);
    
// The BuzzWears part of the email is often used as the user name by 
// the mail client; hence the camel case
//        instance.setSenderEmail("BuzzWears@buzzwears.com");
//        instance.setSenderPassword("xM-nuPe3");
//        instance.setRecipients(recipients);
        
//        instance.setSelectedProductIds(this.getProductIds());
        
//        instance.setSubject(subject);
        
//        instance.setText(msg);
//        instance.setLogLevel(Level.FINE);
//        instance.start(); 
    }
    
    public String getProductIds() {
//#3 live-items:Dec 15 upper price range
//sponsoredItems=483,496,507,501,475,445,478,279,110,306,509,488,485,252,442,486

//#3 live-items:Dec 15 lower price range
//#sponsoredItems=455,286,463,412,454,457,456,139,218,485,252,442,486,152,176,124
return "455,286,412,454,457,456,218,485,252,442,486,152,176,124";        
        
// Products on local testing server
//        instance.setSelectedProductIds("7,10,13,16,19,22,25,28,31,33,36,39,42,45");
        
// Products on live server        
//        return "111,129,126,139,145,152,150,169,99,106,109,110,118,130,200,195";
//        return "130,118,110,109,106,99,169,150,152,145,139,126,129,111";
//        return "111,129,126,139,145,152,150,169";
//        return "99,106,109,110,118,129,130,137";
//        return "142,153,154,161,165,167,174,176";
    }
}
