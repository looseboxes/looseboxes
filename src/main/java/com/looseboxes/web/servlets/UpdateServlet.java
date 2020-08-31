package com.looseboxes.web.servlets;

import com.bc.util.Log;
import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.validators.Validator;
import com.bc.web.core.captcha.CaptchaFactory;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.FormUpdater;
import com.bc.web.core.form.FormUpdaterImpl;
import com.bc.web.core.form.UpdatemultistagedformHandler;
import com.bc.web.core.form.HttpImageManager;
import com.bc.web.core.util.ServletUtil;
import com.looseboxes.core.LbApp;
import com.looseboxes.web.Attributes;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.form.DefaultImageManager;
import java.awt.Dimension;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.looseboxes.web.components.forms.FormFieldOld;
import com.looseboxes.web.form.FormValidator;
import com.looseboxes.web.formhandlers.DefaultUpdateformHandler;
import javax.servlet.http.HttpSession;

/**
 * @author Josh
 * @param <E> The type of the entity this servlet may update
 */
public abstract class UpdateServlet<E> extends BaseServlet  {

    public UpdateServlet() { }
    
    protected abstract Form<E> createForm(HttpServletRequest request) throws ServletException;

    protected Dimension getImageTargetDimension() {
        return null; // This means don't resize images
    }
    
    protected Validator<Form> createFormParametersValidator() {
        return new FormValidator(UserType.existingUser);
    }
    
    protected Validator<HttpServletRequest> createCaptchaValidator() {
        return CaptchaFactory.createValidator(this.getCaptchaParameterName());
    }
    
    protected HttpImageManager createImageManager() {
        return new DefaultImageManager();
    }
    
    protected FormUpdater createFormUpdater() {
        return new FormUpdaterImpl(LbApp.getInstance().getJpaContext());
    }
    
    public String getCaptchaParameterName() {
        return FormFieldOld.WebOnlyField.userCaptchaInput.name();
    }
    
    public UpdatemultistagedformHandler<E> createUpdateHandler(
            Validator<Form> validator, Validator<HttpServletRequest> captchaValidator, 
            HttpImageManager imageManager, Dimension imageDimension, FormUpdater formUpdater) {
        
        return new DefaultUpdateformHandler(
                validator, captchaValidator, imageManager, imageDimension, formUpdater);
    }
    
    public String getFormPage() {
        return WebPages.FORM;
    }

    public String getConfirmationPage() {
        return WebPages.CONFIRM_FORM_ENTRIES;
    }

    protected Form initForm(UpdatemultistagedformHandler<E> updateHandler, HttpServletRequest request) throws ServletException {
        
        final String formIdAttributeName = "UpdateServlet.currentFormId";
        
        final String formIdAttributeValue = (String)ServletUtil.find(formIdAttributeName, request);
        
        com.bc.web.core.form.Form<E> form = formIdAttributeValue == null ? null : 
                (com.bc.web.core.form.Form<E>)ServletUtil.find(formIdAttributeValue, request);
        
        if(form == null && (updateHandler.isBeforeFirstStage() || updateHandler.isFirstStage())) {
            
            form = this.createForm(request);

            // This must be set once, immediately after creation
            form.setRequest(request);

            ServletUtil.removeAllAttributes(Attributes.CURRENT_UPDATE_FORM, request);
            ServletUtil.removeAllAttributes(formIdAttributeName, request);
            
            HttpSession session = request.getSession();
            session.setAttribute(Attributes.CURRENT_UPDATE_FORM, form);
            session.setAttribute(formIdAttributeName, form.getId());
        }

Log xlog = Log.getInstance();
if(xlog.isLoggable(Level.FINER, this.getClass())) {
    xlog.log(Level.FINER, "Form: {0}", this.getClass(), form);
}else if(xlog.isLoggable(Level.FINE, this.getClass())) {
    xlog.log(Level.FINE, "Form:: ID: {0}, entity type: {1}, action type: {2}", this.getClass(), 
    form==null?null:form.getId(), form==null?null:form.getEntityClass().getName(), form==null?null:form.getType());
}
        
        if(form != null) {
            
            if(!form.isHideCaptcha()) {

                CaptchaFactory.createGenerator().generate(request);
            }

            form.setAction(this.getFormAction(request));
            
            form.setStage(this.getStage(request));
        
            Map<String, String> requestParams = this.getParameters(request);
        
xlog.log(Level.FINE, "Request Parameters: {0}", this.getClass(), requestParams);

            if(requestParams != null && !requestParams.isEmpty()) {

                // Form details must be available immediately after initialization
                // 
                form.addUpdateDetails(requestParams); 
            }
        }
        
        return form;
    }
    
