package com.looseboxes.web.servlets;

/**
 * @(#)Search.java   12-Apr-2015 19:32:17
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.jpa.dao.search.BaseSearchResults;
import com.bc.jpa.dao.search.SearchResults;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.query.SelectProduct;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import com.bc.jpa.dao.Select;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Search", urlPatterns="/search")
public class Search extends SearchServlet<Product> {

    private transient static final Logger LOG = Logger.getLogger(Search.class.getName());
    
    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }

    @Override
    public Class<Product> getEntityClass() {
        return Product.class;
    }

    @Override
    public SearchResults<Product> getExistingSearchResults(HttpServletRequest request) {
        
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        SearchResults<Product> searchResults = user.getProductSearchResults();
        
        LOG.log(Level.FINER, "Existing search results size: {0}", 
                searchResults==null?null:searchResults.getSize());
        return searchResults;
    }

    @Override
    public SearchResults<Product> getNewSearchResults(
            final String query, final Map whereParams) {
        
        Select<Product> select = new SelectProduct(
                query, LbApp.getInstance().getJpaContext(), Product.class);
        
        select.where(Product.class, whereParams);
        
        SearchResults<Product> searchResults = new BaseSearchResults(select);
        
        return searchResults;
    }
    
    @Override
    public void setCurrentProductSearchResultsAndClosePrevious(HttpServletRequest request, SearchResults<Product> searchResults) {
        this.setCurrentProductSearchResultsAndClosePrevious(request.getSession(), searchResults);
    }

    public void setCurrentProductSearchResultsAndClosePrevious(HttpSession session, SearchResults<Product> searchResults) {
        
        LOG.log(Level.FINER, "Updating existing with search results of size: {0}", 
                searchResults==null?null:searchResults.getSize());

        final UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);
        
        user.setCurrentProductSearchResultsAndClosePrevious(searchResults);
    }
}
