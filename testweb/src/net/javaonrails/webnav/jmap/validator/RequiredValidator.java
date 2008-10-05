/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package net.javaonrails.webnav.jmap.validator;

import net.javaonrails.webnav.jmap.validator.annotation.Required;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public class RequiredValidator implements Validator<Required> {

    static private RequiredValidator required = null;
    
    static public RequiredValidator newInstance() {
        if(required == null) {
            required = new RequiredValidator();
        }
        return required;
    }
    
    private RequiredValidator() {
        
    }
    
    public List<ErrorMessage> validate(Required a, 
            String key, String[] value, Locale locale) {
        int index = 0;
        ArrayList<ErrorMessage> errorMessages = null;
        if (a.value()) {
            for (String v : value) {
                ErrorMessage emsg = validate(a, key, v, index, locale);
                if(emsg != null) {
                    emsg.setErrorMessage(formatMessage(a, emsg));
                    if(errorMessages == null) {
                        errorMessages = new ArrayList<ErrorMessage>();
                    }
                    errorMessages.add(emsg);
                }
                index ++;
            }
        }
        return errorMessages;
    }

    public String formatMessage(Required a, ErrorMessage errorMessage) {
        String msg = errorMessage.getMessage(a.message());
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.indexOf("%s") > -1) {
                String emsg = String.format(msg, 
                        errorMessage.getMessage(a.fieldName()));
                msg = emsg;
            }
        }
        return msg;
    }

    public ErrorMessage validate(Required a, String key, String value, int index, 
            Locale locale) {
        ErrorMessage errorMessage = null;
        if (a.value()) {
            if (StringUtils.isEmpty(value)) {
                errorMessage = new ErrorMessage(key, value, 
                        index, locale);
                errorMessage.setErrorMessage(formatMessage(a, errorMessage));
            }
        }
        return errorMessage;
    }

}
