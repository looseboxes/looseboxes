package com.looseboxes.web.servlets;

import com.looseboxes.web.AppProperties;
import com.bc.imageutil.impl.ImageLoaderImpl;
import com.bc.web.core.captcha.CaptchaFactory;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.form.LbImageManager;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Josh
 */
@WebServlet(name="Captcha", urlPatterns={"/captcha", "/Captcha"})
public class Captcha extends Viewimg {
   
    @Override
    protected String getParameterName() {
        return "ImageCaptcha_BufferedImage";
    }

    @Override
    public void forwardError(
            Throwable t, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        this.log(t);
        
        String s = "Error";
        
        this.sendMessage(MessageType.warningMessage, s, s, s, request, response);
    }

    @Override
    public BufferedImage getImage(HttpServletRequest request) throws ServletException, IOException {

        BufferedImage image = CaptchaFactory.createGenerator().generate(request);
                
        int width = WebApp.getInstance().getConfig().getInt(AppProperties.CAPTCHA_IMAGE_WIDTH, 125);
        int height = WebApp.getInstance().getConfig().getInt(AppProperties.CAPTCHA_IMAGE_HEIGHT, 35);
        
        image = new LbImageManager(WebApp.getInstance()).scaleImage(image, width, height, BufferedImage.TYPE_INT_RGB);

        // Since we want to view the image, we load it
        new ImageLoaderImpl().loadImage(image);

        return image;
    }
}
