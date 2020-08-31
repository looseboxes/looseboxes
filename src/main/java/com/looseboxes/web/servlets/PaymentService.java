package com.looseboxes.web.servlets;

/**
 * @(#)PaymentService.java   27-Apr-2015 10:14:31
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.util.Log;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.payment.PaymentNotification;
import com.looseboxes.web.payment.PaymentRequest;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="PaymentService", urlPatterns={"/paymentservice", "/paymentsvc"})
public class PaymentService extends BaseServlet {
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.NOTICES_EPAYMENT;
    }

    @Override
    protected void handleRequest(
            HttpServletRequest request, 
            HttpServletResponse response) throws ServletException {

        Map<String, String> params = ServletUtil.getParameterMap(request);

Log.getInstance().log(Level.FINE, "Params: {0}", this.getClass(), params);

        final String event = params.get("event");
        
        switch(event) {
            case "success":
                this.processSuccess(request, response); 
                break;
            case "failure":    
                this.processFailure(request, response);
                break;
            case "notification":
            default:  
                throw new UnsupportedOperationException("Unexpected event: "+event+", expected any of [success, failure, notification]");
        }
    }
    
    public void processSuccess(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException {
        
        this.addMessage(MessageType.informationMessage, "Payment Successfull!");
        
        this.processGatewayReply(request, response, true);
    }

    public void processFailure(HttpServletRequest request, 
            HttpServletResponse response) throws ServletException {
        
        this.addMessage(MessageType.warningMessage, "Payment Failed!");
        
        processGatewayReply(request, response, false);
    }

    public void processGatewayReply(HttpServletRequest request, 
            HttpServletResponse response, boolean success) throws ServletException {

        // SessionUser may differ from PaymentUser. For example if the user
        // logs out and logs in before the reponse from the PaymentRequest
        // So we use paymentUser
//        User sessionUser = SessionObjects.getInstance().getUser(request.getSession());
//XLogger.getInstance().log(Level.FINE, "Session User: {0}", this.getClass(), sessionUser);                

        Map<String, String> params = ServletUtil.getParameterMap(request);
        
        String sval = params.get(Productorder_.productorderid.getName());
        
        Integer orderId = null;
        if(sval != null) {
            try{
                orderId = Integer.valueOf(sval);
            }catch(NumberFormatException e) {
Log.getInstance().log(Level.WARNING, "Response from payment gateway contains invalid value for required parameter: {0}={1}", this.getClass(), Productorder_.productorderid.getName(), sval);
            }
        }else{
Log.getInstance().log(Level.WARNING, "Response from payment gateway does not contain required parameter: {0}", this.getClass(), Productorder_.productorderid.getName());
            throw new ServletException("The response from the payment gateway is invalid");
        }

        // We remove this first, even before vaidation
        //
        PaymentRequest pmtRequest = PaymentRequest.remove(orderId); 
        
        pmtRequest.validate(request);
        
        UserBean paymentUser = pmtRequest.getUser();
Log.getInstance().log(Level.FINE, "Payment User: {0}", this.getClass(), paymentUser);                

        com.looseboxes.web.components.ShoppingCart cart = paymentUser.getShoppingCart();
        
        if(cart != null && success) {

            cart.concludePayment(References.paymentstatus.PaymentReceived); 

            cart.concludeShopping(References.orderstatus.Ordered);
        }
        
        if(cart != null && success) {

            boolean prev = cart.isSyncWithDatabase();
            try{
                // false implies clear items in local list, but don't update database
                cart.setSyncWithDatabase(false);
                cart.clear();
            }finally{
                cart.setSyncWithDatabase(prev);
            }
        }
        
        if(success) {
            
            PaymentNotification notice = new PaymentNotification(pmtRequest);

            notice.notifyBuyer(request);

            if(cart != null) {
                notice.notifySeller(cart); notice.notifySeller(cart);
            }
        }
    }
}
