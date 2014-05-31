/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */

package net.javaonrails.webnav.jmap.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * the `create` default is true, means create the java bean when a http request
 * come in and the http method matches the attribute `createOnHttpMethod`.
 * 
 * The createOnHttpMethod is a string array, default value is 
 * {"GET", "POST"}. You can control this value to avoid create a java bean 
 * when a http GET is coming.
 * 
 * The requestAttrName is a string name for HttpRequest attribute map key name
 * to point to a java bean instance. So you can use this name to retrive a
 * java bean instance from request.getAttribute("thisNameValue"). If this value
 * is empty strng and the condition of create a bean is true, the default
 * name will be the Controller Class name + response method Name. For example,
 * the URI is /main/login, so the Controller class name is Main, and 
 * the response method is Login, so this default name will be "MainLogin". 
 * 
 * The beanClassName is for assigning a class bean name, if omit as an empty
 * string the default class name will be the 
 * (package.prefixname.)bean.(servletname).(methodname) to looking for and 
 * create an instance. If the class can not be found, this bean will not be 
 * created.
 *
 * The maxRequestSizeLimit is for the file upload bean to limit the total
 * requst data size. 
 * 
 * 
 * 
 * @author Steve Yao <steve.yao@lvup.com>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CreateBean {
    boolean create() default true;
    
    String[] createOnHttpMethod() default {"GET", "POST"};
    
    String requestAttrName() default "";
    
    String beanClassName() default "";
    
}
