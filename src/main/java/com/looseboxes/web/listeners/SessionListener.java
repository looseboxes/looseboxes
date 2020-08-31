package com.looseboxes.web.listeners;

import com.bc.jpa.dao.search.SearchResults;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.servlets.Login;
import com.looseboxes.web.servlets.Search;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author Josh
 */
public class SessionListener implements HttpSessionListener {

    private transient static final Logger LOG = Logger.getLogger(SessionListener.class.getName());

    @Override
    public void sessionCreated(HttpSessionEvent evt) {
        
        HttpSession session = evt.getSession();
        
        LOG.log(Level.FINE, "Created session: {0}", session.getId());

        UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);
        
        if(user == null) {
            user = new UserBean();
            session.setAttribute(UserBean.ATTRIBUTE_NAME, user);
        }
        
        final Search search = new Search();
        
        final SearchResults searchResults = search.getNewSearchResults(Collections.EMPTY_MAP);
        
        LOG.fine(() -> "Initial search has " + searchResults.getSize() + " for session: " + session.getId());
        
        search.setCurrentProductSearchResultsAndClosePrevious(session, searchResults);
    }
    
    /**
     * ### Method from HttpSessionListener ###
     * 
     * Called when a session is destroyed(invalidated).
     * @param evt
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent evt) {
        
        HttpSession session = evt.getSession();
        
        LOG.log(Level.FINE, "About to destroy session: {0}", session.getId());
        
        UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);
        
        if(user != null) {
            
            if(user.isLoggedIn()) {
                
                String username = user.getName();
                
                try{
                    user.logout();
                }catch(RuntimeException e) {
                    LOG.log(Level.WARNING, "While destroying session, failed to log out user: "+user, e);
                }
                
                if(username != null) {
                    Login.loggedInUsers.remove(username);
                }
            }
            
            SearchResults<Product> results = user.getProductSearchResults();
            if(results instanceof AutoCloseable) {
                LOG.finer(() -> "Closing SearchResults: " + results);
                try{
                    ((AutoCloseable)results).close();
                }catch(Exception e) {
                    LOG.log(Level.WARNING, "Error closing " + results.getClass().getName(), e);
                }
            }
        }
    }
}
