package com.looseboxes.web.components;

import com.looseboxes.web.WebApp;
import java.io.Serializable;


/**
 * @(#)DeliveryGroup.java   30-Dec-2014 13:55:36
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
public class DeliveryGroup implements Serializable {

    private String group;
    
    public DeliveryGroup() { }
    
    public float getDeliveryRate() {
        
        String sval = WebApp.getInstance().getConfig().getString(group+".rate");
        
        return Float.parseFloat(sval);
    }
    
    public float getExpressDeliveryRate() {
        
        String sval = WebApp.getInstance().getConfig().getString(group+".rate.express");
        
        return Float.parseFloat(sval);
    }
    
    public String getDeliveryPeriod() {
        
        return WebApp.getInstance().getConfig().getString(group+".period");
    }
    
    public String getExpressDeliveryPeriod() {
        
        return WebApp.getInstance().getConfig().getString(group+".period.express");
    }
    
    public String getDeliveryLocation() {
        
        return WebApp.getInstance().getConfig().getString(group+".locations");
    }

    public String getDeliveryGroup() {
        
        return WebApp.getInstance().getConfig().getString(group);
    }
    
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
