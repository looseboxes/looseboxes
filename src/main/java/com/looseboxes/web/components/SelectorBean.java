package com.looseboxes.web.components;

import com.bc.jpa.controller.EntityController;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.bc.jpa.context.JpaContext;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import com.bc.jpa.dao.Select;

/**
 * @(#)SelectorBean.java   10-May-2015 19:02:17
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
/**
 * @param <E>
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public class SelectorBean<E> implements Serializable {

    private boolean addParametersFromRequest;
    
    private int offset = -1;
    
    private int limit = -1;
    
    private Class<E> entityClass;
    
    private String columnName;
    
    private Object columnValue;
    
    private Map params;

    public SelectorBean() { }
    
    public void setRequest(HttpServletRequest request) {
        if(this.isAddParametersFromRequest()) {
            Map map = ServletUtil.getParameterMap(request);
            int size = map == null ? 1 : map.size() + 1;
            params = new HashMap(size, 1.0f);
            if(map != null) {
                params.putAll(map);
            }
            if(columnName != null && columnValue != null) {
                params.put(columnName, columnValue);
            }
        }else{
            if(columnName != null && columnValue != null) {
                params = Collections.singletonMap(columnName, columnValue);
            }
        }
    }
    
    public E getSingleResult() {
        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        try(Select<E> qb = jpaContext.getDaoForSelect(entityClass)) {
            return qb.from(entityClass).where(params).createQuery().getSingleResult();
        }catch(NoResultException e) {
            return null;
        }
    }
    
    public List<E> getResultList() {
        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        try(Select<E> qb = jpaContext.getDaoForSelect(entityClass)) {
            TypedQuery<E> tq = qb.from(entityClass).where(params).createQuery();
            if(offset > -1) {
                tq.setFirstResult(offset);
            }
            if(limit > -1) {
                tq.setMaxResults(limit);
            }
            return tq.getResultList();
        }
    }
    
    public List<Map<String, ?>> getResultListMappings() {
        JpaContext cf = WebApp.getInstance().getJpaContext();
        EntityController<E, ?> ec = cf.getEntityController(this.getEntityClass());
        return ec.toMapList(this.getResultList(), -1);
    }
    
    public String getTableName() {
        JpaContext cf = WebApp.getInstance().getJpaContext();
        return cf.getMetaData().getTableName(entityClass);
    }

    public void setTableName(String table) {
        JpaContext cf = WebApp.getInstance().getJpaContext();
        this.entityClass = cf.getMetaData().findEntityClass(table);
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        this.entityClass = entityClass;
    }
    
    public boolean isAddParametersFromRequest() {
        return addParametersFromRequest;
    }

    public void setAddParametersFromRequest(boolean addParametersFromRequest) {
        this.addParametersFromRequest = addParametersFromRequest;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
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
