package aaa.util;

import com.bc.imageutil.DrawConfig;
import com.bc.imageutil.DrawConfigs;
import com.bc.imageutil.ImageDimensions;
import com.bc.imageutil.ImageManager;
import com.bc.imageutil.impl.ImageManagerImpl;
import com.bc.imageutil.ImageOverlay;
import static com.bc.imageutil.MathUtil.divide;
import com.bc.imageutil.impl.OverlayImageWithText;
import com.bc.util.Util;
import com.looseboxes.core.LbApp;
import com.looseboxes.pu.entities.Product;
import com.looseboxes.pu.entities.Product_;
import com.looseboxes.pu.entities.Productvariant;
import com.looseboxes.web.components.ProductBean;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bc.jpa.context.JpaContext;
import java.net.URISyntaxException;
import com.bc.jpa.dao.Select;
import com.looseboxes.JpaInfo;
import java.awt.Color;


/**
 * @(#)ImageDataManager.java   29-Jun-2015 20:42:20
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
public class ImageDataManager {

    private static final String ID = "ID";
    private static final String PRICE = "Price:";
    private static final String IMG = "Image:";
    private static final String NAME = "Name:";
    
    public static void main(String [] args) {
        try{
            ImageDataManager main = new ImageDataManager();
            File dataFile = Paths.get(System.getProperty("user.home"), "Desktop", "website_misc_collated_15.01.09", "directuploads", "ADVERT ITEMS.txt").toFile();
            Path [] imageDirs = {
//                Paths.get(System.getProperty("user.home"), "Desktop", "website_misc_collated_15.01.09", "directuploads", "1", "images_1"),
//                Paths.get(System.getProperty("user.home"), "Desktop", "website_misc_collated_15.01.09", "directuploads", "1", "images_2"),
                Paths.get(System.getProperty("user.home"), "Desktop", "website_misc_collated_15.01.09", "directuploads", "2", "2015", "05"),
                Paths.get(System.getProperty("user.home"), "Desktop", "website_misc_collated_15.01.09", "directuploads", "3", "images"),
                Paths.get(System.getProperty("user.home"), "Desktop", "website_misc_collated_15.01.09", "directuploads", "4", "images"),
                Paths.get(System.getProperty("user.home"), "Desktop", "website_misc_collated_15.01.09", "directuploads", "5", "images")
            };
            Path tgtPath = Paths.get(System.getProperty("user.home"), "Desktop", "website_misc_collated_15.01.09", "directuploads", "brochure_images");
            main.transferImages(dataFile, imageDirs, tgtPath);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void transferImages(File dataFile, Path [] imageDirs, final Path tgtPath) throws IOException {

        File tgtDir = tgtPath.toFile();
        
        if(!tgtDir.exists()) {
            tgtDir.mkdirs();
        }
        
        if(!tgtDir.isDirectory()) {
            throw new UnsupportedOperationException();
        }
        
        Map<String, ProductData> data = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)))) {
            String line;
            while((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) {
                    continue;
                }
                ProductData productData = getProductDataFromTextLine(line);
                data.put(productData.getImageName(), productData);
            }
        }

log("Extracted "+data.size()+" lines of data");
        
        if(data.isEmpty()) {
            return;
        }
        
                // @TODO make these properties
        final Font font = Font.decode(Font.MONOSPACED+"-PLAIN-24");
        final Color color = Color.DARK_GRAY;
        float fromBottom = 0f;
        float fromRight = 0.05f; 
        
        final DrawConfig drawConfig = DrawConfigs.fromBottomRight(fromBottom, fromRight, font, color);

        final ImageOverlay imageOverlay = new OverlayImageWithText();
        
        final ImageManager imgMgr = new ImageManagerImpl(
                (sibling) -> tgtPath.resolve(sibling).toString(), imageOverlay){
        };
        
        for(Path imageDir:imageDirs) {
            
log("Checking for images in: "+imageDir);

            File file = imageDir.toFile();
            
            if(!file.isDirectory()) {
log("Not a dir: "+imageDir);
                continue;
            }
            
            File [] imageFiles = file.listFiles();
            
log("Found "+(imageFiles==null?null:imageFiles.length)+" files in dir: "+imageDir);

            for(File imageFile:imageFiles) {
                
                String imgFilename = imageFile.getName();
                
                if(!data.containsKey(imgFilename)) {
                    continue;
                }

log("Accepted image: "+imgFilename+" of dir: "+imageDir);
                
                FileInputStream fis = new FileInputStream(imageFile);
                
                ProductData productData = data.get(imgFilename);

                BufferedImage buffImage = imgMgr.read(fis, Util.getExtension(imgFilename));

                BigDecimal price = productData.getPrice();
                String priceStr = "N" + price.setScale(0);

log("Adding price: "+priceStr+" to image: "+imgFilename);

//                Font font = imgMgr.getSuggestedFont(buffImage, priceStr);
                
//                imgMgr.drawString(buffImage, priceStr, font, Color.RED);
                
                Dimension suggested = ImageDimensions.getSuggestedDimension(buffImage, ImageDimensions.medium());

                String saveTo = Paths.get(tgtPath.toString(), imgFilename).toString();

                boolean saved = imgMgr.saveImage(buffImage, suggested.width, suggested.height, saveTo);
                
log("Image saved: "+saved+", to file: "+saveTo);                
            }
        }
    }    
    
    private ProductData getProductDataFromTextLine(String line) {
        final int id = Integer.parseInt(getData(line, ID));
        final BigDecimal price = BigDecimal.valueOf(Double.parseDouble(getData(line, PRICE)));
        final String img = getData(line, IMG);
        final String name = getData(line, NAME);
        return new ProductData() {
            @Override
            public int getId() { return id; }
            @Override
            public BigDecimal getPrice() { return price; }
            @Override
            public String getImageName() { return img; }
            @Override
            public String getName() { return name; }
        };
    }

    private String getData(String line, String key) {
        int start = line.indexOf(key) + key.length() + 1;
        int end = line.indexOf(',', start);
        if(end == -1) {
            end = line.length();
        }
        return line.substring(start, end).trim();
    }
    
    private static interface ProductData{
        int getId();
        BigDecimal getPrice();
        String getImageName();
        String getName();
    }

    private void createDataFile() throws URISyntaxException, IOException {
        createDataFileFromProductIds(this.getProductids());
    }
    
    private void createDataFileFromProductIds(Integer [] productids) throws URISyntaxException, IOException {
        
        LbApp.getInstance().init(JpaInfo.CONFIG_LOCATION);
        
        JpaContext jpaContext = LbApp.getInstance().getJpaContext();
        
        int offset = 0;
        int limit = 20;
        
        ProductBean bean = new ProductBean();
        
        while(true) {

            int end = offset + limit;
            
            if(end > productids.length) {
                end = productids.length;
            }
            
            int len = end - offset;
            
log("Next batch is from "+offset+" to "+end);            
        
            Object [] batchids = new Object[len];

            System.arraycopy(productids, offset, batchids, 0, batchids.length);
            
log("Batch ids: "+Arrays.toString(batchids));            
            
            List<Product> products;
            
            try(Select<Product> qb = jpaContext.getDaoForSelect(Product.class)) {
        
                products = qb.from(Product.class)
                .where(Product_.productid.getName(), batchids) 
                .createQuery().setMaxResults(limit).getResultList();
            }

log("Found: "+(products==null?null:products.size())+" products for batch");            
            
            if(products == null) {
                break;
            }
            
            printProducts(bean, products);
            
            if(products.size() < limit) {
                break;
            }
            
            offset += products.size();
        }
    }
    
    private Integer [] getProductids() {
        return new Integer[]{
            216,299,28,297,296,292,293,291,289,288,287,285,215,188,265,
            249,350,349,343,342,338,330,264,329,268,222,209,245,232,280,
            231,151,136,161,321,167,307,242,70,369,366,367,365,341,340,
            337,200,213,261,259,266,251,364,363,362,361,347,262,250,224,
            223,150,149,197,109,201,191,353,230,370,253,252,320,169,153,
            178,111,103,248,279,277
        };        
    }
    
    private void printProducts(ProductBean bean, List<Product> products) {
        
        StringBuilder builder = new StringBuilder();
        
        for(Product product:products) {
            
            List<Productvariant> variants = product.getProductvariantList();
            
            if(variants == null || variants.isEmpty()) {
                continue;
            }
            
            String imagePath = null;
            
            for(Productvariant variant:variants) {
                
                bean.setSelectedVariant(variant);
                
                if((imagePath = bean.getImagePath()) != null) {
                    break;
                }
            }
            
            BigDecimal discountPrice = product.getDiscount() == null ?
                    product.getPrice() : product.getPrice().subtract(product.getDiscount());
            
            builder.append("ID ").append(product.getProductid()).append(',').append('\t');
            builder.append("Price: ").append(discountPrice).append(',').append('\t');
            builder.append("Image: ").append(Paths.get(imagePath).getFileName().toString()).append(',').append('\t');
            builder.append("Name: ").append(product.getProductName()).append('\n');
        }
System.out.println(builder);        
    }
    
    private void log(Object msg) {
System.out.println(this.getClass().getName()+". "+msg);        
    }
}
