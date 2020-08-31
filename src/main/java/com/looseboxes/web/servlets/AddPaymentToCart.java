package com.looseboxes.web.servlets;

/**
 * @(#)AddPaymentToCart.java   12-May-2015 16:08:36
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Payment;
import com.looseboxes.pu.entities.Payment_;
import com.looseboxes.pu.entities.Userpaymentmethod;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="AddPaymentToCart", urlPatterns={"/addpayment"})
public class AddPaymentToCart extends AddCartReferenceDetails<Payment, Userpaymentmethod> {

    @Override
    public boolean isOneToOneRelationship() {
        // Only one payment record expected per cart/orderid
        // multiples will be deleted
        return true;
    }
    
    @Override
    public void format(HttpServletRequest request) {
        
        super.format(request);
        
        Map params = this.getFormInputs();
        
        params.put(Payment_.paymentstatusid.getName(), References.paymentstatus.Pending);
    }    
    
    @Override
    public String getFormId() {
        return "InsertUserpaymentmethod";
    }

    @Override
    public Class<Payment> getReferencingEntityClass() {
        return Payment.class;
    }
    
    @Override
    public Class<Userpaymentmethod> getEntityClass() {
        return Userpaymentmethod.class;
    }

    @Override
    public String getSuccessMessage() {
        return "PAYMENT DETAILS UPDATED";
    }
}
