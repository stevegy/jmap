/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.javaonrails.webnav.jmap.validator;

import net.javaonrails.webnav.jmap.validator.annotation.MinLength;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author steve
 */
public class MinLengthValidator implements Validator<MinLength>{
    
    private static MinLengthValidator minLength = null;
    
    private MinLengthValidator() {
        
    }
    
    public static MinLengthValidator newInstance() {
        if(minLength == null) {
            minLength = new MinLengthValidator();
        }
        return minLength;
    }

    public List<ErrorMessage> validate(MinLength a, String key, String[] value, Locale locale) {
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

    public ErrorMessage validate(MinLength a, String key, String value, int index, Locale locale) {
        ErrorMessage msg = null;
        if(a.value() < 1)   return msg;
        int len = StringUtils.isEmpty(value) ? 0 : value.getBytes().length;
        if( len < a.value()) {
            msg = new ErrorMessage(key, value, index, locale);
            msg.setErrorMessage(formatMessage(a, msg));
        }
        return msg;
    }

    public String formatMessage(MinLength a, ErrorMessage errorMessage) {
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
