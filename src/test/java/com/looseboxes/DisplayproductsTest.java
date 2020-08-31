package com.looseboxes;

import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.servlets.Displayproduct;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.Test;


/**
 * @(#)DisplayproductsTest.java   24-Jul-2015 19:37:37
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
public class DisplayproductsTest extends AbstractTest {

    public DisplayproductsTest() { }
    
    @Test
    public void testShoppingCart() throws ServletException, IOException {
        
        BaseServletTest servletTest = new BaseServletTest();
        
        for(Integer productid:this.getProductids(10)) {
            
            // We use a different servlet for each
            //
            servletTest.setBaseServlet(new Displayproduct());
            
            servletTest.addExclusiveParameter(Product_.productid.getName(), productid);
            
            servletTest.testServlet();
        }
System.out.println(this.getClass().getName()+". COMPLETED");        
    }
}
