package com.looseboxes.web.payment;

import com.bc.util.CurrencyFormatter;
import com.bc.util.Log;
import com.looseboxes.core.LbCurrencyFormatter;
import com.looseboxes.pu.References;
import com.looseboxes.web.ServletMailer;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.ShoppingCart;
import com.looseboxes.web.components.UserBean;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @(#)VoguePay.java   10-Jun-2013 18:39:16
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
/**
 * 
See https://voguepay.com/developers
<pre>
<form method='POST' action='https://voguepay.com/pay/'>

<input type='hidden' name='v_merchant_id' value='qa331322179752' />
<input type='hidden' name='merchant_ref' value='234-567-890' />
<input type='hidden' name='memo' value='Bulk order from McAckney Web Shop' />

<input type='hidden' name='notify_url' value='http://www.mydomain.com/notification.php' />
<input type='hidden' name='success_url' value='http://www.mydomain.com/thank_you.html' />
<input type='hidden' name='fail_url' value='http://www.mydomain.com/failed.html' />

<input type='hidden' name='total' value='13000' />

<input type='image' src='http://voguepay.com/images/buttons/buynow_blue.png' alt='Submit' />

</form>  
</pre>
 * 
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class VoguePay extends PaymentRequest {

    private String paymentCode;
    
    private References.paymentstatus paymentStatus;
    
    private Map<String, String> parameters;
    
    public VoguePay() { }
    
    
    @Override
    public String getForwardPage(){
        return WebPages.CART_REDIRECTTOGATEWAY;
    }
    
    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }
    
    @Override
    public String getEndpoint() {
        return "https://voguepay.com/pay/";
    }
    
    @Override
    public Map<String, String> getParameters() {
        return this.parameters;
    }
    
    @Override
    public void doProcessRequest(
            HttpServletRequest request, HttpServletResponse response) throws ServletException {
       
        this.paymentStatus = References.paymentstatus.Pending;
        
        ShoppingCart cart = getUser().getShoppingCart();
        
        BigDecimal amountB4 = cart.getPaymentAmount(); 
        
        // Convert the currency to this merchant's currency
//@todo what does this mean         

        Locale fromLocale = getUser().getLocale();
        Locale toLocale = this.getLocale();
        
        CurrencyFormatter currFmt = new LbCurrencyFormatter();
        
        BigDecimal amount = (BigDecimal)currFmt.convertNumber(amountB4, fromLocale, toLocale);
        
Log.getInstance().log(Level.INFO, "Payment amount converted from {0} to {1} i.e from {2} to {3}", 
this.getClass(), amountB4, amount, fromLocale.getDisplayName(), toLocale.getDisplayName());
        
        final Integer orderId = cart.getOrderId();
        
        this.paymentCode = this.newPaymentCode();

        // We don't encode these parameters because they will be submitted via
        // a form on the /cart/redirectToGateway.jsp page. Otherwise encode
        // the parameters
        //
        add("v_merchant_id", "13160-9702", false);
        
        try{
            add("merchant_ref", this.encryptRef(orderId.toString()), false);
        }catch(GeneralSecurityException e) {
            add("merchant_ref", orderId.toString(), false);
        }
        
        add("memo", this.getMemo(getUser(), orderId).toString(), false); // not encoded
        try{
            add("notify_url", this.getNotificationURL(orderId, paymentCode).toString(), false);
            add("success_url", this.getSuccessURL(orderId, paymentCode).toString(), false);
            add("fail_url", this.getFailureURL(orderId, paymentCode).toString(), false);
        }catch(MalformedURLException e) {
            new ServletMailer().sendError(e, "Error processing payment", Level.ALL);
            throw new ServletException("An unexpected error occured while processing your request. Please try again later", e);
        }
        add("total", amount.toPlainString(), false);
Log.getInstance().log(Level.FINE, "Parameters: {0}", this.getClass(), this.parameters);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        
        paymentStatus = References.paymentstatus.ProcessingPayment;
        
        PaymentRequest.add(getUser().getShoppingCart().getOrderId(), this);
        
        super.doPost(request, response);
    }
        
    
    
    @Override
    public void validate(HttpServletRequest request) {
        
/////// VALIDATING MEANS WE TRUST THE VOGUE PAY RESPONSE TO BE CONSISTENT ///////
        
        Map<String, String> params = ServletUtil.getParameterMap(request);

        String remoteRef = params.get("merchant_ref");
        
        if(remoteRef == null || parameters == null) {
//            throw new ValidationException("Received an invalid response from the Payment Gateway. Please try paying again later");
            return;
        }
        
        String localRef = parameters.get("merchant_ref");
        
Log.getInstance().log(Level.INFO, "Remote ref: {0}, Local ref: {1}", 
        this.getClass(), remoteRef, localRef);        
        
        if(!remoteRef.equals(localRef)) {
//            throw new ValidationException("Received an invalid response from the Payment Gateway. Please try paying again later");
        }
    }
    
    private String encryptRef(String ref) throws GeneralSecurityException {
        return WebApp.getInstance().getEncryption().encrypt(ref.toCharArray());        
    }
    private String decryptRef(String ref) throws GeneralSecurityException {
        return new String(WebApp.getInstance().getEncryption().decrypt(ref));        
    }
    
    private StringBuilder getMemo(UserBean user, Object orderId) {
        String username = user.getName() == null ? user.getEmailAddress() : user.getName();
        StringBuilder memo = new StringBuilder("Payment for Order: ").append(
        orderId).append(" by ").append(username).append(
                " on ").append(WebApp.getInstance().getName()).append(".com");
        return memo;
    }
    
    private void add(String key, String value, boolean encode) {
        if(this.parameters == null) {
            this.parameters = new HashMap<>();
        }
        if(encode) {
            try{
                this.parameters.put(key, URLEncoder.encode(value, "UTF-8"));
            }catch(Exception e) { // No exception should stop this
                Log.getInstance().log(Level.WARNING, "", this.getClass(), e);
                this.parameters.put(key, value.replace(" ", "%20"));
            }
        }else{
            this.parameters.put(key, value);
        }
    }
    
    @Override
    public References.paymentstatus getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String getPaymentCode() {
        return paymentCode;
    }
}
