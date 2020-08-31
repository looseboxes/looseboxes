package com.looseboxes.web.mail;

import com.bc.jpa.fk.EnumReferences;
import com.bc.util.Util;
import com.bc.util.Log;
import com.bc.html.HtmlGen;
import com.bc.html.HtmlPageGen;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Productcategory;
import com.looseboxes.web.WebApp;
import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import com.bc.jpa.context.JpaContext;


/**
 * @(#)AbstractMessageFormatter.java   30-May-2015 11:02:35
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
public abstract class AbstractMessageFormatter implements Serializable {

    private boolean useCID;
    private boolean localContext;
    
    private String category;
    private String sender;
    private String recipientName;
    private String recipient;
    
    protected AbstractMessageFormatter() {  }

    public abstract String getLogoFilename();
    
    protected abstract String getReturnURL();
    
    protected abstract String getUnsubscribeLink();

    protected abstract String getInvitationLink();
    
    public HtmlEmail format(
            HtmlEmail email, String subject, String message, Set<String> attachments) 
            throws EmailException {

        Set<String> defaultAttachments = this.initDefaultAttachments();

        StringBuilder html = new StringBuilder();

        final String tableStyle="border: 1px ridge white";

        html.append("<table style=\""+tableStyle+"\"><tr>");

        // The height of each image is set at 70px because this is the
        // default advert height. i.e adverts are 440x70

        String logoSrc = this.getLogoSrc();
        
        if(logoSrc != null) {
            // Show our logo where available
            html.append("<td><img height=\"70\" src=\"");
            html.append(logoSrc);
            html.append("\"/></td>");
        }

        String bannerSrc = this.getBannerSrc();
        
        if(bannerSrc != null) {
            // and one randomly selected advert banner
            html.append("<td><img height=\"70\"src=\"");
            html.append(bannerSrc);
            html.append("\"/></td>");
        }

        html.append("</tr></table>");

////////////////// open the div //////////////////
// logo = 192px, banner = 440px, border = 1px * 2, total = 634px
//
        html.append("<div>");
        
        String name = null;
        
        if(this.getRecipientName() != null) {
            name = this.getRecipient();
        }else if(this.getRecipient() != null){
            name = this.getRecipient().substring(0, getRecipient().lastIndexOf('@'));
        }

        if(name != null) {
            html.append("<br/><br/><i>Dear ");
            html.append(name);
        }else{
            html.append("<br/><br/><i>Hi");
        }
        
        final String appName = WebApp.getInstance().getName();
        
        html.append("</i>,<br/><br/>");
        html.append(message);
        html.append("<br/><br/><i>Thanks<br/>The ");
        html.append(appName);
        html.append(" team</i>");
        
        html.append("<br/><br/>");
        String linkText = "return to <i>"+appName+"</i>";
        HtmlGen.AHREF(this.getReturnURL(), linkText, html);
        final String invite = this.getInvitationLink();
        if(invite != null) {
            html.append("&emsp;or&emsp;");
            html.append(invite);
        }

        html.append("<br/><br/><HR/><small>");

        if(this.getSender() != null) {
            html.append("In order to ensure proper delivery of messages from ");
            html.append("<i>");
            html.append(appName);
            html.append("</i>");
            html.append(" please add ");
            html.append(this.getSender());
            html.append(" to your list of trusted senders in your email client.");
        }

        html.append("<br/><br/><small>DO NOT reply this message!");
        if(name != null) {
            html.append("<br/><br/>This message was intended for ");
            html.append(name);
        }
        html.append("<br/>If you are not ");
        if(name != null) {
            html.append(name);
        }else{
            html.append("the intended recipient");
        }
        html.append(" please delete this message immediately.");
        
//@related_50 Unsubscribe link
        this.appendUnsubscribeLink(html);
        
        html.append("</small>");

/////////////////// close the div ////////////////////
//
        html.append("</div>");

Log.getInstance().log(Level.FINER, "Message HTML:\n{0}", this.getClass(), html);

        String contents = new HtmlPageGen().getPage(
                "Mail From: "+appName, html.toString()).toString();

        email.setSubject(subject);
        email.setHtmlMsg(contents);

        this.updateAttachments(email, defaultAttachments);

//Logger.getLogger(this.getClass().getName()).info(this.getClass().getName()+". Attachments: "+email.getAttachments().size());
        return email;
    }
    
    public void updateAttachments(HtmlEmail htmlEmail, Set<String> attachments) 
            throws EmailException {
        if(attachments != null && this.isUseCID()) {
            int i = 0;
            for(String attachment:attachments) {
                if(!this.isLocalContext()) {
                    htmlEmail.embed(attachment, "image" + (i++));
                }else{
                    htmlEmail.embed(new File(attachment));
                }
            }
        }
    }

    public String getBannerFilename() {
        return this.getProductcategory() == null ? null : this.getProductcategory().toLowerCase() + ".jpg";
    }
    
    public String getLogoSrc() {
        return this.getImageSrc(this.getLogoFilename());
    }
    
    public String getBannerSrc() {
        return this.getImageSrc(this.getBannerFilename());
    }
    
    private String getImageSrc(String fname) {
        if(fname == null) {
            return null;
        }else{
            String imagePath = this.getImagePath(fname);
            String src = new HtmlEmailHandler().getSrc(imagePath, useCID);
Log.getInstance().log(Level.FINER, "Filename: {0}, use CID: {1}, src: {2}", this.getClass(), fname, useCID, src);
            return src;
        }
    }
    private String getImagePath(String fname) {
// This didn't work. Output url had '\' as shown below
// http://www.buzzwears.com\images\logo.jpg       
// The images were not displayed in the email client
//        
//        String relativePath = Paths.get("/images", fname).toString();
        String relativePath = "/images/" + fname;
                
        String imagePath = getPath(relativePath);
Log.getInstance().log(Level.FINER, "Filename: {0}, Path: {1}", this.getClass(), fname, imagePath);
        return imagePath;
    }
    private String getPath(String relativePath) {
        if(this.localContext) {
            return WebApp.getInstance().getPath(relativePath);
        }else{
            try{
                return WebApp.getInstance().getURL(relativePath).toExternalForm();
            }catch(MalformedURLException e) {
                Log.getInstance().log(Level.WARNING, "Error constructing URL for: "+relativePath, this.getClass(), e);
                return  null;
            }
        }
    }
    
    private void appendUnsubscribeLink(StringBuilder appendTo) {
        if(this.getUnsubscribeLink() == null) return;
        appendTo.append("<br/><br/>you may also wish to unsubscribe ");
        HtmlGen.AHREF(this.getUnsubscribeLink(), "here", appendTo);
    }
    
    public Set<String> initDefaultAttachments() {

        Set<String> defaultAttachments = new HashSet<>();
        
        if(!useCID) {
            
            return defaultAttachments;
        }

        String logoPath = this.getImagePath(this.getLogoFilename());
        if(logoPath != null) {
            defaultAttachments.add(logoPath);
        }

        // We need a table name to be able to display an
        // associated banner
        //
        if(this.getProductcategory() == null) {
            this.setProductcategory(this.getRandomProductcategory().getProductcategory());
        }
        
        String bannerPath = this.getImagePath(this.getBannerFilename());
        
        if(bannerPath != null) {
            
            defaultAttachments.add(bannerPath);
        }
        
        return defaultAttachments;
    }
    
    private Productcategory getRandomProductcategory() {
        JpaContext cf = WebApp.getInstance().getJpaContext();
        EnumReferences refs = cf.getEnumReferences();
        List entities = refs.getEntities(References.productcategory.Fashion);
        int randomIndex = Util.randomInt(entities.size());
        return (Productcategory)entities.get(randomIndex);
    }

    public String getProductcategory() {
        return category;
    }

    public void setProductcategory(String cat) {
        this.category = cat;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String emailAddress) {
        this.sender = emailAddress;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String emailAddress) {
        this.recipient = emailAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public boolean isUseCID() {
        return useCID;
    }

    public void setUseCID(boolean useCID) {
        this.useCID = useCID;
    }

    public boolean isLocalContext() {
        return localContext;
    }

    public void setLocalContext(boolean localContext) {
        this.localContext = localContext;
    }
}
