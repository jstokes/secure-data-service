package org.slc.sli.web.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * simple blacklist validator
 * @author agrebneva
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NoBadCharsValidator.class)
public @interface NoBadChars {
    String message() default "The object contains a string with blacklisted chars";
    String regexp() default "['<>%$+]";
    int depth() default 10;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
