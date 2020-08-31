package com.looseboxes.web.servlets;

import com.bc.jpa.controller.EntityController;
import com.bc.oauth.OAuthProperties;
import com.bc.oauth.OAuthProviderNames;
import com.bc.oauth.OAuthSessionIx;
import com.bc.oauth.OAuthSessionProperties;
import static com.bc.oauth.OAuthSessionProperties.AUTH_URL;
import static com.bc.oauth.OAuthSessionProperties.PROVIDER;
import com.bc.oauth.OAuthSessions;
import com.bc.oauth.OAuthStages;
import com.bc.util.Log;
import com.bc.validators.ValidationException;
import com.looseboxes.pu.entities.Oauthuser;
import com.looseboxes.pu.entities.Oauthuser_;
import com.looseboxes.pu.entities.Siteuser_;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.UserBean;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import com.bc.jpa.context.JpaContext;
import com.bc.config.ConfigService;
import com.bc.security.SecurityTool;
import java.io.StringReader;
import java.util.function.Predicate;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import com.bc.config.Config;
import com.bc.config.ConfigData;
import java.util.Set;

/**
 * @(#)Oauth.java   23-Jun-2015 22:23:07
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
public class OAuth extends BaseServlet implements OAuthSessionProperties {
    
    private String forwardPage;

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.PRODUCTS_SEARCHRESULTS;
    }

    @Override
    public void destroy(HttpServletRequest request) throws ServletException {
        try{
            super.destroy(request);
        }finally{
            forwardPage = null;
        }
    }

    @Override
    public void forward(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //@related_40
        String authURL = (String)request.getAttribute(AUTH_URL);
        
        if(authURL != null) {
            
            response.sendRedirect(response.encodeRedirectURL(authURL));
            
        }else{
            
            OAuthSessionIx oauthSession = this.getOauthSession(request);
            
            if(oauthSession == null) {
                
Log.getInstance().log(Level.WARNING, "OauthSession: {0}", this.getClass(), oauthSession);                

            }else{
                if("login".equals(oauthSession.getType())) {
                    
                    Login login = (Login)request.getAttribute("OauthService_LoginHandler");                
                        
                    if(login != null) {
                        
                        login.forward(request, response);
                        
                        return;
                    }
                }
            }
            
            super.forward(request, response);
        }
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        this.forwardPage = WebPages.PRODUCTS_SEARCHRESULTS;
        
        HttpSession session = request.getSession();
        
        final String provider = request.getParameter(PROVIDER);

        String attributeName = OAuthSessions.getAttributeName(provider);

        try{
            if(OAuthStages.REQUEST_TOKEN.equals(request.getParameter(OAuthSessionProperties.STAGE))) {
                this.handleAuthURL(request, response);
            }else{
                try{
                    this.handleOAuth(request, response);
                }finally{
                    // We are through with this oauthSession
                    session.removeAttribute(attributeName);
                }
            }
        }catch(ValidationException e) {
            if(e.getLocalizedMessage() == null) {
                throw new ServletException(e);
            }else{
                throw new ServletException(e.getLocalizedMessage(), e);
            }
        }
    }
    
    private void handleAuthURL(final HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
      
        String noscript = request.getParameter("noscript");
        
Log.getInstance().log(Level.FINE, "Noscript: {0}", this.getClass(), noscript);        

        if("1".equals(noscript)) {
            
            final String provider = request.getParameter(PROVIDER);
            final String type = request.getParameter(TYPE);
            
            if(provider == null || type == null) {
                throw new NullPointerException();
            }
            
            OAuthSessionIx oauthSession = OAuthSessions.getInstance(
                    provider, type, OAuth.getProviderProperties(provider));
            
            // @related_37
            request.getSession().setAttribute(oauthSession.getAttributeName(), oauthSession);
            
            String authURL = oauthSession.getAuthURL();
Log.getInstance().log(Level.FINE, "Auth URL: {0}", this.getClass(), authURL);        

            //@related_40
            request.setAttribute(AUTH_URL, authURL);

        }else{

            final String type = request.getParameter(OAuthSessionProperties.TYPE);

            // Just go back to referer page since com.loosebox.web.servlets.GetValue
            // will use AJAX to handle the process
            if("join".equalsIgnoreCase(type)) {
                this.forwardPage = WebPages.JOIN;
            }else{
                this.forwardPage = WebPages.LOGIN;
            }
        }
    }
    
    private void handleOAuth(final HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, ValidationException {

        // @related_37
        OAuthSessionIx oauthSession = this.getOauthSession(request);
        
Log.getInstance().log(Level.FINE, "OAuthSession: {0}", this.getClass(), oauthSession);

        Token accessToken = getAccessToken(oauthSession, request, 1, 2, 2000);
        
        final String oauthType = oauthSession.getType();

        final String provider = request.getParameter(PROVIDER);
        
        if("getAccessToken".equalsIgnoreCase(oauthType)) {
//@breakpoint
            // Admin.AdminLogin.validate(request);
            
            if(OAuthProviderNames.FACEBOOK.equalsIgnoreCase(provider)) {
                
                this.updateAccessToken(accessToken);
            }
            
            return;
        }

        OAuthProperties oauthProperties = oauthSession.getOAuthProperties();
        
        String url = oauthProperties.getCredentialsURL();
        
        Response oauthRes = oauthSession.get(url, Verb.GET, accessToken);
        
Log.getInstance().log(Level.FINE, "JSON Response:\n{0}", this.getClass(), oauthRes.getBody());            

        try {
            
            if(!oauthRes.isSuccessful()) {
                
                throw new ServletException(new StringBuilder(oauthType.toUpperCase()).append(
                        " via ").append(provider).append(" unsuccessful.").toString());
            }   
            
            final JsonObject jsonObject = this.parseToJsonObject(oauthRes.getBody());

            final String email = OAuth.this.getEmailAddress(provider, jsonObject);
//logger.log(Level.INFO, "{0}. Email from JSON Response:\n{1}", new Object[]{logger.getName(), email});            
            this.saveAccessToken(email, accessToken);
            
            UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);

            if("join".equalsIgnoreCase(oauthType)) {
                
                this.forwardPage = WebPages.WELCOME;

                Joinx quickjoin = new Joinx(){
                    @Override
                    public Map<String, String> getParameters(HttpServletRequest request) {
                        Map<String, String> params = new HashMap<>(3, 1.0f);
                        params.put(Siteuser_.emailAddress.getName(), email);
                        SecurityTool sy = new SecurityTool();
                        params.put(Siteuser_.username.getName(), sy.generateUsername());
                        params.put("password", sy.getRandomUUID(8));
                        return params;
                    }
                };
                
                quickjoin.processRequest(request, response, false);

            }else if("login".equalsIgnoreCase(oauthType)){

// logout = false, rememberMe = false, false, ignorepassword = true, usertable, email, password, request
                
                
                Loginx quicklogin = new Loginx(){
                    @Override
                    public Map<String, String> getParameters(HttpServletRequest request) {
                        Map<String, String> params = Collections.singletonMap(Siteuser_.emailAddress.getName(), email);
                        return params;
                    }
                };
                
                // We don't have a password so we ignore authentication
                quicklogin.setIgnoreAuthentication(true);
                
                quicklogin.handleRequest(request, response);

                request.setAttribute("OauthService_LoginHandler", quicklogin);

            }else{
                throw new UnsupportedOperationException();
            }
        }finally{
            // Extract the users contacts
            extractContacts(accessToken, oauthSession);
        }
    }
    
    private Token getAccessToken(OAuthSessionIx oauthSession, HttpServletRequest request, int trial, int maxTrials, long sleepTime) {
        
        OAuthProperties properties = oauthSession.getOAuthProperties();
        
        String verifierKey = properties.getVerifierKey();
        
        final String verificationCode = request.getParameter(verifierKey);
     
        Token token = oauthSession.getAccessToken(verificationCode, trial, maxTrials, sleepTime);
        
Logger.getLogger(this.getClass().getName()).log(Level.FINER, "Token: {0}", token.getToken());        

        return token;
    }
    
    private OAuthSessionIx getOauthSession(HttpServletRequest request) {
        String provider = request.getParameter(PROVIDER);
        return (OAuthSessionIx)request.getSession().getAttribute(OAuthSessions.getAttributeName(provider));
    }
    
    private void extractContacts(Token accessToken, OAuthSessionIx oauthSession) {
Log.getInstance().log(Level.FINE, "About to extract contacts. OauthSession: {0}", 
this.getClass(), oauthSession);            
        // Extract the users contacts
        OAuthProperties oauthProperties = oauthSession.getOAuthProperties();
Log.getInstance().log(Level.FINE, "Properties: {0}", 
this.getClass(), oauthProperties);            
        String url = oauthProperties.getContactsURL();
        if(url == null || url.isEmpty())  return;
        
        Response oauthRes = oauthSession.get(url, Verb.GET, accessToken);
        
Log.getInstance().log(Level.FINE, "JSON Response:\n{0}", this.getClass(), oauthRes.getBody());            
        if(oauthRes.isSuccessful()) {

            final JsonObject jsonObject = this.parseToJsonObject(oauthRes.getBody());
            
            List<String> contacts = this.getContacts(oauthSession.getProvider(), jsonObject);

            if(contacts != null && !contacts.isEmpty()) {
//@breakpoint                
//                int status = UnofficialEmails.EmailStatus.Verified.KEY;
//                new Db2EmailInsert(status).run(contacts);
            }
        }    
    }
    
    private void saveAccessToken(String email, Token accessToken) {
        
        if(email == null || accessToken == null) throw new NullPointerException();
        
        Map<String, String> wherePairs = new HashMap<>(3, 1.0f);
        wherePairs.put(Oauthuser_.emailAddress.getName(), email);
        
        JpaContext cf = WebApp.getInstance().getJpaContext();
        
        EntityController<Oauthuser, String> ec = cf.getEntityController(Oauthuser.class, String.class);
        
        final long count = ec.count(wherePairs);
        
        wherePairs.put(Oauthuser_.accessToken.getName(), accessToken.getToken());
        
        String secret = accessToken.getSecret();
        if(secret != null && secret.isEmpty()) {
            secret = null;
        }
//@breakpoint        
//        wherePairs.put(Oauthuser_.secret.getName(), null);
        wherePairs.put(Oauthuser_.secret.getName(), secret);
        
        if(count > 0) {
            Map updateValues = Collections.singletonMap(Siteuser_.emailAddress.getName(), email);
            ec.update(wherePairs, updateValues);
        }else{
            ec.insert(wherePairs);
        }
    }
    
    private String getEmailAddress(String provider, JsonObject jsonObject) {
        return jsonObject.getString("email");
    }
    
    private List<String> getContacts(String provider, JsonObject jsonObject) {
        List<String> output;
        switch(provider) {
            case OAuthProviderNames.YAHOO:
                // "emails": [ {"handle": "apollo13_vn@yahoo.com","id": "12","type": "HOME"},
                // {"handle": "nguyenmanhtam123@yahoo.com", "id": "13","primary": "true","type": "HOME"} ]
                output = this.getData(jsonObject, "emails", "handle", this.getYahooEmailFilter());
                break;
            case OAuthProviderNames.GOOGLE:
                output = this.getData(jsonObject, "entry", "displayName", null);
                break;
            default:
                output = null;
        }
        return output;
    }

    private List<String> getData(JsonObject jsonObject, String arrayKey, 
            String dataKey, Predicate<JsonObject> dataFilter) {
        try{
            final JsonArray emailArr = jsonObject.getJsonArray(arrayKey);
            
            if(emailArr == null || emailArr.isEmpty()) {
                throw new JsonException("Emails array not found in yahoo json response for user profile data");
            }
            List<String> contacts = new ArrayList<>();
            for(int i=0; i<emailArr.size(); i++) {
                JsonObject emailObj = emailArr.getJsonObject(i);
                if(dataFilter != null && dataFilter.test(emailObj)) {
                    contacts.add(emailObj.getString(dataKey).trim());
                }
            }
            // Just return the first email
            return contacts;
        }catch(JsonException e) {
            Log.getInstance().log(Level.WARNING, "{0}. {1}", this.getClass(), e, null);
            return null;
        }
    }
    
    private Predicate<JsonObject> _use_getYahooEmailFilter;
    
    private Predicate<JsonObject> getYahooEmailFilter() {
        if(_use_getYahooEmailFilter == null) _use_getYahooEmailFilter = new Predicate<JsonObject>() {
            @Override
            public boolean test(JsonObject emailObj) {
                try{
                    return !emailObj.getBoolean("primary");
                }catch(NullPointerException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
        return _use_getYahooEmailFilter;
    }
    
    public void updateAccessToken(Token accessToken) throws IOException {
        
        if(accessToken == null) throw new NullPointerException();
        
        String tokenValue = accessToken.getToken();
        String tokenSecret = accessToken.getSecret(); // Usually an empty string
//XLogger.getInstance().log(Level.INFO, "Token value: {0}, token secret: {1}", 
//        this.getClass(), tokenValue, tokenSecret);

        this.updateAccessToken(tokenValue);
    }
    
    public void updateAccessToken(String accessToken) throws IOException {
        
        if(accessToken == null) throw new NullPointerException();

        ConfigService configSvc = WebApp.getInstance().getConfigService();
        
        configSvc.storeFor("facebook.properties", "facebook."+OAuthProperties.ACCESS_TOKEN, accessToken);
        
Log.getInstance().log(Level.FINE, "Successfully saved access token and created Default Facebook Client", this.getClass());        
    }
    
    public JsonObject parseToJsonObject(String s) {
        try(final JsonReader jsonReader = Json.createReader(new StringReader(s))) {
            return jsonReader.readObject();
        }
    }
    
    public static Properties getProviderProperties(String provider) {
        final Config<Properties> config = WebApp.getInstance().getConfig();
        final ConfigData<Properties> providerSubset = config.subset(provider, ".");
        final Properties output = new Properties();
        final Set<String> names = providerSubset.getNames();
        for(String name : names) {
            output.setProperty(name, providerSubset.getString(name));
        }
        return output;
    }
}
