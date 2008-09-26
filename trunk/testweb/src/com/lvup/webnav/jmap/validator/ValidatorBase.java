/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package com.lvup.webnav.jmap.validator;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public abstract class ValidatorBase implements Validator {
    
    public static Log logger = LogFactory.getLog(Validator.class);

    protected List<ErrorMessage> errorMessages;
    
    protected Locale locale = Locale.getDefault();
    
    public String getMessage(Message message) {
        return getMessage(message.value(), message.resource());
    }

    public String getMessage(String msgId, String msgpath) {
        String msg = "";
        try {
            ResourceBundle res = ResourceBundle.getBundle(msgpath, getLocale());
            msg = res.getString(msgId);
        } catch (MissingResourceException e) {
            logger.warn("Cannot locate the resource. " +
                    msgpath + "->" + msgId);
        } catch (NullPointerException e) {
            logger.error("NullPointerException for the message.", e);
        }
        return msg;
    }

    public List<ErrorMessage> getErrorMessages() {
        return this.errorMessages;
    }

    public Locale getLocale() {
        return locale;
    }
    
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
