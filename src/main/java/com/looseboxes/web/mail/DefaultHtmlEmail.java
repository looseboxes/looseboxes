package com.looseboxes.web.mail;

import com.bc.mail.EmailBuilder;
import com.bc.mail.EmailBuilderImpl;
import com.bc.util.Log;
import com.looseboxes.core.LbApp;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.WebApp;
import java.util.logging.Level;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author poshjosh
 */
public class DefaultHtmlEmail extends HtmlEmail {

    public DefaultHtmlEmail() 
            throws EmailException {
        this(
                WebApp.getInstance().getConfig().getString(AppProperties.address_default_noreply),
                WebApp.getInstance().getConfig().getString(AppProperties.address_default_noreply+".password"),
                true,
                true
        );
    }
    
    public DefaultHtmlEmail(
            String user, String pass, boolean ssl, boolean outgoing) 
            throws EmailException {
        
        this(new EmailBuilderImpl(LbApp.getInstance().getMailConfig()),
                user, pass, ssl, outgoing);
    }

    public DefaultHtmlEmail(
            EmailBuilder emailBuilder, 
            String user, String pass, boolean ssl, boolean outgoing) 
            throws EmailException {
        
        emailBuilder.build(DefaultHtmlEmail.this, user, pass, ssl, outgoing);
    }
    
    public boolean isProductionMode() {
        return WebApp.getInstance().isProductionMode();
    }

    @Override
    public String send() throws EmailException {
        if(!isProductionMode()) {
            Log.getInstance().log(Level.INFO, "Production mode: false. Email message will not be sent", this.getClass());
            return null;
        }else{
            return super.send();
        }
    }
}
