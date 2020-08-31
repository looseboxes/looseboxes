package com.looseboxes.web;

import com.authsvc.client.AuthDetailsStore;
import com.authsvc.client.AppAuthenticationSessionImpl;
import com.authsvc.client.JsonResponseIsErrorTestImpl;
import com.authsvc.client.net.HttpClientImpl;
import com.bc.util.Log;
import com.looseboxes.pu.entities.Siteuser_;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import java.text.ParseException;
import java.util.logging.Logger;


/**
 * @(#)LbAuthSvcSession.java   24-Apr-2015 11:14:35
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
public class LbAuthSvcSession extends AppAuthenticationSessionImpl {

    private transient static final Logger LOG = Logger.getLogger(LbAuthSvcSession.class.getName());

    public LbAuthSvcSession(AuthDetailsStore store, String target) {
        super(new HttpClientImpl(), 
                target, 
                store, 
                new JsonResponseIsErrorTestImpl()
        );
    }

    public void format(Map where) {
        String emailCol = com.authsvc.client.parameters.Loginuser.ParamName.emailaddress.name();
        Object emailAddress = where.get(emailCol);
        if(emailAddress == null) {
            emailAddress = where.get(Siteuser_.emailAddress.getName());
            if(emailAddress != null) {
                where.put(emailCol, emailAddress);
            }
        }
    }

    public RemoteResponse remoteGetUser(Map params) throws ServletException {
        RemoteResponse response;
        Map responseJson;
        try{
            this.format(params); 
            responseJson = this.getUser(params);
            response = new RemoteResponseImpl(responseJson);
            LOG.log(Level.FINE, "Get user. Server response: {0}", responseJson);
        }catch(ParseException | IOException | RuntimeException e) {
            response = null;
            responseJson = null;
            String msg = this.getErrorMessage("GetUser", params, response);
            Log.getInstance().log(Level.WARNING, msg, this.getClass(), e);
            throw new ServletException("Error communicating with authentication server");
        }
        return response;
    }
    
    public RemoteResponse remoteEditUserStatus(Map userDetails) throws ServletException {
        RemoteResponse response;
        Map responseJson;
        try{
            this.format(userDetails);
            responseJson = this.editUserStatus(this.getAppDetails(), userDetails);
            response = new RemoteResponseImpl(responseJson);
            LOG.log(Level.FINE, "Edit user status. Server response: {0}", responseJson);
        }catch(ParseException | IOException | RuntimeException e) {
            response = null;
            responseJson = null;
            String msg = this.getErrorMessage("Edit user status", userDetails, response);
            Log.getInstance().log(Level.WARNING, msg, this.getClass(), e);
            throw new ServletException("Error communicating with authentication server");
        }
        return response;
    }

    public RemoteResponse remoteRequestPassword(Map userDetails) throws ServletException {
        RemoteResponse response;
        Map responseJson;
        try{
            this.format(userDetails);
            responseJson = this.requestUserPassword(this.getAppDetails(), userDetails);
            response = new RemoteResponseImpl(responseJson);
            LOG.log(Level.FINE, "Request user password. Server response: {0}", responseJson);
        }catch(ParseException | IOException | RuntimeException e) {
            response = null;
            responseJson = null;
            String msg = this.getErrorMessage("request password", userDetails, response);
            Log.getInstance().log(Level.WARNING, msg, this.getClass(), e);
            throw new ServletException("Error communicating with authentication server");
        }
        return response;
    }
    
    private void formatCreateUser(Map where) {
        // We handle the sending of account activation mail in-house
        //
        Boolean sendRegistrationMail = Boolean.FALSE;
        where.put(com.authsvc.client.parameters.Createuser.ParamName.sendregistrationmail.name(), sendRegistrationMail);
    }

    public RemoteResponse remoteCreateUser(Map params) throws ServletException {
        RemoteResponse response;
        Map responseJson;
        try{
            this.format(params);
            this.formatCreateUser(params);
            responseJson = this.createUser(params);
            response = new RemoteResponseImpl(responseJson);
            LOG.log(Level.FINE, "Create new user. Server response: {0}", responseJson);
        }catch(ParseException | IOException | RuntimeException e) {
            response = null;
            responseJson = null;
            String msg = this.getErrorMessage("Create new user", params, response);
            Log.getInstance().log(Level.WARNING, msg, this.getClass(), e);
            throw new ServletException("Error communicating with authentication server");
        }
        return response;
    }
    
    public RemoteResponse remoteLogin(Map params) throws ServletException {
        RemoteResponse response;
        Map responseJson;
        try{
            this.format(params);
            responseJson = this.loginUser(params);
            response = new RemoteResponseImpl(responseJson);
            LOG.log(Level.FINE, "Login user. Server response: {0}", responseJson);
        }catch(ParseException | IOException | RuntimeException e) {
            response = null;
            responseJson = null;
            String msg = this.getErrorMessage("login", params, response);
            Log.getInstance().log(Level.WARNING, msg, this.getClass(), e);
            throw new ServletException("Error communicating with authentication server");
        }
        return response;
    }

    public RemoteResponse remoteAuthorize(Map params) {
        RemoteResponse response;
        Map responseJson;
        try{
            this.format(params);
            responseJson = this.authorizeUser(params);
            response = new RemoteResponseImpl(responseJson);
            LOG.log(Level.FINE, "Enable 'rememberme'. Server response: {0}", responseJson);
        }catch(ParseException | IOException | RuntimeException e) {
            response = null;
            responseJson = null;
            String msg = this.getErrorMessage("enable 'rememberme'", params, response);
            Log.getInstance().log(Level.WARNING, msg+", reason: "+e, this.getClass());
            response = new LbAuthSvcSession.ErrorCommunicatingWithAuthServer();
        }
        return response;
    }

    public RemoteResponse remoteDeauthorize(Map params) {
        RemoteResponse response;
        Map responseJson;
        try{
            this.format(params);
            responseJson = this.deauthorizeUser(params);
            response = new RemoteResponseImpl(responseJson); 
            LOG.log(Level.FINE, "Disable 'rememberme'. Server response: {0}", responseJson);
        }catch(ParseException | IOException | RuntimeException e) {
            response = null;
            responseJson = null;
            String msg = this.getErrorMessage("Disable 'rememberme'", params, response);
            Log.getInstance().log(Level.WARNING, msg, this.getClass(), e);
            response = new LbAuthSvcSession.ErrorCommunicatingWithAuthServer();
        }
        return response;
    }
    
    public String getErrorMessage(String action_name, Map request_params, RemoteResponse response) {
        assert action_name != null : "Required. Method parameter: " +action_name;
        String msg = response == null ? null : response.getMessageText();
        if(msg == null) {
            StringBuilder builder = new StringBuilder();
            builder.append(action_name).append(" failed");
            if(request_params != null) {
                builder.append(", request parameters: ").append(request_params);
            }
            msg = builder.toString();
        }
        return msg;
    }
    
    public boolean isReadyForUse() {
        Map ad = this.getAppDetails();
        Map at = this.getAppToken();
        return ad != null && !ad.isEmpty() && !this.isError(ad) && 
                at != null && !at.isEmpty() && !this.isError(at);
    }
    
    public static interface RemoteResponse{
        boolean isSuccess();
        String getMessageText();
        Map getMessage();
    }

    private class ErrorCommunicatingWithAuthServer extends RemoteResponseImpl {
        private ErrorCommunicatingWithAuthServer() { 
            super(Collections.singletonMap("error", "Error communicating with authentication server"));
        }
    }
    
    private class RemoteResponseImpl implements RemoteResponse {
        private final boolean success;
        private final String message;
        private final Map res;
        private RemoteResponseImpl(Map res) {
            success = res == null ? false : !LbAuthSvcSession.this.isError(res);
            if(res == null) {
                message = "null";
            }else {
                message = res.values().stream().findFirst()
                        .orElse("Unexpected response from authentication server").toString();
            }
            this.res = res;
        }
        @Override
        public boolean isSuccess() {
            return success;
        }
        @Override
        public String getMessageText() {
            return message;
        }
        @Override
        public Map getMessage() {
            return res;
        }
        @Override
        public String toString() {
            return this.getMessageText();
        }
    }
}
