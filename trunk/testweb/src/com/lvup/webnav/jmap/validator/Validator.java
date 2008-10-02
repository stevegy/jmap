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
public interface Validator <T extends Annotation>{
    public List<ErrorMessage> validate(T a, String key, String[] value, Locale locale);
    
    public ErrorMessage validate(T a, String key, String value, int index, Locale locale);

    public String formatMessage(T a, ErrorMessage errorMessage);

}
