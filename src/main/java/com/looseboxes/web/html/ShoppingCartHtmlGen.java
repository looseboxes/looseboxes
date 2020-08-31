package com.looseboxes.web.html;

import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Product;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @(#)ShoppingCartHtmlGen.java   22-Jul-2015 10:47:23
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
public class ShoppingCartHtmlGen extends OrderproductHtmlGen {
    
    private double totalSum;
    
    private int rowCount;
    
    public ShoppingCartHtmlGen() {
        this.setImageAndSummarySeparator(null);
        this.setSummaryAttributes(Collections.singletonMap("style", "float:right;"));
        Map attr = new HashMap(2, 1.0f);
        attr.put("cellspacing", "20");
        attr.put("style", "border:4px #EEDDEE solid;");
        this.setContainerAttributes(attr);
    }

    @Override
    public int getColumnCount() {
        return 1;
    }
    
    @Override
    public int getImageWidth() {
        return 75;
    }

    @Override
    public int getImageHeight() {
        return this.getImageWidth();
    }
    
    @Override
    public void appendItems(Collection<Orderproduct> entities, StringBuilder builder) {

        this.rowCount = 0;
        this.totalSum = 0;
        
        super.appendItems(entities, builder);
        
        String totalStr = this.getNumberFormat().format(totalSum);
        
        builder.append("<h2>Total: ").append(totalStr).append("&emsp;<small>(Less shipping)</small></h2>");
    }
    
    @Override
    public void append(
            String tagName, Map params, Orderproduct entity, StringBuilder appendTo) 
            throws MalformedURLException {
        
        if(rowCount == 0) {
            
            this.totalSum = 0;
            
            // Append Headings Row
            //
            this.tagStart(this.getRowTagName(), this.getRowAttributes(), appendTo);
            this.enclosingTag(tagName, "<b>Details</b>", appendTo);
            this.enclosingTag(tagName, "<b>Unit Price</b>", appendTo);
            this.enclosingTag(tagName, "<b>Qty</b>", appendTo);
            this.enclosingTag(tagName, "<b>Amount</b>", appendTo);
            appendTo.append('<').append('/').append(this.getRowTagName()).append('>');
        }
        
        Product product = entity.getProductvariantid().getProductid();
        
        // Append summary
        //
        super.append(tagName, params, entity, appendTo);
        
        // Append Unit Price
        //
        BigDecimal price = product.getPrice();
        BigDecimal discount = product.getDiscount();
        String priceDisplay = this.getPriceDisplay(price, discount, false);
        this.enclosingTag(tagName, priceDisplay, appendTo);
        
        // Append Qty
        //
        this.enclosingTag(tagName, entity.getQuantity(), appendTo);
        
        // Append Amount
        //
        double amount = (price.doubleValue() - discount.doubleValue()) * entity.getQuantity();
        
        totalSum += amount;
        
        NumberFormat format = this.getNumberFormat();
        
        this.enclosingTag(tagName, format.format(amount), appendTo);
        
        ++rowCount;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public int getRowCount() {
        return rowCount;
    }
}
