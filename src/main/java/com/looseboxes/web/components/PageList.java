package com.looseboxes.web.components;

import com.bc.util.Log;
import com.looseboxes.web.WebApp;
import com.looseboxes.web.components.PageList.PageMetaData;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

/**
 * @(#)PageList.java   18-Apr-2014 16:10:59
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
public class PageList extends ArrayList<PageMetaData> implements FileFilter {
    
    /**
     * The relative path the directory whose files will be used to create 
     * the elements of this list
     */
    private Path dir;
    
    /**
     * Comma separated list of fileTypes. E.g. <tt>jsp,html,xml</tt>
     */
    private String fileTypes;
    
    private String [] fileTypesArr;
    
    /**
     * Comma separated list of filenames to exclude
     */
    private String exclude;
    
    private String [] excludeArr;
    
//    private CharFileIO fileIO;
    
//    private ParseJob parseJob;
    
    public PageList() { 
//        fileIO = new CharFileIO("utf-8");
// We are not currently using this to extract 'title' because it doesn't work 
// for whose title and meta-description tags are not explicitly specified;
// Such as pages written in scripting languages like .jsp, .asp etc        
//        parseJob = new ParseJob();
    }
    
    public PageList(PageList src) {
        this.dir = src.dir;
        this.exclude = src.exclude;
        this.excludeArr = src.excludeArr;
        this.fileTypes = src.fileTypes;
        this.fileTypesArr = src.fileTypesArr;
    }
    
    private boolean acceptFilename(String fname) {
        
        boolean accept = true;
        
        // @related filename matches done in lower case
        //
        fname = fname.toLowerCase();

        if(fileTypesArr != null && fileTypesArr.length != 0) {
            accept = this.endsWithAny(fname, fileTypesArr);
Log.getInstance().log(Level.FINER, "File: {0}, exluded: {1}", this.getClass(), fname, accept);        
        }
        
        if(accept) {
            if(excludeArr != null && excludeArr.length != 0) {
                boolean isExclude = this.endsWithAny(fname, excludeArr);
                accept = !isExclude;
Log.getInstance().log(Level.FINER, "File: {0}, exluded: {1}", this.getClass(), fname, isExclude);        
            }
        }
        
        return accept;
    }
    
    private boolean endsWithAny(String s, String [] arr) {
        boolean endsWith = false;
        for(String e:arr) {
            if(s.endsWith(e)) {
                endsWith = true;
                break;
            }
        }
        return endsWith;
    }

    /**
     * This method simply returns <tt>false</tt>.<br/><br/>
     * This method adds all accepted files to this List and
     * simply returns <tt>false</tt>
     * @param pathname. The File to accept or reject
     * @return false
     */
    @Override
    public boolean accept(File pathname) {

        if(pathname.isDirectory()) {
            
            String subDir = this.getRelativePath(pathname.getPath());
            
            if(subDir != null) {

                PageList subList = new PageList(this);
                
                // populates this list
                subList.setDir(subDir);

                this.addAll(subList);
            }
        }else{
            
            boolean accept = this.acceptFilename(pathname.getName());
                
            if(accept) {
                this.addFile(pathname);
            }
        }
        
        return false;
    }
    
    private void dirChanged() {

        if(exclude != null) {
            this.excludeArr = exclude.split(",");
            // @related filename matches done in lower case
            //
            this.toLowerCase(excludeArr);
Log.getInstance().log(Level.FINER, "Exclude: {0}", this.getClass(), Arrays.toString(excludeArr));        
        }
        
        if(fileTypes != null) {
            this.fileTypesArr = fileTypes.split(",");
            // @related filename matches done in lower case
            //
            this.toLowerCase(fileTypesArr);
Log.getInstance().log(Level.FINER, "File types: {0}", this.getClass(), Arrays.toString(fileTypesArr));        
        }

        File dirFile = new File(WebApp.getInstance().getPath(this.getDir()));

        // The necessary files are added to this List in the filter
        //
        dirFile.listFiles(this);
    }

    private void addFile(File file) {
        
        try{

            PageMetaData pageMeta = getMetaData(file);

            if(pageMeta != null) {

                String fname = file.getName().toLowerCase();

                if(fname.contains("index") || fname.contains("home")) {

                    this.add(0, pageMeta);

                }else{

                    this.add(pageMeta);
                }
            }
        }catch(IOException e) {
            Log.getInstance().log(Level.WARNING, "Failed to create instance of "+PageMetaData.class.getName()+" from file: "+file, this.getClass(), e);
        }
    }
    
    /**
     * @see #getRelativePath(java.nio.file.Path, java.nio.file.Path) 
     */
    private String getRelativePath(String path) {
        return this.getRelativePath(dir, Paths.get(path));
    }
    
    /**
     * For parent: <tt>/cat/goodies/pages</tt><br/><br/>
     * And child: <tt>/home/html/cat/goodies/pages/articles/reviews</tt><br/><br/>
     * Output: <tt>/articles/reviews</tt>
     * @param reference The relative path returned is with reference to this path
     * @param child The path to use in constructing a relative path.
     * @return A path constructed by the input relative to <tt>reference</tt>
     * or null if <tt>reference</tt> is not a sub path of <tt>child</tt>
     */
    private String getRelativePath(Path reference, Path child) {
        String output;
        String sval = child.toString(); 
        final int index = sval.indexOf(reference.toString());
        if(index != -1) {
            output = sval.substring(index);
        }else{
            output = null;
        }
Log.getInstance().log(Level.FINER, "Path: {0}, relative: {1}", this.getClass(), child, output);        
        return output;
    }
    
    private PageMetaData getMetaData(final File file) 
            throws IOException, MalformedURLException {
        
        final String title = this.getFileId(file);

Log.getInstance().log(Level.FINER, "Title: {0}", this.getClass(), title);        

        String contextPath = WebApp.getInstance().getPath("");

        String relative = PageList.this.getRelativePath(Paths.get(contextPath), Paths.get(file.getPath()));

        final String link = WebApp.getInstance().getURL(relative).toExternalForm();

Log.getInstance().log(Level.FINER, "Link: {0}", this.getClass(), link);        

        return new PageMetaData() {
            @Override
            public String getAuthor() {
                return WebApp.getInstance().getName();
            }
            @Override
            public String getDescription() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public long getLastModified() {
                return file.lastModified();
            }
            @Override
            public String getLink() {
                return link;
            }
            @Override
            public long getSize() {
                return file.length();
            }
            @Override
            public String getTitle() {
                return title;
            }
            @Override
            public String toString() {
                return title;
            }
        };
    }
    
    private String getFileId(File file) {
        String fname = this.getFileNameWithoutExtension(file.getName());        
        fname = fname.replace('_', ' ');
        fname = fname.replace('-', ' ');
        return fname;
    }
    
    private String getFileNameWithoutExtension(String fname) {
        int end = fname.lastIndexOf('.');
        if(end != -1) {
            fname = fname.substring(0, end);
        }
        return fname;
    }
    
    private void toLowerCase(String [] arr) {
        if(arr == null) {
            return;
        }
        for(int i=0; i<arr.length; i++) {
            arr[i] = arr[i].toLowerCase();
        }
    }
    
    /**
     * @return String. The relative path the directory whose files will be 
     * used to create the elements of this list
     */
    public String getDir() {
        return dir == null ? null : dir.toString();
    }

    /**
     * If the dir was changed by calling this method, then the list will
     * be repopulated from the new dir.
     * @param dir
     * @see #getDir() 
     */
    public void setDir(String dir) {
        // Do this before setting the value
        boolean dirChanged = dir != null && !dir.equals(this.getDir());
        this.dir = Paths.get(dir);
        if(dirChanged) {
            this.dirChanged();
        }
    }

    /**
     * @return 
     * @see #fileTypes
     */
    public String getFileTypes() {
        return fileTypes;
    }

    /**
     * @param fileTypes
     * @see #fileTypes
     */
    public void setFileTypes(String fileTypes) {
        this.fileTypes = fileTypes;
    }

    /**
     * @return 
     * @see #exclude
     */
    public String getExclude() {
        return exclude;
    }

    /**
     * @param exclude
     * @see #exclude
     */
    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public interface PageMetaData {

        public String getAuthor();

        public String getDescription();

        public String getTitle();

        public long getSize();

        public long getLastModified();

        public String getLink();
    }
}
