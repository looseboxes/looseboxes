package com.looseboxes.web.components;

import com.bc.util.Log;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import com.bc.jpa.controller.EntityController;
import com.bc.jpa.exceptions.NonexistentEntityException;
import com.bc.jpa.exceptions.PreexistingEntityException;
import com.bc.jpa.fk.EnumReferences;
import com.bc.util.CurrencyFormatter;
import com.looseboxes.web.AppProperties;
import com.looseboxes.core.LbApp;
import com.looseboxes.core.LbCurrencyFormatter;
import com.looseboxes.core.ProductorderManager;
import com.looseboxes.core.Util;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Orderproduct_;
import com.looseboxes.pu.entities.Orderstatus;
import com.looseboxes.pu.entities.Payment;
import com.looseboxes.pu.entities.Payment_;
import com.looseboxes.pu.entities.Paymentmethod;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Shippingdetails;
import com.looseboxes.pu.entities.Shippingdetails_;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Userpaymentmethod;
import com.looseboxes.web.ServletMailer;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.mail.DefaultHtmlEmail;
import com.looseboxes.web.payment.PaymentRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;

/**
 * @(#)ShoppingCart.java   29-May-2013 22:24:49
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
/**
 * <p>Usage:</p>
 * <pre>
 * <code>
 *   User user; // The session user 
 *   ShoppingCart cart = new ShoppingCart();
 *   cart.setUser(user);
 *   cart.setOrderId(234); // may be null. If null the last order will be loaded
 * </code>
 * </pre>
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public class ShoppingCart extends ProductorderManager implements Serializable{
    
    private boolean syncWithDatabase;
    
    private boolean initalPaymentSelected; 
    
    private Userpaymentmethod selectedPaymentoption;

    private UserBean user;
    
    private Productorder order;
    
    private final Delivery delivery;
    
    public ShoppingCart() { 
        this(true, null, null);
    }
    
    public ShoppingCart(boolean syncWithDatabase, UserBean user, Integer orderId) { 
        this.syncWithDatabase = syncWithDatabase;
        this.delivery = new Delivery(this);
        if(user != null) {
            ShoppingCart.this.setUser(user);
            ShoppingCart.this.setOrderId(orderId);
        }
Log.getInstance().log(Level.FINER, "Done creating shopping cart: {0}", this.getClass(), ShoppingCart.this);        
    }
    
    public boolean isFreeShipping() {
        return this.delivery.isFreeShipping(this.getTotalAmount());
    }
    
    public boolean isFreeShippingLocation() {
        return this.delivery.isFreeShippingLocation();
    }
    
    public boolean isFreeShippingAmount() {
        return this.delivery.isFreeShippingAmount(this.getTotalAmount());
    }
    
    /**
     * Sets the Shopping cart's user to the specified user
     * @param u 
     */
    public void setUser(UserBean u) {
        this.user = u;
        if(u != null) {
            u.setShoppingCart(ShoppingCart.this);
        }
    }

    public Delivery getDelivery() {
        return delivery;
    }
    
    public UserBean getUser() {
        return this.user;
    }
    
    public boolean contains(Orderproduct orderproduct) { 
        if(this.order == null) {
            return false;
        }
        List<Orderproduct> items = this.order.getOrderproductList();
        if(items == null || items.isEmpty()) {
            return false;
        }
        return items.contains(orderproduct);
    }

    public boolean contains(Productvariant variant) { 
        return this.getOrderItem(variant) != null;
    }
    
    /**
     * Loads the order with id <tt>orderId</tt> from the database, and populates
     * this ShoppingCart with order items.
     * @param orderId The orderId for the order to be loaded from the database.
     */
    public void setOrderId(Integer orderId) {
        
        // Order of method call important
        //
        this.order = this.getOrder(orderId, true);
        
       final Siteuser userEntity = this.order.getBuyer();
            
        if(this.user == null) {
        
            if(userEntity  != null) {
                this.user = new UserBean(){
                    @Override
                    public Siteuser getDetails() {
                        return userEntity;
                    }
                    @Override
                    public Siteuser getRecord() {
                        return userEntity;
                    }
                };
            }
        }else{

            if(user.getDetails() != null && !user.getDetails().equals(userEntity)) {
                throw new UnsupportedOperationException();
            }
        }
        
        if(this.order.getOrderproductList() == null) {
            this.order.setOrderproductList(new ArrayList<Orderproduct>());
        }
Log.getInstance().log(Level.FINE, "Input orderId: {0}, Output orderId: {1}",
        this.getClass(), orderId, this.getOrderId());
    }
    
    public Integer getOrderId() {
        return this.getOrder() == null ? null : this.getOrder().getProductorderid();
    }

