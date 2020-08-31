package com.looseboxes.web.components.forms;

import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Userpaymentmethod;
import com.looseboxes.pu.entities.Userpaymentmethod_;

/**
 * @author Josh
 */
public class InsertUserpaymentmethodForm extends BaseForm<Userpaymentmethod>{

    public InsertUserpaymentmethodForm() {
        super(ActionType.INSERT, "InsertUserpaymentmethod", Userpaymentmethod.class);
    }

    @Override
    protected Form createReferenced(String columnName) {
        if(Userpaymentmethod_.billingAddress.getName().equals(columnName)) {
            return new InsertAddressForm();
        }else{
            return null;
        }
    }

    @Override
    public int getMaxFileSize() {
        return -1;
    }

    @Override
    public String[] getFileNames() {
        return null;
    }
}
