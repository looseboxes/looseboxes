package com.looseboxes.web.servlets;

/**
 * @(#)BaseServlet.java   23-Apr-2015 20:38:14
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.util.Log;
import com.bc.web.core.util.Util;
import com.bc.html.HtmlGen;
import com.bc.html.HtmlPageGen;
import com.looseboxes.web.HasMessages;
import com.looseboxes.web.Attributes;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.exceptions.LoginException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class BaseServlet extends HttpServlet implements HasMessages {

    private transient static final Logger LOG = Logger.getLogger(BaseServlet.class.getName());
    
    public static final String NEXT_SERVLET_CLASSNAME = "NextServletClassName";
    
    private Map<MessageType, StringBuilder> messages;
    
    /**
     * <p>
     * Don't not call this method directly. Preferably call 
     * {@link #processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) processRequest(HttpServletRequest, HttpServletResponse)}
     * or {@link #processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, boolean) processRequest(HttpServletRequest, HttpServletResponse, boolean)}.
     * However if you must call this method make sure you call 
     * {@link #init(javax.servlet.http.HttpServletRequest) init(HttpServletRequest)} before and
     * {@link #destroy(javax.servlet.http.HttpServletRequest) destroy(HttpServletRequest)} after.
     * </p>
     * <p>
     * This method is called by the method 
     * {@link #processRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse) processRequest(HttpServletRequest, HttpServletResponse)}
     * Sub classes should override this method to add functionality to the Servlet.
     * </p>
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected abstract void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
    
    public abstract String getForwardPage(HttpServletRequest request); 
    
    public Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> params = ServletUtil.getParameterMap(request, false, false);
        return params;
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void processRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
//@related NextServletClass session attribute
//            
        final HttpSession session = request.getSession();
        
        final String nextServletClassName = (String)session.getAttribute(BaseServlet.NEXT_SERVLET_CLASSNAME);

        LOG.log(nextServletClassName==null?Level.FINER:Level.FINE, 
        () -> MessageFormat.format("Next Servlet Class Name: {0} for session: {1}", 
        nextServletClassName, session.getId()));

        Class nextServletClass = null;
        
        if(nextServletClassName != null) {
            try{
                nextServletClass = Class.forName(nextServletClassName);
            }catch(Exception e) {
                LOG.log(Level.WARNING, "Error loading class: "+nextServletClassName, e);
            }
        }
        
        final boolean sameClass = nextServletClass == this.getClass();
        
        final boolean forward = nextServletClass == null || sameClass;
        
        this.processRequest(request, response, forward);

        LOG.log(nextServletClassName==null?Level.FINER:Level.FINE, 
        () -> MessageFormat.format("Removing attribute {0} = {1} for session: {2}", 
        BaseServlet.NEXT_SERVLET_CLASSNAME, nextServletClassName, session.getId()));

//@related NextServletClass session attribute
//            
        request.getSession().removeAttribute(BaseServlet.NEXT_SERVLET_CLASSNAME);

        if(!forward && nextServletClass != null && !sameClass) {

            try{

                BaseServlet baseServlet = (BaseServlet)nextServletClass.getConstructor().newInstance();

                LOG.fine(() -> MessageFormat.format("Transfering control from: {0} to {1}",
                this.getClass().getName(), nextServletClassName));

                baseServlet.processRequest(request, response);

            }catch(NoSuchMethodException | InstantiationException | 
                    IllegalAccessException | InvocationTargetException e) {

                throw new ServletException("An unexpected error occured while processing the request", e);
            }
        }
    }

    /**
     * @param request The request to process
     * @param response The response to send
     * @param forward If true the response will be sent after the request is process, false otherwise
     * @throws ServletException
     * @throws IOException 
     */
    public void processRequest(
            HttpServletRequest request, HttpServletResponse response, boolean forward)
            throws ServletException, IOException {
        
        try{
            
            this.init(request);
            
            if(response != null) {
                response.setContentType("text/html;charset=UTF-8");
            }

            this.handleRequest(request, response);
            
            this.buildMessages(request);

            if(forward) {
                this.forward(request, response);
            }
            
        }catch(ServletException | IOException | RuntimeException e) {
            
            String page = this.getErrorPage(request, e);
            
            if(page == null) {
    
                final String msg = this.getClass().getName() + 
                " does not define an error page for this exception type hence exception will be re-thrown";
                
                if(WebApp.getInstance().isProductionMode()) {
                    LOG.log(Level.WARNING, msg + '\n', e.toString());
                }else{
                    LOG.log(Level.WARNING, msg, e);
                }

                this.buildMessages(request);

                throw e;
                
            }else{

                this.log(e);

                if(e instanceof ServletException) {
                    this.addMessage(MessageType.warningMessage, e.getLocalizedMessage());
                }else{
                    this.addMessage(MessageType.warningMessage, "An unexpected error occured while processing the request");
                }
                
                this.buildMessages(request);
            
                if(forward) {
                    
                    this.forwardError(e, request, response);
                }
            }
        }finally{
            
            this.destroy(request);
        }
    } 
    
    public void init(HttpServletRequest request) throws ServletException {  }

    public void destroy(HttpServletRequest request) throws ServletException {  
        messages = null;
    }
    
    public void buildMessages(HttpServletRequest request) {
        // @related Map myMessages. key=messageType, value=messageTypeValue
        request.setAttribute("myMessages", this.messages);
        Level level = this.messages == null || this.messages.isEmpty() ? Level.FINER : Level.FINE;
        LOG.log(level, "Request Message(s): {0}", this.messages);
    }
    
    @Override
    public void addMessage(MessageType messageType, Object message) {
        if(message == null) {
            return;
        }
        if(messages == null) {
            messages = new EnumMap<>(MessageType.class);
        }
        StringBuilder messageBuffer = messages.get(messageType);
        if(messageBuffer == null) {
            messageBuffer = new StringBuilder();
            messages.put(messageType, messageBuffer);
        }
        this.addMessage(messageBuffer, message);
    }

    private boolean addMessage(StringBuilder builder, Object message) {
        if(message == null) {
            return false;
        }
        if(builder.length() > 0) {
            builder.append("<br/>");
        }
        builder.append(message);
        return true;
    }
    
    @Override
    public boolean hasMessages() {
        return messages != null && !messages.isEmpty();
    }
    
    @Override
    public Map<MessageType, StringBuilder> getMessages() {
        return messages;
    }
   
    @Override
    public void addMessages(Map<MessageType, ?> messages) {
        if(messages == null || messages.isEmpty()) {
            return;
        }
        for(MessageType messageType:messages.keySet()) {
            this.addMessage(messageType, messages.get(messageType));
        }
    }

    public void forward(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String page = this.getTargetPage(request);
        if(page == null) {
            page = this.getForwardPage(request);
        }
        if(page == null) {
            page = ServletUtil.getSourcePage(request, WebPages.PRODUCTS_SEARCHRESULTS);
        }
        
        LOG.log(Level.FINE, "Forwarding to page: {0}", page);
        
        if(page == null) {
            throw new UnsupportedOperationException();
        }

        boolean redirect = false;
        if(Util.isHttpUrl(page)) {
            URL contextUrl = WebApp.getInstance().getContextURL();
            String sval = contextUrl.toExternalForm();
            int i = page.indexOf(sval);
            if(i != -1) {
                String relative = page.substring(i + sval.length());
                if(LOG.isLoggable(Level.FINER)) {
                    LOG.log(Level.FINER, "Formatted forward page from {0} to {1}",
                            new Object[]{page, relative});
                }
                page = relative;
            }else{
                redirect = true;
            }
        }
        
        if(redirect) {
            
            response.sendRedirect(response.encodeRedirectURL(page));
            
        }else{
            
            RequestDispatcher rd = request.getRequestDispatcher(page);

            if(rd == null) {
                throw new NullPointerException("Could not get "+RequestDispatcher.class.getName()+" for page: "+page);
            }else{
                rd.forward(request, response);
            }
        }
    }
    
    public void forwardError(Throwable t, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String page = this.getErrorPage(request, t);
        LOG.log(Level.FINE, "Forwarding to error page: {0}", page);
        
        if(page == null) {
            throw new UnsupportedOperationException("No error page defined for exception type: "+t.getClass().getName());
        }
        
        RequestDispatcher rd = request.getRequestDispatcher(page);
        
        if(rd == null) {
            throw new NullPointerException("Could not get "+RequestDispatcher.class.getName()+" for page: "+page);
        }else{
            rd.forward(request, response);
        }
    }

    public String getTargetPage(HttpServletRequest request) {
        Object oval = ServletUtil.find(Attributes.TARGET_PAGE, request);
        String sval = oval == null ? null : oval.toString();
        return sval;
    }
    
    public String getErrorPage(HttpServletRequest request, Object message) {
        String errorPage;
        if(message instanceof LoginException) {
            errorPage = WebPages.LOGIN;
        }else {
            errorPage = null;
        }
        return errorPage;
    }

    public void log(Throwable thrown) {
        if(WebApp.getInstance().isProductionMode() && 
                thrown instanceof com.looseboxes.web.exceptions.LoginException && 
                !Log.getInstance().isLoggable(Level.FINE, this.getClass())) {
            LOG.warning(thrown.toString());
        }else{
            LOG.log(Level.WARNING, null, thrown);
        }
    }
    
    /**
     * Send a message by writing directly to the response
     * @param messageType The type of message
     * @param title The title of the page to which the message will be written
     * @param heading The message heading
     * @param body The message body
     * @param request The servlet request
     * @param response The servlet response to be written to
     */
    public void sendMessage(MessageType messageType, String title,
            String heading, String body, ServletRequest request, ServletResponse response) {

        LOG.log(Level.FINE, "Writing message directly to response");

        String str = this.createMessage(messageType, title, heading, body);

        StringBuilder message = new StringBuilder("<p style=\"width:100%; text-align:justify\">");

        message.append(str);
        message.append("<br/><br/>To return to ");
        message.append(WebApp.getInstance().getName());
        message.append(" click ");
        HtmlGen.AHREF(WebPages.INDEX, "here", message);

        message.append("</p>");

        response.setContentType("text/html");

        PrintStream ps = null;
        PrintWriter pw = null;
        ServletOutputStream sos = null;

        try {
            sos = response.getOutputStream();
            ps = new PrintStream(sos);
            pw = new PrintWriter(ps);
            pw.print(message);
        }catch(Exception e) {
            LOG.warning(e.toString());
        }finally{
            if(pw != null) pw.close();
            if(ps != null) ps.close();
            try{
                if(sos != null) sos.close();
            }catch(IOException e) {
                LOG.warning(e.toString());
            }
        }
    }

    private String createMessage(MessageType messageType, String title,
            String heading, String body) {

        if (body == null) {
            if(heading == null) {
                throw new NullPointerException("Message has no heading and no body");
            }else{
                body = "";
            }
        }else{
            if (heading==null) {
                heading = "An unexpected error occured while processing your request!";
            }
        }

        if (title == null && messageType != null) title = this.geDefaultTitle(messageType);

        return new HtmlPageGen().getPage(title, heading, body).toString();
    }

    private String geDefaultTitle(MessageType messageType) {
        switch(messageType) {
            case warningMessage:
                return "Error";
            default:
                return "Info";
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
/**
 * 

    public MessageDispatcher getMessageDispatcher() {
        return this;
    }

    @Override
    public void forwardErrorMessage(
            Object message,
            HttpServletRequest request,
            HttpServletResponse response) {
        String errorPage = this.getErrorPage(request, message);
        if(errorPage == null) {
            errorPage = WebPages.ERROR_DEFAULT;
        }
        this.fowardErrorMessage(errorPage, message, request, response);
    }
    
    @Override
    public boolean fowardErrorMessage(String page, Object message,
            ServletRequest request, ServletResponse response) {
        
        return this.foward(MessageType.warningMessage, message, page, request, response);
    }

    @Override
    public boolean fowardMessage(String page, Object message,
            ServletRequest request, ServletResponse response) {

        return this.foward(MessageType.informationMessage, message, page, request, response);
    }

    @Override
    public boolean foward(MessageType msgType, Object oval, String page,
            ServletRequest request, ServletResponse response) {

        String sval =  this.getMessage(oval);
        
XLogger.getInstance().log(Level.FINER, "Page: {0}, Input: {1}, Output: {2}", 
        this.getClass(), page, oval, sval);

        try {

            // Adds pending messages to this message and flushes the message buffer
            //
            sval = this.combineSessionMessages((HttpServletRequest)request, sval, msgType);
            
            RequestDispatcher dispatcher = request.getRequestDispatcher(page);

XLogger.getInstance().log(Level.FINER, 
"Response is committed: {0}, Dispatcher: {1}, Message: {2}", 
this.getClass(), response.isCommitted(), dispatcher, sval);

            dispatcher.forward(request, response);

            return true;

        }catch(IOException | ServletException | RuntimeException e) {

            StringBuilder logMsg = new StringBuilder("While trying to forward");

            if(sval != null) {
                logMsg.append(" message: (");
                logMsg.append(sval);
                logMsg.append(")");
            }else{
                logMsg.append(" request");
            }

            logMsg.append(" to page: ");
            logMsg.append(page);
            logMsg.append("\nEncountered the following problem: ");

            sval = logMsg.toString();

            XLogger.getInstance().log(Level.WARNING, logMsg.toString(), this.getClass(), e);

            this.sendMessage(
                    MessageType.warningMessage, "text/html", 
                    "Error Message", "INTERNAL ERROR", 
                    sval, request, response);

            return false;
        }
    }
    
    private String getMessage(Object oval) {
        
        String message;
        
        if(oval instanceof Throwable) {

            Throwable tval = (Throwable)oval;

            message = this.getMessage(tval);
            
        }else{
            
            message = oval == null ? null : oval.toString();
        }
        
        return message;
    }
    
    private String getMessage(Throwable tval) {
        String sval;
        if (tval instanceof ServletException) {
            sval = tval.getLocalizedMessage();
        }else{
            sval = null;
        }
XLogger.getInstance().log(Level.FINER, "Throwable: {0}, Message: {1}", this.getClass(), tval, sval);
        return sval;
    }

    @Override
    public void log(Object message) {
        
        if(message instanceof Throwable) {

            Throwable tval = (Throwable)message;

            log(tval);
            
        }else{
            
            XLogger.getInstance().log(Level.INFO, "{0}", this.getClass(), message);
        }
    }
    
    @Override
    public String getTypeString(MessageType messageType) {
        return messageType.toString();
    }

    @Override
    public void sendMessage(String title, String heading,
            String bodyContents, ServletRequest request, ServletResponse response) {

        this.sendMessage(MessageType.informationMessage, "text/html",
                title, heading, bodyContents, request, response);
    }

    @Override
    public void sendErrorMessage(String heading, Exception t, ServletRequest request, ServletResponse response) {

        String body = this.getMessage(t);

        this.sendErrorMessage(heading, body, request, response);
    }

    @Override
    public void sendErrorMessage(String heading, String body, ServletRequest request, ServletResponse response) {

       this.sendMessage(MessageType.warningMessage, "text/html", null,
               heading, body, request, response);
    }

    @Override
    public String combineSessionMessages(HttpServletRequest request, Throwable message) {
        String msg = this.getMessage(message);
        return this.combineSessionMessages(request, msg, MessageType.warningMessage);
    }

    @Override
    public String combineSessionMessages(HttpServletRequest request, Object message, MessageType msgType) {
        
        return this.combineSessionMessages(request, this.getMessage(message), msgType);
    }
    
    @Override
    public String combineSessionMessages(HttpServletRequest request, String message, MessageType msgType) {
        
        String requestMsg = this.combineMessages(request.getSession(), message);

        if(requestMsg != null) {
            // @related Map myMessages. key=messageType, value=messageTypeValue
            request.setAttribute("messageType", getTypeString(msgType));
            /// NOTE: NO MORE COMBINATION NEEDED myMessages should be a Map
            request.setAttribute("myMessages", requestMsg);
        }
XLogger.getInstance().log(Level.FINER, "Message: {0}", this.getClass(), requestMsg);
        return requestMsg;
    }
    
    private String combineMessages(HttpSession session, String msgStr) {

        StringBuilder sessionMessages =
                (StringBuilder)session.getAttribute("sessionMessages");

        String output;
        if (sessionMessages == null || sessionMessages.length() == 0) {
            output = msgStr;
        }else{
            synchronized(sessionMessages) {
                if(msgStr != null) {
                    sessionMessages.append("<br/>");
                    sessionMessages.append(msgStr);
                }
                output = sessionMessages.toString();
                // Reset the original messages
//                sessionMessages = new StringBuilder(); this didn't work well
                sessionMessages.setLength(0);
            }
        }
        return output;
    }

    private StringBuilder getSessionMessages(HttpSession session) {

        StringBuilder sessionMessages =
                (StringBuilder)session.getAttribute("sessionMessages");

        if(sessionMessages == null) {
            sessionMessages = new StringBuilder();
            session.setAttribute("sessionMessages", sessionMessages);
        }
XLogger.getInstance().log(Level.FINER, "Session messages: {0}", this.getClass(), sessionMessages);
        return sessionMessages;
    }

    protected void checkUser(HttpServletRequest request, Map parameters) throws LoginException {

        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);

        // User may have logged out
        if(!user.isLoggedIn()) {
            throw new LoginException("You must be signed in to perform the requested operation");
        }

        String userEmail = user.getEmailAddress();

        Object tgtEmail = parameters.get(Siteuser_.emailAddress.getName());

        // When a user is editing tgtEmail will be available
        // When a user is deleting it will be null
        // In other words tgtEmail may not always be available
        //
        if(tgtEmail == null) return;

        if(!userEmail.equals(tgtEmail.toString())) {

            throw new LoginException("You must be logged in as '"+tgtEmail+"' to perform the requested operation");
        }
    }
    
 * 
 */