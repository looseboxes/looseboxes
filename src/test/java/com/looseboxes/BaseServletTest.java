package com.looseboxes;

import com.looseboxes.web.servlets.BaseServlet;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;

/**
 * @(#)ServletTest.java   13-May-2015 19:37:00
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
public class BaseServletTest extends AbstractTest {

    private BaseServlet baseServlet;
    
    private boolean forwardResponse;
    
    public BaseServletTest() { }
    
    @Test
    public void testServlet() throws ServletException, IOException {
        
        Class servletClass = baseServlet.getClass();
        
        Map parameters = this.getParameters();
        
        HttpServletRequest request = this.getRequest(servletClass, parameters);
        
        final String actionName = this.getActionName(servletClass, parameters);
        
        baseServlet.processRequest(request, null, forwardResponse);
            
log(this.getClass(), "{0}. {1} COMPLETED", "#testServlet", actionName);        
    }

    public boolean isForwardResponse() {
        return forwardResponse;
    }

    public void setForwardResponse(boolean forwardResponse) {
        this.forwardResponse = forwardResponse;
    }
    
    public BaseServlet getBaseServlet() {
        return baseServlet;
    }

    public void setBaseServlet(BaseServlet baseServlet) {
        this.baseServlet = baseServlet;
    }
}
