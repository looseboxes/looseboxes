package com.looseboxes.web.components.forms;

import java.io.Serializable;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


/**
 * @(#)FormFieldBean.java   20-Apr-2015 15:06:49
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
public class FormFieldBeanOld implements Serializable, FormFieldOld {
    
    private FormOld form;
    
    private String columnName;
    private Object columnValue;
    
    public FormFieldBeanOld() { }
    
    public FormFieldBeanOld(FormOld form) {
        this.form = form;
    }
    
    @Override
    public String getSelectOptionRealValue() {
        return this.form.getSelectOptionRealValue(columnName, columnValue);
    }
    
    @Override
    public String getTableName() {
        return this.form.getTableName();
    }
    
    public void setTableName(String tableName) {
        FormBeanOld formBean = new FormBeanOld();
        formBean.setTableName(tableName);
        Class entityClass = formBean.getEntityClass();
        formBean.setId(entityClass.getSimpleName());
        this.form = formBean;
    }
    
    public Class getEntityClass() {
        return this.form.getEntityClass();
    }
    
    public void setEntityClass(Class entityClass) {
        FormBeanOld formBean = new FormBeanOld();
        formBean.setEntityClass(entityClass);
        formBean.setId(entityClass.getSimpleName());
        this.form = formBean;
    }
    
    public void setRequest(HttpServletRequest request) throws ServletException {
        this.form.setRequest(request);
    }
    
    @Override
    public String getId() {
        return form.getId(this.columnName);
    }

    @Override
    public int getMaxLength() {
        return form.getMaxLength(columnName);
    }

    @Override
    public boolean isOptional() {
        return form.isOptional(columnName);
    }

    @Override
    public int getNullable() {
        return form.getNullable(columnName);
    }

    @Override
    public Map getSelectOptions() {
        return form.getSelectOptions(columnName);
    }

    @Override
    public int getSize() {
        return form.getSize(columnName);
    }

    @Override
    public String getType() {
        return form.getType(columnName);
    }
    
    @Override
    public boolean isNumberType() {
        return form.isNumberType(columnName);
    }

    @Override
    public boolean isTextType() {
        return form.isTextType(columnName);
    }

    @Override
    public boolean isDateType() {
        return form.isDateType(columnName);
    }
    
    @Override
    public boolean isTimeType() {
        return form.isTimeType(columnName);
    }
    
    @Override
    public boolean isTimestampType() {
        return form.isTimestampType(columnName);
    }
    
    @Override
    public boolean isHidden() {
        return form.isHidden(columnName);
    }
    
    @Override
    public boolean isUserChoice() {
        return form.isUserChoice(columnName);
    }
    
    @Override
    public boolean isFileType() {
        return form.isFileType(columnName);
    }
    
    @Override
    public boolean isSelectOptionType() {
        return form.isSelectOptionType(columnName);
    }
    
    @Override
    public boolean isAuxillaryType() {
        return form.isAuxillaryType(columnName);
    }
    
    @Override
    public String getSchemaName() {
        return form.getSchemaName(columnName);
    }
    
    @Override
    public String getLabel() {
        return form.getLabel(columnName);
    }

    public int [] getSqlTypes() {
        return form.getSqlTypes(columnName);
    }
    
    public Class getFieldType() {
        return form.getFieldType(columnName);
    }
    
    @Override
    public String getName() {
        return columnName;
    }
    
    @Override
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * If no value was set, defers to that in this form fields form which is 
     * retrieved via the {@link com.looseboxes.web.components.forms.FormBeanOld#getDetails()} 
     * method. 
     * @return The value for this form field
     * @see #getColumnValue() 
     */
    @Override
    public Object getValue() {
        return this.getColumnValue() == null ? form.getDetails() == null ? null : form.getDetails().get(columnName) : this.getColumnValue();
    }

    /**
     * If no value was set, does not defer to a default but rather returns null, 
     * unlike method {@link #getValue()}
     * @return The value that was set for this FormOld Field
     * @see #getValue() 
     */
    @Override
    public Object getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Object columnValue) {
        this.columnValue = columnValue;
    }

    public FormOld getForm() {
        return form;
    }

    public void setForm(FormOld form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return this.getClass().getName()+'{' + columnName + '=' + columnValue + '}';
    }
}
