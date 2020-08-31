package com.looseboxes.web.servlets;

import com.bc.jpa.dao.search.SearchResults;
import com.bc.html.HtmlGen;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.components.UserBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * @(#)AjaxSearch.java   07-Jul-2012 01:18:48
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
public class AjaxSearch implements Serializable {

    private transient static final Logger LOG = Logger.getLogger(AjaxSearch.class.getName());
    
    private boolean stopped;

    public AjaxSearch() { }
    
    public String getResults(HttpServletRequest request) {

        String xhtmlOutput;
        
        try{

            final Map<String, String> params = stopped ? Collections.EMPTY_MAP : ServletUtil.getParameterMap(request, false, false);
            
            final Search search = this.getSearch(params);
            
            if(stopped || search == null) {
                
                xhtmlOutput = "";
                
            }else{
                
                final SearchResults<Product> searchResults = search.getNewSearchResults(request);

                LOG.log(Level.FINE, "Search results size: {0}", searchResults==null?null:searchResults.getSize());

                LOG.log(Level.FINER, "PARSING Search results: {0}", searchResults);

                if(stopped || searchResults == null) {
                
                    xhtmlOutput = "";
                    
                }else {

                    final UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);

                    try{
                        // Get the result for ajax display
                        //
                        xhtmlOutput = getSearchResultsXML(searchResults, user.getLocale(), user.getCurrency());
//System.out.println("===================================\n"+xhtmlOutput+"\n====================================");                        
                    }finally{
                        if(searchResults instanceof AutoCloseable) {
                            try{
                                LOG.log(Level.FINER, "CLOSING Search results: {0}", searchResults);
                                ((AutoCloseable)searchResults).close();
                            }catch(Exception e) {
                                LOG.warning(e.toString());
                            }
                        }
                    }
                } 
            }
        }catch(SQLException e) {
            
            // Lighter logging for this, no stack trace etc
            LOG.log(Level.WARNING, "{0}", e.toString());
            
            xhtmlOutput = "Error";
        }
        
        return xhtmlOutput;
    }

    public Search getSearch(final Map<String, String> params) {
        
        Search search = null;

        if(!stopped && params != null) {

            final String query = params.get(SearchServlet.SEARCH_TEXT);

            if(!stopped && query != null) {

                search = new Search(){
                    @Override
                    public Map<String, String> getSearchParameters(HttpServletRequest request) {

                        LOG.log(Level.FINE, "Parameters: {0}", params);

                        // Very important
                        //
                        params.remove("type"); // our AjaxSearch specific parameter

                        return params;
                    }
                };
            }
        }
        
        return search;
    }
    
    public String getSearchResultsXML(
            SearchResults<Product> results, Locale locale, Currency currency) 
            throws SQLException {
        
        if(stopped || results == null) {
            return "";
        }
        
        if(stopped || results.getPageCount() == 0) {
            return "0 search results";
        }
        
        List<Product> productListPage = results.getPage(0);
        
        if(stopped || productListPage == null) {
            return "";
        } 
        
        if(productListPage.isEmpty()) {
            return "0 search results";
        } 

        StringBuilder builder = new StringBuilder("<table style=\"width:100%; background-color:white; text-align:left; border:1px solid gray\">");
        
        // Some rows might not be added, so we keep a count of those that are.
        int added = 0;
        
        final int rowsInBatch = productListPage.size();

        synchronized(productListPage) {

            LOG.log(Level.FINER, "Ajax results count: {0}", rowsInBatch);

            // First row for message
            builder.append("<tr><th> showing ");
            builder.append(rowsInBatch).append(" of ").append(results.getSize()).append(" results.");
            builder.append("</th></tr>");

            NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            format.setMaximumFractionDigits(2);
            format.setCurrency(currency);
            
            for(Product product : productListPage) {
                
                if(stopped) {
                    break;
                }

                BigDecimal price = product.getPrice();
                BigDecimal discount = product.getDiscount();
                BigDecimal discountPrice = discount == null ? null : price.subtract(discount);
                
                String priceStr;
                if(discountPrice == null) {
                    priceStr = "<span style=\"font-weight:700; color:red;\">" + format.format(price) + "</span>";
                }else{
                    priceStr = "<span style=\"text-decoration:line-through;\">" + format.format(price) + "</span>&emsp;<span style=\"font-weight:700; color:red;\">" + format.format(discountPrice) + "</span>";
                }
                
                builder.append("<tr><td>");
                StringBuilder href = new StringBuilder();
                try{
                    ServletUtil.appendLink(product, false, false, href);
                    HtmlGen.AHREF(href, product.getProductName()+"&emsp;"+priceStr, builder);
                }catch(MalformedURLException e) {
                    LOG.log(Level.WARNING, "Error appending link for: "+product+". {0}", e.toString());
                    builder.append(product.getProductName());
                }
                
                builder.append("</td></tr>");

                ++added;
            }

            if(added <= 0) return "0 search results";
        }
        return builder.append("</table>").toString();
    }
    
    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
