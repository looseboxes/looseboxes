package com.looseboxes.web.components.forms;

import com.bc.util.Log;
import com.looseboxes.pu.entities.Product_;
import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Level;


/**
 * @(#)FormFieldComparator.java   10-May-2015 18:16:05
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
public class FormFieldComparatorOld 
        implements Comparator<String>, Serializable {
    
    private FormOld form;
    
    public FormFieldComparatorOld() {
        this(null);
    }

    public FormFieldComparatorOld(FormOld form) {
        FormFieldComparatorOld.this.setForm(form);
    }
    
    @Override
    public int compare(String col1, String col2) {
        try{
            return this.compare_(col1, col2);
        }catch(RuntimeException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private int compare_(String col1, String col2) {
        
        final int i1 = form.getFieldIndex(col1);
        final int i2 = form.getFieldIndex(col2);
        
        if(i2 == -1 && i1 == -1) {
            return 0;
        }else{
            if(i1 == -1) {
                return 1;
            }else if(i2 == -1) {
                return -1;
            }    
        }        
        
        // compare by nullables
        boolean nonulls1 = form.getNullable(col1) == ResultSetMetaData.columnNoNulls;
Log.getInstance().log(Level.FINER, "Column 1: {0}, no nulls: {1}", this.getClass(), col1, nonulls1);

        boolean nonulls2 = form.getNullable(col2) == ResultSetMetaData.columnNoNulls; 
Log.getInstance().log(Level.FINER, "Column 2: {0}, no nulls: {1}", this.getClass(), col2, nonulls2);
        
        if(nonulls2 != nonulls1) {
            return nonulls2 ? 1 : -1;
        }
        
        Map m1 = form.getSelectOptions(col1);
        Map m2 = form.getSelectOptions(col2);
        
        boolean has1Listing = m1 != null && !m1.isEmpty();
        boolean has2Listing = m2 != null && !m2.isEmpty();
        if(has2Listing != has1Listing) {
            return has2Listing ? 1 : -1;
        }
        
        int rating1 = this.getRating(col1);
        int rating2 = this.getRating(col2);
        if(rating2 != rating1) {
            return rating2 > rating1 ? 1 : -1;
        }else{
            return 0;
        }
    }
    
    private int getRating(String col) {
        int rating;
// subtype, price, discount, keywords  
        if(col.equals(Product_.productsubcategoryid.getName())) {
            rating = 4;
        }else if(col.equals(Product_.price.getName())){
            rating = 3;
        }else if(col.equals(Product_.discount.getName())){
            rating = 2;
        }else if(col.equals(Product_.keywords.getName())){
            rating = 1;
        }else{
            rating = 0;
        }
        return rating;
    }

    public Class getEntityClass() {
        return this.form.getEntityClass();
    }

    public FormOld getForm() {
        return form;
    }

    public void setForm(FormOld form) {
        this.form = form;
    }
}
