package com.looseboxes.web.servlets;

/**
 * @(#)InsertProduct.java   14-May-2015 01:16:32
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.validators.Validator;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.FormUpdater;
import com.bc.web.core.form.HttpImageManager;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.components.forms.InsertProductForm;
import com.looseboxes.web.formhandlers.InsertProduct;
import com.looseboxes.web.formhandlers.UpdateProduct;
import java.awt.Dimension;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
//@MultipartConfig(location="/tmp", fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*7)
@WebServlet(name="InsertProduct", urlPatterns={"/insert"})
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*7)
public class InsertProductServlet extends UpdateProductServlet {
    
    public InsertProductServlet() { }

    @Override
    protected Form<Product> createForm(HttpServletRequest request) throws ServletException {
        
        return new InsertProductForm();
    }

    @Override
    public UpdateProduct createUpdateHandler(
            Validator<Form> validator, Validator<HttpServletRequest> captchaValidator, 
            HttpImageManager imageManager, Dimension imageDimension, FormUpdater formUpdater) {
        
        return new InsertProduct(validator, captchaValidator, imageManager, imageDimension, formUpdater);
    }
}
