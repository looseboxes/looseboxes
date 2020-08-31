package com.looseboxes.web.servlets;

import com.looseboxes.web.formhandlers.UpdateProduct;
import com.bc.validators.Validator;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.FormUpdater;
import com.bc.web.core.form.HttpImageManager;
import com.looseboxes.pu.entities.Product;
import java.awt.Dimension;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Josh
 */
public abstract class UpdateProductServlet extends UpdateServlet<Product> {

    @Override
    public UpdateProduct createUpdateHandler(
            Validator<Form> validator, Validator<HttpServletRequest> captchaValidator, 
            HttpImageManager imageManager, Dimension imageDimension, FormUpdater formUpdater) {
        
        return new UpdateProduct(validator, captchaValidator, imageManager, imageDimension, formUpdater);
    }
}
