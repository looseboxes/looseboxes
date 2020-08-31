package com.looseboxes.web.servlets;

import com.looseboxes.web.ChatService;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @(#)Chat.java   24-Apr-2015 22:46:42
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
@WebServlet(name="Chat", urlPatterns={"/chat"})
public class Chat extends BaseServlet {

    private transient static final Logger LOG = Logger.getLogger(Chat.class.getName());
    
    private String forwardPath;
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return forwardPath;
    }

    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {

        Map<String, String> parameters = this.getParameters(request);
        LOG.log(Level.FINE, "Parameters: {0}", parameters);        
        
        String subType = parameters.get("styp");        
        
        boolean customerserviceChat = "cs".equals(subType);
        
        forwardPath = customerserviceChat ? "/messaging/customerservicechat.jsp" : "/messaging/chat.jsp";
        
        ChatService.forRequest(request, response);
    }
}
