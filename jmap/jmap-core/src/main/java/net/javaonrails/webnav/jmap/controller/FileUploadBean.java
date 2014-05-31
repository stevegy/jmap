/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package net.javaonrails.webnav.jmap.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * You can override the "initFormValues" method to setFilesizeLimit(),
 * setRequestSizeLimit(), setMemorySize(), setTempFileDir() to control the
 * file upload behave.
 * The default size of requestSizeLimit is 4M, and default file size limit is 2M.
 * If the file size limit exceed, isFileSizeOver() will be true.
 * 
 * @author Steve Yao <steve.yao@lvup.com>
 */
public abstract class FileUploadBean extends BasicBean {
    
    private int memorySize = 100 * 1024; // 100K
    
    private String tempFileDir = System.getProperty("java.io.tmpdir");
    
    private long fileSizeLimit = 2000 * 1024; // 2M
    
    private long requestSizeLimit = 4000 * 1024; // 4M
    
    private boolean fileSizeOver = false;
    
    protected Map formValueMap = null;
    
    protected List<FileItem> uploadFiles;
    
    /**
     * This method can be override to set the file size limit and other upload
     * parameters use the setters in this object.
     * 
     * @param controller
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    @Override 
    @SuppressWarnings("unchecked")
    public void initFormValues(ControllerBase controller) 
            throws IllegalAccessException, InvocationTargetException {
        
        this.setController(controller);
        if(! ServletFileUpload.isMultipartContent(controller.getRequest())) {
            super.initFormValues(controller);
        } else {
            DiskFileItemFactory factory = 
                    new DiskFileItemFactory(getMemorySize(),
                    new File(getTempFileDir()));
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setFileSizeMax(getFileSizeLimit());
            upload.setSizeMax(getRequestSizeLimit());
            String encoding = controller.getRequest().getCharacterEncoding();
            upload.setHeaderEncoding(encoding);
            List<FileItem> items = null;
            try {
                items = upload.parseRequest(controller.getRequest());
            } catch (FileSizeLimitExceededException e) {
                fileSizeOver = true;
                logger.warn("File size limit exceeds, the limit is "
                        + getFileSizeLimit() + " bytes. Actual size is " 
                        + e.getActualSize() + " bytes.");
            } catch (SizeLimitExceededException e) {
                fileSizeOver = true;
                logger.warn("Request size over the size limit " 
                        + getRequestSizeLimit()  + " bytes. Actual size is " 
                        + e.getActualSize() + " bytes.");
            } catch (FileUploadException e) {
                logger.error("File upload exception.", e);
            }
            try {
                String key = null;
                ArrayList<String> value = null;
                if(items == null)
                    return;
                for(FileItem item : items) {
                    if(item.isFormField()) {
                        if(formValueMap == null) {
                            formValueMap = new HashMap();
                        }
                        key = item.getFieldName();
                        if(formValueMap.containsKey(key)) {
                            value = (ArrayList<String>)formValueMap.get(key);
                        } else {
                            value = new ArrayList<String>();
                        }
                        value.add(item.getString(encoding));
                        formValueMap.put(key, value);
                    } else if(item.getSize() > 0) {
                        if(uploadFiles == null) {
                            uploadFiles = new ArrayList<FileItem> ();
                        }
                        uploadFiles.add(item);
                    }
                }
                if(formValueMap != null) {
                    Iterator it = formValueMap.keySet().iterator();
                    while(it.hasNext()) {
                        key = (String) it.next();
                        value = (ArrayList<String>)formValueMap.get(key);
                        String[] a = {};
                        formValueMap.put(key, value.toArray(a));
                    }
                }
            } catch (UnsupportedEncodingException ex) {
                // ? encoding error ?
                logger.error("Request encoding error?", ex);
            }
            BeanUtils.populate(this, formValueMap);
            this.validateFormMap(formValueMap);
        }
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public String getTempFileDir() {
        return tempFileDir;
    }

    public void setTempFileDir(String tempFileDir) {
        this.tempFileDir = tempFileDir;
    }

    public long getFileSizeLimit() {
        return fileSizeLimit;
    }

    public void setFileSizeLimit(long filesizeLimit) {
        this.fileSizeLimit = filesizeLimit;
    }

    /**
     * If the file size limit exceed, this value will be true.
     * @return
     */
    public boolean isFileSizeOver() {
        return fileSizeOver;
    }

    public Map getFormValueMap() {
        return formValueMap;
    }

    public List<FileItem> getUploadFiles() {
        return uploadFiles;
    }

    public long getRequestSizeLimit() {
        return requestSizeLimit;
    }

    public void setRequestSizeLimit(long requestSizeLimit) {
        this.requestSizeLimit = requestSizeLimit;
    }
}
