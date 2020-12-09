package io.github.ztkmkoo.dss.core.exception.annotation;

import io.github.ztkmkoo.dss.core.service.rest.AbstractDssRestService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceExceptionHandler {

    Class<? extends AbstractDssRestService> service();
    Class<? extends Exception>[] exception();
}
