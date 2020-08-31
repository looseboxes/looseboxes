package com.looseboxes.web.servlets;

/**
 * @(#)Logout.java   23-Apr-2015 19:33:16
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.web.LbAuthSvcSession;
import com.looseboxes.web.LbAuthSvcSession.RemoteResponse;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Logout", urlPatterns={"/logout"})
public class Logout extends BaseServlet {
   
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }
   
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        if(!user.isLoggedIn()) {
            throw new ServletException("You are not logged in. No need to log out");
        }

        String username = user.getName();

        // We need this here because after logout it won't be available
        //
        Siteuser siteuser = user.getDetails();
        
        user.logout();
        
        if(username != null) {
            Login.loggedInUsers.remove(username);
        }

        // Disable rememberme
        try{

//<!-- @related default_session_timeout 120 minutes changes must be reflected in all related -->        

            int timeoutSeconds = (int)TimeUnit.MINUTES.toSeconds(120); 

Log.getInstance().log(Level.FINE, "Setting session timeout. seconds: {0}, in minutes: {1}", this.getClass(), timeoutSeconds, TimeUnit.SECONDS.toMinutes(timeoutSeconds));
        
            request.getSession().setMaxInactiveInterval(timeoutSeconds);
            
            EntityController<Siteuser, ?> ec = WebApp.getInstance().getJpaContext().getEntityController(Siteuser.class);
            Map params = ec.toMap(siteuser, false);
            // This disables 'remember me'
            //
            LbAuthSvcSession authSvcSess = WebApp.getInstance().getAuthSvcSession();
// Use method remoteXXX e.g remoteDeauthorize not deauthorizeUser
            RemoteResponse authResponse = authSvcSess.remoteDeauthorize(params);
            
            if(!authResponse.isSuccess()) {
                Log.getInstance().log(Level.WARNING, "{0}", this.getClass(), authResponse.getMessageText());
//                throw new ServletException(authResponse.getMessageText());
            }
            
        }catch(RuntimeException e) {
            Log.getInstance().log(Level.WARNING, "An unexpected error occured", this.getClass(), e);
        }
    } 
}
