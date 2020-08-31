package com.looseboxes.web.servlets;

import com.bc.jpa.dao.search.SearchResults;
import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.components.forms.Forms;
import com.looseboxes.web.form.FormValidator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @(#)UpdateProduct.java   06-Jun-2015 18:53:28
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class UpdateProductOld extends UpdateFormStage<Product> {
    
    private Form<Productvariant> productVariantForm;

    @Override
    protected String getFormPage() {
        return WebPages.FORM;
    }

    @Override
    protected String getConfirmationPage() {
       return WebPages.CONFIRM_FORM_ENTRIES;
    }

    @Override
    public void init(HttpServletRequest request) throws ServletException {
        
        super.init(request); 
        
        Form<Product> form = this.getForm();
        
        if(form.getReferencedForms() == null || form.getReferencedForms().length == 0) {

            productVariantForm = Forms.getEntityInstance(
            request, Productvariant.class, "ProductvariantForm", 
                    form.getType(), form.getStageCount(), true);
            
            productVariantForm.setAction(this.getFormAction(request));
   
//@todo            
//            form.addChild(productVariantForm);
            if(true) {
                throw new UnsupportedOperationException("Find a solution for commented out block above");
            }
        }
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
    
    @Override
    public Class<Product> getEntityClass() {
        return Product.class;
    }

    @Override
    public FormValidator.UserType getUserType() {
        return FormValidator.UserType.existingUser;
    }
    

    public Form<Productvariant> getProductVariantForm() {
        return productVariantForm;
    }

    public void setProductVariantForm(Form<Productvariant> productVariantForm) {
        this.productVariantForm = productVariantForm;
    }
}
