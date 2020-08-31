package com.looseboxes.web.components.forms;

/**
 * @author Josh
 * @param <E>
 */
public class SingleFieldForm<E> extends BaseForm<E> {
    
    private final int maxFileSize;
    
    private final String fieldName;
    
    private final String [] fileNames;

    public SingleFieldForm(
            ActionType actionType, Class<E> entityClass, String fieldName) {
        
        this(actionType, entityClass, fieldName, 5_000_000, false);
    }
    
    public SingleFieldForm(
            ActionType actionType, Class<E> entityClass, String fieldName, int maxFileSize, boolean isFile) {
        super(actionType, entityClass);
        this.maxFileSize = maxFileSize;
        this.fieldName = fieldName;
        this.fileNames = isFile ? new String[]{fieldName} : null;
    }

    @Override
    protected com.bc.web.core.form.Form createReferenced(String columnName) {
        return null;
    }

    @Override
    public int getMaxFileSize() {
        return this.maxFileSize;
    }

    @Override
    public String[] getFileNames() {
        return this.fileNames;
    }

    @Override
    public boolean isFormField(String columnName) {
        boolean formField = super.isFormField(columnName);
        if(formField) {
            formField = this.fieldName.equals(columnName);
        }
        return formField;
    }
}

