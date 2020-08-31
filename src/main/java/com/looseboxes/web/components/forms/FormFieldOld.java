package com.looseboxes.web.components.forms;

import com.looseboxes.pu.entities.Siteuser_;
import java.util.Map;


/**
 * @(#)FormField.java   30-Apr-2015 19:08:04
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
public interface FormFieldOld {

    public static enum WebOnlyField{ userCaptchaInput, 
        agreeToTerms, confirmPassword, corporateCheckbox}
 
    public static final String [] HIDDEN_FIELDS = {
        Siteuser_.username.getName(),
        Siteuser_.emailAddress.getName(), 
        FormFieldOld.WebOnlyField.confirmPassword.name(),
        "reset", "submit", "back", "cancel", "rh", "targetPage", FormFieldOld.WebOnlyField.userCaptchaInput.name()
    };

    String getSelectOptionRealValue();

    String getColumnName();

    Object getColumnValue();
    
    String getId();

    String getLabel();

    int getMaxLength();

    String getName();

    String getSchemaName();
        
    Map getSelectOptions();

    int getSize();

    String getTableName();

    String getType();

    Object getValue();

    boolean isDateType();

    boolean isFileType();

    boolean isHidden();
    
    int getNullable();
    
    boolean isNumberType();
    
    boolean isOptional();

    boolean isSelectOptionType();
    
    boolean isAuxillaryType();

    boolean isTextType();

    boolean isTimeType();

    boolean isTimestampType();

    boolean isUserChoice();
}
