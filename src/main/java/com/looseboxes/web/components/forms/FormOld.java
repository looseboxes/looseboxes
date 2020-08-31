package com.looseboxes.web.components.forms;

import com.looseboxes.web.components.UserBean;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


/**
 * @(#)Form.java   30-Apr-2015 23:39:23
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 * @param <E> Entity type for database operations
 */
public interface FormOld<E> {
    
    /** 
     * These literal values are used in the web pages. 
     * Don't change without changing those in the web pages. 
     */
    enum ActionType{
        INSERT("UPLOAD"),
        SELECT("SEARCH"),
        EDIT("UPDATE"),
        DELETE("DELETE");
        private final String label;
        private ActionType(String label) {
            this.label = label;
        }
        public final int getOrdinal() {
            return ordinal();
        }
        public final String getName() {
            return name();
        }
        public final String getLabel() {
            return label;
        }
    }

    String getSelectOptionRealValue(String columnName, Object columnValue);

    List<FormOld> getParents();
    
    List<FormOld> getChildren();
    
    void addParent(FormOld form);
    
    void addChild(FormOld form);
    
    int getStage();

    void setStage(int stage);
    
    int getStageCount();
    
    /**
     * @param action The html form action 1.e &lt;form action="[FORM_ACTION]"...
     */
    void setAction(String action);

    /**
     * @return The html form action 1.e &lt;form action="[FORM_ACTION]"...
     */
    String getAction();
    
    ActionType getType();
    
    void setRequest(HttpServletRequest request) throws ServletException;

    Map getSelectedDetails();

    boolean addSelectedDetails(Map selectedDetails);
    
    Map getUpdateDetails();

    boolean addUpdateDetails(Map updateDetails);

///////////// In web pages remove from FormFieldOld. and add to FormOld.xxx    
    int getMinimumPasswordLength();

    String [] getDatePatterns();

    String [] getDateTimePatterns();

    String [] getTimePatterns();

    String [] getTimestampPatterns();
    
    String [] getSupportedImageTypes();
    
    int getMaxFileSize();
    
//////////////////////////////////////////////    
    
    Collection<FormFieldOld> getFormFields();
    
    Map<String, Object> getFormDetails();
    
    String [] getOptionalFields();
    
    String [] getMandatoryFields();

    String [] getColumnNames();
    
    Class<E> getEntityClass();

    String[] getFieldNames();

    String getId();

    Map getDetails();

    String getStartPage();

    String getTableName();

    UserBean getUser();
    
    boolean isHasImageFields();

    Boolean isHideTuring();
    
///////////////////////////////////
    
    FormFieldOld getFormField(String columnName, Object columnValue);
    
    int getColumnIndex(String columnName);
    
    int getFieldIndex(String columnName);

/////////////////////////////////////////////

    boolean isUserChoice(String columnName);

    String getId(String columnName);

    int getMaxLength(String columnName);

    boolean isOptional(String columnName);

    int getNullable(String columnName);

    Map getSelectOptions(String columnName);

    int getSize(String columnName);

    String getType(String columnName);
    
    boolean isNumberType(String columnName);

    boolean isTextType(String columnName);

    boolean isDateType(String columnName);
    
    boolean isTimeType(String columnName);
    
    boolean isTimestampType(String columnName);
    
    boolean isHidden(String columnName);
    
    boolean isFileType(String columnName);
    
    boolean isSelectOptionType(String columnName);
    
    boolean isAuxillaryType(String columnName);
    
    int [] getSqlTypes(String columnName);
    
    Class getFieldType(String columnName);

    String getSchemaName(String columnName);

    String getLabel(String columnName);
}
