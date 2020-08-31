package com.looseboxes.web.html;

import com.bc.html.HtmlGen;
import com.bc.jpa.controller.EntityController;
import com.bc.jpa.dao.search.BaseSearchResults;
import com.bc.util.Log;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.ProductBean;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.mail.HtmlEmailHandler;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;

/**
 * @(#)EntityHtmlGen.java   04-Jun-2015 20:33:54
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @param <E>
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class EntityHtmlGen<E> extends HtmlGen {

    private boolean useCID;
    
    private boolean mobile;
    
    private boolean externalOutput;
    
    private boolean useSearchText;

    private String groupSeparation;
    
    private String containerTagName;
    
    private String rowTagName;
    
    private String cellTagName;
    
    private String imageAndSummarySeparator;
    
    private Map summaryAttributes;
    
    private Map priceAttributes;
    
    private Map containerAttributes;
    
    private Map cellAttributes;
    
    private Map rowAttributes;
    
    private Locale locale;

    private final Set<String> files;
    
    public EntityHtmlGen() { 
        this.files = new HashSet<>();
        this.setUseNewLine(true);
        this.groupSeparation = "<br/><br/>";
        this.containerTagName = "table";
        this.rowTagName = "tr";
        this.cellTagName = "td";
        this.imageAndSummarySeparator = "<br/>";
        this.summaryAttributes = Collections.singletonMap("style", "font-size:1.5em;");
        this.containerAttributes = Collections.singletonMap("cellspacing", "20");
        this.cellAttributes = Collections.singletonMap("style", "border:4px #EEDDEE solid");
        this.priceAttributes = Collections.singletonMap("style", "background-color:white; color:#FF0000; font-weight:900; border:none; margin:0; padding:0 0.25em 0 0.25em;");
    }
    
    public abstract Class<E> getEntityClass();
    
    public abstract String getImagePath(E entity);

    public abstract String getURL(E entity) throws MalformedURLException;

    public abstract String getImageAlt(E entity);
    
    protected abstract void appendSummary(E entity, StringBuilder appendTo);
    
    public int getColumnCount() {
        return this.isMobile() ? 1 : 4;
    }
    
    public int getImageWidth() {
        return this.isMobile() ? 240 : 120; // Mobile is bigger
    }

    public int getImageHeight() {
        return this.getImageWidth();
    }
    
    public void updateEmail(Collection<E> items, StringBuilder htmlMessage, HtmlEmail email) throws EmailException {

        this.appendItems(items, htmlMessage);

Log.getInstance().log(Level.FINE, "Attachments: {0}", this.getClass(), this.getFiles().size());
        
Log.getInstance().log(Level.FINER, "Attachments HTML\n{0}", this.getClass(), htmlMessage);

        email.setHtmlMsg(htmlMessage.toString());
        
        new HtmlEmailHandler().addAttachments(email, this.getFiles(), externalOutput);
    }

    public void updateEmail(int itemCount,
            Map<String, Object> searchParams, StringBuilder htmlMessage, HtmlEmail email) 
            throws EmailException {

        this.appendLast(itemCount, searchParams, htmlMessage);

Log.getInstance().log(Level.FINER, "@updateEmail. Attachment HTML: {0}", this.getClass(), htmlMessage);

        email.setHtmlMsg(htmlMessage.toString());
        
Log.getInstance().log(Level.FINE, "@updateEmail. Attachments: {0}", this.getClass(), this.getFiles().size());

        new HtmlEmailHandler().addAttachments(email, this.getFiles(), externalOutput);
    }
    
    public void updateEmail(String [] productIds, StringBuilder attHtml, HtmlEmail email) 
            throws EmailException {

        this.appendSelected(productIds, attHtml);

Log.getInstance().log(Level.FINER, 
"Email Attachment HTML. {0}", this.getClass(), attHtml);

        email.setHtmlMsg(attHtml.toString());
        
Log.getInstance().log(Level.FINE, "Attachments: {0}", this.getClass(), this.getFiles().size());

        new HtmlEmailHandler().addAttachments(email, this.getFiles(), externalOutput);
    }

    public void appendLast(
            final int itemCount,
            final Map<String, Object> searchParams, 
            StringBuilder builder) {
        
Log.getInstance().log(Level.FINE, "Items to find: {0}", this.getClass(), itemCount);

        Collection searchResults = this.select(itemCount, searchParams);
        
        appendItems(searchResults, builder);
        
Log.getInstance().log(Level.FINER, "{0}", this.getClass(), builder);
    }
    
    private Collection<E> select(
            final int itemCount, final Map<String, Object> searchParams) {
        
        final Class<E> entityType = this.getEntityClass();
        
        try(Select select = LbApp.getInstance().getJpaContext().getDaoForSelect(entityType)) {
        
            select.where(entityType, searchParams);

            BaseSearchResults<E> searchResults = new BaseSearchResults<>(select, itemCount, true);

            return searchResults.getCurrentPage();
        }
    }

    public void appendSelected(String [] ids, StringBuilder builder) {
        appendSelected(convert(ids), builder);
    }

    public void appendSelected(Integer ids[], StringBuilder builder) {
        
        JpaContext cf = WebApp.getInstance().getJpaContext();
        EntityController<E, Object> ec = cf.getEntityController(this.getEntityClass());
        List<E> items = ec.select(ec.getIdColumnName(), ids);
        
Log.getInstance().log(Level.FINER, "Entity type: {0}\nIds: {1}\nItems: {2}", 
this.getClass(), this.getEntityClass(), ids==null?null:Arrays.toString(ids), items);

        appendItems(items, builder);
    }
    
    public void appendItems(Collection<E> entities, StringBuilder builder) {
        
Log.getInstance().log(Level.FINE, "Number of items: {0}", this.getClass(), entities==null?null:entities.size());
        
        if(entities == null || entities.isEmpty()) {
            return;
        }
        
        if(this.groupSeparation == null) {
            throw new NullPointerException();
        }
        
        builder.append(this.groupSeparation);

        try{
            
            this.validate(this.containerTagName);
            this.tagStart(this.containerTagName, this.containerAttributes, builder);

            Iterator<E> iter = entities.iterator();

            int columnInRow = 0;

            while (iter.hasNext()) {

                E toAppend = iter.next();

                if (toAppend == null) {
                    continue;
                }        

                try{

                    if(columnInRow == 0) {
                        this.validate(this.rowTagName);
                        this.tagStart(this.rowTagName, this.rowAttributes, builder);
                    }

                    this.validate(this.cellTagName);
                    this.append(this.cellTagName, this.cellAttributes, toAppend, builder);

                    if(++columnInRow == this.getColumnCount()) {
                        columnInRow = 0;
                        builder.append('<').append('/').append(this.rowTagName).append('>');
                    }

                }catch(MalformedURLException e) {
                    Log.getInstance().log(Level.WARNING, null, this.getClass(), e);
                }
            }
        }finally{
            
            builder.append('<').append('/').append(this.containerTagName).append('>');
        }

Log.getInstance().log(Level.FINE, "Html Output length: {0}", this.getClass(), builder.length());
Log.getInstance().log(Level.FINER, "Html Output:\n{0}", this.getClass(), builder);
    }
    
    private Integer[] convert(String arr[]) {
        Integer output[] = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            output[i] = Integer.valueOf(arr[i].trim());
        }        
        return output;
    }
    
    public void setRequest(HttpServletRequest request) {
        if(request == null) {
            locale = Locale.getDefault();
        }else{
            UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
            locale = user.getLocale();
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale outputLocale) {
        this.locale = outputLocale;
    }
    
    public void append(
            String tagName, Map params, E entity, StringBuilder appendTo) 
            throws MalformedURLException {

        if(tagName != null) {
            this.tagStart(tagName, params, appendTo);
        }

        this.append(params, entity, appendTo);

        if(tagName != null) {
            appendTo.append('<').append('/').append(tagName).append('>');
        }
    }

    public void append(Map params, E entity, StringBuilder appendTo) 
            throws MalformedURLException {

Log.getInstance().log(Level.FINER, "Appending:: {0}", this.getClass(), entity);

        String imagePath = this.getImagePath(entity);
        
        final String url = getURL(entity);

Log.getInstance().log(Level.FINE, "Image path: {0}, url: {1}", this.getClass(), imagePath, url);
        
        Map<String, String> attributes = new HashMap<>();

        if(imagePath != null) {

            if(!externalOutput && useCID) {
                throw new UnsupportedOperationException("Cannot use CONTENT-ID as source for images within a local context");
            }
            
            boolean checkIfLinkExists = this.useCID;
  
            String imageLink = this.getLink(imagePath, this.externalOutput, checkIfLinkExists);

            if(imageLink != null) {

                attributes.put("href", url);
                this.tagStart("a", attributes, appendTo);

                if(useCID) {
                    files.add(imageLink);
Log.getInstance().log(Level.FINE, "Adding image link to attachment files: {0}", this.getClass(), imageLink);
                }

                attributes.clear();
                String src = new HtmlEmailHandler().getSrc(imageLink, useCID);
                attributes.put("src", src);

                attributes.put("alt", this.getImageAlt(entity));
                
                attributes.put("width", Integer.toString(this.getImageWidth()));
                attributes.put("height", Integer.toString(this.getImageHeight()));
                
                this.straightTag("img", attributes, appendTo);

                appendTo.append("</a>");

            }else{
                String siteName = WebApp.getInstance().getName();
                String noImageText = "<br/><br/><span style=\"color:#CCDDEE; font-size:0.75em; font-family:'lucida calligraphy', sans-serif\">"+siteName+"</span><br/><br/><span style=\"color:#AABBCC; font-size:0.6875em\">no image</span>";
                this.enclosingTag("div", attributes, noImageText, appendTo);
            }
            
            if(this.imageAndSummarySeparator != null) {
                appendTo.append(this.imageAndSummarySeparator);
            }
        }
        
// INNER SPAN
        if(this.summaryAttributes != null && !this.summaryAttributes.isEmpty()) {
            this.tagStart("span", this.summaryAttributes, appendTo);
        }else{
            appendTo.append("<span>");
        }
    
        this.appendSummary(entity, appendTo);

        //<a href="${contextURL}/dp?pt=${itemRecord.tableName}&amp;id=${itemRecord.productId}">more</a>
        this.enclosingTag("a", "href", url, "more", appendTo);
        
// INNER SPAN END
        appendTo.append("</span>");
        
        if(this.isMobile()) {
            appendTo.append("&nbsp;");
        }else{
            appendTo.append("<br/>");
        }
    }
    
    private String getLink(
            String relativeLink, boolean externalOutput, boolean checkIfExists) throws MalformedURLException {
        
Log.getInstance().log(Level.FINE, "Check if exists: {0}, external output: {1}, link to find: {2}", 
        this.getClass(), checkIfExists, externalOutput, relativeLink);
        
        String link = null;
        
        if(relativeLink != null) {
            
            boolean isURL = ServletUtil.isHttpUrl(relativeLink);

            if(isURL) {
                link = relativeLink;
            }else{
                // WebApp.getExternalPath returns the path relative to the applications external local folder
                String fullPath = WebApp.getInstance().getExternalPath(relativeLink);
Log.getInstance().log(Level.FINER, "Relative: {0}, file; {1}", this.getClass(), relativeLink, fullPath);
                if(!checkIfExists || new File(fullPath).exists()) {
                    if(!externalOutput) {
                        // WebApp.getExternalPath returns the path relative to the applications external local folder
                        link = fullPath;
                    }else{
                        link = WebApp.getInstance().getExternalURL(relativeLink).toString();
Log.getInstance().log(Level.FINER, "Relative: {0}, URL: {1}", this.getClass(), relativeLink, link);
                    }
                }else{
                    link = null;
                }
            }
        }
        
Log.getInstance().log(Level.FINE, "External output: {0}, link to find: {1}, found: {2}", 
        this.getClass(), externalOutput, relativeLink, link);
        
        return link;
    }

    private void validate(String name) {
        if(name == null || name.isEmpty()) {
            throw new NullPointerException();
        }
    }
    
    private ProductBean bean;
    public String getImagePath(Productvariant entity) {
        if(bean == null) {
            bean = new ProductBean();
        }
        bean.setSelectedVariant(entity);
        return bean.getImagePath();
    }
    
    public String getURL(Product product) throws MalformedURLException {
        boolean method1 = true;
        if(!useSearchText && method1) {
            // This logic may break if products are not accessible via format:
            // /products/298_h and m baby jumpers.jsp
            //
            StringBuilder appendTo = new StringBuilder();
            
            appendTo.append("/products/").append(product.getProductid()).append('_');
            
            try{
                appendTo.append(URLEncoder.encode(product.getProductName(), "utf-8"));
            }catch(UnsupportedEncodingException e) {
                appendTo.append(product.getProductName().replace(' ', '+'));
            }
            
            appendTo.append(".jsp");
            
            return WebApp.getInstance().getURL(appendTo.toString()).toExternalForm();
            
        }else{
            StringBuilder appendTo = new StringBuilder();
            ServletUtil.appendLink(product, this.externalOutput, useSearchText, appendTo);    
            return appendTo.toString();
        }
    }    

    public String getPriceDisplay(BigDecimal price, BigDecimal discount) {
        
        return this.getPriceDisplay(price, discount, true);
    }
    
    public String getPriceDisplay(BigDecimal price, BigDecimal discount, boolean displayNondiscountPrice) {
        
        String output;
        
        if(price == null) {
            
            output = null;
            
        }else{
            
            NumberFormat format = this.getNumberFormat();
            
            String priceStr = format.format(price.doubleValue());
            
            StringBuilder builder = new StringBuilder();
            
            String toDisplay;

            if(discount == null || discount.doubleValue() == 0) {

                toDisplay = priceStr;

            }else{
                
                String discountPriceStr = format.format(price.doubleValue() - discount.doubleValue());
                
                if(displayNondiscountPrice) {
                    this.enclosingTag("span", "style", "text-decoration:line-through", priceStr, builder);
                    builder.append(' ');
                }
                
                toDisplay = discountPriceStr;
            }

            if(this.priceAttributes == null) {
                this.enclosingTag("span", toDisplay, builder);
            }else{
                this.enclosingTag("span", this.priceAttributes, toDisplay, builder);
            }
            
            output = builder.toString();
        }

        return output;
    }
    
    private NumberFormat fmt;
    public NumberFormat getNumberFormat() {
        if(fmt == null) {
            try{
                if(this.getLocale() != null) {
                    fmt = NumberFormat.getCurrencyInstance(this.getLocale());
                }else{
                    fmt = NumberFormat.getCurrencyInstance();
                }
            }catch(Exception e) {
                if(this.getLocale() != null) {
                    fmt = NumberFormat.getCurrencyInstance(this.getLocale());
                }else{
                    fmt = NumberFormat.getCurrencyInstance();
                }
            }
            fmt.setMaximumFractionDigits(2);
        }
        return fmt;
    }
    
    public Set<String> getFiles() {
        return files;
    }

    public String getGroupSeparation() {
        return groupSeparation;
    }

    public void setGroupSeparation(String groupSeparation) {
        this.groupSeparation = groupSeparation;
    }

    public String getContainerTagName() {
        return containerTagName;
    }

    public void setContainerTagName(String containerTagName) {
        this.containerTagName = containerTagName;
    }

    public String getRowTagName() {
        return rowTagName;
    }

    public void setRowTagName(String rowTagName) {
        this.rowTagName = rowTagName;
    }

    public String getCellTagName() {
        return cellTagName;
    }

    public void setCellTagName(String cellTagName) {
        this.cellTagName = cellTagName;
    }

    public String getImageAndSummarySeparator() {
        return imageAndSummarySeparator;
    }

    public void setImageAndSummarySeparator(String imageAndSummarySeparator) {
        this.imageAndSummarySeparator = imageAndSummarySeparator;
    }

    public boolean isUseSearchText() {
        return useSearchText;
    }

    public void setUseSearchText(boolean useSearchText) {
        this.useSearchText = useSearchText;
    }


    public boolean isMobile() {
        return this.mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public boolean isExternalOutput() {
        return externalOutput;
    }
    
    public void setExternalOutput(boolean externalOutput) {
        this.externalOutput = externalOutput;
    }

    public boolean isUseCID() {
        return useCID;
    }

    public void setUseCID(boolean useCID) {
        this.useCID = useCID;
    }

    public Map getSummaryAttributes() {
        return summaryAttributes;
    }

    public void setSummaryAttributes(Map summaryAttributes) {
        this.summaryAttributes = summaryAttributes;
    }

    public Map getPriceAttributes() {
        return priceAttributes;
    }

    public void setPriceAttributes(Map priceAttributes) {
        this.priceAttributes = priceAttributes;
    }

    public Map getContainerAttributes() {
        return containerAttributes;
    }

    public void setContainerAttributes(Map containerAttributes) {
        this.containerAttributes = containerAttributes;
    }

    public Map getCellAttributes() {
        return cellAttributes;
    }

    public void setCellAttributes(Map cellAttributes) {
        this.cellAttributes = cellAttributes;
    }

    public Map getRowAttributes() {
        return rowAttributes;
    }

    public void setRowAttributes(Map rowAttributes) {
        this.rowAttributes = rowAttributes;
    }
}
