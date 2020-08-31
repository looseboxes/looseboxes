package com.looseboxes.web.servlets;

/**
 * @(#)EditProductvariant.java   01-Jul-2015 00:52:39
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="EditProductvariant", urlPatterns={"/editvariant"})
public class EditProductvariant extends EditServlet<Productvariant> {
    
    public EditProductvariant() {
        super(Productvariant.class, Productvariant_.productvariantid.getName());
    }
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return "/products/productvariants.jsp";
    }
}
