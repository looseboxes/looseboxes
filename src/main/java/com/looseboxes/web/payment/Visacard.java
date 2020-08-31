package com.looseboxes.web.payment;

import com.looseboxes.pu.References;


/**
 * @(#)Visacard.java   28-Apr-2015 23:27:23
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
public class Visacard extends VoguePay {

    @Override
    public References.paymentmethod getType() {
        return References.paymentmethod.VisaCard;
    }
}
