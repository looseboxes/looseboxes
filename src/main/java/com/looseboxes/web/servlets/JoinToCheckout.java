package com.looseboxes.web.servlets;

/**
 * @(#)JoinToCheckout.java   18-Jul-2015 10:41:21
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.validators.ValidationException;
import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Siteuser;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="JoinToCheckout", urlPatterns={"/joinToCheckout"})
public class JoinToCheckout extends LoginToCheckout {
   
    @Override
    public UserType getUserType(HttpServletRequest request) {
        return UserType.newUser;
    }
    
    @Override
    public void validate(HttpServletRequest request, Form<Siteuser> form, Map<String, String> params) throws ServletException {
        
        try{
            
            this.getValidator(request, form, UserType.newUser).validate(form);
            
        }catch(ValidationException e_1) {
            
            try{
                
                this.getValidator(request, form, UserType.existingUser).validate(form);
                
                this.setUserType(UserType.existingUser);
                
            }catch(ValidationException ignored) {
                String msg = e_1.getLocalizedMessage() == null ? "Invalid login details" : e_1.getLocalizedMessage();
                throw new ServletException(msg, e_1);
            }
        }
    }
}
