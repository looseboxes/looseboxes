package com.looseboxes.web.mail;

import com.bc.security.Encryption;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


/**
 * @(#)ActivationSettings.java   25-May-2015 10:00:27
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
public class ActivationSettings {

    public Map getEncryptedParameters(Siteuser siteuser) 
            throws GeneralSecurityException {
        
        Encryption sy = WebApp.getInstance().getEncryption();
        Map<String, String> output = new HashMap<>(2, 1.0f);
        Integer userid = siteuser.getSiteuserid();
        String email = siteuser.getEmailAddress();
        if(userid == null || email == null) {
            throw new NullPointerException();
        }
        output.put(Siteuser_.siteuserid.getName(), sy.encrypt(userid.toString().toCharArray()));
        output.put(Siteuser_.emailAddress.getName(), sy.encrypt(email.toCharArray()));
        return output;
    }

    public Map<String, String> getDecryptedParameters(HttpServletRequest request) 
            throws GeneralSecurityException, ServletException {
        
        Map<String, String> params = ServletUtil.getParameterMap(request);
        
        Encryption sy = WebApp.getInstance().getEncryption();
        
        String enc_userid = params.get(Siteuser_.siteuserid.getName());
        if(enc_userid == null) {
            throw new ServletException("Missing activation details(s)");
        }        
        String userid = new String(sy.decrypt(enc_userid));
        try{
            Integer.parseInt(userid);
        }catch(NumberFormatException e){
            throw new ServletException("Invalid activation details(s)", e);
        }
        
        String enc_email = params.get(Siteuser_.emailAddress.getName());
        if(enc_email == null) {
            throw new ServletException("Missing activation details(s)");
        }
        String email = new String(sy.decrypt(enc_email));
        
        HashMap<String, String> output = new HashMap<>(2, 1.0f);
        output.put(Siteuser_.siteuserid.getName(), userid);
        output.put(Siteuser_.emailAddress.getName(), email);
        
        return output;
    }
}
