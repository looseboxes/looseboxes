package com.looseboxes.web.servlets;

/**
 * @(#)Viewimg.java   30-Apr-2015 10:58:12
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */

import com.bc.util.Log;
import com.looseboxes.core.Util;
import com.bc.imageutil.ImageDimensions;
import com.bc.imageutil.impl.ImageLoaderImpl;
import com.bc.imageutil.impl.ImageManagerImpl;
import com.looseboxes.pu.entities.Productvariant_;
import com.looseboxes.web.ServletUtil;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.WebPages;
import com.looseboxes.web.components.ProductBean;
import com.looseboxes.web.components.UserBean;
import com.looseboxes.web.form.LbImageManager;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Viewimg", urlPatterns={"/viewimg", "/local/images"})
public class Viewimg extends BaseServlet {
    
    private BufferedImage targetImage;
   
    protected String getParameterName() {
        return "DisplayImage_BufferedImage";
    }

    @Override
    public String getForwardPage(HttpServletRequest request) {
        throw new UnsupportedOperationException("Not supported."); 
    }

    @Override
    public String getErrorPage(HttpServletRequest request, Object message) {
        return WebPages.PRODUCTS;
    }
    
    @Override
    protected void handleRequest(
            HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        targetImage = this.getImage(request);
    }

    @Override
    public void destroy(HttpServletRequest request) throws ServletException {
        try{
            super.destroy(request);
        }finally{
            this.targetImage = null;
        }
    }

    public BufferedImage getImage(HttpServletRequest request) 
            throws ServletException, IOException {
//@related_2 - Query produced at DisplayProduct.class.
// Expected format: {siteroot}?productTable={tableName}&productId={productId}&image={imageName}
        
        String imageName = (String)ServletUtil.find("image", request);
        String imagePath;
        if (imageName == null || imageName.trim().isEmpty()){
            imagePath = (String)ServletUtil.find("imageSource", request);
        }else {
            imagePath = getImagePath(imageName, request);
        }    
        
        if(imagePath == null) {
            
            String path = request.getServletPath();
            
            if(path.startsWith("/local/images/")) {
                
                // Since we use getExternalPath later, we have to remove the local context here
                //
                imagePath = path.substring(path.indexOf("/images/"));
            }
        }

        if (imagePath == null) {
            throw new ServletException("Image not found: "+imageName);
        }    

        //@related_images
        
        boolean isRemote = Util.isHttpUrl(imagePath);
        
        File imageFile = null;
        URL imageURL;
        BufferedImage output;
        try{
            if(isRemote) {
                imageURL = new URL(imagePath);
Log.getInstance().log(Level.FINE, "Image path: {0}, Image URL: {1}", this.getClass(), imagePath, imageURL);
                output = javax.imageio.ImageIO.read(imageURL);
            }else{
                imageFile = new File(WebApp.getInstance().getExternalPath(imagePath));
Log.getInstance().log(Level.FINE, "Image path: {0}, Image file: {1}", this.getClass(), imagePath, imageFile);
                output = javax.imageio.ImageIO.read(imageFile); 
            }
        }catch(javax.imageio.IIOException e) {
            Log.getInstance().log(Level.WARNING, "Image: "+imageName+", path: "+
                    (imageFile==null?imagePath:imageFile), this.getClass(), e);
            throw new ServletException("Failed to read image from source");
        }

        int width = output.getWidth(null);
        int height = output.getHeight(null);

        if(width == -1 || height == -1) {
            Log.getInstance().log(Level.WARNING, "Image: "+imageName+", path: "+imagePath, this.getClass());
            throw new ServletException("Failed to load image from source");
        }

        // Compute the preferred dimensions
        Dimension preferredDim = ImageDimensions.rescale(
                new Dimension(width, height), getTargetDimension());

        ImageManagerImpl imageManager = new LbImageManager(WebApp.getInstance());
        
        // Scale the image to the preferred dimensions
        output = imageManager.scaleImage(output,
                preferredDim.width, preferredDim.height, BufferedImage.TYPE_INT_RGB);

        if(output == null) {

            throw new ServletException("Failed to resize image: "+imagePath);

        }else{

            // Since we want to view the image, we load it
            new ImageLoaderImpl().loadImage(output);
        }

        return output;
    }

    private String getImagePath(
            String imageName, HttpServletRequest request) throws ServletException {
        
        Integer productvariantId = Integer.valueOf((String)ServletUtil.find(Productvariant_.productvariantid.getName(), request));

        UserBean user = (UserBean)request.getSession().getAttribute(UserBean.ATTRIBUTE_NAME);
        
        ProductBean productBean = user.getSelectedItem();

        if(productBean == null) {
            productBean = new ProductBean();
        }

        String imagePath = productBean.getImagePath(productvariantId, imageName);

        if(imagePath == null) {
Log.getInstance().log(Level.WARNING, "Failed to find image: {0} for product variant with ID: {1}", this.getClass(), imageName, productvariantId);
            throw new ServletException("Could not find the requested image");
        }
        
        return imagePath;
    }

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response)
        throws IOException {

        // Output the finished image straight to the response as a JPEG!
        //
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        try(ServletOutputStream responseOutputStream = response.getOutputStream()) {
            javax.imageio.ImageIO.write(this.targetImage, "jpeg", responseOutputStream);
            responseOutputStream.flush();
        }
    }
    
    private Dimension getTargetDimension() {

        Dimension targetDim;

        try {
            targetDim = Toolkit.getDefaultToolkit().getScreenSize();
        }catch(java.awt.HeadlessException ex) {
            // Default
            targetDim = ImageDimensions.large();
        }

        return targetDim;
    }

    public BufferedImage getTargetImage() {
        return targetImage;
    }

    public void setTargetImage(BufferedImage targetImage) {
        this.targetImage = targetImage;
    }
}
