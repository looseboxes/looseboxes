package com.looseboxes;

import com.bc.jpa.controller.EntityController;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.servlets.InsertProductServletOld;
import java.util.Map;
import javax.servlet.http.HttpSession;
import com.looseboxes.web.components.forms.FormOld;
import com.bc.jpa.context.JpaContext;


/**
 * @(#)InsertProductStage0Test.java   14-May-2015 09:43:57
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
public class InsertProductTest extends BaseServletTest {
    public InsertProductTest() throws Exception {
        InsertProductServletOld insertProduct = new InsertProductServletOld();
        this.setBaseServlet(insertProduct);
        String formId = insertProduct.getFormId();
        HttpSession session = TestWebApp.getSession();
        FormOld<Product> form = (FormOld<Product>)session.getAttribute(formId);
        Map params;
        if(form == null) {
            Product sample = new SampleProducts().getProduct();
            JpaContext cf = WebApp.getInstance().getJpaContext();
            EntityController<Product, Integer> ec = cf.getEntityController(Product.class, Integer.class);
            params = ec.toMap(sample, false);
System.out.println("Created parameters: "+params);            
        }else{
            params = form.getDetails();
System.out.println("Found parameters: "+params);            
        }
        this.setParameters(params);
        this.setForwardResponse(true);
    }
}
