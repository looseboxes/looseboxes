package com.looseboxes.web.servlets;

import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.web.Attributes;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;


/**
 * @(#)AddShoppingcartReference.java   12-May-2015 17:44:36
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @param <REFING>
 * @param <REF>
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class AddCartReferenceDetails<REFING, REF> extends AddReferenceToExisting<REFING, REF>{

    @Override
    public String getReferencingColumnName(HttpServletRequest request) {
        return Productorder_.productorderid.getName();
    }

    @Override
    public Integer getReferencingColumnValue(HttpServletRequest request) {
        
        Object oval = ServletUtil.find(Productorder_.productorderid.getName(), request);
        
        Integer output;
        if(oval == null) {
            UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
            output = user.getShoppingCart().getOrderId();
        }else{
            output = Integer.parseInt(oval.toString());
        }
        
        if(output == null) {
            throw new NullPointerException();
        }
        
        return output;
    }

    @Override
    public String getSuccessMessage() {
        return "DETAILS UPDATED";
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.CART_INDEX;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.CART_INDEX;
    }
    
    @Override
    public void format(HttpServletRequest request) {
        
        super.format(request);
        
        Map params = this.getFormInputs();
        
        // @related express shipping
        //
        Object oval = params.remove(Attributes.EXPRESS_SHIPPING);
        
        boolean express = (oval != null && oval.toString().equalsIgnoreCase("true"));

        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        com.looseboxes.web.components.ShoppingCart cart = user.getShoppingCart();
        if(cart != null) {
            cart.setExpressDelivery(express);
        }
    }
}
