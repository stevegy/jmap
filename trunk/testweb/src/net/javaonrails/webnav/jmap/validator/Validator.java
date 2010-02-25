/*
 * 
 * 
 */

package net.javaonrails.webnav.jmap.validator;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public interface Validator <T extends Annotation>{
    /**
     * If there is not error, should return null.
     * @param a the validator annotation
     * @param key
     * @param value
     * @param locale
     * @return null if there is no error.
     */
    public List<ErrorMessage> validate(T a, String key, String[] value, Locale locale);
    
    public ErrorMessage validate(T a, String key, String value, int index, Locale locale);

    public String formatMessage(T a, ErrorMessage errorMessage);
}
