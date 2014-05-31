/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package net.javaonrails.webnav.jmap.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface RulePattern {
    String value() default "";
    
    int flag() default Pattern.CASE_INSENSITIVE;
    
    String validClass() default "net.javaonrails.webnav.jmap.validator.RulePatternValidator";

    Message fieldName() default @Message("std.pattern");

    Message message() default @Message("std.pattern");
}
