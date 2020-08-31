package com.looseboxes.web.form;

import com.bc.web.core.form.HttpImageManager;
import com.looseboxes.web.AppProperties;
import com.looseboxes.web.WebApp;

/**
 * @author Josh
 */
public class DefaultImageManager extends HttpImageManager {
    
    public DefaultImageManager() {
        super(WebApp.getInstance().getConfig().getString(AppProperties.LOCAL_PATH), 
                WebApp.getInstance().getName());
    }

    @Override
    public String getPath(String fname) {
        return WebApp.getInstance().getExternalPath(fname);
    }
}
