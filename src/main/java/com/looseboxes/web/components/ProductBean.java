package com.looseboxes.web.components;

import com.bc.jpa.controller.EntityController;
import com.bc.util.Log;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.forms.Forms;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.servlet.ServletException;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;


/**
 * @(#)SelectProduct.java   18-Apr-2015 01:53:29
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
public class ProductBean implements Serializable {

    private Productvariant selectedVariant;
    
    private UserBean user;

    public ProductBean() { }
    
    public ProductBean(UserBean user, Productvariant variant) {
        this.user = user;
        this.selectedVariant = variant; 
    }
    
    public Product selectProduct(Map<String, String> params) throws ServletException {
        
        return this.selectProduct(selectedVariant, params);
    }
    
    public Product selectProduct(
            Productvariant previous, Map<String, String> params) throws ServletException {
        
Log.getInstance().log(Level.FINE, "Parameters: {0}", this.getClass(), params);
Log.getInstance().log(Level.FINER, "Parameters: {0}", this.getClass(), user);

        String sval = params.get(Product_.productid.getName());
        
        if(sval == null) {
            throw new ServletException("Required parameter '"+Product_.productid.getName()+"' is missing");
        }
        
        Integer productId;
        try{
            productId = Integer.valueOf(sval); 
        }catch(NumberFormatException e) {
            throw new ServletException("Invalid value for parameter '"+Product_.productid.getName()+"'", e);
        }
        
        Product product;
        
        if(previous == null || !productId.equals(this.getProductid())) {
            
            EntityController<Product, Integer> ec = 
                    WebApp.getInstance().getJpaContext().getEntityController(Product.class, Integer.class);

            product = ec.find(productId);
            
        }else{
            
            product = previous.getProductid();
        }
        
        return product;
    }
    
    public Productvariant selectVariant(Map<String, String> params) throws ServletException {
        
        return this.selectVariant(this.getDetails(), params);
    }

    public Productvariant selectVariant(
            Product product, Map<String, String> params) throws ServletException {
        
        Productvariant output = this.getProductvariant(product, params, true);
        
        if(output == null) {
            output = this.getProductvariant(product, params, false);
        }

Log.getInstance().log(Level.FINE, "Selected candidate: {0}", this.getClass(), this.selectedVariant);
        
        if(output != null) {
            this.selectedVariant = output;
        }
        
        return output;
    }
    
    private Productvariant getProductvariant(
            Product product, Map<String, String> params, boolean mergeWithPrevious) {
        
        String size = params.get(Productvariant_.productSize.getName());
        String color = params.get(Productvariant_.color.getName());
        
        if(mergeWithPrevious && this.selectedVariant != null) {
            // If no size was specified by the request's originator, use the previous
            if(size == null && this.selectedVariant.getProductSize() != null) {
                size = this.selectedVariant.getProductSize(); 
            }
            // if no color was specified by the request's originator, use the previous
            if(color == null && this.selectedVariant.getColor() != null) {
                color = this.selectedVariant.getColor();
            }
        }
        
Log.getInstance().log(Level.FINE, "To find. Product id: {0}, color: {1}, size: {2}",
this.getClass(), product.getProductid(), color, size);

        List<Productvariant> variants = product.getProductvariantList();
        
        Productvariant output = null;
        
        for(Productvariant variant:variants) {
            String s = variant.getProductSize();
            String c = variant.getColor();
Log.getInstance().log(Level.FINER, "Candidate. color: {0}, size: {1}", this.getClass(), c, s);
            if((size == null || size.equals(s)) && 
               (color == null || color.equals(c)) &&
               (this.getAvailable(variant) > 0)) {
                output = variant;
                break;
            }
        }
        
        return output;
    }
    
    public String getSourceUrl() {
        return this.getRecord() == null ? null : getRecord().getUrl();
    }
    
    public boolean isCanBeAddedToShoppingCart() {
        return this.getSeller() != null && !this.isThirdParty() && 
                this.getRecord().getPrice() != null &&
                this.getAvailable() > 0;
    }

    public Set<Productvariant> getSizeVariants() {
        Product product = getRecord();
        Set<Productvariant> output = null;
        if(product != null) {
            List<Productvariant> units = product.getProductvariantList();
            Set<String> uniqueSizes = new HashSet<>(units.size());
            output = new HashSet<>(units.size());
            for(Productvariant variant:units) {
                if(this.getAvailable(variant) < 1) {
                    continue;
                }
                String size = variant.getProductSize();
                if(size != null && uniqueSizes.add(size)) {
                    output.add(variant);
                }
            }
        }
        return output == null || output.isEmpty() ? Collections.EMPTY_SET : output;
    }
    
    public Set<Productvariant> getColorVariants() {
        Product product = getRecord();
        Set<Productvariant> output = null;
        if(product != null) {
            List<Productvariant> units = product.getProductvariantList();
            Set<String> uniqueColors = new HashSet<>(units.size());
            output = new HashSet<>(units.size());
            for(Productvariant unit:units) {
                if(this.getAvailable(unit) < 1) {
                    continue;
                }
                String color = unit.getColor();
                if(color != null && uniqueColors.add(color)) {
                    output.add(unit);
                }
            }
        }
        return output == null || output.isEmpty() ? Collections.EMPTY_SET : output;
    }

    public int getAllAvailable() {
        return getRecord() == null ? 0 : this.getAllAvailable(this.getRecord());
    }
    
    public int getAllAvailable(Product product) {
        List<Productvariant> units = product.getProductvariantList();
        int i = 0;
        for(Productvariant unit:units) {
            int available = this.getAvailable(unit);
            i += available;
        }
        return i;
    }
    
    public int getAvailable() {
        return this.getAvailable(selectedVariant);
    }
    
    public int getAvailable(Productvariant variant) {
        int instock = variant.getQuantityInStock();
        Integer ival = this.getQuantityOnUserOrder(variant);
        int ordered = ival == null ? 0 : ival;
        int available = instock - ordered;
Log.getInstance().log(Level.FINER, "Product variant ID: {0}, quantityInStock: {1}, ordered: {2}, available: {3}", 
    this.getClass(), variant.getProductvariantid(), instock, ordered, available);
        return available;
    }

// This quantity is inaccurate as it will return multiple sums for the same product
// For example if an item has quantityInStock = 2
// User A may only add 2
// User B may also add 2
// The sum of quantities is 4, however only 2 may be ordered
// Once either User A or B makes an order, quantityInStock is reduced to 0
//    
//    public Integer getQuantityOnAllOrders(Productvariant unit) {
//        Map where = Collections.singletonMap(Orderproduct_.productvariantid.getName(), unit);
//        return this.getDao().getSum(Integer.class, Orderproduct.class, Orderproduct_.quantity.getName(), where);
//    }

//    public Integer getQuantityOnAllOrders(Productvariant unit) {
//        Map where = Collections.singletonMap(Orderproduct_.productvariantid.getName(), unit);
//        return this.getDao().getSum(Integer.class, Orderproduct.class, Orderproduct_.quantity.getName(), where);
//    }

    public Integer getQuantityOnUserOrder(Productvariant unit) {
        
        if(this.user.getShoppingCart() == null) {
            
            return 0;
            
        }else {
            
            return this.user.getShoppingCart().getQuantityOnOrder(unit);
        }
    }
    
    public String getMetaKeywords() {
//@todo        
        return this.getRecord() == null ? null : this.getRecord().getProductName();
    }
    
    /**
     * @return The address e.g: <tt>1 heavensgate street, carlifonia 90210, LA, USA</tt>
     */
    public String getAddress() {
//@todo        
        return null;
    }
    
    public boolean isSellerLoggedIn() {
//@todo        
        return false;
    }
    
    public boolean isSellerLoggedInToChat() {
//@todo        
        return false;
    }
    
    public boolean isCanBeLocated() {
//@todo        
        return false;
    }
    
    public boolean isThirdParty() {
        Siteuser sellerRecord = this.getSeller();
        return sellerRecord != null && 
                UserBean.isAdmin(sellerRecord.getUsername()) && 
                sellerRecord.getUrl() != null;
    }

    /**
     * @deprecated Rather use {@link #getProductid()}
     * @return The product ID
     */
    @Deprecated
    public Integer getProductId() {
        return this.getProductid();
    }
    
    /**
     * @return The product ID
     */
    public Integer getProductid() {
        return this.getRecord() == null ? null : getRecord().getProductid();
    }
    
    /**
     * @return Map whose keys are column names and corresponding values are 
     * column values for image columns in the Product table. 
     * <br/><br/>
     * E.g:
     * <pre>
     * {  image1=/externalfolder/resources/images/fashion/203image1.jpg, 
     *    image2=/externalfolder/resources/images/fashion/203image2.jpg
     * }
     * </pre>
     */
    public Map<String, String> getImages() {
        
        String [] imageNames = Forms.getImageNames(Productvariant.class);
        
        HashMap<String, String> output = new HashMap<>(imageNames.length, 1.0f);
        
        for(String imageName:imageNames) {
            String imagePath = this.getImagePath(imageName);
            if(imagePath == null) {
                continue;
            }
            output.put(imageName, imagePath);
        }
        
        return output;
    }
    
    /**
     * @param productvariantId The ID of the product variant whose image will be returned
     * @param imageName The column name of the image whose relative path will be returned
     * @return The path (<tt>relative to the web app's context</tt>) of the image for the 
     * product variant with the specified product ID and column name
     */
    public String getImagePath(Integer productvariantId, String imageName) {
        // First check if the images is for the current selected prodcut
        String imagePath;
        if(this.getSelectedVariant() != null && this.getSelectedVariant().getProductvariantid().equals(productvariantId)) {
            imagePath = this.getImagePath(imageName);
        }else{
            JpaContext jpaContext = WebApp.getInstance().getJpaContext();
            try(Select<String> qb = jpaContext.getDaoForSelect(Productvariant.class, String.class)) {
                imagePath = qb.from(Productvariant.class)
                .where(Productvariant_.productvariantid.getName(), Select.EQ, productvariantId)
                .createQuery().getSingleResult();
            }catch(javax.persistence.NoResultException ignore) {
                imagePath = null;
            } 
        }
        return imagePath;
    }

    /**
     * @param imageName The column name of the image whose relative path will be returned
     * @return The path (<tt>relative to the web app's context</tt>) of the image for the 
     * {@link #selectedVariant selected product variant} referenced by this object.
     */
    public String getImagePath(String imageName) {
        String imagePath;
        if(Productvariant_.image1.getName().equals(imageName)) {
            imagePath = selectedVariant.getImage1();
        }else if(Productvariant_.image2.getName().equals(imageName)) {
            imagePath = selectedVariant.getImage2();
        }else if(Productvariant_.image3.getName().equals(imageName)) {
            imagePath = selectedVariant.getImage3();
        }else if(Productvariant_.image4.getName().equals(imageName)) {
            imagePath = selectedVariant.getImage4();
        }else if(Productvariant_.image5.getName().equals(imageName)) {
            imagePath = selectedVariant.getImage5();
        }else if(Productvariant_.image6.getName().equals(imageName)) {
            imagePath = selectedVariant.getImage6();
        }else if(Productvariant_.image7.getName().equals(imageName)) {
            imagePath = selectedVariant.getImage7();
        }else{
            imagePath = null;
        }
        return imagePath;
    }
    
    public String getLogo() {
        return this.getRecord() == null ? null : this.getRecord().getLogo();
    }
    
    /**
     * @return The path (<tt>relative to the web app's context</tt>) to the first available image from the product's images
     */
    public String getImagePath() {
        String image;
        if(selectedVariant != null) {
            image = selectedVariant.getImage1();
            if(image == null) {
                image = selectedVariant.getImage2();
                if(image == null) {
                    image = selectedVariant.getImage3();
                    if(image == null) {
                        image = selectedVariant.getImage4();
                        if(image == null) {
                            image = selectedVariant.getImage5();
                            if(image == null) {
                                image = selectedVariant.getImage6();
                                if(image == null) {
                                    image = selectedVariant.getImage7();
                                }
                            }
                        }
                    }
                }
            }
        }else{
            image = null;
        }
        return image;
    }
    
    public Siteuser getSeller() {
        return this.getRecord() == null? null : getRecord().getSeller();
    }

    public Product getDetails() {
        return this.getRecord();
    }
    
    public Product getRecord() {
        return selectedVariant == null ? null : selectedVariant.getProductid();
    }

    public Productvariant getSelectedVariant() {
        return selectedVariant;
    }

    public void setSelectedVariant(Productvariant selectedVariant) {
        this.selectedVariant = selectedVariant;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
