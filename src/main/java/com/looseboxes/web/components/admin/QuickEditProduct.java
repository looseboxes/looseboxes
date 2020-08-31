package com.looseboxes.web.components.admin;

import java.io.Serializable;

/**
 *
 * @author Josh
 */
public class QuickEditProduct implements Serializable {

}
/**
 * 
    private String productIds;
    
    private String fromTable;
    
    private String toTable;
    
    private int startId;
    
    private int endId;
    
    private int type;
    
    public void setStart(boolean start) {
        
        if(!start) {
            return;
        }

        try{
            this.run();
        }catch(Exception e) {
            XLogger.getInstance().log(Level.WARNING, null, this.getClass(), e);
        }
    }
    
    public void run() throws Exception {
        
        if(fromTable == null || toTable == null) {
            throw new NullPointerException();
        }
        
        if(fromTable.equals(toTable)) {
            
            if(type < 1) {
                
                throw new ValidationException("Source table: "+fromTable+" cannot be equal to target table: "+toTable);
                
            }else{
                
                this.changeType();
                
                return;
            }
        }
        
        EntityController srcDao = WebApp.getInstance().getEntityController(
                Db1.NAME, fromTable, null);
        
        EntityController tgtDao = WebApp.getInstance().getEntityController(
                Db1.NAME, toTable, null);
        
        FormField srcMeta = new FormField();
        srcMeta.setTableName(fromTable);
        
        FormField tgtMeta = new FormField();
        tgtMeta.setTableName(toTable);
        
        DisplayLink linkGen = new DisplayLink();
        StringBuilder msgBuffer = new StringBuilder();
        FileIO fileIO = new FileIO();
        
        MetaDataIx tgtTableMeta = Database.getInstance(Db1.NAME).getMetaData(toTable);

        if(productIds != null) {

            this.run(this.getIds(this.productIds), srcDao, tgtDao, srcMeta, tgtMeta, tgtTableMeta, linkGen, msgBuffer, fileIO);
        }

        if(endId > 0 && startId > 0) {
            
            this.run(startId, endId, srcDao, tgtDao, srcMeta, tgtMeta, 
                    tgtTableMeta, linkGen, msgBuffer, fileIO);
        }
    }
    
    private int [] getIds(String idsStr) {
        
        String [] parts = idsStr.split(",");

XLogger.getInstance().log(Level.FINE, "From: {0}, to: {1}, ids: {2}",
    this.getClass(), fromTable, toTable, productIds);  

        int [] arr;

        if(parts != null && parts.length != 0) {

            IntegerArray intArray = new IntegerArray();

            for(int i=0; i<parts.length; i++) {
                if(parts[i].isEmpty()) {
                    continue;
                }
                int n = Integer.parseInt(parts[i]);
                intArray.add(n);
            }
            
            arr = intArray.toArray();
            
        }else{
            arr = null;
        }
        
        return arr;
    }

    private void run(int start, int end, EntityController srcDao, EntityController tgtDao,
            FormField srcMeta, FormField tgtMeta, TableMetaDataIx tgtTableMeta, 
            DisplayLink linkGen, StringBuilder msgBuffer, FileIO fileIO) throws Exception {
        
        for(int i=start; i<end; i++) {
            
            run(i, srcDao, tgtDao, srcMeta, tgtMeta, tgtTableMeta, linkGen, msgBuffer, fileIO);
        }
    }
    
    private void run(int [] ids, EntityController srcDao, EntityController tgtDao,
            FormField srcMeta, FormField tgtMeta, TableMetaDataIx tgtTableMeta, 
            DisplayLink linkGen, StringBuilder msgBuffer, FileIO fileIO) throws Exception {
        
        for(int id:ids) {
            
            run(id, srcDao, tgtDao, srcMeta, tgtMeta, tgtTableMeta, linkGen, msgBuffer, fileIO);
        }
    }
    
    private void changeType() throws Exception {
        
        EntityController dao = WebApp.getInstance().getEntityController(
                Db1.NAME, fromTable, null);

        if(this.productIds != null) {

            int [] ids = this.getIds(this.productIds);
            for(int id:ids) {
                this.changeType(dao, id, type);
            }
        }

        if(endId > 0 && startId > 0) {

            for(int i=startId; i<endId; i++) {
                this.changeType(dao, i, type);
            }
        }
    }
    
    private void changeType(EntityController dao, int productId, int newType) throws Exception {

        Object entity = dao.selectById(productId);
        
        dao.setValue(entity, Autos.Column.type.name(), newType);
        
        dao.edit(entity);
    }
    
    private void run(int srcId, EntityController srcDao, EntityController tgtDao,
            FormField srcMeta, FormField tgtMeta, TableMetaDataIx tgtTableMeta, 
            DisplayLink linkGen, StringBuilder msgBuffer, FileIO fileIO) throws Exception {
        
        Object srcEntity = srcDao.selectById(srcId);
        Map srcValues = srcDao.toMap(srcEntity);

        srcValues.remove(srcDao.getIdColumnName());
        srcValues = this.addDefaults(tgtMeta, srcValues);

        boolean imagesTransfered = false;
        boolean created = false;

        try{

            Map tgtValues = this.transferImages(srcValues, fromTable, toTable, fileIO);
            
            if(type > 0) {
                tgtValues.put(Autos.Column.type.name(), type);
            }

            imagesTransfered = true;

            tgtValues = SqlUtils.getMapOfTable(tgtTableMeta, tgtValues, false);

            Object tgtEntity = tgtDao.toEntity(tgtValues, true);

            tgtDao.create(tgtEntity);

            Object tgtId = tgtDao.getId(tgtEntity);

            assert tgtId != null;

            created = true;

            Map newValuesForSrc = this.getNewValuesForSrc(srcMeta, 
                    srcValues, srcDao.getIdColumnName() , tgtId, linkGen, msgBuffer);

            srcDao.updateEntity(srcEntity, newValuesForSrc, true);

            srcDao.edit(srcEntity);

        }finally{

            if(imagesTransfered && !created) {

                this.transferImages(srcValues, toTable, fromTable, fileIO);
            }
        }
    }
    
    private Map addDefaults(FormField tgtMeta, Map values) {
        
        Set<Map.Entry> entrySet = (Set<Map.Entry>)values.entrySet();
        
        for(Map.Entry entry:entrySet) {

            Object columnValue = entry.getValue();
            
            if(columnValue != null) {
                continue;
            }
            
            Object columnName = entry.getKey();
            
            tgtMeta.setColumnName(columnName.toString());
            
            Object defaultValue = tgtMeta.getDefaultValue();
            
            if(defaultValue != null) {
                
                entry.setValue(defaultValue);
            }
        }
        
        return values;
    }
    
    public Map getNewValuesForSrc(FormField srcMeta, Map oldValues, 
            String idColumnName, Object newId, 
            DisplayLink linkGen, StringBuilder msgBuffer) {

        // Order of method call important
        //
        Object emailAddress = oldValues.get(Autos.Column.emailAddress.name());
        
        if(emailAddress == null) {
            throw new NullPointerException();
        }
        
        Map newValues = this.removeNullables(srcMeta, oldValues);
        
        newValues.put(Autos.Column.emailAddress.name(), emailAddress);
        
        linkGen.setExternal(false);
        linkGen.setMobile(false);
        linkGen.setShortest(true);
        linkGen.setTableName(toTable);
        linkGen.setColumnName(idColumnName);
        linkGen.setColumnValue(newId);

        msgBuffer.setLength(0);
        
        // We use description because the keywords column may not contain
        // all the chars 
        msgBuffer.append("The requested item has been moved to category: ");
        msgBuffer.append(toTable).append(". ");
        HtmlGen.AHREF(linkGen.getValue(), "<b>Click Here</b>", msgBuffer);
        msgBuffer.append(" to view it.");
        
        newValues.put(Autos.Column.description.name(), msgBuffer.toString());
        
        msgBuffer.setLength(0);
        
        msgBuffer.append("Item was moved to category ");
        msgBuffer.append(toTable);
        
        newValues.put(Autos.Column.keywords.name(), msgBuffer.toString());
        
        newValues.put(Autos.Column.status.name(), Autos.Status.Moved.KEY);
        
        return newValues;
    }

    private Map removeNullables(FormField srcMeta, Map oldValues) {

        Map newValues = new HashMap(oldValues.size(), 1.0f);
        
        newValues.putAll(oldValues);
        
        oldValues = null; // Prevent usage
        
        Set<Map.Entry> entrySet = (Set<Map.Entry>)newValues.entrySet();
        
        for(Map.Entry entry:entrySet) {

            Object columnValue = entry.getValue();
            
            if(columnValue == null) {
                continue;
            }
            
            Object columnName = entry.getKey();
            
            srcMeta.setColumnName(columnName.toString());
            
            Boolean nullable = srcMeta.getNullable();
            
            if(nullable == null || nullable) {
                
                entry.setValue(null);
                
            }else{
                
                if(srcMeta.getClassName().equals("java.lang.String")) {
                    
                    entry.setValue("");
                } 
            }
        }
        
        return newValues;
    }
    
    public Map transferImages(Map inputParams, String srcTable, 
            String tgtTable, FileIO fileIO) throws IOException {

        // Extract the emailAddress from the input params
        // If emailAddress is null or empty throw NullPointerException
        //
        
        String emailAddress = (String)inputParams.get(Autos.Column.emailAddress.name());
        if(emailAddress == null || emailAddress.isEmpty()) {
            throw new NullPointerException();
        }
        
        MetaDataIx metaData = Database.getInstance(Db1.NAME).getMetaData(srcTable);

        Map<String, String> outputParams = new HashMap<String, String>(inputParams);
        
        // Local images and icons
        //
        Map<String, String> localImages = Util.getImagePaths(metaData, inputParams, true, false, true);
        
        if(localImages != null && !localImages.isEmpty()) {
            
            File tgtImagesDir = Util.createImagesDir(tgtTable, emailAddress);

XLogger.getInstance().log(Level.FINE, "Target table: {0}, images dir: {1}",
        this.getClass(), tgtTable, tgtImagesDir);

            PathContext localCtx = WebApp.getInstance().getLocalPaths();

            for(Map.Entry<String, String> entry:localImages.entrySet()) {

                String columnName = entry.getKey();
                String srcImagePath = entry.getValue();

XLogger.getInstance().log(Level.FINER, "{0}={1}",
        this.getClass(), columnName, srcImagePath);
                
                File srcImageFile = new File(srcImagePath);

                File tgtImageFile = new File(tgtImagesDir, srcImageFile.getName());

 //               boolean success = srcImageFile.renameTo(tgtImageFile);
                
                fileIO.cut(false, srcImageFile, tgtImagesDir);


XLogger.getInstance().log(Level.FINER, "Transfered {0} to {1}", this.getClass(), srcImageFile, tgtImageFile);

                if(!srcImageFile.delete()) {
                    srcImageFile.deleteOnExit();
                }else{
XLogger.getInstance().log(Level.FINER, "Deleted {0}", this.getClass(), srcImageFile);
                }

                String tgtImageRelativePath = localCtx.getRelativePath(tgtImageFile.getPath());

XLogger.getInstance().log(Level.FINER, "Adding {0}={1}", 
    this.getClass(), columnName, tgtImageRelativePath);

                outputParams.put(columnName, tgtImageRelativePath);
            }
        }

        // Remote images and icons
        //
        Map<String, String> remoteImages = Util.getImagePaths(metaData, inputParams, false, true, false);
        
        if(remoteImages != null && !remoteImages.isEmpty()) {

            outputParams.putAll(remoteImages);
        }
        
XLogger.getInstance().log(Level.FINE, " Input: {0}\nOutput: {1}", 
        this.getClass(), inputParams, outputParams);        

        return outputParams;
    }
    
    public String getFromTable() {
        return fromTable;
    }

    public void setFromTable(String fromTable) {
        this.fromTable = fromTable;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getToTable() {
        return toTable;
    }

    public void setToTable(String toTable) {
        this.toTable = toTable;
    }

    public int getEndId() {
        return endId;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
 * 
 */
