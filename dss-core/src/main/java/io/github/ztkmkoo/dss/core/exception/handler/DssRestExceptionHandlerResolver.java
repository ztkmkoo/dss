package io.github.ztkmkoo.dss.core.exception.handler;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.exception.annotation.ExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.annotation.ServiceExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DssRestExceptionHandlerResolver {

    private static DssRestExceptionHandlerResolver dssRestExceptionHandlerResolver = new DssRestExceptionHandlerResolver();

    public static DssRestExceptionHandlerResolver getInstance() {
        return dssRestExceptionHandlerResolver;
    }

    private final Logger logger = LoggerFactory.getLogger(DssRestExceptionHandlerResolver.class);
    private final Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, ExceptionHandleMethod>> exceptionHandlerMap;

    private DssRestExceptionHandlerResolver() {
        this.exceptionHandlerMap = new HashMap<>();
    }

    public void setExceptionHandlerMap(DssExceptionHandler dssExceptionHandler) {
        Objects.requireNonNull(dssExceptionHandler);

        Method[] methods = dssExceptionHandler.getClass().getMethods();
        HashMap<Class<? extends Exception>, ExceptionHandleMethod> globalExceptionHandlerMap = new HashMap<>();

        for (Method method : methods) {
            if (method.isAnnotationPresent(ExceptionHandler.class)) {
                ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);

                for (Class<? extends Exception> exception : annotation.exception()) {
                    globalExceptionHandlerMap.put(exception, setExceptionHandleMethod(dssExceptionHandler, method));
                }
            } else if (method.isAnnotationPresent(ServiceExceptionHandler.class)) {
                ServiceExceptionHandler annotation = method.getAnnotation(ServiceExceptionHandler.class);
                exceptionHandlerMap.putIfAbsent(annotation.service(), new HashMap<>());
                Map<Class<? extends Exception>, ExceptionHandleMethod> serviceExceptionHandlerMap = exceptionHandlerMap.get(annotation.service());

                for (Class<? extends Exception> exception : annotation.exception()) {
                    serviceExceptionHandlerMap.put(exception, setExceptionHandleMethod(dssExceptionHandler, method));
                }
            }
        }
        exceptionHandlerMap.put(DssRestActorService.class, globalExceptionHandlerMap);
    }

    private ExceptionHandleMethod setExceptionHandleMethod(DssExceptionHandler dssExceptionHandler, Method method) {
        return request -> {
            try {
                method.setAccessible(true);
                return (DssRestServiceResponse) method.invoke(dssExceptionHandler, request);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                logger.error("exception handling method invoke error: ", e);
            } catch (Exception e) {
                logger.error("exception handling error: ", e);
            }
            return null;
        };
    }

    public Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, ExceptionHandleMethod>> getExceptionHandlerMap() {
        return this.exceptionHandlerMap;
    }
}
