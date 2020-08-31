package com.looseboxes.web.servlets;

/**
 * @(#)ToggleExpressDelivery.java   11-May-2015 15:52:17
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="ToggleExpressDelivery", urlPatterns={"/ted"})
public class ToggleExpressDelivery extends BaseServlet {
   
    public ToggleExpressDelivery() { }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.CART_INDEX;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.CART_INDEX;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) {

        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        com.looseboxes.web.components.ShoppingCart cart = user.getShoppingCart();
        
        if(cart != null) {
            cart.setExpressDelivery(!cart.isExpressDelivery());
        }
    }
}
