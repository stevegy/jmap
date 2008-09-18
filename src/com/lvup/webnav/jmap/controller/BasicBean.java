/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */
package com.lvup.webnav.jmap.controller;

import com.lvup.webnav.jmap.validator.MaxLength;
import com.lvup.webnav.jmap.validator.Message;
import com.lvup.webnav.jmap.validator.MinLength;
import com.lvup.webnav.jmap.validator.Required;
import com.lvup.webnav.jmap.validator.RulePattern;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public abstract class BasicBean {

    private Locale locale = Locale.getDefault();
    
    protected boolean formValid = true;
    
    private List<String> errorMessage = null;
    
    private ControllerBase controller = null;

    public boolean isFormValid() {
        return this.formValid;
    }
    
    public String getMessage(Message message) {
        String msg = message.value();
        try {
            ResourceBundle res = getResource(message.resource(), getLocale());
            msg = res.getString(message.value());
        } catch (MissingResourceException e) {
            ControllerBase.logger.warn("Cannot locate the resource. " +
                    message.resource() + "->" + message.value());
        } catch (NullPointerException e) {
            ControllerBase.logger.error("NullPointerException for the message.", e);
        }
        return msg;
    }
    
    /**
     * This method will be called after the initFormValues by the controller.
     * You can override this method for some actions.
     */
    public void execute() {
        // do nothing for override
    }
    
    /**
     * This method is to set the fromValid field to false and avoid the 
     * setFormValid(true|false) method declaring.
     */
    public void invalidFormValue() {
        this.formValid = false;
    }

    public void initFormValues(ControllerBase controller) 
            throws IllegalAccessException, InvocationTargetException {
        
        this.controller = controller;
        
        Map param = this.controller.getRequest().getParameterMap();
        Iterator it = param.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String[] value = (String[]) param.get(key);
            try {
                Field field = this.getClass().getDeclaredField(key);
                if (PropertyUtils.isWriteable(this, key) &&
                        PropertyUtils.isReadable(this, key)) {
                    // validate
                    Annotation[] fas = field.getAnnotations();
                    for (Annotation a : fas) {
                        validateField(a, key, value);
                    }
                }
            } catch (NoSuchFieldException e) {
                ControllerBase.logger.info("field not found, name is " + key);
            }
        }
        BeanUtils.populate(this, param);
    }

    protected Method getFormatMsgMethod(String formatMethod, Class annotationClass) {
        Method method = null;
        try {
            method = this.getClass().getMethod(formatMethod, annotationClass, 
                    String.class, Class.forName("[Ljava.lang.String;"), int.class);
        } catch (NoSuchMethodException ex) {
            ControllerBase.logger.warn(
                    "No validating method defined, method name is " 
                    + formatMethod + ". Use the default method.");
        } catch (SecurityException ex) {
            ControllerBase.logger.error("Call format error message error.", ex);
        } catch(ClassNotFoundException e) {
            ControllerBase.logger.error("", e);
        }
        return method;
    }
    
    /**
     * validate the field value that passed in as a String[] use the Annotation
     * @param a the annotation marked at the field
     * @param value this is a requestMap
     * @param bean
     */
    protected void validateField(Annotation a, String key, String[] value) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (a instanceof Required) {
            validRequired((Required) a, key, value);
        }
        if (a instanceof MaxLength) {
            validMaxLength((MaxLength) a, key, value);
        }
        if (a instanceof MinLength) {
            validMinLength((MinLength) a, key, value);
        }
        if (a instanceof RulePattern) {
            validRulePattern((RulePattern) a, key, value);
        }
        // TODO other annotation types, like MaxLength ...
    }
    
    protected void validRulePattern(RulePattern rule, String key, String[] value) 
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(StringUtils.isEmpty(rule.value())) return;
        Method formatMethod = getFormatMsgMethod(rule.formatmsg(), RulePattern.class);
        Pattern pattern = Pattern.compile(rule.value(), rule.flag());
        for(int i=0, n=value.length; i<n; i++) {
            String v = value[i];
            if(StringUtils.isNotEmpty(v)) {
                if( ! pattern.matcher(v).matches()) {
                    this.invalidFormValue();
                    String msg = (formatMethod != null) ? 
                        (String) formatMethod.invoke(this, rule, key, value, i) : 
                        this.formatMsg(rule, key, value, i);
                    this.appendErrorMessage(msg);
                }
            }
        }
    }

    public String formatMsg(RulePattern rule, String key, String[] value, int index) {
        String msg = getMessage(rule.message());
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.indexOf("%s") > -1) {
                String emsg = String.format(msg, 
                    getMessage(rule.fieldName()), 
                    rule.value(), index);
                msg = emsg;
            }
        }
        return msg;
    }
    
    protected void validMinLength(MinLength minlength, String key, String[] value) 
        throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(minlength.value() < 1) return;
        Method formatMethod = getFormatMsgMethod(minlength.formatmsg(), 
                MinLength.class);
        for(int i=0, n=value.length; i<n; i++) {
            String v = value[i];
            if(StringUtils.isNotEmpty(v)) {
                if(v.getBytes().length < minlength.value()) {
                    this.invalidFormValue();
                    String msg = (formatMethod != null) ? 
                        (String) formatMethod.invoke(this, minlength, key, value, i) : 
                        this.formatMsg(minlength, key, value, i);
                    this.appendErrorMessage(msg);
                }
            }
        }
    }
     
    public String formatMsg(MinLength minlength, String key, String[] value, int index) {
        String msg = getMessage(minlength.message());
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.indexOf("%s") > -1) {
                String emsg = String.format(msg, 
                    getMessage(minlength.fieldName()), 
                    value[index].getBytes().length, minlength.value(), index);
                msg = emsg;
            }
        }
        return msg;
    }
    
    protected void validMaxLength(MaxLength maxLength, String key, String[] value) 
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if(maxLength.value() < 1)   return;
        Method formatMethod = getFormatMsgMethod(maxLength.formatmsg(), 
                MaxLength.class);
        for(int i=0, n=value.length; i<n; i++) {
            String v = value[i];
            if(StringUtils.isNotEmpty(v)) {
                if(v.getBytes().length > maxLength.value()) {
                    this.invalidFormValue();
                    String msg = (formatMethod != null) ? 
                        (String) formatMethod.invoke(this, maxLength, key, value, i) : 
                        this.formatMsg(maxLength, key, value, i);
                    this.appendErrorMessage(msg);
                }
            }
        }
    }
    
    public String formatMsg(MaxLength maxlength, String key, String[] value, int index) {
        String msg = getMessage(maxlength.message());
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.indexOf("%s") > -1) {
                String emsg = String.format(msg, 
                        getMessage(maxlength.fieldName()), 
                        value[index].getBytes().length, maxlength.value(), index);
                msg = emsg;
            }
        }
        return msg;
    }
    
    protected void validRequired(Required required, String key, String[] value) 
            throws InvocationTargetException, IllegalAccessException, 
            IllegalArgumentException {
        if (required.value()) {
            int index = 0;
            Method method = getFormatMsgMethod(required.formatmsg(), 
                    Required.class);
            for (String v : value) {
                if (StringUtils.isEmpty(v)) {
                    this.invalidFormValue();
                    String msg = (method != null) ? 
                        (String) method.invoke(this, required, key, value, index) : 
                        this.formatMsg(required, key, value, index);
                    this.appendErrorMessage(msg);
                }
                index ++;
            }
        }
    }

    /**
     * The default format error message method
     * @param required the annotation class Required
     * @param key the servlet input parameter key name
     * @param input the servlet parameter, this is a String[]
     * @param index the current parameter index value for the input array
     * @return the formatted error message string
     */
    public String formatMsg(Required required, 
            String key, String[] input, int index) {
        String msg = getMessage(required.message());
        if (StringUtils.isNotEmpty(msg)) {
            if (msg.indexOf("%s") > -1) {
                String emsg = String.format(msg, 
                        getMessage(required.fieldName()));
                msg = emsg;
            }
        }
        return msg;
    }
    
    public ResourceBundle getResource(String resource, Locale locale) {
        return ResourceBundle.getBundle(resource, locale);
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    @SuppressWarnings("unchecked")
    public void appendErrorMessage(String msg) {
        if (this.errorMessage == null) {
            this.errorMessage = new ArrayList();
        }
        this.errorMessage.add(msg);
    }

    public String getFormattedErrorMessage() {
        String ret = null;
        if (this.errorMessage != null) {
            if (this.errorMessage.size() > 0) {
                StringBuffer strb = new StringBuffer();
                strb.append("<ul>");
                for (String msg : this.errorMessage) {
                    strb.append("<li>");
                    strb.append(StringEscapeUtils.escapeHtml(msg));
                    strb.append("</li>");
                }
                strb.append("</ul>");
                ret = strb.toString();
            }
        }
        return ret;
    }

    public ControllerBase getController() {
        return controller;
    }

    public void setController(ControllerBase controller) {
        this.controller = controller;
    }

}
