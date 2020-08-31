package com.looseboxes.web.formhandlers;

/**
 * @(#)InsertProduct.java   17-May-2016 14:17:32
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.util.Log;
import com.bc.validators.Validator;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.FormField;
import com.bc.web.core.form.FormUpdater;
import com.bc.web.core.form.HttpImageManager;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.web.components.UserBean;
import java.awt.Dimension;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  1.0
 * @since    1.0
 */
public class InsertProduct extends UpdateProduct {

    public InsertProduct(String captchaParameterName, Dimension imageDimension) {
        super(captchaParameterName, imageDimension);
    }

    public InsertProduct(
            Validator<Form> validator, Validator<HttpServletRequest> captchaValidator, 
            HttpImageManager imageManager, Dimension imageDimension, FormUpdater formUpdater) {
        super(validator, captchaValidator, imageManager, imageDimension, formUpdater);
    }

    @Override
    public void format(HttpServletRequest request) {
        
        super.format(request);
        
        Map params = this.getFormInputs();
        
Log.getInstance().log(Level.FINER, "Parameters: {0}", this.getClass(), params);
        
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);

        params.put(Product_.seller.getName(), user.getDetails());

        if(params.get(Product_.availabilityid) == null) {
            
            params.put(Product_.availabilityid.getName(), References.availability.InStock);
            params.put(Product_.currencyid.getName(), user.getCurrencyEntity());
            params.put(Product_.minimumOrderQuantity.getName(), 1);
            params.put(Product_.productstatusid.getName(), References.productstatus.New);
Log.getInstance().log(Level.FINER, "AFTER Parameters: {0}", this.getClass(), params);
            
            FormField [] formFields = this.getForm().getFormFields();
            
            for(FormField formField:formFields) {
                
                Form referencedForm = formField.getForm();
                
                if(referencedForm == null) {
                    continue;
                }
                
                if(referencedForm.getEntityClass() == Productvariant.class) {
                    final String columnName = Productvariant_.quantityInStock.getName();
                    Map updateDetails = referencedForm.getUpdateDetails();
                    if(updateDetails == null || updateDetails.get(columnName) == null) {
                        Map variantParams = Collections.singletonMap(columnName, 1);
                        referencedForm.addUpdateDetails(variantParams);
                    }
                }
            }
        }
    }
}
