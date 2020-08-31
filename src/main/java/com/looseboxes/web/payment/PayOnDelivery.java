package com.looseboxes.web.payment;

import com.looseboxes.pu.References;
import com.looseboxes.web.WebPages;

/**
 * @(#)PayOnDelivery.java   20-Aug-2014 19:20:33
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
public class PayOnDelivery extends CashPay {
    
    @Override
    public References.paymentmethod getType() {
        return References.paymentmethod.BookonHold;
    }
    
    @Override
    public String getForwardPage() {
        return WebPages.NOTICES_PAYONDELIVERY;
    }
}

