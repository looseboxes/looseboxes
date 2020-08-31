package com.looseboxes.web.exceptions;


/**
 * @(#)LoginToCheckoutException.java   18-Jul-2015 09:27:55
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
public class LoginToCheckoutException extends LoginException {

    /**
     * Creates a new instance of <code>LoginToCheckoutException</code> without detail message.
     */
    public LoginToCheckoutException() { }


    /**
     * Constructs an instance of <code>LoginToCheckoutException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public LoginToCheckoutException(String msg) {
        super(msg);
    }
}
