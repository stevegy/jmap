/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package com.lvup.webnav.jmap.validator;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public class ErrorMessage {
    private String key = "";
    
    private String value = "";
    
    private int index = 0;
    
    private String errorMessage = "";
    
    public ErrorMessage(String key, String value, int index, String errorMessage) {
        this.key = key;
        this.value = value;
        this.index = index;
        this.errorMessage = errorMessage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
