package com.looseboxes.web.mail;

import com.bc.util.Log;
import com.bc.validators.ValidationException;
import com.bc.validators.Validator;
import com.bc.validators.ValidatorFactory;
import com.bc.html.HtmlGen;
import com.bc.security.Encryption;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.servlets.Sendmail;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.logging.Level;


/**
 * @(#)MessageFormatter.java   30-May-2015 11:33:01
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
public class MessageFormatter extends AbstractMessageFormatter {

    /**
     * @param productCategoryName. May be null
     * @param senderEmail The sender email address. May be null in which case the 
     *        default address is used. The default address is gotten by passing 
     *        the value {@linkplain org.lb.mail.MailUtils#address_default_noreply} into 
     *        the method {@linkplain org.lb.mail.MailUtils#getAddress(java.lang.String)} 
     * @param recipientEmail. May be null
     * @param recipientName. May be null
     * @throws ValidationException If the email address format is invalid
     */
    public MessageFormatter(
            String productCategoryName, String senderEmail, 
            String recipientEmail, String recipientName) 
            throws ValidationException {
        
        Validator v = ValidatorFactory.newValidator(ValidatorFactory.EMAIL_CHECK);
        
        if(recipientEmail != null) {
            
            v.validate(recipientEmail);

            if (recipientName == null || recipientName.length() == 0) {
                recipientName = recipientEmail.substring(0, recipientEmail.indexOf('@'));
            }        
        }
        
        this.setProductcategory(productCategoryName);
        
        if(senderEmail == null) {
            String defaultSender = WebApp.getInstance().getConfig().getString(AppProperties.address_default_noreply);
            this.setSender(defaultSender);
        }
        
        this.setRecipient(recipientEmail);
        this.setRecipientName(recipientName);
    }

    /**
     * @return The filename of the logo in the WEB-INF/images directory e.g site_logo.jpg OR logo.png etc
     */
    private String _lfn;
    @Override
    public String getLogoFilename() {
        if(_lfn ==null) {
            _lfn = WebApp.getInstance().getConfig().getString(AppProperties.LOGO_FILENAME);
        }
        return _lfn;
    }
    
    @Override
    protected String getInvitationLink() {
        if(this.getRecipient() == null || this.getRecipientName() == null) {
            return null;
        }
//http://looseboxes.com/sendMail?mt=invitation&mse=px9o4qs95hUW.7Rb5ecF3@UWgV7mRxamAiPGlG4.7RcF3o4qmRx&msn=iPGkWBwF3umAamAgV7wF3umA
        HashMap params = new HashMap(3, 1.0f);
        params.put(Sendmail.MAIL_TYPE, Sendmail.INVITATION);
        Encryption sy = WebApp.getInstance().getEncryption();
        try{
            params.put(Sendmail.SENDER_EMAIL, sy.encrypt(this.getRecipient().toCharArray()));
            params.put(Sendmail.SENDER_NAME, sy.encrypt(this.getRecipientName().toCharArray()));
            String href = ServletUtil.getURL(Sendmail.class, params);
            return HtmlGen.AHREF(href, "Invite a friend!", new StringBuilder()).toString();
        }catch(GeneralSecurityException | MalformedURLException e) {
            Log.getInstance().log(Level.WARNING, "Error creating invitation link", this.getClass(), e);
            return null;
        }
    }

    @Override
    protected String getUnsubscribeLink() {
        return null;
    }
    
    @Override
    public String getReturnURL() {
        try{
            return WebApp.getInstance().getContextURL().toExternalForm();
        }catch(MalformedURLException e) {
            Log.getInstance().log(Level.WARNING, "Error context URL", this.getClass(), e);
            return null;
        }
    }
}
