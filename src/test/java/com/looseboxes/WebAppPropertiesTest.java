package com.looseboxes;

import com.looseboxes.web.AppProperties;
import com.looseboxes.web.WebApp;
import java.util.Collection;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.bc.config.ConfigGroup;
import com.bc.config.ConfigService;
import com.bc.config.Config;
import com.bc.config.ConfigData;


/**
 * @(#)WebAppPropertiesTest.java   23-May-2015 15:17:32
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
public class WebAppPropertiesTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TestWebApp.init();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        TestWebApp.destroy();
    }
    
    @Test
    public void testAll() {
       
        ConfigService configSvc = WebApp.getInstance().getConfigService();
        
        ConfigGroup configGroup = configSvc.getConfigs();
        
        Config<Properties> config = WebApp.getInstance().getConfig();
        
        Collection<String> freeShippingCountries = config.getCollection(AppProperties.FREESHIPPING_COUNTRIES);
System.out.println(this.getClass().getName()+". Free shipping countries: "+freeShippingCountries);
        
System.out.println(this.getClass().getName()+". PropertiesService: "+config);

System.out.println(this.getClass().getName()+". PropertiesService.stringPropertyNames: "+config.getNames());

        Config shipping = configGroup.get("shipping"); 
System.out.println(this.getClass().getName()+". Configuration.get('shipping'): "+shipping);

        Config shippingProps = configGroup.get("shipping.properties");
System.out.println(this.getClass().getName()+". PropertiesService.get('shipping.properties'): "+shippingProps);

        ConfigData columnToLabel = config.subset("column_to_label", ".");
System.out.println(this.getClass().getName()+". PropertiesService.subset('column_to_label', '.'): "+columnToLabel);

        Properties props = config.getSourceData(); 
System.out.println(this.getClass().getName()+". PropertiesService.getProperties().keySet(): "+props.keySet());
        
System.out.println(this.getClass().getName()+". PropertiesService.stringPropertyNames(): "+config.getNames());
    }
}
