package com.looseboxes.web.mail;

import com.bc.util.Log;
import com.bc.html.HtmlGen;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.servlets.Activation;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import org.apache.commons.mail.EmailException;


/**
 * @(#)ActivationMail.java   25-May-2015 08:40:08
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
public class ActivationMail extends DefaultHtmlEmail {

    public ActivationMail(Siteuser user) 
            throws ServletException, GeneralSecurityException, MalformedURLException, EmailException { 
        
        String appName = WebApp.getInstance().getName();
        
        StringBuilder builder = new StringBuilder();
        builder.append("Activate your ").append(appName);
        builder.append(" subscription");
        
        this.setSubject(builder.toString());
        
        builder.setLength(0);
        builder.append("You subscribed to ").append(appName);
        builder.append(". Click ");
        
        Map params = new ActivationSettings().getEncryptedParameters(user);
        String url = ServletUtil.getURL(Activation.class, params);
Log.getInstance().log(Level.FINE, "Activation link: {0}", this.getClass(), url);

        HtmlGen.AHREF(url, "here", builder); 
        
        builder.append(" to activate your subscription.");
        
        this.setHtmlMsg(builder.toString());
    }
}
