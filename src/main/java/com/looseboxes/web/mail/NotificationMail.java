/**
 * @(#)NotificationMail.java   25-Jun-2010 15:01:41
 *
 * Copyright 2009 BC Enterprise, Inc. All rights reserved.
 * BCE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.looseboxes.web.mail;

import com.bc.validators.ValidationException;
import com.looseboxes.core.LbApp;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import com.bc.html.HtmlGen;
import com.bc.mail.EmailBuilder;
import com.bc.mail.EmailBuilderImpl;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.html.ProductHtmlGen;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.UserBean;
import java.util.Locale;
import org.apache.commons.mail.EmailException;

/**
 * @author  chinomso bassey ikwuagwu
 * @version 1.0
 * @since   1.0
 */
public class NotificationMail extends InvitationMail {

    private String [] ids;
    
    public NotificationMail(
            HttpServletRequest request, String senderName, 
            String senderEmail, String senderPassword,
            String recipientName, String recipientEmail
    ) throws EmailException, ValidationException { 
        
        this(
                request,
                new EmailBuilderImpl(LbApp.getInstance().getMailConfig()),
                senderName, senderEmail, senderPassword,
                recipientName, recipientEmail
        );
    }
    
    public NotificationMail(
            HttpServletRequest request,
            EmailBuilder emailBuilder, 
            String senderName, String senderEmail, String senderPassword,
            String recipientName, String recipientEmail
    ) throws EmailException, ValidationException { 

        String productCategory = (String)ServletUtil.find(Product_.productcategoryid.getName(), request);
        
        MessageFormatter messageFormatter = 
                new MessageFormatter(productCategory, senderEmail, recipientEmail, recipientName);
//@related_7 productIds not productId. productIds seperated by commas
        
        // Notice how we add 's'
        //
        String idStr = (String)ServletUtil.find(Product_.productid.getName()+"s", request);
//Logger.getLogger(this.getClass().getName()).fine(this.getClass().getName()+". Ids: "+idStr);

        this.ids = idStr.split(",");
        
        String subj = senderName + " wants you to check these out.";
        
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        Locale locale = user.getLocale();
        
        StringBuilder messageBody = new StringBuilder();

        messageBody.append(this.getMessageText(senderName));

        messageBody.append(this.getItemsHtml(messageFormatter, locale));

        messageFormatter.format(NotificationMail.this, subj, messageBody.toString(), null);
        
        emailBuilder.build(NotificationMail.this, senderEmail, senderPassword, senderPassword != null, true);
        
        this.addTo(recipientEmail, recipientName);
    }
    
    public NotificationMail(
            EmailBuilder emailBuilder, MessageFormatter messageFormatter,
            String senderName, String senderEmail, String senderPassword,
            String recipientName, String recipientEmail, 
            String subject, StringBuilder messageText, StringBuilder itemsHtml) throws EmailException, ValidationException { 
        
        super(emailBuilder, messageFormatter,
                senderName, senderEmail, senderPassword, recipientName, recipientEmail,
                subject, messageText, itemsHtml);
    }

    @Override
    public StringBuilder getMessageText(String senderName) {

        StringBuilder builder = new StringBuilder();
        
        HtmlGen.AHREF(WebApp.getInstance().getBaseURL(), WebApp.getInstance().getName(), builder);
        
        String baseLink = builder.toString();
        builder.setLength(0);
        
        builder.append(senderName);
        builder.append("&nbsp;wants you to check these out at ");
        builder.append(baseLink);
        builder.append("<br/><br/>");

        return builder;
    }

    @Override
    public StringBuilder getItemsHtml(
            MessageFormatter messageFormatter, Locale locale) 
            throws EmailException {

        ProductHtmlGen recordParser = new ProductHtmlGen();
        recordParser.setExternalOutput(true);
        
        recordParser.setLocale(locale);

        StringBuilder builder = new StringBuilder();

        recordParser.appendSelected(this.ids, builder);

        Set<String> files = recordParser.getFiles();

        if(files != null) {
            
            messageFormatter.updateAttachments(this, files);
        }

//Logger.getLogger(this.getClass().getName()).info(this.getClass().getName()+"\n"+this.getAttachments());

        return builder;
    }
}//~END
