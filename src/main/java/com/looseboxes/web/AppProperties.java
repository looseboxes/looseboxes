package com.looseboxes.web;

/**
 * @(#)AppProperties.java   11-Apr-2015 06:22:46
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
public interface AppProperties {
    
    String BASE_URL = "baseURL";

    String LOCAL_URL = "localURL";
    
    String BASE_PATH = "basePath";
    
    String LOCAL_PATH = "localPath";
    
    String PERSISTENCE_URI = "persistenceURI";
    
    String AUTHSVC_EMAIL = "authsvc.emailaddress";
    String AUTHSVC_PASSWORD = "authsvc.password";
    String AUTHSVC_URL = "authsvc.url";
    String AUTHSVC_INITIALIZATION_DELAY = "authsvc.initialization.delay";
    
    String ALGORITHM = "algorithm";
    String ENCRYPTION_KEY = "encryptionkey";
    
    String DATABASE_NAME = "database.name";
    String DATABASE_USER = "database.user";
    String DATABASE_PASSWORD = "database.password";

    String ACCEPTED_IMAGETYPES = "acceptedImageTypes";
    
    String MINIMUM_PASSWORD_LENGTH = "minimumPasswordLength";
    
    String DATE_PATTERNS = "datePatterns";
    String DATE_TIME_PATTERNS = "dateTimePatterns";
    String TIME_PATTERNS = "timePatterns";        
    String TIMESTAMP_PATTERNS = "timestampPatterns";
    
    String ADMIN_EMAILS = "adminEmails";
    
    /** In minutes */    
    String PAYMENT_LIFESPAN = "paymentLifespan";
    
    String INITIAL_PAYMENT_ALLOWED = "initialPaymentAllowed";
    
    String REMEMBERME_TIMEOUT_DAYS = "remembermeTimeoutDays";
    
    String LOGO_FILENAME = "logoFilename";

    String BANNER_FILENAME = "bannerFilename";
    
    String SPONSORED_ITEMS = "sponsoredItems";
    
    String HIDE_CAPTCHA = "hideCaptcha";
    
// META-INF/properties/default/captcha.properties, META-INF/properties/captcha.properties    
    String CAPTCHA_IGNORE_CASE = "ignoreCase";
    String CAPTCHA_IMAGE_WIDTH = "image.width";
    String CAPTCHA_IMAGE_HEIGHT = "image.height";
    String CAPTCHA_LETTERS_MIN = "letters.min";
    String CAPTCHA_LETTERS_MAX = "letters.max";
    String CAPTCHA_FONTSIZE_MIN = "fontSize.min";
    String CAPTCHA_FONTSIZE_MAX = "fontSize.max";

// META-INF/properties/default/shipping.properties, META-INF/properties/shipping.properties    
    String FREESHIPPING_MINIMUM = "freeShippingMinimumAmount";
    String FREESHIPPING_COUNTRIES = "freeShippingCountries";
    String FREESHIPPING_STATES = "freeShippingStates";
    String SHIPPING_MIN_WEIGHT = "shipping.minimumWeight";
    String SHIPPING_VAT_RATE = "shipping.vat.rate";
    String SHIPPING_FEE_AMOUNT = "shipping.fee.amount";
    
// META-INF/properties/default/mail.properties, META-INF/properties/mail.properties
    String address_default_support="address.default.support";
    String address_default_noreply="address.default.noreply";
    String address_default_notice="address.default.notice";
    String address_default_error="address.default.error";
    String address_default_complaints="address.default.complaints";
}
