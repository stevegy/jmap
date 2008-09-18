/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package com.lvup.webnav.jmap.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The @Message annotation is for localization string format.
 * `value' is the message key in the `resource' ResourceBundle.
 * @author Steve Yao <steve.yao@lvup.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Message {
    /**
     * this is message id of the errmessages.properties
     * this property is required
     * @return the error message string id in the multi-language property file.
     */
    String value();
    
    String resource() default "com/lvup/webnav/jmap/validator/errmessages";
}
