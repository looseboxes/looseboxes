package com.looseboxes.web.servlets;

import com.bc.jpa.dao.search.SearchResults;
import com.bc.jpa.dao.search.SingleSearchResult;
import com.looseboxes.core.LbApp;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bc.jpa.context.PersistenceUnitContext;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * @(#)SearchServlet.java   13-Jun-2015 22:37:28
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @param <E> The type of the entity whose corresponding database table will be searched
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class SearchServlet<E> extends BaseServlet {

    private transient static final Logger LOG = Logger.getLogger(SearchServlet.class.getName());

    public static final String SEARCH_TEXT = "searchText";
    
    public static final String SEARCH_PAGE = "page";
    
    public abstract Class<E> getEntityClass();
   
    public abstract SearchResults<E> getExistingSearchResults(HttpServletRequest request);
    
    public abstract SearchResults<E> getNewSearchResults(String query, Map whereParams);
    
    public abstract void setCurrentProductSearchResultsAndClosePrevious(HttpServletRequest request, SearchResults<E> searchResults);
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String page = request.getParameter(SearchServlet.SEARCH_PAGE);
        
        final String searchText = request.getParameter(SearchServlet.SEARCH_TEXT);
        
        request.getSession().setAttribute(SearchServlet.SEARCH_TEXT, searchText);

        if(page != null) {

            int pageNumber = Integer.parseInt(page);

            this.updateSearchPage(request, pageNumber);

        }else{
            
            SearchResults<E> searchResults = this.getNewSearchResults(request);
            
            LOG.log(Level.FINE, "Search results size: {0}", 
                    searchResults==null?null:searchResults.getSize());
        
            LOG.log(Level.FINER, "Search results: {0}", searchResults);

            if(searchResults != null) {

                this.setCurrentProductSearchResultsAndClosePrevious(request, searchResults);
                
            }else{
                
                LOG.log(Level.WARNING, "Neither request parameter {0} nor {1} were specified", 
                    new Object[]{SearchServlet.SEARCH_TEXT, SearchServlet.SEARCH_PAGE});
            }
        }
    } 
    
    public SearchResults<E> getNewSearchResults(HttpServletRequest request) {
        
        Map<String, String> params = this.getSearchParameters(request);

        SearchResults<E> searchResults = this.getNewSearchResults(params);
        
        return searchResults;
    }
    
    public SearchResults<E> getNewSearchResults(Map<String, String> params) {
        
        final String query = this.getSearchText(params);

        LOG.fine(() -> MessageFormat.format("{0} = {1}, search params: {2}", 
                Search.SEARCH_TEXT, query, params));

        // A search for 209 should return product with productID 209
        //
        Integer ID = null;
        if(query != null) {
            try{
                ID = Integer.valueOf(query); 
            }catch(NumberFormatException ignored) { }
        }

        LOG.log(Level.FINE, "ID = {0}", ID);
        
        SearchResults<E> searchResults = null;
        
        if(ID != null) {
            
            final PersistenceUnitContext jpaUnit = LbApp.getInstance().getJpaContext();
            final E singleResult = jpaUnit.getDao().find(this.getEntityClass(), ID);
            searchResults = new SingleSearchResult(singleResult);
            
        }else{
            
            if(query != null || (params != null && !params.isEmpty())) {

                searchResults = this.getNewSearchResults(query, params);
            }
        }    
        
        return searchResults;
    }
    
    protected void updateSearchPage(HttpServletRequest request, int pageNumber) {
        
        LOG.fine(() -> "page number = " + pageNumber);

        SearchResults<E> searchResults = this.getExistingSearchResults(request);

        LOG.log(Level.FINE, "Search results: {0}", searchResults == null ? null : searchResults.getSize());

        if(searchResults == null) {

            this.setCurrentProductSearchResultsAndClosePrevious(request, SearchResults.EMPTY_INSTANCE);
            
        }else{

            if(pageNumber >= 0 && pageNumber < searchResults.getPageCount()) {
                
                searchResults.setPageNumber(pageNumber);
            }
        }
    }

    public Map<String, String> getSearchParameters(HttpServletRequest request) {
        
        Map<String, String> params = this.getParameters(request);

        LOG.log(Level.FINE, "Parameters: {0}", params);
        
        return params;
    }
    
    public String getSearchText(Map<String, String> params) {
        return params == null ? null : params.get(SearchServlet.SEARCH_TEXT);
    }
}
/**
 * 
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        SearchResults<E> searchResults = this.getNewSearchResults(request);

XLogger.getInstance().log(Level.FINE, "Search results size: {0}", this.getClass(), searchResults==null?null:searchResults.getSize());
        
XLogger.getInstance().log(Level.FINER, "Search results: {0}", this.getClass(), searchResults);

        if(searchResults != null) {
            
            this.setCurrentSearchResults(request, searchResults);
            
        }else {    
            
            String page = request.getParameter(SearchServlet.SEARCH_PAGE);

            if(page != null) {

                int pageNumber = Integer.parseInt(page);
                
                this.updateSearchPage(request, pageNumber);

            }else{

XLogger.getInstance().log(Level.WARNING, "Neither request parameter {0} nor {1} were specified", 
    this.getClass(), SearchServlet.SEARCH_TEXT, SearchServlet.SEARCH_PAGE, page);
            }
        }
    } 
 * 
 */