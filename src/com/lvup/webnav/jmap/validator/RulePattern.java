/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package com.lvup.webnav.jmap.validator;

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
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface RulePattern {
    String value() default "";
    
    int flag() default Pattern.CASE_INSENSITIVE;
    
    String formatmsg() default "formatMsg";

    Message fieldName() default @Message("std.pattern");

    Message message() default @Message("std.pattern");
}
