/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.javaonrails.webnav.jmap.validator;

import net.javaonrails.webnav.jmap.validator.annotation.MaxLength;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author steve
 */
public class MaxLengthValidator implements Validator<MaxLength> {
    
    private static MaxLengthValidator maxLength = null;
    
    public static MaxLengthValidator newInstance() {
        if(maxLength == null) {
            maxLength = new MaxLengthValidator();
        }
        return maxLength;
    }
    
    private MaxLengthValidator() {
        
    }

    public List<ErrorMessage> validate(MaxLength a, String key, String[] value, 
            Locale locale) {
        ArrayList<ErrorMessage> msgs = null;
        int index = 0;
        for(String v : value) {
            ErrorMessage msg = validate(a, key, v, index, locale);
            if(msg != null) {
                if(msgs == null) {
                    msgs = new ArrayList<ErrorMessage>();
                }
                msgs.add(msg);
            }
            index ++;
        }
        
        return msgs;
    }

    public ErrorMessage validate(MaxLength a, String key, String value, int index, 
            Locale locale) {
        ErrorMessage msg = null;
        if(a.value() < 1)   return msg;
        if(StringUtils.isNotEmpty(value)) {
            if(value.getBytes().length > a.value()) {
                msg = new ErrorMessage(key, value, index, locale);
                msg.setErrorMessage(formatMessage(a, msg));
            }
        }
        return msg;
    }

    public String formatMessage(MaxLength a, ErrorMessage errorMessage) {
        String msg = errorMessage.getMessage(a.message());
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.indexOf("%s") > -1) {
                String emsg = String.format(msg, 
                        errorMessage.getMessage(a.fieldName()), 
                        errorMessage.getValue().getBytes().length, a.value(), 
                        errorMessage.getIndex());
                msg = emsg;
            }
        }
        return msg;
    }

}
