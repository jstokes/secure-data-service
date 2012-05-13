package org.slc.sli.web.util;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for NoBadChars.
 * TODO: fix for other objects
 * @author agrebneva
 *
 */
public class NoBadCharsValidator implements ConstraintValidator<NoBadChars, Object> {
    private Pattern pattern;
    private int depth;
    @Override
    public void initialize(NoBadChars noBadChars) {
        pattern = Pattern.compile(noBadChars.regexp());
        this.depth = noBadChars.depth();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        return traverseObject(object, context, 0);
    }

    private boolean traverseObject(Object object, ConstraintValidatorContext context, int count) {
        if (count >= depth) {
            return true;
        }
        if (object instanceof String) {
            return validateString((String) object, context);
        }
        if (object instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, ?> map = (Map<String, ?>) object;
            for (Map.Entry<String, ?> item :  map.entrySet()) {
                if (!(validateString(item.getKey(), context) && traverseObject(item.getValue(), context, count))) {
                    return false;
                }
            }
        }
        if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            for (Object item : collection) {
                if (!traverseObject(item, context, count + 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateString(String s, ConstraintValidatorContext context) {
        return !pattern.matcher(s).find();
    }
}