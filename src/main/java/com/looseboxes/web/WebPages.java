package com.looseboxes.web;

/**
 * @(#)WebPages.java   28-Apr-2015 08:35:39
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
public interface WebPages {
    
///////// site root //////////
//    public static final String APPLY = "/apply.jsp";
    public static final String AUXILLARY_FORM = "/auxform.jsp";
    public static final String CONFIRM_FORM_ENTRIES = "/confirmformentries.jsp";
    public static final String CURRENCY_CONVERTER = "/currencyconverter.jsp";
    public static final String FORM = "/form.jsp";
    public static final String INDEX = "/index.jsp";
//    public static final String PAGE = "/page.jsp";
//    public static final String SEARCH = "/search.jsp";
    public static final String WELCOME = "/welcome.jsp";

/////////// admin /////////////
    public static final String SEND_MASSMAIL = "/admin/sendmassmail.jsp";
    public static final String TRANSFER = "/admin/transfer.jsp";
//    public static final String ADMIN_INDEX = "/admin/index.jsp";
//    public static final String ADMIN_BOT_TRAP = "/admin/page.jsp";
//    public static final String RELOAD_PROPERTIES = "/admin/rp.jsp";
//    public static final String ADMIN_SEND_MAIL = "/admin/sm.jsp";
//    public static final String SUBMIT_QUERY = "/admin/sq.jsp";
//    public static final String UPLOAD_DATA = "/admin/ud.jsp";
//    public static final String UPLOAD_PAGE_DATA = "/admin/upd.jsp";
//    public static final String UPDATE_TIPS = "/admin/ut.jsp";
//    public static final String MISCELLAENOUS = "/admin/misc.jsp";
    
//////////// articles /////////////
//    public static final String ARTICLES_AUTOS_INDEX = "/articles/autos/index.jsp";
//    public static final String ARTICLES_FUELSUBSIDY_INDEX = "/articles/fuelsubsidy/index.jsp";
//    public static final String ARTICLES_GADGETS_INDEX = "/articles/gadgets/index.jsp";
    public static final String ARTICLES_HOWTOUSE_INDEX = "/articles/howto_use/index.jsp";
//    public static final String ARTICLES_JOBS_INDEX = "/articles/jobs/index.jsp";
//    public static final String ARTICLES_PROPERTY_INDEX = "/articles/property/index.jsp";
    public static final String ARTICLES_INDEX = "/articles/index.jsp";

///////// cart ////////
//    public static final String CART_ADDSHPGTOEXISTINGPMT = "/cart/addShippingToExistingPayment.jsp";
    public static final String CART_ADDSHPGTONEWPMT = "/cart/addShippingToNewPayment.jsp";
    public static final String CART_ADDPAYMENT = "/cart/addpayment.jsp";
    public static final String CART_ADDSHIPPING = "/cart/addshipping.jsp";
    public static final String CART_AllORDERS = "/cart/allorders.jsp";
    public static final String CART_CONFIRMPAYMENTDETAILS = "/cart/confirmPaymentDetails.jsp";
    public static final String CART_CONFIRMSHIPPINGDETAILS = "/cart/confirmShippingDetails.jsp";
    public static final String CART_DELIVERYRATES = "/cart/deliveryrates.jsp";
    public static final String CART_INDEX = "/cart/index.jsp";
    public static final String CART_LOGINTOCHECKOUT = "/cart/loginToCheckout.jsp";
    public static final String CART_MYORDERS = "/cart/myOrders.jsp";
    public static final String CART_ORDERS = "/cart/orders.jsp";
    public static final String CART_PAYMENTDETAILS = "/cart/paymentdetails.jsp";
    public static final String CART_PAYMENTOPTIONS = "/cart/paymentoptions.jsp";
    public static final String CART_REDIRECTTOGATEWAY = "/cart/redirectToGateway.jsp";
    public static final String CART_SHIPPINGDETAILS = "/cart/shippingdetails.jsp";
    public static final String CART_VIEWORDER = "/cart/viewOrder.jsp";

//////////// error pages ///////////
//    public static final String ERROR_403 = "/errorpages/403.jsp";
//    public static final String ERROR_404 = "/errorpages/404.jsp";
//    public static final String ERROR_408 = "/errorpages/408.jsp";
//    public static final String ERROR_500 = "/errorpages/500.jsp";
//    public static final String ERROR_503 = "/errorpages/503.jsp";
    public static final String ERROR_DEFAULT = "/errorpages/default.jsp";
//    public static final String ERROR_JSPERRORPAGE = "/errorpages/jsperrorpage.jsp";
//    public static final String ERROR_ACCESSS = "/errorpages/accessviolation.jsp";
//    public static final String ERROR_EXPIREDSESSION = "/errorpages/expiredsession.jsp";
//    public static final String ERROR_MAILAUTH = "/errorpages/mailauthenticationfailed.jsp";
//    public static final String ERROR_UNKNOWNMAILPROVIDER = "/errorpages/unknownmailprovider.jsp";
    
///////////// feeds //////////////
//    public static final String NEWS_FEEDS = ContextDirs.NEWS_FEEDS + "/index.jsp";
//    public static final String TWITTER_TRENDS = "/feeds/twitter/trends.jsp";
    
/////////// help //////////////
    public static final String HELP_FAQS = "/help/FAQs.jsp";
    public static final String HELP_INDEX = "/help/index.jsp";
    public static final String HELP_TIPS_AND_TRICKS = "/help/tips_and_tricks.jsp";
    
///////// info ////////////////    
    public static final String ABOUT = "/info/about.jsp";
    public static final String ABOUT_NUROXLTD = "/info/about_nuroxltd.jsp";
    public static final String INFO_BUYERPROTECTION = "/info/buyerProtection.jsp";
    public static final String CONTACT_NUROXLTD = "/info/contact_nuroxltd.jsp";
    public static final String CONTACT_US = "/info/contact_us.jsp";
    public static final String INFO_INDEX = "/info/index.jsp";
    public static final String SITEMAP = "/info/sitemap.jsp";
    public static final String UPDATE_SHIPPINGDETAILS = "/info/update_shippingdetails.jsp";
    public static final String WHY_US = "/info/why_us.jsp";
    
//////// legal /////////////////
    public static final String EMAIL_DISCLAIMER = "/legal/emailDisclaimer.jsp";
    public static final String LEGAL_INDEX = "/legal/index.jsp";
    public static final String PRIVACY_POLICY = "/legal/privacy_policy.jsp";
    public static final String PROMOS_TERMS = "/legal/promos_terms.jsp";
    public static final String LEGAL_RETURNPOLICY = "/legal/returnpolicy.jsp";
    public static final String LEGAL_SUBMITCOMPLAIN = "/legal/submitcomplain.jsp";
    public static final String USER_AGREEMENT = "/legal/user_agreement.jsp";

//////// mail /////////////////
//    public static final String MAIL_INDEX = "/mail/index.jsp";
    
/////// messaging ////////////
    public static final String MESSAGING_CHAT = "/messaging/chat.jsp";
    public static final String MESSAGING_CUSTOMERSERVICECHAT = "/messaging/customerservicechat.jsp";
    public static final String MESSAGING_RESPONDTOENQUIRY = "/messaging/respondToEnquiry.jsp";

/////////// notices /////////////    
    public static final String NOTICES_BANKPAYMENT = "/notices/bankpayment.jsp";
    public static final String NOTICES_CASHPAYMENT = "/notices/cashpayment.jsp";
    public static final String NOTICES_PAYONDELIVERY = "/notices/payondelivery.jsp";
    public static final String NOTICES_EPAYMENT = "/notices/epayment.jsp";

/////////// products ///////////
    public static final String PRODUCTS = "/products/productdetails.jsp";
    public static final String PRODUCTS_SEARCHRESULTS = "/products/searchresults.jsp";
   
    
//////// texts /////////////////
//    public static final String TEXTS_INDEX = "/texts/index.jsp";
    
//////// user /////////////////
    public static final String COMMENT_FORM = "/user/commentForm.jsp";
    public static final String GUEST_LOGIN = "/user/guestLogin.jsp";
    public static final String USER_INDEX = "/user/index.jsp";
    public static final String JOIN = "/user/join.jsp";
    public static final String LOGIN = "/user/login.jsp";
    public static final String PROMO = "/user/promo.jsp";
    public static final String QUICK_EDIT_PRODUCT = "/user/quickeditproduct.jsp";
    public static final String REQUEST_PASSWORD = "/user/requestPassword.jsp";
    public static final String SEND_MAIL = "/user/sendMail.jsp";
    public static final String UPLOAD_PROFILE_PICTURE = "/user/uploadProfilePicture.jsp";
}//END