//@todo move this method to a selector bean    
    public Map<Short, String> getPaymentTypes() {
        JpaContext cf = this.getJpaContext();
        EnumReferences refs = cf.getEnumReferences();
        Map entities = refs.getEntities(References.paymentmethod.class);
        Map paymentMethods = new HashMap(entities.size(), 1.0f);
        Collection c = entities.values();
        for(Object e:c) {
            Paymentmethod pm = (Paymentmethod)e;
            paymentMethods.put(pm.getPaymentmethodid(), pm.getPaymentmethod());
        }
Log.getInstance().log(Level.FINER, "Payment types: {0}", this.getClass(), paymentMethods);        
        return paymentMethods;
    }
    
    public List<Orderproduct> getItems() {
        return this.order == null ? Collections.EMPTY_LIST : this.order.getOrderproductList();
    }
    
    /**
     * product_1 = 4 units<br/>
     * product_2 = 3 units<br/>
     * {@link #getProductCount()} = <b>2</b><br/>
     * {@link #getItemCount()} = <b>7</b><br/>
     * @return The product count
     */
    public int getProductCount() {
        
        int productCount  = 0;
        
        if(this.order != null) {
        
            List<Orderproduct> orderItems = this.order.getOrderproductList();

            if(orderItems != null) {
                
                productCount = orderItems.size();
            }
        }
        
Log.getInstance().log(Level.FINER, "Product count: {0}", this.getClass(), productCount);        

        return productCount;
    }
    
    /**
     * product_1 = 4 units<br/>
     * product_2 = 3 units<br/>
     * {@link #getProductCount()} = <b>2</b><br/>
     * {@link #getItemCount()} = <b>7</b><br/>
     * @return The item count
     */
    public int getItemCount() {
        
        return this.getItemCount(order);
    }
    
    public int getInitialPaymentPercent() {
        Double d = WebApp.getInstance().getConfig().getDouble(AppProperties.INITIAL_PAYMENT_ALLOWED);
        return d == null ? -1 : d.intValue();
    }
    
    public BigDecimal getInitialPaymentAmount() {
        int percentValue = this.getInitialPaymentPercent();
        MathContext ctx = this.getDefaultMathContext();
        // factor = percentValue divided by 100
        BigDecimal factor = new BigDecimal(percentValue).divide(new BigDecimal(100), ctx);            
        BigDecimal paymentAmount = getTotalAmount().multiply(factor, ctx);
Log.getInstance().log(Level.FINE, "Initial payment amount: {0}", this.getClass(), paymentAmount);
        return paymentAmount;
    }
    
    public BigDecimal getPaymentAmount() {
        return this.getSelectedAmount().add(
                this.getDeliveryAmount(), 
                this.getDefaultMathContext());
    }
    
    public BigDecimal getSelectedAmount() {
        if(this.isInitalPaymentSelected()) {
            return this.getInitialPaymentAmount();
        }else{
            return this.getTotalAmount();
        }
    }
    
    public BigDecimal getTotalAmount() {

        CurrencyFormatter currFmt = new LbCurrencyFormatter();
        
        MathContext mc = WebApp.getInstance().getDefaults().getDefaultMathContext();
        
        final BigDecimal ZERO = new BigDecimal(0, mc); 

        List<Orderproduct> results = this.order.getOrderproductList();
            
        Iterator<Orderproduct> iter = results.iterator();
        
        float amount  = 0;
        float fmtAmount = 0;
        
        while(iter.hasNext()) {

            Orderproduct orderItem = iter.next();

            int quantity = orderItem.getQuantity();

            BigDecimal price = orderItem.getPrice();
            BigDecimal discount = orderItem.getDiscount();

            if(discount == null) {
                discount = ZERO;
            }

            com.looseboxes.pu.entities.Currency curr = orderItem.getCurrencyid();
            Locale orderItemLocale = Util.getLocaleForCurrencyCode(curr.getCurrency(), Locale.getDefault());
            Locale userLocale = user.getLocale();
            
Log.getInstance().log(Level.FINER, "Order item locale: {0}, user locale: {1}", 
this.getClass(), orderItemLocale.getDisplayName(), userLocale.getDisplayName());

            double fmtPrice;
            double fmtDiscount;
            if(!orderItemLocale.equals(userLocale)) {
                fmtPrice = this.format(currFmt, price, orderItemLocale, userLocale);
                fmtDiscount = this.format(currFmt, discount, orderItemLocale, userLocale);
            }else{
                fmtPrice = price.doubleValue();
                fmtDiscount = discount.doubleValue();
            }

            double itemAmount = ((price.subtract(discount)).doubleValue() * quantity);
            
            amount += itemAmount;
            
            double fmtItemAmount = ((fmtPrice - fmtDiscount) * quantity);
            
Log.getInstance().log(Level.FINER, "Orderproduct ID: {0}, discount price: {1}, quantity: {2}, amount: {3}",
this.getClass(), orderItem.getOrderproductid(), (fmtPrice - fmtDiscount), quantity, fmtItemAmount);

            fmtAmount += fmtItemAmount;
            
        }

        BigDecimal selling = new BigDecimal(fmtAmount, mc);
        
Log.getInstance().log(Level.FINE, "Amount: {0}, locale formatted amount: {1}, output: {2}", this.getClass(), amount, fmtAmount, selling);        

        return selling;
    }
    
    public BigDecimal getDeliveryAmount() {
        float totalWeight = this.getTotalWeight(); 
        BigDecimal deliveryAmount = this.delivery.getDeliveryAmount(totalWeight);
Log.getInstance().log(Level.FINE, "Total weight: {0}, delivery amount: {1}", this.getClass(), totalWeight, deliveryAmount);
        return deliveryAmount;
    }

    public float getTotalWeight() {
        
        float totalWeight = 0f;
        
        List<Orderproduct> orderproducts = this.order.getOrderproductList();

        if(orderproducts != null) {
            
            for(Orderproduct orderproduct:orderproducts) {

                Productvariant variant = orderproduct.getProductvariantid();
                
                float toAdd = this.delivery.getWeight(variant, 0);
                
Log.getInstance().log(Level.FINER, "Order product ID: {0}, weight: {1}, computed weight: {2}", 
        this.getClass(), orderproduct.getOrderproductid(), variant.getWeight(), toAdd);        
                
                totalWeight += (toAdd * orderproduct.getQuantity());
            }
        }
Log.getInstance().log(Level.FINE, "Total weight: {0}", this.getClass(), totalWeight);        
        
        return totalWeight;
    }
    
    private double format(
            CurrencyFormatter currFmt, BigDecimal input, 
            Locale inputLocale, Locale outputLocale) {
        double output = 0;
        if(input != null && input.doubleValue() != 0) {
            Number n = ((Number)currFmt.convertNumber(
                input, inputLocale, outputLocale));
            if(n != null) {
                output = n.doubleValue();
            }
        }
        return output;
    }
    
    private int getMinimumOrderQuantity(Product product, int defaultValue) {
        return product.getMinimumOrderQuantity() < 1 ? defaultValue : product.getMinimumOrderQuantity();
    }
    
    public void add(Productvariant productunit) throws ServletException {
        add(productunit, this.getMinimumOrderQuantity(productunit.getProductid(), 1));
    }
    
    public boolean add(Productvariant productunit, int quantity) throws ServletException {

        boolean updated;
Log.getInstance().log(Level.FINE, "Quantity: {0}, product: {1}, productunit: {2}", 
        this.getClass(), quantity, productunit.getProductid(), productunit);    
        
        if(quantity < 1) {
            quantity = this.getMinimumOrderQuantity(productunit.getProductid(), 1);
        }
        
        this.validateQuantity(productunit, quantity); 
        
        this.validateQuantityForAdditon(productunit, quantity);
        
Log.getInstance().log(Level.FINE, "Productvariant ID: {0}, available: {1}, qty to add: {2}",
this.getClass(), productunit.getProductvariantid(), this.getAvailable(productunit), quantity);
        
        this.validatePrice(productunit);
        
        Orderproduct orderItem = this.getOrderItem(productunit);
        
        JpaContext cf = this.getJpaContext();
        
        EntityController<Orderproduct, Integer> ec = cf.getEntityController(Orderproduct.class, Integer.class);
        
        if(orderItem == null) {
            
            orderItem = new Orderproduct();
            orderItem.setCurrencyid(user.getCurrencyEntity());
            orderItem.setDatecreated(new Date());
            Product product = productunit.getProductid();
            orderItem.setDiscount(product.getDiscount());
            orderItem.setPrice(product.getPrice());
            orderItem.setProductvariantid(productunit);
            orderItem.setProductorderid(this.getOrder());
            orderItem.setQuantity(quantity);

Log.getInstance().log(Level.FINER, "Adding: {0}", this.getClass(), orderItem);    

            try{
                
                List<Orderproduct> orderItems = this.order.getOrderproductList();

Log.getInstance().log(Level.FINER, "BEFORE ADDING. item quantity: {0}, cart size: {1}", this.getClass(), orderItem.getQuantity(), this.getItemCount());

                
                if(this.isSyncWithDatabase()) {
                    ec.persist(orderItem);  
                }
                
final int expectedSize = orderItem.getQuantity() + this.getItemCount();
                
                orderItems.add(orderItem); 
                
Log.getInstance().log(Level.FINER, "AFTER ADDING. item quantity: {0}, cart size: {1}", this.getClass(), orderItem.getQuantity(), this.getItemCount());
                
assert this.getItemCount() == expectedSize : "Size mismatch for order items in cart, after adding an order item. Expected: "+expectedSize+", found: "+this.getItemCount();

                updated = true;
                
            }catch(Exception e) {
                
                updated = false;
                
                Log.getInstance().log(Level.WARNING, "Error updating order item: "+orderItem, this.getClass(), e);
            }
Log.getInstance().log(Level.FINER, "After adding: {0}", this.getClass(), orderItem);    
            
        }else{

final int expectedSize = quantity + this.getItemCount();
            
            this.addQuantity(orderItem, quantity);
            
assert this.getItemCount() == expectedSize : "Size mismatch for order items in cart, after incrementing qty of an order item. Expected: "+expectedSize+", found: "+this.getItemCount();
            
            try{
                
                if(this.isSyncWithDatabase()) {

Log.getInstance().log(Level.FINER, "BEFORE UPDATING. item quantity: {0}, cart size: {1}", 
        this.getClass(), orderItem.getQuantity(), this.getItemCount());
                    
                    ec.merge(orderItem);
  
Log.getInstance().log(Level.FINE, "AFTER UPDATING. item quantity: {0}, cart size: {1}", 
        this.getClass(), orderItem.getQuantity(), this.getItemCount());
                }    
                
                updated = true;
                
            }catch(Exception e) {
                updated = false;
                Log.getInstance().log(Level.WARNING, "Error updating order item: "+orderItem, this.getClass(), e);
            }
        }
        
        return updated;
    }
    
    public void remove(Productvariant productunit) throws ServletException {
        remove(productunit, this.getMinimumOrderQuantity(productunit.getProductid(), 1));
    }

    public void remove(Productvariant productunit, int quantity) throws ServletException {
        
Log.getInstance().log(Level.FINE, "Quantity: {0}, product: {1}, variant: {2}", 
        this.getClass(),  quantity, productunit.getProductid(), productunit);        

        Orderproduct orderItem = this.getOrderItem(productunit);
        
        if(orderItem == null) {
            throw new ServletException("Could not find order item with productId: "+productunit.getProductid().getProductid()+", productunitId: "+productunit.getProductvariantid());
        }

        if(quantity < 1) {
            quantity = this.getMinimumOrderQuantity(productunit.getProductid(), 1);
        }
        
Log.getInstance().log(Level.FINER, "Selected item: {0}", this.getClass(), orderItem);        

        this.validateQuantity(productunit, quantity);

Log.getInstance().log(Level.FINE, "Productvariant ID: {0}, qty in cart: {1}, qty to remove: {2}",
this.getClass(), productunit.getProductvariantid(), orderItem.getQuantity(), quantity);
        
        boolean qtyRemoved = false;
        boolean updated = false;
        boolean itemRemoved = false;
        try{
            
            JpaContext cf = this.getJpaContext();
            
            EntityController<Orderproduct, Integer> ec = cf.getEntityController(Orderproduct.class, Integer.class);

            qtyRemoved = true;

final int expectedSize = this.getItemCount() - quantity;

Log.getInstance().log(Level.FINER, "BEFORE REMOVING. Item quantity: {0}, cart size: {1}", 
        this.getClass(), orderItem.getQuantity(), this.getItemCount());

// After this the original Orderitem.getQuatity would be have changed
//
            final int updatedQty = orderItem.getQuantity() - quantity;

            if(updatedQty == 0) {
Log.getInstance().log(Level.FINER, "Removing item: {0}", this.getClass(), orderItem);        

                List<Orderproduct> orderItems = this.order.getOrderproductList();

                int updateCount = 0;
                
                if(this.isSyncWithDatabase()) {
                    updateCount = ec.deleteById(orderItem.getOrderproductid()); 
                }    

Log.getInstance().log(Level.FINER, "Update count: {0}", this.getClass(), updateCount);

                updated = updateCount > 0;
                
                if(!this.isSyncWithDatabase() || updated) {

                    itemRemoved = orderItems.remove(orderItem); 
                    
Log.getInstance().log(Level.FINER, "Removed: {0}", this.getClass(), itemRemoved);
               
assert this.getItemCount() == expectedSize : "Size mismatch for order items in cart, after removing an order item. Expected: "+expectedSize+", found: "+this.getItemCount();

                }
Log.getInstance().log(Level.FINE, "AFTER REMOVING. cart size: {0}", this.getClass(), this.getItemCount());

assert itemRemoved : "Remove failed for: "+ec.toMap(orderItem);

        }else{

Log.getInstance().log(Level.FINER, "Updating item: {0}", this.getClass(), orderItem);        

                try{
                    
                    if(this.isSyncWithDatabase()) {
Log.getInstance().log(Level.FINER, "BEFORE UPDATING. item quantity: {0}, cart size: {1}", 
        this.getClass(), orderItem.getQuantity(), this.getItemCount());
                        
                        int updateCount = ec.updateById(orderItem.getOrderproductid(), Orderproduct_.quantity.getName(), updatedQty);
                        
Log.getInstance().log(Level.FINE, "AFTER UPDATING. item quantity: {0}, cart size: {1}", 
        this.getClass(), orderItem.getQuantity(), this.getItemCount());

                        updated = updateCount > 0;
                    }    
                    
                    if(!this.isSyncWithDatabase() || updated) {
                        
                        orderItem.setQuantity(updatedQty);

assert this.getItemCount() == expectedSize : "Size mismatch for order items in cart, after decrementing qty for an order item. Expected: "+expectedSize+", found: "+this.getItemCount();
                        
                    }
                }catch(Exception e) {
                    updated = false;
                    Log.getInstance().log(Level.WARNING, "Error updating order item: "+orderItem, this.getClass(), e);
                }
            }
        }finally {
            if(qtyRemoved && this.isSyncWithDatabase() && !updated) {
                this.addQuantity(orderItem, quantity);
            }
        }
    }
    
    /**
     * Retrieves the user's shipping details from the database
     * and checks if country and streetAddress have been specified.
     * If specified, returns <tt>false</tt> otherwise return's <tt>true</tt>
     * @return boolean <tt>true</tt> If the user has not provided shipping 
     * details, otherwise returns <tt>false</tt>
     */
    public boolean isShippingDetailsNeeded() {
        Shippingdetails shipping = this.getOrderShipping();
        if(shipping == null) {
            return true;
        }
        if(shipping.getDeliveryAddress() == null) {
            return true;
        }
        return shipping.getDeliveryAddress().getStreetAddress() == null;
    }
    
    public void clear() {
        List<Orderproduct> orderItems = this.order.getOrderproductList();
        if(orderItems == null || orderItems.isEmpty()) {
            return;
        }
        
        JpaContext cf = this.getJpaContext();
        
        EntityController<Orderproduct, Integer> ec = cf.getEntityController(Orderproduct.class, Integer.class);
        
        // We remove orderproduct one by one
        Iterator<Orderproduct> iter = orderItems.iterator();
        
        while(iter.hasNext()) {
            
            Orderproduct orderproduct = iter.next();
            
            try{
                
                if(this.isSyncWithDatabase()) {
                    ec.deleteById(orderproduct.getOrderproductid());
                }
                
                iter.remove();
                
            }catch(Exception e) {
                Log.getInstance().log(Level.WARNING, "Failed to remove "+
                        Orderproduct.class.getSimpleName()+": "+orderproduct,
                        this.getClass(), e);
            }
        }
    }

    /**  
     * Rather use method {@link com.looseboxes.web.components.UserBean#deleteShoppingCart()}  
     */
    public void delete() {
        this.clear();
        if(this.isSyncWithDatabase()) {
            JpaContext cf = this.getJpaContext();
            EntityController<Productorder, Integer> ec = cf.getEntityController(Productorder.class, Integer.class);
            ec.deleteById(this.getOrderId());
        }    
        if(this.user != null) {
            this.user.setShoppingCart(null);
        }
    }
    
    public boolean concludePayment(References.paymentstatus paymentStatus) {
        if(!this.isSyncWithDatabase()) {
            throw new UnsupportedOperationException("Payment not supported for carts not connected/synchronized with the database");
        }
        if(paymentStatus == null) {
            throw new NullPointerException();
        }
        BigDecimal amount = this.getPaymentAmount();
        Integer orderId = this.getOrderId();
        boolean success = this.updatePayment(amount, paymentStatus, orderId);
        if(!success) {

            try {
                HtmlEmail htmlEmail = new DefaultHtmlEmail();
                htmlEmail.setSubject(WebApp.getInstance().getName()+" WARNING - Unable to conclude shopping process");
                htmlEmail.setHtmlMsg("Unable to update payment status to paid (after successful payment) for order with orderId: "+orderId);
                String recipient = WebApp.getInstance().getConfig().getString(AppProperties.address_default_error);
                htmlEmail.addTo(recipient);
                
                htmlEmail.send();
                
            }catch(EmailException e) {
                Log.getInstance().log(Level.WARNING, "Error while trying to notify admin of error sending shoping cart data for orderId "+orderId,
                        this.getClass(), e);
            }
        }
        return success;
    }
    
    /**
     * Call this method immediately after the order is made.
     * Payment may or may not have been made (depending on payment type)
     * e.g pay on delivery. This method sets the order status to ordered.
     * @param orderStatus
     * @return <tt>true</tt> if successful, otherwise returns <tt>false</tt>
     */
    public boolean concludeShopping(References.orderstatus orderStatus) {
        
        if(!this.isSyncWithDatabase()) {
            throw new UnsupportedOperationException("Cart not connected/synchronized with the database");
        }
        
        boolean success = this.updateOrderDateAndStatus(orderStatus, this.getOrderId());
        
        if(!success){
            try{
                
                HtmlEmail htmlEmail = new DefaultHtmlEmail();
                htmlEmail.setSubject(WebApp.getInstance().getName()+" WARNING - Unable to conclude shopping process");
                htmlEmail.setHtmlMsg("Unable to update order status to ordered (after successful payment) for order with orderId: "+this.getOrderId());
                String recipient = WebApp.getInstance().getConfig().getString(AppProperties.address_default_error);
                htmlEmail.addTo(recipient);

                htmlEmail.send();
                
            }catch(EmailException e) {
                Log.getInstance().log(Level.WARNING, "Error trying to update order data and status",
                        this.getClass(), e);
            }
        }
        
        return success;
    }

    private boolean updatePayment(BigDecimal amount, References.paymentstatus paymentStatus, Integer orderId) {
        
        if(!this.isSyncWithDatabase()) {
            throw new UnsupportedOperationException("Payment not supported for carts not connected/synchronized with the database");
        }
        
        Map whereParams = Collections.singletonMap(Payment_.productorderid.getName(), orderId);
        HashMap updateValues = new HashMap(2, 1.0f);
        updateValues.put(Payment_.paymentstatusid.getName(), paymentStatus);
        updateValues.put(Payment_.paymentAmount.getName(), amount);
        
        JpaContext cf = LbApp.getInstance().getJpaContext();
        
        EntityController<Payment, Integer> ec = cf.getEntityController(Payment.class, Integer.class);
        
        int updateCount = ec.update(whereParams, updateValues);
        
Log.getInstance().log(Level.FINE, 
"After updating payment status to {0}, update count: {1}", 
this.getClass(), paymentStatus, updateCount);        
        
        if(updateCount < 1) {
            StringBuilder msg = new StringBuilder("Failed to update paymnet status to");
            msg.append(paymentStatus);
            msg.append(" and amount to: ").append(amount);
            msg.append(" for orderId: ").append(orderId);
            new ServletMailer().sendErrorMessage(msg.toString());
        }
        
        return updateCount == 1;
    }
    
    private boolean updateOrderDateAndStatus(References.orderstatus orderStatus, Integer orderId) {
        
        if(!this.isSyncWithDatabase()) {
            throw new UnsupportedOperationException("Cart not connected/synchronized with the database");
        }
        
        Map searchParams = Collections.singletonMap(Productorder_.productorderid.getName(), orderId);
        
        HashMap updateValues = new HashMap(2, 1.0f);
        updateValues.put(Productorder_.orderDate.getName(), new Date());
        updateValues.put(Productorder_.orderstatusid.getName(), orderStatus);
        
        JpaContext cf = this.getJpaContext();
        EntityController<Productorder, Integer> ec = cf.getEntityController(Productorder.class, Integer.class);

        int updateCount = ec.update(searchParams, updateValues);
Log.getInstance().log(Level.FINE, 
"After updating order status to {0}, update count: {1}", 
this.getClass(), orderStatus, updateCount);        
        if(updateCount < 1) {
            StringBuilder msg = new StringBuilder("Failed to update paymnet status");
            msg.append(". Search parameters: ").append(searchParams);
            msg.append(". Update values: ").append(updateValues);
            new ServletMailer().sendErrorMessage(msg.toString());
        }
        return updateCount == 1;
    }
    
    private void validatePrice(Productvariant productunit) throws ServletException {
        Object priceValue = productunit.getProductid().getPrice();
        if(priceValue == null) {
            throw new ServletException("Cannot order for an item whose 'price' is not specified");
        }    
    }

    private void validateQuantity(Productvariant productunit, int quantity) {
        int moq = this.getMinimumOrderQuantity(productunit.getProductid(), 1);
        if(quantity < 1) {
            throw new IllegalArgumentException("Product quantity < 1. quantity: "+quantity);
        }    
        if(quantity > moq) {
            throw new IllegalArgumentException("Quantity: "+quantity+" > Minimum Order Quantity: "+moq);
        }    
    }

    public int getAvailable(Productvariant productunit) {
//        Integer ival = this.getQuantityOnAllOrders(productunit);
        Integer ival = this.getQuantityOnOrder(productunit);
        int ordered = ival == null ? 0 : ival;
        return productunit.getQuantityInStock() - ordered;
    }
    
    public void validateQuantityForAdditon(Productvariant productunit, int quantity) {
        int available = this.getAvailable(productunit);
        if(quantity > available) {
            throw new IllegalArgumentException("Quantity: "+quantity+" > available quantity: "+available);
        }
    }
    
    public Orderproduct getOrderItem(Productvariant productunit) {
        
        Orderproduct output = null;
        
        List<Orderproduct> catOrderItems = order.getOrderproductList();
        
        for(Orderproduct oval:catOrderItems) {
            if(oval.getProductvariantid().equals(productunit)) {
                output = oval;
                break;
            }
        }
Log.getInstance().log(Level.FINER, "Product variant: {0}, Orderproduct: {1}", this.getClass(), productunit, output);
        return output;
    }
    
    private int addQuantity(Orderproduct orderItem, int quantity) {
        int totalQty = orderItem.getQuantity() + quantity;
        orderItem.setQuantity(totalQty);
        return totalQty;
    }

