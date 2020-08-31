package aaa.util;

import com.bc.mail.EmailBuilder;
import com.bc.mail.EmailBuilderImpl;
import com.looseboxes.TestWebApp;
import com.looseboxes.core.LbApp;
import com.looseboxes.web.html.ProductHtmlGen;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author Chinomso Bassey Ikwuagwu on Dec 16, 2016 10:15:29 PM
 */
public class ASendMail {

    public static void main(String [] args) {
        
        try{
            
            if(!TestWebApp.isInitialized()) {
                TestWebApp.init();
            }

            EmailBuilder emailBuilder = new EmailBuilderImpl(LbApp.getInstance().getMailConfig());
            
            HtmlEmail email = new HtmlEmail();
            
            String senderEmail = "posh.bc@gmail.com";
            String senderPassword = "uuid-391120";
            String recipientEmail = "chinomsoikwuagwu@yahoo.com";
            String [] bcc;
            String subject;
            String htmlMsg;
            String [] productIds;
            
            emailBuilder.build(email, "senderEmail", "senderPassword", true);
            
            email.addTo(recipientEmail);

//            email.addBcc(bcc);
            
//            email.setSubject(subject);
            
//            email.setHtmlMsg(htmlMsg);
            
            ProductHtmlGen htmlGen = new ProductHtmlGen();
            
//            htmlGen.updateEmail(productIds, new StringBuilder(), email);
            
            email.send();
                    
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
