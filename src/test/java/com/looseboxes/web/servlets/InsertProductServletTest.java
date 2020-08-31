package com.looseboxes.web.servlets;

import com.looseboxes.BaseServletTest;
import com.looseboxes.TestWebApp;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Josh
 */
public class InsertProductServletTest {
    
    public InsertProductServletTest() { }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        TestWebApp.init();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
        TestWebApp.destroy();
    }
    
    @Before
    public void setUp() { }
    
    @After
    public void tearDown() { }

    @Test
    public void testAll() throws Exception {
        
//        HttpSession session = TestWebApp.getSession();
        
//        UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);
        
        BaseServletTest servletTest = new BaseServletTest();
        
        if(!TestWebApp.isUserLoggedIn()) {
            Login loginServlet = new Login();
            servletTest.setBaseServlet(loginServlet);
            servletTest.testServlet();
        }
        
        InsertProductServlet insertProductServlet = new InsertProductServlet();
        servletTest.setBaseServlet(insertProductServlet);
        servletTest.addExclusiveParameter("cat", "fashion");
//        servletTest.setForwardResponse(false);
        servletTest.testServlet();
    }
}
