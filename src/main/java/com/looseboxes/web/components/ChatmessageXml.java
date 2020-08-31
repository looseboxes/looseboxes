package com.looseboxes.web.components;

import com.bc.html.HtmlGen;
import com.looseboxes.pu.entities.Chatmessage;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @(#)ChatmessageXml.java   26-Jun-2015 00:44:19
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
public class ChatmessageXml extends HtmlGen implements Serializable {

    private boolean ownMessage;
    
    private String messageTagName;
    
    private String messageTagStyle;
    
    private String messageTagClass;
    
    private final SimpleDateFormat dateFormat;
    
    private Chatmessage chatmessage;
    
    public ChatmessageXml() {
        this.setUseNewLine(false);
        this.messageTagName = "p";
        this.dateFormat = new SimpleDateFormat();
        ChatmessageXml.this.setDateTimePattern("HH:mm:ss");
    }

    public String getDateTimePattern() {
        return dateFormat.toPattern();
    }

    public void setDateTimePattern(String dateTimePattern) {
        this.dateFormat.applyLocalizedPattern(dateTimePattern);
    }

    public String getMessageTagName() {
        return messageTagName;
    }

    public void setMessageTagName(String messageTagName) {
        this.messageTagName = messageTagName;
    }

    public String getMessageTagStyle() {
        return messageTagStyle;
    }

    public void setMessageTagStyle(String messageTagStyle) {
        this.messageTagStyle = messageTagStyle;
        this.loadAttributes = true;
    }

    public String getMessageTagClass() {
        return messageTagClass;
    }

    public void setMessageTagClass(String messageTagClass) {
        this.messageTagClass = messageTagClass;
        this.loadAttributes = true;
    }

    public boolean isOwnMessage() {
        return ownMessage;
    }

    public void setOwnMessage(boolean ownMessage) {
        this.ownMessage = ownMessage;
    }

    public Chatmessage getChatmessage() {
        return chatmessage;
    }

    public void setChatmessage(Chatmessage chatmessage) {
        this.chatmessage = chatmessage;
    }
    
    public StringBuilder getXml() {
        int initialCapacity = this.chatmessage == null ? 0 : this.chatmessage.getChatText().length() * 2;
        StringBuilder xml = new StringBuilder(initialCapacity);
        this.appendXml(xml);
        return xml;
    }
    
    public void appendXml(StringBuilder builder) {
        if(chatmessage != null) {
            
            if(this.messageTagName == null || this.messageTagName.isEmpty()) {
                this.messageTagName = "p";
            }
            
            // BEGIN message view
            this.straightTag(this.messageTagName, this.getTagAttributes(), builder);
            
            // BEGIN Username/email and time
            builder.append("<small>");
            if(!this.ownMessage) {
                String fromEmail = this.chatmessage.getFromEmail();
                builder.append(fromEmail);
                builder.append("&emsp;");
            }
            Date date = this.chatmessage.getDatecreated();
            String dateStr = this.dateFormat.format(date);
            
            // END Username/email and time
            builder.append(dateStr).append("</small><br/>--------------------<br/>");
            
            builder.append(this.chatmessage.getChatText());
            
            // END message view
            builder.append('<').append('/').append(this.messageTagName).append('>');
        }
    }
    
    private boolean loadAttributes = true;
    private Map attr_accessViaGetter;
    public Map getTagAttributes() {
        if(loadAttributes) {
            loadAttributes = false;
            if(this.messageTagClass != null || this.messageTagStyle != null) {
                attr_accessViaGetter = new HashMap(2, 1.0f);
                if(this.messageTagClass != null) {
                    attr_accessViaGetter.put("class", this.messageTagClass);
                }
                if(this.messageTagStyle != null) {
                    attr_accessViaGetter.put("style", this.messageTagStyle);
                }
            }else{
                attr_accessViaGetter = Collections.singletonMap("style", this.getDefaultStyle());
            }
        }
        return attr_accessViaGetter;
    }
    
    public String getDefaultStyle() {
        String output;
        if(this.ownMessage) {
            output = "border:2px solid #CCCCCC; float:left; background-color:#EEEEEE;";
        }else{
            output = "border:2px solid #EEEEEE; float:right; background-color:#FFFFFF";
        }
        return output;
    }
}
