package com.looseboxes.web.servlets;

/**
 * @(#)InsertPaymentAndShippingForCart.java   12-May-2015 15:26:03
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.looseboxes.web.WebPages;
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
@WebServlet(name="AddPaymentAndShippingForCart", urlPatterns={"/apasfc"})
public class AddPaymentAndShippingForCart extends BaseServlet {

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.CART_INDEX;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.CART_INDEX;
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        AddPaymentToCart addPayment = new AddPaymentToCart();
        try{
            addPayment.processRequest(request, response, false);
        }finally{
            this.addMessages(addPayment.getMessages());
        }
        
        AddShippingToCart addShipping = new AddShippingToCart();
        try{
            addShipping.processRequest(request, response, false);
        }finally{
            this.addMessages(addShipping.getMessages());
        }
    }
}
