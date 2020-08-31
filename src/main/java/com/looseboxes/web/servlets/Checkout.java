package com.looseboxes.web.servlets;

/**
 * @(#)Checkout.java   18-May-2013 05:25:27
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.bc.util.Log;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Paymentmethod;
import com.looseboxes.pu.entities.Paymentmethod_;
import com.looseboxes.pu.entities.Userpaymentmethod;
import com.looseboxes.pu.entities.Userpaymentmethod_;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.exceptions.LoginException;
import com.looseboxes.web.exceptions.LoginToCheckoutException;
import com.looseboxes.web.payment.PaymentRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.looseboxes.web.components.forms.FormOld;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;

/**
 * Handles requests to: checkout of a shopping cart.
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0s
 */
@WebServlet(name="Checkout", urlPatterns={"/checkout"})
public class Checkout extends BaseServlet {
    
    /**
     * Currently we use VoguePay which helps us handle a myriad of payment systems
     * including Master/Visa/Verve card and eTransact (PocketMoni)
     */
    private boolean ownPaymentsystemAvailable;
    
    private String forwardPath;
    
    public Checkout() {}

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        if(message instanceof LoginException) {
            return WebPages.CART_LOGINTOCHECKOUT;
        }else{
            return WebPages.CART_INDEX;
        }
    }
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return forwardPath;
    }

    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        this.forwardPath = WebPages.CART_INDEX;

        UserBean user = this.getUser(request);
        
        // @related_52 If true user selected to make a down payment 
        // 
        Object initialPmtSelected = ServletUtil.find("initialPaymentSelected", request);        
        
Log.getInstance().log(Level.FINER, "Part payment selected: {0}", this.getClass(), initialPmtSelected);

        if(initialPmtSelected != null) {
            user.getShoppingCart().setInitalPaymentSelected(Boolean.parseBoolean(initialPmtSelected.toString()));
        }

        Object userPmtmethodid = ServletUtil.find(Userpaymentmethod_.userpaymentmethodid.getName(), request);
        if(userPmtmethodid != null) {
//@related payment method session attributes
//
            request.getSession().setAttribute(Userpaymentmethod_.userpaymentmethodid.getName(), userPmtmethodid);
        }
            
        Object pmtmethodid = ServletUtil.find(Paymentmethod_.paymentmethodid.getName(), request);
        if(pmtmethodid != null) {
//@related payment method session attributes
//
            request.getSession().setAttribute(Paymentmethod_.paymentmethodid.getName(), pmtmethodid);
        }
        
        this.checkLogin(user);
        
        if(user.getShoppingCart().isShippingDetailsNeeded()) {
            
            this.forwardPath = WebPages.CART_ADDSHIPPING;
            
//@related NextServletClass session attribute
//            
            // By doing this we are saying checkout after adding shipping
            HttpSession session = request.getSession();
  
// Using the class as a session attribute didn't work, so we use the class name            
//            session.setAttribute("NextServletClass", this.getClass());
            session.setAttribute(Checkout.NEXT_SERVLET_CLASSNAME, this.getClass().getName());
            
Log.getInstance().log(Level.FINE, "Setting next servlet class to: {0} for session: {1}", 
        this.getClass(), this.getClass(), session.getId());
            
            this.addMessage(MessageType.informationMessage, "Please provide a delivery address.");
            
            return;
        }
        
