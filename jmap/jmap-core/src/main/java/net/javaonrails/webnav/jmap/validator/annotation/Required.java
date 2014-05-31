/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package net.javaonrails.webnav.jmap.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Required annotation has some features:
 * <ul>
 * <li>mark a private field in a java bean for validating that field is required
 * </li>
 * <li>mark the error message format method name in string, when the validating
 * failed, this method in the bean will be called. <br />
 * This method has a prototype looks like:<br />
 * public String formatMsg(Required required, String key, String[] input, int index)<br/>
 * You can find the default implement method in BasicBean.formatMsg(...).
 * The `key' and String[] input are the request parameter map, so the `index' is
 * the index of current validating input, it means input[index] is the current value.
 * </li>
 * <li>To use the fieldName as a @Message annotation. This message will get some
 * localize string from the resource bundle for multi-language messages.
 * </li>
 * <li>The `message' is also the @Message annotation for localization string.</li>
 * <li>This annotation does not support array data validating. It means, if you 
 * have an array input, just like a checkbox input, this annotation can not handle it.</li>
 * </ul>
 * Usage example:<br/>
 * <pre>
 *  private static final String RES_PATH = "com/lvup/webnav/test/resources";
 *  @Required(fieldName=@Message(value="main.loginname", resource=RES_PATH))
 *  private String loginName;
 * </pre>
 * @see @Message annotation
 * @author Steve Yao <steve.yao@lvup.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Required {
    boolean value() default true;

    String validClass() default "net.javaonrails.webnav.jmap.validator.RequiredValidator";
    
    Message fieldName() default @Message("std.required");

    Message message() default @Message("std.required");
}
