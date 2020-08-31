package com.looseboxes;

import com.bc.jpa.controller.EntityController;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Shippingdetails;
import com.looseboxes.pu.entities.Shippingdetails_;
import com.looseboxes.web.WebApp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;


/**
 * @(#)TestReferences.java   13-May-2015 17:58:35
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
public class ParametersConversionTest {
    
    public ParametersConversionTest() { }
    
    @Test
    public void testParametersConversion() throws Exception {

        JpaContext jpaContext = LbApp.getInstance().getJpaContext();
        
        try(Select<Integer> qb = jpaContext.getDaoForSelect(Productorder.class, Integer.class)) {
            List<Integer> selected = qb.from(Productorder.class)
            .select(Productorder_.productorderid.getName())
            .createQuery().setMaxResults(15).getResultList();
            
            for(Integer ival:selected) {

                this.test(ival);
            }
        }
    }
    
    private void test(Integer productorderid) throws Exception {
        
        JpaContext cf = WebApp.getInstance().getJpaContext();

        Map params = new HashMap();
        // We use string version of the order id
        params.put(Shippingdetails_.productorderid.getName(), ""+productorderid);
        params.put(Shippingdetails_.shippingstatusid.getName(), References.shippingstatus.Pending);
System.out.println(this.getClass().getName()+". Params: "+params);                    
        
        Map dbParams = cf.getDatabaseFormat().toDatabaseFormat(Shippingdetails.class, params);
System.out.println(this.getClass().getName()+". DbParams: "+dbParams);                    
        
        EntityController<Shippingdetails, Integer> ec = cf.getEntityController(Shippingdetails.class, Integer.class);
        Shippingdetails shippingdetails = ec.create(dbParams, false);
System.out.println(this.getClass().getName()+". Entity: "+shippingdetails);                    
System.out.println(this.getClass().getName()+". COMPLETED");                    
    }
}