Log.getInstance().log(Level.FINER, 
"Selected. paymentmethodid: {0}, userpaymentmethodid: {1}", 
this.getClass(), pmtmethodid, userPmtmethodid);

        if(pmtmethodid == null) { 
            
            if(userPmtmethodid == null) {
                throw new ServletException("Please select a payment method");
            }
            
            this.checkLogin(user);
            
            // User selected to use an existing payment option
            
            // Search for the paymentmethod in the user
            
            Userpaymentmethod upm = this.getUserpaymentmethod(user, Integer.valueOf(userPmtmethodid.toString()));
            
            assert upm != null;

            this.payForItem(upm, request, response);
            
        }else { // User selected to use a new payment option
            
            JpaContext cf = WebApp.getInstance().getJpaContext();
            
            EnumReferences refs = cf.getEnumReferences();
            
            References.paymentmethod paymentmethodEnum = (References.paymentmethod)refs.getEnum(
                    Paymentmethod_.paymentmethodid.getName(), pmtmethodid);
            
            boolean electronic = PaymentRequest.isElectronic(paymentmethodEnum);
            
Log.getInstance().log(Level.FINER, "Payment method enum: {0}, isElectronic: {1}",
        this.getClass(), paymentmethodEnum, electronic);

            if(electronic && this.ownPaymentsystemAvailable) {
                
                // Only electronic payments require filling in form details ???
                this.addUserpaymentmethod(request, user, paymentmethodEnum);
                
            }else{
                
                Paymentmethod paymentmethod = (Paymentmethod)cf.getEnumReferences().getEntity(paymentmethodEnum);
                
                Userpaymentmethod upm = this.getUserpaymentmethod(user, paymentmethod);
                
                if(upm == null) {
                    
                    upm = this.createUserpaymentmethod(user, paymentmethod);
                    
                    upm.setDatecreated(new Date());
                    
                    EntityController<Userpaymentmethod, Integer> ec = 
                            cf.getEntityController(Userpaymentmethod.class, Integer.class);

                    try{
                        ec.persist(upm);
Log.getInstance().log(Level.FINE, "Created new user payment method: {0}", this.getClass(), upm);
                        
                    }catch(Exception e) {
                        throw new ServletException("An unexpected exception occured while processing the request", e);
                    }
                }

                this.payForItem(upm, request, response);
            }
        }
    }
    
    private void addUserpaymentmethod(
            HttpServletRequest request, UserBean user, 
            References.paymentmethod paymentmethodEnum) 
            throws LoginException {
        
        this.checkLogin(user);
        
        // Only electronic payments require filling in form details ???

        this.forwardPath = WebPages.CART_ADDPAYMENT;
        
        FormOld form = (FormOld)ServletUtil.find("InsertUserpaymentmethod", request);
        
        form.setStage(0);

        Map params = ServletUtil.getParameterMap(request);
Log.getInstance().log(Level.FINER, "Request parameters: {0}", this.getClass(), params);

        params.put(Userpaymentmethod_.paymentmethoduser.getName(), user.getDetails());
        params.put(Userpaymentmethod_.paymentmethodid.getName(), paymentmethodEnum);

        form.addSelectedDetails(params);

Log.getInstance().log(Level.FINER, "{0}. Form: {1}", this.getClass(), form);
    }
    
    public Userpaymentmethod createUserpaymentmethod(UserBean user, Paymentmethod paymentmethod) throws LoginException {
        
        if(paymentmethod == null) {
            throw new NullPointerException();
        }
        
        this.checkLogin(user);
        
        Userpaymentmethod upm = new Userpaymentmethod();
        upm.setBillingAddress(user.getDetails().getAddressid());
        upm.setPaymentmethodid(paymentmethod);
        upm.setPaymentmethoduser(user.getDetails());
        
        return upm;
    }
    
    public Userpaymentmethod getUserpaymentmethod(
            UserBean user, Paymentmethod paymentmethod) throws LoginException {
        
Log.getInstance().log(Level.FINER, "Select payment method: {0}",  this.getClass(), paymentmethod);

        this.checkLogin(user);

        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        
        try(Select<Userpaymentmethod> qb = jpaContext.getDaoForSelect(Userpaymentmethod.class)) {
            List<Userpaymentmethod> found = 
                qb.from(Userpaymentmethod.class)
                .where(Userpaymentmethod_.paymentmethodid.getName(), paymentmethod)
                .and().where(Userpaymentmethod_.paymentmethoduser.getName(), user.getDetails())
                .createQuery().setMaxResults(1).getResultList();
            Userpaymentmethod output = found == null || found.isEmpty() ? null : found.get(0);
Log.getInstance().log(Level.FINE, "Select user payment method: {0}", this.getClass(), output);
            return output;
        }
    }
    
    public Userpaymentmethod getUserpaymentmethod(UserBean user, Integer ival) {
        
        Userpaymentmethod output = null;
        
Log.getInstance().log(Level.FINE, "Selected user payment method ID: {0}",  this.getClass(), ival);

        List<Userpaymentmethod> upms = user.getPaymentoptions();

Log.getInstance().log(Level.FINER, "User has {0} payment methods",  this.getClass(), upms==null?null:upms.size());
        
        for(Userpaymentmethod upm:upms) {
            if(upm.getUserpaymentmethodid().equals(ival)) {
                output = upm;
                break;
            }
        }
Log.getInstance().log(Level.FINE, "Select user payment method: {0}", this.getClass(), output);
        return output;
    }

    public final void payForItem(Userpaymentmethod upm, 
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
        
        if(upm == null) {
            throw new NullPointerException();
        }
        
        UserBean user = this.getUser(request);
        
        // In addition to being used as a servlet, Checkout may be used thus:
        // new Checkout().handleRequest, new Checkout().payForItem 
        // Hence the additional check
        //
        this.checkLogin(user);
        
        com.looseboxes.web.components.ShoppingCart cart = user.getShoppingCart();
        
        PaymentRequest paymentRequest = PaymentRequest.getInstance(cart.getOrderId(), upm);

Log.getInstance().log(Level.FINE, "Payment request: {0}", this.getClass(), paymentRequest);        

        cart.trimQuantities();

        cart.validateQuantities();
        
        cart.setSelectedPaymentmethod(upm);
        
        String param = (String)ServletUtil.find("useHtmlPages", request);
        
        boolean useHtmlPages = "1".equals(param);
        
Log.getInstance().log(Level.FINER, "Use HTML Pages: {0}", this.getClass(), useHtmlPages);        
        paymentRequest.setUseHtmlPaymentPage(useHtmlPages);
        
        paymentRequest.processRequest(request, response); 

Log.getInstance().log(Level.FINE, "Payment status: {0}", this.getClass(), paymentRequest.getPaymentStatus());

//@related payment method session attributes
//
        request.getSession().removeAttribute(Userpaymentmethod_.userpaymentmethodid.getName());
        request.getSession().removeAttribute(Paymentmethod_.paymentmethodid.getName());

        this.forwardPath = paymentRequest.getForwardPage();

        this.addMessages(paymentRequest.getMessages());
    }

    private UserBean getUser(HttpServletRequest request) {
        return (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
    }
    
    private void checkLogin(UserBean user) throws LoginToCheckoutException {
        
        if(!user.isLoggedIn()) {
            
            // This exception is handled by a special login page
            // The special login page forwards to a servlet which:
            // Register the user (if necessary), Login the user, Pay for items in the shopping cart
            // 
            throw new LoginToCheckoutException("Please login to pay for items in your shopping cart");
        }
    }
}


