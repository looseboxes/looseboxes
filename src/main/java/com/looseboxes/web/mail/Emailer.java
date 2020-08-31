/**
 * @(#)MyEmailer.java   22-Feb-2011 08:21:41
 *
 * Copyright 2009 BC Enterprise, Inc. All rights reserved.
 * BCE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.looseboxes.web.mail;

import com.bc.util.Log;
import com.looseboxes.web.AppProperties;
import com.looseboxes.core.Util;
import com.looseboxes.web.WebApp;
import java.util.Date;
import java.util.logging.Level;
import javax.xml.bind.ValidationException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author  chinomso bassey ikwuagwu
 * @version 1.0
 * @since   1.0
 */
public class Emailer {

    public Emailer() {  }

    public StringBuilder format(StringBuilder email) {
        return email;
    }
    
    public void sendError(
            final Exception t, final Object msg, final Level level) {

        if(t != null && t instanceof ValidationException) {
Log.getInstance().log(Level.WARNING, "ValidationExceptions are currently not sent via email", this.getClass());
            return;
        }

        StringBuilder builder = new StringBuilder();
        if(msg != null) {
            builder.append(msg);
        }

        if(t != null) {
            if(level == Level.FINER) {
                builder.append("<br/>");
                builder.append(Util.getStackTrace(t));
            }else {
                builder.append("<br/>");
                builder.append(t);
            }
        }

        final String title = "Application Error: "+new Date();
        
        builder = format(builder);

        String defaultRecipient = WebApp.getInstance().getConfig().getString(AppProperties.address_default_error);
        
        this.sendMessage(title, builder.toString(), defaultRecipient);
    }
    
    public void sendNotice(String msg) {
        String siteName = WebApp.getInstance().getName();
        this.sendMessage("NOTICE - "+siteName+": "+new Date(), msg);
    }
    
    public void sendErrorMessage(String msg) {
        String defaultRecipient = WebApp.getInstance().getConfig().getString(AppProperties.address_default_error);
        String siteName = WebApp.getInstance().getName();
        this.sendMessage("APPLICATION ERROR - "+siteName+": "+new Date(), msg, defaultRecipient);
    }

    public boolean sendMessage(String subject, String message) {
        
        String defaultRecipient = WebApp.getInstance().getConfig().getString(AppProperties.address_default_notice);
        
        return sendMessage(subject, message, defaultRecipient);
    }
    
    public boolean sendMessage(String subject, String message, String recipient) {
        try{
            HtmlEmail email = new DefaultHtmlEmail();
            email.setSubject(subject);
            email.setHtmlMsg(message);
            email.send();
            return true;
        }catch(EmailException e) {
            String msg = "Error sending email to: " + recipient;
            Log.getInstance().log(Level.WARNING, recipient, this.getClass(), e);
            return false;
        }
    }
    
    public boolean sendMessage(String subject, String message, String... recipients) {
        try{
            HtmlEmail email = new DefaultHtmlEmail();
            email.setSubject(subject);
            email.setHtmlMsg(message);
            email.send();
            return true;
        }catch(EmailException e) {
            StringBuilder builder = new StringBuilder();
            builder.append("Error sending email to '").append(recipients[0]);
            if(recipients.length > 1) {
                builder.append(" and ").append(recipients.length-1).append(" others.");
            }
            Log.getInstance().log(Level.WARNING, builder.toString(), this.getClass(), e);
            return false;
        }
    }
}//~END
