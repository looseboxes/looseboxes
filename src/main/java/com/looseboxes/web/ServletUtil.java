package com.looseboxes.web;

import com.bc.util.QueryParametersConverter;
import com.bc.util.Log;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.servlets.Displayproduct;
import com.looseboxes.web.servlets.Search;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @(#)ServletUtil.java   23-Apr-2015 19:49:08
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
public class ServletUtil extends com.looseboxes.core.Util {

    //@related_useragent_pattern
    //@last_update:30 May 2015
    //@next_update:30 August 2015@http://detectmobilebrowsers.com/
    public static final Pattern userAgent_pattern = 
            Pattern.compile("(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*");
    public static final Pattern userAgent_pattern2 = 
            Pattern.compile("(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-");

    public static boolean isStatic(String link) {
        return isStaticResource(link) || isStaticPage(link);
    }
    
    public static boolean isStaticResource(String link) {
        boolean staticResource = 
                link.endsWith(".js") || link.endsWith(".JS") ||
                link.endsWith(".css") || link.endsWith(".CSS") ||
                link.endsWith(".jpg") || link.endsWith(".JPG") ||
                link.endsWith(".jpeg") || link.endsWith(".JPEG") ||
                link.endsWith(".png") || link.endsWith(".PNG") ||
                link.endsWith(".gif") || link.endsWith(".GIF") ||
                link.endsWith(".ico") || link.endsWith(".icon") ||
                link.endsWith(".txt") || link.endsWith(".TXT");
        return staticResource;
    }

    public static boolean isStaticPage(String link) {
        boolean staticResource = 
                link.endsWith(".xml") || link.endsWith(".XML") ||
                link.endsWith(".html") || link.endsWith(".HTML") ||
                link.endsWith(".htm") || link.endsWith(".HTM") ||
                link.endsWith(".xhtml") || link.endsWith(".XHTML");
        return staticResource;
    }
    
    public static Map<String, String> getParameterMap(ServletRequest request) {
    
        return getParameterMap(request, false, false);
    }
    
    public static Map<String, String> getParameterMap(
            ServletRequest request, boolean nullsAllowed, boolean emptyStringsAllowed) {
        
        Enumeration<String> names = request.getParameterNames();

        Map params = null;

        while(names.hasMoreElements()) {

            String name = names.nextElement();

            String value = request.getParameter(name);

            if(!nullsAllowed && value == null) {
                continue;
            }

            if(!emptyStringsAllowed && value.isEmpty()) {
                continue;
            }
            
            if(params == null) {
                params = new HashMap();
            }

            params.put(name, value);
        }
        
        return params;
    }
    
    public static String getURL(
            Class<? extends HttpServlet> servletClass, Map params) 
            throws MalformedURLException {
        
        return getURL(servletClass, params, true);
    }
    
    public static String getURL(
            Class<? extends HttpServlet> servletClass, 
            Map params, boolean shortest) throws MalformedURLException {
        
        StringBuilder appendto = new StringBuilder();
        
        appendURL(servletClass, params, appendto, shortest);
        
        return appendto.toString();
    }

    public static void appendLink(
            Product record, boolean external, boolean useSearchText, StringBuilder appendTo) 
            throws MalformedURLException {
        
        Map params = new HashMap<>(2, 1.0f);
        
        if(useSearchText) {
            params.put(Product_.productcategoryid.getName(), record.getProductcategoryid().getProductcategoryid());
            params.put(Search.SEARCH_TEXT, record.getProductName());
        }else{
            params.put(Product_.productid.getName(), record.getProductid());
        }
        
        Class<? extends HttpServlet> servletClass = useSearchText ? Search.class : Displayproduct.class;
        
        if(external) {
            appendURL(servletClass, params, appendTo, true);
        }else{
            appendContextQuery(servletClass, params, appendTo, true);
        }
    }
    
    public static void appendURL(
            Class<? extends HttpServlet> servletClass, 
            Map params, StringBuilder appendTo, boolean shortest) throws MalformedURLException {
        
        StringBuilder relativePath = new StringBuilder();
        
        appendContextQuery(servletClass, params, relativePath, shortest);
        
        appendTo.append(WebApp.getInstance().getURL(relativePath.toString()));
    }
    
    /**
     * Appends a relative path starting with the context path and having the 
     * query which when called ensures execution is passed to the specified class
     */
    public static void appendContextQuery(
            Class<? extends HttpServlet> servletClass, 
            StringBuilder appendTo, boolean shortest) {
            appendContextQuery(servletClass, null, appendTo, shortest);
    }

    /**
     * Appends a relative path starting with the context path and having the 
     * query which when called ensures execution is passed to the specified class
     * @param servletClass The class name of the Handler class to handle the generated query.
     * @param params
     * @param appendTo
     * @param shortest
     */
    public static void appendContextQuery(
            Class<? extends HttpServlet> servletClass, Map params, StringBuilder appendTo, boolean shortest) {
        
        ServletUtil.appendServletPath(servletClass, appendTo);
        
        if(params != null && !params.isEmpty()) {

            appendTo.append('?');
        
            ServletUtil.appendQuery(params, appendTo, shortest);
        }
Log.getInstance().log(Level.FINER, "StringBuilder: {0}", ServletUtil.class, appendTo);        
    }

    public static void appendServletPath(Class<? extends HttpServlet> servletClass, StringBuilder appendTo) {
        
        WebServlet ws = servletClass.getAnnotation(WebServlet.class);
        
        if(ws == null) {
            
            throw new NullPointerException("Class does not have the "+WebServlet.class.getName()+" annotation. Class: "+servletClass.getName());
            
        }else{
            
            String [] urlPatterns = ws.urlPatterns();
            
            String target = get(urlPatterns, false);
            
            appendTo.append(target);
        }
    }
    
    public static void appendQuery(Map params, StringBuilder builder, boolean shortest){
        appendQuery(params, builder, "utf-8", shortest);
    }
    
    public static void appendQuery(Map params, StringBuilder builder, final String charset, boolean shortest){
        if(!shortest) {
            appendQuery(params, builder, charset);
        }else{
            QueryParametersConverter qpc = new QueryParametersConverter() {
                @Override
                public Object convertKey(Object key, Object val) {
                    String shortName = AlternateName.getShortName(key.toString());
                    return shortName == null ? key : shortName;
                }
                @Override
                public Object convertValue(Object key, Object val, boolean encode, String charset) {
                    String shortName = AlternateName.getShortName(val.toString());
                    if(shortName != null) {
                        val = shortName;
                    }
                    return super.convertValue(key, val, encode, charset);
                }
            };
            builder.append(qpc.convert(params, charset != null, charset));
        }
    }
    
    public static boolean isMultipart(HttpServletRequest request) {
        String type = getConentType(request);
        return (type != null &&
            type.toLowerCase().startsWith("multipart/form-data"));
    }

    public static String getConentType(HttpServletRequest request) {
        // Check the content type to make sure it's "multipart/form-data"
        // Access header two ways to work around WebSphere oddities
        String type = null;
        String type1 = request.getHeader("Content-Type");
        String type2 = request.getContentType();
        // If one value is null, choose the other value
        if (type1 == null && type2 != null) {
          type = type2;
        }
        else if (type2 == null && type1 != null) {
          type = type1;
        }
        // If neither value is null, choose the longer value
        else if (type1 != null && type2 != null) {
          type = (type1.length() > type2.length() ? type1 : type2);
        }
        return type;
    }

    public static boolean isMobile(HttpSession session) {
        Boolean isMobile = (Boolean)session.getAttribute(Attributes.MOBILE);
        return isMobile == null ? false : isMobile;
    }
    
    /**
     * Check's if the request was from a mobile platform. If true, set's
     * the session attribute {@link com.loosebox.web.Attributes#MOBILE}
     * to true, otherwise set's the attribute to false.
     * @param request
     * @return
     */
    public static boolean isMobile(HttpServletRequest request) {

        // Yep create a session if no one exists
        // 
        HttpSession session = request.getSession(true);

        Boolean mobile;

        String mobileStr = request.getParameter("mobile");

        if(mobileStr != null) {
            mobile = Boolean.valueOf(mobileStr);
        }else{
            mobile = (Boolean)session.getAttribute(Attributes.MOBILE);
            if(mobile == null) {
                String URL = request.getRequestURL().toString();
                String referer = request.getHeader("referer");
Log.getInstance().log(Level.FINER, "Referer: {0}, URI: {1}.", ServletUtil.class, referer, URL);
                String tgt = URL == null ? referer : URL;
                if(tgt != null && (tgt.endsWith(Dirs.MOBILE) | tgt.contains(Dirs.MOBILE+"/"))) {
                    mobile = true;
                }else{
                    
                    String user_agent = request.getHeader("user-agent");
Log.getInstance().log(Level.FINER, "User agent: {0}.", ServletUtil.class, user_agent);
                    if(user_agent != null) {

                        user_agent = user_agent.toLowerCase();

                        if(userAgent_pattern.matcher(user_agent).find()) {
                            mobile = true;
                        }else{
                            if(user_agent.length()>3) {
                                mobile = userAgent_pattern2.matcher(user_agent.substring(0,4)).find();
                            }
                        }
                    }
                }
            }
        }

        if(mobile == null) mobile = false;

Log.getInstance().log(Level.FINER, "Is mobile: {0}", ServletUtil.class, mobile);
        session.setAttribute(Attributes.MOBILE, mobile);

        return mobile;
    }

    /**
     * @param request The request whose source page will be returned
     * @param defaultPage The page to return if not valid source page was found
     * @return The page which called the input request
     * @see #getSourcePage(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String...) 
     */
    public static String getSourcePage(
            HttpServletRequest request, String defaultPage) {    
        
        return getSourcePage(request, defaultPage, (String[])null);
    }

    /**
     * Only referers having filenames with extenions and matching the request's
     * context are returned. If these criterias are not met then the input
     * <tt>defaultPage</tt> is returned.
     * @param request The request whose source page will be returned
     * @param defaultPage The page to return if not valid source page was found
     * @param extensions The extensions <tt>(e.g 'jpg', 'html', 'jsp')</tt> 
     * for which a page will be considered valid
     * @return The page which called the input request
     */
    public static String getSourcePage(
            HttpServletRequest request, String defaultPage, String ...extensions) {    

        String sourcePage;
        
        String referer = request.getHeader("referer");

        if(referer == null) {
            
            sourcePage = defaultPage;
            
        }else{

            String extension = com.bc.util.Util.getExtension(referer);
            
Log.getInstance().log(Level.FINER, "Referer: {0}, extension: {1}", ServletUtil.class, referer, extension);

            if(extension == null) {
                
                sourcePage = defaultPage;
                
            }else{
                try{

                    boolean OK = (extensions == null || extensions.length == 0) || 
                            contains(extension, true, (Object[])extensions);

                    if(OK) {
                        
                        WebApp webApp = WebApp.getInstance();
                        String contextPath = webApp.getServletContext().getContextPath();
                        String contextUrl;

                        if(referer.startsWith(contextPath)) {

                            sourcePage = referer.substring(contextPath.length());

                        }else if(referer.startsWith(contextUrl = webApp.getContextURL().toString())){    

                            sourcePage = referer.substring(contextUrl.length());

                        }else {

                            sourcePage =  defaultPage;
                        }
                    }else{
                        
                        sourcePage = defaultPage;
                    }
                }catch(MalformedURLException e) {
                    
                    Log.getInstance().log(Level.WARNING, "Unexpected exception", ServletUtil.class, e);

                    sourcePage = defaultPage;
                }
            }
        }
        
Log.getInstance().log(Level.FINER, "Referer: {0}, RequestURI: {1}, Source page: {2}", 
ServletUtil.class, referer, request.getRequestURI(), sourcePage);

        return sourcePage;
    }
    
    public static String toString(String name, HttpServletRequest request) {
        StringBuilder builder = new StringBuilder("HttpRequest.getParameter:");
        builder.append(request.getParameter(name)).append(' ');
        builder.append("HttpRequest.getAttribute:").append(request.getAttribute(name)).append(' ');
        builder.append("HttpSession.getAttribute:").append(request.getSession().getAttribute(name)).append(' ');
        builder.append("ServletContext.getAttribute:").append(request.getSession().getServletContext().getAttribute(name));
        return builder.toString();
    }
    
    public static String getDetails(ServletContext context, String separator, Level level) {

        StringBuilder builder = new StringBuilder("DETAILS FOR SERVLET CONTEXT:\n");
        builder = append(builder, "ContextPath", context.getContextPath(), separator);
        try {
            builder = append(builder, "getResource(/)", context.getResource("/"), separator);
        }catch(Exception e) {
            // Yep do nothing
        }
        builder = append(builder, "RealPath", context.getRealPath("/"), separator);
        builder = append(builder, "ServerInfo", context.getServerInfo(), separator);

        if(level != Level.FINER) return builder.toString();

        Enumeration en = context.getAttributeNames();
        while(en.hasMoreElements()) {
            Object key = en.nextElement();
            builder = append(builder, key, context.getAttribute(key.toString()), separator);
        }

        return builder.toString();
    }

    public static String getDetails(HttpSession session, String separator, Level level) {

        StringBuilder builder = new StringBuilder("DETAILS FOR SESSION:\n");
        
        builder = append(builder, "Id", session.getId(), separator);

        if(level != Level.FINER) return builder.toString();

        builder = append(builder, "CreationTime", session.getCreationTime(), separator);
        builder = append(builder, "LastAccessedTime", session.getLastAccessedTime(), separator);
        builder = append(builder, "ServletContext", session.getServletContext(), separator);
        Enumeration en = session.getAttributeNames();
        while(en.hasMoreElements()) {
            Object key = en.nextElement();
            builder = append(builder, key, session.getAttribute(key.toString()), separator);
        }

        return builder.toString();
    }

    public static String getDetails(ServletRequest request, String separator, Level level) {

        HttpServletRequest httpReq = null;
        if(request instanceof HttpServletRequest) {
            httpReq = (HttpServletRequest)request;
        }

        StringBuilder builder = new StringBuilder("DETAILS FOR HTTP_REQUEST:\n");

        if(httpReq != null) {
            builder = append(builder, "RequestURL", httpReq.getRequestURL(), separator);
            builder = append(builder, "QueryString", httpReq.getQueryString(), separator);
            builder = append(builder, "HttpRequest#getHeader('from')", httpReq.getHeader("from"), separator);
        }

        if(level.intValue() > Level.FINER.intValue()) return builder.toString();

        if(httpReq != null) {
            builder = append(builder, "RequestURI", httpReq.getRequestURI(), separator);
            builder = append(builder, "ServletPath", httpReq.getServletPath(), separator);
            builder = append(builder, "UserPrincipal", httpReq.getUserPrincipal(), separator);
        }

        builder = append(builder, "ServerName", request.getServerName(), separator);
        builder = append(builder, "ServerPort", request.getServerPort(), separator);

        builder = append(builder, "LocalAddr", request.getLocalAddr(), separator);
        builder = append(builder, "LocalName", request.getLocalName(), separator);
        builder = append(builder, "LocalPort", request.getLocalPort(), separator);

        builder = append(builder, "RemoteAddr", request.getRemoteAddr(), separator);
        builder = append(builder, "RemoteHost", request.getRemoteHost(), separator);
        builder = append(builder, "RemotePort", request.getRemotePort(), separator);

        if(httpReq != null) {
            builder = append(builder, "RemoteUser", httpReq.getRemoteUser(), separator);

            builder = append(builder, "ContextPath", httpReq.getContextPath(), separator);
            builder = append(builder, "PathInfo", httpReq.getPathInfo(), separator);
            builder = append(builder, "PathTranslated", httpReq.getPathTranslated(), separator);
        }

        if(level.intValue() > Level.FINEST.intValue()) return builder.toString();

        builder.append("\nATTRIBUTES:\n");
        Enumeration en = request.getAttributeNames();
        while(en.hasMoreElements()) {
            Object key = en.nextElement();
            builder = append(builder, key, request.getAttribute(key.toString()), separator);
        }

        builder.append("\nPARAMETERS:\n");
        Map params = getParameterMap(request); 
        Iterator iter = params.keySet().iterator();
        while(iter.hasNext()) {
            Object key = iter.next();
            builder = append(builder, key, params.get(key), separator);
        }

        if(httpReq != null) {
            builder.append("\nHEADERS:\n");
            en = httpReq.getHeaderNames();
            while(en.hasMoreElements()) {
                Object key = en.nextElement();
                builder = append(builder, key, httpReq.getHeader(key.toString()), separator);
            }
            
            // Note this
            builder = append(builder, "referer", httpReq.getHeader("referer"), separator);
        }

        return builder.toString();
    }

    public static String getDetails(URL url, String separator) {
        StringBuilder builder = new StringBuilder("DETAILS FOR URL: "+url+"\n");
        builder = append(builder, "Authority", url.getAuthority(), separator);
        builder = append(builder, "File", url.getFile(), separator);
        builder = append(builder, "Host", url.getHost(), separator);
        builder = append(builder, "Path", url.getPath(), separator);
        builder = append(builder, "Port", url.getPort(), separator);
        builder = append(builder, "Protocol", url.getProtocol(), separator);
        builder = append(builder, "Query", url.getQuery(), separator);
        builder = append(builder, "Ref", url.getRef(), separator);
        builder = append(builder, "UserInfo", url.getUserInfo(), separator);
        return builder.toString();
    }

    private static StringBuilder append(StringBuilder builder, Object key, Object value, String separator) {
        builder.append(key);
        builder.append(" = ");
        if(value instanceof Object[]) {
            value = Arrays.toString((Object[])value);
        }
        
        try {
            builder.append(value);
        }catch(NullPointerException e) {
            builder.append("null");
            // This is any ugly bug
        }

        builder.append(separator);
        
        return builder;
    }

    public static void remove(String name, HttpServletRequest request) {
        removeAllAttributes(name, request);
    }
    
    /**
     * The named attribute is removed from the request, its session and its 
     * session's servlet context.<br/>
     * @param name The name of the attribute to be removed 
     * @param request The request involved
     */
    public static void removeAllAttributes(String name, HttpServletRequest request) {
        request.removeAttribute(name);
        HttpSession session = request.getSession();
        session.removeAttribute(name);
        session.getServletContext().removeAttribute(name);
//logger.log(Level.INFO, "{0}. HttpRequest.getAttribute {1}: ", new Object[]{logger.getName(), name});
    }
    
    public static Object find(String name, HttpServletRequest request) {
        Object value = request.getParameter(name);
//logger.log(Level.INFO, "{0}. HttpRequest.getParameter {1}: ", new Object[]{logger.getName(), name});
        if(value == null) {
            
            value = request.getAttribute(name);
            
//logger.log(Level.INFO, "{0}. HttpRequest.getAttribute {1}: ", new Object[]{logger.getName(), name});
            if(value == null) {
                value = find(name, request.getSession());
            }
        }
        return value;
    }

    public static Object find(String name, HttpSession session) {
        Object value = session.getAttribute(name);
        if(value == null) {
            value = session.getServletContext().getAttribute(name);
        }
        return value;
    }

    public static String createImagesDir(String tableName)
            throws IOException {
        String outputFolder = getImagesPath(tableName);
        File file = new File(outputFolder);
        if(!file.exists() && !file.mkdirs()) {
            // Not expected
            throw new IOException("Could not create directory: "+file);
        }
        return outputFolder;
    }

    /**
     * This tries to create the dir if it doesn't exist
     */
    public static File createImagesDir(String tableName, String emailAddress) throws IOException{
        String dirPath = getImagesPath(tableName, emailAddress);
        File dir = new File(dirPath);
        if(!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create directory: "+dir);
        }
        return dir;
    }
    
    public static String getImagesPath(String tableName) {
        return WebApp.getInstance().getPath(LocalDirs.IMAGES_FOLDER + "/" + tableName);
    }
    
    public static String getImagesPath(String tableName, String userEmail) {

        return getImagesPath(tableName) + "/" + UUID.nameUUIDFromBytes(userEmail.getBytes());
    }
    
}//END
