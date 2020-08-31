package com.looseboxes.web.servlets;

import com.bc.jpa.controller.EntityController;
import com.bc.jpa.exceptions.EntityInstantiationException;
import com.bc.jpa.exceptions.NonexistentEntityException;
import com.bc.jpa.exceptions.PreexistingEntityException;
import com.bc.util.Log;
import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.web.core.form.Form.ActionType;
import com.looseboxes.pu.Listings;
import com.looseboxes.web.WebApp;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import com.bc.jpa.context.JpaContext;


/**
 * @(#)AddAuxillaryDetails.java   12-May-2015 16:20:02
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @param <REFING>
 * @param <REF>
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class AddReferenceToExisting<REFING, REF> extends UpdateServletOld<REF> implements Serializable {
    
    public abstract Class<REFING> getReferencingEntityClass();

    public abstract String getReferencingColumnName(HttpServletRequest request) throws ServletException;
    
    public abstract Integer getReferencingColumnValue(HttpServletRequest request) throws ServletException;

    @Override
    public ActionType getFormActionType() {
        return ActionType.INSERT;
    }
    
    @Override
    public void postUpdate(HttpServletRequest request) throws ServletException {
        
        super.postUpdate(request);
        
        try{

            Listings listings = WebApp.getInstance().getListings(this.getEntityClass());

            if(listings == null) return;

Log.getInstance().log(Level.FINER, "Update parameters: {0}", this.getClass(), this.getFormInputs());                

            listings.increment(this.getFormInputs());
            
        }catch(SQLException ignored) {
            Log.getInstance().log(Level.WARNING, "Error upating listings", this.getClass(), ignored);
        }
    }
    
    public boolean isOneToOneRelationship() {
        return false;
    }
    
    @Override
    public UserType getUserType() {
        return UserType.existingUser;
    }

    @Override
    public String getSuccessMessage() {
        return "UPDATE SUCCESSFUL";
    }

    @Override
    public int update(HttpServletRequest request) throws ServletException {
        
        try{
            
            return this.doUpdate(request);
            
        }catch(RuntimeException e) {
            
            throw e;
            
        }catch(Exception e) {
            
            throw new ServletException(e);
        }
    }    
    private int doUpdate(HttpServletRequest request) 
            throws EntityInstantiationException, 
            PreexistingEntityException,
            NonexistentEntityException,
            Exception {

        JpaContext cf = WebApp.getInstance().getJpaContext();
        
        EntityController<REFING, Integer> refingCtrl = 
                cf.getEntityController(this.getReferencingEntityClass(), Integer.class);

        // First ensure we have this
        //
//        final String refingColName = refingCtrl.getIdColumnName(); // May not be the id column
        final String refingColName = this.getReferencingColumnName(request);
        final Integer refingColValue = this.getReferencingColumnValue(request);
        
        List<REFING> found = 
                refingCtrl.select(refingColName, refingColValue, -1, -1);
        
Log.getInstance().log(Level.FINER, "For {0}={1}, found: {2}", 
this.getClass(), refingColName, refingColValue, found);
        
        boolean create;
        
        REFING refing;
        
        if(found == null || found.isEmpty()) {
            
            Map formInputs = this.getFormInputs();
            
            formInputs.put(refingColName, refingColValue);
            
            Map dbParams_refing = cf.getDatabaseFormat().toDatabaseFormat(this.getReferencingEntityClass(), formInputs);

Log.getInstance().log(Level.FINE, "Form inputs: {0}\nReferencing database parameters: {1}", 
        this.getClass(), this.getFormInputs(), dbParams_refing);

            refing = refingCtrl.create(dbParams_refing, true);

Log.getInstance().log(Level.FINER, "Created referencing entity: {0}", this.getClass(), refing);
            
            create = true;
            
        }else{
            
            create = false;
            
            if(this.isOneToOneRelationship() && found.size() > 1) {
                
                Log.getInstance().log(Level.WARNING, "Only one {0} record required per order. Found {1} records for {2} = {3}", 
                        this.getClass(), this.getReferencingEntityClass().getName(), 
                        found.size(), refingColName, refingColValue);
                
                try{
                    List<Integer> idsToDelete = new ArrayList<>(found.size()-1);
                    for(int i=1; i<found.size(); i++) {
                        REFING toDelete = found.get(i);
                        idsToDelete.add(refingCtrl.getId(toDelete));
                    }
                    int destroyed = refingCtrl.remove(idsToDelete);
                    Log.getInstance().log(Level.FINE, "To destroy: {0}, destroyed: {1}", this.getClass(), idsToDelete.size(), destroyed);
                }catch(Exception e) {
                    Log.getInstance().log(Level.WARNING, "Error deleting multiple "+this.getReferencingEntityClass().getName()+" records. Attempted to delete records because only one such record was expected.", 
                            this.getClass(), e);
                }
            }
            
            refing = found.get(0);
            
Log.getInstance().log(Level.FINER, "Found referencing entity: {0}", this.getClass(), refing);
            
        }

        if(refing != null) {
    
            // We want fine control over subsequent transactions
            //
            EntityManager em = cf.getEntityManager(this.getEntityClass());
            
            try{
            
                EntityTransaction t = em.getTransaction();
                
                try{

                    t.begin();

                    EntityController<REF, Integer> refCtrl = cf.getEntityController(this.getEntityClass(), Integer.class);

                    Map dbParams_ref = this.getDatabaseParameters(this.getForm());
                    
Log.getInstance().log(Level.FINE, "Form inputs: {0}\nReference database parameters: {1}", 
this.getClass(), this.getForm().getDetails(), dbParams_ref);

                    REF ref = refCtrl.create(dbParams_ref, true);

Log.getInstance().log(Level.FINER, "Created reference entity: {0}", this.getClass(), ref);
                    
                    em.persist(ref);

                    String refingColumn = null;
                    Map<Class, String> refings = cf.getMetaData().getReferencing(this.getEntityClass());
                    for(Class refingClass:refings.keySet()) {
                        if(refingClass.equals(this.getReferencingEntityClass())) {
                            refingColumn = refings.get(refingClass);
                            break;
                        }
                    }

                    if(refingColumn == null) {
                        throw new IllegalArgumentException(this.getReferencingEntityClass().getName()+" does not reference "+this.getEntityClass().getName());
                    }

Log.getInstance().log(Level.FINE, "Ref class: {0}, refing class: {1}, refing column: {2}", 
this.getClass(), this.getEntityClass().getName(), this.getReferencingEntityClass().getName(), refingColumn);

                    refingCtrl.setValue(refing, refingColumn, ref);

                    if(create) {
                        em.persist(refing);
                    }else{
                        em.merge(refing);
                    }

                    t.commit();
                    
                }finally{
                    if(t.isActive()) {
                        t.rollback();
                    }
                }
            }finally{
                em.close();
            }
            
            return 1;
            
        }else{
            
            return -1;
        }
    }    
}

