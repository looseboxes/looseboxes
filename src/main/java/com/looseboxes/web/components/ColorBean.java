package com.looseboxes.web.components;

import java.awt.Color;
import java.io.Serializable;


/**
 * @(#)ColorBean.java   27-May-2015 10:38:51
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
public class ColorBean implements Serializable {

    private String colorName;

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
    
    public String getColorCode() {
        return this.getColorCode(colorName);
    }
    
    public String getColorCode(String name) {
// makes a color object for R:255, G:0, B:150
// Color aColor = new Color(0xFF0096); // Use the hex number syntax
// Alternatively, use Color.decode
// Color bColor = Color.decode("FF0096");
// String rgb = Integer.toHexString(bColor.getRGB()); Return 0xFF0096
// rgb = rgb.substring(2, rgb.length()); Returns FF0096
        String output;
        if(name != null) {
            name = name.toLowerCase().trim();
            switch(name) {
                case "black":
                    output = "#"+Integer.toHexString(Color.BLACK.getRGB()).substring(2); break;
                case "blue":
                    output = "#"+Integer.toHexString(Color.BLUE.getRGB()).substring(2); break;
                case "brown":
                    output = "#A52A2A"; break;
                case "cyan":
                    output = "#"+Integer.toHexString(Color.CYAN.getRGB()).substring(2); break;
                case "dark gray":
                    output = "#"+Integer.toHexString(Color.DARK_GRAY.getRGB()).substring(2); break;
                case "gray":
                    output = "#"+Integer.toHexString(Color.GRAY.getRGB()).substring(2); break;
                case "green":
                    output = "#"+Integer.toHexString(Color.GREEN.getRGB()).substring(2); break;
                case "lavender":
                case "lavenda":
                    output = "#E6E6FA"; break;
                case "light gray":
                    output = "#"+Integer.toHexString(Color.LIGHT_GRAY.getRGB()).substring(2); break;
                case "magneta":
                    output = "#"+Integer.toHexString(Color.MAGENTA.getRGB()).substring(2); break;
                case "orange":
                    output = "#"+Integer.toHexString(Color.ORANGE.getRGB()).substring(2); break;
                case "pink":
                    output = "#"+Integer.toHexString(Color.PINK.getRGB()).substring(2); break;
                case "red":
                    output = "#"+Integer.toHexString(Color.RED.getRGB()).substring(2); break;
                case "white":
                    output = "#"+Integer.toHexString(Color.WHITE.getRGB()).substring(2); break;
                case "yellow":
                    output = "#"+Integer.toHexString(Color.YELLOW.getRGB()).substring(2); break;
                case "ivory":
                    output = "#FFFFFO"; break;
                case "maroon":
                    output = "#800000"; break;
                case "olive":
                    output = "#808000"; break;
                case "light green":
                    output = "#90EE90"; break;
                case "light blue":
                    output = "#ADD8E6"; break;
                case "lime":
                case "lime green":
                    output = "#00FF00"; break;
                case "navy":
                case "navy blue":
                    output = "#000080"; break;
                case "purple":
                    output = "#800080"; break;
                case "aqua":
                    output = "#00FFFF"; break;
                case "crimson":
                    output = "#DC143C"; break;
                case "peach":
                case "peachpuff":    
                    output = "#FFDAB9"; break;
                case "indigo":
                    output = "#4B0082"; break;
                case "silver":
                    output = "#C0C0C0"; break;
                case "sky blue":
                    output = "#87CEEB"; break;
                case "violet":
                    output = "#EE82EE"; break;
                default: output = null;    
            }
        }else{
            output = null;
        }
        return output;
    }
}
