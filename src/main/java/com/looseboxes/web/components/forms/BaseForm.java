package com.looseboxes.web.components.forms;

import com.bc.web.core.form.AbstractForm;
import com.bc.web.core.form.Form.ActionType;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.WebApp;
import com.bc.config.Config;
import com.bc.config.ConfigData;

/**
 * @author Josh
 * @param <E>
 */
public abstract class BaseForm<E> extends AbstractForm<E> {

    private final boolean hideCaptcha;
    
    private final int minimumPasswordLength;
    
    private final String [] supportedImageTypes;
    
    private final ConfigData columnToSchemaNames;
    
    private final ConfigData columnToLabels;   
    
    public BaseForm(ActionType actionType, Class<E> entityClass) {

        this(actionType, actionType.getLabel()+entityClass.getSimpleName(), entityClass);
    }    
    
    public BaseForm(ActionType actionType, String id, Class<E> entityClass) {
        
        super(
                actionType, id, null, 
                LbApp.getInstance().getJpaContext(), entityClass
        );
        
        Config config = WebApp.getInstance().getConfig();
        this.hideCaptcha = config.getBoolean(AppProperties.HIDE_CAPTCHA, false);
        this.minimumPasswordLength = config.getInt(AppProperties.MINIMUM_PASSWORD_LENGTH, 6);
        this.columnToSchemaNames = config.subset("column_to_schemaname", ".");
        this.columnToLabels = config.subset("column_to_label", ".");
        this.supportedImageTypes = config.getArray(AppProperties.ACCEPTED_IMAGETYPES, null, ",");
    }
    
    @Override
    protected String createLabel(String columnName) {
        final String label = columnToLabels == null ? null : columnToLabels.getString(columnName, null);
        return label != null ? label : super.createLabel(columnName);
    }

    @Override
    protected String createSchemaName(String columnName) {
        final String schemaName = columnToSchemaNames == null ? null : columnToSchemaNames.getString(columnName, null);
        return schemaName != null ? schemaName : columnName;
    }
    
    @Override
    public boolean isFormField(String columnName) {
        return !(Product_.datecreated.getName().equals(columnName) ||
                Product_.timemodified.getName().equals(columnName)); 
    }

    @Override
    public int getStageCount() {
        return 2;
    }

    @Override
    public int getDefaultFieldSize() {
        return 35;
    }

    @Override
    public int getDefaultFieldMaxLength() {
        return 100;
    }
    
    @Override
    public final boolean isHideCaptcha() {
        return hideCaptcha;
    }

    @Override
    public final int getMinimumPasswordLength() {
        return minimumPasswordLength;
    }

    @Override
    public String[] getSupportedImageTypes() {
        return this.copyOfOrNull(supportedImageTypes);
    }

    @Override
    public String[] getDatePatterns() {
        Config config = WebApp.getInstance().getConfig();
        return config.getArray(AppProperties.DATE_PATTERNS, null, ",");
    }

    @Override
    public String[] getDateTimePatterns() {
        Config config = WebApp.getInstance().getConfig();
        return config.getArray(AppProperties.DATE_TIME_PATTERNS, null, ",");
    }

    @Override
    public String[] getTimePatterns() {
        Config config = WebApp.getInstance().getConfig();
        return config.getArray(AppProperties.TIME_PATTERNS, null, ",");
    }

    @Override
    public String[] getTimestampPatterns() {
        Config config = WebApp.getInstance().getConfig();
        return config.getArray(AppProperties.TIMESTAMP_PATTERNS, null, ",");
    }
}
