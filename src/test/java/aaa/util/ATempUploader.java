package aaa.util;

import com.bc.imageutil.ImageManager;
import com.bc.imageutil.impl.ImageManagerImpl;
import com.bc.jpa.controller.EntityController;
import com.bc.jpa.fk.EnumReferences;
import com.bc.util.Log;
import com.looseboxes.pu.LbJpaContext;
import com.looseboxes.pu.References;
import com.looseboxes.pu.entities.Address;
import com.looseboxes.pu.entities.Availability;
import com.looseboxes.pu.entities.Brand;
import com.looseboxes.pu.entities.Currency;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Productcategory;
import com.looseboxes.pu.entities.Productstatus;
import com.looseboxes.pu.entities.Productsubcategory;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.pu.entities.Siteuser;
import com.looseboxes.pu.entities.Siteuser_;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import com.bc.jpa.context.JpaContext;
import com.looseboxes.JpaInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Function;

/**
 * @(#)TempUploader.java   26-May-2015 19:48:57
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
public class ATempUploader {
    
    public static final int PRODUCT_NAME_COL = 0;
    public static final int PRICE_COL = 1;
    public static final int COLOR_COL = 2;
    public static final int SIZE_COL = 3;
    public static final int QTY_COL = 4;
    public static final int CAT_COL = 5;
    public static final int PRODUCT_DESC_COL = 6;
    
    // Use this if price is in euro and you want it converted to naira (exchange rate = 260)
    private boolean customFormatPrice = false;
    
    private float exchangeRate = 365.0f;
    
    private float markup = 0.5f;
    
    private boolean sortImages = true;
    
    private boolean confirmEntries = false;
    
    private boolean updateDatabase = true;
    
    private int offset = 0;
    
    /**
     * <p>For ${@link #discountFactor} of 0.7 and discount price of 100:</p>
     * We have discount of 70 and non-discount price of 170.
     */
    private float discountFactor;
    
    private Availability availability;
    
    private Address address;
    
    private Brand brand;
    
    private Currency currency;
    
    private Product previous;
    
    private Productcategory cat;
    
    private Productstatus status;
    
    private Siteuser seller;
    
    private JpaContext jpaContext;
    
    private EnumReferences refs;
    
    private JPanel panel;
    private JLabel descriptionLabel;
    private JLabel imageLabel;
    
    private String qtySeparator;
    
    private String colorSeparator;
    
    private String sizeSeparator;
    
    private String propertySeparator;
    
    private ImageManager imageManager;
    
    private String [] acceptedImageTypes;
    
    public ATempUploader() throws URISyntaxException, IOException {
        acceptedImageTypes = new String[]{"jpg", "jpeg", "png", "gif"};
        panel = new JPanel();
        descriptionLabel = new JLabel();
        descriptionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        descriptionLabel.setDoubleBuffered(true);
        imageLabel = new JLabel();
        panel.setLayout(new BorderLayout());
        panel.add(descriptionLabel, BorderLayout.NORTH);
        panel.add(imageLabel, BorderLayout.CENTER);
// Check File.getName(), Create Buzzwears profile on local        
        discountFactor = 0.5f;
        URI uri = new URI(JpaInfo.CONFIG_LOCATION);
        jpaContext = new LbJpaContext(uri);
        refs = jpaContext.getEnumReferences();
        availability = (Availability)refs.getEntity(References.availability.InStock);
        currency = (Currency)refs.getEntity(References.currency.NGN);
        cat = (Productcategory)refs.getEntity(References.productcategory.Fashion);
        status = (Productstatus)refs.getEntity(References.productstatus.New);
        EntityController<Siteuser, Integer> ec = jpaContext.getEntityController(Siteuser.class, Integer.class);
        seller = ec.selectFirst(Siteuser_.emailAddress.getName(), "buzzwears@yahoo.com");
        if(seller == null) {
            throw new NullPointerException();
        }
        qtySeparator = "\\s";
        colorSeparator = "\\s";
        sizeSeparator = "\\s";
        propertySeparator = "\t{1,}";
        final Function<String, String> getPath = (relativePath) -> 
                Paths.get(relativePath).toAbsolutePath().toString();
        imageManager = new ImageManagerImpl(getPath) {
            // Construct relative path of format: /images/fashion/2015/06/imageName.jpeg
            @Override
            public String buildRelativePath(String filename) {
                String s = Paths.get("images", "fashion", this.getYearMonthPathPrefix(), filename).toString();
                // We are uploading from a swindows machine to a unix server
                s = s.replace('\\', '/');
                if(!s.startsWith("/")) {
                    s = "/" + s;
                }
                return s;
            }
        };
    }

    public static void main(String [] args) {

        try{
            
            Path root = Paths.get(System.getProperty("user.home"), "Documents", "uploads_to_buzzwears", "2017_08_24");
            
            File detailsFile = Paths.get(root.toString(), "data_final.txt").toFile();
            
            File imagesDir = Paths.get(root.toString(), "images_final").toFile();
            
            ATempUploader u = new ATempUploader();
            
            u.parse(detailsFile, imagesDir);

        }catch(URISyntaxException | IOException e) {

            e.printStackTrace();
        }
    }

    public void parse(File prdouctDetailsFile, File imagesDir) throws IOException {

        if(!prdouctDetailsFile.exists()) {
            throw new FileNotFoundException(prdouctDetailsFile.getPath());
        }

        if(!imagesDir.exists()) {
            throw new FileNotFoundException(imagesDir.getPath());
        }
        
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try{
            
            fis = new FileInputStream(prdouctDetailsFile);
            
            isr = new InputStreamReader(fis, "UTF-8");
            
            br = new BufferedReader(isr);
            
            final List list = Arrays.asList(acceptedImageTypes);
//System.out.println(this.getClass().getName()+". Accepted image types: "+list);            
            
            File [] imageFiles = imagesDir.listFiles(new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name) {
                    String ext = com.bc.util.Util.getExtension(name);
//System.out.println(this.getClass().getName()+". Name: "+name+", extension: "+ext);                    
                    boolean accepted = ext == null ? false : list.contains(ext.toLowerCase());
//System.out.println(this.getClass().getName()+". Accepted: "+accepted+", extension: "+ext);                    
                    return accepted;
                }
            });

            // Sort according to name
            if(sortImages) {
                Arrays.sort(imageFiles, new ATempUploader.DigitFileComparator());
StringBuilder builder = new StringBuilder();
for(File file:imageFiles) {
    builder.append(file.getName()).append(',').append(' ');
}
System.out.println(this.getClass().getName()+". Printing sorted images:");
System.out.println(builder.toString());
            }
            
            int index = 0;
            String line;
            while((line = br.readLine()) != null) {

                if(line.trim().isEmpty()) {
                    ++index; continue;
                }
                
                if(line.startsWith("#") || line.startsWith("//")) {
                    // Comments
                    ++index; continue;
                }
                
                if(index< offset) {
                    ++index; continue;
                }
                
                String [] parts = line.split(propertySeparator);
                
                if(parts == null || parts.length < 2) {
                    continue;
                }
                
                BigDecimal price = this.getPriceBeforeDiscount(parts);
                BigDecimal discount = this.getDiscount(parts);
                descriptionLabel.setText(line+", Non-discounted price: "+price+", discounted price: "+(price.subtract(discount)));
                BufferedImage buffimg = ImageIO.read(imageFiles[index]);
                
                Image img = buffimg.getScaledInstance(700, 500, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
                
                if(confirmEntries) {
                    int sel = JOptionPane.showConfirmDialog(
                            null, panel, "Confirm Details match Image", JOptionPane.OK_CANCEL_OPTION);

                    if(sel != JOptionPane.OK_OPTION) {
                        throw new UnsupportedOperationException();
                    }
                }
                
                this.parse(index, parts, imageFiles[index].getName());
                
                ++index;
            }
        }finally{
            if(br != null) try{ br.close(); }catch(IOException e){
                Log.getInstance().log(Level.WARNING, "Error closing stream", this.getClass(), e);
            }
            if(isr != null) try{ isr.close(); }catch(IOException e){
                Log.getInstance().log(Level.WARNING, "Error closing stream", this.getClass(), e);
            }
            if(fis != null) try{ fis.close(); }catch(IOException e){
                Log.getInstance().log(Level.WARNING, "Error closing stream", this.getClass(), e);
            }
        }
    }
    
    private void parse(int index, String [] parts, String imageName) {
// 0=name, 1=price, 2=color(s), 3=size(s), 4-quantity(s), 5=subcategory
        
System.out.println(this.getClass().getName()+". Creating item: "+(index + 1)+" from: "+Arrays.toString(parts));

        String [] qtys = parts[QTY_COL].trim().split(this.qtySeparator);
        
//System.out.println(this.getClass().getName()+". Found "+qtys.length+" qtys: "+Arrays.toString(qtys));
        
        String [] colors;
        String [] sizes;
        if(qtys.length == 1) {
            colors = new String[]{parts[COLOR_COL].trim()};
            sizes = new String[]{parts[SIZE_COL].trim()};
        }else{
            colors = parts[COLOR_COL].trim().split(this.colorSeparator);
            if(colors.length != qtys.length) {
                colors = new String[qtys.length];
                for(int i=0; i<colors.length; i++) {
                    colors[i] = parts[COLOR_COL].trim();
                }
            }
            
            sizes = parts[SIZE_COL].trim().split(this.sizeSeparator);
        }
        
//        if(colors.length != sizes.length || colors.length != qtys.length) {
          if(sizes.length != qtys.length) {
            final String note = "Colors: "+colors.length+", sizes: "+sizes.length+", quantities: "+qtys.length;            
            throw new RuntimeException("Item at position: "+(index + 1)+", has mismatched property lengths. Item: "+Arrays.toString(parts)+"\nProperties: "+note);
        }
        
        Product product = new Product();

        product.setAvailabilityid(availability);
        product.setAvailableAtOrFrom(address);
        product.setBrandid(brand);
        product.setCurrencyid(currency);
        product.setDatecreated(new Date());
        product.setDiscount(this.getDiscount(parts));
        product.setIsRelatedTo(this.getRelated(parts[PRODUCT_NAME_COL].trim()));
        product.setMinimumOrderQuantity(1);
        product.setPrice(this.getPriceBeforeDiscount(parts)); 
        product.setProductName(parts[PRODUCT_NAME_COL].trim());
        // Remember this may be null
        if(parts.length > PRODUCT_DESC_COL) {
            product.setDescription(parts[PRODUCT_DESC_COL]);
        }
        product.setProductcategoryid(cat);
        product.setProductstatusid(status);
        product.setProductsubcategoryid(this.getSubcategory(parts[CAT_COL].trim()));
        product.setSeller(seller);
        product.setViews(1);

//System.out.println("Price: "+product.getPrice().floatValue()+", discount: "+product.getDiscount().floatValue()+", actual price: "+(product.getPrice().subtract(product.getDiscount())));

        List<Productvariant> variants = new ArrayList<>(qtys.length);
        
        for(int i=0; i<qtys.length; i++) {
            
            Productvariant variant = new Productvariant();
            
            variant.setColor(colors[i].trim());
            variant.setDatecreated(new Date());
            // Construct relative path of format: /images/fashion/2015/06/imageName.jpeg
            String imagePath = imageManager.buildRelativePath(imageName);
System.out.println(this.getClass().getName()+". Image path: "+imagePath);            
            variant.setImage1(imagePath);
            variant.setProductSize(sizes[i].trim());
            variant.setProductid(product);
            variant.setQuantityInStock(Integer.parseInt(qtys[i].trim()));
            
            variants.add(variant);
        }

        product.setProductvariantList(variants);
        
        if(!updateDatabase) {
            return;
        }
        
        EntityManager em = jpaContext.getEntityManager(Product.class);
        
        try{
            
            EntityTransaction t = em.getTransaction();
            
            try{
                
                t.begin();
                
                em.persist(product);
                        
                for(Productvariant variant:variants) {
                    em.persist(variant);
                }
                
                t.commit();

System.out.println("SUCCESS: Item: "+(index + 1));
                
            }finally{
                if(t.isActive()) {
                    t.rollback();
                }
            }
        }finally{
            em.close();
        }
    }
    
    private BigDecimal getPriceBeforeDiscount(String [] parts) {
        float f = this.getPriceAfterDiscount(parts);
        int i = Math.round(f + (f * discountFactor));
        BigDecimal bd = new BigDecimal(i);
        return bd;
    }
    
    private BigDecimal getDiscount(String [] parts) {
        float f = this.getPriceAfterDiscount(parts);
        int i = Math.round(f * discountFactor);
        return new BigDecimal(i);
    }
    
    private float getPriceAfterDiscount(String [] parts) {
        
        float targetPrice = Float.parseFloat(parts[PRICE_COL].trim());
        
        if(customFormatPrice) {
            targetPrice = this.formatPrice(targetPrice);
        }else{
            targetPrice = (targetPrice + (targetPrice * this.markup)) * this.exchangeRate;  
        }
        
        return targetPrice;
    }
    
    private Product getRelated(String name)  {
        return previous != null && previous.getProductName().trim().equalsIgnoreCase(name.trim()) ? previous : null;
    }
    
    private Productsubcategory getSubcategory(String part) {
        
        References.productsubcategory subcat = this.getSubcategoryEnum(part);
        
        return (Productsubcategory)refs.getEntity(subcat);
    }
    
    private References.productsubcategory getSubcategoryEnum(String part) {
        References.productsubcategory output;
        part = part.toLowerCase();
        if(part.contains("baby")) {
            if(part.contains("acces")) {
                output = References.productsubcategory.BabysAccessories;
            }else if(part.contains("cloth")) {
                output = References.productsubcategory.BabysClothing;
            }else if(part.contains("shoe")) {
                output = References.productsubcategory.BabysShoes;
            }else{
                throw new RuntimeException("What manner of subcategory is this: "+part);
            }
        }else if(part.contains("kid") || part.contains("boy") || part.contains("girl")) {
            if(part.contains("acces")) {
                output = References.productsubcategory.KidsAccessories;
            }else if(part.contains("cloth")) {
                output = References.productsubcategory.KidsClothing;
            }else if(part.contains("shoe")) {
                output = References.productsubcategory.KidsShoes;
            }else{
                throw new RuntimeException("What manner of subcategory is this: "+part);
            }
// Make sure women comes before men as the letters 'men' is a sub part of 'women'            
        }else if(part.contains("women")) {
            if(part.contains("acces")) {
                output = References.productsubcategory.WomensAccessories;
            }else if(part.contains("cloth")) {
                output = References.productsubcategory.WomensClothing;
            }else if(part.contains("shoe")) {
                output = References.productsubcategory.WomensShoes;
            }else{
                throw new RuntimeException("What manner of subcategory is this: "+part);
            }
        }else if(part.contains("men")) {
            if(part.contains("acces")) {
                output = References.productsubcategory.MensAccessories;
            }else if(part.contains("cloth")) {
                output = References.productsubcategory.MensClothing;
            }else if(part.contains("shoe")) {
                output = References.productsubcategory.MensShoes;
            }else{
                throw new RuntimeException("What manner of subcategory is this: "+part);
            }
        }else{
            throw new RuntimeException("What manner of subcategory is this: "+part);
        }
        return output;
    }
    
    private float formatPrice(float original) {
        return formatPrice_0(original);
    }

    private float formatPrice_2(float original) {
        float output;
        if(original <= 1.0) {
            output = 550;
        }else {
            output = 550 + (original - 1) * 500;
        }
        return output;
    }
    
    private float formatPrice_1(float original) {
        int foundation1 = 650;
        int foundation2 = 800; 
        float output;
        if(original <= 1.3) {
            output = 600;
        }else if(original > 1.3 && original <= 1.5) {
            output = foundation1;
        }else if(original > 1.5 && original <= 2) {
            output = (float)(foundation1 + (original - 1.5) * 300);
        }else{
            output = (float)(foundation2 +(original -2) * 400);
        }
//        output = output - 50;
        if(output < 603) {
            output += 50;
        }
        return output;
    }
    
    private float formatPrice_0(float original) {
        float output;
        if(original <= 1) {
            output = 750;
        }else if(original <= 1.5) {
            output = 1000;
        }else if(original <= 2) {
            output = 1500;
        }else if(original <= 2.5) {
            output = 1750;
        }else if(original <= 3) {
            output = 2100;
        }else if(original <= 3.5) {
            output = 2500;
        }else if(original <= 4) {
            output = 2800;
        }else if(original <= 4.5) {
            output = 3200;
        }else if(original <= 5) {
            output = 3500;
        }else if(original <= 5.5) {
            output = 3900;
        }else if(original <= 6.0) {
            output = 4200;
        }else if(original <= 6.5) {
            output = 4600;
        }else if(original <= 7.0) {
            output = 5000;
        }else if(original <= 7.5) {
            output = 5300; //2900
        }else if(original <= 8) {
            output = 5700; //3100
        }else if(original <= 8.5) {
            output = 6000; // 3300
        }else if(original <= 9) {
            output = 6400; //3500
        }else if(original <= 9.5) {
            output = 6700; //3700
        }else if(original <= 10) {
            output = 7000; //3900
        }else if(original <= 10.5) {
            output = 7400; //3900
        }else if(original <= 11) {
            output = 7750; //3900
        }else{
            throw new IllegalArgumentException("Unexpected price value: "+original);
        }
        return output;
    }

    private static class DigitFileComparator implements Comparator<File> {
        private final Pattern pattern = Pattern.compile("(\\d{1,10})\\p{Punct}");
        @Override
        public int compare(File o1, File o2) {
            int n1 = extractInt(o1.getName(), pattern, 1, 0);
            int n2 = extractInt(o2.getName(), pattern, 1, 0);
            return Integer.compare(n1, n2);
        }
    }
    
    private static class DigitFilenameComparator implements Comparator<String> {
        private final Pattern pattern = Pattern.compile("(\\d{1,10})\\p{Punct}");
        @Override
        public int compare(String o1, String o2) {
            int n1 = extractInt(o1, pattern, 1, 0);
            int n2 = extractInt(o2, pattern, 1, 0);
            return Integer.compare(n1, n2);
        }
    }
    private static int extractInt(String s, Pattern p, int digitGroup, int defaultValue) {
        Matcher m = p.matcher(s);
        if(m.find()) {
            String extract = m.group(digitGroup);
            return Integer.parseInt(extract);
        }else{
            return defaultValue;
        }
    }
}
