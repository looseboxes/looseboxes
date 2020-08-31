package com.looseboxes.web.components.forms;

import com.bc.util.Log;
import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.web.servlets.InsertProductServlet;
import java.util.logging.Level;
import javax.servlet.annotation.MultipartConfig;

/**
 * @author Josh
 */
public class ProductvariantForm extends BaseForm<Productvariant> {
    
    private final String [] fileNames;
    
    public ProductvariantForm(ActionType actionType) {
        super(actionType, Productvariant.class);
        this.fileNames = new String[]{Productvariant_.image1.getName(),
            Productvariant_.image2.getName(), Productvariant_.image3.getName(),
            Productvariant_.image4.getName(), Productvariant_.image5.getName(),
            Productvariant_.image6.getName(), Productvariant_.image7.getName()
        };
    }
    
    @Override
    protected com.bc.web.core.form.Form createReferenced(String columnName) {
        Form referenced;

        if(Productvariant_.productid.getName().equals(columnName)) {
            referenced = new ProductForm(this.getType());
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
}
