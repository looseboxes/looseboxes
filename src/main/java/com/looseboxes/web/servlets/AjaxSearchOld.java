package com.looseboxes.web.servlets;

import com.bc.jpa.dao.search.SearchResults;
import com.bc.util.Log;
import com.bc.html.HtmlGen;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.components.UserBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
public class AjaxSearchOld implements Serializable {
    
    public AjaxSearchOld() { }
    
    public String getResults(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String s = null;
        try{
            this.addSearchThread(session);
            s = doGetResults(request);
        }catch(Exception t){
            Log.getInstance().log(Level.WARNING, "", AjaxSearchOld.class, t);
        }finally{
            this.removeSearchThread(session);
        }
        return s;
    }

    private String doGetResults(HttpServletRequest request) {

        String xhtmlOutput;
        
        try{

            Search search = this.getSearch(request);
            
            if(search == null) {
                
                xhtmlOutput = "";
                
            }else{
                
                SearchResults<Product> searchResults = search.getNewSearchResults(request);

Log.getInstance().log(Level.FINE, "Search results size: {0}", this.getClass(), searchResults==null?null:searchResults.getSize());

Log.getInstance().log(Level.FINER, "PARSING Search results: {0}", this.getClass(), searchResults);

                if(searchResults != null) {
                    try{
                        // Get the result for ajax display
                        //
                        xhtmlOutput = getSearchResultsXML(request, searchResults);
//System.out.println("===================================\n"+xhtmlOutput+"\n====================================");                        
                    }finally{
                        
                        try{
Log.getInstance().log(Level.FINER, "CLOSING Search results: {0}", this.getClass(), searchResults);
                            ((AutoCloseable)searchResults).close();
                        }catch(Exception e) {
                            Log.getInstance().log(Level.WARNING, "", this.getClass());
                        }
                    }
                } else{

                    xhtmlOutput = "";
                } 
            }
        }catch(SQLException e) {
            
            // Lighter logging for this, no stack trace etc
            Log.getInstance().log(Level.WARNING, "{0}. {1}", this.getClass(), e);
            
            xhtmlOutput = "Error";
        }
        
        return xhtmlOutput;
    }
    
    private Search getSearch(HttpServletRequest request) {
        
        Search search = null;

        final Map<String, String> params = ServletUtil.getParameterMap(request, false, false);

        if(params != null) {

            String query = params.get(SearchServlet.SEARCH_TEXT);

            if(query != null) {

                search = new Search(){
                    @Override
                    public Map<String, String> getSearchParameters(HttpServletRequest request) {

Log.getInstance().log(Level.FINE, "Parameters: {0}", this.getClass(), params);

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
    
    private String getSearchResultsXML(
            HttpServletRequest request, SearchResults<Product> results) 
            throws SQLException {
        
        if(results == null) {
            return "";
        }
        if(results.getPageCount() == 0) {
            return "0 search results";
        }
        
        List<Product> batch = results.getPage(0);
        if(batch == null) {
            return "";
        } 
        if(batch.isEmpty()) {
            return "0 search results";
        } 

        StringBuilder builder = new StringBuilder("<table style=\"width:100%; background-color:white; text-align:left; border:1px solid gray\">");
        
        // Some rows might not be added, so we keep a count of those that are.
        int added = 0;
        
        final int rowsInBatch = batch.size();

        synchronized(batch) {

Log.getInstance().log(Level.FINER, "Ajax results count: {0}", this.getClass(), rowsInBatch);

            // First row for message
            builder.append("<tr><th> showing ");
            builder.append(rowsInBatch).append(" of ").append(results.getSize()).append(" results.");
            builder.append("</th></tr>");

            UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
            
            NumberFormat format = NumberFormat.getCurrencyInstance(user.getLocale());
            format.setMaximumFractionDigits(2);
            format.setCurrency(user.getCurrency());
            
            for(Product product:batch) {

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
                    Log.getInstance().log(Level.WARNING, "Error appending link for: "+product, this.getClass(), e);
                    builder.append(product.getProductName());
                }
                
                builder.append("</td></tr>");

                ++added;
            }

            if(added <= 0) return "0 search results";
        }
        return builder.append("</table>").toString();
    }
    
    private void addSearchThread(HttpSession session) {
        Thread prev = getSearchThread(session);
        Thread curr = Thread.currentThread();
        if(prev != null && !prev.equals(curr)) {
            this.interrupt(prev);
        }
Log.getInstance().log(Level.FINER, "ADDING Thread: {0}", this.getClass(), curr);
        session.setAttribute(this.getSearchThreadAttributeName(), curr);
    }
    
    private void removeSearchThread(HttpSession session) {
Log.getInstance().log(Level.FINER, "REMOVING Thread: {0}", this.getClass(), 
        session.getAttribute(this.getSearchThreadAttributeName()));
        session.removeAttribute(this.getSearchThreadAttributeName());
    }

    private Thread getSearchThread(HttpSession session) {
        Thread thread = (Thread)session.getAttribute(this.getSearchThreadAttributeName());
        return thread;
    }
    
    private boolean interrupt(Thread thread) {
        if(thread != null && thread.isAlive() && !thread.isInterrupted()) {
Log.getInstance().log(Level.FINER, "INTERRUPTING Thread: {0}", this.getClass(), thread);
            thread.interrupt(); 
            return true;
        }
        return false;
    }
    
    private String getSearchThreadAttributeName() {
        return this.getClass().getSimpleName()+"SearchThread";
    }
}
