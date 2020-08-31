package com.looseboxes.web.chat;

import com.looseboxes.cometd.chat.ChatMessageDataNames;
import com.looseboxes.cometd.chat.functions.GetDataFromMessage;
import com.looseboxes.pu.entities.Chatmessage;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiFunction;
import org.cometd.bayeux.server.ServerMessage;

/**
 * @author Chinomso Bassey Ikwuagwu on May 19, 2018 8:09:08 PM
 */
public class ChatMessageFormatter implements Serializable, ChatMessageDataNames, 
        BiFunction<ServerMessage.Mutable, Chatmessage, Chatmessage> {

    private final BiFunction<ServerMessage.Mutable, Map, Map> getDataFromMessage;

    public ChatMessageFormatter() {
        this.getDataFromMessage = new GetDataFromMessage();
    }
    
    @Override
    public Chatmessage apply(ServerMessage.Mutable message, Chatmessage outputIfNone) {
        final Map dataMap = this.getDataFromMessage.apply(message, Collections.EMPTY_MAP);
        final String user = (String)dataMap.get(USER);
        final String peer = (String)dataMap.get(PEER);
        final String chatText = (String)dataMap.get(CHAT);
        if(this.accept(user) && this.accept(peer) && this.accept(chatText)) {
            final Chatmessage chatmessage = new Chatmessage();
            chatmessage.setChatText(chatText);
            chatmessage.setFromEmail(user);
            chatmessage.setToEmail(peer);
            return chatmessage;
        }else{
            return outputIfNone;
        }
    }
    
    private boolean accept(String s) {
        return s != null && !s.isEmpty();
    }
}
