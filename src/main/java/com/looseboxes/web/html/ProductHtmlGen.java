package com.looseboxes.web.html;

import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;


/**
 * @(#)ProductHtmlGen.java   27-Apr-2015 20:34:34
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
public class ProductHtmlGen extends EntityHtmlGen<Product> {
    
    public ProductHtmlGen() {  }
    
    @Override
    public Class<Product> getEntityClass() {
        return Product.class;
    }
    
    @Override
    public String getImagePath(Product product) {
        return this.getImagePath(this.getProductvariant(product));
    }

    @Override
    public String getImageAlt(Product entity) {
        return entity.getProductName();
    }
    
    @Override
    protected void appendSummary(Product entity, StringBuilder appendTo) {
        appendTo.append("ID: ").append(entity.getProductid()).append("&nbsp;");
        appendTo.append(entity.getProductName());
        appendTo.append("<br/>");
        appendTo.append(this.getPriceDisplay(entity.getPrice(), entity.getDiscount()));
    }
    
    private Productvariant getProductvariant(Product product) {
        return product.getProductvariantList().get(0);
    }
}
