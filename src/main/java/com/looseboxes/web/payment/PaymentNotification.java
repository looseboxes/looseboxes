package com.looseboxes.web.payment;


import com.bc.util.Log;
import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.web.html.ProductvariantHtmlGen;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.mail.DefaultHtmlEmail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @(#)PaymentNotification.java   10-Jun-2013 21:41:01
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
public class PaymentNotification {
    
    private final PaymentRequest paymentRequest;
    
    public PaymentNotification(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }
    
    protected PaymentRequest getPaymentRequest(HttpServletRequest request) {
        return paymentRequest;
    }
    
    public void notifyBuyer(HttpServletRequest request) throws ServletException {

        try{
            
            PaymentRequest pmtRequest = this.getPaymentRequest(request);

            UserBean user = pmtRequest.getUser();

            ProductvariantHtmlGen productParser = new ProductvariantHtmlGen();
            productParser.setExternalOutput(true);

            List<Orderproduct> orderItems = user.getShoppingCart().getItems();

            List<Productvariant> productunits = new ArrayList<>(orderItems.size());

            for(Orderproduct o:orderItems) {
                productunits.add(o.getProductvariantid());
            }

            String subject = this.getBuyerSubject(pmtRequest);
            StringBuilder message = this.getBuyerMessage(pmtRequest);
            
            HtmlEmail email = new DefaultHtmlEmail();

            email.setSubject(subject);
            
            productParser.updateEmail(productunits, message, email);

            email.addTo(user.getEmailAddress());

            email.send();
            
        }catch(EmailException e) {
            throw new ServletException("Error while trying to notify buyer by email", e);
        }
    }
    
    public void notifySeller(com.looseboxes.web.components.ShoppingCart shoppingCart) 
            throws ServletException {
        
        try{
            
            List<Orderproduct> orderItems = shoppingCart.getItems();

            Map<String, Collection<Productvariant>> allSellerProducts = new HashMap<>();

            for(Orderproduct orderItem:orderItems) {

                Productvariant productunit = orderItem.getProductvariantid();

                String email = productunit.getProductid().getSeller().getEmailAddress();

Log.getInstance().log(Level.FINER, "Seller: {0}, Item: {1}", this.getClass(), email, productunit);

                Collection<Productvariant> sellerProducts = allSellerProducts.get(email);
                if(sellerProducts == null) {
                    sellerProducts = new ArrayList<>();
                    allSellerProducts.put(email, sellerProducts);
                }

                sellerProducts.add(productunit);
            }

            ProductvariantHtmlGen productParser = new ProductvariantHtmlGen();
            productParser.setExternalOutput(true);

            for(String sellerEmail:allSellerProducts.keySet()) {

                HtmlEmail email = new DefaultHtmlEmail();
                
                String subject = this.getSellerSubject(sellerEmail, null, shoppingCart);
                StringBuilder message = this.getSellerMessage(sellerEmail, null, shoppingCart);

                Collection<Productvariant> sellerProducts = allSellerProducts.get(sellerEmail);

    Log.getInstance().log(Level.FINE, "Seller: {0}, Item count: {2}", 
    this.getClass(), sellerEmail, sellerProducts==null?null:sellerProducts.size());

                email.setSubject(subject);

                productParser.updateEmail(sellerProducts, message, email);

                email.addTo(sellerEmail);

                email.send();
            }
        }catch(EmailException e) {
            throw new ServletException("Error while trying to notify buyer by email", e);
        }
    }
    
    private String getBuyerSubject(PaymentRequest pmtRequest) throws EmailException {
        return WebApp.getInstance().getName() + " Transaction Notification";
    }
    
    private StringBuilder getBuyerMessage(PaymentRequest pmtRequest) throws EmailException {
        
        UserBean user = pmtRequest.getUser();
        
        StringBuilder message = new StringBuilder();
        message.append("Hello ").append(user.getName()).append(',');
        message.append("<br/><br/>Your order for the underlisted items has been successfully taken.");
        BigDecimal paymentAmount = user.getShoppingCart().getPaymentAmount();
        if(paymentAmount != null) {
            message.append("<br/>Amount Paid: ").append(paymentAmount);
        }        
        message.append("<br/>Payment Code: ").append(pmtRequest.getPaymentCode());
        message.append("<br/>You will be notified at every stage of the delivery process.");
        
        return message;
    }

    private String getSellerSubject(
            String emailAddress, String userName, com.looseboxes.web.components.ShoppingCart shoppingCart) 
            throws EmailException {
        
        return "An order has been placed for a product you uploaded on "+WebApp.getInstance().getName();
    }
    
    /**
     * @param emailAddress The email Address of the seller
     * @param userName The user name of the seller, may be null
     * @param shoppingCart The shopping cart through which the order (containing
     * one or more of the seller's product) was made.
     * @return An Email for notifying the seller of the transaction
     */
    private StringBuilder getSellerMessage(
            String emailAddress, String userName, com.looseboxes.web.components.ShoppingCart shoppingCart) 
            throws EmailException {

// Username may be null        
        
        // Notify product owner of order
        // Explain why they need to create and maintain shipping details
        StringBuilder message = new StringBuilder();
        if(userName != null) {
            message.append("Hello ").append(userName).append(',');
        }else{
            message.append("Hi,");
        }
        message.append("<br/><br/>Your item(s) (listed below) were ordered for on ");
        message.append(WebApp.getInstance().getName());
//        message.append("<br/><br/><b>It is very important to update shipping details at every step of the delivery process</b>. ");
//        message.append("Following the steps here to update shipping details");
//        message.append("<br/><br/>");
//        String whyUpdateShippingDetails = this.getWhyUpdateShippinigDetails();
//        message.append(whyUpdateShippingDetails);
        
        return message;
    }
}
