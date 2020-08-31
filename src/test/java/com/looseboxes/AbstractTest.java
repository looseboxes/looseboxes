package com.looseboxes;

import com.bc.jpa.controller.EntityController;
import com.bc.util.QueryParametersConverter;
import com.bc.util.Util;
import com.bc.webapptest.HttpServletRequestImpl;
import com.bc.webapptest.HttpSessionImpl;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Address_;
import com.looseboxes.pu.entities.Paymentmethod_;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.servlets.AddShippingToCart;
import com.looseboxes.web.servlets.Checkout;
import com.looseboxes.web.servlets.InsertShipping;
import com.looseboxes.web.servlets.Join;
import com.looseboxes.web.servlets.Login;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import com.looseboxes.web.components.forms.FormFieldOld;
import javax.servlet.http.HttpSession;
import com.bc.jpa.context.JpaContext;
import java.io.IOException;
import java.net.URISyntaxException;


/**
 * @(#)BaseTest.java   24-Jul-2015 20:22:33
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
public abstract class AbstractTest {
    
    static{
        try{
            LbApp.getInstance().init(JpaInfo.CONFIG_LOCATION);
        }catch(IOException | URISyntaxException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Map parameters;
    
    public AbstractTest() { }
    
    public String getActionName(
            Class<? extends HttpServlet> servletClass, Map params) {
        String output;
        String urlPattern = this.getUrlPattern(servletClass);
        if(params == null) {
            output = urlPattern;
        }else{
            QueryParametersConverter qpc = new QueryParametersConverter();
            output = urlPattern + '?' + qpc.convert(params);
        }
        return output;
    }
    
    public HttpServletRequest getRequest(
            final Class<? extends HttpServlet> servletClass, Map params) {

        HttpSessionImpl session = TestWebApp.getSession();
        
        HttpServletRequestImpl request = new HttpServletRequestImpl(
                (HttpSession)session, null, getParameters(servletClass, params), null){
            @Override
            public String getServletPath() {
                return getUrlPattern(servletClass);
            }
            @Override
            public String getRequestURI() {
                String urlPattern = getUrlPattern(servletClass);
                return this.getServletContext().getContextPath() + urlPattern;
            }
        };
        
        return request;
    }
    
    public <E> E getRandom(List<E> list) {

        // Select a random product variant
        int random = Util.randomInt(list.size());

        E selected = list.get(random); 
        
        return selected;
    }
    
    public List<Integer> getProductids(int size) {
        return this.getProductids(size, References.availability.InStock);
    }
    
    public List<Integer> getProductids(int size, References.availability availability) {
        JpaContext cf = LbApp.getInstance().getJpaContext();
        EntityController<Product, ?> ec = cf.getEntityController(Product.class);
        Map where = Collections.singletonMap(Product_.availabilityid.getName(), availability);
        where = cf.getDatabaseFormat().toDatabaseFormat(Product.class, where);
//        List<Integer> all = ec.selectColumn(Product_.productid.getName(), where, (Map)null, 0, size * 10);
        List<Integer> all = ec.selectColumn(Product_.productid.getName(), where, (Map)null, -1, -1);
        List<Integer> output;
        if(all.size() > size) {
            output = new ArrayList<>(size);
            while(output.size() < size) {
                int random = Util.randomInt(all.size());
                output.add(all.get(random));
            }
        }else{
            output = all;
        }
        return output;
    }

    public List<Product> getProducts(int size) {
        return this.getProducts(size, References.availability.InStock);
    }
    
    public List<Product> getProducts(int size, References.availability availability) {
        JpaContext cf = LbApp.getInstance().getJpaContext();
        EntityController<Product, ?> ec = cf.getEntityController(Product.class);
        Map where = Collections.singletonMap(Product_.availabilityid.getName(), availability);
        where = cf.getDatabaseFormat().toDatabaseFormat(Product.class, where);
//        List<Product> all = ec.select(where, null, 0, size * 10);
        List<Product> all = ec.select(where, null, -1, -1);
        List<Product> output;
        if(all.size() > size) {
            output = new ArrayList<>(size);
            while(output.size() < size) {
                int random = Util.randomInt(all.size());
                output.add(all.get(random));
            }
        }else{
            output = all;
        }
        return output;
    }
    
    public Map getParameters(
            Class<? extends HttpServlet> servletClass, Map input) {
        Map output = new HashMap();
        if(servletClass == Join.class || servletClass == Login.class) {
            output.put(Siteuser_.emailAddress.getName(), "posh.bc@gmail.com");
            output.put("password", "1kjvdul-");
            if(servletClass == Join.class) {
                output.put(FormFieldOld.WebOnlyField.confirmPassword.name(), "1kjvdul-");
            }
        }else if(servletClass == AddShippingToCart.class || servletClass == InsertShipping.class) {
            output.put(Address_.countryid.getName(), References.country.Nigeria); 
            output.put(Address_.city.getName(), "Abuja");
            output.put(Address_.county.getName(), "FCT");
            output.put(Address_.streetAddress.getName(), "123 Yawuri Street");
        }else if(servletClass == Checkout.class) {    
            output.put(Paymentmethod_.paymentmethodid.getName(), References.paymentmethod.Verve);
        }
        if(input != null) {
            // These come last as they override any of the above defaults
            output.putAll(input);
        }
        return output;
    }

    public String getUrlPattern(Class<? extends HttpServlet> servletClass) {
        WebServlet ws = servletClass.getAnnotation(WebServlet.class);
        return ws.urlPatterns()[0];
    }
    
    public void addParameter(Object key, Object value) {
        if(parameters == null) {
            parameters = new HashMap();
        }
        parameters.put(key, value);
    }

    public void addExclusiveParameter(Object key, Object value) {
        parameters = Collections.singletonMap(key, value);
    }
    
    public Object removeParameter(Object key) {
        Object output;
        if(parameters != null) {
            output = parameters.remove(key);
        }else{
            output = null;
        }
        return output;
    }
    
    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }
    
    public void log(Class aClass, String fmt, Object ...values) {
        log(aClass, MessageFormat.format(fmt, values));
    }
    
    public void log(Class aClass, Object msg) {
Logger.getLogger(aClass.getName()).log(Level.INFO, msg==null?"null":msg.toString());
    }
}
