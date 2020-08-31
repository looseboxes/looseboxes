package com.looseboxes.web.mail;

import java.util.Set;

/**
 * @(#)ProductEnquiryEmailIx.java   04-Jul-2013 16:32:53
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
public interface ProductEnquiryEmailIx {

    String getSubject();
    
    String getMessage();
    
    Set<String> getAttachments();
    
    String getFromEmail();

    String getFromUsername();

    Integer getTargetProductId();

    String getToEmail();

    String getToUsername();
}
