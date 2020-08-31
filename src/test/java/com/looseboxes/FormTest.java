package com.looseboxes;

import com.bc.web.core.form.Form;
import com.looseboxes.pu.entities.Address;
import com.looseboxes.web.components.forms.Forms;
import com.looseboxes.web.servlets.Chat;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;


/**
 * @(#)FormTest.java   13-May-2015 19:52:45
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
public class FormTest extends BaseServletTest {

    public FormTest() {
        this.setBaseServlet(new Chat());
    }

    @Test
    @Override
    public void testServlet() throws ServletException, IOException {

        Class servletClass = this.getBaseServlet().getClass();
        
        HttpServletRequest request = this.getRequest(servletClass, null);
        
        Form<Address> form = Forms.getEntityInstance(request, Address.class, "AddressForm");
        
        String [] cols = form.getFieldNames();
System.out.println(this.getClass().getName()+". Field names: "+(cols==null?null:Arrays.toString(cols)));            
        
        for(String col:cols) {
            int nullable = form.getNullable(col);
System.out.println(this.getClass().getName()+". col: "+col+", nullable: "+nullable);            
        }
System.out.println(this.getClass().getName()+". COMPLETED");        
    }
}
