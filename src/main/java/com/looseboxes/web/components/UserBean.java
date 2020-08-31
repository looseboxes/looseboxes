package com.looseboxes.web.components;

import com.bc.jpa.controller.EntityController;
import com.bc.jpa.dao.search.SearchResults;
import com.looseboxes.web.AppProperties;
import com.looseboxes.core.Util;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Chatmessage;
import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.pu.entities.Userpaymentmethod;
import com.looseboxes.pu.entities.Userpaymentmethod_;
import com.looseboxes.web.ChatService;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.servlets.Login;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletException;
import com.bc.jpa.context.JpaContext;
import java.util.Objects;
import com.bc.jpa.dao.Select;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * @(#)User.java   11-Apr-2015 23:29:17
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
public class UserBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(UserBean.class.getName());
    
    private static int guestCount;
    
    public static final String ATTRIBUTE_NAME = "User";
    
    private SearchResults<Product> productSearchResults;
    
    private Siteuser details;
    
    private ProductBean selectedItem;
    
    private ShoppingCart shoppingCart;
    
    private List<Userpaymentmethod> paymentoptions;
    
//    private Cookie [] loginCookies;
    
    public void reset() {
        productSearchResults = null;
        details = null;
        selectedItem = null;
        shoppingCart = null;
        paymentoptions = null;
        this.clearChats();
    }
    
    public static int getGuestCount() {
        return getGuestCount(false);
    }

    public static int getGuestCount(boolean increment) {
        return increment ? ++guestCount : guestCount;
    }
    
    public static String [] getCustomerServiceEmails() {
        return getAdminEmails();
    }

    public static String [] getCustomerServiceUsernames() {
        return getAdminUsernames();
    }
    
    private static String [] _admin_emails;
    public static String [] getAdminEmails() {
        if(_admin_emails == null) {
            _admin_emails = WebApp.getInstance().getConfig().getArray(AppProperties.ADMIN_EMAILS);
            if(_admin_emails != null && _admin_emails.length != 0) {
                List<String> list = new ArrayList<>(_admin_emails.length);
                for(String s:_admin_emails) {
                    if(s == null || s.trim().isEmpty()) {
                        continue;
                    }
                    list.add(s);
                }
                _admin_emails = list.toArray(new String[0]);
            }
        }
        return _admin_emails;
    }
    
    private static String [] _admin_usernames;
    public static String [] getAdminUsernames() {
        if(_admin_usernames == null) {
            String [] arr = UserBean.getAdminEmails();
            if(arr != null && arr.length != 0) {
                List<String> list = new ArrayList<>(arr.length);
                for(String email:arr) {
                    if(email == null || email.isEmpty()) {
                        continue;
                    }
                    String name = UserBean.getName(email);
                    if(name == null || name.trim().isEmpty()) {
                        continue;
                    }
                    list.add(name);
                }
                _admin_usernames = list.toArray(new String[0]);
            }else{
                _admin_usernames = new String[0];
            }
        }
        return _admin_usernames;
    }

    public static boolean isLoggedIn(String username) {
        boolean output = username != null && Login.loggedInUsers.contains(username);
        return output;
    }

    public static boolean isLoggedInToChat(String username) {
        return ChatService.isUserLoggedIn(username);
    }
    
    public static boolean isLoggedIn(String [] usernames) {
        if(usernames == null) {
            return false;
        }
        for(String user:usernames) {
            if(isLoggedIn(user)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isLoggedInToChat(String [] username) {
        if(username == null) return false;
        for(String user:username) {
            if(isLoggedInToChat(user)) {
                return true;
            }
        }
        return false;
    }

    public static final boolean isAdmin(String userid) {
        boolean admin = isAdmin(userid, getAdminUsernames());
        if(!admin) {
            admin = isAdmin(userid, getAdminEmails());
        }
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "User email: {0}, is admin: {1}", new Object[]{userid, admin});
        }
        return admin;
    }
    
    private static boolean isAdmin(String userid, String [] ids) {
        if(userid == null) {
            return false;
        }
        boolean admin = false;
        synchronized(ids) {
            for(String user:ids) {
                if(user.equals(userid)) {
                    admin = true;
                    break;
                }
            }
        }
        return admin;
    }
    
    public void setEmailAddress(String email) throws ServletException {
        if(email == null) {
            throw new NullPointerException();
        }
        if(this.details == null || !this.details.getEmailAddress().equals(email)) {
            JpaContext cf = WebApp.getInstance().getJpaContext();
            EntityController<Siteuser, Integer> ec = cf.getEntityController(Siteuser.class, Integer.class);
            Siteuser found = ec.selectFirst(Siteuser_.emailAddress.getName(), email);
            if(found == null) {
                throw new ServletException("User not found: "+email);
            }
            this.reset();
            this.details = found;
        }
    }

    public boolean isSelectedItemInShoppingCart() {
        
        if(this.selectedItem == null || this.shoppingCart == null) {
            return false;
        }
        LOG.log(Level.FINER, "Selected item: {0}", selectedItem);                

        Productvariant variant = this.selectedItem.getSelectedVariant();
        
        if(variant == null) {
            return false;
        }
        
        List<Orderproduct> results = this.shoppingCart.getItems();
        
        if(results == null || results.isEmpty()) {
            return false;
        }
        
        Integer mVariantId = variant.getProductvariantid();
                
        for(Orderproduct orderproduct:results) {
            
            Integer variantId = orderproduct.getProductvariantid().getProductvariantid();
            
            if(variantId.equals(mVariantId)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean addChat(String toEmail, Chatmessage chat) {
        if(toEmail == null || chat == null) {
            throw new NullPointerException();
        }
        List<Chatmessage> mChats = this.getChats(true).get(toEmail);
        if(mChats == null) {
            mChats = new ArrayList<>(ChatService.getMessageLimit());
        }
        if(!mChats.contains(chat)) {
            return mChats.add(chat);
        }
        return false;
    }

    public void addChats(String toEmail, List<Chatmessage> chatsToAdd) {
        if(chatsToAdd != null && !chatsToAdd.isEmpty()) {
            this.getChats(true).put(toEmail, chatsToAdd);
        }
    }
    
    private Map<String, List<Chatmessage>> c_accessViaGetter;
    public void clearChats() {
        c_accessViaGetter = null;
    }

    public Map<String, List<Chatmessage>> getChats() {
        return this.getChats(false);
    }
    
    /**
     * @param create If <tt>true</tt> attempt an is made to load chats from the 
     * database if not already loaded.
     * @return A Map containing entry pairs for which:
     * key = other chat user email, value = List of chat messages between this 
     * user and other user
     */
    public Map<String, List<Chatmessage>> getChats(boolean create) {
        if(c_accessViaGetter == null && create) {
            c_accessViaGetter = ChatService.getLastChats(this.getEmailAddress());
            if(c_accessViaGetter == null) {
                c_accessViaGetter = new LinkedHashMap<>(ChatService.getChatLimit(), 1.0f);
            }
        }
        return c_accessViaGetter;
    }

    public List<Userpaymentmethod> getPaymentoptions() {
        if(this.paymentoptions == null && this.details != null) {
            JpaContext jpaContext = WebApp.getInstance().getJpaContext();
            try(Select<Userpaymentmethod> qb = jpaContext.getDaoForSelect(Userpaymentmethod.class)) {
                this.paymentoptions = qb.from(Userpaymentmethod.class)
                .where(Userpaymentmethod_.paymentmethoduser.getName(), this.details)
                .createQuery().getResultList();
            }
        }
        return this.paymentoptions;
    }
    
    public void deleteShoppingCart() {
        if(this.shoppingCart == null) {
            return;
        }
        this.shoppingCart.delete();
        this.shoppingCart = null;
    }

    public void clearShoppingCart() {
        if(this.shoppingCart == null) {
            return;
        }
        this.shoppingCart.clear();
    }

    public ShoppingCart getShoppingCart() {
// Once a user logs in the shopping cart should be available            
        if(this.shoppingCart == null) {
            boolean syncWithDatabase = this.isLoggedIn();
            this.shoppingCart = new ShoppingCart(syncWithDatabase, this, null);
            LOG.log(Level.FINE, "Created shopping cart: {0}", this.shoppingCart);
        }
        return this.shoppingCart;
    }

    public void setShoppingCart(ShoppingCart cart) {
        this.shoppingCart = cart;
    }
    
///////////////////////////////////////    
    
    public boolean isLoggedIn() {
        return this.details != null;
    }

    public void login(Map params) throws ServletException { 
    
        LOG.finest(() -> "Params: " + params);
        
        if(params == null || params.isEmpty()) {
            throw new NullPointerException();
        }
        
        Map loginParams = new LinkedHashMap(params);
        loginParams.put(Siteuser_.userstatusid.getName(), References.userstatus.Activated);
        
        if(LOG.isLoggable(Level.FINER)) {
            final Map view = new HashMap(loginParams);
            view.replace("password", "[PASSWORD]");
            LOG.finer(() -> "Login Params: " + view);
        }

        JpaContext jpaContext = WebApp.getInstance().getJpaContext();
        
        try(Select<Siteuser> qb = jpaContext.getDaoForSelect(Siteuser.class)) {
        
            Siteuser user_entity = qb.from(Siteuser.class)
            .where(loginParams)
            .createQuery().getSingleResult();
            
            this.login(user_entity);
            
        }catch(javax.persistence.NoResultException e) {
            
            throw new ServletException("Invalid login credentials", e);
        }
    }
    
    public void login(Siteuser userdetails) { 
        
        this.details = Objects.requireNonNull(userdetails);
        
        // We use this reference because after a shopping cart is created
        // it is automatically set to be the creating user's cart
        //
        ShoppingCart existing = this.shoppingCart;
        
        LOG.log(Level.FINE, "Existing shopping cart has {0} items",  
        (existing == null ? null : existing.getItemCount()));

        if(existing != null) {
            
            if(!existing.isSyncWithDatabase()) {
                
                synchronized(this) {
                    
                    LOG.log(Level.FINER, "Syncing shopping carts");

                    // Load shopping cart from database/create if none exists
                    ShoppingCart syncdcart = new ShoppingCart(true, this, null);

                    LOG.log(Level.FINE, "Created shopping cart has {0} items", 
                            syncdcart.getItemCount());

                    try{

                        LOG.finer(() -> "BEFORE SYNCING: Src cart: "+existing.getItemCount()+
                                " items, Tgt cart: {1} items"+syncdcart.getItemCount());

                        syncdcart.syncOrderWith(existing);

                        LOG.finer(() -> " AFTER SYNCING: Src cart: "+existing.getItemCount()+
                                " items, Tgt cart: {1} items"+syncdcart.getItemCount());

    ////////////////////////////////////////////////////////////////////////////////
                        long count = syncdcart.countProductunits(syncdcart.getOrderId());

                        LOG.fine(() -> "Tgt cart productunits counted from database: " + count);

                        assert syncdcart.getItemCount() == count : "Item count mismatch for order: " + syncdcart.getOrderId() + ", from cart: "+syncdcart.getItemCount()+", from database: "+count;
    ////////////////////////////////////////////////////////////////////////////////                    

                        this.shoppingCart = syncdcart;

                        LOG.log(Level.FINER, "Done syncing shopping carts");

                    }catch(Exception e) {
                        LOG.log(Level.WARNING, "Error syncing shopping cart with database", e);
                    }
                }
            }
        }else{
            
            // Load shopping cart from database/create if none exists
            this.shoppingCart = new ShoppingCart(true, this, null);
        }
    }

    public void logout() { 
        this.details = null;
        this.clearChats();
        this.selectedItem = null;
        this.paymentoptions = null;
//        this.loginCookies = null;
        
        if(this.isLoggedInToChat()) {
            this.logoutOfChat();
        }
    }

////////////////////////    
    public boolean loginToChat() {
        return ChatService.loginUser(this.getName());
    }
    
    public boolean logoutOfChat() {
        return ChatService.logoutUser(this.getName());
    }

    public boolean isLoggedInToChat() {
        return UserBean.isLoggedInToChat(this.getName());
    }
    
    public boolean isCustomerServiceLoggedIn() {
        return UserBean.isLoggedIn(UserBean.getCustomerServiceUsernames());
    }
    
    public boolean isCustomerServiceLoggedInToChat() {
        return UserBean.isLoggedInToChat(UserBean.getCustomerServiceUsernames());
    }
    
    public String getEmailAddress() {
        String email = this.details == null ? null : this.details.getEmailAddress();
        if(this.isLoggedIn()) {
            assert email != null: "Expected a value but found 'null' for email address of logged in user";
        }
        return email;
    }
    
    /**
     * @deprecated Rather use {@link #getImage()}
     * @return A url string pointing to the image of the user
     */
    @Deprecated
    public String getImage1() {
        return this.getImage();
    }
    
    public String getLogo() {
        return this.details == null ? null : this.details.getLogo();
    }
    
    public String getImage() {
        String image;
        if(details != null) {
            image = details.getImage1();
            if(image == null) {
                image = details.getImage2();
                if(image == null) {
                    image = details.getImage3();
                }
            }
        }else{
            image = null;
        }
        return image;
    }
    
    public com.looseboxes.pu.entities.Currency getCurrencyEntity() {
        com.looseboxes.pu.entities.Currency curr = null;
        if(this.details != null) {
            curr = this.details.getCurrencyid();
        }
        if(curr != null) {
            return curr;
        }else{
            return ServletUtil.getCurrencyEntityForLocale(this.getLocale());
        }
    }
    
    private Locale l_accessViaGetter;
    public Locale getLocale() {
        if(this.l_accessViaGetter == null && details != null) {
            com.looseboxes.pu.entities.Currency curr = details.getCurrencyid();
            if(curr != null) {
                l_accessViaGetter = Util.getLocaleForCurrencyCode(curr.getCurrency(), null);
            }
            if(l_accessViaGetter == null){
                l_accessViaGetter = Util.getLocale(details.getAddressid(), Locale.getDefault());
            }
        }
        return this.l_accessViaGetter != null ? this.l_accessViaGetter : Locale.getDefault();
    }
    
    public boolean isCustomerServiceRep() {
        return this.isAdmin();
    }
    
    public boolean isAdmin() {
        if(this.details == null) {
            return false;
        }else{
            return UserBean.isAdmin(this.getName());
        }
    }
    
    /**
     * Returns the username of the UserBean. Or null if the user is not logged in.
     * When the user is logged in and <tt>username</tt> is <tt>null</tt>, this
     * method returns<br/> <code>emailAddress.split("@")[0]</code>.<br/>
     * For example, if <tt>username</tt> is <tt>null</tt>, for an email address
     * of: <tt>loose@provider.com</tt> this method will return <tt>loose</tt>.
     * @return The username
     * @see #getUsername(com.looseboxes.pu.entities.Siteuser) 
     */
    public String getName() {
        return this.details == null ? null : this.getUsername(this.details);
    }
    
    public String getUsername(Siteuser user) {
        Objects.requireNonNull(user);
        final String output;
        final String username = user.getUsername();
        if(username != null) {
            output = username;
        }else{
            final String emailAddress = user.getEmailAddress();
            if(emailAddress == null) {
                output = null;
            }else{
                final int end = emailAddress.indexOf("@");
                if(end != -1 || end > 0) {
                    output = emailAddress.substring(0, end);
                }else{
                    output = emailAddress;
                }
            }
        }
        return output;
    }

    public Currency getCurrency() {
        return Currency.getInstance(this.getLocale());
    }

    public Siteuser getDetails() {
        return details;
    }

    public Siteuser getRecord() {
        return this.getDetails();
    }
    
    public static String getName(String email) {
        EntityController<Siteuser, ?> ec = 
                WebApp.getInstance().getJpaContext().getEntityController(Siteuser.class);
        Siteuser siteuser = ec.selectFirst(Siteuser_.emailAddress.getName(), email);
        return siteuser == null ? null : siteuser.getUsername();
    }
    
    public static String getEmail(String name) {
        EntityController<Siteuser, ?> ec = 
                WebApp.getInstance().getJpaContext().getEntityController(Siteuser.class);
        Siteuser siteuser = ec.selectFirst(Siteuser_.username.getName(), name);
        return siteuser == null ? null : siteuser.getEmailAddress();
    }

    public ProductBean getSelectedItem() {
        return selectedItem;
    }
    
    public void setSelectedItem(Productvariant productunit) {
        if(productunit == null) {
            this.selectedItem = null;
        }else{
            this.selectedItem = new ProductBean(this, productunit);
        }
    }

    public void setSelectedItem(ProductBean selectedItem) {
        this.selectedItem = selectedItem;
    }

//    public Cookie[] getLoginCookies() {
//        return loginCookies;
//    }

//    public void setLoginCookies(Cookie[] loginCookies) {
//        this.loginCookies = loginCookies;
//    }

    public SearchResults<Product> getProductSearchResults() {
        return productSearchResults;
    }

    /**
     * Closes the previous search results and assigns the input parameter as the
     * current search results for this user.
     * @param productSearchResults The search results to assign as the current
     */
    public void setCurrentProductSearchResultsAndClosePrevious(SearchResults<Product> productSearchResults) {
        if(this.productSearchResults != null && 
                !this.productSearchResults.equals(productSearchResults)) {
            if(this.productSearchResults instanceof AutoCloseable) {
                try{
                    ((AutoCloseable)this.productSearchResults).close();
                }catch(Exception e) { 
                    LOG.log(Level.WARNING, "Exception closing "+
                            this.productSearchResults.getClass().getName()+". {0}", e.toString());
                }
            }
        }
        this.productSearchResults = productSearchResults;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append(", id: ").append(this.getDetails() == null ? null : this.getDetails().getSiteuserid());
        builder.append(", name: ").append(this.getEmailAddress());
        builder.append(", email: ").append(this.getName());
        builder.append(", locale: ").append(this.getLocale());
        builder.append(", currency: ").append(this.getCurrency());
        return builder.toString();
    }
}
