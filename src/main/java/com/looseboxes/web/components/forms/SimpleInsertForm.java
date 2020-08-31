package com.looseboxes.web.components.forms;

/**
 * @author Josh
 * @param <E>
 */
public class SimpleInsertForm<E> extends SimpleFormImpl<E> {

    public SimpleInsertForm(Class<E> entityClass) {
        super(ActionType.INSERT, entityClass);
    }

    public SimpleInsertForm(String id, Class<E> entityClass) {
        super(ActionType.INSERT, id, entityClass);
    }
}
