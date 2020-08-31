package com.looseboxes.web.payment;

import java.util.concurrent.ScheduledExecutorService;
import com.bc.util.Log;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.bc.util.QueryParametersConverter;
import com.bc.util.Util;
import com.bc.util.concurrent.NamedThreadFactory;
import com.looseboxes.core.LbApp;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.HasMessages;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Payment;
import com.looseboxes.pu.entities.Payment_;
import com.looseboxes.pu.entities.Paymentmethod;
import com.looseboxes.pu.entities.Paymentmethod_;
import com.looseboxes.pu.entities.Paymentstatus;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Shippingdetails;
import com.looseboxes.pu.entities.Shippingstatus;
import com.looseboxes.pu.entities.Userpaymentmethod;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.ShoppingCart;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.mail.DefaultHtmlEmail;
import com.looseboxes.web.servlets.PaymentService;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import org.apache.commons.mail.HtmlEmail;
import com.bc.jpa.context.JpaContext;
import com.bc.security.SecurityTool;

/**
 * @(#)PaymentRequest.java   10-Jun-2013 19:41:57
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
/**
 * This class sends the payment request to the Payment Gateway (e.g Mastercard)
 * {@link com.loosebox.web.servlets.util.PaymentService} processes the response
 * from the Payment Gateway
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class PaymentRequest implements Serializable, HasMessages {

    private boolean useHtmlPaymentPage;
    
    private final long timeCreated;
    
    private Map<MessageType, StringBuilder> messages;
    
    private UserBean user;

    private static final Map<Integer, PaymentRequest> paymentRequestCache = new HashMap<>();
    
    public PaymentRequest() { 
        timeCreated = System.currentTimeMillis();
    }
    
    public abstract References.paymentmethod getType();
    
    /**
     * Currencies are converted to this locale before processing
     * @return A preferred locale for this PaymentRequest or null if none is specified
     */
    public abstract Locale getLocale();
    
    public abstract String getPaymentCode();
    
    public abstract References.paymentstatus getPaymentStatus();
    
    public abstract Map<String, String> getParameters();
    
    public abstract String getForwardPage();
    
    public abstract String getEndpoint();
    
    public abstract void validate(HttpServletRequest request) throws ServletException;
    
    protected abstract void doProcessRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException;
    
    public boolean isElectronic() {
        return isElectronic(getType());
    }
    
    public void processRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
        
// Order of method call important
//
        UserBean u = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        this.setUser(u);

        this.doProcessRequest(request, response);
        
