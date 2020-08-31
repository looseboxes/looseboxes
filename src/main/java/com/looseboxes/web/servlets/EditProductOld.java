package com.looseboxes.web.servlets;

import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.Form.ActionType;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productvariant;
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
@WebServlet(name="EditProduct_xxxxxxxxxxxxxxxxxxxxxxOld", urlPatterns={"/edit_xxxxxxxxxxxxxxxxxxxOld"})
@MultipartConfig(fileSizeThreshold=1024*1024, maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*7)
public class EditProductOld extends UpdateProductOld {

    @Override
    public void format(HttpServletRequest request) {
        
        super.format(request);
        
        Map params = this.getFormInputs();

        Map selected = this.getForm().getSelectedDetails();

Log.getInstance().log(Level.FINE, "Selected: {0}", this.getClass(), selected);
            
        if(selected == null) {
            
            Object oval = params.get(Product_.productid.getName());
            
            if(oval == null) {
                throw new NullPointerException();
            }
            
            Integer productId = Integer.valueOf(oval.toString());
            
            EntityController<Product, Integer> ec = this.getEntityController();
            
            Product entity = ec.find(productId);
            
            Map entityMap = ec.toMap(entity, false);

            this.getForm().addSelectedDetails(entityMap);

Log.getInstance().log(Level.FINE, "For update: {0}", this.getClass(), this.getForm().getSelectedDetails());
            
            Form<Productvariant> productVariantForm = this.getProductVariantForm();
            
            if(productVariantForm != null) {
                productVariantForm.addSelectedDetails(entityMap);
            }
        }
    }

    @Override
    public ActionType getFormActionType() {
        return ActionType.EDIT;
    }

    @Override
    public String getSuccessMessage() {
        return "UPDATE SUCCESSFUL";
    }
}    
