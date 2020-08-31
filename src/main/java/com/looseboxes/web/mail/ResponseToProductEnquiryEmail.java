package com.looseboxes.web.mail;

import com.bc.util.Log;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.ServletMailer;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @(#)ResponseToProductEnquiryEmail.java   04-Jul-2013 17:01:48
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
public class ResponseToProductEnquiryEmail extends ProductEnquiryEmail {
    
    private boolean decryptEmails;
    
    public ResponseToProductEnquiryEmail() { }

    @Override
    public void setRequest(HttpServletRequest request) 
            throws ServletException {
        
        StringBuilder emailTitle = new StringBuilder();
        emailTitle.append("A response has been made to your enquiry about a product on ");
        this.setSubject(emailTitle.append(WebApp.getInstance().getName()).toString());
        
        String fromEmail = this.getValue("fromEmail", "Sender Email", this.isDecryptEmails(), request);
        this.setFromEmail(fromEmail);

        String fromUsername = this.getValue("fromUsername", "Sender Username", false, request);
        this.setFromUsername(fromUsername);
        
        String toEmail = this.getValue("toEmail", "Recipient Email", this.isDecryptEmails(), request);
        this.setToEmail(toEmail);

        String toUsername = this.getValue("toUsername", "Recipient Username", false, request);
        this.setToUsername(toUsername);
        
        String sval = this.getValue(Product_.productid.getName(), "Product ID", false, request);
        
        this.setTargetProductId(Integer.parseInt(sval));
    }
    
    private String getValue(
            String name, String label, 
            boolean decrypt, HttpServletRequest request) 
            throws ServletException {
        
        String val = (String)ServletUtil.find(name, request);
Log.getInstance().log(Level.FINE, "Name: {0}, Value: {1}", this.getClass(), name, val);        
        this.checkNull(label, val);
        if(decrypt) {
            try{
                val = this.decrypt(val);
            }catch(GeneralSecurityException e) {
                String msg = "Failed to decrypt value required to respond to product enquiry.";
                new ServletMailer(request, Level.ALL).sendError(e, msg);
                throw new ServletException(msg+" Site admin has been notified", e);
            }
Log.getInstance().log(Level.FINE, "Decrypted val: {0}", this.getClass(), val);        
        }
        return val;
    }
    
    public void initMessage(HttpServletRequest request) 
            throws ServletException {
        
        // This is not the email subject
        String messageSubject = (String)ServletUtil.find("messageSubject", request);
        String messageText = (String)ServletUtil.find("messageText", request);

        this.checkNull("Message Text", messageText);
        
        this.initMessage(messageSubject, messageText, request);
    }

    public boolean isDecryptEmails() {
        return decryptEmails;
    }

    public void setDecryptEmails(boolean decryptEmails) {
        this.decryptEmails = decryptEmails;
    }
}
