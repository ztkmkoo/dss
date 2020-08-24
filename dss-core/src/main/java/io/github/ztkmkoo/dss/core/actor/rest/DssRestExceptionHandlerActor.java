package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.actor.exception.DssRestRequestMappingException;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.AbstractDssRestActorService;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import io.github.ztkmkoo.dss.core.message.rest.DssRestExceptionHandlerCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestExceptionHandlerCommandRequest;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class DssRestExceptionHandlerActor {
    public static Behavior<DssRestExceptionHandlerCommand> create(Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, Method>> exceptionHandlerMap) {
        return Behaviors.setup(context -> new DssRestExceptionHandlerActor(context, exceptionHandlerMap).dssRestExceptionHandlerActor());
    }

    private final ActorContext<DssRestExceptionHandlerCommand> context;
    private final Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, Method>> exceptionHandlerMap;

    public DssRestExceptionHandlerActor(ActorContext<DssRestExceptionHandlerCommand> context, Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, Method>> exceptionHandlerMap) {
        this.context = context;
        this.exceptionHandlerMap = exceptionHandlerMap;
    }

    public Behavior<DssRestExceptionHandlerCommand> dssRestExceptionHandlerActor() {
        return Behaviors.receive(DssRestExceptionHandlerCommand.class)
                .onMessage(DssRestExceptionHandlerCommandRequest.class, this::exceptionHandling)
                .build();
    }

    private Behavior<DssRestExceptionHandlerCommand> exceptionHandling(DssRestExceptionHandlerCommandRequest commandRequest) {
        Exception e = commandRequest.getException();
        HttpResponseStatus httpResponseStatus;
        DssRestServiceResponse dssRestServiceResponse;

        if (e instanceof DssRestRequestMappingException) {
            context.getLog().error("Json request mapping error: ", e);
            httpResponseStatus = HttpResponseStatus.BAD_REQUEST;
        } else {
            context.getLog().error("Handling rest request error: ", e);
            httpResponseStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR;
        }

        dssRestServiceResponse = findExceptionHandleMethod(commandRequest);

        replyRequest(commandRequest.getRequest(), httpResponseStatus, dssRestServiceResponse);

        return Behaviors.same();
    }

    private DssRestServiceResponse findExceptionHandleMethod(DssRestExceptionHandlerCommandRequest commandRequest) {
        Map<Class<? extends Exception>, Method> serviceExceptionHandlerMap = exceptionHandlerMap.get(commandRequest.getService().getClass());

        if (containMethod(serviceExceptionHandlerMap, commandRequest)){
            Method method = serviceExceptionHandlerMap.get(commandRequest.getException().getClass());
            return invokeMethod(method, commandRequest);
        }

        Map<Class<? extends Exception>, Method> globalExceptionHandler = exceptionHandlerMap.get(DssRestActorService.class);
        if (containMethod(globalExceptionHandler, commandRequest)){
            Method method = globalExceptionHandler.get(commandRequest.getException().getClass());
            return invokeMethod(method, commandRequest);
        }

        return null;
    }

    private DssRestServiceResponse invokeMethod(Method method, DssRestExceptionHandlerCommandRequest commandRequest) {
        try {
            Constructor<?> declaredConstructor = method.getDeclaringClass().getDeclaredConstructor();
            declaredConstructor.setAccessible(true);

            Object exceptionHandler = declaredConstructor.newInstance();
            Object response;

            if (method.getParameterCount() == 1){
                Method makeRequest = AbstractDssRestActorService.class.getDeclaredMethod("makeRequest", DssRestServiceActorCommandRequest.class);
                makeRequest.setAccessible(true);

                DssRestServiceRequest request = (DssRestServiceRequest) makeRequest.invoke(commandRequest.getService(), commandRequest.getRequest());
                response = method.invoke(exceptionHandler, request.getBody());
            } else {
                response = method.invoke(exceptionHandler, null);
            }

            if (response instanceof DssRestServiceResponse) {
                return (DssRestServiceResponse) response;
            }
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void replyRequest(DssRestServiceActorCommandRequest request, HttpResponseStatus status, DssRestServiceResponse response) {
        Objects.requireNonNull(request);
        request
                .getSender()
                .tell(
                        DssRestChannelHandlerCommandResponse
                                .builder()
                                .channelId(request.getChannelId())
                                .status(status.code())
                                .response(response)
                                .build()
                );
    }

    private boolean containMethod(Map<Class<? extends Exception>, Method> exceptionHandler, DssRestExceptionHandlerCommandRequest commandRequest){
        return Objects.nonNull(exceptionHandler) && exceptionHandler.containsKey(commandRequest.getException().getClass());
    }
}
