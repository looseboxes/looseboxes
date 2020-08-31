package com.looseboxes.web.components;

import com.bc.reflection.ReflectionUtil;
import com.bc.util.Log;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


/**
 * @(#)EntityCollection.java   17-May-2015 00:37:27
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
public class EntityCollection implements Serializable {

    private Collection entities;
    
    private String columnName;
    
    private Object columnValue;
    
    private final ReflectionUtil reflection;

    private final Map<Class, Method[]> methodsCache;

    public EntityCollection() {
        this.reflection = new ReflectionUtil();
        this.methodsCache = new HashMap();
    }
    
    public List getValues() {
        
        List values = new ArrayList(entities.size());
        
        Method method = null;
        
        
        for(Object entity:entities) {
            
            if(method == null) {
                final Class entityClass = entity.getClass();
                method = this.reflection.getMethod(false, this.getMethods(entityClass), columnName);
                if(method == null) {
                    Log.getInstance().log(Level.WARNING, "Error accessing method for "+columnName+" in type: "+entity.getClass(), this.getClass());
                    break; //@TODO break or continue
                }
            }
            
            Object value = null;
            if(method != null){
                try{
                    value = method.invoke(entity);
                }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    Log.getInstance().log(Level.WARNING, "Error invoking method for "+columnName+" in type: "+entity.getClass(), this.getClass(), e);
                    break;
                }
            }
            
            if(this.columnValue == null) {
                if(value != null) {
                    values.add(value);
                }
            }else{
                if(this.columnValue.equals(value)) {
                    values.add(value);
                }
            }
        }
        
        return values;
    }
    
    private String getMethodName(boolean setter, Method [] methods, String columnName) {
        
        StringBuilder builder = new StringBuilder();
        String prefix = setter ? "set" : "get";
        builder.append(prefix);
        for(int i=0; i<columnName.length(); i++) {
            char ch = columnName.charAt(i);
            if(i == 0) {
                ch = Character.toUpperCase(ch);
            }
            builder.append(ch);
        }
        return builder.toString();
    }
    
    public Method [] getMethods(Class type) {
        Method [] methods = this.methodsCache.get(type);
        if(methods == null) {
            methods = type.getMethods();
            this.methodsCache.put(type, methods==null?new Method[0]:methods);
        }
        return methods;
    }

    public Collection getEntities() {
        return entities;
    }

    public void setEntities(Collection entities) {
        this.entities = entities;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Object columnValue) {
        this.columnValue = columnValue;
    }
}
