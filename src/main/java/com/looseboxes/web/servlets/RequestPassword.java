package com.looseboxes.web.servlets;

/**
 * @(#)RequestPassword.java   23-May-2015 10:45:56
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.pu.entities.Userstatus;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.LbAuthSvcSession;
import com.looseboxes.web.LbAuthSvcSession.RemoteResponse;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bc.jpa.context.JpaContext;
import java.util.logging.Logger;
import com.bc.config.Config;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="RequestPassword", urlPatterns={"/reqpwd", "/requestPassword", "/requestpassword"})
public class RequestPassword extends BaseServlet {

    private transient static final Logger LOG = Logger.getLogger(RequestPassword.class.getName());

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.REQUEST_PASSWORD;
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }
   
    @Override
    public void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {

        final String emailAddress = request.getParameter(Siteuser_.emailAddress.getName());

        if (emailAddress == null || emailAddress.equals("")) {
            throw new ServletException("Please enter your "+Siteuser_.emailAddress.getName());
        }    

        JpaContext cf = WebApp.getInstance().getJpaContext();
        
        EntityController<Siteuser, Integer> ec = cf.getEntityController(Siteuser.class, Integer.class);
        
        Siteuser user = ec.selectFirst(Siteuser_.emailAddress.getName(), emailAddress);
        
        LOG.fine(() -> "For email address: " + emailAddress + ", selected user: " + user);
        
        if(user == null) {
            
            LOG.fine(() -> "Source:: "+request.getHeader("referer")+
                    ", User Action:: PASSWORD_REQUEST, Error Message:: Invalid email: "+emailAddress);
                
//            StringBuilder builder = new StringBuilder("Email address: ");
//            builder.append(emailAddress).append(" is not yet taken. Click ");
//            builder.append(HtmlGen.getInstance().getAHREF(WebApp.getInstance().getServletContext()+WebPages.JOIN, "here", null));
//            builder.append(" to join ");
//            builder.append(WebApp.getInstance().getName()).append('.');
            
            throw new ServletException("Invalid email: "+emailAddress);
        }
        
        // If user is not activated throw an exception

        final Userstatus status = user.getUserstatusid();
        
        LOG.fine(() -> "User status: " + status);
        
        final EnumReferences refs = cf.getEnumReferences();
        
        final Userstatus activated = (Userstatus)refs.getEntity(References.userstatus.Activated);
        
        if(!status.equals(activated)) {
            throw new ServletException("Sorry, you don't have an active subscription");
        }
        
        final Map userDetails = ec.toMap(user, false);
        
        LOG.finer(() -> "User details: " + userDetails);
        
        Config config = WebApp.getInstance().getConfig(); 
        String senderEmail = config.getString(AppProperties.address_default_noreply);
        String senderPass = config.getString(AppProperties.address_default_noreply+".password");
        if(senderEmail != null) {
            userDetails.put(com.authsvc.client.parameters.Requestuserpassword.ParamName.sender_emailaddress.name(), senderEmail);
        }
        if(senderEmail != null && senderPass != null) {
            userDetails.put(com.authsvc.client.parameters.Requestuserpassword.ParamName.sender_password.name(), senderPass);
        }
        
        LbAuthSvcSession authSess = WebApp.getInstance().getAuthSvcSession();
        
        LOG.fine(() -> "Request password with user details: " + userDetails);

        // Always use the remoteXXX version of the method
        //
        RemoteResponse remoteResponse = authSess.remoteRequestPassword(userDetails);
        
        LOG.fine(() -> "Request password remote response: " + remoteResponse); 

        if(remoteResponse.isSuccess()) {
            
            this.addMessage(MessageType.informationMessage, "Password sent to: "+emailAddress);
            
        }else{
            
            throw new ServletException(remoteResponse.getMessageText());
        }
    }
}