    @Override
    public void destroy(HttpServletRequest request) throws ServletException {
        
        super.destroy(request); 
        
        UpdatemultistagedformHandler<E> updateHandler = this.getUpdateHandler(false);
        
        if(updateHandler != null) {
            
            if(!updateHandler.isInStages() || updateHandler.isLastStage()) {

                Form<E> form = updateHandler.getForm();

Log.getInstance().log(Level.FINE, "Removing Form: {0}", this.getClass(), form==null?null:form.getId());

                if(form != null) {

                    ServletUtil.removeAllAttributes(form.getId(), request);
                }
            }
        }
    }

    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UpdatemultistagedformHandler<E> updateHandler = this.getUpdateHandler(true);
        
        Form<E> form = this.initForm(updateHandler, request);
        
        updateHandler.execute(form, request);
    }
    
    protected int getStage(HttpServletRequest request) {
        String sval = request.getParameter("stage");
Log.getInstance().log(Level.FINE, "HttpServletRequest.getParameter('Stage'): {0}", this.getClass(), sval);
        return sval == null ? 0 : Integer.parseInt(sval);
    }

    public String getFormAction(HttpServletRequest request) throws ServletException {
        return this.getClass().getAnnotation(WebServlet.class).urlPatterns()[0];
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        
        UpdatemultistagedformHandler<E> updateHandler = this.getUpdateHandler(false);
        
        String output;
        if(updateHandler != null && updateHandler.isInStages()) {
            
            final int stage = updateHandler.getStage();
            
            // page of 'null' implies: Go back to where you came from 
            //
            switch(stage) {
                case -1:
                    output = null; // -1= = cancel
                    break;
                case 0:
                    output = this.getFormPage();
                    break;
                case 1:
                    output = this.getConfirmationPage();
                    break;
                case 2:
                    output = null; // Form completed
                    break;
                default: throw new IllegalArgumentException("Unexpected Update Form Stage: "+stage);    
            }
Log.getInstance().log(Level.FINE, "Stage: {0}, forward page: {1}", this.getClass(), stage, output);
        }else{
            output = null; // Signals to this form to use the default
        }
        return output;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        
        UpdatemultistagedformHandler<E> updateHandler = this.getUpdateHandler(false);
        
        String output;
        if(updateHandler != null && updateHandler.isInStages()) {
            
            final int stage = updateHandler.getStage();
            
            // page of 'null' implies: Go back to where you came from 
            //
            switch(stage) {
                case -1:
                    output = null; // -1= = cancel
                    break;
                case 0:
                    output = null; 
                    break;
                case 1:
                    output = this.getFormPage();
                    break;
                case 2:
                    output = this.getConfirmationPage();
                    break;
                default: throw new IllegalArgumentException("Unexpected Update Form Stage: "+stage);    
            }
Log.getInstance().log(Level.FINE, "Stage: {0}, error page: {1}", this.getClass(), stage, output);
        }else{
            output = null; // Signals to this form to use the default
        }
        return output;
    }

    private UpdatemultistagedformHandler<E> _update_handler;
    
    public UpdatemultistagedformHandler<E> getUpdateHandler(boolean create) {
        
        if(_update_handler == null && create) {
            
            _update_handler = this.createUpdateHandler(
                    this.createFormParametersValidator(), 
                    this.createCaptchaValidator(), 
                    this.createImageManager(), 
                    this.getImageTargetDimension(), 
                    this.createFormUpdater());

            Log.getInstance().log(Level.FINE, "Created Update Handler: {0}", 
                    this.getClass(), _update_handler);
        }
        
        return _update_handler;
    }
}