//@related Quantity in stock                            
        this.insertPaymentAndDecrementQuantityInStock(getUser().getShoppingCart(), request);
        
        this.doPost(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        
// Order of method call important
//
        this.insertShipping(getUser(), request);
        
        this.notifyAdmin(request);
    }
    
    private void notifyAdmin(HttpServletRequest request) {
        
        try{
            StringBuilder msg = new StringBuilder();

            msg.append("Order Id: ");

            msg.append(getUser().getShoppingCart().getOrderId());

            msg.append("<br/><br/>User Details:<br/>");

            msg.append(user.getDetails());

            msg.append("<br/><br/>HttpRequest Details:<br/>");

            msg.append(ServletUtil.getDetails(request, "<br/>", Level.ALL));
            
            HtmlEmail htmlEmail = new DefaultHtmlEmail();
            
            htmlEmail.setSubject("PAYMENT INITIATED BUY USER ON LOOSEBOXES");
            htmlEmail.setHtmlMsg(msg.toString());
            
            htmlEmail.addTo(
                    WebApp.getInstance().getConfig().getString(AppProperties.address_default_notice));
            
            htmlEmail.send();
            
        }catch(Exception e) {
            Log.getInstance().log(Level.WARNING, "Failed to notify admin of user initiated payment", this.getClass(), e);
        }
    }
    
    public static PaymentRequest getInstance(Integer orderId, Userpaymentmethod selectedPaymentoption) {
        
        Paymentmethod paymentmethod = selectedPaymentoption.getPaymentmethodid();
        
        return getInstance(orderId, paymentmethod);
    }

    public static PaymentRequest getInstance(Integer orderId, Paymentmethod paymentmethod) {
        
        final Integer ival = paymentmethod.getPaymentmethodid().intValue();
        
        return getInstance(orderId, ival);
    }
    
    public static PaymentRequest getInstance(Integer orderId, Integer paymentType) {
        EnumReferences refs = WebApp.getInstance().getJpaContext().getEnumReferences();
        References.paymentmethod paymentmethod = (References.paymentmethod)refs.getEnum(Paymentmethod_.paymentmethodid.getName(), paymentType.intValue());
        return getInstance(orderId, paymentmethod);
    }
        
    public static PaymentRequest getInstance(Integer orderId, References.paymentmethod paymentType) {

        if(orderId == null) {
            throw new NullPointerException();
        }
        
        // Remove any previous payment request for the specified order
        // This is important as it involves incrementing the quantityInStock
        //
        PaymentRequest paymentRequest = get(orderId);
        
        if(paymentRequest != null) {
//@related Quantity in stock                            
            // Increment quantity instock
            //
            paymentRequest.getUser().getShoppingCart().updateQuantityInStock(true);

Log.getInstance().log(Level.FINER, "Removing: {0}", PaymentRequest.class, paymentRequest);

            remove(orderId);
        }
        
        PaymentRequest pmtRequest;
        switch(paymentType) {
            case BankDeposit: 
                pmtRequest = new BankPay(); break;
            case BookonHold:
                pmtRequest = new PayOnDelivery(); break;
            case CashonDelivery:
                pmtRequest = new CashPay(); break;
            case MasterCard:
                pmtRequest = new Mastercard(); break;
            case VisaCard:
                pmtRequest = new Visacard(); break;
            case Verve:
                pmtRequest = new Verve(); break;
            default:
                throw new UnsupportedOperationException();
        }
        return pmtRequest;
    }

    public static boolean isElectronic(References.paymentmethod paymentType) {
        boolean output;
        switch(paymentType) {
            case BankDeposit: 
            case BookonHold:
            case CashonDelivery:
                output = false; break;
            default:
                output = true;
        }
        return output;
    }
    
    public static void add(Integer orderId, PaymentRequest request) {
        paymentRequestCache.put(orderId, request);
        schedule();
    }
    
    public static PaymentRequest get(Integer orderId) {
        return paymentRequestCache.get(orderId);
    }
    
    public static PaymentRequest remove(Integer orderId) {
        return paymentRequestCache.remove(orderId);
    }
    
    public Currency getCurrency() {
        if(getLocale() == null) return null;
        return Currency.getInstance(this.getLocale());
    }
    
    public com.looseboxes.pu.entities.Currency getCurrencyEntity() {
        if(getLocale() == null) return null;
        return ServletUtil.getCurrencyEntityForLocale(this.getLocale());
    }
    
    protected String newPaymentCode() {
        return new SecurityTool().getRandomUUID(8);
    }
    
    public String getResponseServletPath() {
        StringBuilder builder = new StringBuilder();
        ServletUtil.appendContextQuery(PaymentService.class, null, builder, true);
        return builder.toString();
    }
    
    public URL getSuccessURL(Integer orderId, Object paymentCode) throws MalformedURLException {
        return this.getURL(orderId, paymentCode, "success");
    }
    
    public URL getFailureURL(Integer orderId, Object paymentCode) throws MalformedURLException {
        return this.getURL(orderId, paymentCode, "failure");
    }
    
    public URL getNotificationURL(Integer orderId, Object paymentCode) throws MalformedURLException {
        return this.getURL(orderId, paymentCode, "notification");
    }

    private URL getURL(Integer orderId, Object paymentCode, String key) throws MalformedURLException {
        StringBuilder buff = new StringBuilder();
        if(this.isUseHtmlPaymentPage()) {
            // E.g  http://www.looseboxes.com/responseservlet/success.html
            buff.append(this.getResponseServletPath()).append('/').append(key).append(".html");
        }else{
            // E.g  http://www.looseboxes.com/responseservlet?event=success
            buff.append(this.getResponseServletPath()).append('?');
            Map params = this.getURLParams(orderId, paymentCode, "event", key);
            String queryString = this.getQueryString(params);
            buff.append(queryString);
        }    
        URL url = WebApp.getInstance().getURL(buff.toString());
Log.getInstance().log(Level.FINER, "Resource: {0}, URL: {1}", this.getClass(), buff, url);
        return url;
    }
    
    private Map getURLParams(Object orderId, Object paymentCode, String key, Object value) {
        HashMap m = new HashMap(4, 1.0f);
        m.put(Productorder_.productorderid.getName(), orderId);
        m.put(Payment_.paymentCode.getName(), paymentCode);
        m.put(key, value);
        return m;
    }

    protected Map<String, ?> getConnProperties(String charset) {
        HashMap<String, String> props = new HashMap<>();
        props.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000");
        if(charset == null) {
            props.put("Content-Type", "application/x-www-form-urlencoded");
        }else{
            props.put("Content-Type", "application/x-www-form-urlencoded;charset="+charset);
        }
        if(charset != null) {
            props.put("Accept-Charset", charset);
        }
        return props;
    }
    
    protected String getQueryString(Map map) {
        final QueryParametersConverter qpc = new QueryParametersConverter();
        final String output = qpc.convert(map, false);
Log.getInstance().log(Level.FINE, "Query: {0}", this.getClass(), output);        
        return output;
    }
    
    protected boolean insertPaymentAndDecrementQuantityInStock(ShoppingCart cart, HttpServletRequest request) {

        cart.validateQuantities();

        Userpaymentmethod upm = cart.getSelectedPaymentmethod();
        
        if(upm == null) {
            throw new NullPointerException();
        }
        
        References.paymentstatus statusEnum = this.getPaymentStatus();
        
        if(upm == null || statusEnum == null || upm == null) {
            throw new NullPointerException();
        }
        
        // There can be only one payment per Order, so we check if one already exists
        //
        Payment payment = cart.getPayment();

        boolean create = false;
        
        if(payment == null) {
            payment = new Payment();
            payment.setDatecreated(new Date());
            payment.setProductorderid(cart.getOrder());
            create = true;
        }
        
        JpaContext factory = LbApp.getInstance().getJpaContext();
        EnumReferences refs = factory.getEnumReferences();
        Paymentstatus statusEntity = (Paymentstatus)refs.getEntity(statusEnum);
        
        try{

//            payment.setDatecreated(new Date()); // Already done above
            payment.setPaymentAmount(cart.getPaymentAmount());
            payment.setPaymentCode(this.getPaymentCode());
            payment.setPaymentstatusid(statusEntity);
//            payment.setProductorderid(order); Already done above
            payment.setUserpaymentmethodid(upm);
            
            payment.setCurrencyid(com.looseboxes.core.Util.getCurrencyEntityForLocale(this.getLocale()));

            JpaContext cf = WebApp.getInstance().getJpaContext();
            
            EntityManager em = cf.getEntityManager(Payment.class); 
            
            try{
                
                EntityTransaction t = em.getTransaction();
                
                try{
                    
                    t.begin();
                    
                    if(create) {
                        em.persist(payment);
                    }else{
                        em.merge(payment);
                    }
                    
                    cart.updateQuantityInStock(em, cart.getOrder(), false);
                    
                    t.commit();
                    
                }finally{
                    if(t.isActive()) {
                        t.rollback();
                    }
                }
            }finally{
                em.close();
            }
Log.getInstance().log(Level.FINE, "Successfully created payment: {0}", this.getClass(), payment);        

            return true;

        }catch(Exception e) {
            
            Log.getInstance().log(Level.WARNING, "Failed to create payment for order: "+cart.getOrder(), this.getClass(), e);
            
            return false;
        }
    }

    protected boolean insertShipping(UserBean user, HttpServletRequest request) {
        
        ShoppingCart cart = user.getShoppingCart();

        // There can be only one shipping details per order, so we check if one already exists
        //
        Shippingdetails shipping = cart.getOrderShipping();

        JpaContext cf = WebApp.getInstance().getJpaContext();
        Shippingstatus statusEntity = (Shippingstatus)cf.getEnumReferences().getEntity(References.shippingstatus.Pending);
        
        EntityController<Shippingdetails, Integer> sc = WebApp.getInstance().getJpaContext().getEntityController(Shippingdetails.class, Integer.class);

        boolean created = false;
        try{
            if(shipping != null) {
                shipping.setShippingstatusid(statusEntity);
                sc.merge(shipping);
Log.getInstance().log(Level.FINE, "Successfully edited {0} record", this.getClass(), shipping.getClass().getName());        
            }else{
                shipping = new Shippingdetails();
                shipping.setDatecreated(new Date());
                shipping.setProductorderid(cart.getOrder());
                shipping.setShippingstatusid(statusEntity);
                sc.persist(shipping);
Log.getInstance().log(Level.FINE, "Successfully created {0} record", this.getClass(), shipping.getClass().getName());        
                created = true;
            }
        }catch(Exception e) {
            Log.getInstance().log(Level.WARNING, "Successfully created record of entity type: "+shipping.getClass().getName(), this.getClass(), e);
        }
        return created;
    }
    
    private void handleException(String ref, Map m, SQLException e, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder(WebApp.getInstance().getName());
        builder.append(" WARNING. Failed to insert ");
        builder.append(ref).append(" for ").append(this.getClass().getName());
        builder.append("\n  Values: ").append(m);
// @todo        
//        new ServletMailer().sendError(e, builder, request, Level.ALL);
    }
    
    private static boolean scheduled;
    
    private static void schedule() {

        if(scheduled) {
            return;
        }
        
        scheduled = true;
        
        final PaymentRequestReaperTask reaperTask = new PaymentRequestReaperTask();
        
        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor(
                new NamedThreadFactory("PaymentRequestReaper_ThreadPool"));
        
        ses.scheduleWithFixedDelay(reaperTask, getInterval(), getInterval(), TimeUnit.MILLISECONDS);
        
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {

                try{
                    
                    Util.shutdownAndAwaitTermination(ses, 1, TimeUnit.SECONDS);
                    
                }finally{
                    
                    reaperTask.reapAll = true;

System.out.println(this.getClass().getName()+"= = = = = = = REAPING ALL OUTSTANDING PAYMENT REQUESTS ON SHUTDOWN = = = = = = = ");

                    reaperTask.run();
                }
            }
        });
    }
    
    private static class PaymentRequestReaperTask implements Runnable {
        private boolean reapAll;
        @Override
        public void run() {
            try{
                synchronized(PaymentRequest.paymentRequestCache) {

                    Iterator<Integer> iter = paymentRequestCache.keySet().iterator();

                    while(iter.hasNext()) {

                        Integer productorderid = iter.next();

                        PaymentRequest paymentRequest = paymentRequestCache.get(productorderid);

                        if(reapAll || paymentRequest.isExpired()) {

    Log.getInstance().log(Level.FINER, "Removing expired {0}: {1}", 
    this.getClass(), PaymentRequest.class.getSimpleName(), paymentRequest);

    //@related Quantity in stock                            
                            // Increment quantity instock
                            //
                            paymentRequest.getUser().getShoppingCart().updateQuantityInStock(true);

                            iter.remove();
                        }
                    }
                }
            }catch(RuntimeException e) {
                Log.getInstance().log(Level.WARNING, "Thread: "+Thread.currentThread().getName(), this.getClass(), e);
            }
        }
    }
    
    public boolean isExpired() {
        long expiryDate = PaymentRequest.getExpiryDate(this);
        return System.currentTimeMillis() >= expiryDate;
    }
    
    public static long getExpiryDate(PaymentRequest pmtRequest) {
        return pmtRequest.getTimeCreated() + getPaymentLifespan();
    }
    
    public static long getInterval() {
        return getPaymentLifespan();
    }
    
    public static long getPaymentLifespan() {
        long lifespanMinutes = WebApp.getInstance().getConfig().getLong(AppProperties.PAYMENT_LIFESPAN, 30);
        return TimeUnit.MINUTES.toMillis(lifespanMinutes);
    }
    
    // HasMessages interface methods

    @Override
    public void addMessage(MessageType messageType, Object message) {
        if(message == null) {
            return;
        }
        if(messages == null) {
            messages = new EnumMap<>(MessageType.class);
        }
        StringBuilder messageBuffer = messages.get(messageType);
        if(messageBuffer == null) {
            messageBuffer = new StringBuilder();
            messages.put(messageType, messageBuffer);
        }
        messageBuffer.append(message).append("<br/>");
    }
    
    @Override
    public void addMessages(Map<MessageType, ?> messages) {
        if(messages == null || messages.isEmpty()) {
            return;
        }
        for(MessageType messageType:messages.keySet()) {
            this.addMessage(messageType, messages.get(messageType));
        }
    }
    
    @Override
    public boolean hasMessages() {
        return messages != null && !messages.isEmpty();
    }
    
    @Override
    public Map<MessageType, StringBuilder> getMessages() {
        return messages;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public boolean isUseHtmlPaymentPage() {
        return useHtmlPaymentPage;
    }

    public void setUseHtmlPaymentPage(boolean useHtmlPaymentPage) {
        this.useHtmlPaymentPage = useHtmlPaymentPage;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getName());
        try{
            builder.append(", type: ").append(this.getType());
            builder.append(", time create: ").append(this.getTimeCreated());
            builder.append(", status: ").append(this.getPaymentStatus());
            builder.append('\n');
            builder.append(", user: ").append(this.getUser());
            builder.append(", currency: ").append(this.getCurrency());
            builder.append(", locale: ").append(this.getLocale());
            builder.append('\n');
            builder.append(", forward page").append(this.getForwardPage());
            builder.append(", endpoint: ").append(this.getEndpoint());
            builder.append(", response path:").append(this.getResponseServletPath());
            builder.append('\n');
            builder.append(", messages: ").append(this.getMessages());
        }catch(Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
