package com.looseboxes.web.components.forms;

import com.looseboxes.pu.entities.Userpaymentmethod;


/**
 * @(#)InsertPayment.java   12-May-2015 23:15:10
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
public class InsertPaymentFormOld extends FormBeanOld<Userpaymentmethod> {
    public InsertPaymentFormOld() {
        this.setEntityClass(Userpaymentmethod.class);
        this.setId("InsertUserpaymentmethod"); // This value is used in web pages
        this.setType(FormOld.ActionType.INSERT);
    }
}
