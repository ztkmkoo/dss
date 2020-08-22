package io.github.ztkmkoo.dss.core.exception.handler;

import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.exception.annotation.ExceptionHandler;
import io.github.ztkmkoo.dss.core.exception.annotation.ServiceExceptionHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DssRestExceptionHandler {

    private static DssRestExceptionHandler dssRestExceptionHandler = new DssRestExceptionHandler();

    public static DssRestExceptionHandler getInstance(){
        return dssRestExceptionHandler;
    }

    private final Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, Method>> exceptionHandlerMap;

    private DssRestExceptionHandler(){
        this.exceptionHandlerMap = new HashMap<>();
    }

    public void setExceptionHandlerMap(DssExceptionHandler dssExceptionHandler){
        Objects.requireNonNull(dssExceptionHandler);

        Method[] methods = dssExceptionHandler.getClass().getMethods();
        HashMap<Class<? extends Exception>, Method> globalExceptionHandlerMap = new HashMap<>();

        for(Method method: methods){
            if (method.isAnnotationPresent(ExceptionHandler.class)){
                ExceptionHandler annotation = method.getAnnotation(ExceptionHandler.class);

                for (Class<? extends Exception> exception : annotation.exception()) {
                    globalExceptionHandlerMap.put(exception, method);
                }
            } else if (method.isAnnotationPresent(ServiceExceptionHandler.class)) {
                ServiceExceptionHandler annotation = method.getAnnotation(ServiceExceptionHandler.class);
                exceptionHandlerMap.putIfAbsent(annotation.service(), new HashMap<>());
                Map<Class<? extends Exception>, Method> serviceExceptionHandlerMap = exceptionHandlerMap.get(annotation.service());

                for (Class<? extends Exception> exception : annotation.exception()) {
                    serviceExceptionHandlerMap.put(exception, method);
                }
            }
        }
        exceptionHandlerMap.put(DssRestActorService.class, globalExceptionHandlerMap);
    }

    public Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, Method>> getExceptionHandlerMap(){
        return this.exceptionHandlerMap;
    }
}
