package com.looseboxes.web.components;

import com.bc.jpa.controller.EntityController;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.web.WebApp;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import com.bc.jpa.context.JpaContext;


/**
 * @(#)GetProductvariants.java   01-Jul-2015 00:28:15
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
public class Productvariants implements Serializable {

    private Integer productid;
    
    private Product product;
    
    public Productvariants() { }
    
    public List<Productvariant> getProductvariants() {
        List<Productvariant> output;
        if(product == null) {
            output = Collections.EMPTY_LIST;
        }else{
            output = product.getProductvariantList();
        }
        return output;
    }
    
    public Product getProduct() {
        return this.product;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        if(productid != null) {
            if(!productid.equals(this.productid)) {
                JpaContext cf = WebApp.getInstance().getJpaContext();
                EntityController<Product, Integer> ec = cf.getEntityController(Product.class, Integer.class);
                this.product = ec.selectById(productid);
            }
        }else{
            this.product = null;
        }
        this.productid = productid;
    }
}
