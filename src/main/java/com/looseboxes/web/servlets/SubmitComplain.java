package com.looseboxes.web.servlets;

/**
 * @(#)SubmitComplain.java   30-May-2015 17:37:07
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.looseboxes.pu.entities.Supportticket;
import com.looseboxes.pu.entities.Supportticket_;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.mail.DefaultHtmlEmail;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import com.bc.jpa.context.JpaContext;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="SubmitComplain", urlPatterns={"/submitComplain"})
public class SubmitComplain extends BaseServlet {

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }

    @Override
    public void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {

        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);

        String email = user.getEmailAddress();
        
        if(email == null) {
            throw new ServletException("Please enter a value for required input: "+Supportticket_.emailAddress.getName());
        }
        
        Map<String, String> parameters = this.getParameters(request);

        String subject = parameters.get(Supportticket_.subject.getName());
        String message = parameters.get(Supportticket_.message.getName());
Log.getInstance().log(Level.FINE, "Subject: {0}, Text: {1}", this.getClass(), subject, message);        
        
        if(message == null) {
            throw new ServletException("Please enter a value for required input: "+Supportticket_.message.getName());
        }
        
        HashMap complaint = new HashMap(parameters.size()+2, 1.0f);
        complaint.putAll(parameters);
        complaint.put(Supportticket_.emailAddress.getName(), email);
        complaint.put(Supportticket_.datecreated.getName(), new Date());
        
        JpaContext cf = WebApp.getInstance().getJpaContext();
        
        EntityController<Supportticket, Integer> ec = cf.getEntityController(Supportticket.class, Integer.class);
        
        ec.insert(complaint);
        
        this.notifyAdmin(subject, message);
    }
    
    private void notifyAdmin(String title, final String text) {
        final StringBuilder emailTitle = new StringBuilder(WebApp.getInstance().getName());
        emailTitle.append(" USER COMPLAINT ");
        if(title != null) {
            emailTitle.append('-').append(' ');
            emailTitle.append(title);
        }
        try{
            
            final String recipient = WebApp.getInstance().getConfig().getString(AppProperties.address_default_complaints);
            HtmlEmail htmlEmail = new DefaultHtmlEmail();
            htmlEmail.addTo(recipient);
            htmlEmail.setSubject(emailTitle.toString());
            htmlEmail.setHtmlMsg(text);

            htmlEmail.send();
            
        }catch(EmailException e) {
            Log.getInstance().log(Level.WARNING, 
                    "Error notifying admin via email of user complaint", this.getClass(), e);
        }
    }
}
