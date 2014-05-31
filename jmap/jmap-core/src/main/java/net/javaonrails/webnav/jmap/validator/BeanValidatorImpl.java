/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.javaonrails.webnav.jmap.validator;

import net.javaonrails.webnav.jmap.validator.annotation.MaxLength;
import net.javaonrails.webnav.jmap.validator.annotation.MinLength;
import net.javaonrails.webnav.jmap.validator.annotation.Required;
import net.javaonrails.webnav.jmap.validator.annotation.RulePattern;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author steve
 */
public class BeanValidatorImpl implements BeanValidator{

    public static Log logger = LogFactory.getLog(BeanValidatorImpl.class);
    
    public BeanValidatorImpl() {
        
    }
    
    public List<ErrorMessage> validate(Object bean, Locale locale) {
        List<ErrorMessage> msg = null;
        Field[] fields = bean.getClass().getDeclaredFields();
        // the declared fields not include the inherit fields
        for(Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            msg = validAnnotations(annotations, field, bean, locale, msg);
        }
        // Method[] methods = bean.getClass().getMethods();
        return msg;
    }

    public String validClassName(Annotation a) {
        String vclass = null;
        if (a instanceof Required) {
            Required r = (Required) a;
            vclass = r.validClass();
        } else if(a instanceof MaxLength) {
            MaxLength m = (MaxLength) a;
            vclass = m.validClass();
        } else if(a instanceof MinLength) {
            MinLength m = (MinLength) a;
            vclass = m.validClass();
        } else if(a instanceof RulePattern) {
            RulePattern r = (RulePattern) a;
            vclass = r.validClass();
        }
        return vclass;
    }
    
    private List<ErrorMessage> validAnnotations(Annotation[] annotations, Field field, Object bean, Locale locale, List<ErrorMessage> msg) {
        for (Annotation a : annotations) {
            Validator rv = null;
            String vclass = validClassName(a);
            if (StringUtils.isNotEmpty(vclass)) {
                try {
                    Class rvc = this.getClass().getClassLoader().loadClass(vclass);
                    rv = (Validator) rvc.newInstance();
                } catch (ClassNotFoundException classNotFoundException) {
                    logger.error("Cannot find the validator class you defined in the Required annotation --> " + vclass, classNotFoundException);
                } catch (InstantiationException instantiationException) {
                    logger.error("Cannot instance the class you defined in the Required annotation --> " + vclass, instantiationException);
                } catch (IllegalAccessException illegalAccessException) {
                    logger.error("Cannot access the class you defined in the Required annotation --> " + vclass, illegalAccessException);
                }
            }
            msg = validMessage(field, bean, rv, a, locale, msg);
        }
        return msg;
    }

    @SuppressWarnings("unchecked")
    private List<ErrorMessage> validMessage(Field field, Object bean, 
            Validator rv, Annotation a, Locale locale, List<ErrorMessage> msg) {
        try {
            Class fieldType = field.getType();
            ErrorMessage emsg = null;
            if (fieldType.getName().indexOf("[") > -1) {
                Object[] ov = (Object[]) PropertyUtils.getProperty(bean, field.getName());
                String[] sv = new String[ov.length];
                for (int i = 0, n = ov.length; i < n; i++) {
                    sv[i] = (ov[i] != null) ? ov.toString() : null;
                }
                msg = rv.validate(a, field.getName(), sv, locale);
            } else {
                emsg = rv.validate(a, field.getName(), (String) PropertyUtils.getProperty(bean, field.getName()), 0, locale);
                if (emsg != null) {
                    if (msg == null) {
                        msg = new ArrayList<ErrorMessage>();
                    }
                    msg.add(emsg);
                }
            }
        } catch (IllegalAccessException ex) {
            logger.error("", ex);
        } catch (InvocationTargetException ex) {
            logger.error("", ex);
        } catch (NoSuchMethodException ex) {
            logger.error("", ex);
        }
        return msg;
    }

}
