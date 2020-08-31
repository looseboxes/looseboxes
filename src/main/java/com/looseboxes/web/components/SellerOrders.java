package com.looseboxes.web.components;

import com.bc.util.Log;
import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Orderproduct_;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.JoinType;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;

/**
 * @(#)SellerOrders.java   17-Jun-2013 22:01:44
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
public class SellerOrders  extends UserOrders {
    
    @Override
    protected void initRecords() {

long mb4 = com.bc.util.Util.availableMemory();
long tb4 = System.currentTimeMillis();

        LinkedList<Productorder> allorders = new LinkedList<>();
        
        int offset = 0;
        int limit = 20;
        
        List<Productvariant> batch;
        
        while((batch = this.getMorevariants(offset, limit)) != null) {

            if(batch.isEmpty()) {
                break;
            }
            
            this.addOrders(batch, allorders);
            
            offset += batch.size();
        }
        
        this.setRecords(allorders);

Log.getInstance().log(Level.FINER, "Expended time: {0}, memory: {1} initializing {2} seller orders", 
this.getClass(), System.currentTimeMillis()-tb4, mb4-com.bc.util.Util.usedMemory(mb4), allorders.size());

    }

    private void addOrders(List<Productvariant> variants, List<Productorder> allorders) {

final int sizeb4 = allorders.size();

        final JpaContext cf = this.getJpaContext();

        try(Select<Productorder> select = cf.getDaoForSelect(Productorder.class)) { 

            for(Productvariant variant:variants) {

                TypedQuery<Productorder> tq =
                        select.where(Orderproduct.class, Orderproduct_.productvariantid.getName(), Select.EQUALS, variant)
                        .join(Productorder.class, Productorder_.orderproductList.getName(), JoinType.INNER, Orderproduct.class)
                        .createQuery();
                       
                List<Productorder> orders = tq.getResultList();

                if(orders != null) {
                    for(Productorder order:orders) {
                        if(!allorders.contains(order)) {
                            allorders.add(order);
                        }
                    }
                }
            }
        }
        
Log.getInstance().log(Level.FINER, "Added {0} orders for {1} productvariants", 
this.getClass(), allorders.size() - sizeb4, variants.size());

    }
    
    private List<Productvariant> getMorevariants(int offset, int limit) {
        
        final JpaContext cf = this.getJpaContext();
        
        try(Select<Productvariant> select = cf.getDaoForSelect(Productvariant.class)) {
            
            TypedQuery<Productvariant> tq = 
                    select.where(Product.class, Product_.seller.getName(), Select.EQUALS, this.getSiteuser())
                    .join(Productvariant.class, Productvariant_.productid.getName(), JoinType.INNER, Product.class)
                    .createQuery();
            
            tq.setFirstResult(offset);
            
            tq.setMaxResults(limit);
            
            List<Productvariant> output = tq.getResultList();
            
Log.getInstance().log(Level.FINER, "Retrieved {0} product variants for user: {1}",
        this.getClass(), output==null?null:output.size(), this.getEmailAddress());

            return output;
            
        }
    }
}

