package com.looseboxes;

import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.web.servlets.Viewimg;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import org.junit.Test;


/**
 * @(#)ViewimgTest.java   24-May-2015 17:31:11
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
public class ViewimgTest extends BaseServletTest {

    public ViewimgTest() { }
    
    @Test
    public void testShoppingCart() throws ServletException, IOException {
        
        BaseServletTest servletTest = new BaseServletTest();
        
        for(Product product:this.getProducts(10)) {

            List<Productvariant> variants = product.getProductvariantList();
            
            // Select a random product variant
            Productvariant variant = this.getRandom(variants);
            
            // We use a different servlet for each
            //
            servletTest.setBaseServlet(new Viewimg());
            
            servletTest.addParameter(Productvariant_.productvariantid.getName(), variant.getProductvariantid());
            
            servletTest.addParameter("image", Productvariant_.image1.getName());
            
            servletTest.testServlet();
        }
System.out.println(this.getClass().getName()+". COMPLETED");        
    }
}
