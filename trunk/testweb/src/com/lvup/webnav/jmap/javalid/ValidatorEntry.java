/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lvup.webnav.jmap.javalid;

import org.javalid.core.AnnotationValidator;
import org.javalid.core.AnnotationValidatorImpl;

public class ValidatorEntry {

    private static AnnotationValidator validator = null;

    private ValidatorEntry() {
    }

    public static final AnnotationValidator getValidator() {
        if (validator == null) {
            validator = new AnnotationValidatorImpl();
        }
        return validator;
    }
}

