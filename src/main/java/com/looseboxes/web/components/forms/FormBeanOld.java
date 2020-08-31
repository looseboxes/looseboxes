package com.looseboxes.web.components.forms;

import com.bc.jpa.fk.EnumReferences;
import com.bc.jpa.dao.sql.SQLUtils;
import com.bc.util.Log;
import com.bc.web.core.captcha.CaptchaFactory;
import com.looseboxes.pu.entities.Address_;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productorder_;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.InitParameters;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.UserBean;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.Basic;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.bc.jpa.context.JpaContext;
import com.bc.config.Config;


/**
 * @(#)FormBean.java   30-Apr-2015 18:58:48
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * The {@link java.util.Collection Collection} returned by this object's 
 * ${@link com.looseboxes.web.components.forms.FormBeanOld#getFormFields() getFormFields()}
 * method contains a <b>re-used</b> {@link com.looseboxes.web.components.forms.FormFieldOld FormFieldOld}
 * object. This is suitable for web pages but not for real life.
 * @param <E> Type of the entity 
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public class FormBeanOld<E> implements Serializable, FormOld<E> {
    
    private ActionType actionType;
    
    private int defaultSize;
    
    private int defaultMaxLength;
    
    private int stage;
    
    private int stageCount;
    
    private String id;
    
    private String action;
    
    private String startPage;
    
    private Class<E> entityClass;
    
    private Map<String, Object> selectedDetails;
    
    private Map<String, Object> updateDetails;
    
    private UserBean user;

    private List<FormOld> parents;
    
    private List<FormOld> children;
    
    public FormBeanOld() { 
        this((Class)null);
    }
    
    public FormBeanOld(Class<E> entityClass) { 
        FormBeanOld.this.setEntityClass(entityClass);
        this.defaultSize = 35;
        this.defaultMaxLength = 100; 
        this.stageCount = 2;
    }

    public FormBeanOld(String tableName) { 
        FormBeanOld.this.setTableName(tableName);
        this.defaultSize = 35;
        this.defaultMaxLength = 100;
        this.stageCount = 2;
    }
    
    @Override
    public String getSelectOptionRealValue(String columnName, Object columnValue) {
        if(columnName == null) {
            throw new NullPointerException();
        }
        String output;
        if(columnValue == null) {
            output = null;
        }else{
            if(!this.isSelectOptionType(columnName)) {
                throw new UnsupportedOperationException();
            }
            this.getControllerFactory();
            JpaContext cf = WebApp.getInstance().getJpaContext();
            EnumReferences refs = cf.getEnumReferences();
            Enum en = refs.getEnum(columnName, columnValue);
            output =  en == null ? null : en.toString();
        }
        return output;
    }
    
    @Override
    public List<FormOld> getParents() {
        return parents;
    }
    
    @Override
    public List<FormOld> getChildren() {
        return children;
    }
    
    @Override
    public void addParent(FormOld form) {
        if(parents == null) {
            parents = new ArrayList<>();
        }
        parents.add(form);
    }
    
    @Override
    public void addChild(FormOld form) {
        if(children == null) {
            children = new ArrayList<>();
        }
        children.add(form);
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public void setStage(int stage) {
        this.stage = stage;
    }

    @Override
    public int getStageCount() {
        return this.stageCount;
    }

    public void setStageCount(int stageCount) {
        this.stageCount = stageCount;
    }

    /**
     * @return The html form action 1.e &lt;form action="[FORM_ACTION]"...
     */
    @Override
    public String getAction() {
        return action;
    }

    /**
     * @param action The html form action 1.e &lt;form action="[FORM_ACTION]"...
     */
    @Override
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public ActionType getType() {
        return actionType;
    }
    
    public void setTypeInt(int i) {
        ActionType [] values = ActionType.values();
        for(ActionType value:values) {
            if(value.ordinal() == i) {
                this.setType(value);
                break;
            }
        }
    }
    
    public void setTypeString(String name) {
        ActionType value = ActionType.valueOf(name);
        this.setType(value);
    }

    public void setType(ActionType type) {
        this.actionType = type;
    }
    
    public Map<String, Object> getFormRecord() {
        return this.getFormDetails();
    }
    
    @Override
    public Map<String, Object> getFormDetails() {

        Map<String, Object> record = this.getDetails();
        
Log.getInstance().log(Level.FINER, "Form details: {0}", this.getClass(), record);

        String [] fieldNames = this.getFieldNames(); 

        List<String> sort = new ArrayList<>(Arrays.asList(fieldNames));
        
        // We went through this seemingly long process because
        // new TreeMap(comparator) did not work well with our custom
        // comparator 
        FormFieldComparatorOld comparator = new FormFieldComparatorOld(this); 

Log.getInstance().log(Level.FINER, "BEFORE sort: {0}", this.getClass(), sort);            

        Collections.sort(sort, comparator);

Log.getInstance().log(Level.FINER, " AFTER sort: {0}", this.getClass(), sort);            
        
        Map<String, Object> pairs = new LinkedHashMap(sort.size(), 1.0f);
        
        for(String columnName:sort) {
            if(record != null) {
                pairs.put(columnName, record.get(columnName));
            }else{
                pairs.put(columnName, null);
            }
        }
Log.getInstance().log(Level.FINER, "Form record: {0}", this.getClass(), pairs);

        return pairs;
    }
    
    @Override
    public void setRequest(HttpServletRequest request) throws ServletException {
        
        HttpSession session = request.getSession();

        if(this.id == null) {
            throw new NullPointerException();
        }
        
        session.setAttribute(this.getId(), this);
        
        this.startPage = ServletUtil.getSourcePage(request, null);
        
        this.user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);
        
        if(!this.isHideTuring()) {
            
            CaptchaFactory.createGenerator().generate(request);
        }
