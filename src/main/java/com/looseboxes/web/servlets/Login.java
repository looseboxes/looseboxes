package com.looseboxes.web.servlets;

/**
 * @(#)Login.java   23-Apr-2015 20:14:50
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.validators.ValidationException;
import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.LbAuthSvcSession;
import com.looseboxes.web.LbAuthSvcSession.RemoteResponse;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.looseboxes.web.components.forms.SimpleFormImpl;
import com.looseboxes.web.form.FormValidator;
import java.util.logging.Logger;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Login", urlPatterns={"/login"})
public class Login extends BaseServlet {

    private transient static final Logger LOG = Logger.getLogger(Login.class.getName());

    public static final List<String> loggedInUsers = new ArrayList<>();
    
    private boolean ignoreAuthentication;
    
    private Form<Siteuser> mForm;

    public <E> Form getForm(
            HttpServletRequest request, Class<E> entityClass, String formId, Form.ActionType actionType) 
            throws ServletException {
        
        com.bc.web.core.form.Form<E> form = 
                (com.bc.web.core.form.Form<E>)com.bc.web.core.util.ServletUtil.find(formId, request);
        
        if(form == null) {
            
            form = new SimpleFormImpl(actionType, formId, entityClass); 
                
            // This must be set once, immediately after creation
            form.setRequest(request);
        }
        
        return form;
    }
    
    @Override
    public void destroy(HttpServletRequest request) throws ServletException {
        try{
            super.destroy(request); 
        }finally{
            mForm = null;
        }
    }
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.LOGIN;
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);

        LOG.fine(() -> "Already loggedin: "+user.isLoggedIn()+". User: "+user);
            
        if(user.isLoggedIn()) {

            this.handleUserAlreadyLoggedIn(request, user);
            
        }else{
        
            Map<String, String> params = this.getParameters(request);

            this.mForm = this.getForm(request, Siteuser.class, "Login", Form.ActionType.INSERT);
            
            this.mForm.addSelectedDetails(params);

            this.validate(request, this.mForm, params);
            
            if(!this.isIgnoreAuthentication()) {

                RemoteResponse authResponse = this.remoteLogin(request, params);

                this.handleRemoteLoginResponse(authResponse, request, params);
                
            }else{
                
                login(request, params);
            }
        }
    } 
    
    public void handleUserAlreadyLoggedIn(HttpServletRequest request, UserBean user) throws ServletException {
        StringBuilder msg = new StringBuilder("You are already logged in as <b>");
        msg.append(user.getDetails().getEmailAddress());
        msg.append("</b><br/>To log in as another user, log out first.");
        throw new ServletException(msg.toString());
    }
    
    public void validate(HttpServletRequest request, Form<Siteuser> form, Map<String, String> params) throws ServletException {
        try{
            this.getValidator(request, form, UserType.existingUser).validate(form);
        }catch(ValidationException e) {
            String msg = e.getLocalizedMessage() == null ? "Invalid login details" : e.getLocalizedMessage();
            throw new ServletException(msg, e);
        }
    }
    
    public FormValidator getValidator(HttpServletRequest request, Form<Siteuser> form, UserType userType) {
        return new FormValidator(userType);
    }

    public RemoteResponse remoteLogin(HttpServletRequest request, Map<String, String> params) throws ServletException {
        
        LbAuthSvcSession authSvcSess = WebApp.getInstance().getAuthSvcSession();

        HashMap remoteAuthParams = new HashMap(params);

        LOG.log(Level.FINE, "Remote login parameters: {0}", remoteAuthParams);
        
// Use method remoteXXX e.g remoteLogin not loginUser
//        
        RemoteResponse authResponse = authSvcSess.remoteLogin(remoteAuthParams);
        
        return authResponse;
    }
    
    public void handleRemoteLoginResponse(RemoteResponse authResponse, 
            HttpServletRequest request, Map<String, String> params) 
            throws ServletException {
        
        final Map auth_user = authResponse.getMessage();

        LOG.log(Level.FINER, "Remote login response: {0}", auth_user);

        if(authResponse.isSuccess()) {
        
            login(request, params);
            
        }else{
            
            throw new ServletException(authResponse.getMessageText());
        }
    }
    
    public void login(HttpServletRequest request, Map<String, String> params) throws ServletException {
        
        if(params == null || params.isEmpty()) {
            throw new NullPointerException();
        }
        
        HttpSession session = request.getSession();
        
        UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);
        
        user.login(params);

        String name = user.getName();
        
        if(name == null) {
            throw new NullPointerException();
        }

        loggedInUsers.add(name);

        rememberUser(session, params);
    }
    
    private boolean rememberUser(HttpSession session, Map<String, String> params) {
        try{
            
            String sval = params.get("rememberme");
            
            if(sval != null && Boolean.parseBoolean(sval)) {
            
                UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);

                if(!user.isLoggedIn()) {
                    throw new UnsupportedOperationException();
                }

                int timeoutDays = WebApp.getInstance().getConfig().getInt(AppProperties.REMEMBERME_TIMEOUT_DAYS, 30);
                
                int timeoutSeconds = (int)TimeUnit.DAYS.toSeconds(timeoutDays);
                
                LOG.fine(() -> "Setting session timeout. seconds: "+timeoutSeconds+
                        ", in days: "+TimeUnit.SECONDS.toDays(timeoutSeconds));
        
                session.setMaxInactiveInterval(timeoutSeconds);

                return true;
                
            }else{
                
//<!-- @related default_session_timeout 120 minutes changes must be reflected in all related -->        
                final int timeoutSeconds = (int)TimeUnit.MINUTES.toSeconds(120); 

                LOG.fine(() -> "Setting session timeout. seconds: "+timeoutSeconds+
                        ", in minutes: "+TimeUnit.SECONDS.toMinutes(timeoutSeconds));

                session.setMaxInactiveInterval(timeoutSeconds);
                
                return false;
            }
            
        }catch(RuntimeException e) {
            LOG.log(Level.WARNING, 
                    "An unexpected error occured while trying to check if a user is authorized and then login the user if authorized", e);
            return false;
        }
    }

    public boolean isIgnoreAuthentication() {
        return ignoreAuthentication;
    }

    public void setIgnoreAuthentication(boolean ignoreAuthentication) {
        this.ignoreAuthentication = ignoreAuthentication;
    }
}
