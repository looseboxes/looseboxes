package com.looseboxes.web.servlets;

/**
 * @(#)Displayproduct.java   18-Apr-2015 04:23:43
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.ProductBean;
import com.looseboxes.web.components.UserBean;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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
@WebServlet(name="DisplayProduct", urlPatterns={"/displayproduct"})
public class Displayproduct extends BaseServlet {
   
    public synchronized boolean isIncrementCount() {
        return true;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.PRODUCTS;
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        ProductBean productBean = user.getSelectedItem();
        
        if(productBean == null) {
            productBean = new ProductBean(user, null);
            user.setSelectedItem(productBean);
        }
        
        Map<String, String> params = ServletUtil.getParameterMap(request);
        
        Product product = productBean.selectProduct(params);
        
        if(product == null) {
            
            throw new FileNotFoundException(Product.class.getSimpleName()+" with details: "+params);
        
        }else{
            
            Productvariant newSelection = productBean.selectVariant(product, params);

            if(newSelection == null) {
                
                // Item no longer available, select the first variant in the list to display
                //
                List<Productvariant> variants = product.getProductvariantList();
                
                if(variants == null || variants.isEmpty()) {
                    
                    throw new FileNotFoundException(Product.class.getSimpleName()+" with details: "+params);
                }
                
                this.addMessage(MessageType.warningMessage, "The selected item is no longer available");
                
                newSelection = variants.get(0);
                
                productBean.setSelectedVariant(newSelection);
            }
        }
        
        if(this.isIncrementCount()) {
            this.incrementViewCount(productBean.getDetails());
        }
    } 
    
    private synchronized void incrementViewCount(Product product) {
        try{
            
            EntityController<Product, Integer> ec = 
                    WebApp.getInstance().getJpaContext().getEntityController(Product.class, Integer.class);
            
            int views = product.getViews();
            product.setViews(++views);
            
            ec.merge(product);
            
        }catch(Exception e) {
            Log.getInstance().log(Level.WARNING, "Failed to update view count for entity: "+product, this.getClass(), e);
        }
    }
}
