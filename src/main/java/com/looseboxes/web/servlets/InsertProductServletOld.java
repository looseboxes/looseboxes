package com.looseboxes.web.servlets;

/**
 * @(#)InsertProduct.java   14-May-2015 01:16:32
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.util.Log;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.Form.ActionType;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.web.components.UserBean;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
//@MultipartConfig(location="/tmp", fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*7)
@WebServlet(name="InsertProduct_xxxxxxxxxxxxxxxxxxxxxOld", urlPatterns={"/insert_xxxxxxxxxxxxxxxold"})
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*7)
public class InsertProductServletOld extends UpdateProductOld {

    @Override
    public void format(HttpServletRequest request) {
        
        super.format(request);
        
        Map params = this.getFormInputs();
        
Log.getInstance().log(Level.FINER, "Parameters: {0}", this.getClass(), params);
        
        if(params.get(Product_.availabilityid) == null) {
            
            UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
            
            params.put(Product_.availabilityid.getName(), References.availability.InStock);
            params.put(Product_.currencyid.getName(), user.getCurrencyEntity());
            params.put(Product_.minimumOrderQuantity.getName(), 1);
            params.put(Product_.productstatusid.getName(), References.productstatus.New);
            params.put(Product_.seller.getName(), user.getDetails());

Log.getInstance().log(Level.FINER, "AFTER Parameters: {0}", this.getClass(), params);
            
            Form<Productvariant> productVariantForm = this.getProductVariantForm();
            
            if(productVariantForm != null) {
                Map variantParams = Collections.singletonMap(Productvariant_.quantityInStock.getName(), 1);
                productVariantForm.addUpdateDetails(variantParams);
            }
        }
    }

    @Override
    public ActionType getFormActionType() {
        return ActionType.INSERT;
    }

    @Override
    public String getSuccessMessage() {
        return "UPLOAD SUCCESSFUL";
    }
}
