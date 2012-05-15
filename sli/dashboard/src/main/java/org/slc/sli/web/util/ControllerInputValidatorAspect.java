package org.slc.sli.web.util;

import java.lang.annotation.Annotation;

import javax.validation.Valid;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Fix for 3.0 spring to validate @requestbody and @requestparam
 * @author agrebneva
 *
 */
@Aspect
@Component
@Scope(value = "singleton")
public class ControllerInputValidatorAspect {

    private class DefaultStringValidatable {
        @SuppressWarnings("unused")
        @NoBadChars
        private String validatableString;

        public DefaultStringValidatable(String validatableString) {
            this.validatableString = validatableString;
        }
    }

    private Validator validator;

    @Pointcut("execution(@org.springframework.web.bind.annotation.RequestMapping * *(..))")
    @SuppressWarnings("unused")
    private void controllerInvocation() {
    }


    @Around("controllerInvocation()")
    public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
        String[] paramNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            if (checkAnnotations(annotations[i])) {
                validateArg(args[i], paramNames[i]);
            }
        }
        return joinPoint.proceed(args);
    }

    private boolean checkAnnotations(Annotation[] paramAnnotations) {
        boolean isValidate = false, isNonModelParam = false;
        for (Annotation annotation : paramAnnotations) {
            if (Valid.class.isInstance(annotation)) {
                isValidate = true;
            } else if (RequestParam.class.isInstance(annotation)
                       || PathVariable.class.isInstance(annotation)
                       || RequestBody.class.isInstance(annotation)) {
                isNonModelParam = true;
            }
        }
        return isValidate && isNonModelParam;
    }

    private void validateArg(Object arg, String argName) {
        BindingResult result = new BeanPropertyBindingResult(arg, argName);
        ValidationUtils.invokeValidator(getValidator(), arg, result);
        // force string validation for bad chars
        if (arg instanceof String) {
            ValidationUtils.invokeValidator(getValidator(), new DefaultStringValidatable((String) arg), result);
        }
        if (result.hasErrors()) {
            throw new HttpMessageConversionException("Invalid input parameter " + argName, new BindException(result));
        }
    }

    @Autowired
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public Validator getValidator() {
        return validator;
    }
}