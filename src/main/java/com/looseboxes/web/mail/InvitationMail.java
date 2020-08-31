/**
 * @(#)InvitationMail.java   24-Jun-2010 14:39:09
 *
 * Copyright 2009 BC Enterprise, Inc. All rights reserved.
 * BCE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.looseboxes.web.mail;

import com.bc.validators.ValidationException;
import java.util.Locale;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import com.bc.util.Log;
import com.looseboxes.core.LbApp;
import com.bc.html.HtmlGen;
import com.bc.mail.EmailBuilder;
import com.bc.mail.EmailBuilderImpl;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.html.ProductHtmlGen;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.UserBean;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import org.apache.commons.mail.EmailException;

/**
 * @author  chinomso bassey ikwuagwu
 * @version 1.0
 * @since   1.0
 */
public class InvitationMail extends ProductMail {

    public InvitationMail() {
    }

    public InvitationMail(
            HttpServletRequest request,
            String productCategory, String senderName, 
            String senderEmail, String senderPassword,
            String recipientName, String recipientEmail
    ) throws EmailException, ValidationException { 
        
        this(
                request,
                new EmailBuilderImpl(LbApp.getInstance().getMailConfig()),
                new MessageFormatter(productCategory, senderEmail, recipientEmail, recipientName),
                senderName, senderEmail, senderPassword,
                recipientName, recipientEmail
        );
    }
    
    public InvitationMail(
            HttpServletRequest request,
            EmailBuilder emailBuilder, 
            MessageFormatter messageFormatter,
            String senderName, String senderEmail, String senderPassword,
            String recipientName, String recipientEmail
    ) throws EmailException, ValidationException { 

        
        String subj = senderName + " wants you to link up at "+this.getSitenameLink();
        
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        Locale locale = user.getLocale();
        
        StringBuilder messageBody = new StringBuilder();

        messageBody.append(this.getMessageText(senderName));

        messageBody.append(this.getItemsHtml(messageFormatter, locale));

        messageFormatter.format(InvitationMail.this, subj, messageBody.toString(), null);
        
        emailBuilder.build(InvitationMail.this, senderEmail, senderPassword, senderPassword != null, true);
        
        this.addTo(recipientEmail, recipientName);
    }
    
    public InvitationMail(
            EmailBuilder emailBuilder, 
            MessageFormatter messageFormatter,
            String senderName, String senderEmail, String senderPassword,
            String recipientName, String recipientEmail, 
            String subject, StringBuilder messageText, StringBuilder itemsHtml) throws EmailException, ValidationException { 
        
        super(emailBuilder, messageFormatter, 
                senderName, senderEmail, senderPassword, recipientName, recipientEmail,
                subject, messageText, itemsHtml);
    }
    
    public UserBean getUser(HttpServletRequest request) {
        return (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
    }
    
    public String getSitenameLink() {
        try{
            return HtmlGen.AHREF(WebApp.getInstance().getContextURL().toExternalForm(), 
                    WebApp.getInstance().getName(), new StringBuilder()).toString();    
        }catch(MalformedURLException e) {
            Log.getInstance().log(Level.WARNING, "Unexpected error", this.getClass(), e);
            return WebApp.getInstance().getName();
        }
    }

    public StringBuilder getMessageText(String senderName) {

        String sitenameLink = this.getSitenameLink();
        
        StringBuilder messageText = new StringBuilder("You have been invited by ");
        messageText.append(senderName).append(" to ").append(sitenameLink).append("<br/>");
        messageText.append(sitenameLink).append("&nbsp;is where you find giveaway prices for everything<br/><br/>Check these out:<br/>");

        return messageText;
    }

    public StringBuilder getItemsHtml(MessageFormatter messageFormatter, Locale locale) throws EmailException {

        ProductHtmlGen recordParser = new ProductHtmlGen();
        recordParser.setExternalOutput(true);

        recordParser.setLocale(locale);

        StringBuilder builder = appendLastItems(recordParser, new StringBuilder(), 20, locale);

        Set<String> files = recordParser.getFiles();

        if(files != null) {
            
            messageFormatter.updateAttachments(this, files);
        }
//Logger.getLogger(this.getClass().getName()).info(this.getClass().getName()+".updated MyAttachments: "+getAttachments().size());

        return builder;
    }

    public StringBuilder appendLastItems(ProductHtmlGen recordParser, StringBuilder builder, int itemCount, Locale locale) {

        Map searchParams = Collections.singletonMap(Product_.availabilityid.getName(), References.availability.InStock);
        
        // Important
        recordParser.setLocale(locale);

        // Generate HTML and append
        
        recordParser.appendLast(itemCount, searchParams, builder);

        return builder;
    }
}//~END
