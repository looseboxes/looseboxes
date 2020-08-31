package com.looseboxes.web.servlets;

import com.bc.util.Log;
import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Shippingdetails;
import com.looseboxes.pu.entities.Shippingdetails_;
import com.looseboxes.pu.entities.Shippingstatus;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bc.jpa.context.JpaContext;

/**
 * @(#)PostOrderHandler.java   25-Jun-2013 21:07:03
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
/**
 * Handles requests on a user order
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="PostOrderHandler", urlPatterns={"/poh"})
public class PostOrderHandler extends BaseServlet {
    
    public static enum ActionType {
        
        viewOrder(1, "View Order Details"),
        makePayment(2, "Make payment"),
        confirmReceipt(3, "Confirm Receipt"),
        submitComplain(4, "Submit Complain"),
        viewShipping(5, "View Shipping Details"),
        addShipping(6, "Add Shipping Details");
        
        private final int key;
        private final String label;
        private ActionType(int key, String label) {
            this.key = key;
            this.label = label;
        }
        public boolean isBuyerAction() {
            return key == 1 || key == 2 || key == 3;
        }
        public boolean isSellerAction() {
            return key == 2 || key == 3 || key == 4;
        }
        public int getKey() {
            return key;
        }
        public String getName() {
            return this.name();
        }
        public String getLabel() {
            return label;
        }
        @Override
        public String toString() {
            return this.label;
        }
    }

    private ActionType actionType;
    
    private Integer productorderId;
    
    private String forwardPage;
            
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return forwardPage;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {
        
        String sval = (String)ServletUtil.find("axt", request);
        
        if(sval == null) {
            throw new NullPointerException();
        }
        
        this.actionType = getActionType(Integer.parseInt(sval));
        
        this.productorderId = this.getOrderId(request);
        
Log.getInstance().log(Level.FINE, "Action type: {0}, order ID: {1}", 
        this.getClass(), actionType, productorderId);

        switch(actionType) {
            case viewOrder:
                request.setAttribute(Productorder_.productorderid.getName(), productorderId);
                forwardPage = "/cart/viewOrder.jsp";
                break;
            case makePayment:
                forwardPage = "/cart/index.jsp";
                break;
            case confirmReceipt:
                this.confirmReceipt();
                break;
            case submitComplain:
                // Send the user to the view order details page. Ask them 
                // to click the particular productId they want to complain
                // against. 
                forwardPage = "/legal/submitcomplain.jsp";
                break;
            case viewShipping:
                boolean success = this.viewShipping(request);
                if(success) {
                    forwardPage = "/cart/shippingdetails.jsp";
                }else{
                    forwardPage = "/cart/myOrders.jsp";
                }
                break;
            case addShipping:
                StringBuilder link = new StringBuilder();
                Map params = Collections.singletonMap(Productorder_.productorderid.getName(), productorderId);
                ServletUtil.appendContextQuery(InsertShipping.class, params, link, true);
                forwardPage = link.toString();
                break;
            default:
                throw new IllegalArgumentException("Unexpected parameter value for axt: "+actionType);
        }
    }
    
    private void confirmReceipt() {
        
        JpaContext cf = WebApp.getInstance().getJpaContext();
        
        EnumReferences refs = cf.getEnumReferences();
        
        Shippingstatus status = (Shippingstatus)refs.getEntity(References.shippingstatus.FullyReceived);
        
        EntityController<Shippingdetails, Integer> ec = 
                cf.getEntityController(Shippingdetails.class, Integer.class);
        
        int updateCount = ec.update(
                Shippingdetails_.productorderid.getName(), this.productorderId, 
                Shippingdetails_.shippingstatusid.getName(), status);
        
        if(updateCount == -1) {
            this.addMessage(MessageType.warningMessage, "Failed to update shipping status to: "+References.shippingstatus.FullyReceived);
        }else{
            this.addMessage(MessageType.warningMessage, "Successfully updated shipping status to: "+References.shippingstatus.FullyReceived);
        }
    }

    private boolean viewShipping(HttpServletRequest request) {
        
        JpaContext cf = WebApp.getInstance().getJpaContext();
        
        EntityController<Productorder, Integer> oec = cf.getEntityController(Productorder.class, Integer.class);
        
        Productorder order = oec.selectById(this.productorderId);
        
        if(order == null) {
            this.addMessage(MessageType.warningMessage, "Could not find the selected order");
            return false;
        }

        EntityController<Shippingdetails, Integer> sec = cf.getEntityController(Shippingdetails.class, Integer.class);
        
        Shippingdetails shippingdetails = sec.selectFirst(Productorder_.productorderid.getName(), this.productorderId);
        
        if(shippingdetails != null) {
            
//@related ShippingRecord attribute
//
            request.setAttribute("ShippingRecord", shippingdetails);
        }

//<%-- //@related shippingdetails.jsp orderId request attribute --%>        
        request.setAttribute("orderId", this.productorderId);
        
        return true;
    }
    
    /**
     * @return An Integer value for the orderId parameter in the input request
     */
    private Integer getOrderId(HttpServletRequest request) {
        
        Integer output;
        
        Object oval = ServletUtil.find(Productorder_.productorderid.getName(), request);
        
        if(oval == null) {
            throw new NullPointerException();
        }
        
        try{
            output = (Integer)oval;
        }catch(ClassCastException e) {
            output = Integer.valueOf(oval.toString());
        }
        
        return output;
    }

    public ActionType getActionType(int key) {
        for(ActionType at:getActionTypes()) {
            if(at.key == key) return at;
        }
        throw new IllegalArgumentException("Unexpected PostOrderHandler.ActionType.key: "+key);
    }
    
    public ActionType [] getActionTypes() {
        return ActionType.values();
    }
}
