/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package com.lvup.webnav.jmap.validator;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public class RequiredValidator extends ValidatorBase {

    public RequiredValidator(Locale locale) {
        this.locale = locale;
    }
    
    public boolean validate(Annotation a, String key, String[] value) {
        Required required = (Required) a;
        int index = 0;
        boolean ret = true;
        if (required.value()) {
            for (String v : value) {
                if (StringUtils.isEmpty(v)) {
                    ErrorMessage emsg = new ErrorMessage(key, value[index], 
                            index, formatMessage(required, key, value, index));
                    if(this.errorMessages == null) {
                        this.errorMessages = new ArrayList<ErrorMessage>();
                    }
                    this.errorMessages.add(emsg);
                    ret = false;
                }
                index ++;
            }
        }
        return ret;
    }

    public String formatMessage(Annotation a, String key, String[] value, int index) {
        Required required = (Required) a;
        String msg = getMessage(required.message());
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.indexOf("%s") > -1) {
                String emsg = String.format(msg, 
                        getMessage(required.fieldName()));
                msg = emsg;
            }
        }
        return msg;
    }
}
