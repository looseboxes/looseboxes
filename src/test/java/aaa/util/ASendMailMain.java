package aaa.util;

import aaa.util.SendShoppingCartReminder.Orderdetails;
import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.bc.util.Log;
import com.looseboxes.TestWebApp;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Currency;
import com.looseboxes.pu.entities.Orderproduct;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productorder;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.html.EntityHtmlGen;
import com.looseboxes.web.html.ShoppingCartHtmlGen;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import com.bc.jpa.context.JpaContext;


/**
 * @(#)ASendMailMain.java   02-Jul-2015 10:34:18
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
public class ASendMailMain {

    public static void main(String [] args) {
        
        try{
            
            if(!TestWebApp.isInitialized()) {
                TestWebApp.init();
            }
            
            AbstractSendMail x = getSendMail(71);
    
//            x.sendMail("btoun08@yahoo.com", null);
            x.sendMail("coolbuyng@gmail.com", null);
            x.sendMail("nelotache@yahoo.com", null);
            x.sendMail("nelotache@gmail.com", null);
            x.sendMail("posh.bc@gmail.com", null);

if(true) {
    return;
}            
            SendShoppingCartTipsMail sendMail = new SendShoppingCartTipsMail();
            String emailsStr = "chemordy@gmail.com,flukzy2003@yahoo.com,joyoggo02@yahoo.com,Osyvina@yahoo.com,okonitepurelove@yahoo.com,talktobims@gmail.com,sjbriggs52@gmail.com,ebelebridget@yahoo.com,amahekarika@Gmail.com,btoun08@yahoo.com,nelotache@gmail.com,posh.bc@gmail.com";
            emailsStr = "goosyg2003@yahoo.com,Godiyaz@yahoo.com,rhemmy2803@yahoo.co.uk,Hobsadiq@yahoo.com,lamideey@yahoo.com,switmaro4life@yahoo.com,kaffynwales@gmail.com,zajems@yahoo.com,agnes.isesele@yahoo.com,olusolapeolanrewaju@gmail.com,zeemaikano1@gmail.com,coolbuyng@gmail.com,posh.bc@gmail.com";
            String [] emails = emailsStr.split(",");
            for(String email:emails) {
                if(email == null || email.isEmpty()) {
                    continue;
                }
                sendMail.sendMail(email, null);
            }
if(true) {
    return;
}            
            
            SendInvoiceAsEmail sx0 = new SendInvoiceAsEmail();
            
//            sx0.sendMail("posh.bc@gmail.com,coolbuyng@gmail.com", null);
            sx0.sendMail("demoteddy@yahoo.com,posh.bc@gmail.com,coolbuyng@gmail.com", null);

if(true) {
    return;
}            
            
            SendInvoiceAsEmail sx = new SendInvoiceAsEmail() {
                @Override
                public String getProductIds() {
                    return null;
                }
                @Override
                public String getMessage(String email, String password) {

                    EntityHtmlGen<Orderproduct> htmlGen = new ShoppingCartHtmlGen();
                    htmlGen.setExternalOutput(true);
                    htmlGen.setMobile(true);
                    htmlGen.setUseCID(false);
                    
                    StringBuilder message = new StringBuilder();
//                    message.setSubject("Sample Subject of Email to Self");
                    message.append("<p>Here are the details of your order no:<b>00256</b> on <a href=\"http://www.buzzwears.com\">BuzzWears.com</a>.</p>");
                    
                    List<Orderproduct> orderproducts = getOrderItems();

                    htmlGen.appendItems(orderproducts, message);
                    
                    return message.toString();
                }
            };
            
if(true) {
    sx.sendMail("demoteddy@yahoo.com", null);
    sx.sendMail("posh.bc@gmail.com", null);
    return;
}            
            
            AbstractSendMail sm;

            sm = getSendMail(44);
            
//            sm.sendMail("chiangel4real2k@yahoo.com", null);
//            sm.sendMail("buzzwears@yahoo.com", null);
//            sm.sendMail("coolbuyng@gmail.com", null);
            sm.sendMail("posh.bc@gmail.com", null);
            
if(true) {
    return;
}            
            
            sm = getSendMail(32);
            sm.sendMail("tayo_adekunle@yahoo.com", null);
            sm.sendMail("buzzwears@yahoo.com", null);
            sm.sendMail("coolbuyng@gmail.com", null);
            sm.sendMail("posh.bc@gmail.com", null);

            Orderdetails od_0 = SendShoppingCartReminder.getOrderdetails(152, 197, "Charmykitty girl's tshirt", 1298, 1);
            Orderdetails od_1 = SendShoppingCartReminder.getOrderdetails(301, 460, "Bambini girl's top", 1098, 1);
            sm = new SendShoppingCartReminder(od_0, od_1);
            sm.sendMail("elfsalawu@yahoo.com", null);
            sm.sendMail("buzzwears@yahoo.com", null);
            sm.sendMail("coolbuyng@gmail.com", null);
            sm.sendMail("posh.bc@gmail.com", null);
            
            od_0 = SendShoppingCartReminder.getOrderdetails(287, 419, "Lupilu girl's tshirt", 648, 1);
            od_1 = SendShoppingCartReminder.getOrderdetails(18, 29, "Rebel girl's top", 699, 1);
            sm = new SendShoppingCartReminder(od_0, od_1);
            sm.sendMail("bowaleosisami@yahoo.com", null);
            sm.sendMail("buzzwears@yahoo.com", null);
            sm.sendMail("coolbuyng@gmail.com", null);
            sm.sendMail("posh.bc@gmail.com", null);
            
              od_0 = SendShoppingCartReminder.getOrderdetails(315, 485, "Disney baby girl's top", 1098, 2);
              od_1 = SendShoppingCartReminder.getOrderdetails(279, 398, "Young dimensions girl's shoe", 1598, 1);
            sm = new SendShoppingCartReminder(od_0, od_1);
            sm.sendMail("adebukolaomolara@yahoo.com", null);
            sm.sendMail("buzzwears@yahoo.com", null);
            sm.sendMail("coolbuyng@gmail.com", null);
            sm.sendMail("posh.bc@gmail.com", null);
            
//            SendOrderNoticeEmail sendMail = new SendOrderNoticeEmail();
//            sendMail.sendMail("posh.bc@gmail.com", null);
//            sendMail.sendMail("coolbuyng@gmail.com", null);
//            sendMail.sendMail("buzzwears@yahoo.com", null);
//            sendMail.sendMail("bummyte4real@yahoo.com", null);
            
//            SendShoppingCartTipsMail sendMail = new SendShoppingCartTipsMail();
//            sendMail.sendMail("posh.bc@gmail.com", null);
//            sendMail.sendMail("bummyte4real@yahoo.com", null);
            
//            SendOrderAssistanceMail sendMail = new SendOrderAssistanceMail();
//            sendMail.sendMail("posh.bc@gmail.com", null);
//            sendMail.sendMail("bummyte4real@yahoo.com", null);
            
        }catch(Exception e) {
            Log.getInstance().log(Level.WARNING, "Error sending mail", SendMail.class, e);
        }finally{
            try{
                TestWebApp.destroy();
            }catch(Exception e) {
                Log.getInstance().log(Level.WARNING, "Error destroying "+TestWebApp.class.getName(), SendMail.class, e);
            }finally{
                System.exit(0);                
            }
        }
    }
    
    public static List<Orderproduct> getOrderItems() {
        Map<Integer, Integer> details = new TreeMap<>();
//        details.put(71, 1); details.put(230, 2); details.put(19, 1); details.put(61, 1); 
//        details.put(293, 1); details.put(52, 1); details.put(282, 1);
         
        details.put(325, 1); details.put(366, 1); details.put(367, 1);
        details.put(213, 1); details.put(198, 1); details.put(260, 1); details.put(244, 1);
        details.put(170, 1); details.put(364, 1); details.put(261, 1); details.put(363, 1);
        details.put(256, 1); details.put(205, 1); details.put(201, 1); details.put(219, 1);
        details.put(163, 1); details.put(274, 1); details.put(164, 1);
        details.put(348, 2); details.put(254, 1); details.put(332, 1); details.put(253, 1);
        details.put(288, 1); 
        details.put(320, 1); details.put(152, 1); details.put(336, 1); details.put(69, 1);
        details.put(72, 1); details.put(285, 1); details.put(283, 1);
        details.put(294, 1); details.put(290, 1); details.put(284, 1); 
        details.put(207, 2); details.put(220, 1); details.put(105, 2);
        details.put(321, 1); details.put(344, 1); details.put(264, 1);
        details.put(345, 1); details.put(211, 1); details.put(307, 1); details.put(353, 1);
        JpaContext cf = WebApp.getInstance().getJpaContext();
        EnumReferences refs = cf.getEnumReferences();
        Currency c = (Currency)refs.getEntity(References.currency.NGN);
        Date d = new Date();
        Productorder o = new Productorder();
        o.setDatecreated(d);
        o.setOrderDate(d);
        o.setProductorderid(256);
        EntityController<Product, Integer> ec = cf.getEntityController(Product.class, Integer.class);
        List<Orderproduct> output = new ArrayList<>(details.size());
        for(Integer pid:details.keySet()) {
            Integer qty = details.get(pid);
            Orderproduct op = getOrderItem(ec, pid, o, qty, d, c);
            output.add(op);
        }
        return output;        
    }
    
    public static Orderproduct getOrderItem(
            EntityController<Product, Integer> ec, Integer productId, 
            Productorder order, int qty, Date date, Currency currency) {
        Product p = ec.find(productId);
        Orderproduct op = new Orderproduct();
        op.setCurrencyid(currency);
        op.setDatecreated(date);
        op.setDiscount(p.getDiscount());
        op.setOrderproductid(productId); ///
        op.setPrice(p.getPrice());
        op.setProductorderid(order);
        op.setProductvariantid(p.getProductvariantList().get(0));
        op.setQuantity(qty);
        op.setTimemodified(date);
        return op;
    }
    
    public static AbstractSendMail getSendMail(final int orderid) {
        AbstractSendMail sm = new AbstractSendMail(){
            @Override
            public String getSubject(String email, String password) {
                return "BuzzWears careline - Help us assist you";
            }
            @Override
            public String getProductIds() {
                return null;
            }
            @Override
            public String getMessage(String email, String password) {
                return getMessageForOrderNo(orderid);
            }
        };
        return sm;
    }
    
    public static String getMessageForOrderNo(int orderid) {
        String msg;
        switch(orderid) {
            case 32:
                msg = getMessageForOrderNo32(); break;
            case 44:
//                msg = getMessageForOrderNo44(); break;
                msg = getMessage(orderid).toString(); break;
            default:
                msg = getMessage(orderid).toString();
        }
        return msg;
    }
    
    public static StringBuilder getMessage(Integer orderId) {
        
        Productorder productorder = getOrder(orderId);

        List<Orderproduct> orderpoducts = productorder.getOrderproductList();
        
Log.getInstance().log(Level.INFO, "Orderproducts: {0}", ASendMailMain.class, orderpoducts);

        EntityHtmlGen<Orderproduct> htmlGen = new ShoppingCartHtmlGen();
        htmlGen.setExternalOutput(true);
        htmlGen.setMobile(true);

String msg = "<p>You recently added items to your shopping cart on <a href=\"http://www.buzzwears.com\">BuzzWears.com</a>.</p>\n" +
"<p>If you encounter any problem, please let us know.</p>\n" +
"<p style=\"background-color:#EEDDEE; width:100%;\">\n" +
"  Email: <tt><a href=\"mailto:support@buzzwears.com\">support@buzzwears.com</a></tt><br/>\n" +
//"  BB Pin: <tt>55F156B8</tt><br/>\n" +
"  Call Helen: <tt>08092925682</tt>\n" +
"</p>" +
"<p>Your order <b>2015/12/00"+orderId+"</b> is detailed below:</p>";
        
        StringBuilder message = new StringBuilder();
        
        message.append(msg);
        
        htmlGen.appendItems(orderpoducts, message);
        
        message.append("<p>Thanks.<br/><i>BuzzWears</i></p>");        
        
        return message;
    }

    public static Productorder getOrder(Integer orderId) {
        
        JpaContext cf = LbApp.getInstance().getJpaContext();

        EntityController<Productorder, Integer> ec = cf.getEntityController(Productorder.class, Integer.class);

        Productorder productorder = ec.find(orderId);
        
        return productorder;
    }    
    
    public static String getMessageForOrderNo44() {
String msg = 
"        <h3>Shopping Cart - Order no: 44</h3>  \n" +
"        <table id=\"cartitems\" class=\"myBorder curved\">\n" +
"<thead class=\"spaced tone0\">\n" +
"    <tr style=\"height:2em;\">\n" +
"      <th>ID</th>\n" +
"          <th>Image</th>\n" +
"          <th>Description</th>     \n" +
"          <th>Unit Price</th>    \n" +
"          <th>Qty</th>\n" +
"          <th>Amount&nbsp;<span class=\"mySmaller\">(NGN)</span></th>\n" +
"          <th>\n" +
"            </th>  \n" +
"        </tr>      \n" +
"  </thead>  \n" +
"<tbody>\n" +
"    <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">\n" +
"                <b>ID</b>:70<br/><b>SKU</b>:66</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75\" height=\"75\" id=\"image70\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2884ef36-f8d0-33e8-a9a7-32821682b6fe/1418749744212_1.jpg\" \n" +
"         alt=\"Ergee baby boy shorts\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                <a href=\"http://www.buzzwears.com/products/70.jsp\">Ergee baby boy shorts</a></td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">\n" +
"                <b>ID</b>:26<br/><b>SKU</b>:30</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75\" height=\"75\" id=\"image26\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2884ef36-f8d0-33e8-a9a7-32821682b6fe/1418749734822_1.jpg\" \n" +
"         alt=\"Kik baby boys trousers\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                <a href=\"http://www.buzzwears.com/products/26.jsp\">Kik baby boys trousers</a></td>\n" +
"              <td class=\"centeredContent\">\n" +
"                899</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                899</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">\n" +
"                <b>ID</b>:185<br/><b>SKU</b>:256</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75\" height=\"75\" id=\"image185\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6018.JPG\" \n" +
"         alt=\"Okay boys baby body\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                <a href=\"http://www.buzzwears.com/products/185.jsp\">Okay boys baby body</a></td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">\n" +
"                <b>ID</b>:253<br/><b>SKU</b>:360</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75\" height=\"75\" id=\"image253\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/39_lupilu_2_baby_bodys_2.jpg\" \n" +
"         alt=\"Lupilu 2 in 1 babybody\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                <a href=\"http://www.buzzwears.com/products/253.jsp\">Lupilu 2 in 1 babybody</a></td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,098</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,098</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">\n" +
"                <b>ID</b>:164<br/><b>SKU</b>:216</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75\" height=\"75\" id=\"image164\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/05/IMG_5855.JPG\" \n" +
"         alt=\"M&m baby set\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                <a href=\"http://www.buzzwears.com/products/164.jsp\">M&M baby set</a></td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,298</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,298</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">\n" +
"                <b>ID</b>:239<br/><b>SKU</b>:340</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75\" height=\"75\" id=\"image239\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/25_5o.png\" \n" +
"         alt=\"Ergee baby boy shirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                <a href=\"http://www.buzzwears.com/products/239.jsp\">Ergee baby boy shirt</a></td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,798</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,798</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">\n" +
"                <b>ID</b>:313<br/><b>SKU</b>:482</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75\" height=\"75\" id=\"image313\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6066.JPG\" \n" +
"         alt=\"C & A baby club polo tshirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                <a href=\"http://www.buzzwears.com/products/313.jsp\">C & A baby club polo tshirt</a></td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,698</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,698</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">\n" +
"                <b>ID</b>:103<br/><b>SKU</b>:103</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75\" height=\"75\" id=\"image103\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/05/IMG_5789.JPG\" \n" +
"         alt=\"Ernstings family baby boy polo shirts\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                <a href=\"http://www.buzzwears.com/products/103.jsp\">Ernstings family baby boy polo shirts</a></td>\n" +
"              <td class=\"centeredContent\">\n" +
"                2,298</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                2,298</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\">\n" +
"      <td colspan=\"1\"><b>Sub Total</b></td>\n" +
"      <td colspan=\"4\"></td>\n" +
"      <td colspan=\"2\" class=\"centeredContent\">\n" +
"        10,486</td>\n" +
"    </tr>  \n" +
"    <tr style=\"height:2em;\" class=\"mySmaller\">\n" +
"      <td colspan=\"1\">Delivery</td>\n" +
"      <td colspan=\"4\">(0.6664 kg)</td>\n" +
"      <td colspan=\"2\" class=\"centeredContent\">\n" +
"        1,177.5</td>\n" +
"    </tr>  \n" +
"    <tr style=\"height:2em; font-size:0.75em;\" class=\"mySmaller\">\n" +
"      <td colspan=\"1\">\n" +
"        <a href=\"http://www.buzzwears.com/cart/deliveryrates.jsp\">View delivery rates</a>\n" +
"      </td>\n" +
"      <td colspan=\"4\"></td>\n" +
"      <td colspan=\"2\" class=\"centeredContent\"></td>\n" +
"    </tr>  \n" +
"    <tr style=\"height:2em; background-color:#4BBC99; color:white;\">\n" +
"      <td colspan=\"1\"><b>Total</b></td>\n" +
"      <td colspan=\"4\"></td>\n" +
"      <td colspan=\"2\" class=\"centeredContent\">\n" +
"        <b>NGN11,663.50</b>  \n" +
"      </td>\n" +
"    </tr>  \n" +
"    </tbody>\n" +
"  </table>\n";        
return msg;
    }
    
    public static String getMessageForOrderNo32() {
String msg = 
"        <h3>Shopping Cart - Order no: 32</h3>  \n" +
"        \n" +
"        <table id=\"cartitems\" style=\"background-color:#EEEEEE;\">\n" +
"    \n" +
"<thead class=\"spaced tone0\">\n" +
"    <tr style=\"height:2em;\">\n" +
"      <th>ID</th><th>Image</th><th>Description</th>     \n" +
"          <th>Unit Price</th>    \n" +
"          <th>Qty</th>\n" +
"          <th>Amount&nbsp;<span class=\"mySmaller\">(NGN)</span></th>\n" +
"          <th>\n" +
"            </th>  \n" +
"        </tr>      \n" +
"  </thead>  \n" +
"  \n" +
"<tbody>\n" +
"      \n" +
"    <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">298</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image298\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_5973.JPG\" \n" +
"         alt=\"lupilu boys T. shirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                lupilu boys T. shirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">200</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image200\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6033.JPG\" \n" +
"         alt=\"Primark boys top\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Primark boys top</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">296</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image296\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_5971.JPG\" \n" +
"         alt=\"lupilu boys T. shirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                lupilu boys T. shirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">325</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image325\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6079.JPG\" \n" +
"         alt=\"T.C.M baby boy tshirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                T.C.M baby boy tshirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">294</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image294\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_5969.JPG\" \n" +
"         alt=\"lupilu boys T. shirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                lupilu boys T. shirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">324</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image324\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6078.JPG\" \n" +
"         alt=\"T.C.M baby boy tshirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                T.C.M baby boy tshirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                648</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">188</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image188\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6021.JPG\" \n" +
"         alt=\"Disney Minnie Mouse knickers\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Disney Minnie Mouse knickers</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">235</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image235\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/21_4e2.jpg\" \n" +
"         alt=\"Stikeez boys shirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Stikeez boys shirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                798</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                798</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">326</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image326\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6080.JPG\" \n" +
"         alt=\"T.C.M baby boy polo\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                T.C.M baby boy polo</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                798</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                798</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">15</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image15\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2884ef36-f8d0-33e8-a9a7-32821682b6fe/1418164238873_1.jpg\" \n" +
"         alt=\"Kik baby unisex knickers\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Kik baby unisex knickers</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">81</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image81\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2884ef36-f8d0-33e8-a9a7-32821682b6fe/1418749445677_1.jpg\" \n" +
"         alt=\"Kik baby tshirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Kik baby tshirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">70</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image70\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2884ef36-f8d0-33e8-a9a7-32821682b6fe/1418749744212_1.jpg\" \n" +
"         alt=\"Ergee baby boy shorts\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Ergee baby boy shorts</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">71</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image71\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2884ef36-f8d0-33e8-a9a7-32821682b6fe/1418162445932_1.jpg\" \n" +
"         alt=\"Early days boys tshirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Early days boys tshirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                699</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">186</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image186\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6019.JPG\" \n" +
"         alt=\"Okay boys baby body\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Okay boys baby body</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">185</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image185\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6018.JPG\" \n" +
"         alt=\"Okay boys baby body\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Okay boys baby body</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                698</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">261</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image261\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/47_lupilu_3_baby_langarmshirts_13.jpg\" \n" +
"         alt=\"Lupilu unisex baby top\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Lupilu unisex baby top</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                798</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                798</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">352</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image352\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6109.JPG\" \n" +
"         alt=\"Young dimensions girls dress\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Young dimensions girls dress</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,098</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,098</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">253</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image253\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/39_lupilu_2_baby_bodys_2.jpg\" \n" +
"         alt=\"Lupilu 2 in 1 babybody\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Lupilu 2 in 1 babybody</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,098</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,098</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">348</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image348\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/IMG_6105.JPG\" \n" +
"         alt=\"Primark sleeveless 3pc baby body\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Primark sleeveless 3pc baby body</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,298</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,298</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">239</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image239\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/25_5o.png\" \n" +
"         alt=\"Ergee baby boy shirt\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Ergee baby boy shirt</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,798</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                1,798</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">256</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image256\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/42_lupilu_3_baby_langarmshirts_7.jpg\" \n" +
"         alt=\"Lupilu baby girl top\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Lupilu baby girl top</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                798</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                798</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\" class=\"spaced tone0\">\n" +
"        <td class=\"centeredContent\">240</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                <img width=\"75px\" height=\"75px\"  id=\"image240\" itemprop=\"image\" class=\"icon borderless\" src=\"http://www.buzzwears.com/local/images/fashion/2015/06/26_6e.png\" \n" +
"         alt=\"Ergee baby girl set\" title=\"\"/>  \n" +
"  </td>\n" +
"              <td class=\"centeredContent mySmaller\">\n" +
"                Ergee baby girl set</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                2,248</td>\n" +
"              <td class=\"centeredContent\">1</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                2,248</td>\n" +
"              <td class=\"centeredContent\">\n" +
"                </td>\n" +
"            </tr>     \n" +
"      <tr style=\"height:2em;\">\n" +
"      <td colspan=\"1\"><b>Sub Total</b></td>\n" +
"      <td colspan=\"4\"></td>\n" +
"      <td colspan=\"2\" class=\"centeredContent\">\n" +
"        19,560</td>\n" +
"    </tr>  \n" +
"    <tr style=\"height:2em;\" class=\"mySmaller\">\n" +
"      <td colspan=\"1\">Delivery</td>\n" +
"      <td colspan=\"4\">(1.8326 kg)</td>\n" +
"      <td colspan=\"2\" class=\"centeredContent\">\n" +
"        2,157.89</td>\n" +
"    </tr>  \n" +
"    <tr style=\"height:2em; font-size:0.75em;\" class=\"mySmaller\">\n" +
"      <td colspan=\"1\">\n" +
"        <a href=\"http://www.buzzwears.com/cart/deliveryrates.jsp\">View delivery rates</a>\n" +
"      </td>\n" +
"      <td colspan=\"4\"></td>\n" +
"      <td colspan=\"2\" class=\"centeredContent\"></td>\n" +
"    </tr>  \n" +
"    <tr style=\"height:2em; background-color:#4BBC99; color:white;\">\n" +
"      <td colspan=\"1\"><b>Total</b></td>\n" +
"      <td colspan=\"4\"></td>\n" +
"      <td colspan=\"2\" class=\"centeredContent\">\n" +
"        <b>NGN21,717.89</b>  \n" +
"      </td>\n" +
"    </tr>  \n" +
"    </tbody>\n" +
"  </table>\n" +
"";
return msg;
    }
}
