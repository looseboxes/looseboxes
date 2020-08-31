package com.looseboxes.web.mail;

import com.bc.util.Log;
import com.bc.html.HtmlGen;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.ServletMailer;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.servlets.Respondtoux;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;

/**
 * @(#)ProductEnquiryEmail.java   04-Jul-2013 16:33:18
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
public class ProductEnquiryEmail implements ProductEnquiryEmailIx{
    
    private String subject;
    private StringBuilder message;
    private String fromEmail;
    private String fromUsername;
    private String toEmail;
    private String toUsername;        
    private Integer targetProductId;
    
    public ProductEnquiryEmail() { }

    public void setRequest(HttpServletRequest request) 
            throws ServletException {
        
        StringBuilder emailTitle = new StringBuilder();
        emailTitle.append("An equiry has been made about your product on ");
        subject = emailTitle.append(WebApp.getInstance().getName()).toString();

        String sval = (String)ServletUtil.find(Product_.productid.getName(), request);

        this.checkNull("Product ID", sval);
        
        this.targetProductId = Integer.parseInt(sval);

        toEmail = (String)ServletUtil.find("toEmail", request);
        toUsername = (String)ServletUtil.find("toUsername", request);

        if(toEmail == null) {

            if(toUsername != null) {

                JpaContext cf = WebApp.getInstance().getJpaContext();

                try(Select<String> qb = cf.getDaoForSelect(Siteuser.class, String.class)) {
                    toEmail = qb.from(Siteuser.class)
                    .select(Siteuser_.emailAddress.getName())
                    .where(Siteuser_.username.getName(), toUsername)
                    .createQuery().getSingleResult();
                }catch(javax.persistence.NoResultException noNeedToLog) {
                    toEmail = null;
                }
                
                if(toEmail == null) {

                    try(Select<Product> qb = cf.getDaoForSelect(Product.class)) {
                        
                        Product product = qb.from(Product.class)
                        .where(Product_.productid.getName(), this.targetProductId)
                        .createQuery().getSingleResult();
                        if(product != null && product.getSeller() != null) {
                            toEmail = product.getSeller().getEmailAddress();
                        }
                    }catch(javax.persistence.NoResultException noNeedToLog) { }

                    if(toUsername == null && toEmail != null) {

                        try(Select<String> qb = cf.getDaoForSelect(Siteuser.class, String.class)) {
                            toUsername = qb.from(Siteuser.class)
                            .select(Siteuser_.username.getName())
                            .where(Siteuser_.emailAddress.getName(), toEmail)
                            .createQuery().getSingleResult();
                        }catch(javax.persistence.NoResultException noNeedToLog) {
                            toUsername = null;
                        }
                    }
                }
            }
        }
Log.getInstance().log(Level.FINE, "To email: {0}, To username: {1}", 
        this.getClass(), toEmail, toUsername);        

        this.checkNull("Recipient Email Address", toEmail);
        this.checkNull("Recipient Username", toUsername);
        
        // This is not the email subject
        String messageSubject = (String)ServletUtil.find("messageSubject", request);
        String messageText = (String)ServletUtil.find("messageText", request);

        this.checkNull("Message Text", messageText);
        
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        this.fromEmail = user.getEmailAddress();
        this.fromUsername = user.getName();
        
        this.initMessage(messageSubject, messageText, request);
        
Log.getInstance().log(Level.FINE, "{0}", this.getClass(), this);        
    }
    
    protected void checkNull(String name, Object oval) throws ServletException {
        if(name == null) name = "";
        if(oval == null) throw new ServletException("Please enter a value for required input: "+name);
    }
    
    protected void initMessage(String messageSubject, String messageText,
            HttpServletRequest request) throws ServletException{
        
        message = new StringBuilder("This message was sent by ");
        message.append(getFromUsername());
        message.append(" concerning product with ID: <tt>");
        message.append(getTargetProductId());
        message.append("</tt><br/><br/>");
        if(messageSubject != null) {
            message.append("Subject: ");
            message.append(messageSubject);
            message.append("<br/><br/>");
        }
        message.append(messageText);
        
        // Response option
        
        HashMap m = new HashMap(7, 1.0f);
        
        try{
            m.put("fromEmail", encrypt(this.getToEmail()));
            m.put("fromUsername", this.getToUsername());
            m.put("toEmail", encrypt(this.getFromEmail()));
        }catch(GeneralSecurityException e) {
            String msg = "Failed to decrypt value required to respond to product enquiry.";
            new ServletMailer(request, Level.ALL).sendError(e, msg);
            throw new ServletException(msg+" Site admin has been notified", e);
        }
        
        m.put("toUsername", this.getFromUsername());
        m.put(Product_.productid.getName(), this.getTargetProductId());
        m.put("type", "toRes");

        message.append("<br/><br/>");
        
        try{
            
            String url = ServletUtil.getURL(Respondtoux.class, m, true);
Log.getInstance().log(Level.FINE, "URL: {0}", this.getClass(), url);

            HtmlGen.AHREF(url, "Click here to respond", message);

        }catch(MalformedURLException e) {
            
            Log.getInstance().log(Level.WARNING, "Error creating URL for servlet: " + Respondtoux.class.getName(), this.getClass(), e);
        }
    }
    
    protected String encrypt(String val) throws GeneralSecurityException {
        return WebApp.getInstance().getEncryption().encrypt(val.toCharArray());
    }

    protected String decrypt(String val) throws GeneralSecurityException {
        return new String(WebApp.getInstance().getEncryption().decrypt(val));
    }
    
    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public Set<String> getAttachments() {
        return null;
    }

    @Override
    public String getMessage() {
        return this.message == null ? null : this.message.toString();
    }

    @Override
    public String getFromEmail() {
        return fromEmail;
    }

    @Override
    public String getFromUsername() {
        return fromUsername;
    }

    @Override
    public Integer getTargetProductId() {
        return targetProductId;
    }

    @Override
    public String getToEmail() {
        return toEmail;
    }

    @Override
    public String getToUsername() {
        return toUsername;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public void setMessage(StringBuilder message) {
        this.message = message;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTargetProductId(Integer targetProductId) {
        this.targetProductId = targetProductId;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getName());
        builder.append(' ');
        builder.append("From: ").append(this.getFromUsername());
        builder.append(' ').append(this.getFromEmail());
        builder.append(" To: ").append(this.getToUsername());
        builder.append(' ').append(this.getToEmail());
        builder.append(" Product ID: ").append(this.getTargetProductId());
        builder.append(" Subject: ").append(this.getSubject());
        builder.append(" Message length: ").append(this.getMessage()==null?-1:this.getMessage().length());
        return builder.toString();
    }
}
