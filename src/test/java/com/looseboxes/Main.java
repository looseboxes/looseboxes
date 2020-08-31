package com.looseboxes;

import com.bc.webapptest.HttpSessionImpl;
import com.bc.webapptest.TestServletContext;
import com.looseboxes.web.listeners.ContextListener;
import com.looseboxes.web.listeners.SessionListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

/**
 * @(#)Main.java   08-May-2015 20:41:19
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
public class Main {
    
    public static void x() throws Exception {
        
        ServletContext servletContext = new TestServletContext(
                "http://localhost:8081",
                "C:/Users/Josh/Documents/NetBeansProjects/looseboxes/web",
                "/looseboxes");
        
        ContextListener cl = new ContextListener();
        
        try{
            
            cl.contextInitialized(new ServletContextEvent(servletContext));
        
            SessionListener sl = new SessionListener();
            
            HttpSession session = new HttpSessionImpl(servletContext);
            
            sl.sessionCreated(new HttpSessionEvent(session));
            
            try{
                
                
//                session = testServlet(servletContext, session, new Join());
                
//                session = testRequest(servletContext, session, new Login());

//                session = testServlet(servletContext, session, new Login());
                
//                session = testServlet(servletContext, session, new AddShippingToCart(), Productorder_.productorderid.getName(), 2);
                
//                String [] actions = {"add", "remove", "delete", "add", "add", "clear", "add", "add", "checkout"};
//                String [] actions = {"add", "add", "remove", "add", "checkout"};
//                testShoppingCart(servletContext, session, actions);

System.out.println(Main.class.getName()+"#main(String[]) = x = x = x = x = x = x = x SUCCESS x = x = x = x = x = x = x =");
//            }catch(ServletException | IOException e) {
//                e.printStackTrace();
            }finally{
                sl.sessionDestroyed(new HttpSessionEvent(session));
            }
        }catch(RuntimeException e) {    
            e.printStackTrace();
        }finally{
            cl.contextDestroyed(new ServletContextEvent(servletContext));
        }
    }
}
