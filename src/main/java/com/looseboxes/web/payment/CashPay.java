package com.looseboxes.web.payment;

import com.bc.util.Log;
import com.looseboxes.pu.References;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @(#)CashPay.java   12-Jun-2013 17:14:10
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
public class CashPay extends PaymentRequest {

    private String paymentCode;
    private References.paymentstatus paymentStatus;
    private Locale locale;
    
    public CashPay() { }
    
    @Override
    public References.paymentmethod getType() {
        return References.paymentmethod.CashonDelivery;
    }
    
    @Override
    public String getForwardPage() {
        return WebPages.NOTICES_CASHPAYMENT;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }
    
    @Override
    public String getEndpoint() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Map<String, String> getParameters() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void doProcessRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
       
        this.paymentStatus = References.paymentstatus.ProcessingPayment;
        
        this.paymentCode = this.newPaymentCode();
        
        this.locale = getUser().getLocale();

        PaymentNotification notice = new PaymentNotification(this);
        
        // SessionUser may differ from PaymentUser. For example if the user
        // logs out and logs in before the reponse from the PaymentRequest
        // So we use paymentUser
//        UserBean sessionUser = SessionObjects.getInstance().getUser(request.getSession());
        UserBean paymentUser = this.getUser();
        
Log.getInstance().log(Level.FINE, "Payment User: {0}", this.getClass(), paymentUser);                

        this.validate(request);
        
        com.looseboxes.web.components.ShoppingCart cart = paymentUser.getShoppingCart();
        
        if(cart != null) {
            
            cart.concludePayment(References.paymentstatus.Pending);

            cart.concludeShopping(References.orderstatus.Ordered);
            
            boolean updateDb = cart.isSyncWithDatabase();
            try{
                // false implies clear items in local list, but don't update database
                cart.setSyncWithDatabase(false);
                cart.clear();
            }finally{
                cart.setSyncWithDatabase(updateDb);
            }
        }
        
        notice.notifyBuyer(request); 

        if(cart != null) {
            notice.notifySeller(cart);
        }
    }
    
    @Override
    public void validate(HttpServletRequest request) { }
    
    @Override
    public References.paymentstatus getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String getPaymentCode() {
        return paymentCode;
    }
}