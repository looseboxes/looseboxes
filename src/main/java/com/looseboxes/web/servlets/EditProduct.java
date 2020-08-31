package com.looseboxes.web.servlets;

/**
 * @(#)EditProduct.java   20-Jun-2015 16:00:09
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="EditProduct", urlPatterns={"/edit"})
public class EditProduct extends EditServlet<Product> {
    
    public EditProduct() {
        super(Product.class, Product_.productid.getName());
    }
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return "/products/productvariants.jsp";
    }
}
