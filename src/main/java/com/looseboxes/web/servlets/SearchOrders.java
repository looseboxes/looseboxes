package com.looseboxes.web.servlets;

import com.bc.jpa.dao.search.BaseSearchResults;
import com.bc.jpa.dao.search.SearchResults;
import com.bc.util.Log;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.web.WebPages;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import com.bc.jpa.dao.Select;

/**
 * @(#)SearchOrders.java   08-Jul-2015 14:14:06
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
@WebServlet(name="SearchOrders", urlPatterns="/searchorders")
public class SearchOrders extends SearchServlet<Productorder> {
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.CART_AllORDERS;
    }

    @Override
    public Class<Productorder> getEntityClass() {
        return Productorder.class;
    }

    @Override
    public SearchResults<Productorder> getExistingSearchResults(HttpServletRequest request) {
        
        SearchResults<Productorder> searchResults = 
                (SearchResults<Productorder>)request.getSession().getAttribute(this.getAttributeName());
        
Log.getInstance().log(Level.FINER, "Existing search results size: {0}", 
        this.getClass(), searchResults==null?null:searchResults.getSize());
        return searchResults;
    }

    @Override
    public SearchResults<Productorder> getNewSearchResults(Map<String, String> params) {
        
        return this.getNewSearchResults(null, params);
    }

    @Override
    public SearchResults<Productorder> getNewSearchResults(
            final String query, final Map whereParams) {
        
        Select qb = LbApp.getInstance().getJpaContext().getDaoForSelect(Productorder.class);
        
        qb.where(Productorder.class, whereParams);
        
        BaseSearchResults<Productorder> searchResults = new BaseSearchResults<>(qb);
        
        return searchResults;
    }
    
    @Override
    public void setCurrentProductSearchResultsAndClosePrevious(
            HttpServletRequest request, SearchResults<Productorder> searchResults) {
        
Log.getInstance().log(Level.FINER, "Updating existing with search results of size: {0}", 
        this.getClass(), searchResults==null?null:searchResults.getSize());

        SearchResults<Productorder> existingSearchResults = this.getExistingSearchResults(request);
        
        if(existingSearchResults instanceof AutoCloseable) {
            try{
                ((AutoCloseable)existingSearchResults).close();
            }catch(Exception e) {
                Log.getInstance().log(Level.WARNING, "Exception closing "+existingSearchResults.getClass().getName(), this.getClass(), e);
            }
        }

        request.getSession().setAttribute(this.getAttributeName(), searchResults);
    }
    
    public String getAttributeName() {
        return "ordersresults";
    }
}