// This quantity is inaccurate as it will return multiple sums for the same product
// For example if an item has quantityInStock = 2
// User A may only add 2
// User B may also add 2
// The sum of quantities is 4, however only 2 may be ordered
// Once either User A or B makes an order, quantityInStock is reduced to 0
//    
//    public Integer getQuantityOnAllOrders(Productvariant unit) {
//        Map where = Collections.singletonMap(Orderproduct_.productvariantid.getName(), unit);
//        return this.getDao().getSum(Integer.class, Orderproduct.class, Orderproduct_.quantity.getName(), where);
//    }

    public Integer getQuantityOnOrder(Productvariant unit) {
        return this.getQuantityOnOrder(this.getOrder(), unit);
    }
    
    public Integer getQuantityOnOrder(Productorder productorder, Productvariant unit) {
        int output = 0;
        if(productorder != null) {
            List<Orderproduct> orderproducts = productorder.getOrderproductList();
            if(orderproducts != null) {
                for(Orderproduct orderproduct:orderproducts) {
                    Productvariant variant = orderproduct.getProductvariantid();
                    if(variant.equals(unit)) {
                        output = orderproduct.getQuantity();
                        break;
                    }
                }
            }
        }else{
            output = 0;
        }
        return output;
    }
    
    /**
     * 
     * If <tt>orderId</tt> is not null and no such order is found an exception is thrown
     * even if the second method parameter <tt>(i.e create)</tt> is <tt>true</tt>.
     * @param orderId The <tt>orderId</tt> to find. May be null.
     * @param create. If true and <tt>orderId</tt> was not specified a new order
     * will be created
     * @return
     * @throws IllegalArgumentException If <tt>orderId</tt> is not null and no such order is found
     */
    public Productorder getOrder(Integer orderId, boolean create) throws IllegalArgumentException {
        
        JpaContext jpaContext = this.getJpaContext();
        
        EnumReferences refs = jpaContext.getEnumReferences();
        
        EntityController<Productorder, Integer> ec = jpaContext.getEntityController(Productorder.class, Integer.class);
        
        String buyer_ = Productorder_.buyer.getName();
        String orderstatusId_ = Productorder_.orderstatusid.getName();
        String orderId_ = Productorder_.productorderid.getName();
        
        Orderstatus orderstatus = (Orderstatus)refs.getEntity(References.orderstatus.Pending);

        Productorder productorder;
        
        if(!this.isSyncWithDatabase()) {
          
            if(create) {
                productorder = new Productorder();
                productorder.setBuyer(this.user == null ? null : this.user.getDetails());
                productorder.setDatecreated(new Date());
                productorder.setOrderstatusid(orderstatus);
            }else{
                productorder = null;
            }
            
        }else{
            
            HashMap whereParams = new HashMap(2, 1.0f);

            if(orderId != null) {

                whereParams.put(orderId_, orderId);

            }else{

                whereParams.put(buyer_, this.user.getDetails());

                // We only deal with pending orders here. This is because, in this
                // case, the user does not need to see those already ordered anymore
                // Also, the user can only have one pending order at a time
                whereParams.put(orderstatusId_, orderstatus);
            }
            
            try(Select<Productorder> qb = jpaContext.getDaoForSelect(Productorder.class)) {
                
                productorder = qb.where(Productorder.class, whereParams).createQuery().getSingleResult();
                
            }catch(javax.persistence.NoResultException ignore) {
                
                productorder = null;
            }

Log.getInstance().log(Level.FINE, "Selected order: {0}", this.getClass(), productorder);            

            if(orderId != null) {
                
                if(productorder == null) {
                    throw new IllegalArgumentException("Productorder not found, ID: "+orderId);
                }
            }else {    

                if(productorder == null && create) {

                    productorder = new Productorder();
                    productorder.setBuyer(this.user.getDetails());
                    productorder.setDatecreated(new Date());
                    productorder.setOrderstatusid(orderstatus);

Log.getInstance().log(Level.FINER, "Inserting order: {0}", this.getClass(), productorder);            

                    try{
                        ec.persist(productorder); 
                    }catch(Exception e) {
                        throw new RuntimeException(e);
                    }
Log.getInstance().log(Level.FINE, "Newly inserted order: {0}", this.getClass(), productorder);            
                }             
            }
        }
        
Log.getInstance().log(Level.FINE, "Order id: {0}, order: {1}", this.getClass(), orderId, productorder);            
        
        return productorder;
    }
    
    public void syncOrderWith(ShoppingCart cart) throws PreexistingEntityException, Exception {
        
        ProductorderManager orderMgr = new ProductorderManager();
        
        orderMgr.sync(cart.getOrder(), this.getOrder(), this.isSyncWithDatabase());
        
        if(cart.isSyncWithDatabase()) {
//            cart.syncOrderWithDatabase();
            cart.refreshOrderFromDatabase();
        }
        
        if(this.isSyncWithDatabase()) {
//            this.syncOrderWithDatabase();
            this.refreshOrderFromDatabase();
        }
    }
    
    public boolean refreshOrderFromDatabase() 
            throws PreexistingEntityException, NonexistentEntityException, Exception {

Log.getInstance().log(Level.FINER, "Updating order form database, order: {0}", this.getClass(), order);

        if(this.getOrderId() == null) {
            return false;
        }

        JpaContext cf = this.getJpaContext();

        EntityManager em = cf.getEntityManager(Productorder.class);
        
        Productorder found = em.find(Productorder.class, this.getOrderId());
        
        if(found != null) {
            this.setOrder(found);
            return true;
        }else{
            return false;
        }
    }    
    
    public boolean syncOrderWithDatabase() throws PreexistingEntityException, Exception {
        if(this.isSyncWithDatabase()) {
            Productorder syncd = this.syncOrderWithDatabase(this.getOrder());
            this.setOrder(syncd);
            return true;
        }else{
            return false;
        }
    }
    
     /**
     * Some one may have bought some of the items in the cart. So this ensures
     * the items are available
     * @throws javax.servlet.ServletException
     */
    public synchronized void trimQuantities() throws ServletException {

        int sizeB4 = order.getOrderproductList() == null ? 0 : order.getOrderproductList().size();
        
        if(sizeB4 > 0) {
            
            int removedVariants = this.trimQuantities(order, syncWithDatabase);
            
            int itemsRemoved = sizeB4 - order.getOrderproductList().size();

            if(removedVariants > 0 || itemsRemoved > 0) {
    
                if(this.isSyncWithDatabase()) {
                    try{
                        this.refreshOrderFromDatabase();
Log.getInstance().log(Level.FINE, "AFTER REFRESH ORDER FROM DATABASE, itemcount: {0}, order: {1}", 
        this.getClass(), this.getItemCount(order), order);
                        
                    }catch(Exception e) {
                        Log.getInstance().log(Level.WARNING, "Error updating order with database instance, order: "+order, this.getClass(), e);
                    }
                }

                throw new ServletException("Some items in your shopping cart were recently purchased. Please review the available items before continuing.");            
            }
        }
    }
    
    public void validateQuantities() {
        this.validateQuantities(this.getOrder());
    }
    
    public void updateQuantityInStock(boolean increment) {
        this.updateQuantityInStock(this.getOrder(), increment);
    }
    
    /**
     * Get the order record for this Shopping Cart if necessary fetch it from 
     * the database
     * @return The order record for this Shopping Cart or null if none exists
     */
    public Productorder getOrder() {
        return this.order;
    }

    public void setOrder(Productorder order) {
        this.order = order;
    }
    
    private MathContext getDefaultMathContext() {
        return WebApp.getInstance().getDefaults().getDefaultMathContext();
    }
    
    public boolean isInitalPaymentSelected() {
        return initalPaymentSelected;
    }

    public void setInitalPaymentSelected(boolean initalPaymentSelected) {
        this.initalPaymentSelected = initalPaymentSelected;
    }

    public Userpaymentmethod getSelectedPaymentmethod() {
        return selectedPaymentoption;
    }

    public void setSelectedPaymentmethod(Userpaymentmethod selectedPaymentoption) {
        this.selectedPaymentoption = selectedPaymentoption;
    }

    public Map getOrderShippingDetails() {
        if(!this.isSyncWithDatabase()) {
            return null;
        }else{
            JpaContext cf = this.getJpaContext();
            EntityController<Shippingdetails, Integer> ec = cf.getEntityController(Shippingdetails.class, Integer.class);
            Shippingdetails shipping = ec.selectFirst(Shippingdetails_.productorderid.getName(), this.getOrderId());
            return shipping == null ? null : ec.toMap(shipping, false);
        }
    }

    public Shippingdetails getOrderShipping() {
        if(!this.isSyncWithDatabase()) {
            return null;
        }else{
            JpaContext cf = this.getJpaContext();
            EntityController<Shippingdetails, Integer> ec = cf.getEntityController(Shippingdetails.class, Integer.class);
            Shippingdetails shipping = ec.selectFirst(Shippingdetails_.productorderid.getName(), this.getOrderId());
            return shipping;
        }
    }
    
    public Payment getPayment() {
        if(!this.isSyncWithDatabase()) {
            return null;
        }else{
            JpaContext jpaContext = this.getJpaContext();
            try(Select<Payment> qb = jpaContext.getDaoForSelect(Payment.class)) {
                return qb.where(Payment.class, Payment_.productorderid.getName(), this.getOrder())
                .createQuery().getSingleResult();
            }catch(javax.persistence.NoResultException ignore) {
                return null;
            }
        }
    }
    
    @Override
    public JpaContext getJpaContext() {
        return WebApp.getInstance().getJpaContext();
    }
    
    public PaymentRequest getPaymentRequest() {
        return this.getOrderId() == null ? null : PaymentRequest.get(this.getOrderId());
    }

    public boolean isSyncWithDatabase() {
        return syncWithDatabase;
    }

    public void setSyncWithDatabase(boolean updateDatabase) {
        // If was 'false' and now 'true'. (i.e changed from 'false' to 'true')
        //
        if(!this.syncWithDatabase && updateDatabase) {
//            this.syncOrderWithDatabase();
            throw new UnsupportedOperationException("Not yet supported");
        }
        this.syncWithDatabase = updateDatabase;
    }

    public Userpaymentmethod getSelectedPaymentoption() {
        return selectedPaymentoption;
    }

    public void setSelectedPaymentoption(Userpaymentmethod selectedPaymentoption) {
        this.selectedPaymentoption = selectedPaymentoption;
    }

    public boolean isExpressDelivery() {
        return this.delivery.isExpress();
    }
    
    public void setExpressDelivery(boolean express) {
        this.delivery.setExpress(express);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString()); 
        try{
            builder.append(", orderId: ").append(this.getOrder()==null?null:this.getOrderId());
            builder.append(", items: ").append(this.getItemCount());
            builder. append(", total weight: ").append(this.getTotalWeight());
            builder.append(", user: ").append(this.getUser()==null?null:this.getUser().getEmailAddress());
            builder.append('\n');
            builder.append(", cost: ").append(this.getTotalAmount());
            if(this.initalPaymentSelected) {
                builder.append(", initial payment %: ").append(this.getInitialPaymentPercent());
                builder.append(", initial payment: ").append(this.getInitialPaymentAmount());
    //            builder.append(", selected cost: ").append(this.getSelectedAmount());
            }
            builder.append(", delivery cost: ").append(this.getDeliveryAmount());
            builder.append(", total cost: ").append(this.getPaymentAmount());
            if(Log.getInstance().isLoggable(Level.FINE, this.getClass())) {
                builder.append('\n');
                builder.append(", selected payment method: ").append(this.getSelectedPaymentmethod());
                builder.append('\n');
                builder.append(", shipping: ").append(this.getOrderShippingDetails());
                builder.append('\n');
                builder.append(", Payment request::\n").append(this.getPaymentRequest());
            }
        }catch(RuntimeException e) {
            System.err.println("Error in "+this.getClass().getName()+"#toString(). "+e);
            e.printStackTrace();
        }
        return builder.toString();
    }
}
