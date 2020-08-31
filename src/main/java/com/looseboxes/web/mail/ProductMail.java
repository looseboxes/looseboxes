/**
 * @(#)ProductMail.java   12-Feb-2011 09:40:38
 *
 * Copyright 2009 BC Enterprise, Inc. All rights reserved.
 * BCE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.looseboxes.web.mail;

import com.bc.mail.EmailBuilder;
import com.bc.mail.EmailBuilderImpl;
import com.bc.util.Log;
import com.bc.validators.ValidationException;
import com.looseboxes.core.LbApp;
import com.looseboxes.web.WebApp;
import java.util.logging.Level;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * <p>How to use:</p>
 * <pre>
 ProductMail productMail = new ProductMail();
 productMail.setSenderName("ismail");
 productMail.setRecipientName("kobi");  // May be null
 productMail.setRecipientEmail("kobi@yahoo.com");
 productMail.setProductCategory("jobs);      
 productMail.updateMessageText();
 productMail.updateItemsHtml();
 productMail.init();

 // Send the mail
 </pre>
 * <p>
 * When a single sender is sending to many recipients within the same
 * {@link javax.servlet.http.HttpServletRequest request} do this:
 * </p>
 * <pre>
 * ProductMail productMail = new ProductMail();
 * productMail.setSenderName("ismail");
 * productMail.setRequest(aRequest);      // May be null
 * productMail.updateItemsHtml();
 * Map<String, String> recipients = new Hashtable<String, String>();
 * Iterator<Entry<String, String>> entries = recipients.entrySet().iterator();
 * while(entries.hasNext()) {
 *   Entry<String, String> entry = entries.next();
 *   String recipientEmail = entry.getKey();
 *   String recipientName = entry.getValue();
 *   productMail.setRecipientEmail(recipientEmail);
 *   productMail.setRecipientName(recipientName);
 *   productMail.updateMessageText();
 *   productMail.init();
 *
 *   // Send this mail
 * }
 * </pre>
 *
 * @author  chinomso bassey ikwuagwu
 * @version 1.0
 * @since   1.0
 */
public class ProductMail extends HtmlEmail {

    public ProductMail() {}
    
    public ProductMail(
            String productCategory, String senderName, 
            String senderEmail, String senderPassword,
            String recipientName, String recipientEmail, 
            String subject, StringBuilder messageText, StringBuilder itemsHtml) throws EmailException, ValidationException { 
    
        this(
                new EmailBuilderImpl(LbApp.getInstance().getMailConfig()),
                new MessageFormatter(productCategory, senderEmail, recipientEmail, recipientName),
                senderName, senderEmail, senderPassword,
                recipientName, recipientEmail, subject, messageText, itemsHtml
        );
    }
    
    public ProductMail(
            EmailBuilder emailBuilder, 
            MessageFormatter messageFormatter,
            String senderName, String senderEmail, String senderPassword,
            String recipientName, String recipientEmail, 
            String subject, StringBuilder messageText, StringBuilder itemsHtml) throws EmailException, ValidationException { 
        
        StringBuilder messageBody = new StringBuilder();

        if(messageText != null) {
            messageBody.append(messageText);
        }
        if(itemsHtml != null) {
            messageBody.append(itemsHtml);
        }

        messageFormatter.format(ProductMail.this, subject, messageBody.toString(), null);
        
        emailBuilder.build(ProductMail.this, senderEmail, senderPassword, senderPassword != null, true);
        
        this.addTo(recipientEmail, recipientName);
    }


    @Override
    public String send() throws EmailException {
        if(!WebApp.getInstance().isProductionMode()) {
            Log.getInstance().log(Level.INFO, "Production mode: false. Email message will not be sent", this.getClass());
            return null;
        }else{
            return super.send();
        }
    }
}//~END
