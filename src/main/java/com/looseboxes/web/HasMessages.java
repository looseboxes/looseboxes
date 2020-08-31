package com.looseboxes.web;

import java.util.Map;


/**
 * @(#)HasMessages.java   29-Apr-2015 18:51:16
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
public interface HasMessages {
    
    public static enum MessageType{informationMessage, warningMessage}

    void addMessage(MessageType messageType, Object message);
    
    void addMessages(Map<MessageType, ?> messages);
    
    Map<MessageType, StringBuilder> getMessages();
    
    boolean hasMessages();
}
