package com.looseboxes.web.servlets;

import com.looseboxes.web.WebApp;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import com.bc.jpa.context.JpaContext;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import com.bc.jpa.dao.util.EntityMemberAccess;

/**
 * @author Josh
 */
public class Edit implements Serializable {

    private transient static final Logger logger = Logger.getLogger(Edit.class.getName());
    
    private final boolean strict;
    
    public Edit() {
        this(false);
    }
    
    public Edit(boolean strict) {
        this.strict = strict;
    }
    
    protected <T> T execute(Class<T> entityType, Map<String, String> parameters, String idColumnName) throws ServletException {
        
        logger.log(Level.FINE, "Parameters: {0}", parameters);

        if(!validate(parameters)) {
            return null;
        }
        
        final JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        
        final Map updateParams = jpaContext.getDatabaseFormat().toDatabaseFormat(entityType, parameters);
        
        logger.log(Level.FINE, "Update Parameters: {0}", updateParams);

        if(!validate(updateParams)) {
            return null;
        }
        
        final Function<EntityManager, Optional<T>> editEntityAction = (em) -> {
    
            final EntityMemberAccess updater = jpaContext.getEntityMemberAccess(entityType);
        
            final Object idObj = updateParams.get(idColumnName);

            T entity = null;

            try{

                if(validate(idObj)) {
                    
                    final Integer id = Integer.parseInt(idObj.toString());

                    entity = em.find(entityType, id);

                    final int updateCount = updater.update(entity, updateParams, true); 

                    logger.finer(() -> "Update count: " + updateCount);

                    entity = em.merge(entity);
                }
            }catch(NumberFormatException e) {

                logger.log(Level.WARNING, "", e);
                
            }catch(ServletException e) {
                
                logger.log(Level.WARNING, "", e);
            }
            
            return Optional.ofNullable(entity);
        };
        
        final T entity = jpaContext.executeTransaction(
                jpaContext.getEntityManager(entityType), editEntityAction).orElseThrow(
                () -> new ServletException("An unexpected error occured while processing the request.")
        );
         
        return entity;
    }
    
    public boolean validate(Object oval) throws ServletException{
        if(strict) {
            return this.validateStrictly(oval);
        }
        return oval != null;
    }
    
    private boolean validateStrictly(Object oval) throws ServletException {
        if(oval == null || (oval instanceof Map && ((Map)oval).isEmpty())) {
            throw new ServletException("Invalid Request");
        }
        return true;
    }
}
