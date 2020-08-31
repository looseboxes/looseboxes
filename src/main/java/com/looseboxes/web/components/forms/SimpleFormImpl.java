package com.looseboxes.web.components.forms;

import com.bc.web.core.form.SimpleForm;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.WebApp;
import com.bc.config.Config;

/**
 * @author Josh
 * @param <E>
 */
public class SimpleFormImpl<E> extends SimpleForm<E> {

    public SimpleFormImpl(ActionType actionType, Class<E> entityClass) {
        this(
                actionType, actionType.getLabel()+entityClass.getSimpleName(), entityClass
        );
    }
    
    public SimpleFormImpl(ActionType actionType, String id, Class<E> entityClass) {
        super(
                actionType, id, null, 
                LbApp.getInstance().getJpaContext(), entityClass
        );
    }

    @Override
    public boolean isFormField(String columnName) {
        return !(Product_.datecreated.getName().equals(columnName) ||
                Product_.timemodified.getName().equals(columnName)); 
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
