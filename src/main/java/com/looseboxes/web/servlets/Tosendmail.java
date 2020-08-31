package com.looseboxes.web.servlets;

import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.security.GeneralSecurityException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Josh
 */
@WebServlet(name="Tosendmail", urlPatterns={"/tosendmail"})
public class Tosendmail extends BaseServlet {

    public Tosendmail() { }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.SEND_MAIL;
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.SEND_MAIL;
    }

    @Override
    public void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {

        String mailType = request.getParameter(Sendmail.MAIL_TYPE);
        if(mailType == null) throw new NullPointerException();
        request.setAttribute(Sendmail.MAIL_TYPE, mailType);

        // If the user is logged in then emailAddress will already be available
        // as a session attribute
        //
        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        String str = user.getEmailAddress();

        String encodedEmail = null;

        if(str != null) {
            try{
                encodedEmail = WebApp.getInstance().getEncryption().encrypt(str.toCharArray());
            }catch(GeneralSecurityException e) {
                throw new ServletException("An unexpected error occured while processing the request", e);
            }
        }else{
            encodedEmail = request.getParameter(Sendmail.SENDER_EMAIL);
        }
        if(encodedEmail != null) {
            request.setAttribute(Sendmail.SENDER_EMAIL, encodedEmail);
        }

        String username = (String)request.getSession().getAttribute(Siteuser_.username.getName());
        String encodedUsername;
        if(username != null) {
            try{
                encodedUsername = WebApp.getInstance().getEncryption().encrypt(username.toCharArray());
            }catch(GeneralSecurityException e) {
                throw new ServletException("An unexpected error occured while processing the request", e);
            }
        }else{
            encodedUsername = request.getParameter(Sendmail.SENDER_NAME);
        }
        if(encodedUsername != null) {
            request.setAttribute(Sendmail.SENDER_NAME, encodedUsername);
        }

        if(mailType.equals(Sendmail.NOTIFICATION)) {

            String productTable = (String)ServletUtil.find(Product_.productcategoryid.getName(), request);
            request.getSession().setAttribute(Product_.productcategoryid.getName(), productTable);

//@related_7
            request.setAttribute(Product_.productid.getName(), request.getParameter(Product_.productid.getName()));
            request.setAttribute(Product_.productid.getName()+"s", request.getParameter(Product_.productid.getName()+"s"));
        }
    }
}
