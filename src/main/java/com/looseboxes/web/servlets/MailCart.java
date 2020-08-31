package com.looseboxes.web.servlets;

/**
 * @(#)MailCart.java   18-Jul-2015 22:01:06
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.util.Log;
import com.bc.html.HtmlGen;
import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.web.html.EntityHtmlGen;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.html.ShoppingCartHtmlGen;
import com.looseboxes.web.mail.DefaultHtmlEmail;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="MailCart", urlPatterns={"/mailCart"})
public class MailCart extends BaseServlet {

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String recipient = (String)ServletUtil.find("recipient", request);
        
        if(recipient == null || recipient.isEmpty()) {
            throw new ServletException("Please specify a recipient");
        }
        
        Integer orderId = this.getOrderId(request);
        
        Productorder order = this.getOrder(request, orderId);
        
        if(order == null) {
            throw new ServletException("Order does not exist for order ID: "+orderId);
        }
        
        this.mailOrder(order, new String[]{recipient});
    }

    public void mailOrder(Productorder order, String [] recipients) 
            throws ServletException, IOException {
        
        if(order == null) {
            throw new NullPointerException();
        }
        
        if(recipients == null) {
            throw new NullPointerException();
        }
        
        if(recipients.length == 0) {
            throw new IllegalArgumentException("Recipients not specified for email");
        }
        
        List<Orderproduct> orderproducts = order.getOrderproductList();
        
        if(orderproducts == null || orderproducts.isEmpty()) {
            throw new ServletException("The selected order is empty. Order ID: "+order.getProductorderid());
        }
        
        StringBuilder messageBody = new StringBuilder();
        messageBody.append("<p>Hi,</p>");
        messageBody.append("<p>You have been sent details of an Order placed on ");
        HtmlGen.AHREF(WebApp.getInstance().getBaseURL(), "BuzzWears.com", messageBody);
        Siteuser buyer = order.getBuyer();
        String buyerId = buyer.getUsername() == null ? buyer.getEmailAddress() :buyer.getUsername();
        messageBody.append(" by ").append(buyerId);
        messageBody.append("</p>");
        messageBody.append("<p>Order ID: ").append(order.getProductorderid()).append("</p>");
        EntityHtmlGen<Orderproduct> htmlGen = new ShoppingCartHtmlGen();
        htmlGen.setExternalOutput(true);
        htmlGen.setMobile(true);
        htmlGen.appendItems(orderproducts, messageBody);
        
Log.getInstance().log(Level.FINER, "Shopping Cart Message Body:\n{0}", this.getClass(), messageBody);
        
        try{
            
            HtmlEmail message = new DefaultHtmlEmail();

            message.setSubject("BuzzWears Order Notice");
            message.setHtmlMsg(messageBody.toString());

            message.addTo(recipients);
            
        }catch(EmailException e) {
            throw new ServletException("Error sending email", e);
        }
    }
    
    public Integer getOrderId(HttpServletRequest request) {
        Object oval = ServletUtil.find(Productorder_.productorderid.getName(), request);
        String sval;
        Integer orderId;
        if(oval == null || (sval = oval.toString()).isEmpty()) {
            UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
            orderId = user.getShoppingCart().getOrderId();
        }else{
            orderId = Integer.parseInt(sval);
        }
        return orderId;
    }
    
    public Productorder getOrder(HttpServletRequest request, Integer orderId) {
        
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        Productorder output;
        
        if(orderId == null || orderId.equals(user.getShoppingCart().getOrderId())) {
            output = user.getShoppingCart().getOrder();
        }else{
            JpaContext cf = WebApp.getInstance().getJpaContext();
            try(Select<Productorder> qb = cf.getDaoForSelect(Productorder.class)) {
                output = qb.where(Productorder.class, Productorder_.productorderid.getName(), orderId)
                .createQuery().getSingleResult();
            }catch(javax.persistence.NoResultException e) {
                output = null;
            }
        }
        
        return output;
    }
}
