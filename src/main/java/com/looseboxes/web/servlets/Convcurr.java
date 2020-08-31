package com.looseboxes.web.servlets;

/**
 * @(#)Convcurr.java   26-Apr-2015 19:01:55
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebPages;
import java.io.IOException;
import java.util.Collection;
import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.bc.fxrateservice.FxRate;
import com.bc.fxrateservice.FxRateService;
import com.bc.fxrateservice.impl.DefaultFxRateService;
import java.util.logging.Logger;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Convcurr", urlPatterns={"/convcurr"})
public class Convcurr extends BaseServlet {

    private transient static final Logger LOG = Logger.getLogger(Convcurr.class.getName());
    
    private float lastResult;
    private String fromCode;
    private String toCode;
    
    private transient static final FxRateService fxRateSvc = new DefaultFxRateService();
    
    public Convcurr() {}

    @Override
    public String getForwardPage(HttpServletRequest request) {
        return WebPages.CURRENCY_CONVERTER;
    }
    
    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.CURRENCY_CONVERTER;
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response) 
            throws IOException, ServletException {

        this.lastResult = 0;
        
        final Object fromCurr = ServletUtil.find("fromCurrency", request);
        final Object toCurr = ServletUtil.find("toCurrency", request);
        LOG.fine(() -> "From curr: "+fromCurr+", to curr: "+toCurr);        
        
        if(fromCurr == null || fromCurr.toString().isEmpty()) {
            return;
        } 
        if(toCurr == null || toCurr.toString().isEmpty()) {
            return;
        } 
        
        final String FRM_CODE = fromCurr.toString().toUpperCase();
        final String TO_CODE = toCurr.toString().toUpperCase();

        request.setAttribute("currencyConverterFromCode", FRM_CODE);
        request.setAttribute("currencyConverterToCode", TO_CODE);
        request.setAttribute("currencyConverterResult", 
        this.getRate(FRM_CODE, TO_CODE));
    }
    
    public Collection<String> getCurrencyCodes() {
        Collection<String> codes = this.getAppCurrencyCodes();
        Locale [] locales = Locale.getAvailableLocales();
        if(locales == null) return codes;
        for(Locale locale:locales) {
            Currency currency;
            try{
                currency = Currency.getInstance(locale);
            }catch(IllegalArgumentException ignored) { 
                currency = null;
            }
            if(currency != null) {
                codes.add(currency.getCurrencyCode());
            }
        }
        return codes;
    }
    public Collection<String> getAppCurrencyCodes() {
        HashSet<String> codes = new HashSet<>();
        Locale [] locales = {Locale.getDefault(), Locale.US, Locale.UK, Locale.FRANCE};
        for(Locale locale:locales) {
            codes.add(Currency.getInstance(locale).getCurrencyCode());
        }
        return codes;
    }
    public float getRate() {
        this.lastResult = 0;
        return this.getRate(this.getFromCode(), this.getToCode());
    }
    public float getRate(String fromCode, String toCode) {
        
        this.lastResult = 0;
        
        if(fromCode == null || fromCode.isEmpty()) {
            fromCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        }
        if(toCode == null || toCode.isEmpty()) {
            toCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        }

        this.setFromCode(fromCode);
        this.setToCode(toCode);
        if(fromCode.equals(toCode)) {
            this.lastResult = 1.0f;
        }else{
            FxRate fxRate = fxRateSvc.getRate(fromCode, toCode);
            if(fxRate != null && fxRate != FxRate.NONE) {
                this.lastResult = fxRate.getRate();
            }
        }        
        if(LOG.isLoggable(Level.FINER)) {
            LOG.log(Level.FINER, "From {0} to {1}, currency rate: {2}", 
                    new Object[]{fromCode, toCode, this.lastResult});        
        }
        return this.lastResult;
    }

    public float getLastResult() {
        return lastResult;
    }
    
    public String getFromCode() {
        return fromCode;
    }
    public void setFromCode(String fromCode) {
        this.fromCode = fromCode;
    }
    public String getToCode() {
        return toCode;
    }
    public void setToCode(String toCode) {
        this.toCode = toCode;
    }
}
