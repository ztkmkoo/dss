package io.github.ztkmkoo.dss.core.exception.annotation;

import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceExceptionHandler {

    Class<? extends DssRestActorService> service();
    Class<? extends Exception>[] exception();
}
