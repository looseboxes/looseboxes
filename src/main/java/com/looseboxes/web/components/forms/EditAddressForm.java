package com.looseboxes.web.components.forms;

import com.looseboxes.pu.entities.Address;

/**
 * @author Josh
 */
public class EditAddressForm extends SimpleEditForm<Address> {
    public EditAddressForm() {
        super(Address.class);
    }
}