Log.getInstance().log(Level.FINER, "Created form: {0}", Forms.class, FormBeanOld.this);
    }
    
    @Override
    public String getStartPage() {
        return startPage;
    }
    
    @Override
    public UserBean getUser() {
        return user;
    }
    
    public Map<String, Object> getRecord() {
        return this.getDetails();
    }
    
    @Override
    public Map<String, Object> getDetails() {
        return (updateDetails != null) ? updateDetails : selectedDetails;
    }
    
    private Collection<FormFieldOld> f_fields;
    @Override
    public Collection<FormFieldOld> getFormFields() {
        if(f_fields == null) {
            f_fields = new FormFieldCollection();
        }        
        return f_fields;
    }

    Boolean hasImages;
    @Override
    public boolean isHasImageFields() {
        if(hasImages == null && this.entityClass != null) {
            String [] imageNames = Forms.getImageNames(this.entityClass);
            if(imageNames != null) {
                String [] columnNames = this.getColumnNames();
                for(String imageName:imageNames) {
                    if(this.contains(columnNames, imageName)) {
                        hasImages = Boolean.TRUE;
                        break;
                    }
                }
            }
            if(hasImages == null) {
                hasImages = Boolean.FALSE;
            }
        }
        return hasImages == null ? false : hasImages;
    }
    
    private String [] f_names;
    @Override
    public String [] getFieldNames() {
        if(f_names == null) {
            List<String> list = new ArrayList<>(Arrays.asList(this.getColumnNames()));
            Iterator<String> iter = list.iterator();
            while(iter.hasNext()) {
                String s = iter.next();
                if(!this.isUserChoice(s)) {
                    iter.remove();
                }
            }
            f_names = list.toArray(new String[0]);
Log.getInstance().log(Level.FINE, "Entity class: {0}, field names: {1}", this.getClass(), this.entityClass, list);
        }
        return f_names;
    }
    
    private Boolean h_turing;
    @Override
    public Boolean isHideTuring() {
        if(h_turing == null) {
            String sval = WebApp.getInstance().getServletContext().getInitParameter(InitParameters.HIDE_TURING);
            h_turing = sval == null ? Boolean.FALSE : Boolean.parseBoolean(sval) ? Boolean.TRUE : Boolean.FALSE;
        }
        return h_turing;
    }

    @Override
    public String[] getOptionalFields() {
        String [] fieldNames = this.getFieldNames();
        List<String> optionals = new ArrayList<>(fieldNames.length);
        for(String fieldName:fieldNames) {
            if(this.isOptional(fieldName)) {
                optionals.add(fieldName);
            }
        }
        return optionals.toArray(new String[0]);
    }

    @Override
    public String[] getMandatoryFields() {
        String [] fieldNames = this.getFieldNames();
        List<String> mandatory = new ArrayList<>(fieldNames.length);
        for(String fieldName:fieldNames) {
            if(!this.isOptional(fieldName)) {
                mandatory.add(fieldName);
            }
        }
        return mandatory.toArray(new String[0]);
    }
    
    private String [] c_names;
    @Override
    public String [] getColumnNames() {
        if(c_names == null) {
            c_names = WebApp.getInstance().getJpaContext().getMetaData().getColumnNames(entityClass);
        }
        return c_names;
    }
    
    public int getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public int getDefaultMaxLength() {
        return defaultMaxLength;
    }

    public void setDefaultMaxLength(int defaultMaxLength) {
        this.defaultMaxLength = defaultMaxLength;
    }

    @Override
    public Map getSelectedDetails() {
        return selectedDetails;
    }

    @Override
    public boolean addSelectedDetails(Map details) {
        if(details == null || details.isEmpty()) {
            return false;
        }
        if(this.selectedDetails == null) {
            this.selectedDetails = new HashMap();
        }
        this.selectedDetails.putAll(details);
        return true;
    }

    @Override
    public Map getUpdateDetails() {
        return updateDetails;
    }

    @Override
    public boolean addUpdateDetails(Map details) {
        if(details == null || details.isEmpty()) {
            return false;
        }
        if(this.updateDetails == null) {
            this.updateDetails = new HashMap();
        }
        this.updateDetails.putAll(details);
        return true;
    }

    /**
     * <p>Maintained for backward compaitibility.</p>
     * Rather use {@link #getTableName()}
     * @deprecated 
     * @return The name of the table associated with this object
     * @see #getTableName() 
     */
    @Deprecated
    public String getTable() {
        return this.getTableName();
    }

    /**
     * @return The name of the table associated with this object
     */
    @Override
    public String getTableName() {
        return entityClass == null ? null :
        WebApp.getInstance().getJpaContext().getMetaData().getTableName(entityClass);
    }
    
    public void setTableName(String tableName) {
        if(!this.isEqual(this.getTableName(), tableName)) {
            WebApp webApp = WebApp.getInstance();
//            Class entityType = webApp.getJpaContext().getMetaData().getEntityClass(
//                    webApp.getDatabaseName(), null, tableName);
            Class entityType = webApp.getJpaContext().getMetaData().getEntityClass(
                    null, null, tableName);
            this.setEntityClass(entityType);
        }
    }
    
    @Override
    public Class<E> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<E> entityClass) {
        if(!this.isEqual(this.entityClass, entityClass)) {
            this.c_names = null;
            this.f_names = null;
//            this.e_declaredfields = null;
        }
        this.entityClass = entityClass;
    }
    
    private boolean isEqual(Object a, Object b) {
        return a == null && b == null || (a != null && a.equals(b));
    }
    
    @Override
    public int getMinimumPasswordLength() {
        return this.getConfig().getInt(AppProperties.MINIMUM_PASSWORD_LENGTH, 6);
    }

    @Override
    public String [] getDatePatterns() {
        return this.getConfig().getArray(AppProperties.DATE_PATTERNS, (String[])null);
    }

    @Override
    public String [] getDateTimePatterns() {
        return this.getConfig().getArray(AppProperties.DATE_TIME_PATTERNS, (String[])null);
    }

    @Override
    public String [] getTimePatterns() {
        return this.getConfig().getArray(AppProperties.TIME_PATTERNS, (String[])null);
    }

    @Override
    public String [] getTimestampPatterns() {
        return this.getConfig().getArray(AppProperties.TIMESTAMP_PATTERNS, (String[])null);
    }
    
    @Override
    public String [] getSupportedImageTypes() {
        return this.getConfig().getArray(AppProperties.ACCEPTED_IMAGETYPES, (String[])null);
    }
    
    @Override
    public int getMaxFileSize() {
        return 5000000;
    }
    
