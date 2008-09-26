/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package com.lvup.webnav.jmap.validator;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public interface Validator {
    public boolean validate(Annotation a, String key, String[] value);
    
    public String formatMessage(Annotation a, String key, String[] value, int index);
    
    public List<ErrorMessage> getErrorMessages();
    
    public Locale getLocale();
    
    public void setLocale(Locale locale);
}
