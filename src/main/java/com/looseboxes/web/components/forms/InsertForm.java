package com.looseboxes.web.components.forms;

/**
 * @author Josh
 * @param <E>
 */
public abstract class InsertForm<E> extends BaseForm<E> {

    public InsertForm(Class<E> entityClass) {
        super(ActionType.INSERT, entityClass);
    }

    public InsertForm(String id, Class<E> entityClass) {
        super(ActionType.INSERT, id, entityClass);
    }
}
