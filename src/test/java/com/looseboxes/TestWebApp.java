package com.looseboxes;

import com.bc.webapptest.HttpSessionImpl;
import com.bc.webapptest.TestServletContext;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.listeners.ContextListener;
import com.looseboxes.web.listeners.SessionListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSessionEvent;


/**
 * @(#)TestWebApp.java   13-Jun-2015 09:17:44
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
public class TestWebApp {

    private static ContextListener contextListener;
    
    private static ServletContext servletContext;
    
    private static SessionListener sessionListener;
    
    private static HttpSessionImpl session;
    
    private static boolean initialized;

    public static void init() throws Exception {
        
System.out.println(TestWebApp.class.getName()+"#init");    

        servletContext = new TestServletContext(
                "http://localhost:8080",
                System.getProperty("user.home")+"/Documents/NetBeansProjects/looseboxes/web",
                "");
        
        contextListener = new ContextListener();
        
        contextListener.contextInitialized(new ServletContextEvent(servletContext));

        sessionListener = new SessionListener();

        session = new HttpSessionImpl(servletContext);

        sessionListener.sessionCreated(new HttpSessionEvent(session));

//        DatabaseSetup dbsetup = new DatabaseSetup();
//        if(!dbsetup.isTestDataInDatabase()) {
//            dbsetup.insertTestData();
//        }
        
        initialized = true;
    }
    
    public static void destroy() throws Exception {
System.out.println(TestWebApp.class.getName()+"#destroy");    
        try{
            if(sessionListener != null && session != null) {
                sessionListener.sessionDestroyed(new HttpSessionEvent(session));
            }
        }finally{
            if(contextListener != null && servletContext != null){
                contextListener.contextDestroyed(new ServletContextEvent(servletContext));
            }
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
    
    public static boolean isUserLoggedIn() {
        return getUser().isLoggedIn();
    }
    
    public static UserBean getUser() {
        return (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);
    }
    
    public static ContextListener getContextListener() {
        return contextListener;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static SessionListener getSessionListener() {
        return sessionListener;
    }

    public static HttpSessionImpl getSession() {
        return session;
    }
}
