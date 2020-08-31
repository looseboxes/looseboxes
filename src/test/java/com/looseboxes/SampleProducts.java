package com.looseboxes;

import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Address;
import com.looseboxes.pu.entities.Availability;
import com.looseboxes.pu.entities.Country;
import com.looseboxes.pu.entities.Currency;
import com.looseboxes.pu.entities.Gender;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productcategory;
import com.looseboxes.pu.entities.Productstatus;
import com.looseboxes.pu.entities.Productsubcategory;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Region;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.WebApp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import com.bc.jpa.context.JpaContext;


/**
 * @(#)Products.java   25-May-2015 12:45:18
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
public class SampleProducts {

    private EnumReferences refs;
    private Currency currency;
    private Productcategory productcategory;
    private List availability;
    private List subcat;
    private List status;
    private String [] keys;
    private String [] images;
    
    public SampleProducts() {
        refs = this.getControllerFactory().getEnumReferences();
        currency = (Currency)refs.getEntity(References.currency.NGN);
        productcategory = (Productcategory)refs.getEntity(References.productcategory.Fashion);
        keys = new String[]{"aboki", "aboki wa", "Na wa", "E don happen"};
        images = new String[]{
            "http://www.dres-s.com/wp-content/uploads/2011/12/Kids-Dresses2.jpg",
//                "http://www.babyprem.com/images/graphics/lb079lionrompernavyskyivory.jpg",
            "http://mn66.com/system/imgcache/8e6542f52c.imgcache.jpg",
            "http://img.alibaba.com/wsphoto/v0/452682466/free-shipping-12sets-new-toddler-girl-magic-cube-Beautiful-dress-set-skirt-kid-cloth-summer-wholesale.jpg",
            "file://C:/Users/Josh/Pictures/fakeimage.jpg"
        };
        availability = refs.getEntities(References.availability.InStock);
        subcat = refs.getEntities(References.productsubcategory.BabysAccessories);
        status = refs.getEntities(References.productstatus.New);
    }
    
    public EntityManager getEntityManager() {
        return this.getControllerFactory().getEntityManager(Product.class);
    }
    
    public JpaContext getControllerFactory() {
        return WebApp.getInstance().getJpaContext();
    }

    public Product [] getProducts(boolean persist) throws Exception {
        
        Siteuser user = this.getUser();
        
        return this.getProducts(user, persist);
    }
    
    public Product [] getProducts(Siteuser siteuser, boolean persist) throws Exception {
        
        final int size = keys.length;
        
        Product [] products = new Product[size];
        
        for(int i=0; i<size; i++) {
            
            products[i] = this.getProduct(i, i==0?null:products[i-1], siteuser);
            
            if(persist) {
                this.persistProduct(products[i]);
            }
        }
        
        return products;
    }

    public Product getProduct() throws Exception {
        
        Siteuser user = this.getUser();
        
        return this.getProduct(0, null, user);
    }
    
    public Product getProduct(int i, Product related, Siteuser siteuser) {
        int row = i  + 1;
        Product product = new Product();
        
        product.setAvailabilityid((Availability)availability.get(i));
        product.setAvailableAtOrFrom(siteuser.getAddressid());
        product.setBrandid(null);
        product.setCurrencyid(currency);
        product.setDatecreated(new Date());
        product.setDescription("Temp product intentionally added by developer "+row+", key: "+keys[i]);
        product.setDiscount(new BigDecimal(300 * row));
        if(i == 0) {
            product.setIsRelatedTo(null);
        }else{
            product.setIsRelatedTo(related); 
        }
        product.setMinimumOrderQuantity(i+1);
        product.setModel("Temp model "+row+", key: "+keys[i]);
        product.setPrice(new BigDecimal(2000 * row));
        product.setProductName("Temp product name "+row+", key: "+keys[i]);
        product.setProductcategoryid(productcategory);
        product.setProductstatusid((Productstatus)status.get(i));
        product.setProductsubcategoryid((Productsubcategory)subcat.get(i));
        product.setRatingPercent((short)(Math.round(Math.random() * 100)));
        product.setSeller(siteuser);
        product.setValidThrough(new Date());
        product.setViews((int)(Math.round(Math.random() * 1000)));

System.out.println("Adding product at row: "+row);  

        List<Productvariant> variants0 = new ArrayList<>(images.length);
        for(int j=0; j<images.length; j++) {
            Productvariant productunit = this.getVariants(i, images[j]);
            productunit.setProductid(product);
            productunit.setDatecreated(new Date());
            variants0.add(productunit);
        }
System.out.println("Created: "+variants0.size()+" variants for product");  

        List<Productvariant> variants1 = this.getVariants(product);
System.out.println("Created: "+variants1.size()+" variants for product");  
        
        variants0.addAll(variants1);
        
        return product;
    }
    
    public void persistProduct(Product product) {
        
        List<Productvariant> variants = product.getProductvariantList();
        
        EntityManager em = this.getEntityManager();
        
        try{
            
            EntityTransaction t = em.getTransaction();
            
            try{

                t.begin();
                
                em.persist(product);
                
                for(Productvariant variant:variants) {
                    em.persist(variant);
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
    }
    
    public Productvariant getVariants(int index, String ...images) {
        Productvariant productunit = new Productvariant();
        productunit.setColor("#"+index+"C09E"+index);
        productunit.setQuantityInStock(index * (int)Math.round(Math.random() * 100));
        productunit.setWeight(new BigDecimal(index * 10));
        productunit.setProductSize(""+(index * 2));
        
        int imageCount = images.length - index;

        for(int j=0; j<imageCount; j++) {
            switch(j) {
                case 0: productunit.setImage1(images[j]); break;
                case 1: productunit.setImage2(images[j]); break;
                case 2: productunit.setImage3(images[j]); break;
                case 3: productunit.setImage4(images[j]); break;
                case 4: productunit.setImage5(images[j]); break;
                case 5: productunit.setImage6(images[j]); break;
                case 6: productunit.setImage7(images[j]); break;
                default: break;
            }
        }
        return productunit;
    }
    
    public List<Productvariant> getVariants(Product product) {
        List<Productvariant> variants = new ArrayList<>();
        for(int j=0; j<3; j++) {
            Productvariant variant = new Productvariant();
            variant.setColor("#"+ (j>2?"E":"0") +"7CC"+j+""+(j>1?"B":j));
            variant.setDatecreated(new Date());
            variant.setProductSize(""+(j + 5));
            variant.setProductid(product);
            variant.setQuantityInStock((j + 1) * 2);
            variant.setWeight(new BigDecimal(j * (j+1)));
            variants.add(variant);
        }
        return variants;
    }
    
    public Siteuser getUser() throws Exception {
        
        JpaContext cf = this.getControllerFactory();
        
        EntityController<Siteuser, Integer> ec = cf.getEntityController(Siteuser.class, Integer.class);
        
        Siteuser siteuser = ec.selectFirst(Siteuser_.emailAddress.getName(), "looseboxes@gmail.com");
        
        if(siteuser == null) {
            
            List<Siteuser> found = ec.find(1, 0);

            siteuser = found == null ? null : found.get(0);
        }
System.out.println("Siteuser: "+siteuser);            

        return siteuser;
    }

    public Siteuser createUser() throws Exception{
        
        JpaContext cf = this.getControllerFactory();
        
        Country country = (Country)refs.getEntity(References.country.Nigeria);
        Region region = (Region)refs.getEntity(References.region.AbujaFederalCapitalTerritory);
        Gender gender = (Gender)refs.getEntity(References.gender.Male);

        EntityController<Address, Integer> addressec = cf.getEntityController(Address.class, Integer.class);
        Address address = new Address();
        address.setCity("Abuja");
        address.setCountryid(country);
        address.setCounty("Garki II");
        address.setDatecreated(new Date());
        address.setProductList(null);
        address.setRegionid(region);
        address.setSiteuserList(null);
        address.setStreetAddress("18 Yawuri Street");
        address.setUserpaymentmethodList(null);
        addressec.persist(address);

        Siteuser siteuser = new Siteuser();
        siteuser.setAddressid(addressec.selectById(1));
        siteuser.setCurrencyid(currency);
        siteuser.setDatecreated(new Date());
        siteuser.setEmailAddress("posh.bc@gmail.com");
        siteuser.setFirstName("Chinomso");
        siteuser.setGenderid(gender);
        siteuser.setLastName("Ikwuagwu");
//        siteuser.setPassword("kjvdul-");
        siteuser.setUsername("Nonny");

        EntityController<Siteuser, Integer> ec = cf.getEntityController(Siteuser.class, Integer.class);
        ec.persist(siteuser);
        
        return siteuser;
    }
}
