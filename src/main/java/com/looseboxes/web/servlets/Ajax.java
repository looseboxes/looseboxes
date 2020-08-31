package com.looseboxes.web.servlets;

/**
 * @(#)Ajax.java   26-May-2013 15:57:28
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
import com.bc.oauth.OAuthSessionIx;
import static com.bc.oauth.OAuthSessionProperties.PROVIDER;
import static com.bc.oauth.OAuthSessionProperties.TYPE;
import com.bc.oauth.OAuthSessions;
import com.bc.validators.AbstractDatabaseInputValidator.UserType;
import com.bc.validators.ValidationException;
import com.bc.web.core.form.Form;
import com.bc.web.core.form.Form.ActionType;
import com.looseboxes.web.ChatService;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.UserBean;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.looseboxes.web.components.forms.SimpleFormImpl;
import com.bc.jpa.context.JpaContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Ajax", urlPatterns={"/ajax"})
public class Ajax extends HttpServlet {

    private transient static final Logger LOG = Logger.getLogger(Ajax.class.getName());
    
    private static transient final Lock lock = new ReentrantLock();
    
    private static transient AjaxSearch previousAjaxSearch;
    
    public Ajax() { }

    @Override
    public void init() throws ServletException {
        super.init();
        LOG.finer("Done Initializing");
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(
            HttpServletRequest request, 
            HttpServletResponse response) {

        String value;
        
        try{
            
            value = this.handleRequest(request, response);
            
        }catch(Exception e) {
            //@related_41
            value = "error";
            log(e);
        }
        
        PrintWriter out = null;
        
        try{
            
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            
            out = response.getWriter();

            out.write(value);
            
        }catch(Exception e){
            log(e);
        }finally{
            if(out != null) {
                out.close();
            }
        }
    }
    
    private String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        final String type = request.getParameter("type");
        
        if(type == null) {
            String msg = "No action type specified";    
            final Enumeration<String> en = request.getParameterNames();
            final List<String> paramNames = en == null ? Collections.EMPTY_LIST : Collections.list(en);
            LOG.warning(() -> "No action type specified in request parameters: " + paramNames);            
            return msg;
        }
        
        LOG.finer(() -> "Type: " + type);
        
        String value = null;

        if(type.startsWith("search")) {
            
            try{
                
                lock.lock();

                if(previousAjaxSearch != null) {
//System.out.println("\t\t============   Stopping: " + previousAjaxSearch);                    
                    previousAjaxSearch.setStopped(true);
                    previousAjaxSearch = null;
                } 

                previousAjaxSearch = new AjaxSearch();
//System.out.println("\t\t============    Created: " + previousAjaxSearch);
            }finally{
                
                lock.unlock();
            }
            
//System.out.println("\t\t============ Retrieving: " + previousAjaxSearch);
            value = previousAjaxSearch.getResults(request);
//System.out.println("\t\t============  Retrieved: " + previousAjaxSearch);            
        }else if(type.equalsIgnoreCase("validate")) {
            
            value = getValue(request);
            
        }else if(type.equalsIgnoreCase("chat")) {
            
            StringBuilder builder = ChatService.forAjax(request, response);
            
            if(builder != null) {
                value = builder.toString();
            }
            
            LOG.finer(value);            
            
        }else if(type.equals("cart")) {
            
            com.looseboxes.web.servlets.ShoppingCart cartServlet = new com.looseboxes.web.servlets.ShoppingCart();
            cartServlet.handleRequest(request, response);
            UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
            com.looseboxes.web.components.ShoppingCart cart = user.getShoppingCart();
            int itemCount = cart == null ? 0 : cart.getItemCount();
            value = Integer.toString(itemCount);
            
	}else if (OAuthSessions.accept(type)) {
            
            final String oauthProvider = request.getParameter(PROVIDER);
            final String oauthType = request.getParameter(TYPE);
            
            if(oauthProvider == null || oauthType == null) {
                throw new NullPointerException();
            }
            
            OAuthSessionIx oauthSession = OAuthSessions.getInstance(oauthProvider, oauthType, OAuth.getProviderProperties(oauthProvider));

            value = oauthSession.getAuthURL();
            
        }else if("convcurr".equals(type)) {
            
//            LOG.log(Level.FINER, "Converting Currencies");            

            Convcurr convcurr = new Convcurr();
            
            convcurr.handleRequest(request, response);
            
//            LOG.log(Level.FINER, "Done converting currencies, result: {0}", convcurr.getLastResult());
            
            if(convcurr.getLastResult() > 0) {
                StringBuilder builder = new StringBuilder();
                builder.append("1 ").append(convcurr.getFromCode());
                builder.append(" = ").append(convcurr.getLastResult()).append(convcurr.getToCode());
                value = builder.toString();
                LOG.log(Level.FINEST, "Value: {0}", convcurr.getLastResult());            
            }
        }
        
        LOG.log(Level.FINER, "Value: {0}", value);        

        if(value == null) { value = ""; }
        
//        if(!value.isEmpty()) {
            
            // Very important
            //
//            value = EscapeChars.forXML(value);
//        }
        
        return value;
    }

    private String getValue(HttpServletRequest request)
            throws ServletException, IOException {

        String message = null;
        
        String tableName;
        
        String columnName;
        
        String columnValue;
        
        try {

            // Expected format 'tableName=aTable&columnName=aColumn'
            //
            final Map<String, String> params = ServletUtil.getParameterMap(request, false, false);
            LOG.fine(() -> "Parameters: " + params);

            params.remove("type");
            
            tableName = params.remove("tableName");

            // @related_48
            final String userTypeString = params.remove("userType");
            
            // Should be only one pair left at this time
            columnName = params.keySet().iterator().next();
            columnValue = params.get(columnName);

            if(LOG.isLoggable(Level.FINER)) {
                LOG.log(Level.FINER, "Table: {0}, Column: {1}, Value: {2}", 
                        new Object[]{tableName, columnName, columnValue});
            }        

            if (tableName != null && userTypeString != null) {
                
                JpaContext cf = WebApp.getInstance().getJpaContext();
                
//                Class entityClass = cf.getMetaData().getEntityClass(
//                        WebApp.getInstance().getDatabaseName(), null, tableName);
                Class entityClass = cf.getMetaData().getEntityClass(
                        null, null, tableName);
                
                UserType userType = UserType.valueOf(userTypeString);
                
                if(LOG.isLoggable(Level.FINER)) {
                    LOG.log(Level.FINER, "Table: {0}, entity type: {1}, user type: {2}", 
                            new Object[]{tableName, entityClass, userType});
                }        
                
                final Form form = this.getForm(request, entityClass, "Ajax.form", null);
                
                final com.looseboxes.web.form.FormValidator formValidator = 
                        new com.looseboxes.web.form.FormValidator(userType){
                    @Override
                    public String getConfirmPasswordName() {
                        return null;
                    }
                };
                
                formValidator.setForm(form);
                
                formValidator.validateField(columnName, columnValue);
                
            }else{
                
                message = "Unable to validate";
            }
        }catch(ValidationException ve) {

            LOG.log(Level.FINER, "Error validating", ve);

            message = ve.getMessage();

        }
        
        LOG.log(Level.FINE, "Message: {0}", message);

        return (message == null || message.length() == 0) ? "valid" : message;
    }
    
    public <E> Form getForm(
            HttpServletRequest request, Class<E> entityClass, String formId, ActionType actionType) 
            throws ServletException {
        
        com.bc.web.core.form.Form<E> form = 
                (com.bc.web.core.form.Form<E>)com.bc.web.core.util.ServletUtil.find(formId, request);
        
        if(form == null) {
            
            form = new SimpleFormImpl(actionType, formId, entityClass); 
                
            // This must be set once, immediately after creation
            form.setRequest(request);
        }
        
        return form;
    }
    
    private void log(Exception e) {
        if(WebApp.getInstance().isProductionMode()) {
            LOG.log(Level.WARNING, "{0}", e.toString());
        }else{
            LOG.log(Level.WARNING, "Error encountered in Ajax servlet", e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
