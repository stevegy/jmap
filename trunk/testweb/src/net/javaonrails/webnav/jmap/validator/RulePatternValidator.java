/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.javaonrails.webnav.jmap.validator;

import net.javaonrails.webnav.jmap.validator.annotation.RulePattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author steve
 */
public class RulePatternValidator implements Validator<RulePattern>{
    
    private static RulePatternValidator rulePattern = null;
    
    private RulePatternValidator() {
        
    }
    
    public static RulePatternValidator newInstance() {
        if(rulePattern == null) {
            rulePattern = new RulePatternValidator();
        }
        return rulePattern;
    }

    public List<ErrorMessage> validate(RulePattern a, String key, String[] value, Locale locale) {
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

    public ErrorMessage validate(RulePattern a, String key, String value, int index, Locale locale) {
        ErrorMessage msg = null;
        if( StringUtils.isEmpty(a.value()) || value == null)   return msg;
        Pattern p = Pattern.compile(a.value());
        if(! p.matcher(value).matches()) {
            msg = new ErrorMessage(key, value, index, locale);
            msg.setErrorMessage(formatMessage(a, msg));
        }
        return msg;
    }

    public String formatMessage(RulePattern a, ErrorMessage errorMessage) {
        String msg = errorMessage.getMessage(a.message());
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.indexOf("%s") > -1) {
                String emsg = String.format(msg, 
                        errorMessage.getMessage(a.fieldName()), 
                        a.value());
                msg = emsg;
            }
        }
        return msg;
    }

}
