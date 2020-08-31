package com.looseboxes.web.payment;

import com.looseboxes.pu.References;
import com.looseboxes.web.WebPages;

/**
 * @(#)BankPay.java   15-Jun-2014 02:27:59
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
public class BankPay extends CashPay {
    
    @Override
    public References.paymentmethod getType() {
        return References.paymentmethod.BankDeposit;
    }
    
    @Override
    public String getForwardPage() {
        return WebPages.NOTICES_BANKPAYMENT;
    }
}
