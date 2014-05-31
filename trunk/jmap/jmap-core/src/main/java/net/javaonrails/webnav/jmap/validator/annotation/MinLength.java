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
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MinLength {
    int value() default 0;
    
    String validClass() default "net.javaonrails.webnav.jmap.validator.MinLengthValidator";

    Message fieldName() default @Message("std.minlength");

    Message message() default @Message("std.minlength");
}
