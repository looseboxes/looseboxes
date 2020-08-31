package com.looseboxes.web.components;

import com.looseboxes.pu.LbJpaContext;
import com.looseboxes.pu.entities.Productorder;
import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.bc.jpa.context.JpaContext;
import java.net.URI;

/**
 * @author Josh
 */
public class SellerOrdersTest {
    
    public SellerOrdersTest() { }
    
    @BeforeClass
    public static void setUpClass() { }
    
    @AfterClass
    public static void tearDownClass() { }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }

    /**
     * Test of initRecords method, of class SellerOrders.
     * @throws java.lang.Exception
     */
    @Test
    public void testAll() throws Exception {
        
log("testAll");
        URI uri = new URI("file:/C:/Users/Josh/Documents/NetBeansProjects/looseboxespu/src/test/resources/META-INF/persistence.xml");
        final JpaContext jpaContext = new LbJpaContext(uri);
        UserOrders userOrdersBean = new UserOrders(){
            @Override
            public JpaContext getJpaContext() {
                return jpaContext;
            }
        };
        userOrdersBean.setEmailAddress("posh.bc@gmail.com");
        LinkedList<Productorder> userOrders = userOrdersBean.getRecords();
log(userOrders==null?null:userOrders.size());        

        SellerOrders sellerOrdersBean = new SellerOrders(){
            @Override
            public JpaContext getJpaContext() {
                return jpaContext;
            }
        };
        sellerOrdersBean.setEmailAddress("buzzwears@yahoo.com");
        LinkedList<Productorder> sellerOrders = sellerOrdersBean.getRecords();
log(sellerOrders==null?null:sellerOrders.size());        

    }
    
    private void log(Object msg) {
System.out.println(this.getClass().getName()+". "+msg);        
    }
}
