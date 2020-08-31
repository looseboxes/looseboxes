package com.looseboxes.web.components.forms;

import com.bc.util.Log;
import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Address;
import com.looseboxes.pu.entities.Brand;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.servlets.InsertProductServlet;
import java.util.logging.Level;
import javax.servlet.annotation.MultipartConfig;

/**
 * @author Josh
 */
public class ProductForm extends BaseForm<Product> {
    
    private final String [] fileNames;
    
    public ProductForm(ActionType actionType) {
        super(actionType, Product.class);
        this.fileNames = new String[]{Product_.logo.getName()};
    }
    
    @Override
    protected com.bc.web.core.form.Form createReferenced(String columnName) {
        Form referenced;
        if(Product_.availableAtOrFrom.getName().equals(columnName)) {
            referenced = new SimpleFormImpl<>(this.getType(), Address.class);
        }else if(Product_.brandid.getName().equals(columnName)){
            referenced = new SimpleFormImpl<>(this.getType(), Brand.class);
        }else if(Product_.isRelatedTo.getName().equals(columnName)){
            referenced = new SingleFieldForm<>(this.getType(), Product.class, Product_.productid.getName());
        }else{    
            referenced = null;
        }
Log.getInstance().log(referenced!=null?Level.FINE:Level.FINER, 
"Column: {0}, referenced form: {1}", this.getClass(), columnName, referenced);
        return referenced;
    }

    @Override
    public int getMaxFileSize() {
        return (int)InsertProductServlet.class.getAnnotation(MultipartConfig.class).maxFileSize();
    }

    @Override
    public String[] getFileNames() {
        return this.fileNames;
    }

    @Override
    public boolean isFormField(String columnName) {
        boolean formField = super.isFormField(columnName);
        if(formField) {
            formField = !Product_.seller.getName().equals(columnName) &&
                    !Product_.views.getName().equals(columnName) &&
                    !Product_.ratingPercent.getName().equals(columnName);
        }
        return formField;
    }
}
