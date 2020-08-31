package com.looseboxes;

import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.servlets.AddShippingToCart;
import com.looseboxes.web.servlets.Checkout;
import com.looseboxes.web.servlets.Login;
import com.looseboxes.web.servlets.ShoppingCart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


/**
 * @(#)ShoppingCartTest.java   13-May-2015 19:35:26
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
public class ShoppingCartTest extends AbstractTest {

    private String [] actions;
    
    public ShoppingCartTest() {
//        actions = new String[]{"add", "add", "remove", "clear", "delete", "add", "add"};
        actions = new String[]{"add", "add", "add", "add", "add", "add", "add", "add", "add", 
                                "remove", "clear", "add", "add", "remove", "remove", "add",
                                "clear", "add", "add", "clear", "add", "remove", "add", "add",
                                "add", "add", "add", "add", "add", "add", "add", "add", "add",
//                                "add", "add", "add", "add", "add", "add", "add", "add", "add",
//                                "add", "add", "add", "add", "add", "add", "add", "add", "add",
                                "add", "add", "add", "add", "add", "add", "add", "add", "add"};
//        actions = new String[]{"add", "add", "remove", "clear", "add", "add"};
    }
    
    @Test
    public void testShoppingCart() throws ServletException, IOException {
        
        HttpSession session = TestWebApp.getSession();
        
        UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);
        com.looseboxes.web.components.ShoppingCart cart = user.getShoppingCart();
        
        BaseServletTest servletTest = new BaseServletTest();
        
        int size = 0;
        
        List<Productvariant> added = new ArrayList<>();

long mb4 = com.bc.util.Util.availableMemory();
long tb4 = System.currentTimeMillis();

        for(String action:actions) {
            
            // We use a different ShoppingCart servlet for each
            servletTest.setBaseServlet(new ShoppingCart());

            if("add".equals(action) || "remove".equals(action)) {

                Productvariant selected;
                if("add".equals(action)) {
                    selected = this.getSelectedVariant(cart);
                    if(selected == null) {
                        continue;
                    }
//                    int instock = selected.getQuantityInStock();
//                    if(instock < 1) {
//log(this.getClass(), "Ignoring 'add' as instock: "+instock+" < 1");                        
//                    }
//                    int ordered = cart.getQuantityOnOrder(selected);
//                    if(ordered >= instock) {
//log(this.getClass(), "Ignoring 'add' as already ordered: "+ordered+" > instock: "+instock);                        
//                    }else{
                        added.add(selected);
                        size += selected.getProductid().getMinimumOrderQuantity();
//                    }
                }else {
                    if(added.isEmpty()) {
log(this.getClass(), "Ignoring 'remove' as nothing has been added yet");                        
                        continue;
                    }
                    selected = this.getRandom(added);
                    if(!cart.contains(selected)) {
log(this.getClass(), "Though add was attempted for it, cart found not to contain: ", selected);                        
                        continue;
                    }
                    added.remove(selected);
                    size -= selected.getProductid().getMinimumOrderQuantity();
                }
                
this.log(this.getClass(), "Action: {0}, product variant ID: {1}", action, selected.getProductvariantid());

                servletTest.addParameter(Productvariant_.productvariantid.getName(), selected.getProductvariantid());
                
            }else if("delete".equals(action) || "clear".equals(action)) {
                added.clear();
                size = 0;
            }
            
            servletTest.addParameter("action", action);
            
            servletTest.testServlet();

this.log(this.getClass(), "After action: {0}, shopping cart:\n{1}", action, cart);

assertEquals("Unexpected shopping cart size", size, cart.getItemCount());
        }
        
log(this.getClass(), "After adding/removing etc to cart, consumed, memory: {0}, time: {1}", mb4-com.bc.util.Util.usedMemory(mb4), System.currentTimeMillis()-tb4);

        if(!TestWebApp.isUserLoggedIn()) {
            servletTest.setBaseServlet(new Login());
            servletTest.testServlet();
            // This has been updated
            cart = user.getShoppingCart();
        }
        
log(this.getClass(), "After login and sync cart, consumed, memory: {0}, time: {1}", mb4-com.bc.util.Util.usedMemory(mb4), System.currentTimeMillis()-tb4);

//if(true) {
//    cart.setSyncWithDatabase(true);
//    cart.clear();
//    return;
//}
        servletTest.setBaseServlet(new AddShippingToCart());
        Integer integer = cart.getOrderId();
        servletTest.addExclusiveParameter(Productorder_.productorderid.getName(), integer);
        servletTest.testServlet();
        
        servletTest.setBaseServlet(new Checkout());
        servletTest.testServlet();
        
log(this.getClass(), "COMPLETED");        
    }

    public Productvariant getSelectedVariant(com.looseboxes.web.components.ShoppingCart cart) {
        
        List<Product> products = this.getProducts(1);

        Product product = products.get(0);

        List<Productvariant> variants = product.getProductvariantList();

        final int moq = product.getMinimumOrderQuantity();
        
        // Select a random product variant
        Productvariant selected = null;
        for(Productvariant variant:variants) {
            int available = cart.getAvailable(variant);
//            if(variant.getQuantityInStock() > 0) {
            if(moq <= available) {
                selected = variant;
                break;
            }
        }       
    
        if(selected == null) {
log(this.getClass(), "No product variant with available qty in stock > 0 found for: {0}", product);            
        }
        
        return selected;
    }
    
    public String[] getActions() {
        return actions;
    }

    public void setActions(String ...actions) {
        this.actions = actions;
    }
}
