package com.looseboxes.web.components;

import com.bc.jpa.fk.EnumReferences;
import com.bc.util.Log;
import com.looseboxes.pu.Listings;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.WebApp;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

/**
 * @(#)ListingsBean.java   05-Apr-2014 12:37:33
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
public class ListingsBean implements Serializable {
    
    private String productcategory;
    
    public ListingsBean() { }
    
    public boolean isHidden(Object refTypeName) {

        EnumReferences refs = this.getReferences();
        
        boolean isHidden = (
                refTypeName.equals(refs.getTableName(References.productcategory.class)) ||
                refTypeName.equals(refs.getTableName(References.currency.class)) 
        ); 
        
        return isHidden;
    }
    
    public StringBuilder getMetaKeywords() {
        if(this.getKeywords() == null) {
            return new StringBuilder(0);
        }
        StringBuilder builder = new StringBuilder();
        for(Enum keyword:this.getKeywords()) {
            builder.append(keyword).append(',');
        }
        return builder;
    }

    public StringBuilder getTypeMetaKeywords() {
        StringBuilder output;
        if(this.getTypeKeywords() == null) {
            output = new StringBuilder(0);
        }else{
            output = new StringBuilder();
            for(Enum keyword:this.getTypeKeywords()) {
                output.append(keyword).append(',');
            }
        }
Log.getInstance().log(Level.FINER, "TypeMetaKeywords: {0}", this.getClass(), output);
        return output;
    }
    
    public Set<Enum> getKeywords() {
        Listings listings = this.getListings();
        if(listings == null) {
            return Collections.emptySet();
        }
        Set<Entry<String, Map<Enum, Integer>>> entries = listings.getValues().entrySet();
        HashSet<Enum> keywords = new HashSet();
        for(Entry<String, Map<Enum, Integer>> entry:entries) {
            Object name = entry.getKey();
            if(this.isHidden(name)) {
                continue;
            }
            Map<Enum, Integer> map = entry.getValue();
            keywords.addAll(map.keySet());
        }
Log.getInstance().log(Level.FINER, "Keywords: {0}", this.getClass(), keywords);
        return keywords;
    }
    
    public Set<Enum> getTypeKeywords() {
        Set<Enum> keywords;
        Listings listings = this.getListings();
        if(listings == null) {
            keywords = Collections.emptySet();
        }else{
            Set<Entry<String, Map<Enum, Integer>>> entries = listings.getValues().entrySet();
            keywords = new HashSet<>();
            EnumReferences refs = this.getReferences();
            String table = refs.getTableName(References.productsubcategory.class);
            String idCol = refs.getIdColumnName(References.productsubcategory.BabysAccessories);
            String dataCol = refs.getDataColumnName(References.productsubcategory.BabysAccessories);
//System.out.println(this.getClass().getName()+". Expected table name: "+table+", or id column: "+idCol+", or data column: "+dataCol);            
            for(Entry<String, Map<Enum, Integer>> entry:entries) {
                Object name = entry.getKey();
                Map<Enum, Integer> map = entry.getValue();
                if(name.equals(table) || name.equals(idCol) || name.equals(dataCol)) {
                    keywords.addAll(map.keySet());
                    break;
                }
            }
        }
Log.getInstance().log(Level.FINER, "Type Keywords: {0}", this.getClass(), keywords);
        return keywords;
    }
    
    private EnumReferences er_accessViaGetter;
    public EnumReferences getReferences() {
        if(er_accessViaGetter == null) {
            er_accessViaGetter = WebApp.getInstance().getJpaContext().getEnumReferences();
        }
        return er_accessViaGetter;
    }

    public Listings getListings() {
        Listings output;
        try{
            output = WebApp.getInstance().getListings(this.getEntityType());
        }catch(SQLException | RuntimeException e) {
            Log.getInstance().log(Level.WARNING, "Failed to initialize listings for: "+this.getEntityType(), this.getClass(), e);
            output = null;
        }
Log.getInstance().log(Level.FINER, "Listings: {0}", this.getClass(), output);
        return output;
    }
    
    public Class getEntityType() {
        return Product.class;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String category) {
        this.productcategory = category;
    }
}
