package com.looseboxes.web.servlets;

import com.bc.security.Encryption;
import com.bc.validators.ValidationException;
import com.bc.validators.Validator;
import com.bc.validators.ValidatorFactory;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.mail.InvitationMail;
import com.looseboxes.web.mail.NotificationMail;
import com.looseboxes.web.mail.ProductMail;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;

/**
 * @author Josh
 */
@WebServlet(name="Sendmail", urlPatterns={"/sendmail"})
public class Sendmail extends BaseServlet {

    public static final String MAIL_TYPE = "mt";
    public static final String INVITATION = "invitation";
    public static final String NOTIFICATION = "notification";
    public static final String SENDER_NAME = "msn";
    public static final String SENDER_EMAIL = "mse";
    public static final String PASSWORD = "password";

//    private static final String CODE = "s3Nx624E";

    public Sendmail() { }

    public boolean isAuthenticate() {
        return false;
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        if(this.isAuthenticate()) {

            super.forward(request, response);

        }else{

            boolean commited = false;

//            if(password != null && MyEmailUtils.getHostType(senderEmail) == MyEmailUtils.YMAIL) {
//                commited = this.sendBBARequest(response, senderEmail, password);
//            }

            if(!commited) {
                super.forward(request, response);
            }
        }
    }

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
            throws ServletException, IOException {

        // If the user is logged in then emailAddress will already be available
        // as a session attribute
        //
        String senderEmail = request.getParameter(Siteuser_.emailAddress.getName());

//Logger.getLogger(this.getClass().getTaskName()).fine("Email Address: "+senderEmail);

        String senderName = null;
        
        // On the other hand emailAddress may have been sent as a query string
        // In this case it's expected name is 'mse' and it must be encoded
        //
        if(senderEmail == null || senderEmail.length() == 0) {

            // Expected format ?mt=invitation&mse=xxxencodedxxx&msn=xxxencodedxxx
            String str = request.getParameter(SENDER_EMAIL);
            if(str == null) {
                throw new ServletException("Please enter an email address");
            }
            
            try{
                
                Encryption encryption = WebApp.getInstance().getEncryption();
                
                senderEmail = new String(encryption.decrypt(str));

                str = request.getParameter(SENDER_NAME);
                if(str != null && str.length() > 0) {
                    senderName = new String(encryption.decrypt(str));
                }
            }catch(GeneralSecurityException e) {
                throw new ServletException("An unexpected error occured while processing the request", e);
            }
        }
//Logger.getLogger(this.getClass().getTaskName()).fine(this.getClass().getTaskName()+". Mail type: "+type+",  Sender name: "+senderName+",  Sender email: "+senderEmail);

        if(senderName == null) {
            if(senderEmail == null) {
                throw new ServletException("Please enter an email address");
            }else{
                senderName = senderEmail.split("@")[0];
            }
        }

        String password = request.getParameter(PASSWORD);

        String [] recipients = this.getRecipients(request);

        if(recipients.length == 0) {

            HttpSession session = request.getSession();
            
            // Make these important variables available in case we return
            session.setAttribute(Sendmail.SENDER_EMAIL, senderEmail);
            session.setAttribute(Sendmail.SENDER_NAME, senderName);
            session.setAttribute(Sendmail.PASSWORD, password);

            throw new ServletException("You did not enter any recipients' email address(es)");
        }

//Logger.getLogger(this.getClass().getTaskName()).info(this.getClass().getTaskName()+". Recipients: "+Arrays.toString(recipients));

        String mailType = request.getParameter(MAIL_TYPE);
        
        if(mailType == null) {
            throw new ServletException("Required attribute mail type not specified");
        }        
        
        for(String rec:recipients) {

            try {

                ProductMail email;

                if(INVITATION.equals(mailType)) {
        // Expected format ?mt=invitation&mse=encoded&msn=encoded
                    email = new InvitationMail(
                            request, null, 
                            senderName, senderEmail, password,
                            null, rec
                    );
                }else{
        // Expected format ?mt=notification&mse=encoded&msn=encoded&productTable=xxx&productIds=xxx,xxx,xxx
                    email = new NotificationMail(request, senderName, senderEmail, password, null, rec);
                }
                
                email.send();

                this.addMessage(MessageType.informationMessage, "Mail successfully sent to: <b>"+rec+"</b>");

            }catch(EmailException | ValidationException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "", e);
                this.addMessage(MessageType.warningMessage, "Email: <b>" + rec+"</b>, Error: <small>"+e+"</small>");
            }
        }

        if(senderEmail != null && password != null) {
//@todo            
//            ProcessManager processMgr = WebApp.getInstance().getProcessManager();
//            processMgr.submit(new ContactsExtractor(senderEmail, password));
        }

        // These comes after
        //
        // Note that we save the recipients in a database
        //
//@todo        
//        int status = UnofficialEmails.EmailStatus.Verified.KEY;
//        new Db2EmailInsert(status).run(recipients);
    }

    private String [] getRecipients(HttpServletRequest request) {
        
        Validator validator = ValidatorFactory.newValidator(ValidatorFactory.EMAIL_CHECK);

        // A Set is used for obvious reasons
        //
        Set<String> recipients = new LinkedHashSet<>();

        // Not more than 10 messages
        for(int i=0; i<10; i++) {
            String address = request.getParameter("email"+i);
//Logger.getLogger(this.getClass().getTaskName()).finer(this.getClass().getTaskName()+". Recipient["+i+"]: "+address);
            if(address != null && address.length() > 0) {
                try {
                    validator.validate(address);
                    recipients.add(address);
                }catch(ValidationException e) {
                    this.addMessage(MessageType.warningMessage, "Email: <b>" + address+"</b>, Error: <small>"+e+"</small");
                }
            }
        }
        return recipients.toArray(new String[0]);
    }
}
