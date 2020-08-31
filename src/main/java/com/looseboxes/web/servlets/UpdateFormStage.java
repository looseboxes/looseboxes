package com.looseboxes.web.servlets;

import com.bc.util.Log;
import com.bc.web.core.form.Form;
import com.looseboxes.web.components.forms.Forms;
import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @(#)UpdateServlet.java   12-May-2015 08:56:14
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
/**
 * @param <E>
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class UpdateFormStage<E> extends UpdateServletOld<E> {
    
    protected abstract String getFormPage();
    
    protected abstract String getConfirmationPage();

    @Override
    protected Form<E> getForm(HttpServletRequest request) throws ServletException {
        
        Form mForm = Forms.getEntityInstance(
                request, this.getEntityClass(), 
                this.getFormId(), this.getFormActionType(), 
                2, true);

        return mForm;
    }
    
    protected int getStage(HttpServletRequest request) {
        String sval = request.getParameter("stage");
Log.getInstance().log(Level.FINE, "HttpServletRequest.getParameter('Stage'): {0}", this.getClass(), sval);
        return sval == null ? 0 : Integer.parseInt(sval);
    }
    
    @Override
    public void init(HttpServletRequest request) throws ServletException {

        super.init(request);
        
        int stage = this.getStage(request);
        
        this.setStage(stage);
    }

    @Override
    public void removeForm(HttpServletRequest request, String formId) {
        if(this.isLastStage()) {
            super.removeForm(request, formId);
        }
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        String output;
        if(this.isInStages()) {
            
            // page of 'null' implies: Go back to where you came from 
            //
            
            int stage = this.getStage();
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
                    output = null; // FormOld completed
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
Log.getInstance().log(Level.FINER, "#getErrorPage. stage: {0}", this.getClass(), this.getStage());
        
        String output;
        if(this.isInStages()) {
            
            // page of 'null' implies: Go back to where you came from 
            //
            
            int stage = this.getStage();
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
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

Log.getInstance().log(Level.FINER, "#handleRequest. form action type: {0}, stage: {1}", 
        this.getClass(), this.getFormActionType(), this.getStage());

        if(!this.isNoAction(request)) {

            this.processStage(request);
        }
    }
    
    /**
     * @param request The servlet request to process
     * @throws ServletException
     * @throws IOException 
     */
    public void processStage(HttpServletRequest request)
            throws ServletException, IOException {

        final int stage = this.getStage();
        
Log.getInstance().log(Level.FINER, "#processStage. form action type: {0}, stage: {1}", 
        this.getClass(), this.getFormActionType(), stage);
        
        if(!this.isInStages()) {
            throw new UnsupportedOperationException("Cannot call this method for an update form which does not processe action in stages");
        }
        
        switch(stage) {
            case -1: // -1 == cancel
            case 0: 
                break;
            case 1: 
                this.format(request);
                this.validate(request);
                this.saveImages(request);
                break;
            case 2: 
                this.format(request);
                int updateCount = this.update(request);
                if(updateCount == 1) {
                    this.postUpdate(request);
                }
                break;
            default: throw new IllegalArgumentException("Unexpected Update Form stage: "+stage);     
        }
    }
    
    public int getStage() {
        return this.getForm() == null ? -1 : this.getForm().getStage();
    }

    public void setStage(int stage) {
Log.getInstance().log(Level.FINE, "Updating stage from {0} to {1}", this.getClass(), getStage(), stage);
        if(this.getForm() != null) {
            this.getForm().setStage(stage);
        }
    }
    
    public boolean isBeforeFirstStage() {
        return this.getStage() == -1;
    }
    
    public boolean isFirstStage() {
        return this.getStage() == 0;
    }
    
    public boolean isLastStage() {
        return this.getStage() == this.getStageCount();
    }

    public int getStageCount() {
        return this.getForm() == null ? 0 : this.getForm().getStageCount();
    }
}
