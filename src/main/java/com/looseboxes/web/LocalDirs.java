package com.looseboxes.web;


/**
 * @(#)LocalDirs.java   27-Apr-2015 13:49:26
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
public interface LocalDirs {
    
    String CATEGORIES = "/cat";
    
    String MOBILE_FOLDER = "/m";

    String PRODUCT_PAGES_FOLDER = "/pages";

    String PRODUCT_PAGES_FOLDER_MOBILE = MOBILE_FOLDER + PRODUCT_PAGES_FOLDER;
    
    String FILES = "/files";

    String EXCEL_FILES = FILES + "/excel";

    String SERIALIZEDOBJECTS_FOLDER = "/cache";

    String LOGS_FOLDER = "/logs";

    String SEARCHES_FOLDER = "/searches";

    String USERSESSIONS_FOLDER = "/sessions";

    String IMAGES_FOLDER = "/images";

    String ADVERTS_FOLDER = IMAGES_FOLDER + "/adverts";

    String ADVERTS_2_FOLDER = IMAGES_FOLDER + "/adverts2";
}
