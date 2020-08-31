package com.looseboxes.web.servlets;

import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.Form.ActionType;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @(#)DeleteProduct.java   06-Jun-2015 18:43:38
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="DeleteProduct", urlPatterns={"/delete"})
public class DeleteProduct extends UpdateProductOld {
    
    private Map details;

    @Override
    public void init(HttpServletRequest request) throws ServletException {
        
        super.init(request);

        if(this.isBeforeFirstStage() || this.isFirstStage()) {
            
            String sval = request.getParameter(Product_.productid.getName());

            if(sval == null) {
                throw new ServletException("Invalid request");
            }

            Integer ival;
            try{
                ival = Integer.valueOf(sval);
            }catch(NumberFormatException e) {
                Log.getInstance().log(Level.WARNING, "Error parsing product ID string: "+sval, this.getClass(), e);
                throw new ServletException("Invalid Request");
            }

            EntityController<Product, Integer> ec = this.getEntityController();

            Product product = ec.find(ival);

            if(product == null) {
                throw new ServletException("The selected item is no longer available");
            }

            this.details = ec.toMap(product, false);

            this.setStage(1); // We skip stage 0 , as we don't need to display a form
        }
    }

    @Override
    public void destroy(HttpServletRequest request) throws ServletException {
        try{
            super.destroy(request); 
        }finally{
            this.details = null;
        }
    }
    
    @Override
    public void format(Form form, boolean isRecurseChildren) {
        
        super.format(form, isRecurseChildren);
        
        form.addUpdateDetails(this.details);
    }

    @Override
    public void validate(HttpServletRequest request) { }

    @Override
    public int saveImages(HttpServletRequest request) { return 0; }

    @Override
    public ActionType getFormActionType() {
        return ActionType.DELETE;
    }

    @Override
    public String getSuccessMessage() {
        return "DELETE SUCCESSFUL";
    }
}
/**
 * 
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
                    output = WebPages.CONFIRMATION;
                    break;
                case 1:
                    output = null; // Form completed
                    break;
                default: throw new IllegalArgumentException("Unexpected Delete Form Stage: "+stage);    
            }
XLogger.getInstance().log(Level.INFO, "Stage: {0}, forward page: {1}", this.getClass(), stage, output);
        }else{
            output = null; // Signals to this form to use the default
        }
        return output;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
XLogger.getInstance().log(Level.FINER, "#getErrorPage. stage: {0}", this.getClass(), this.getStage());
        
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
                    output = WebPages.CONFIRMATION;
                    break;
                default: throw new IllegalArgumentException("Unexpected Delete Form Stage: "+stage);    
            }
XLogger.getInstance().log(Level.FINER, "Stage: {0}, error page: {1}", this.getClass(), stage, output);
        }else{
            output = null; // Signals to this form to use the default
        }
        return output;
    }
    
 * 
 */