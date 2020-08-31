package com.looseboxes.web.servlets;

/**
 * @(#)Quickchat.java   15-Aug-2015 12:47:07
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.looseboxes.web.ChatService;
import com.looseboxes.web.InitParameters;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Quickchat", urlPatterns={"/quickchat"})
public class Quickchat extends BaseServlet {

    private transient static final Logger LOG = Logger.getLogger(Quickchat.class.getName());

    private boolean logout;
    
    private String username;
   
    private ServletContext chatContext;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); 
        final ServletContext context = config.getServletContext();
        this.chatContext = context.getContext(context.getInitParameter(InitParameters.QUICK_CHAT_CONTEXT));
    }
    
    @Override
    public void destroy(HttpServletRequest request) throws ServletException {
        try{
            super.destroy(request); 
        }finally{
            this.logout = false;
            this.username = null;
            this.chatContext = null;
        }
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher(this.getForwardPage(request)).forward(request, response);
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        String forwardPage;
        if(logout) {
            forwardPage = WebPages.INDEX;
        }else{
            Objects.requireNonNull(username);
            forwardPage = chatContext.getContextPath() + "/login.jsp?username="+username;
        }
        LOG.log(Level.FINER, "Forward page: {0}", forwardPage);
        return forwardPage;
    }

    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        if(chatContext == null) {
            throw new ServletException("Chat is not avialable");
        }

        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        username = user.getName();
        
        String action = request.getParameter("action");

        logout = "logout".equals(action);
        
        if(logout) {
            if(username != null && ChatService.isUserLoggedIn(username)) {
                ChatService.logoutUser(username);
            }
        }else{
            
//        if user is not logged in username will be null
            
            if(username == null) {
                username = "guest" + UserBean.getGuestCount(true);
            }

            if(!ChatService.isUserLoggedIn(username)) {
                ChatService.loginUser(username);
            }
        }
    }
}
