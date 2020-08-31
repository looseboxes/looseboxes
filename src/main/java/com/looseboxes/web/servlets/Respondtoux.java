package com.looseboxes.web.servlets;

import javax.servlet.annotation.WebServlet;

/**
 * @(#)RespondToUser.java   06-May-2014 20:34:49
 *
 * Copyright 2011 NUROX Ltd, Inc. All rights reserved.
 * NUROX Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license 
 * terms found at http://www.looseboxes.com/legal/licenses/software.html
 */
/**
 * Handlers requests to: Respond to a user.
 * As opposed to its super class, this class does not require login
 * @author   chinomso bassey ikwuagwu
 * @version  2.0
 * @since    2.0
 */
@WebServlet(name="Respondtoux", urlPatterns={"/respondtoux"})
public class Respondtoux extends Contactux { 
}
