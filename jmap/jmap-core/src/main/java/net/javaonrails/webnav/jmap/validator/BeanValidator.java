/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.javaonrails.webnav.jmap.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author steve
 */
public interface BeanValidator <T>{
    List<ErrorMessage> validate(T bean, Locale locale) throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
    
    String validClassName(Annotation a);
}
