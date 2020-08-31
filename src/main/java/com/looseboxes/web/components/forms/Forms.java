package com.looseboxes.web.components.forms;

import com.bc.web.core.form.Form;
import com.bc.web.core.form.Form.ActionType;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.ServletUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


/**
 * @(#)Forms.java   01-May-2015 14:40:40
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
public class Forms {
    
    public static <E extends Form> E getInstance(
            HttpServletRequest request, Class<E> formType, String id, boolean create) 
            throws ServletException {
        
        if(id == null) {
            id = getId(formType);
        }
        
        E form = (E)ServletUtil.find(id, request);
        
        if(form == null && create) {
            try{
                form = formType.getConstructor().newInstance();
            }catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                form = null;
            }
            if(form == null) {
                try{
                    form = formType.getConstructor(String.class).newInstance(id);
                }catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new IllegalArgumentException("Form type: "+formType, e);
                }
            }
            // If id not yet set, then reflexively call method #setId(String) if available
            //
            if(form.getId() == null) {
                try{
                    Method method = form.getClass().getMethod("setId", (Class[])null);
                    method.invoke(form, id);
                }catch(NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    throw new IllegalArgumentException("Form type: "+formType, e);
                }
            }
            // Remove the previous
            ServletUtil.removeAllAttributes(id, request);
            form.setRequest(request);
        }
        
        return form;
    }

    public static <E> Form<E> getEntityInstance(
            HttpServletRequest request, Class<E> entityClass, String id) 
            throws ServletException {
     
        return Forms.getEntityInstance(request, entityClass, id, null, 0, true);
    }
    
    public static <E> Form<E> getEntityInstance(
            HttpServletRequest request, Class<E> entityClass, String id, 
            ActionType actionType, int stageCount, boolean create) 
            throws ServletException {
        
        Form<E> form = (Form<E>)ServletUtil.find(id, request);
        
        if(create) {
            
            if(form == null || form.getEntityClass() != entityClass ||
                    form.getType() != actionType || form.getStageCount() != stageCount) {
                
                DefaultForm formBean = new DefaultForm(actionType, id, entityClass);
                
                // This must be set only once after creation
                //
                formBean.setRequest(request);
                
                form = formBean;
            }
        }
        
        return form;
    }

    public static Form removeInstance(HttpServletRequest request, String id) {
        Form form = (Form)ServletUtil.find(id, request);
        ServletUtil.removeAllAttributes(id, request);
        return form;
    }
    
    private static class DefaultForm<E> extends BaseForm<E> {

        public DefaultForm(ActionType actionType, Class<E> entityClass) {
            super(actionType, entityClass);
        }

        public DefaultForm(ActionType actionType, String id, Class<E> entityClass) {
            super(actionType, id, entityClass);
        }

        @Override
        protected Form createReferenced(String columnName) {
            return null;
        }

        @Override
        public int getMaxFileSize() {
            return 5_000_000;
        }

        @Override
        public String[] getFileNames() {
            return Forms.getFileNames(this.getEntityClass());
        }
    }
    
    private static String getId(Class formType) {
        return formType.getSimpleName();
    }

    public static String [] getFileNames(Class entityClass) {
        if(entityClass == Product.class) {
            return new String[]{
                Product_.logo.getName()
            };
        }else if(entityClass == Productvariant.class) {
            return new String[]{
                Productvariant_.image1.getName(), Productvariant_.image2.getName(),
                Productvariant_.image3.getName(), Productvariant_.image4.getName(),
                Productvariant_.image5.getName(), Productvariant_.image6.getName(),
                Productvariant_.image7.getName()            
            };
        }else if(entityClass == Siteuser.class) {
            return new String[]{
                Siteuser_.logo.getName(), 
                Siteuser_.image1.getName(), Siteuser_.image2.getName(),
                Siteuser_.image3.getName()
            };
        }else{
            return null;
        }
    }
    
    public static String [] getImageNames(Class entityClass) {
        if(entityClass == Productvariant.class) {
            return new String[]{
                Productvariant_.image1.getName(), Productvariant_.image2.getName(),
                Productvariant_.image3.getName(), Productvariant_.image4.getName(),
                Productvariant_.image5.getName(), Productvariant_.image6.getName(),
                Productvariant_.image7.getName()            
            };
        }else if(entityClass == Siteuser.class) {
            return new String[]{
                Siteuser_.image1.getName(), Siteuser_.image2.getName(),
                Siteuser_.image3.getName()
            };
        }else{
            return null;
        }
    }
}
