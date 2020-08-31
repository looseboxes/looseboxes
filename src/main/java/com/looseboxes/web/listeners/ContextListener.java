package com.looseboxes.web.listeners;

import com.bc.jpa.dao.search.RandomSearchResults;
import com.bc.jpa.dao.search.SearchResults;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.Attributes;
import com.looseboxes.web.WebApp;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.bc.jpa.context.PersistenceUnitContext;
import com.bc.jpa.dao.Select;
import com.looseboxes.cometd.chat.CometdContext;
import com.looseboxes.cometd.chat.CometdContextImpl;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Availability;
import com.looseboxes.pu.entities.Availability_;
import com.looseboxes.web.chat.ChatMessageFormatter;
import com.looseboxes.web.chat.MessageConsumerForDatabase;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * Web application lifecycle listener.
 *
 * @author Josh
 */
public class ContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(ContextListener.class.getName());
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        LOG.info("Initializing servlet context");

        ServletContext context = sce.getServletContext();
        
        LOG.log(Level.INFO, "java.util.logging.config.file: {0}", 
                System.getProperty("java.util.logging.config.file")); 

        this.updateAttributesFromInitParameters(context);
        
        try{
            
            WebApp webApp = WebApp.getInstance();
            
            webApp.init(context);
            
            context.setAttribute(WebApp.ATTRIBUTE_NAME, webApp);
            
            context.setAttribute(Attributes.CURRENT_YEAR, Calendar.getInstance().get(Calendar.YEAR));
            
//            final CometdContext cometdContext = new CometdContextImpl(
//                    context, new GetChatFromMessage(), new PrivateMessageConsumerLocalDiscStore(context) 
//            );
            final CometdContext cometdContext = new CometdContextImpl(
                    context, new ChatMessageFormatter(), new MessageConsumerForDatabase(webApp.getJpaContext()) 
            );
            
            String [] sponsoredItems = WebApp.getInstance().getConfig().getArray(AppProperties.SPONSORED_ITEMS);
            
            if(sponsoredItems != null && sponsoredItems.length > 0) {
            
                Integer [] iarr = new Integer[sponsoredItems.length];
                int i = 0;
                for(String s:sponsoredItems) {
                    iarr[i++] = Integer.valueOf(s);
                }
                
                SearchResults<Product> sponsored = new AdvertSearchResults(iarr, 0, 20);
                
                context.setAttribute(Attributes.ADVERT_SEARCH_RESULTS, sponsored);
            }
            
        }catch(RuntimeException | URISyntaxException | IOException | ClassNotFoundException | SQLException e) {

            final String msg = "This program has to exit due to the following problem:";
            
            LOG.log(Level.SEVERE, msg, e);
            
            throw new RuntimeException(msg);

//            System.exit(1); // The system exited without calling context destroyed

//            this.contextDestroyed(evt);; // The web page was still opened after
            // manually calling this method. Also I am suspicious of passing the
            // Initialized Event object into the contextDestroyed method.
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        if(ctx == null) {
            return;
        }
        Enumeration<String> en = ctx.getAttributeNames();
        if(en == null) {
            return;
        }
        try{
            List<String> attrNames = new ArrayList<>();
            while(en.hasMoreElements()) {
                String attrName = en.nextElement();
                attrNames.add(attrName);
// This threw concurrent modification exception                
//                ctx.removeAttribute(attrName);
            }
            for(String attrName:attrNames) {
                ctx.removeAttribute(attrName);
            }
        }catch(Throwable ignored) { 
            LOG.log(Level.WARNING, "Error destroying Servlet Context", ignored);
        }
        LOG.log(Level.INFO, "Done destroying Servlet Context  {0}", new Date());
    }

    private void updateAttributesFromInitParameters(ServletContext context) {
        Enumeration en = context.getInitParameterNames();
        while(en.hasMoreElements()) {
            String key = (String)en.nextElement();
            Object val = context.getInitParameter(key);
            LOG.fine(() -> "Setting context attribute from init param: "+key+"="+val);
            context.setAttribute(key, val);
        }
    }

    private static class AdvertSearchResults extends RandomSearchResults<Product>{
        private AdvertSearchResults(Integer [] arr, int offset, int limit) {
            super(getResults(arr, offset, limit));           
        }
        private static List<Product> getResults(Object[] arr, int offset, int limit) {

            final PersistenceUnitContext jpaUnit = LbApp.getInstance().getJpaContext();

            final List<Availability> available;
            try(final Select<Availability> sel = jpaUnit.getDaoForSelect(Availability.class)) {
                available = sel.from(Availability.class)
                        .search(References.availability.InStock.name(), Availability_.availability)
                        .search(References.availability.LimitedAvailability.name(), Availability_.availability)
                        .createQuery()
                        .getResultList();
            }
            
            try(final Select<Product> sel = jpaUnit.getDaoForSelect(Product.class)) {
                return sel.from(Product.class)
                        .where(Product_.productid, arr)
                        .and().where(Product_.availabilityid, available)
                        .createQuery()
                        .setFirstResult(offset)
                        .setMaxResults(limit)
                        .getResultList();
            }
        }
        @Override
        public int getPageSize() {
            return 4;
        }
    };
}
