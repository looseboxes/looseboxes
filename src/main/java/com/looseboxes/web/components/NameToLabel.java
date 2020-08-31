package com.looseboxes.web.components;

import com.looseboxes.web.WebApp;
import java.io.Serializable;
import java.util.Objects;
import com.bc.config.Config;


/**
 * @(#)NameToLabel.java   11-May-2015 20:43:15
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
public class NameToLabel implements Serializable {

    private String tableName;
    
    private String columnName;
    
    public NameToLabel() { }
    
    public String getLabel() {
        return this.getValue();
    }
    
    public String getValue() {
        
        Objects.requireNonNull(columnName);
        
        String label = null;
        
        if(tableName != null) {
            label = this.getConfig().getString("column_to_label."+tableName+"."+columnName);
        }
        
        if(label == null) {
            label = this.getConfig().getString("column_to_label."+columnName);
        }
        
        return label == null ? toLabel(columnName) : label;
    }
    
    public String toLabel(String name) {
        StringBuilder label = new StringBuilder(name.length() * 2);
        for(int i=0; i<name.length(); i++) {
            char ch = name.charAt(i);
            if(i == 0) {
                label.append(Character.toTitleCase(ch));
            }else{
                if(Character.isTitleCase(ch)) {
                    label.append(' ');
                }
                label.append(ch);
            }
        }
        return label.toString();
    }

    private Config c;
    public Config getConfig() {
        if(c == null) {
            c = WebApp.getInstance().getConfig();
        }
        return c;
    }
    
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
