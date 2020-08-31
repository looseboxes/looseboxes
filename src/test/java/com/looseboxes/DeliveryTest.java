package com.looseboxes;

import com.bc.jpa.controller.EntityController;
import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.UserBean;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import com.bc.jpa.context.JpaContext;


/**
 * @(#)DeliveryTest.java   23-May-2015 12:25:01
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
public class DeliveryTest extends AbstractTest {

    @Test
    public void testAll() {
        List<Integer> pids = this.getProductids(10);
        for(Integer pid:pids) {
            test(pid);
        }
    }
        
    public void test(Integer productid) {    
        
        JpaContext lbcf = WebApp.getInstance().getJpaContext();
        
        EntityController<Siteuser, Integer> sec = lbcf.getEntityController(Siteuser.class, Integer.class);
        Siteuser siteuser = sec.selectFirst(Siteuser_.emailAddress.getName(), "looseboxes@gmail.com");

        UserBean user = new UserBean();
        user.login(siteuser);

        EntityController<Product, Integer> pec = lbcf.getEntityController(Product.class, Integer.class);
        Product targetProduct = pec.find(productid);

        user.setSelectedItem(targetProduct.getProductvariantList().get(0));

        com.looseboxes.web.components.ShoppingCart cart = user.getShoppingCart();
        
        List<Orderproduct> items = cart.getItems();

        for(Orderproduct item:items) {

            Productvariant variant = item.getProductvariantid();

            float weight = cart.getDelivery().getWeight(variant, 0);

            BigDecimal deliveryAmount = cart.getDelivery().getDeliveryAmount(weight);

System.out.println(this.getClass().getName()+". Product variant ID: "+variant.getProductvariantid()+", weight: "+weight+", delivery amount: "+deliveryAmount);                
        }
    }
}
