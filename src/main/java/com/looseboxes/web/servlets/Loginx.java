package com.looseboxes.web.servlets;

/**
 * @(#)Loginx.java   19-Aug-2015 17:42:07
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.bc.validators.AbstractDatabaseInputValidator;
import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.validators.ValidationException;
import com.bc.web.core.form.Form;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.LbAuthSvcSession;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.UserBean;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import com.bc.jpa.context.JpaContext;

/**
 * Provides logic for an existing user to login to the website. However, in this case the 
 * various stages i.e <tt>validation, image upload, confirmation and update etc</tt> 
 * are all coalesced into one single stage. It is thus faster.
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Loginx", urlPatterns={"/loginx"})
public class Loginx extends Login {
    
    private AbstractDatabaseInputValidator.UserType userType;

    @Override
    public void init(HttpServletRequest request) throws ServletException {
        userType = this.getUserType(request);
        super.init(request); 
    }
    
    public AbstractDatabaseInputValidator.UserType getUserType(HttpServletRequest request) {
        return AbstractDatabaseInputValidator.UserType.existingUser;
    }

    @Override
    public void destroy(HttpServletRequest request) throws ServletException {
        try{
            super.destroy(request); 
        }finally{
            userType = null;
        }
    }
   
    @Override
    public void handleUserAlreadyLoggedIn(
            HttpServletRequest request, UserBean user) throws ServletException {  }
    
    @Override
    public void validate(HttpServletRequest request, Form<Siteuser> form, Map<String, String> params) throws ServletException {
        
        try{
            
            this.getValidator(request, form, UserType.existingUser).validate(form);
            
Log.getInstance().log(Level.FINER, "Existing user validation successful", this.getClass());
            
        }catch(ValidationException e_1) {
            
            try{
                
                this.getValidator(request, form, UserType.newUser).validate(form);
                
                userType = AbstractDatabaseInputValidator.UserType.newUser;
                
Log.getInstance().log(Level.FINER, "New user validation successful", this.getClass());
                
            }catch(ValidationException ignored) {
                String msg = e_1.getLocalizedMessage() == null ? "Invalid login details" : e_1.getLocalizedMessage();
                throw new ServletException(msg, e_1);
            }
        }
    }
    
    @Override
    public LbAuthSvcSession.RemoteResponse remoteLogin(HttpServletRequest request, Map<String, String> params) throws ServletException {
        
        LbAuthSvcSession authSvcSess = WebApp.getInstance().getAuthSvcSession();

        HashMap remoteAuthParams = new HashMap(params);
        
        remoteAuthParams.put(com.authsvc.client.parameters.Getuser.ParamName.create.toString(), Boolean.TRUE);

Log.getInstance().log(Level.FINE, "Remote GetUser parameters: {0}", this.getClass(), remoteAuthParams);
        
// Use method remoteXXX e.g remoteLogin not loginUser
//        
        LbAuthSvcSession.RemoteResponse authResponse = authSvcSess.remoteGetUser(remoteAuthParams);

Log.getInstance().log(Level.FINE, "Remote GetUser response: {0}", this.getClass(), authResponse);
        
        return authResponse;
    }

    @Override
    public void handleRemoteLoginResponse(LbAuthSvcSession.RemoteResponse authResponse, 
            HttpServletRequest request, Map<String, String> params) 
            throws ServletException {
        
        final Map auth_user = authResponse.getMessage();

Log.getInstance().log(Level.FINE, "Remote login response: {0}", this.getClass(), auth_user);

        if(authResponse.isSuccess()) {
            
            JpaContext cf = WebApp.getInstance().getJpaContext();
            
            EntityController<Siteuser, ?> ec = cf.getEntityController(Siteuser.class);
            
            Map dbParams = cf.getDatabaseFormat().toDatabaseFormat(Siteuser.class, params);
            
            Siteuser siteuser = ec.selectFirst(dbParams);
            
Log.getInstance().log(Level.FINE, "User exists: {0}, user: {1}", this.getClass(), siteuser != null, dbParams);
            
            if(siteuser == null) { 

                // Note: We activate this user. Priviledge only for those about to checkout/pay
                //
                dbParams.put(Siteuser_.userstatusid.getName(), References.userstatus.Activated);
                dbParams.put(Siteuser_.datecreated.getName(), new Date());
                dbParams.put(Siteuser_.currencyid.getName(), References.currency.NGN);
                
                dbParams = cf.getDatabaseFormat().toDatabaseFormat(Siteuser.class, dbParams);
                
                int updateCount = ec.insert(dbParams);

Log.getInstance().log(Level.FINER, "Insert user update count: {0}, params: {1}", 
        this.getClass(), updateCount, dbParams);
                
                if(updateCount < 1) {
                    
                    throw new ServletException("An unexpected exception occured while processing the request");
                }
            }
        
            login(request, dbParams); 
            
        }else{
            
            throw new ServletException(authResponse.getMessageText());
        }
    }

    public AbstractDatabaseInputValidator.UserType getUserType() {
        return userType;
    }

    public void setUserType(AbstractDatabaseInputValidator.UserType userType) {
        this.userType = userType;
    }
}
