package com.looseboxes.web.html;

import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;
import java.net.MalformedURLException;


/**
 * @(#)ProductvariantHtmlGen.java   04-Jun-2015 21:11:09
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
public class ProductvariantHtmlGen extends EntityHtmlGen<Productvariant> {

    public ProductvariantHtmlGen() {  }
    
    @Override
    public Class<Productvariant> getEntityClass() {
        return Productvariant.class;
    }
    
    @Override
    public String getURL(Productvariant entity) throws MalformedURLException {
        return this.getURL(entity.getProductid());
    }    

    @Override
    public String getImageAlt(Productvariant entity) {
        return entity.getProductid().getProductName();
    }
    
    @Override
    protected void appendSummary(Productvariant entity, StringBuilder appendTo) {
        Product product = entity.getProductid();
        appendTo.append("ID: ").append(product.getProductid()).append("&nbsp;");
        appendTo.append("SKU: ").append(entity.getProductvariantid());
        appendTo.append("<br/>");
        appendTo.append(product.getProductName());
        appendTo.append("<br/>");
        appendTo.append(this.getPriceDisplay(product.getPrice(), product.getDiscount()));
    }
}
