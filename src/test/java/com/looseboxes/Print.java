package com.looseboxes;

import com.bc.io.CharFileIO;
import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.web.components.ShoppingCart;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


/**
 * @(#)Print.java   25-Jul-2015 15:10:57
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
public class Print {

    public static void writeToFile(String title, ShoppingCart cart) {
        writeToFile(title, cart.getOrder());
    }
    
    public static void writeToFile(String title, Productorder order) {
        writeToFile(title, order.getOrderproductList());
    }
    
    public static void writeToFile(String title, List<Orderproduct> orderproducts) {
        
        if(orderproducts == null) {
            return;
        }
        
        File file = Paths.get(System.getProperty("user.home"), "orderproducts.txt").toFile();
        
        CharFileIO io = new CharFileIO();
        
        StringBuilder builder = new StringBuilder(100);
        
        int i = 0;
        
        for(Orderproduct op:orderproducts) {
            
            builder.setLength(0);
            
            try{
                if(i == 0) {
                    if(title != null) {
                        builder.append("\n===================================================\n").append(title).append('\n');
                    }
                    append(builder, op);
                    io.write(builder.toString(), file, true);
                }else{
                    append(builder, op);
                    io.write(false, builder.toString(), file);
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
            
            ++i;
        }
    }
    
    private static void append(StringBuilder builder, Orderproduct op) {
        builder.append("Order: ").append(op.getProductorderid().getProductorderid()).append('\t');
        builder.append("ItemID: ").append(op.getOrderproductid()).append('\t');
        builder.append("SKU: ").append(op.getProductvariantid().getProductvariantid()).append('\t');
        builder.append("InStock: ").append(op.getProductvariantid().getQuantityInStock()).append('\t');
        builder.append("Ordered: ").append(op.getQuantity()).append('\n');
    }
}