/////////////////////////////////////
    
    @Override
    public FormFieldOld getFormField(String columnName, Object columnValue) {
        FormFieldBeanOld formFieldBean = null;
        if(this.contains(this.getFieldNames(), columnName)) {
            Field field = this.getField(columnName);
            if(field != null) {
                // This is reused
                formFieldBean = new FormFieldBeanOld();
                formFieldBean.setForm(this);
                formFieldBean.setColumnName(columnName);
                Map<String, Object> details = this.getDetails();
                if(details != null) {
                    formFieldBean.setColumnValue(details.get(columnName));
                }
            }
        }
        return formFieldBean;
    }
    
    @Override
    public boolean isUserChoice(String columnName) {
        boolean notUserChoice = (
                Product_.productid.getName().equals(columnName) ||
                Product_.logo.getName().equals(columnName) ||
                Productvariant_.productvariantid.getName().equals(columnName) ||
                Product_.datecreated.getName().equals(columnName) || 
                Product_.timemodified.getName().equals(columnName) ||
                Product_.views.getName().equals(columnName) ||
                Product_.ratingPercent.getName().equals(columnName) ||
                Product_.seller.getName().equals(columnName) ||
                Address_.addressid.getName().equals(columnName) ||
                Productorder_.productorderid.getName().equals(columnName) ||
                Productorder_.buyer.getName().equals(columnName));
        return !notUserChoice;
    }
    
    @Override
    public int getColumnIndex(String column) {
        return this.indexOf(this.getColumnNames(), column);
    }    
    
    @Override
    public int getFieldIndex(String column) {
        int index = this.indexOf(this.getFieldNames(), column);
Log.getInstance().log(Level.FINER, "Column: {0}, field index: {1}", this.getClass(), column, index);
        return index;
    }    
    
    @Override
    public String getId(String columnName) {
        this.check(columnName);
        return columnName+"Id";
    }

    @Override
    public int getMaxLength(String columnName) {
        return this.getMaxLength(columnName, this.defaultMaxLength);
    }

    public int getMaxLength(String columnName, int defaultValue) {
        int maxLength;
        if(columnName.equals(Product_.description.getName())) {
            maxLength = 1000; 
        }else if(columnName.equals(Product_.url.getName()) || 
                columnName.equals(Product_.logo.getName()) ||
                columnName.startsWith("image")) {
            maxLength = 255;
        }else if(columnName.equals(Product_.keywords.getName()) ||
                columnName.equals(Siteuser_.emailAddress.getName()) ||
                columnName.equals(Siteuser_.contactDetails.getName())) {
            maxLength = 96;
        }else if(columnName.equals(Product_.productName.getName()) ||
                columnName.equals(Product_.model.getName()) ||
                columnName.equals(Siteuser_.firstName.getName()) ||
                columnName.equals(Siteuser_.lastName.getName()) ||
                columnName.equals(Address_.city.getName()) ||
                columnName.equals(Address_.county.getName()) ||
                columnName.equals(Address_.streetAddress.getName())) {
            maxLength = 64;
        }else if(columnName.equals(Productvariant_.productSize.getName()) ||
                columnName.equals(Address_.fax.getName()) ||
                columnName.equals(Address_.postalCode.getName()) ||
                columnName.equals(Siteuser_.phoneNumber.getName()) ||
                columnName.equals(Siteuser_.mobileNumber.getName()) ||
                columnName.equals(Productvariant_.productSize.getName()) ||
                columnName.equals(Productvariant_.color.getName())) {
            maxLength = 32;
        }else if(columnName.equals(Product_.price.getName()) ||
                columnName.equals(Product_.discount.getName())) {
            maxLength = 12;
        }else if(columnName.equals(Productvariant_.weight.getName()) ||
                columnName.equals(Productvariant_.quantityInStock.getName()) ||
                columnName.equals(Product_.minimumOrderQuantity.getName())) {
            maxLength = 6;
//datePatterns = MM-dd-yyyy,MM/dd/yyyy,MM dd yyyy
//timePatterns = HH:mm:ss
//dateTimePatterns = MM-dd-yyyy HH:mm:ss,MM/dd/yyyy HH:mm:ss,MM dd yyyy HH:mm:ss
//timestampPatterns = yyyyMMddHHmmss
        }else if(this.isDateType(columnName)) {
            maxLength = 12;
        }else if(this.isTimestampType(columnName)) {
            maxLength = 24;
        }else if(this.isTimeType(columnName)) {
            maxLength = 12;
        }else{
Log.getInstance().log(Level.WARNING, "Max length not yet supported for column: {0}.{1}, field type: {1}", 
    this.getClass(), this.getTableName(), columnName, this.getFieldType(columnName));
            maxLength = defaultValue;
        }
        return maxLength;
    }
    
    @Override
    public boolean isOptional(String columnName) {
        return this.getNullable(columnName) == ResultSetMetaData.columnNullable || this.getNullable(columnName) == ResultSetMetaData.columnNullableUnknown;
    }

    @Override
    public int getNullable(String columnName) {
        int nullable;
        this.check(columnName);
        Field field = this.getField(columnName);
        if(field != null) {
            Basic basic = field.getAnnotation(Basic.class);
            if(basic != null) {
                nullable = basic.optional() ? ResultSetMetaData.columnNullable : ResultSetMetaData.columnNoNulls;
            }else{
                // Only OneToOne and ManyToOne may be optional (has the optional() method)
                OneToOne one2one = field.getAnnotation(OneToOne.class);
                if(one2one != null) {
                    nullable = one2one.optional() ? ResultSetMetaData.columnNullable : ResultSetMetaData.columnNoNulls;
                }else{
                    ManyToOne many2one = field.getAnnotation(ManyToOne.class);
                    if(many2one != null) {
                        nullable = many2one.optional() ? ResultSetMetaData.columnNullable : ResultSetMetaData.columnNoNulls;
                    }else{
                        nullable = ResultSetMetaData.columnNullableUnknown;
                    }
                }
            }
        }else{
            nullable = ResultSetMetaData.columnNullableUnknown;
        }
        return nullable;
    }
    

    @Override
    public Map getSelectOptions(String columnName) {
        if(this.isSelectOptionType(columnName)) {
            this.check(columnName);
            EnumReferences refs = this.getEnumReferences();
            Class enumType = refs.getEnumType(columnName);
            // This is an enum map with pairs [Enum = Integer key]
            // E.g for availability: [InStock = 1]
            //
            Map mappings = refs.getMappings(enumType);
            return mappings;
        }else{
            return Collections.emptyMap();
        }
    }

    @Override
    public int getSize(String columnName) {
        int maxLength = this.getMaxLength(columnName);
        // Size must not be greater than maxLength
        int size = (maxLength >= defaultSize) ? defaultSize : maxLength;
        return size;
    }

    @Override
    public String getType(String columnName) {
//<%-- @related HTML FormOld fieldTypes file,text,password,select,hidden,aux --%>
        String columnType;
        if(!this.isUserChoice(columnName)) {
            columnType = null;
        }else if(this.isHidden(columnName)) {    
            columnType = "hidden";
        }else if(this.isFileType(columnName)) {
            columnType = "file";
        }else if(this.isSelectOptionType(columnName)) {
            columnType = "select";
        }else if(this.isAuxillaryType(columnName)) {
            columnType = "aux";
        }else{
            if(columnName.toLowerCase().contains("pasword")) {
                columnType = "password";
            }else{
                columnType = "text";
            }
        }
        return columnType;
    }
    
    @Override
    public boolean isNumberType(String columnName) {
        Class type = this.getFieldType(columnName);
        return type == Number.class || type == BigDecimal.class ||
                type == Long.class || type == long.class ||
                type == Integer.class || type == int.class ||
                type == Short.class || type == short.class ||
                type == Double.class || type == double.class ||
                type == Float.class || type == float.class;
    }

    @Override
    public boolean isTextType(String columnName) {
        Class type = this.getFieldType(columnName);
        return type == String.class;
    }

    @Override
    public boolean isDateType(String columnName) {
        return getFieldType(columnName) == java.sql.Date.class || getFieldType(columnName) == java.util.Date.class;
    }
    
    @Override
    public boolean isTimeType(String columnName) {
        return getFieldType(columnName) == java.sql.Time.class;
    }
    
    @Override
    public boolean isTimestampType(String columnName) {
        return getFieldType(columnName) == java.sql.Timestamp.class;
    }
    
    @Override
    public boolean isHidden(String columnName) {
        return false;
    }
    
    @Override
    public boolean isFileType(String columnName) {
        this.check(columnName);
        String [] fileNames = Forms.getFileNames(this.getEntityClass());
        return fileNames == null ? false : this.contains(fileNames, columnName);
    }
    
    @Override
    public boolean isSelectOptionType(String columnName) {
        this.check(columnName);
        return this.getEnumReferences().getEnumType(columnName) != null;
    }
    
    @Override
    public boolean isAuxillaryType(String columnName) {
        Field field = this.getField(columnName);
        return field.getAnnotation(JoinColumn.class) != null;
    }
    
    @Override
    public int [] getSqlTypes(String columnName) {
        Class fieldType = this.getFieldType(columnName);
        int [] types = fieldType == null ? null : SQLUtils.getTypes(fieldType);
        return types;
    }
    
    @Override
    public Class getFieldType(String columnName) {
        Field field = this.getField(columnName);
        return field == null ? null : field.getType();
    }

    @Override
    public String getSchemaName(String columnName) {
        String tableName = this.getTableName();
        String schema = null;
        if(tableName != null) {
            schema = this.getConfig().getString("column_to_schemaname."+tableName+"."+columnName);
        }
        if(schema == null) {
            schema = this.getConfig().getString("column_to_schemaname."+columnName);
        }
        return schema == null ? columnName : schema;
    } 

    @Override
    public String getLabel(String columnName) {
        String tableName = this.getTableName();
        String label = null;
        if(tableName != null) {
            label = this.getConfig().getString("column_to_label."+tableName+"."+columnName);
        }
        if(label == null) {
            label = this.getConfig().getString("column_to_label."+columnName);
        }
        if(label == null) {
            label = this.createLabel(columnName);
        }
        return label == null ? columnName : label;
    }
    
    public String createLabel(String columnName) {
        int spaces = 0;
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<columnName.length(); i++) {
            char ch = columnName.charAt(i);
            if(i == 0) {
                ch = Character.toUpperCase(ch);
            }else{
                if(Character.isUpperCase(ch) && spaces < 1) {
                    builder.append(' ');
                    ++spaces;
                }
            }
            builder.append(ch);
            spaces = 0;
        }
        return builder.toString();
    }
    
    private Config c;
    public Config getConfig() {
        if(c == null) {
            c = WebApp.getInstance().getConfig();
        }
        return c;
    }
    
    private EnumReferences e_refs_accessViaGetter;
    private EnumReferences getEnumReferences() {
        if(e_refs_accessViaGetter == null) {
            e_refs_accessViaGetter = this.getControllerFactory().getEnumReferences();
        }
        return e_refs_accessViaGetter;
    }
    
    private JpaContext cf_accessViaGetter;
    private JpaContext getControllerFactory() {
        if(cf_accessViaGetter == null) {
            cf_accessViaGetter = WebApp.getInstance().getJpaContext();
        }
        return cf_accessViaGetter;
    }

    private void check(String columnName) {
        if(this.entityClass == null || this.getTableName() == null || columnName == null) {
            throw new NullPointerException();
        }
    }
    
    private Field getField(String columnName) {
        Field output = null;
        Field [] declaredFields = this.getDeclaredFields();
        for(Field field:declaredFields) {
            if(field.getName().equals(columnName)) {
                output = field;
Log.getInstance().log(Level.FINER, "Column: {0}, field type: {1}, field: {2}", 
        this.getClass(), columnName, field.getType(), field);
                break;
            }
        }
        return output;
    }
    
    private Field [] d_fields;
    private Field [] getDeclaredFields() {
        if(d_fields == null) {
            d_fields = this.entityClass.getDeclaredFields();
        }
        return d_fields;
    }
    
    private boolean contains(String [] arr, String e) {
        return this.indexOf(arr, e) != -1;
    }

    private int indexOf(String [] arr, String e) {
        int output = -1;
        for(int i=0; i<arr.length; i++) {
            if(arr[i].equals(e)) {
                output = i;
                break;
            }
        }
        return output;
    }
    
