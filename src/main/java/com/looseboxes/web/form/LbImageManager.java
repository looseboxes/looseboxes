package com.looseboxes.web.form;

import com.bc.web.core.form.HttpImageManager;
import com.looseboxes.web.WebApp;


/**
 * @(#)LbImageManager.java   06-May-2015 22:30:20
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
public class LbImageManager extends HttpImageManager {

    public LbImageManager(final WebApp webApp) {
        
        super((relative) -> webApp.getExternalPath(relative), webApp.getName());
    }
}
