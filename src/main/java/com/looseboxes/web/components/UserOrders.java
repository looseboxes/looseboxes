package com.looseboxes.web.components;

import com.bc.util.Log;
import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Shippingdetails;
import com.looseboxes.pu.entities.Shippingstatus;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.WebApp;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import com.bc.jpa.context.JpaContext;

/**
 * @(#)UserOrders.java   17-Jun-2013 21:03:16
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
public class UserOrders implements Serializable {
    
    private Siteuser siteuser;
    
    private LinkedList<Productorder> records;
    
    protected void initRecords() {
        
        if(siteuser == null) {
            throw new NullPointerException();
        }
        
        JpaContext cf = this.getJpaContext();
        
        EntityController<Productorder, ?> ec = cf.getEntityController(Productorder.class);
        
        List<Productorder> orders = ec.select(Productorder_.buyer.getName(), siteuser, 0, -1);
        
        if(orders == null) {
            
            records = null;
            
        }else{
            
            records = new LinkedList<>();
            
            EnumReferences refs = cf.getEnumReferences();
            
// Received orders are no longer for general viewing, just for record purposes        
            Shippingstatus recieved = (Shippingstatus)refs.getEntity(References.shippingstatus.FullyReceived);

            for(Productorder order:orders) {
                
                Shippingdetails shipping = order.getShippingdetails();
                
                if(shipping == null) {
                    
                    records.add(order);
                    
                }else{
                
                    Shippingstatus status = shipping.getShippingstatusid();

                    if(status.equals(recieved)) {
                        records.add(order);
                    }
                }
            }
        }

Log.getInstance().log(Level.FINE, "After adding records, size: {0}", 
this.getClass(), records.size());        
    }
    
    public JpaContext getJpaContext() {
        return WebApp.getInstance().getJpaContext();
    }

    public void setEmailAddress(String emailAddress) {
        if(emailAddress == null) {
            siteuser = null;
        }else{
            JpaContext cf = this.getJpaContext();
            EntityController<Siteuser, ?> ec = cf.getEntityController(Siteuser.class);
            siteuser = ec.selectFirst(Siteuser_.emailAddress.getName(), emailAddress);
            this.initRecords();
        }
    }

    public String getEmailAddress() {
        return siteuser == null ? null : siteuser.getEmailAddress();
    }

    public LinkedList<Productorder> getRecords() {
        return records;
    }

    public void setRecords(LinkedList<Productorder> records) {
        this.records = records;
    }

    public Siteuser getSiteuser() {
        return siteuser;
    }

    public void setSiteuser(Siteuser siteuser) {
        this.siteuser = siteuser;
    }
}
