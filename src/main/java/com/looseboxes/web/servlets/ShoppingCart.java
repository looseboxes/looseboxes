package com.looseboxes.web.servlets;

/**
 * @(#)ShoppingCart.java   11-May-2013 20:23:46
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.looseboxes.pu.entities.Orderproduct_;
import com.looseboxes.pu.entities.Paymentmethod_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.pu.entities.Userpaymentmethod;
import com.looseboxes.pu.entities.Userpaymentmethod_;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.ProductBean;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.exceptions.LoginException;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.annotation.WebServlet;
import com.bc.jpa.context.JpaContext;

/**
 * Handles requests to: view/update a user's shopping cart.
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="ShoppingCart", urlPatterns={"/cart"})
public class ShoppingCart extends BaseServlet {
    
    private String forwardPage;

    public ShoppingCart() { }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return forwardPage;
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, 
            HttpServletResponse response) 
             throws ServletException, IOException {
       
        Map<String, String> params = ServletUtil.getParameterMap(request);
        
Log.getInstance().log(Level.FINE, "Parameters: {0}", this.getClass(), params);        
        
        final String action = params.get("action");
        
        if(action == null) {
            throw new NullPointerException();
        }
        
        switch(action) {
            case "add": 
                this.forwardPage = WebPages.PRODUCTS_SEARCHRESULTS;
                this.addToCart(request, params); 
                break;
            case "remove": 
                this.forwardPage = WebPages.PRODUCTS_SEARCHRESULTS;
                this.removeFromCart(request, params); 
                break;
            case "delete": 
                this.forwardPage = WebPages.PRODUCTS_SEARCHRESULTS;
                UserBean user = this.getUser(request);
                user.deleteShoppingCart();
                break;
            case "clear":
                this.forwardPage = WebPages.PRODUCTS_SEARCHRESULTS;
                user = this.getUser(request);
                user.clearShoppingCart();
                break;
            case "checkout":
                this.forwardPage = WebPages.CART_INDEX;
                this.checkout(request, response);
                break;
            case "buynow":
                this.forwardPage = WebPages.PRODUCTS_SEARCHRESULTS;
                this.addToCart(request, params);
                this.buyNow(request, response);
            default :    
                throw new IllegalArgumentException("Unexpected value for parameter 'action': "+action);
        }
    }
    
    private void buyNow(            
            HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
        
        UserBean user = this.getUser(request);

        if(!user.isLoggedIn()) {
            throw new LoginException("Please login to pay for the items in your cart.");
        }
        
        List<Userpaymentmethod> paymentOptions = user.getPaymentoptions();

        // Buy Now is express only if the user has at least 1 payment option
        //
        if(paymentOptions != null && !paymentOptions.isEmpty()) {

            // The checkout servlet requires these attributes
            //
            Userpaymentmethod selectedPaymentoption = paymentOptions.get(0);
            
            user.getShoppingCart().setSelectedPaymentmethod(selectedPaymentoption);
            
            this.checkout(request, response);

        }else{
            
            this.addMessage(MessageType.informationMessage, "Please select a payment method");

            this.forwardPage = WebPages.CART_INDEX;
        }
    }

    private void checkout(            
            HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
        
        UserBean user = this.getUser(request);
        
        if(!user.isLoggedIn()) {
            throw new LoginException("Please login to pay for the items in your cart.");
        }
        
        Userpaymentmethod selectedPaymentoption = user.getShoppingCart().getSelectedPaymentmethod();

        // Buy Now is express only if the user has at least 1 payment option
        //
        if(selectedPaymentoption != null) {

            Integer userpaymentmethodId = selectedPaymentoption.getUserpaymentmethodid();
            request.setAttribute(Userpaymentmethod_.userpaymentmethodid.getName(), userpaymentmethodId);
            
            Short paymentmethodId = selectedPaymentoption.getPaymentmethodid().getPaymentmethodid();
            request.setAttribute(Paymentmethod_.paymentmethodid.getName(), paymentmethodId);

            Checkout checkout = new Checkout();

            checkout.processRequest(request, response, false);

            this.addMessages(checkout.getMessages());
            
            this.forwardPage = checkout.getForwardPage(request);

        }else{
            
            this.addMessage(MessageType.informationMessage, "Please select a payment method.");

            this.forwardPage = WebPages.CART_INDEX;
        }
    }
    
    private void addToCart(
            HttpServletRequest request, Map<String, String> params) 
            throws ServletException {
        
        UserBean user = this.getUser(request);
        
        com.looseboxes.web.components.ShoppingCart shoppingCart = user.getShoppingCart();

Log.getInstance().log(Level.FINER, "ShoppingCart: {0}", this.getClass(), shoppingCart);        

        int qty = this.getInt(params.get(Orderproduct_.quantity.getName()), -1);
        
        Productvariant variant = this.getSelectedVariant(user, params);
        
        shoppingCart.add(variant, qty);
        
        if(!user.isLoggedIn() && !shoppingCart.isSyncWithDatabase()) {
            
/////////////////////////////////////////////////////////////////////////////////////////////////        
// A non loggedin user who adds to shopping cart is automatically given a long session life span        
/////////////////////////////////////////////////////////////////////////////////////////////////        
            
            int timeOutDays = WebApp.getInstance().getConfig().getInt(AppProperties.REMEMBERME_TIMEOUT_DAYS, 30);
            
            request.getSession().setMaxInactiveInterval((int)TimeUnit.DAYS.toSeconds(timeOutDays));
        }
    }
    
    private void removeFromCart(
            HttpServletRequest request, Map<String, String> params) throws ServletException {
        
        UserBean user = this.getUser(request);
        
        com.looseboxes.web.components.ShoppingCart shoppingCart = user.getShoppingCart();
        
Log.getInstance().log(Level.FINER, "ShoppingCart: {0}", this.getClass(), shoppingCart);        

        int qty = this.getInt(params.get(Orderproduct_.quantity.getName()), -1);
        
        Productvariant variant = this.getSelectedVariant(user, params);
        
        shoppingCart.remove(variant, qty);
    }
    
    private Productvariant getSelectedVariant(UserBean user, Map<String, String> params) {
        Productvariant output = null;
        Integer productvariantid = this.getInteger(params.get(Productvariant_.productvariantid.getName()));
        if(productvariantid == null) {
            throw new NullPointerException();
        }
        ProductBean selectedItem = user.getSelectedItem();
        if(selectedItem != null) {
            Productvariant selectedVariant = selectedItem.getSelectedVariant();
            if(selectedVariant != null && selectedVariant.getProductvariantid().equals(productvariantid)) {
                output = selectedVariant;
            }
        }
        if(output == null) {
            JpaContext cf = WebApp.getInstance().getJpaContext();
            EntityController<Productvariant, Integer> ec = cf.getEntityController(Productvariant.class, Integer.class);
            output = ec.find(productvariantid);
        }
        return output;
    }
    
    private UserBean getUser(HttpServletRequest request) {
        return (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
    }
    
    private int getInt(String sval, int defaultVal)  {
        int output;
        if(sval != null) {
            output = Integer.parseInt(sval);
        }else{
            output = defaultVal;
        }
        return output;
    }

    private Integer getInteger(String sval)  {
        Integer output;
        if(sval != null) {
            output = Integer.valueOf(sval);
        }else{
            output = null;
        }
        return output;
    }
}//END
