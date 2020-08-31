package com.looseboxes.web;

import com.looseboxes.web.mail.Emailer;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;

/**
 * @(#)ServletMailer.java   31-Jul-2013 04:50:22
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
public class ServletMailer extends Emailer {

    private final Level level;
    private final HttpServletRequest request;

    public ServletMailer () { 
        this(null, null);
    }
    
    public ServletMailer (HttpServletRequest request, Level level) { 
        this.request = request;
        this.level = level;
    }

    @Override
    public StringBuilder format(StringBuilder email) {
        
        if(request == null) return email;

        if(level != null) {
            email.append("<br/>");
            email.append(ServletUtil.getDetails(request, "<br/>", level));
        }

        email.append("<br/>");
        email.append(ServletUtil.getDetails(((HttpServletRequest)request).getSession(), "<br/>", level));
        
        return email;
    }

    public void sendError(final Exception t, final Object msg) {
    
        super.sendError(t, msg, level);
    }
}
