package com.looseboxes.web.filters;

import com.bc.web.core.filters.BaseFilter;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.Attributes;
import com.looseboxes.web.HasMessages;
import com.looseboxes.web.exceptions.LoginException;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Josh
 */
@WebFilter(filterName  = "LoggedinuserFilter",
           asyncSupported=true, 
           urlPatterns = {
                "/insert", "/edit", "/editproduct", "/delete",
                "/editvariant",
//                "/cart", "/shoppingCart", !!! NOTE: A user can add/remove without being logged in !!!
//                "/checkout", // Checkout has its custom login page
                "/insertShipping", "/insertshipping",
                "/paymentsvc", "/addshipping", "/addpayment",
                "/ipasfc", "/apasfc",
                "/contactux", /** This requires login but /respondtoux doesn't */
                "/poh", "/searchorders",
                "/submitComplain"
                         } 
           )
public class LoggedinuserFilter extends BaseFilter {

    private transient static final Logger LOG = Logger.getLogger(LoggedinuserFilter.class.getName());
    
    public LoggedinuserFilter() { }    
    
    @Override
    public void doFilter(
            ServletRequest request, 
            ServletResponse response, 
            FilterChain chain) throws IOException, ServletException {

        Map<HasMessages.MessageType, StringBuilder> messages = null;
        
        try{
            
            super.doFilter(request, response, chain);
            
        }catch(IOException | ServletException | RuntimeException e) {
            
            if(e instanceof ServletException) {
                messages = Collections.singletonMap(HasMessages.MessageType.warningMessage, new StringBuilder(e.getLocalizedMessage()));
            }
            
            // No need to log here as it is logged in the super class
            
            throw e;
            
        }finally{
            
            // @related Map myMessages. key=messageType, value=messageTypeValue
            request.setAttribute("myMessages", messages);
            Level level = messages == null || messages.isEmpty() ? Level.FINER : Level.FINE;
            LOG.log(level, "Request Message(s): {0}", messages);
            
        }
    }
    

    @Override
    public boolean doBeforeProcessing(
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        HttpSession session = request.getSession();

        com.looseboxes.web.components.UserBean user = (com.looseboxes.web.components.UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);

        if (! user.isLoggedIn()) {
            
            String targetPage = ServletUtil.getSourcePage(request, WebPages.PRODUCTS_SEARCHRESULTS);
            LOG.log(Level.FINER, "Target Page: {0}", targetPage);            
            //@related_24
            request.setAttribute(Attributes.TARGET_PAGE, targetPage);

            //@related_33 If the user logs in, this saved value would be used
            // to direct the user to the appropriate product's page
            //
            Object id = ServletUtil.find(Product_.productid.getName(), request);
            if(id != null) {
                session.setAttribute(Product_.productid.getName(), id);
            }

            throw new LoginException("You must be signed in to perform the requested operation");
        }
        
        return true;
    }
}
