package com.looseboxes.web.servlets;

import com.bc.util.Log;
import com.bc.jpa.controller.EntityController;
import com.looseboxes.pu.entities.Email;
import com.looseboxes.pu.entities.Email_;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.mail.DefaultHtmlEmail;
import com.looseboxes.web.mail.ProductEnquiryEmail;
import com.looseboxes.web.mail.ProductEnquiryEmailIx;
import com.looseboxes.web.mail.ResponseToProductEnquiryEmail;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.HtmlEmail;
import com.bc.jpa.context.JpaContext;

/**
 * @(#)ContactUser.java   10-Jun-2013 17:20:45
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
/**
 * Handles requests to: contact a user.
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Contactux", urlPatterns={"/contactux"})
public class Contactux extends BaseServlet {
    
    private String errorPage;
    
    private String forwardPage;

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return errorPage;
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return forwardPage;
    }
    
    @Override
    public void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
        
        String name = "type";

        String type = (String)ServletUtil.find(name, request);

Log.getInstance().log(Level.FINE, "{0} = {1}", this.getClass(), name, type);

        ProductEnquiryEmail email;
        
        if(null != type) switch (type) {
            case "penq":
                this.errorPage = WebPages.PRODUCTS_SEARCHRESULTS;
                this.forwardPage = WebPages.PRODUCTS_SEARCHRESULTS;
                email = new ProductEnquiryEmail();
                email.setRequest(request);
Log.getInstance().log(Level.FINE, "{0}", this.getClass(), email);
                try{
                    this.sendEmail(email);
                }catch(MessagingException e) {
                    throw new ServletException("Error encountered sending message", e);
                }   
                break;
            case "toRes":
                email = new ResponseToProductEnquiryEmail();
                ((ResponseToProductEnquiryEmail)email).setDecryptEmails(true);
                email.setRequest(request);
                request.setAttribute(ResponseToProductEnquiryEmail.class.getSimpleName(), email);
                this.forwardPage = WebPages.MESSAGING_RESPONDTOENQUIRY;
                break;
            case "res":
                email = new ResponseToProductEnquiryEmail();
                ((ResponseToProductEnquiryEmail)email).setDecryptEmails(false);
                email.setRequest(request);
                ((ResponseToProductEnquiryEmail)email).initMessage(request);
Log.getInstance().log(Level.FINE, "{0}", this.getClass(), email);
                try{
                    this.sendEmail(email);
                }catch(MessagingException e) {
                    throw new ServletException("Error contacting user by email", e);
            }   break;
        }
    }
    
    private void sendEmail(ProductEnquiryEmailIx email) throws MessagingException {
        
        JpaContext cf = WebApp.getInstance().getJpaContext();
        EntityController<Email, Integer> ec = cf.getEntityController(Email.class, Integer.class);
        
        Map record = this.getRecord(
                email.getFromEmail(), email.getToEmail(), 
                email.getSubject(), email.getMessage(), "text/html");

        Email emailEntity = ec.persist(record);

Log.getInstance().log(Level.FINER, "Email: {0}", this.getClass(), email);

        try{
            
            HtmlEmail htmlEmail = new DefaultHtmlEmail();
            htmlEmail.setSubject(email.getSubject());
            htmlEmail.setHtmlMsg(email.getMessage());
            htmlEmail.addTo(email.getToEmail(), email.getToUsername());

            htmlEmail.send();
            
        }catch(Exception e) {
            Log.getInstance().log(Level.WARNING, "Error sending email to "+email.getToEmail(), this.getClass(), e);
        }
            
        this.addMessage(MessageType.informationMessage, "Your message has been successfully sent");

        if(emailEntity != null) {
            
            emailEntity.setSent(true);
            
            try{
                ec.merge(emailEntity);
            }catch(Exception e) {
                Log.getInstance().log(Level.WARNING, "Failed to edit email status to 'sent'.", this.getClass(), e);
            }
            
        }else{
            
Log.getInstance().log(Level.WARNING, "Record not found. Parameters: {0}", this.getClass(), record);
        }
    }
    
    private Map getRecord(String fromEmail, String [] toEmails, String subject, String text, String contentType) {
        return getRecord(fromEmail, Contactux.toString(toEmails), subject, text, contentType);
    }
    
    private Map getRecord(String fromEmail, String toEmail, String subject, String text, String contentType) {
        HashMap email = new HashMap(5, 1.0f);
        email.put(Email_.fromEmail.getName(), fromEmail);
        email.put(Email_.toEmails.getName(), toEmail);
        email.put(Email_.emailSubject.getName(), subject);
        email.put(Email_.emailText.getName(), text);
        email.put(Email_.contentType.getName(), contentType);
        return email;
    }

    public static String toString(String[] a) {
        if (a == null)
            return null;
	int iMax = a.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(a[i]);
            if (i == iMax)
		return b.toString();
	    b.append(", ");
        }
    }
}
