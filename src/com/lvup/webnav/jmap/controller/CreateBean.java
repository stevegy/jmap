/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package com.lvup.webnav.jmap.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CreateBean {
    boolean create() default true;
    
    String requestAttrName() default "";
    
    String beanClassName() default "";
}
