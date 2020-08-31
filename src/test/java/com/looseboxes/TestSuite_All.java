package com.looseboxes;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * @(#)LooseboxesTestSuite.java   13-May-2015 19:19:12
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
@RunWith(Suite.class)
@Suite.SuiteClasses({ShoppingCartTest.class})
//@Suite.SuiteClasses({InsertProductTest.class})
//@Suite.SuiteClasses({FormTest.class, com.looseboxes.ParametersConversionTest.class, DeliveryTest.class})
//@Suite.SuiteClasses({DeliveryTest.class})
//@Suite.SuiteClasses({InsertProductTest.class, InsertProductTest.class, InsertProductTest.class})
//@Suite.SuiteClasses({ViewimgTest.class})
public class TestSuite_All {
    
    public static final Level level = Level.INFO;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
System.out.println(TestSuite_All.class.getName()+"#setUpClass");    
        TestWebApp.init();
        
        Logger logger = Logger.getLogger(TestSuite_All.class.getPackage().getName());
        Handler [] handlers = logger.getHandlers();
        Handler ch = null;
        if(handlers != null) {
            for(Handler handler:handlers) {
System.out.println(TestSuite_All.class.getName()+"#setUpClass. Handler: "+handler);    
                if(handler instanceof ConsoleHandler) {
                    ch = handler;
                    break;
                }
            }
        }
        if(ch == null) {
            ch = new ConsoleHandler();
        }
        logger.setLevel(level);
        ch.setLevel(level);
        logger.addHandler(ch);
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
System.out.println(TestSuite_All.class.getName()+"#tearDownClass");    
        TestWebApp.destroy();
    }
    
    @Before
    public void setUp() throws Exception { 
System.out.println(this.getClass().getName()+"#setUp");  
// Not called.. ????
    }
    
    @After
    public void tearDown() throws Exception { 
System.out.println(this.getClass().getName()+"#tearDown");    
// Not called.. ????
    }
}
