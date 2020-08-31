package com.looseboxes.web.components.forms;

import com.bc.web.core.form.Form.ActionType;

/**
 * @author Josh
 * @param <E>
 */
public abstract class EditForm<E> extends BaseForm<E> {

    public EditForm(Class<E> entityClass) {
        super(ActionType.EDIT, entityClass);
    }

    public EditForm(String id, Class<E> entityClass) {
        super(ActionType.EDIT, id, entityClass);
    }
}
