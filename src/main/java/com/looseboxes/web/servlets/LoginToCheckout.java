package com.looseboxes.web.servlets;

/**
 * @(#)LoginToCheckout.java   18-Jul-2015 08:12:17
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.looseboxes.web.WebPages;
import com.looseboxes.web.exceptions.LoginException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="LoginToCheckout", urlPatterns={"/loginToCheckout"})
public class LoginToCheckout extends Loginx {
    
    @Override
    public void processRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        boolean forwardResponse = false;
        
        this.processRequest(request, response, forwardResponse);
        
        Checkout checkout = new Checkout();
        
        checkout.processRequest(request, response);
    }
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.CART_LOGINTOCHECKOUT;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        if(message instanceof LoginException) {
            return WebPages.CART_LOGINTOCHECKOUT;
        }else{
            return null;
        }
    }
}
