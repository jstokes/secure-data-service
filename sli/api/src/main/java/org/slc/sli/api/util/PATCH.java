package org.slc.sli.api.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.ws.rs.HttpMethod;

/**
 *
 * TODO: add javadoc comment
 *
 */
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PATCH")
public @interface PATCH {
}
