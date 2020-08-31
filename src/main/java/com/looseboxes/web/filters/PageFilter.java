package com.looseboxes.web.filters;

import com.bc.jpa.context.JpaContext;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.AlternateName;
import com.looseboxes.web.InitParameters;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.servlets.Displayproduct;
import com.looseboxes.web.servlets.Search;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import com.bc.web.core.redirection.AbstractDigitsRedirect;
import com.bc.web.core.redirection.Redirect;
import com.bc.web.core.filters.AbstractPageRedirectionFilter;
import com.looseboxes.pu.References;
import com.looseboxes.web.WebApp;
import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * @author Josh
 */
@WebFilter(filterName = "PageFilter",
        asyncSupported=true, 
        urlPatterns = {"*.jsp", "/activation", "/apasfc", 
        "/addpayment", "/addref", "/addshipping", "/captcha", "/Captcha", "/chat",
        "/checkout", "/contactux", "/convcurr", "/delete", "/displayproduct",
        "/edit", "/editvariant", "/insert", "/insertShipping", "/join",
        "/joinToCheckout", "/joinx", "/login", "/loginToCheckout", "/loginx",
        "/logout", "/mailCart", "/paymentservice", "/paymentsvc", "/poh", 
        "/quickchat", "/reqpwd", "/requestPassword", "/requestpassword",
        "/respondtoux", "/search", "/searchorders", "/sendmail", "/cart",
        "/submitComplain", "/ted", "/tosendmail", "/viewimg", "/local/images"
    }
)
public class PageFilter extends AbstractPageRedirectionFilter {

    private transient static final Logger LOG = Logger.getLogger(PageFilter.class.getName());

    @Override
    protected HttpServletRequest wrapRequest(HttpServletRequest request) {
        
        LOG.entering(this.getClass().getName(), "wrapRequest(HttpServletRequest)");

        HttpServletRequest output;
        
        if(this.isStaticResource()) { 
            
            output = request;
            
        }else{
            // Caveat: some servers do not handle wrappers very well for forward or
            // include requests.
            //
            output = new PageFilter.MaskShortNamesRequestWrapper((HttpServletRequest)request);
            LOG.log(Level.FINER, "Wrapped request: {0}", output);
        }
        
        return output;
    }

    @Override
    public Redirect getRedirect(HttpServletRequest request) {
        return this.getProductRedirect(request);
    }
    
    private RedirectImpl r0;
    public Redirect getProductRedirect(HttpServletRequest request) {
            
        Redirect output;
        // Redirect only urls containing /products/ e.g /products/2167.jsp
        // This ensures we do not mistakenly redirect urls of format /404.jsp
        // to /displayproduct?productid=404
        //
        final String requestUri = request.getRequestURI();
        
        if(requestUri.contains("/products/") || 
                requestUri.contains("/cat/"+ServletUtil.find(InitParameters.PRODUCTCATEGORY, request))){
            if(r0 == null) {
                r0 = new RedirectImpl();
            }
            output = r0;
        }else{
            output = null;
        }
        
        return output;
    }
    
    private static final class RedirectImpl extends AbstractDigitsRedirect {
        
        @Override
        public String getRedirectUri(HttpServletRequest request) {
            String str;
            if(request.getRequestURI().contains("/products/")) {
                str = this.getDigitsRedirectUri(request);
            }else{
                str = this.getCategoryRedirectUri(request);
            }
            return str;
        }
        
        public String getCategoryRedirectUri(HttpServletRequest request) {

            final String requestUri = request.getRequestURI();
            
            final Object cat = ServletUtil.find(InitParameters.PRODUCTCATEGORY, request);
            
            final String ref = "/cat/" + cat; // E.g /cat/fashion or /cat/mobile-phones etc

            LOG.finer(() -> MessageFormat.format("Request URI: {0}, Reference: {1}", 
                    requestUri, ref));

            String redirectUri = null;

            // Convert: ../context/cat/fashion/KidsClothing.jsp
            //      To: ../context/search?cat=fashion&subcat=KidsClothing
            //
            if(requestUri.contains(ref)) {

                int n = requestUri.lastIndexOf('/'); 
                
                String file = requestUri.substring(n + 1);

                LOG.log(Level.FINER, "File part: {0}", file);

                int m;
                
                if((m = file.indexOf('.')) != -1) {
                    
                    final String nameWithoutExt = file.substring(0, m);
                    
                    final JpaContext jpa = WebApp.getInstance().getJpaContext();
                    
                    final References.productsubcategory en = (References.productsubcategory)jpa
                            .getEnumReferences().getEnum(Product_.productsubcategoryid.getName(), nameWithoutExt);
                    
                    final Object subcat;
                    if(en != null) {
                        subcat = en.getLabel();
                    }else{
                        subcat = nameWithoutExt;
                    }
                    
                    final StringBuilder builder = new StringBuilder();
                    ServletUtil.appendServletPath(Search.class, builder);
                    builder.append('?');
                    builder.append(Product_.productcategoryid.getName()).append('=').append(cat);
                    builder.append('&');
                    builder.append(Product_.productsubcategoryid.getName()).append('=').append(subcat);
                    
                    redirectUri = builder.toString();
                }
            }

            Level level = redirectUri == null ? Level.FINER : Level.FINE;
            LOG.log(level, "Redirect URI: {0}", redirectUri);

            return redirectUri;
        }
        
        @Override
        protected void appendDigits(long lval, StringBuilder appendTo) {
            //   Convert: /context/234_we are victorious in christ jesus.jsp
            //        To: /context/displayproduct?productid=234
            WebServlet ws = Displayproduct.class.getAnnotation(WebServlet.class);
            appendTo.append(ws.urlPatterns()[0]);
            super.appendDigits(lval, appendTo); 
        }
        @Override
        protected String getDigitsKey() {
            return Product_.productid.getName();
        }
    }
    
