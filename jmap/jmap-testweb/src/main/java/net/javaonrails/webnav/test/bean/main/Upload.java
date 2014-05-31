/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.javaonrails.webnav.test.bean.main;

import net.javaonrails.webnav.jmap.controller.ControllerBase;
import net.javaonrails.webnav.jmap.controller.FileUploadBean;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Steve
 */
public class Upload extends FileUploadBean{

    private String text1;
    
    private int[] chkTest;
    
    @Override
    public void initFormValues(ControllerBase controller) throws IllegalAccessException, InvocationTargetException {
        this.setFileSizeLimit(50*1024); // shrink to 50K
        
        super.initFormValues(controller);
        
        if(this.isFileSizeOver()) {
            // file size or request limit exceeds! The form values are all lost!
            this.appendErrorMessage(String.format( 
                    this.getMessage("std.filesizelimit", 
                        ControllerBase.STD_ERROR_MSG_RESOURCE), 
                    this.getFileSizeLimit()));
        }
    }
    
    @Override
    public void execute() {
        // save the upload files
        if(this.uploadFiles != null) {
            for(FileItem item : this.uploadFiles) {
                // File file = new File(...);
                // item.write(file);
            }
        }
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public int[] getChkTest() {
        return chkTest;
    }

    public void setChkTest(int[] chkTest) {
        this.chkTest = chkTest;
    }
}
