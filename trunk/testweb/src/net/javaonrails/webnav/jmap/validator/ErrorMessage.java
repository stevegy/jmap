/*
 * 
 * 
 */

package net.javaonrails.webnav.jmap.validator;

import net.javaonrails.webnav.jmap.validator.annotation.Message;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Steve Yao 
 */
public class ErrorMessage {
    private String key = "";
    
    private String value = "";
    
    private int index = 0;
    
    private String errorMessage = "";
    
    private Locale locale = Locale.getDefault();
    
    public static Log logger = LogFactory.getLog(ErrorMessage.class);
    
    public ErrorMessage(String key, String value, int index, Locale locale) {
        this.key = key;
        this.value = value;
        this.index = index;
        this.locale = locale;
    }

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
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Key: " + this.key);
        str.append("\r\nValue: " + this.value);
        str.append("\r\nindex: " + this.index);
        str.append("\r\nlocale: " + this.locale.toString());
        str.append("\r\nMessage: " + this.errorMessage);
        str.append("\r\n");
        return str.toString();
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
