package aaa.util;

import java.text.NumberFormat;


/**
 * @(#)SendShoppingCartReminder.java   14-Jul-2015 14:42:29
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
public class SendShoppingCartReminder extends AbstractSendMail {
    
    private final Orderdetails [] orderdetails;
    
    private final NumberFormat fmt;
    
    private boolean format;
    
    public SendShoppingCartReminder(Orderdetails... details) {
        this.orderdetails = details;
        fmt = NumberFormat.getCurrencyInstance();
        fmt.setMaximumFractionDigits(2);
    }
    
    @Override
    public String getSubject(String email, String password) {
        return "BuzzWears careline - Help us assist you";
    }
    
    @Override
    public String getMessage(String email, String password) {
        // Salutation (hi, etc) is automatically added
        //
        String msg = 
        "<p>You recently added items to your shopping cart on <a href=\"http://www.buzzwears.com\">BuzzWears.com</a>.</p>\n" +
        "<p>If you encounter any problem shopping on <i>BuzzWears.com</i>, please let us know.</p>\n" +
        "<p style=\"background-color:#EEDDEE; width:100%;\">\n" +
        "  Email: <tt><a href=\"mailto:support@buzzwears.com\">support@buzzwears.com</a></tt><br/>\n" +
        "  BB Pin: <tt>55F156B8</tt>\n" +
        "</p>" +
        "<p>Your shopping cart is detailed below:</p>" +
        "<table style=\"background-color:#EEEEEE;\">" +
        "<thead>" +        
        "<tr><th>ID</th><th>Image</th><th>Description</th><th>Unit Price</th><th>Qty</th><th>Amount (NGN)</th>" +
        "</thead>" +
        "<tbody>";
        StringBuilder builder = new StringBuilder(msg);
        double total = 0;
        for(Orderdetails od:orderdetails) {
            builder.append(this.getRow(od));
            total += (od.getUnitPrice() * od.getQuantity());
        }        
        builder.append("<tfoot>");
        builder.append("<tr style=\"font-weight:bold;\"><td></td><td></td><td>Total</td><td></td><td></td><td>");
        builder.append(fmt.format(total));
        builder.append("</td>");
        builder.append("</tfoot>");
        builder.append("</tbody></table>"); 
        return builder.toString();
    }
    
    public String getRow(Orderdetails od) {
        String row = "<tr><td>" + od.getProductid() + 
             "</td><td><img width=\"75px\" height=\"75px\" src=\"http://www.buzzwears.com/viewimg?vid=" + 
             od.getVariantid()+"&image=image1\"/></td><td><a href=\"http://www.buzzwears.com/products/"+od.getProductid()+".jsp\">"+
                od.getProductName()+"</a></td><td>" +
             od.getUnitPrice() + "</td><td>" + od.getQuantity() + "</td><td>" + 
             od.getUnitPrice() * od.getQuantity() + 
             "</td></tr>";
        return row;
    }
    
    public String getLabel(double d) {
        return isFormat() ? fmt.format(d) : "" + d;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }
    
    @Override
    public String getProductIds() {
        return null;
    }
    
    public static Orderdetails getOrderdetails(final int productId, final int variantId, 
            final String productName, final float unitPrice, final int qty) {
        return new Orderdetails() {
            @Override
            public int getProductid() {
                return productId;
            }
            @Override
            public int getVariantid() {
                return variantId;
            }
            @Override
            public String getProductName() {
                return productName;
            }
            @Override
            public float getUnitPrice() {
                return unitPrice;
            }
            @Override
            public int getQuantity() {
                return qty;
            }
        };
    }
    
    public static interface Orderdetails {
        int getProductid();
        int getVariantid();
        String getProductName();
        float getUnitPrice();
        int getQuantity();
    }
}
