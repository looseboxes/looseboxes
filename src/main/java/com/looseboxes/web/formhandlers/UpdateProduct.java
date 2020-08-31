package com.looseboxes.web.formhandlers;

import com.bc.jpa.dao.search.SearchResults;
import com.bc.validators.Validator;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.FormUpdater;
import com.bc.web.core.form.HttpImageManager;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.components.UserBean;
import java.awt.Dimension;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Josh
 */
public class UpdateProduct extends DefaultUpdateformHandler<Product> {

    public UpdateProduct(String captchaParameterName, Dimension imageDimension) {
        super(captchaParameterName, imageDimension);
    }

    public UpdateProduct(
            Validator<Form> validator, Validator<HttpServletRequest> captchaValidator, 
            HttpImageManager imageManager, Dimension imageDimension, FormUpdater formUpdater) {
        super(validator, captchaValidator, imageManager, imageDimension, formUpdater);
    }
    
    @Override
    public void postUpdate(HttpServletRequest request) throws ServletException {

        super.postUpdate(request);
        
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        SearchResults<Product> searchResults = user.getProductSearchResults();
        
        if(searchResults != null) {
            // Since there has been updates, we re-load the search results
            searchResults.reset();
        }
    }
}