    /**
     * This request wrapper class extends the support class
     * HttpServletRequestWrapper, which implements all the methods in the
     * HttpServletRequest interface, as delegations to the wrapped request. 
     */
    private static final class MaskShortNamesRequestWrapper extends HttpServletRequestWrapper {
        
        public MaskShortNamesRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public Part getPart(String name) throws IllegalStateException, IOException, ServletException {
            
            HttpServletRequest httpReq = (HttpServletRequest)this.getRequest();
            
            Part output = httpReq.getPart(name);
            
            if(output == null) {
                
                String alt = AlternateName.getAlternate(name);
                
                if(alt != null) {
                
                    output = httpReq.getPart(alt);
                }
            }
            
            return output;
        }

        @Override
        public void removeAttribute(String name) {
            
            getRequest().removeAttribute(name);
            
            String alt = AlternateName.getAlternate(name);

            if(alt != null) {

                getRequest().removeAttribute(alt);
            }
        }

        @Override
        public Enumeration<String> getAttributeNames() {

            Enumeration<String> output = getRequest().getParameterNames();
            
            return this.getCompositeNames(output);
        }

        @Override
        public Object getAttribute(String name) {
            
            Object output = getRequest().getAttribute(name);
            
            if(output == null) {
                
                String alt = AlternateName.getAlternate(name);
                
                if(alt != null) {
                
                    output = getRequest().getAttribute(alt);
                }
            }
            
            return output;
        }

        @Override
        public String getParameter(String name) {
            
            String output = getRequest().getParameter(name);
            
            if(output == null) {
                
                String alt = AlternateName.getAlternate(name);
                LOG.finer(() -> "Name: "+name+", alternate: "+alt);
                if(alt != null) {
                
                    output = getRequest().getParameter(alt);
                    if(LOG.isLoggable(Level.FINER)) {
                        LOG.log(Level.FINER, "Name: {0}, alternate: {1}, value: {2}", 
                                new Object[]{name, alt, output});
                    }
                }
            }
            
            return output;
        }
        
        @Override
        public String[] getParameterValues(String name) {

            String [] output = getRequest().getParameterValues(name);
            
            if(output == null) {
                
                String alt = AlternateName.getAlternate(name);
                
                if(alt != null) {
                
                    output = getRequest().getParameterValues(alt);
                }
            }
            
            return output;
        }

        @Override
        public Enumeration getParameterNames() {

            Enumeration<String> output = getRequest().getParameterNames();
            
            return this.getCompositeNames(output);
        }
        
        private Enumeration getCompositeNames(Enumeration<String> enumeration) {
            
            if(enumeration != null) {

                // Add both names and their long versions
                //
                final List<String> names = new ArrayList<>();
                
                while(enumeration.hasMoreElements()) {
                    
                    String name = enumeration.nextElement();
                    
                    names.add(name);
                    
                    String alt = AlternateName.getAlternate(name);
                    
                    if(alt != null) {
        
                        LOG.finer(() -> "Adding alternate '"+alt+"' for '"+name+'\'');
                        names.add(alt);
                    }
                }

                LOG.log(Level.FINER, "Composite names: {0}", names);
                
                if(!names.isEmpty()) {
                    
                    enumeration = new Enumeration<String>() {
                        int off;
                        @Override
                        public boolean hasMoreElements() {
                            return off < names.size();
                        }
                        @Override
                        public String nextElement() {
                            try{
                                return names.get(off);
                            }finally{
                                ++off;
                            }
                        }
                    };
                }
            }
            
            return enumeration;
        }        
        
        @Override
        public Map getParameterMap() {

            Map output = getRequest().getParameterMap();
            
            if(output != null) {
                
                Set names = output.keySet();
                
                Map altNamesMap = null;
                
                for(Object name:names) {
                
                    String alt = AlternateName.getAlternate(name.toString());
                    
                    if(alt != null) {
                        
                        if(altNamesMap == null) {
                            altNamesMap = new HashMap();
                        }
                        
                        altNamesMap.put(alt, output.get(name));
                    }
                }
                
                output.putAll(altNamesMap);
            }
            
            return output;
        }
    }
}
/***
 * 
    private void loginUserIfAuthorized(HttpSession session) {
        
        Boolean rememberme = (Boolean)session.getAttribute(Login.ATTRIBUTE_REMEMBERME);
        
        if(rememberme != null) { // Remember me already attempted
            return;
        }
        
        try{
            
            LbAuthSvcSession authSess = WebApp.getInstance().getAuthSvcSession();
            
            if(authSess.isReadyForUse()) {

                session.setAttribute(Login.ATTRIBUTE_REMEMBERME, Boolean.FALSE);
                
// Use method remoteXXX e.g remoteAuthorize not authorizeUser
//        
//                Map params = Collections.emptyMap(); WE NEED USER'S DETAILS. REMEMBER ME IS DONE IMM AFTER LOGIN
                
                JSONObject auth_user = WebApp.getInstance().getAuthSvcSession().remoteAuthorize(params);

                if(auth_user != null) {

                    UserBean user = (UserBean)session.getAttribute(UserBean.ATTRIBUTE_NAME);

                    user.login(auth_user);
                    
                    session.setAttribute(Login.ATTRIBUTE_REMEMBERME, Boolean.TRUE);
                }
            }
        }catch(RuntimeException e) {
            XLogger.getInstance().log(Level.WARNING, "An unexpected error occured while trying to check if a user is authorized and then login the user if authorized", this.getClass(), e);
        }
    }
 * 
 */