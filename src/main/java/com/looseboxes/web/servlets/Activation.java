package com.looseboxes.web.servlets;

import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Userstatus;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.mail.ActivationSettings;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;


/**
 * @(#)Activation.java   25-May-2015 09:08:01
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
@WebServlet(name="Activation", urlPatterns={"/activation"})
public class Activation extends BaseServlet {

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.WELCOME;
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        ActivationSettings settings = new ActivationSettings();
        
        Map<String, String> params;
        
        try{
            params = settings.getDecryptedParameters(request);
        }catch(GeneralSecurityException e) {
            throw new ServletException("An unexpected error ocurred while processing the request", e);
        }
        
        Siteuser siteuser = this.getUser(params);
        
        if(siteuser == null) {
            
            throw new ServletException("Invalid activation details");
            
        }else{
            
            JpaContext cf = WebApp.getInstance().getJpaContext();
            EnumReferences refs = cf.getEnumReferences();
            
            Userstatus activated = (Userstatus)refs.getEntity(References.userstatus.Activated);
            siteuser.setUserstatusid(activated);
            
            try{
                
                EntityController<Siteuser, Integer> ec = cf.getEntityController(Siteuser.class, Integer.class);
                ec.merge(siteuser);

                new Login().login(request, params);
                
                this.addMessage(MessageType.informationMessage, "Welcome "+siteuser.getUsername()+". Your profile was successfully activated.");
                
            }catch(Exception e) {
                
                throw new ServletException("An unexpected error occured while processing the request", e);
            }    
        }
    }
    
    public Siteuser getUser(Map where) {
        
        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        
        try(Select<Siteuser> qb = jpaContext.getDaoForSelect(Siteuser.class)) {
            
            return qb.where(Siteuser.class, where).createQuery().getSingleResult();
            
        }catch(javax.persistence.NoResultException noNeedToLog) {
            
            return null;
        }
    }
}
