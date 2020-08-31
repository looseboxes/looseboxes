package com.looseboxes.web;

import com.authsvc.client.AuthDetailsLocalDiscStore;
import com.authsvc.client.AuthDetailsStore;
import com.bc.config.CompositeConfig;
import com.bc.config.DirConfigService;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.PuInstaller;
import com.looseboxes.web.plugins.jcaptcha.ListImageCaptchaEngine;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.TimeZone;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import com.bc.config.ConfigService;
import com.bc.net.FailedConnectionTest;
import com.bc.security.Encryption;
import com.bc.security.SecurityProvider;
import com.bc.util.Retry;
import com.bc.util.Util;
import com.bc.web.core.captcha.CaptchaFactory;
import java.io.File;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import com.bc.config.Config;
import com.bc.util.RetryImpl;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @(#)WebApp.java   11-Apr-2015 07:26:23
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
public class WebApp extends LbApp {

    private transient static final Logger LOG = Logger.getLogger(WebApp.class.getName());
    
    public static final String ATTRIBUTE_NAME = "App";
    
    private static WebApp instance;
    
    private boolean productionMode;
    
    private ServletContext servletContext;
    
    private Config<Properties> config;
    
    private ConfigService configService;
    
    private Encryption encryption;
    
    private LbAuthSvcSession authSvcSession;
    
    protected WebApp() { }
    
    public static WebApp getInstance() {
        if(instance == null) {
            instance = new WebApp();
            LbApp.setInstance(instance);
        }
        return instance;
    }
    
    public static void setInstance(WebApp app) {
        instance = app;
        LbApp.setInstance(app);
    }
    
    @Override
    public boolean isProductionMode() {
        return this.productionMode;
    }
    
    @Override
    public String getPackageLoggerName() {
        return WebApp.class.getPackage().getName();
    }
    
    
    public void init(ServletContext context) 
            throws URISyntaxException, IOException, ClassNotFoundException, SQLException {
        
        this.servletContext = context;
        
        final String sval = context.getInitParameter("productionMode");
        
        this.productionMode = sval == null ? false : Boolean.parseBoolean(sval);

        this.readLoggingConfig(productionMode);
        
        this.initProperties(this.productionMode);
        
        final String persistenceURI = this.config.getString(AppProperties.PERSISTENCE_URI, null);

        super.init(persistenceURI);
        
        this.setServletContextAttributeFromProperty(AppProperties.BASE_URL);
        
        this.setServletContextAttributeFromProperty(AppProperties.LOCAL_URL);

        PuInstaller puInstaller = new PuInstaller();
        if(!puInstaller.isInstalled()) {
            String dbName = config.getString(AppProperties.DATABASE_NAME);
            String dbUser = config.getString(AppProperties.DATABASE_USER);
            String dbPass = config.getString(AppProperties.DATABASE_PASSWORD);
            puInstaller.install(dbName, dbUser, dbPass);
        }
        
        try{
            
            LOG.info("Creating security tool");

            this.encryption = SecurityProvider.DEFAULT.getEncryption(
                    this.config.getString(AppProperties.ALGORITHM),
                    this.config.getString(AppProperties.ENCRYPTION_KEY));
            
        }catch(GeneralSecurityException e) {
            throw new RuntimeException("Failed to create instance of type: "+com.bc.security.Encryption.class.getName(), e);
        }
        
        this.initAuthSvcSession(this.config);
        
        CaptchaFactory.setDefaultImageCaptchaEngine(new ListImageCaptchaEngine());
    }
    
    public void readLoggingConfig(boolean productionMode) throws IOException {
        final URL loggingConfigUrl;
        if(this.productionMode) {
            loggingConfigUrl = Thread.currentThread().getContextClassLoader()
                    .getResource("META-INF/logging.properties");
        }else{
            loggingConfigUrl = Thread.currentThread().getContextClassLoader()
                    .getResource("META-INF/logging_devmode.properties");
        }
        
        LOG.log(Level.INFO, "Logging config URL: {0}", loggingConfigUrl);

        try(final InputStream in = loggingConfigUrl.openStream()) {
            LogManager.getLogManager().readConfiguration(in);
        }
    }
    
    public void initAuthSvcSession(Config config) {
        
        final String authsvc_url = config.getString(AppProperties.AUTHSVC_URL);
        final String app_name = WebApp.getInstance().getName();
        final String app_email = config.getString(AppProperties.AUTHSVC_EMAIL);
        final String app_pass = config.getString(AppProperties.AUTHSVC_PASSWORD);
        
        final AuthDetailsLocalDiscStore.GetPathForName pathContext = new AuthDetailsLocalDiscStore.GetPathForName() {
            @Override
            public String apply(String filename) {
                return LbApp.getInstance().getPath(filename);
            }
        };
        
        final AuthDetailsStore store = new AuthDetailsLocalDiscStore(
                pathContext, 
                "com.looseboxes.web.app.token.map", 
                "com.looseboxes.web.app.details.map");
        
        // We delay initialization so that in the event the authentication service is on
        // the same server and we are just starting, then the authentication service
        // should be already up and running before we call this.
        final float delay = config.getFloat(AppProperties.AUTHSVC_INITIALIZATION_DELAY, 1);
        final long delayMillis = (long)delay * 60000;
        
        final Callable<LbAuthSvcSession> task = () -> {
            final LbAuthSvcSession session = new LbAuthSvcSession(store, authsvc_url);
            session.init(app_name, app_email, app_pass, true);
            authSvcSession = session;
            return session;
        };

        final Retry retry = new RetryImpl(2, 10_000, TimeUnit.MILLISECONDS);
        
        retry.retryAsyncIf(task, new FailedConnectionTest(), delayMillis);
    }
    
    public void setServletContextAttributeFromProperty(String name) {
        final String value = this.config.getString(name);
        Objects.requireNonNull(value, name + " cannot be null");
        this.servletContext.setAttribute(name, value);
    }
    
    public void setDefaultTimeZone(ServletContext context) {
        String timeZoneParam = context.getInitParameter("timeZone");
        TimeZone timeZone;
        if(timeZoneParam == null) {
            timeZone = this.getDefaults().getDefaultTimeZone();
            LOG.log(Level.INFO, "Init parameter 'timeZone' not set. Using default of: {0}", timeZone);                
        }else{
            timeZone = TimeZone.getTimeZone(timeZoneParam);
        }
        TimeZone.setDefault(timeZone);
    }
    
    public URL getContextURL() throws MalformedURLException {
        return getURL(this.servletContext.getContextPath());
    }
    
    /**
     * @param fname The file name of the file whose URL will be returned
     * @return The URL of the file referenced by the input file name
     * @throws java.net.MalformedURLException
     */
    @Override
    public URL getURL(String fname) throws MalformedURLException {
  
        // Some inputs may contian format:   \parent\child\file.ext
        // However urls should be of format: /parent/child/file.ext
        //
        fname = fname.replace('\\', '/');
        if(!fname.startsWith("/")) {
            fname = '/' + fname;
        }
        String contextPath = this.servletContext.getContextPath();
        if(contextPath == null) {
            contextPath = "";
        }
        URL output = new URL(this.getBaseURL() + contextPath + fname);
        return output;
    }

    @Override
    public String getBaseURL() {
        return this.config.getString(AppProperties.BASE_URL);
    }
    
    /**
     * An external url is a link used by the application and located outside 
     * the application's context. This implementation simply calls 
     * {@link #getURL(java.lang.String)}. Subclasses should therefore override 
     * this method to return the appropriate external URL.
     * @param fname The file name of the file whose URL will be returned
     * @return The URL referencing a file located outside this application's context
     * @throws java.net.MalformedURLException
     */
    @Override
    public URL getExternalURL(String fname) throws MalformedURLException {
        
        final String localURL = this.config.getString(AppProperties.LOCAL_URL);
        LOG.finer(() -> "Local folder: "+localURL+", filename: "+fname);
        
// This doesn't work as Paths.get(first, ..more) is not meant for  URLs
//         Paths.get(localURL, fname).toUri().toURL();

        String urlPart = fname.replace('\\', '/');
        if(!urlPart.startsWith("/")) {
            urlPart = "/" + urlPart;
        }
        
        return new URL(localURL + urlPart);
    }
    
    /**
     * An external path is a path used by the application and located outside 
     * the application's context. 
     * @param fname The file name of the file whose external path will be returned
     * @return The path used by this application and located outside this application's context
     */
    @Override
    public String getExternalPath(String fname) {
        
        String localPath = this.config.getString(AppProperties.LOCAL_PATH);
        String realPath = Paths.get(localPath, fname).toString();
        
// In non-production mode, netbeans IDE uses web folder as a kind of default context
// Format /webapp/web/local/images to the less volatile /webapp/local/images        
// 
if(!this.isProductionMode()) {
    realPath = realPath.replace("\\web\\", "\\");
    realPath = realPath.replace("/web/", "/");
}
        return realPath;
    }
    
    /**
     * @param fname The file name of the file whose path will be returned
     * @return The full path of the file referenced by the input file name
     */
    @Override
    public String getPath(String fname) {

        if(!fname.startsWith("/") && !fname.startsWith("\\")) {
            fname = "/" + fname;
        }
        
        String realPath;
        if(this.config != null) {
            String basePath = this.config.getString(AppProperties.BASE_PATH);
            if(basePath != null) {
                realPath = Paths.get(basePath, fname).toString();
            }else{
                realPath = this.servletContext.getRealPath(fname);
            }
        }else{
            realPath = this.servletContext.getRealPath(fname);
        }
        
// In non-production mode, netbeans uses build folder which is very volatile
// format /webapp/build/web/local/images to the less volatile /webapp/web/local/images        
//  
if(!this.isProductionMode()) {
    realPath = realPath.replace("\\build\\", "\\");
    realPath = realPath.replace("/build/", "/");
}
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "Name: {0}, path: {1}", new Object[]{fname, realPath});
        }
        return realPath;
    }
    
    public String getName() {
        return this.servletContext.getInitParameter(InitParameters.SITE_NAME);
    }
    
    public String getDatabaseName() {
        return this.config.getString(AppProperties.DATABASE_NAME);
    }
    
    public void initProperties(boolean productionMode) throws IOException {

        final URL defaultDir = Thread.currentThread().getContextClassLoader()
                .getResource("META-INF/properties/defaults"); 
        
//        this.temp(defaultDir);
        
        final URL dir = Thread.currentThread().getContextClassLoader().getResource(
                productionMode ? "META-INF/properties" : "META-INF/properties/devmode"
        );

        LOG.info(() -> "Default properties path: "+defaultDir+"\n        Properties path: "+dir);        

        final long mb4 = Util.availableMemory();

        this.configService = new DirConfigService(
                new File(defaultDir.getFile()).toString(), new File(dir.getFile()).toString(), null);
        
        // All the properties in one map... best for speed and memory
        //
        this.config = new CompositeConfig(this.configService);
        
        LOG.log(Level.INFO, "Loaded Properties... Memory used: {0}", Util.usedMemory(mb4));        
    }    
    
    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public ConfigService<Properties> getConfigService() {
        return configService;
    }

    public Config<Properties> getConfig() {
        return config;
    }
    
    public Encryption getEncryption() {
        return encryption;
    }

    public LbAuthSvcSession getAuthSvcSession() {
        return authSvcSession;
    }

    public void setAuthSvcSession(LbAuthSvcSession authSvcSession) {
        this.authSvcSession = authSvcSession;
    }

    private void temp(URL url) {
        System.out.println("URL: "+url);
        this.temp(url.getFile());
        try{
            this.temp(Paths.get(url.toURI()).toFile().toString());
        }catch(URISyntaxException e) {
            System.err.println("===========================\n"+e);
        }
    }
    
    private void temp(String path) {
        try{
        System.out.println("Path: " + path);
        final File file = new File(path);
        System.out.println("File: " + file);
        System.out.println("File exists: " + file.exists());
        final String [] names = file.list();
        System.out.println("File names in file: " + (names==null?null:Arrays.asList(names)));
        System.out.println("File name: " + Paths.get(path).getFileName().toString());
        }catch(Exception e) {
            System.err.println(e.toString());
        }
    }
}
