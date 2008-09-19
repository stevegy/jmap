/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package com.lvup.webnav.jmap.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public abstract class FileUploadBean extends BasicBean {
    
    private int memorySize = 100 * 1024; // 100K
    
    private String tempFileDir = System.getProperty("java.io.tmpdir");
    
    private int filesizeLimit = 2000 * 1024; // 2M
    
    private boolean fileSizeOver = false;
    
    private Map formValueMap = null;
    
    private List<FileItem> uploadFiles;
    
    @Override 
    public void initFormValues(ControllerBase controller) 
            throws IllegalAccessException, InvocationTargetException {
        
        this.setController(controller);
        try {
            if(! ServletFileUpload.isMultipartContent(controller.getRequest())) {
                super.initFormValues(controller);
            } else {
                DiskFileItemFactory factory = 
                        new DiskFileItemFactory(getMemorySize(),
                        new File(getTempFileDir()));
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setFileSizeMax(filesizeLimit);
                String encoding = controller.getRequest().getCharacterEncoding();
                upload.setHeaderEncoding(encoding);
                List<FileItem> items = upload.parseRequest(controller.getRequest());
                for(FileItem item : items) {
                    if(item.isFormField()) {
                        if(formValueMap == null) {
                            formValueMap = new HashMap();
                        }
                        formValueMap.put(item.getFieldName(), 
                                item.getString(encoding));
                    } else {
                        if(uploadFiles == null) {
                            uploadFiles = new ArrayList<FileItem> ();
                        }
                        uploadFiles.add(item);
                    }
                }
                this.validateFormMap(formValueMap);
                BeanUtils.populate(this, formValueMap);
            }
        } catch (UnsupportedEncodingException e) {
            ControllerBase.logger.error("The encoding error!", e);
        }
        catch (SizeLimitExceededException e) {
            fileSizeOver = true;
            ControllerBase.logger.warn("File size over the size limit " 
                    + getFilesizeLimit()  + " bytes.");
        } catch (FileUploadException e) {
            ControllerBase.logger.error("", e);
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

    public int getFilesizeLimit() {
        return filesizeLimit;
    }

    public void setFilesizeLimit(int filesizeLimit) {
        this.filesizeLimit = filesizeLimit;
    }

    public boolean isFileSizeOver() {
        return fileSizeOver;
    }

    public Map getFormValueMap() {
        return formValueMap;
    }

    public List<FileItem> getUploadFiles() {
        return uploadFiles;
    }
}
