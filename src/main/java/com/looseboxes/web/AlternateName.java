package com.looseboxes.web;

import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Productvariant_;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * @(#)ShortenedNames.java   17-Apr-2015 23:58:25
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
public final class AlternateName implements Serializable {
    
    private String name;
    
    public String getAlternate() {
        if(name == null) {
            throw new NullPointerException();
        }
        return AlternateName.getAlternate(name);
    }
    
    public static String getAlternate(String name) {
        String output = getShortName(name);
        if(output == null) {
            output = getLongName(name);
        }
        return output;
    }
    
    public String getShortName() {
        if(name == null) {
            throw new NullPointerException();
        }
        return AlternateName.getShortName(name);
    }
    
    public static String getShortName(String key) {
        return AlternateName.getPairs().get(key);
    }
    
    public String getLongName() {
        if(name == null) {
            throw new NullPointerException();
        }
        return AlternateName.getLongName(name);
    }
    
    public static String getLongName(String shortName) {
        String mLongName = null;
        Map<String, String> pairs = AlternateName.getPairs();
        Set<Entry<String, String>> entries = pairs.entrySet();
        for(Entry<String, String> entry:entries) {
            Object mShortName = entry.getValue();
            if(mShortName == null) {
                continue;
            }
            if(mShortName.equals(shortName)) {
                mLongName = entry.getKey();
            }
        }
        return mLongName;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String s) {
        this.name = s;
    }

    private static Map<String, String> p_accessViaGetter;
    private static Map<String, String> getPairs() {
        if(p_accessViaGetter == null) {
/////////////////////////////////////////////            
// CHANGE MAP initialCapacity as you add more
/////////////////////////////////////////////            
            p_accessViaGetter = new HashMap(7, 1.0f); 
            p_accessViaGetter.put(Product_.productid.getName(), "id");
            p_accessViaGetter.put(Product_.productcategoryid.getName(), "cat");
            p_accessViaGetter.put(Product_.productsubcategoryid.getName(), "subcat");
            p_accessViaGetter.put(Productorder_.productorderid.getName(), "orderId");
            p_accessViaGetter.put("action", "ax");
            p_accessViaGetter.put(Productvariant_.productvariantid.getName(), "vid");
            p_accessViaGetter.put("type", "t");
        }
        return p_accessViaGetter;
    }
}
