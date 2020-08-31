package com.looseboxes.web.html;

import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;
import java.net.MalformedURLException;


/**
 * @(#)OrderproductHtmlGen.java   19-Jul-2015 00:26:03
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
public class OrderproductHtmlGen extends EntityHtmlGen<Orderproduct> {

    public OrderproductHtmlGen() {  }
    
    @Override
    public Class<Orderproduct> getEntityClass() {
        return Orderproduct.class;
    }
    
    @Override
    public String getImagePath(Orderproduct entity) {
        return this.getImagePath(entity.getProductvariantid());
    }

    @Override
    public String getURL(Orderproduct entity) throws MalformedURLException {
        return this.getURL(entity.getProductvariantid().getProductid());
    }    

    @Override
    public String getImageAlt(Orderproduct entity) {
        return entity.getProductvariantid().getProductid().getProductName();
    }
    
    @Override
    protected void appendSummary(Orderproduct entity, StringBuilder appendTo) {
        Productvariant variant = entity.getProductvariantid();
        Product product = variant.getProductid();
        appendTo.append("ID: ").append(product.getProductid()).append("&nbsp;");
        appendTo.append("SKU: ").append(variant.getProductvariantid());
        appendTo.append("<br/>");
        appendTo.append(product.getProductName());
        appendTo.append("<br/>");
        appendTo.append(this.getPriceDisplay(product.getPrice(), product.getDiscount()));
    }
}
