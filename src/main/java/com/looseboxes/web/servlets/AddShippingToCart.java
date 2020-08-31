package com.looseboxes.web.servlets;

import com.bc.web.core.form.Form;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Address;
import com.looseboxes.pu.entities.Address_;
import com.looseboxes.pu.entities.Shippingdetails;
import com.looseboxes.pu.entities.Shippingdetails_;
import com.looseboxes.web.WebPages;
import java.io.Serializable;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @(#)AddShippingToCart.java   15-Dec-2014 23:37:52
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * Handles request to: insert shipping details for a shopping cart.
 * Note that this class does not follow the database process stages of:
 * <p>
 * <tt>input - validation - confirmation - update</tt>
 * </p>
 * Also, after execution the user is forwarded to the shopping cart.
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="AddShippingToCart", urlPatterns={"/addshipping"})
public class AddShippingToCart extends AddCartReferenceDetails<Shippingdetails, Address> implements Serializable {

    @Override
    public boolean isOneToOneRelationship() {
        // Only one shipping details expected per cart/orderid
        // multiples will be deleted
        return true;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.CART_ADDSHIPPING;
    }
    
    @Override
    public void validate(HttpServletRequest request, Form form, boolean recurse) throws ServletException {
        
        this.checkNull(form, Address_.streetAddress.getName());
        
        super.validate(request, form, recurse); 
    }

    @Override
    public void format(HttpServletRequest request) {
        
        super.format(request);
        
        Map params = this.getFormInputs();
        
        params.put(Shippingdetails_.shippingstatusid.getName(), References.shippingstatus.Pending);
    }    
    
    @Override
    public String getFormId() {
        return "InsertShippingdetails";
    }
    
    @Override
    public Class<Shippingdetails> getReferencingEntityClass() {
        return Shippingdetails.class;
    }
    
    @Override
    public Class<Address> getEntityClass() {
        return Address.class;
    }

    @Override
    public String getSuccessMessage() {
        return "DELIVERY ADDRESS UPDATED";
    }
}
