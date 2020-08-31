package com.looseboxes.web.servlets;

/**
 * @(#)AddReference.java   15-May-2015 11:32:12
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.util.Log;
import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.web.core.form.Form.ActionType;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import java.io.IOException;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import com.looseboxes.web.components.forms.FormOld;
import com.bc.jpa.context.JpaContext;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="AddReference", urlPatterns={"/addref"})
public class AddReference extends UpdateFormStage {
    
   private FormOld mainForm;
    
    private String columnName;
    
    private Class referenceClass;

    @Override
    protected String getFormPage() {
        return WebPages.AUXILLARY_FORM;
    }

    @Override
    protected String getConfirmationPage() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int getStageCount() {
        return 1;
    }
    
    @Override
    public void init(HttpServletRequest request) throws ServletException {

        this.mainForm = (FormOld)ServletUtil.find(this.getFormId(), request);

        if(this.mainForm.getStage() == 0) {
            
            this.columnName = request.getParameter("name");

            JpaContext cf = WebApp.getInstance().getJpaContext();

            this.referenceClass = cf.getMetaData().getReferenceClass(this.mainForm.getEntityClass(), columnName);

            // These are required for super.init to work
            //
            if(mainForm == null || columnName == null || referenceClass == null) {
                throw new NullPointerException();
            }
        }
        
        super.init(request); 
    }
    
    @Override
    public void destroy(HttpServletRequest request) throws ServletException {

        try{
            super.destroy(request); 
        }finally{

            this.mainForm = null;

            this.columnName = null;

            this.referenceClass = null;
        }
    }

    @Override
    public void processStage(HttpServletRequest request)
            throws ServletException, IOException {

        int stage = this.getStage();
        
Log.getInstance().log(Level.FINER, "#processStage. form action type: {0}, stage: {1}", this.getClass(), this.getFormActionType(), stage);
        
        switch(stage) {
            case 0: 
                break;
            case 1: 
                this.format(request);
                this.validate(request);
                this.saveImages(request);
                int updateCount = this.update(request);
                if(updateCount == 1) {
                    this.postUpdate(request);
                }
                break;
            default: throw new IllegalArgumentException("Unexpected Update Form stage: "+stage);     
        }
    }
    
    @Override
    public Class getEntityClass() {
        return referenceClass;
    }

    @Override
    public ActionType getFormActionType() {
        return ActionType.INSERT;
    }

    @Override
    public UserType getUserType() {
        return UserType.existingUser;
    }
    
    @Override
    public String getSuccessMessage() {
        String label = mainForm.getFormField(columnName, null).getLabel();
        return label + " Details Added";
    }

    @Override
    public String getFormId() {
        return "AddReference";
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.AUXILLARY_FORM;
    }
}
