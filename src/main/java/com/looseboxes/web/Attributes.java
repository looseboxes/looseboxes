package com.looseboxes.web;


/**
 * @(#)Attributes.java   27-Apr-2015 11:12:53
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
public interface Attributes {
    
    /** ServletContext scoped attributes */
    String ADVERT_SEARCH_RESULTS = "advertSearchResults";
    String BASE_URL = "baseURL";
    String LOCAL_FOLDER = "localFolder";
    String MOBILE = "mobile";
    String CURRENT_YEAR = "currentYear";
    
    String EXPRESS_SHIPPING = "expressShipping";
    String TARGET_PAGE = "targetPage";
    
    /** Session scoped attributes */
    String CURRENT_UPDATE_FORM = "CurrentUpdateForm";
    String CURRENT_ADDON_FORM = "CurrentAddonForm";
}
