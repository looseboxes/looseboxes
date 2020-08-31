package com.looseboxes.web.components;

import com.bc.util.StringArrayComparatorImpl;
import com.bc.util.Log;
import com.looseboxes.web.AppProperties;
import com.looseboxes.core.Defaults;
import com.looseboxes.pu.entities.Address;
import com.looseboxes.pu.entities.Country;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productcategory;
import com.looseboxes.pu.entities.Productsubcategory;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Region;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.servlets.Convcurr;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.Currency;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import com.bc.config.Config;
import com.bc.util.StringArrayComparator;


/**
 * @(#)DeliveryRates.java   16-Dec-2014 09:09:46
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
public class Delivery implements Serializable {

    private boolean express;
    
    private int maxPropertiesToIterate = 20;
    
    private float comparisonTolerance = 0.1f;
    
    private ShoppingCart shoppingCart;
    
    private Properties shippingGroupProps;
    
    public Delivery() { 
    
        this(null);
    }

    public Delivery(ShoppingCart cart) { 

        this.shoppingCart = cart;
        
        this.init();
    }
    
    private void init() {
        
        shippingGroupProps = new Properties();
        
        final Config<Properties> config = WebApp.getInstance().getConfig();
        
        Set<String> names = config.getSourceData().stringPropertyNames();
        for(String name:names) {
            if(name.startsWith("shipping.group")) {
                shippingGroupProps.setProperty(name, config.get(name));
            }
        }
    }
    
    public boolean isFreeShipping(BigDecimal total) {
        return this.isFreeShippingLocation() &&
                this.isFreeShippingAmount(total);
    }
    
    public boolean isFreeShippingLocation() {
        try{
            return this.doIsFreeShippingLocation();
        }catch(RuntimeException e) {
            Log.getInstance().log(Level.WARNING, "Could not determine if location is eligible for free shipping", this.getClass(), e);
            return false;
        }
    }
    
    private boolean doIsFreeShippingLocation() {
        
        if(this.getAddress() == null) {
            return false;
        }
        
        Country country = this.getAddress().getCountryid();
        
        if(country == null) {
            return false;
        }
        
        Config props = WebApp.getInstance().getConfig();
        
        Collection<String> freeShippingCountries = props.getCollection(AppProperties.FREESHIPPING_COUNTRIES);
        
        if(freeShippingCountries == null || freeShippingCountries.isEmpty()) {
            return false;
        }

        if(!freeShippingCountries.contains(country.getCountry()) && !freeShippingCountries.contains(country.getIsocode3())) {
            return false;
        }
        
        Region region = this.getAddress().getRegionid();
        
        Collection<String> freeShippingStates = props.getCollection(AppProperties.FREESHIPPING_STATES);
        
        // If no states were specified, but a country was specified
        // then the whole country is eligible for free shiping
        if(freeShippingStates == null || freeShippingStates.isEmpty()) {
            return true;
        }
        
        if(region == null) {
            return false;
        }
        
        boolean freeShipping = freeShippingStates.contains(region.getRegion());
        
Log.getInstance().log(Level.FINER, "Free shipping: {0}, Country: {1}, State: {2}", 
        this.getClass(), freeShipping, country, region.getRegion());
                
        return false;
    }
    
    public boolean isFreeShippingAmount(BigDecimal total) {
        if(total == null) {
            return false;
        }
        Convcurr concurr = new Convcurr();
        String userCode = this.getUser().getCurrency().getCurrencyCode();
        String appCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        float rate = concurr.getRate(userCode, appCode); 
        boolean eligible = (total.doubleValue() * rate) >= 
        WebApp.getInstance().getConfig().getDouble(AppProperties.FREESHIPPING_MINIMUM, 10000);
        return eligible;
    }
    
    public BigDecimal getDeliveryAmount(float totalWeight) {
        BigDecimal output = this.getAddress() == null ? BigDecimal.ZERO : this.getDeliveryAmount(totalWeight, this.getAddress());
        return output;
    }
    
    public BigDecimal getDeliveryAmount(float totalWeight, Address address) {
        
        BigDecimal deliveryAmount = BigDecimal.ZERO; // default
        
        BigDecimal deliveryRate = this.getDeliveryRate(address);
        
Log.getInstance().log(Level.FINER, "Total weight: {0}, Delivery rate: {1}", 
        this.getClass(), totalWeight, deliveryRate);

        if(deliveryRate != BigDecimal.ZERO) {
            
            if(totalWeight != 0) {
                
                if(totalWeight < this.getMinimumWeight()) {
                    totalWeight = this.getMinimumWeight();
                }
                
                float fval = deliveryRate.floatValue() * totalWeight;

Log.getInstance().log(Level.FINER, "Delivery amount: {0}", this.getClass(), fval);

                MathContext mathContext = WebApp.getInstance().getDefaults().getDefaultMathContext();

                deliveryAmount = new BigDecimal(fval, mathContext);
            }
        }
        
Log.getInstance().log(Level.FINE, "Total weight: {0}, delivery amount: {1}, address: {2}", 
this.getClass(), totalWeight, deliveryAmount, this.toString(address));
        
        return deliveryAmount;
    }
    
    public BigDecimal getDeliveryRate() {

        return getDeliveryRate(this.getAddress());
    }

    public BigDecimal getDeliveryRate(Address address) {

        String shippingGroup = this.getShippingGroup(address);
        
        BigDecimal output = shippingGroup == null ? BigDecimal.ZERO : this.getDeliveryRate(shippingGroup, express);
        
Log.getInstance().log(Level.FINE, "Delivery rate: {0}, express: {1}, address: {2}, shipping group: {3}", 
this.getClass(), output, express, this.toString(address), shippingGroup);
        
        return output;
    }
    
    public BigDecimal getDeliveryRate(String shippingGroup, boolean expressDelivery) {

        if(shippingGroup == null) {
            
            throw new NullPointerException();
        }    
        
        String name = expressDelivery ? shippingGroup + ".rate.express" : shippingGroup + ".rate";
        
        String sval = this.shippingGroupProps.getProperty(name);

        float fval = Float.parseFloat(sval);
        
        float total = fval + (fval * this.getVatRate()/100) + this.getFee();
        
        MathContext mathContext = WebApp.getInstance().getDefaults().getDefaultMathContext();
        
        return new BigDecimal(total, mathContext);
    }

    public String getShippingGroup() {
        return this.getAddress() == null ? null : this.getShippingGroup(this.getAddress());
    }
    
    public String getShippingGroup(Address address) {
        
        String output;
        
        WebApp webApp = WebApp.getInstance();
        
        Defaults defaults = webApp.getDefaults();
        
        if(!address.getCountryid().equals(defaults.getDefaultCountry())) {
            output = "shipping.group5";
        }else{
            String cityGroup = null;
            String stateGroup = null;

            StringArrayComparatorImpl sc = new StringArrayComparatorImpl();
            
            Config propsSvc = webApp.getConfig();
            
            for(int i = 0; i < this.maxPropertiesToIterate; i++) {

                String name = "shipping.group"+i;
                String [] values = propsSvc.getArray(name); 

                if(values == null || values.length == 0) {
                    break;
                }

                for(String value:values) {
                    
                    String fvalue = this.format(value);
//System.out.println("====================================\nFvalue: "+fvalue);                    
                    String city = address.getCity();
//System.out.println("City: "+city);                    
                    if(city != null && this.matches(sc, fvalue, format(city))) {
                        cityGroup = name.trim();
//System.out.println("CityGroup: "+cityGroup);                        
                    }
                    // Give this preference
                    if(cityGroup != null) {
                        break;
                    }
                    
                    String state = address.getRegionid() == null ? null : address.getRegionid().getRegion();
//System.out.println("State: "+state);                    
                    if(state != null && this.matches(sc, fvalue, format(state))) {
                        stateGroup = name.trim();
//System.out.println("StateGroup: "+stateGroup);                        
                    }
                }
            }

Log.getInstance().log(Level.FINER, "cityGroup={0}, stateGroup={1}", 
            this.getClass(), cityGroup, stateGroup);

            // shipping.group4 == other states in nigeria
            //
            output =  cityGroup != null ? cityGroup : stateGroup != null ? stateGroup : "shipping.group4";
        }

Log.getInstance().log(Level.FINE, "Address: {0}, Shipping group: {1}", 
            this.getClass(), this.toString(address), output);
        
        return output;
    }
    
    public boolean matches(StringArrayComparator sc, String a, String b) {
        return a.contains(b) || b.contains(a) || sc.compare(a, b, this.comparisonTolerance);
    }
    
    public float getWeight(Productvariant variant, float defaultWeight) {
        
        BigDecimal w = variant.getWeight();

        float output;
        if(w == null || w.floatValue() == 0.0f) {
            output = this.getDefaultWeight(variant, defaultWeight);
        }else{
            output = w.floatValue();
        }
        
        return output;
    }
    
    public float getDefaultWeight(Productvariant variant, float defaultValue) {
        
        Product product = variant.getProductid();
        Productcategory cat = product.getProductcategoryid();
        Productsubcategory subcat = product.getProductsubcategoryid();
        
// E.g      "shipping.averageweight.fashion.babysclothing=0.0833"
        
        String a = this.format(cat.getProductcategory());
        String b = this.format(subcat.getProductsubcategory()); 

Log.getInstance().log(Level.FINER, "Category: {0}, sub category: {1}", this.getClass(), a, b);

        final String mSuffix = a + '.' + b;

        final String PREFIX = "shipping.averageweight";
        
        StringArrayComparatorImpl sc = new StringArrayComparatorImpl();

        float output = defaultValue;
        
        String name = null;
        
        Config propsSvc = WebApp.getInstance().getConfig();
        
        Set<String> names = propsSvc.getNames();
        
        for(String key:names) {
            
            if(key.startsWith(PREFIX)) {

                String suffix = key.substring(PREFIX.length() + 1);
                
Log.getInstance().log(Level.FINER, "For name: {0} comparing {1} with {2}", 
this.getClass(), key, mSuffix, suffix);
                
                if(sc.compare(this.format(suffix), this.format(mSuffix), this.comparisonTolerance)) {
                    
                    name = key;
                    
                    output = propsSvc.getFloat(key, defaultValue);
                    
                    break;
                }
            }
        }

Log.getInstance().log(Level.FINER, "Property: {0}, average weight: {1}", this.getClass(), name, output);
        
        return output;
    }
    
    public float getMinimumWeight() {
        String sval = WebApp.getInstance().getConfig().getString(AppProperties.SHIPPING_MIN_WEIGHT);
        return Float.parseFloat(sval);
    }
    
    public float getVatRate() {
        String sval = WebApp.getInstance().getConfig().getString(AppProperties.SHIPPING_VAT_RATE);
        return Float.parseFloat(sval);
    }
    
    public float getFee() {
        String sval = WebApp.getInstance().getConfig().getString(AppProperties.SHIPPING_FEE_AMOUNT);
        return Float.parseFloat(sval);
    }
    
    public Properties getDeliveryRates() {
        return this.getDeliveryRates(this.isExpress());
    }
    
    public Properties getNormalDeliveryRates() {
        return this.getDeliveryRates(false);
    }
    
    public Properties getExpressDeliveryRates() {
        return this.getDeliveryRates(true);
    }
    
    public Properties getDeliveryRates(boolean expressDelivery) {
        
        String expectedEnd = expressDelivery ? ".rate.express" : ".rate";
        
        Properties rates = new Properties();
        Set<String> names = shippingGroupProps.stringPropertyNames();
        for(String name:names) {
            if(name.startsWith("shipping.group") && name.endsWith(expectedEnd)) {
                rates.setProperty(name, shippingGroupProps.getProperty(name));
            }
        }
Log.getInstance().log(Level.FINER, "prefix={0}", this.getClass(), rates);

        return rates;
    }

    public String getDeliveryPeriod() {

        return this.getShippingPeriod();
    }

    public String getShippingPeriod() {

        String shippingGroup = this.getShippingGroup();
        
        return shippingGroup == null ? null : this.getShippingPeriod(shippingGroup, express);
    }

    public String getDeliveryPeriod(String shippingGroup, boolean expressDelivery) {
        
        return this.getShippingPeriod(shippingGroup, expressDelivery);
    }
    
    public String getShippingPeriod(String shippingGroup, boolean expressDelivery) {
        
        if(shippingGroup == null) {
        
            throw new NullPointerException();
        }
        
        // shipping.group4.period.express=10 working days
        // shipping.group4.period=5 working days
        
        String key = expressDelivery ? 
                shippingGroup + ".period.express" : 
                shippingGroup + ".period";
        
        return WebApp.getInstance().getConfig().getString(key);
    }

    public Properties getDeliveryPeriods() {
        return this.getDeliveryPeriods(this.isExpress());
    }
    
    public Properties getNormalDeliveryPeriods() {
        return this.getDeliveryPeriods(false);
    }
    
    public Properties getExpressDeliveryPeriods() {
        return this.getDeliveryPeriods(true);
    }
    
    public Properties getDeliveryPeriods(boolean expressDelivery) {
        
        String expectedEnd = expressDelivery ? ".period.express" : ".period";
        
        Properties periods = new Properties();
        Set<String> names = shippingGroupProps.stringPropertyNames();
        for(String name:names) {
            if(name.startsWith("shipping.group") && name.endsWith(expectedEnd)) {
                periods.setProperty(name, shippingGroupProps.getProperty(name));
            }
        }
Log.getInstance().log(Level.FINER, "prefix={0}", this.getClass(), periods);

        return periods;
    }
    
    public String getDeliveryLocation(){
        
        return WebApp.getInstance().getConfig().getString(this.getShippingGroup());
    }

    public Properties getDeliveryLocations() {
        
        Properties locs = new Properties();
        Set<String> names = shippingGroupProps.stringPropertyNames();
        for(String name:names) {
            if(name.startsWith("shipping.group") && name.endsWith(".locations")) {
                locs.setProperty(name, shippingGroupProps.getProperty(name));
            }
        }
Log.getInstance().log(Level.FINER, "prefix={0}", this.getClass(), locs);

        return locs;
    }

    public Properties getDeliveryGroups() {
        
        Properties groups = new Properties();
        Config props = WebApp.getInstance().getConfig();
        for(int i=0; i<20; i++) {
            String name = "shipping.group"+i;
            String sval = props.getString("shipping.group"+i);
            if(sval == null || sval.isEmpty()) {
                break;
            }
            groups.setProperty(name, sval);
        }
Log.getInstance().log(Level.FINER, "prefix={0}", this.getClass(), groups);

        return groups;
    }
    
    private String format(String sval) {
        return sval.toLowerCase().trim().replaceAll("\\W", "");
    }
    
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    
    public Integer getOrderId() {
        return shoppingCart.getOrderId();
    }
    
    public Address getAddress() {
        return shoppingCart.getOrderShipping() == null ? null : shoppingCart.getOrderShipping().getDeliveryAddress();
    }

    public UserBean getUser() {
        return shoppingCart.getUser();
    }

    public boolean isExpress() {
        return express;
    }

    public void setExpress(boolean express) {
Log.getInstance().log(Level.FINER, "Express delivery: {0}, updating to: {1}", this.getClass(), this.express, express);
        this.express = express;
    }

    public int getMaxShippingGroupsToCheck() {
        return maxPropertiesToIterate;
    }

    public void setMaxShippingGroupsToCheck(int maxShippingGroupsToCheck) {
        this.maxPropertiesToIterate = maxShippingGroupsToCheck;
    }

    public float getShippingGroupComparsionTolerance() {
        return comparisonTolerance;
    }

    public void setShippingGroupComparsionTolerance(float shippingGroupComparsionTolerance) {
        this.comparisonTolerance = shippingGroupComparsionTolerance;
    }

    private StringBuilder toString(Address address) {
        StringBuilder builder = new StringBuilder();
        this.appendAddress(address, builder);
        return builder;
    }    
        
    private void appendAddress(Address address, StringBuilder builder) {
        builder.append(Address.class.getSimpleName());
        builder.append("{country=").append(address.getCountryid().getCountry());
        if(address.getRegionid() != null) {
            builder.append(", state=").append(address.getRegionid().getRegion());
        }
        if(address.getCity() != null) {
            builder.append(", city=").append(address.getCity());
        }
        if(address.getCounty() != null) {
            builder.append(", county=").append(address.getCounty());
        }
        if(address.getStreetAddress() != null) {
            builder.append(", streetAddress=").append(address.getStreetAddress());
        }
        builder.append('}');
    }

    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("{express=").append(this.express);
        if(this.shoppingCart != null && this.shoppingCart.getOrderShipping() != null
                && this.shoppingCart.getOrderShipping().getDeliveryAddress() != null) {
            builder.append(", address=[");
            this.appendAddress(null, builder); builder.append(']');
        }
        builder.append('}');
        return builder.toString();
    }
}
