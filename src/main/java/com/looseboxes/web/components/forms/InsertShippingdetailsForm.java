package com.looseboxes.web.components.forms;

import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Shippingdetails;
import com.looseboxes.pu.entities.Shippingdetails_;

/**
 * @author Josh
 */
public class InsertShippingdetailsForm extends BaseForm<Shippingdetails> {

    public InsertShippingdetailsForm() {
        super(ActionType.INSERT, "InsertShippingdetails", Shippingdetails.class);
    }

    @Override
    protected Form createReferenced(String columnName) {
        if(Shippingdetails_.deliveryAddress.getName().equals(columnName)) {
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
