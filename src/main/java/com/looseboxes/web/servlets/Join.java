package com.looseboxes.web.servlets;

/**
 * @(#)Join.java   07-May-2015 17:31:12
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.web.core.form.Form;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.LbAuthSvcSession;
import com.looseboxes.web.LbAuthSvcSession.RemoteResponse;
import com.looseboxes.web.ServletMailer;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.mail.ActivationMail;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.EmailException;
import com.bc.jpa.context.JpaContext;
import com.bc.security.SecurityTool;
import com.bc.jpa.dao.Select;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Join", urlPatterns={"/join"})
public class Join extends UpdateServletOld<Siteuser> {
    
    @Override
    public Form.ActionType getFormActionType() {
        return Form.ActionType.INSERT;
    }
    
    @Override
    public String getFormId() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.JOIN;
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.WELCOME;
    }

    @Override
    public Class<Siteuser> getEntityClass() {
        return Siteuser.class;
    }

    @Override
    public UserType getUserType() {
        return UserType.newUser;
    }
   
    @Override
    public String getSuccessMessage() {
        return "USER REGISTRATION SUCCESSFUL!";
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Keep a reference of this, it may not be available after #handleRequest
        //
        Map params = this.getFormInputs();

        super.handleRequest(request, response);
        
        if(params == null || params.isEmpty()) {
Log.getInstance().log(Level.WARNING, "Unable to extract contacts of newly registered user. Reason: Parameters/forminputs = {0}", this.getClass(), params);
        }else{    
            String email =(String)params.get(Siteuser_.emailAddress.getName());
            String password = (String)params.get("password");
            if(email != null && password != null) {
    //@todo            
    //            new ContactsExtractor(email, password).run();
            }
        }
    }
    
    @Override
    public void format(HttpServletRequest request) {

        Map parameters = this.getFormInputs();
        
        if(parameters != null) {
            // Users are initially unactivated, till they activate via their
            // mail-box when their staus will be changed to reflect this
            parameters.put(Siteuser_.userstatusid.getName(), References.userstatus.Unactivated);
            parameters.put(Siteuser_.datecreated.getName(), new Date());
            parameters.put(Siteuser_.currencyid.getName(), References.currency.NGN);
        }

        super.format(request);

        if(parameters != null) {
//@todo        
//        output = ServletUtil.formatImages(database, tableName, output, DbActionIx.ActionType.INSERT);

            // If username is null, as may be the case
            // Generate it from supplied emailAddress
            //
            String username = (String)parameters.get(Siteuser_.username.getName());
            if(username == null) {
                parameters.put(Siteuser_.username.getName(), new SecurityTool().generateUsername("user", 2012));
            }
        }
    }
    
    @Override
    public int update(HttpServletRequest request) throws ServletException {

        int updateCount = super.update(request);
        
        if(updateCount == 1) {
            
            RemoteResponse remoteResponse;
            Exception remoteException;
            try{
                // Notice we use the original parameters
                //
                remoteResponse = this.createRemoteAuthUser(this.getFormInputs());
                remoteException = null;
            }catch(IOException | ServletException e) {
                Log.getInstance().log(Level.WARNING, "Error communicating with remote authentication server", this.getClass(), e);
                remoteResponse = null;
                remoteException = e;
            }
            
            Map databaseParams = this.getDatabaseParameters(this.getForm());
            
            if(remoteResponse != null && remoteResponse.isSuccess()) { 
                
                this.addMessage(MessageType.informationMessage, "User profile successfully created");            
                
                this.sendActivationMail(request);
                
            }else{    
                
                Map params = this.getDatabaseParameters(this.getForm());
                
                EntityController<Siteuser, Integer> ec = this.getEntityController();
                
                updateCount = ec.delete(params);
                
                if(updateCount == 0 || updateCount > 1) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("Attempted to delete One record matching: ");
                    builder.append(databaseParams).append("\nBut delete operation affected ");
                    builder.append(updateCount).append(" rows.");
                    if(updateCount == 0) {
                        builder.append("\nNow we have created a remote user without the corresponding local instance and we have to manually create a local user");
                    }
                    Log.getInstance().log(Level.WARNING, builder.toString(), this.getClass());
                }else{
                    Log.getInstance().log(Level.WARNING, "Delete locally inserted user because failed to create remote authentication service user", this.getClass());
                }
                String msg = remoteResponse == null ? 
                        "An unexpected error occured while processing the request" :
                        remoteResponse.getMessageText();
                throw new ServletException(msg);
            }
        }else{
            this.addMessage(MessageType.warningMessage, "Failed to create user profile");            
        }

        return updateCount;
    }
    
    public void sendActivationMail(HttpServletRequest request) {
        
        final Siteuser user = this.getInsertedUser();
        
        try{
            
            final ActivationMail mail = new ActivationMail(user);

            mail.addTo(user.getEmailAddress());
            
            mail.send();
            
            this.addMessage(MessageType.informationMessage, "An email has been sent to: "+user.getEmailAddress()+". Check you email <em>inbox</em> (<em>or spam folder</em>) to activate your profile");
            
        }catch(ServletException | GeneralSecurityException | MalformedURLException | EmailException e) {
        
            Log.getInstance().log(Level.WARNING, "Error sending profile activation email to: "+(user==null?null:user.getEmailAddress()), this.getClass(), e);

            this.addMessage(MessageType.informationMessage, "Error sending profile activation email. Site admin has been notified.");
            
            new ServletMailer(request, Level.FINEST).sendError(e, "Failed to send activation mail to user: "+(user==null?null:user.getSiteuserid()));
        }
    }
    
    public Siteuser getInsertedUser() {
        Map map = this.getDatabaseParameters(this.getForm());
        String email = (String)map.get(Siteuser_.emailAddress.getName());
        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        try(Select<Siteuser> qb = jpaContext.getDaoForSelect(Siteuser.class)) {
            
            return qb.where(Siteuser.class, Siteuser_.emailAddress.getName(), email)
            .createQuery().getSingleResult();
        }catch(javax.persistence.NoResultException noNeedToLog) {
            return null;
        }
    }
    
    public RemoteResponse createRemoteAuthUser(Map parameters) 
            throws IOException, ServletException {
        
        RemoteResponse remoteResponse;
        
        LbAuthSvcSession authSess = WebApp.getInstance().getAuthSvcSession();
        
        if(authSess != null) {

            // Use a copy of the original parameters
            final Map authParams = new HashMap(parameters);

Log.getInstance().log(Level.FINER, "Creating remote auth user: {0}", this.getClass(), authParams);
            
            remoteResponse = authSess.remoteCreateUser(authParams);
            
            if(remoteResponse.isSuccess()) {
                
                final Map createdUser = remoteResponse.getMessage();
                
                // We activate the user here because we need the password to do
                // this and it is contained in the response
                //
                boolean success = this.activateRemoteUser(createdUser);
                
                if(!success) {
                    
                    new ServletMailer().sendErrorMessage(
                    "You need to manually edit userstatus of remote auth user: "+
                    createdUser+" for "+WebApp.getInstance().getName());
                }
            }
        }else{
            remoteResponse = null;
        }
        
        return remoteResponse;
    }

    private boolean activateRemoteUser(Map userdetails) throws IOException, ServletException {
        
        LbAuthSvcSession authSess = WebApp.getInstance().getAuthSvcSession();
                
        RemoteResponse response = authSess.remoteEditUserStatus(userdetails);
        
        if(!response.isSuccess()) {
Log.getInstance().log(Level.WARNING, "Failed to activate remote auth for user: {0}\nServer response: {1}", this.getClass(), userdetails, response.getMessage());
        }
        
        return response.isSuccess();
    }
}
