package com.looseboxes.web.plugins.jcaptcha;

import com.bc.web.core.captcha.ListImageCaptcha;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.WebApp;
import com.bc.config.Config;


/**
 * @(#)LbListImageCaptcha.java   07-May-2015 00:45:13
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
public class ListImageCaptchaEngine extends ListImageCaptcha {

    @Override
    public boolean isIgnoreCase() {
        Config props = WebApp.getInstance().getConfig();
        return props.getBoolean(AppProperties.CAPTCHA_IGNORE_CASE, true);
    }

    @Override
    public int[] getSizeRange() {
        Config props = WebApp.getInstance().getConfig();
        int imageWidth = props.getInt(AppProperties.CAPTCHA_IMAGE_WIDTH, 125);
        int imageHeight = props.getInt(AppProperties.CAPTCHA_IMAGE_HEIGHT, 35);
        return new int[]{imageWidth, imageHeight};
    }

    @Override
    public int[] getWordsRange() {
        Config props = WebApp.getInstance().getConfig();
        int minLetters = props.getInt(AppProperties.CAPTCHA_LETTERS_MIN, 4);
        int maxLetters = props.getInt(AppProperties.CAPTCHA_LETTERS_MAX, 5);
        return new int[]{minLetters, maxLetters};
    }

    @Override
    public int[] getFontsizeRange() {
        Config props = WebApp.getInstance().getConfig();
        int minFontsize = props.getInt(AppProperties.CAPTCHA_FONTSIZE_MIN, 25);
        int maxFontsize = props.getInt(AppProperties.CAPTCHA_FONTSIZE_MAX, 35);
        return new int[]{minFontsize, maxFontsize};
    }
}
