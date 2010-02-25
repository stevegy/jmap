/*
 * 
 * 
 */

package net.javaonrails.webnav.jmap.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MaxLength {
    int value() default 0;
    
    String validClass() default "net.javaonrails.webnav.jmap.validator.MaxLengthValidator";
    
    String formatmsg() default "formatMsg";

    Message fieldName() default @Message("std.maxlength");

    Message message() default @Message("std.maxlength");
}