/////////////////////////////////////////////    
    
    private class FormFieldCollection extends AbstractCollection<FormFieldOld> {
        @Override
        public Iterator<FormFieldOld> iterator() {
            return new FormFieldIterator();
        }
        @Override
        public int size() {
            String [] cols = FormBeanOld.this.getFieldNames();
            return cols == null ? 0 : cols.length;
        }
    }
    
    private class FormFieldIterator implements Iterator<FormFieldOld> {
        private int offset;
        // This is reused
        private FormFieldBeanOld formFieldBean;
        private FormFieldIterator() { }
        @Override
        public boolean hasNext() {
            return offset < FormBeanOld.this.getFieldNames().length;
        }
        @Override
        public FormFieldOld next() {
            try{
                String [] fieldNames = FormBeanOld.this.getFieldNames();
                String fieldName = fieldNames[offset];
                if(formFieldBean == null) {
                    formFieldBean = new FormFieldBeanOld();
                }
                formFieldBean.setForm(FormBeanOld.this);
                formFieldBean.setColumnName(fieldName);
                Map<String, Object> details = FormBeanOld.this.getDetails();
                if(details != null) {
                    formFieldBean.setColumnValue(details.get(fieldName));
                }
                return formFieldBean;
            }finally{
                ++offset;
            }
        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported. This implementation is unmodifiable");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getName());
        builder.append(", ID :").append(this.getId());
        builder.append(", action: ").append(this.getAction());
        builder.append(", actionType :").append(this.getType());
        builder.append('\n');
        builder.append("tableName :").append(this.getTableName());
        builder.append(", entity class :").append(this.getEntityClass());
        builder.append(", stage :").append(this.getStage());
        builder.append(", startPage :").append(this.getStartPage());
        builder.append("\ncolumns: ").append(this.getColumnNames()==null?null:Arrays.toString(this.getColumnNames()));
        builder.append("\nfields :").append(this.getFieldNames()==null?null:Arrays.toString(this.getFieldNames()));
        builder.append("\nmandatory fields :").append(this.getMandatoryFields()==null?null:Arrays.toString(this.getMandatoryFields()));
        builder.append("\ndetails :").append(this.getFormDetails());
        builder.append("\nfield details :").append(this.getDetails());
        return builder.toString();
    }
}
