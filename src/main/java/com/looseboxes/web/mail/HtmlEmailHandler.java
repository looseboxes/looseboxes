package com.looseboxes.web.mail;

import com.bc.util.Log;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * @author USER
 */
public class HtmlEmailHandler {
    
    public void addAttachments(HtmlEmail htmlEmail, Collection<String> attachments, boolean externalOutput) 
            throws EmailException {
        if(attachments != null) {
            int i = 0;
            for(String attachment:attachments) {
                if(externalOutput) {
                    htmlEmail.embed(attachment, "image" + (i++));
                }else{
                    htmlEmail.embed(new File(attachment));
                }
            }
        }
    }
    
    public String getSrc(String imageLink, boolean useCID) {
        
Log.getInstance().log(Level.FINER, "Link: {0}, use CID: {1}", this.getClass(), imageLink, useCID);
        String src;
        if(imageLink == null) {
            src = null;
        }else{
            if(!useCID) {
                src = imageLink;
            }else{
                src = "cid:"+ getFilename(imageLink);
            }
        }
Log.getInstance().log(Level.FINER, "Link: {0}, use CID: {1}, src: {2}", this.getClass(), imageLink, useCID, src);
        return src;
    }

    private boolean isUrl(String link) {
        try{
            new URL(link);
            return true;
        }catch(MalformedURLException e) {
            return false;
        }
    }
    
    private String getFilename(String imageLink) {
        String fname;
        if(isUrl(imageLink)) {
            fname = UUID.nameUUIDFromBytes(imageLink.getBytes()).toString();
        }else{
            fname = Paths.get(imageLink).getFileName().toString();
        }
        return fname;
    }
}
