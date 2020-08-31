package com.looseboxes.web.exceptions;

import javax.servlet.ServletException;


/**
 * @(#)LoginException.java   10-May-2015 23:44:53
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
public class LoginException extends ServletException {

    /**
     * Creates a new instance of <code>LoginException</code> without detail message.
     */
    public LoginException() { }


    /**
     * Constructs an instance of <code>LoginException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public LoginException(String msg) {
        super(msg);
    }
}
