package com.looseboxes.web.payment;

import com.looseboxes.pu.References;


/**
 * @(#)Verve.java   28-Apr-2015 23:27:31
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
public class Verve extends VoguePay {

    @Override
    public References.paymentmethod getType() {
        return References.paymentmethod.Verve;
    }
}
