package com.looseboxes.web.servlets;

import com.bc.validators.AbstractDatabaseInputValidator;
import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.validators.ValidationException;
import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Siteuser;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;


/**
 * @(#)Joinx.java   19-Aug-2015 17:41:22
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * Provides logic for a new user to join the website. However, in this case the 
 * various stages i.e <tt>validation, image upload, confirmation and update etc</tt> 
 * are all coalesced into one single stage. It is thus faster.
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Joinx", urlPatterns={"/joinx"})
public class Joinx extends Loginx {
   
    @Override
    public AbstractDatabaseInputValidator.UserType getUserType(HttpServletRequest request) {
        return AbstractDatabaseInputValidator.UserType.newUser;
    }
    
    @Override
    public void validate(HttpServletRequest request, Form<Siteuser> form, Map<String, String> params) throws ServletException {
        
        try{
            
            this.getValidator(request, form, UserType.newUser).validate(form);
            
        }catch(ValidationException e_1) {
            
            try{
                
                this.getValidator(request, form, UserType.existingUser).validate(form);
                
                this.setUserType(AbstractDatabaseInputValidator.UserType.existingUser);
                
            }catch(ValidationException ignored) {
                String msg = e_1.getLocalizedMessage() == null ? "Invalid user details" : e_1.getLocalizedMessage();
                throw new ServletException(msg, e_1);
            }
        }
    }
}
