package com.looseboxes.web.components.forms;

import com.bc.web.core.form.Form;

/**
 * @author Josh
 * @param <E>
 */
public class SimpleEditForm<E> extends SimpleFormImpl<E> {

    public SimpleEditForm(Class<E> entityClass) {
        super(Form.ActionType.EDIT, entityClass);
    }

    public SimpleEditForm(String id, Class<E> entityClass) {
        super(Form.ActionType.EDIT, id, entityClass);
    }
}
