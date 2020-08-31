package com.looseboxes.web.servlets;

import com.bc.imageutil.ImageDimensions;
import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.bc.validators.ValidationException;
import com.bc.validators.Validator;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.Form.ActionType;
import com.bc.web.core.form.FormUpdater;
import com.bc.web.core.form.FormUpdaterImpl;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.Listings;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.HasMessages;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.forms.Forms;
import com.looseboxes.web.form.LbImageManager;
import com.looseboxes.web.form.FormValidator;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.bc.jpa.context.JpaContext;

/**
 * @(#)UpdateForm.java   19-Jun-2015 15:46:51
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @param <E> The type/class of the entity of {@link com.looseboxes.web.components.forms.FormOld form} 
 * this servlet is designed to update
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class UpdateServletOld<E> extends BaseServlet {
    
    private Map<String, String> requestParameters;
    
    private Form<E> form;
    
    public abstract Class<E> getEntityClass();
    
    public abstract Form.ActionType getFormActionType();
    
    public abstract FormValidator.UserType getUserType();
    
    public abstract String getSuccessMessage();
    
    protected Form<E> getForm(HttpServletRequest request) throws ServletException {
        
        Form mForm = Forms.getEntityInstance(
                request, this.getEntityClass(), 
                this.getFormId(), this.getFormActionType(), 
                0, true);

        return mForm;
    }
    
    @Override
    public void init(HttpServletRequest request) throws ServletException {

Log.getInstance().log(Level.FINE, "#init. form action type: {0}", this.getClass(), this.getFormActionType());

        super.init(request);
        
        form = this.getForm(request);

Log xlog = Log.getInstance();
if(xlog.isLoggable(Level.FINER, this.getClass())) {
    xlog.log(Level.FINER, "Form: {0}", this.getClass(), form);
}else if(xlog.isLoggable(Level.FINE, this.getClass())) {
    xlog.log(Level.FINE, "Created Form:: ID: {0}, entity type: {1}, action type: {2}", this.getClass(), 
    form==null?null:form.getId(), form==null?null:form.getEntityClass().getName(), form==null?null:form.getType());
}
        
        if(form != null) {
            form.setAction(this.getFormAction(request));
        }

        Map<String, String> map = this.getParameters(request);
        
Log.getInstance().log(Level.FINE, "Parameters: {0}", this.getClass(), map);

        if(map == null || map.isEmpty()) {
            
            requestParameters = Collections.emptyMap();
            
        }else{
            
            requestParameters = Collections.unmodifiableMap(map);
            
            // FormOld details must be available immediately after initialization
            // 
            form.addUpdateDetails(requestParameters); 
        }
    }

    @Override
    public void destroy(HttpServletRequest request) throws ServletException {

Log.getInstance().log(Level.FINER, "#destroy. form action type: {0}", this.getClass(), this.getFormActionType());

        try{
            super.destroy(request);
        }finally{

            this.removeForm(request, form.getId());

            this.form = null;
            this.requestParameters = null;
        }
    }
    
    public void removeForm(HttpServletRequest request, String formId) {
Log.getInstance().log(Level.FINER, "Removing Form: {0}", this.getClass(), form);
        ServletUtil.removeAllAttributes(form.getId(), request);
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

Log.getInstance().log(Level.FINER, "#handleRequest. form action type: {0}", 
        this.getClass(), this.getFormActionType());

        this.format(request);
        
        this.validate(request);
        
        this.saveImages(request);

        int updateCount = this.update(request);

        if(updateCount == 1) {
            this.postUpdate(request);
        }
    }
    
    public void format(HttpServletRequest request) {
        
        this.format(this.form, this.isRecurseFormChildren());
    }
    
    public void format(Form form, boolean recurse) {

        // Add datecreated
        //
        this.format(form);
        
        if(recurse) {
            
            final Form [] children = form.getReferencedForms();

            if(children != null) {
                
                for(Form child:children) {

                    if(child != null) {
                        this.format(child, recurse);
                    }
                }
            }
        }
    }
    
    private void format(Form form) {
        
Log.getInstance().log(Level.FINER, "#format. Form ID: {0},  entity type: {1}, action type: {2}, stage: {3}", 
this.getClass(), form.getId(), form.getEntityClass().getName(), form.getType(), form.getStage());

        // Add datecreated
        //
        if(this.getFormActionType() == ActionType.INSERT) {
            
            JpaContext cf = WebApp.getInstance().getJpaContext();
            
            String [] cols = cf.getMetaData().getColumnNames(form.getEntityClass());
            
            if(indexOf(cols, Product_.datecreated.getName()) != -1) {
                
                Map toAdd = Collections.singletonMap(Product_.datecreated.getName(), new Date());
                
                // FormOld.getDetails may be null so we use this, which create the details if null
                //
                form.addUpdateDetails(toAdd);
            }
        }
    }
    
    public void validate(HttpServletRequest request) throws ServletException {

        this.validate(request, form, this.isRecurseFormChildren());
    }
    
    public void validate(HttpServletRequest request, Form form, boolean recurse) throws ServletException {

        this.validate(request, form);
        
        if(recurse) {
            
            Form [] children = form.getReferencedForms();
            
            if(children != null) {
                
                for(Form child:children) {

                    if(child != null) {
                        this.validate(request, child, recurse);
                    }
                }
            }
        }
    }

    private void validate(HttpServletRequest request, Form form) throws ServletException {
        
Log.getInstance().log(Level.FINER, "#validate. Form ID: {0},  entity type: {1}, action type: {2}, stage: {3}", 
this.getClass(), form.getId(), form.getEntityClass().getName(), form.getType(), form.getStage());
        
        Validator<Form> validator = this.getValidator(request);
        if(validator != null) {
            try{
                validator.validate(form); 
Log.getInstance().log(Level.FINE, "VALIDATION SUCCESSFUL. Parameters: {0}", this.getClass(), form.getDetails());
            }catch(ValidationException e) {
                String msg = e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Validation failed";
                throw new ServletException(msg, e);
            }
        }
    }
    
    public int saveImages(HttpServletRequest request) throws ServletException, IOException {
        
        return this.saveImages(request, form, this.isRecurseFormChildren());
    }    
        
    public int saveImages(HttpServletRequest request, Form form, boolean recurse) throws ServletException, IOException {
        
Log.getInstance().log(Level.FINER, "#saveImages. Form ID: {0},  entity type: {1}, action type: {2}, stage: {3}", 
this.getClass(), form.getId(), form.getEntityClass().getName(), form.getType(), form.getStage());

        String [] imageNames = Forms.getImageNames(form.getEntityClass());

        int saved = 0;
        
        if(imageNames != null && imageNames.length > 0) {
            
            saved += this.saveImages(request, imageNames);
        } 
        
        if(recurse) {
            
            Form [] children = form.getReferencedForms();
            
            if(children != null) {
                
                for(Form child:children) {

                    if(child != null) {
                        saved += this.saveImages(request, child, recurse);
                    }
                }
            }
        }
        
        return saved;
    }

    private int saveImages(HttpServletRequest request, String [] imageNames) {
        
Log.getInstance().log(Level.FINER, "Entity type: {0}, image names: {1}", 
this.getClass(), form.getEntityClass().getName(), imageNames==null?null:Arrays.toString(imageNames));
        
        if(imageNames == null) {
            return 0;
        }
        
        LbImageManager imgMgr = new LbImageManager(WebApp.getInstance());
        
///////////////////// DO WE SCALE IMAGES ??? And what size /////////////////////
//                    
        boolean scaleImage = false;

        Dimension dimension = scaleImage ? ImageDimensions.medium() : null;

        Object id = null; // We don't know the id as the product hasn't been inserted yet
        
        int saved = 0;
        for(String columnName:imageNames) {

            Part part;
            try{
                
                part = request.getPart(columnName);
                
Log.getInstance().log(Level.FINER, "Processing {0}", this.getClass(), columnName);

                if(part == null) {
                    continue;
                }
                
                if(saved == 0) {
                    // Save a logo for the first image
                    String path = imgMgr.saveImage(id, part, "logo", ImageDimensions.small());
                    if(path != null) {
                        this.getFormInputs().put("logo", path);
                    }
                }

                String path = imgMgr.saveImage(id, part, columnName, dimension);
                
                if(path != null) {
                    this.getFormInputs().put(columnName, path);
                    ++saved;
                }
            }catch(ServletException | IOException e) {
                String msg = "Failed to save "+columnName;
                this.addMessage(HasMessages.MessageType.warningMessage, msg);
                Log.getInstance().log(Level.WARNING, msg, this.getClass(), e);
            }
        }
        
        return saved;
    }
    
    public int update(HttpServletRequest request) throws ServletException {

        return this.getUpdater(request).update(form);
    }
    
    public void postUpdate(HttpServletRequest request) throws ServletException {

        String msg = this.getSuccessMessage();
        
Log.getInstance().log(Level.FINE, "Success message: {0}", this.getClass(), msg);

        if(msg != null) {
            this.addMessage(HasMessages.MessageType.informationMessage, msg);
        }
        
        try{
            switch(form.getType()) {
                case INSERT:
                case DELETE:
                    Listings listings = WebApp.getInstance().getListings(this.getEntityClass());
                    if(listings == null) return;
                    Map formInputs = this.getFormInputs();
Log.getInstance().log(Level.FINER, "Updating listings with form inputs: {0}", this.getClass(), formInputs);                
                    if(form.getType() == ActionType.INSERT) {
                        listings.increment(formInputs);
                    }else{
                        listings.decrement(formInputs);
                    }
            }
        }catch(SQLException ignored) {
            Log.getInstance().log(Level.WARNING, "Error updating listings", this.getClass(), ignored);
        }
    }
    
    public void checkNull(Form form, String columnName) throws ServletException {
        
        Map details = form.getDetails();

        if(!details.isEmpty()) {
            
            String [] columnNames = form.getColumnNames();

            if(Arrays.asList(columnNames).contains(columnName)) {

                Object columnValue = details.get(columnName);

                if(columnValue == null) {

                    throw new ServletException("Please enter a value for required input: "+form.getLabel(columnName));
                }
            }
        }
    }
    
    public String getFormAction(HttpServletRequest request) throws ServletException {
        return this.getClass().getAnnotation(WebServlet.class).urlPatterns()[0];
    }

    public Map getDatabaseParameters(Form form) {
        JpaContext cf = WebApp.getInstance().getJpaContext();
        return cf.getDatabaseFormat().toDatabaseFormat(form.getEntityClass(), form.getDetails());
    }
    
    private transient EntityController<E, Integer> ec_accessViaGetter;
    public EntityController<E, Integer> getEntityController() {
        if(ec_accessViaGetter == null) {
            ec_accessViaGetter = WebApp.getInstance().getJpaContext().getEntityController(this.getEntityClass(), Integer.class);    
        }
        return ec_accessViaGetter;
    }

    public boolean isNoAction(HttpServletRequest request) {
// @related formStages action        
        String action = request.getParameter("action");
        return "0".equals(action) || "none".equals("action");
    }

    public String getFormId() {
        return "DbAction";
    }
    
    public boolean isInStages() {
        return true;
    }
    
    public boolean isRecurseFormChildren() {
        return true;
    }
    
    public Validator<Form> getValidator(HttpServletRequest request) {
        return new FormValidator(this.getUserType());
    }
    
    public FormUpdater getUpdater(HttpServletRequest request) {
        return new FormUpdaterImpl(LbApp.getInstance().getJpaContext());
    }
    
    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    public Map getFormInputs() {
        return form == null ? null : form.getDetails();
    }

    public Form<E> getForm() {
        return form;
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

