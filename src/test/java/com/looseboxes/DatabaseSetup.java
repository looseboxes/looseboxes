package com.looseboxes;

import com.bc.jpa.controller.EntityController;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.WebApp;
import java.util.List;
import com.bc.jpa.context.JpaContext;


/**
 * @(#)InsertTestData.java   13-May-2015 20:29:24
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
public class DatabaseSetup {
    
    public DatabaseSetup() { }
    
    public boolean isTestDataInDatabase() {
        
        final JpaContext cf = WebApp.getInstance().getJpaContext();

        EntityController<Product, Integer> ec = cf.getEntityController(Product.class, Integer.class);

        List<Product> found = ec.find(); 

        return found != null && !found.isEmpty();
    }
}
