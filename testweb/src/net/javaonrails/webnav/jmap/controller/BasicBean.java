/*
 * Copyright all rights reserved by LVUP Shanghai.
 * 
 */
package net.javaonrails.webnav.jmap.controller;

import net.javaonrails.webnav.jmap.validator.BeanValidator;
import net.javaonrails.webnav.jmap.validator.BeanValidatorImpl;
import net.javaonrails.webnav.jmap.validator.ErrorMessage;
import net.javaonrails.webnav.jmap.validator.Validator;
import net.javaonrails.webnav.jmap.validator.annotation.Message;
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
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Steve Yao <steve.yao@lvup.com>
 */
public abstract class BasicBean {

    protected boolean formValid = true;
    
    private List<ErrorMessage> errorMessage = null;
    
    private ControllerBase controller = null;
    
    public static Log logger = LogFactory.getLog(BasicBean.class);
    
    public boolean isFormValid() {
        return this.formValid;
    }
    
    public String getMessage(Message message) {
        return getMessage(message.value(), message.resource());
    }
    
    public String getMessage(String msgId, String msgpath) {
        String msg = "";
        try {
            ResourceBundle res = getResource(msgpath, 
                    getController().getLocale());
            msg = res.getString(msgId);
        } catch (MissingResourceException e) {
            logger.warn("Cannot locate the resource. " +
                    msgpath + "->" + msgId);
        } catch (NullPointerException e) {
            logger.error("NullPointerException for the message.", e);
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

    /**
     * This method use the annotations to validating fields and set the
     * fromValid field with false if one field validates failed. The multi-
     * languages error messages will be appended to the list of errorMessage
     * field. You can use the getFormattedErrorMessage() method to format a 
     * ul/li html error messages. If you donot like this kind of format, you 
     * may override this method for other new format.
     * At the end of this method, call the BeanUtils.populate to copy all
     * form values to the inherited class fields with public getter and setter
     * what ever any validating failed. You may override this method and
     * call the super.initFormValues first and call some additional validating
     * code for more customizing validation.
     * This method will be called by the ControllerBase.createBean 
     * and this method can be overrided for different behaves.
     * @param controller the caller
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    @SuppressWarnings("unchecked")
    public void initFormValues(ControllerBase controller) 
            throws IllegalAccessException, InvocationTargetException {
        
        this.controller = controller;
        
        Map param = this.controller.getRequest().getParameterMap();
        BeanUtils.populate(this, param);
        validateFormMap(param);
    }

    public ResourceBundle getResource(String resource, Locale locale) {
        return ResourceBundle.getBundle(resource, locale);
    }

    public List<ErrorMessage> getErrorMessage() {
        return errorMessage;
    }

    public void appendErrorMessage(ErrorMessage msg) {
        if(this.errorMessage == null) {
            this.errorMessage = new ArrayList<ErrorMessage>();
        }
        this.errorMessage.add(msg);
    }
    
    public void appendErrorMessage(String msg) {
        ErrorMessage emsg = new ErrorMessage("", "", 0, getController().getLocale());
        emsg.setErrorMessage(msg);
        if(errorMessage == null)
            errorMessage = new ArrayList<ErrorMessage>();
        this.errorMessage.add(emsg);
    }
    
    public String getFormattedErrorMessage() {
        String ret = null;
        if (this.errorMessage != null) {
            if (this.errorMessage.size() > 0) {
                StringBuffer strb = new StringBuffer();
                strb.append("<ul>");
                for (ErrorMessage msg : this.errorMessage) {
                    strb.append("<li>");
                    strb.append(StringEscapeUtils.escapeHtml(msg.getErrorMessage()));
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
    
    protected static BeanValidator beanValidator = null;
    
    public BeanValidator getBeanValidator() {
        if(beanValidator == null)
            beanValidator = new BeanValidatorImpl();
        return beanValidator;
    }

    @SuppressWarnings("unchecked")
    protected void validateFormMap(Map param) 
            throws InvocationTargetException, 
            SecurityException, 
            IllegalArgumentException, IllegalAccessException {
        Iterator it = param.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            String[] value = (String[]) param.get(key);
            String validClass = null;
            try {
                Field field = this.getClass().getDeclaredField(key);
                if (PropertyUtils.isWriteable(this, key)) {
                    // validate
                    Annotation[] fas = field.getAnnotations();
                    for (Annotation a : fas) {
                        validClass = getBeanValidator().validClassName(a);
                        if(StringUtils.isEmpty(validClass)) 
                            continue;
                        // The Class.forName is not a recommended method
                        // http://blog.bjhargrave.com/2007/09/classforname-caches-defined-class-in.html
                        // Class vclass = Class.forName(validClass);
                        Class vclass = this.getClass().getClassLoader().loadClass(validClass);
                        //Validator v = (Validator) vclass.newInstance();
                        Method m = vclass.getMethod("newInstance");
                        Validator v = (Validator) m.invoke(null);
                        List<ErrorMessage> msgs = 
                                v.validate(a, key, value, 
                                getController().getLocale());
                        if(msgs != null) {
                            this.invalidFormValue();
                            if(this.errorMessage == null) {
                                this.errorMessage = new ArrayList<ErrorMessage>();
                            }
                            this.errorMessage.addAll(msgs);
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                logger.error("Cannot find the " + validClass + ".newInstance()", e);
            } catch(ClassNotFoundException e) {
                logger.error("Cannot find the class " + validClass, e);
            } catch (NoSuchFieldException e) {
                logger.info("The bean class "
                        + this.getClass().getName()
                        + " cannot map the field name \"" + key + "\".");
            }
        }
    }
    
}
