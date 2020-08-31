package com.looseboxes.web.components.forms;

import com.looseboxes.pu.entities.Productcomment;

/**
 * @author Josh
 */
public class InsertProductcommentForm extends SimpleFormImpl<Productcomment>{

    public InsertProductcommentForm() {
        super(ActionType.INSERT, "InsertProductcomment", Productcomment.class);
    }
}
