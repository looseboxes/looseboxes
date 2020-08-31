package com.looseboxes.web;

import com.bc.jpa.controller.EntityController;
import com.bc.jpa.exceptions.PreexistingEntityException;
import com.bc.util.Log;
import com.looseboxes.pu.entities.Chatmessage;
import com.looseboxes.pu.entities.Chatmessage_;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.components.ChatmessageXml;
import com.looseboxes.web.components.UserBean;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Select;

/**
 * @(#)ChatService.java   17-Jun-2013 15:31:26
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
public class ChatService {
    
    private static List<String> chatUsers;

    private ChatService() {  }
    
    public static boolean loginUser(HttpServletRequest request) {
        return loginUser(getUser(request).getName());
    }
    
    public static boolean loginUser(String username) {
        if(username == null) {
            throw new NullPointerException("Username == null");
        }
        return getChatUsers().add(username);
    }
    
    public static boolean logoutUser(HttpServletRequest request) {
        return logoutUser(getUser(request).getName());
    }
    
    public static boolean logoutUser(String username) {
        return getChatUsers().remove(username);
    }
    
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        return isUserLoggedIn(getUser(request).getName());
    }

    public static boolean isUserLoggedIn(String username) {
        return getChatUsers().contains(username);
    }
    
    private static List<String> getChatUsers() {
        if(chatUsers == null) { 
            chatUsers = new ArrayList<>(); 
        }
        return chatUsers;
    }
    
    /**
     * @param request
     * @param response
     * @return The last chat messages for the requested chat formatted for xml/html viewing
     * @throws javax.servlet.ServletException
     * @throws java.sql.SQLException
     */
    public static StringBuilder forAjax(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, SQLException {
        
        String toEmail = forRequest(request, response);
        
        UserBean user = getUser(request);
        
        Map<String, List<Chatmessage>> chats = user.getChats(false);
        
        if(toEmail != null && chats != null) {

            List<Chatmessage> chat = chats.get(toEmail);
            
            ChatmessageXml xml = new ChatmessageXml();
            
            StringBuilder builder = new StringBuilder();
            
            builder.append("<table>");
            
            StringBuilder log = null;

            for(int index=0; index<chat.size(); index++) {  
                
                if(log == null) {
                    log = new StringBuilder();
                }
                
                Chatmessage chatMessage = chat.get(index);

                boolean own = chatMessage.getFromEmail().equals(user.getEmailAddress());

log.append('\n');
log.append(MessageFormat.format("{0}. Own msg: {1}. ID:{2}, Text: {3}", index, own, chatMessage.getChatmessageid(), chatMessage.getChatText()));

                xml.setChatmessage(chatMessage);
                
                xml.setOwnMessage(own);
                
                builder.append("<tr><td>");
                xml.appendXml(builder);
                builder.append("</td></tr>");
            }
if(log != null) {
    Log.getInstance().log(Level.INFO, "{0}", ChatService.class, log);
}            
            
            builder.append("</table>");

            return builder;
        }
        
        return null;
    }
    
    /**
     * Adds the chat message contained in the request to the database (if available)
     * then updates the messages available for viewing.
     * @param request
     * @param response
     * @return The the email address of the recipient of the chat for which the request was made
     * @throws javax.servlet.ServletException
     */
    public static String forRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
        
        Map<String, String> parameters = ServletUtil.getParameterMap(request);
        
Log.getInstance().log(Level.FINE, "Parameters: {0}", ChatService.class, parameters);        

        String toEmail = getToEmail(parameters);
        
        UserBean user = getUser(request);

        boolean ascending = isAscending(parameters);
            
        String action = parameters.get("action");
        
        if(action != null) switch(action) {
            
            case "login":
                loginUser(user.getName());
                break;
                
            case "logout":
                logoutUser(user.getName());
                break;
                
            case "load":
            case "refresh":
                if(toEmail == null) {
                    throw new ServletException("Please enter email address or username of the user you want to chat with");
                }
                List<Chatmessage> latestMessages = getLastMessagesFromDatabase(
                user.getEmailAddress(), toEmail, getMessageLimit(), ascending);
                
                // Update local cache
                user.addChats(toEmail, latestMessages);
                break;
                
            case "add":    
                if(toEmail == null) {
                    throw new ServletException("Please enter email address or username of the user you want to chat with");
                }
                String chatText = parameters.get(Chatmessage_.chatText.getName());
Log.getInstance().log(Level.FINE, "Chat text: {0}", ChatService.class, chatText);        
                // Chat text may be null
                if(chatText != null) {
                    Chatmessage chatMessage = newChatMessage(user.getEmailAddress(), toEmail, chatText);
                    try{
                        addChatmessageToDatabase(chatMessage);
                        
                        // Update local cache
                        user.addChat(toEmail, chatMessage);
                        
                    }catch(SQLException e) {
                        throw new ServletException("Failed to add chat message to database", e);
                    }
                }
                break;
            default:    
                break;
        }
        
        return toEmail;
    }
    
    public static String getToEmail(Map<String, String> parameters) {
        
        String toEmail = parameters.get(Chatmessage_.toEmail.getName());
        
        if(toEmail == null) {
            String username = parameters.get("toUsername");
            if(username != null) {
                JpaContext jpaContext = WebApp.getInstance().getJpaContext();
                try(Select<String> qb = jpaContext.getDaoForSelect(Siteuser.class, String.class)) {
                    toEmail = qb.from(Siteuser.class)
                    .select(Siteuser_.emailAddress.getName())
                    .where(Siteuser_.username.getName(), Select.EQ, username)
                    .createQuery().getSingleResult();
                }
            }
        }

        if(toEmail == null) {
// @related_toUser 'toUser' field indicates either 'toEmail' or 'toUsername' 
            String toUser = parameters.get("toUser");
            if(toUser != null) {
                JpaContext jpaContext = WebApp.getInstance().getJpaContext();
                try(Select<String> qb = jpaContext.getDaoForSelect(Siteuser.class, String.class)) {
                    toEmail = qb.from(Siteuser.class)
                    .select(Siteuser_.emailAddress.getName())
                    .where(Siteuser_.emailAddress.getName(), Select.EQ, toUser)
                    .or()
                    .where(Siteuser_.username.getName(), toUser)
                    .createQuery().getSingleResult();
                }
            }
        }

Log.getInstance().log(Level.FINE, "To email: {0}", ChatService.class, toEmail);        
        
        return toEmail;
    }
    
    private static boolean isAscending(Map<String, String> parameters) {
        String asc = parameters.get("asc");
        if(asc == null) return isAscending();
        boolean output = "1".equals(asc);
Log.getInstance().log(Level.FINE, "@isAscending(java.util.Map): {0}", 
        ChatService.class, output);        
        return output;
    }
    
    public static Chatmessage newChatMessage(String fromEmail, String toEmail, String chatText) {
        Chatmessage chatmessage = new Chatmessage();
        chatmessage.setChatText(chatText);
        chatmessage.setDatecreated(new Date());
        chatmessage.setFromEmail(fromEmail);
        chatmessage.setToEmail(toEmail);
        return chatmessage;
    }
    
    public static void addChatmessageToDatabase(Chatmessage chatMessage) throws SQLException {
        
        EntityController<Chatmessage, Integer> ec = 
                WebApp.getInstance().getJpaContext().getEntityController(
                        Chatmessage.class, Integer.class);
        try{
            ec.persist(chatMessage);
        }catch(PreexistingEntityException e) {
            throw new SQLException(e);
        }catch(Exception e) {
            throw new SQLException(e);
        }
    }
    
    public static Map<String, List<Chatmessage>> getLastChats(String email) {
        return getLastChats(email, getChatLimit(), getMessageLimit(), isAscending());
    }

    public static Map<String, List<Chatmessage>> getLastChats(
            String email, int chatLimit, int msgLimit, boolean ascending) {

// SELECT DISTINCT `fromEmail`, `toEmail` FROM [databasename].`chatmessage` WHERE `fromEmail` = [email] OR `toEmail` = [email] limit 0, 1;
        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        
        List<String[]> fromAndToEmailList;
        try(Select<String[]> qb = jpaContext.getDaoForSelect(Chatmessage.class, String[].class)) {
            fromAndToEmailList = qb.from(Chatmessage.class)
            .distinct(true)
            .select(Chatmessage_.fromEmail.getName(), Chatmessage_.toEmail.getName())
            .where(Chatmessage_.fromEmail.getName(), Select.EQ, email)
            .or().where(Chatmessage_.toEmail.getName(), Select.EQ, email)
            .createQuery().setFirstResult(0).setMaxResults(chatLimit).getResultList();
        }
        
        LinkedHashMap<String, List<Chatmessage>> output;
        
        if(fromAndToEmailList != null && !fromAndToEmailList.isEmpty()) {
        
            output = new LinkedHashMap<>(fromAndToEmailList.size(), 1.0f);

            for(String [] emails:fromAndToEmailList) {
                
                String fromEmail = emails[0];
                String toEmail = emails[1];
                
                List<Chatmessage> messages = getLastMessagesFromDatabase(fromEmail, toEmail, msgLimit, ascending);
                
                boolean fromMe = (email.equals(fromEmail));
                
                output.put(fromMe ? toEmail : fromEmail, messages);
            }
        }else{
            
            output = null;
        }
        
        return output;
    }
     
    public static List<Chatmessage> getLastMessagesFromDatabase(String fromEmail, String toEmail) {
        return getLastMessagesFromDatabase(fromEmail, toEmail, getMessageLimit(), isAscending());
    }
    
    public static List<Chatmessage> getLastMessagesFromDatabase(String fromEmail, String toEmail, int limit, boolean ascending) {
        
        List<Chatmessage> messages;
        
        // WHERE fromEmail = [fromEmail] AND toEmail = [toEmail] OR fromEmail = [toEmail] AND toEmail = [fromEmail]
        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        
        try(Select<Chatmessage> qb = jpaContext.getDaoForSelect(Chatmessage.class)) {
            
            TypedQuery<Chatmessage> tq = qb.from(Chatmessage.class)
            .where(Chatmessage_.fromEmail.getName(), Select.EQ, fromEmail)
            .and().where(Chatmessage_.toEmail.getName(), Select.EQ, toEmail)        
            .or().where(Chatmessage_.fromEmail.getName(), Select.EQ, toEmail)        
            .and().where(Chatmessage_.toEmail.getName(), Select.EQ, fromEmail)
            .ascOrder(Chatmessage_.chatmessageid.getName())
            .createQuery();

            if(limit > 0) {
                tq.setFirstResult(0);
                tq.setMaxResults(limit);
            }
        
            messages = tq.getResultList();
        }
        
Log.getInstance().log(Level.FINE, "Messages: {0}", ChatService.class, messages.size());        
        return messages;
    }
    
    private static UserBean getUser(HttpServletRequest request) {
        return (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
    }
    
    public static int getChatLimit() {
        return 3;
    }
    
    public static int getMessageLimit() {
        return 7;
    }
    
    public static boolean isAscending() {
        return false;
    }
}
