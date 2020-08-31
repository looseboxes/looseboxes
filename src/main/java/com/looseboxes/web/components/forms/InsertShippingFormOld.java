package com.looseboxes.web.components.forms;

import com.looseboxes.pu.entities.Address;


/**
 * @(#)InsertShippingForm.java   12-May-2015 23:18:07
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
public class InsertShippingFormOld extends FormBeanOld<Address> {
    public InsertShippingFormOld() {
        this.setEntityClass(Address.class);
        this.setId("InsertAddress"); // This value is used in web pages
        this.setType(FormOld.ActionType.INSERT);
    }
}
