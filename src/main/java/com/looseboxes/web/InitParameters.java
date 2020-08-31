package com.looseboxes.web;


/**
 * @(#)InitParameters.java   20-Apr-2015 17:48:52
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
public interface InitParameters {

    /** Decides if turing numer will be displayed */
    String HIDE_TURING = "hideTuring";

    /** The default time zone */
    String TIME_ZONE = "timeZone";
    
    /**Website name as will be displayed on web pages */
    String SITE_NAME = "siteName";
    
    String DEFAULT_META_DESCRIPTION = "defaultMetaDescription";
    
    /** Used as a part of the title of most pages */
    String DEFAULT_TITLE = "defaultTitle";

    /** Short text often displayed by logo */
    String LOGO_CAPTION = "logoCaption";
    
    /** Short text describing this site */
    String SITE_CAPTION = "siteCaption";
    
    /** The default category */
    String PRODUCTCATEGORY = "productcategory";
    
    String DEFAULT_META_DESCRIPTION_1 = "defaultMetaDescription1";
    
    String DEFAULT_SEARCH_TEXT = "defaultSearchText";
    
    String DEFAULT_SEARCH_TEXT_MOBILE = "defaultSearchTextMobile";
    
    String QUICK_CHAT_CONTEXT = "quickChatContext";
}
