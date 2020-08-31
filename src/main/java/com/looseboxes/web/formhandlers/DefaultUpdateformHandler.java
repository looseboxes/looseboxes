package com.looseboxes.web.formhandlers;

import com.bc.util.Log;
import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.validators.Validator;
import com.bc.web.core.form.Form;
import static com.bc.web.core.form.Form.ActionType.DELETE;
import static com.bc.web.core.form.Form.ActionType.INSERT;
import com.bc.web.core.form.FormUpdater;
import com.bc.web.core.form.FormUpdaterImpl;
import com.bc.web.core.form.HttpImageManager;
import com.bc.web.core.form.UpdatemultistagedformHandler;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.Listings;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.form.DefaultImageManager;
import com.looseboxes.web.form.FormValidator;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import com.bc.jpa.context.JpaContext;

/**
 * @author Josh
 */
public class DefaultUpdateformHandler<E> extends UpdatemultistagedformHandler<E> {
    
    public DefaultUpdateformHandler(String captchaParameterName, Dimension imageDimension) {
        super(new FormValidator(UserType.existingUser), captchaParameterName, 
                new DefaultImageManager(), imageDimension, 
                new FormUpdaterImpl(LbApp.getInstance().getJpaContext()));
    }

    public DefaultUpdateformHandler(
            Validator<Form> validator, Validator<HttpServletRequest> captchaValidator, 
            HttpImageManager imageManager, Dimension imageDimension, FormUpdater formUpdater) {
        
        super(validator, captchaValidator, imageManager, imageDimension, formUpdater);
    }

    @Override
    protected void doFormat(Form form) {
//XLogger.getInstance().log(Level.FINER, "#format. Form ID: {0},  entity type: {1}, action type: {2}, stage: {3}", 
//this.getClass(), form.getId(), form.getEntityClass().getName(), form.getType(), form.getStage());

        // Add datecreated
        //
        
        if(form.getType() == com.bc.web.core.form.Form.ActionType.INSERT) {
            
            JpaContext cf = WebApp.getInstance().getJpaContext();
            
            String [] cols = cf.getMetaData().getColumnNames(form.getEntityClass());
            
            if(indexOf(cols, Product_.datecreated.getName()) != -1) {
                
                Map toAdd = Collections.singletonMap(Product_.datecreated.getName(), new Date());
                
                // Form.getDetails may be null so we use this, which create the details if null
                //
                form.addUpdateDetails(toAdd);
            }
        }
    }

    @Override
    public void postUpdate(HttpServletRequest request) throws ServletException {

        super.postUpdate(request);
        
        try{
            Form<E> form = this.getForm();
            switch(form.getType()) {
                case INSERT:
                case DELETE:
                    Listings listings = WebApp.getInstance().getListings(form.getEntityClass());
                    if(listings == null) return;
                    Map formInputs = this.getFormInputs();
Log.getInstance().log(Level.FINER, "Updating listings with form inputs: {0}", this.getClass(), formInputs);                
                    if(form.getType() == com.bc.web.core.form.Form.ActionType.INSERT) {
                        listings.increment(formInputs);
                    }else{
                        listings.decrement(formInputs);
                    }
            }
        }catch(SQLException ignored) {
            Log.getInstance().log(Level.WARNING, "Error updating listings", this.getClass(), ignored);
        }
    }
    
    private int indexOf(Object [] arr, String o) {
        int output = -1;
        int index = 0;
        for(Object e:arr) {
            if(e.equals(o)) {
                output = index;
                break;
            }
            ++index;
        }
        return output;
    }
}
