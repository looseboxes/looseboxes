package com.looseboxes.web;


/**
 * @(#)Dirs.java   24-Apr-2015 13:07:03
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
public interface Dirs {
    
    String CATEGORIES = "/cat";
    
    String MOBILE_FOLDER = "/m";

    String PRODUCT_PAGES_FOLDER = "/pages";

    String PRODUCT_PAGES_FOLDER_MOBILE = MOBILE_FOLDER + PRODUCT_PAGES_FOLDER;
    
    String IMAGES = "/images";
    
    String RESOURCES = "/resources";
    
    String WEB_INF = "/WEB-INF";
    
    String META_INF = "/META-INF";
    
    String JSPF = WEB_INF + "/jspf";

    String PROPERTIES = META_INF + "/properties";

    String DEFAULT_PROPERTIES = PROPERTIES + "/defaults";
    
    String MOBILE = "/m";
}
