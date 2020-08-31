package com.looseboxes.web.servlets;

/**
 * @(#)EditProductvariant.java   01-Jul-2015 00:52:39
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @param <T>
 * @since    2.0
 */
public abstract class EditServlet<T> extends BaseServlet {
    
    private final Class<T> entityClass;
    
    private final String idColumnName;
    
    public EditServlet(Class<T> entityClass, String idColumnName) {
        this.idColumnName = idColumnName;
        this.entityClass = entityClass;
    }

    @Override
    protected void handleRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        Map<String, String> reqParams = this.getParameters(request);
        
        Edit edit = new EditStrictly();
        
        T entity = edit.execute(entityClass, reqParams, idColumnName);
    }
    
    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return this.getForwardPage(request);
    }
}
