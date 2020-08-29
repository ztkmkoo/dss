package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.actor.exception.DssRestRequestMappingException;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.exception.handler.ExceptionHandleMethod;
import io.github.ztkmkoo.dss.core.message.rest.DssRestChannelHandlerCommandResponse;
import io.github.ztkmkoo.dss.core.message.rest.DssRestExceptionHandlerCommand;
import io.github.ztkmkoo.dss.core.message.rest.DssRestExceptionHandlerCommandRequest;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.Map;
import java.util.Objects;

public class DssRestExceptionHandlerActor {

    public static Behavior<DssRestExceptionHandlerCommand> create(Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, ExceptionHandleMethod>> exceptionHandlerMap) {
        return Behaviors.setup(context -> new DssRestExceptionHandlerActor(context, exceptionHandlerMap).dssRestExceptionHandlerActor());
    }

    private final ActorContext<DssRestExceptionHandlerCommand> context;
    private final Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, ExceptionHandleMethod>> exceptionHandlerMap;

    public DssRestExceptionHandlerActor(ActorContext<DssRestExceptionHandlerCommand> context, Map<Class<? extends DssRestActorService>, Map<Class<? extends Exception>, ExceptionHandleMethod>> exceptionHandlerMap) {
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
        Map<Class<? extends Exception>, ExceptionHandleMethod> serviceExceptionHandlerMap = exceptionHandlerMap.get(commandRequest.getService().getClass());

        if (containMethod(serviceExceptionHandlerMap, commandRequest)) {
            ExceptionHandleMethod exceptionHandleMethod = serviceExceptionHandlerMap.get(commandRequest.getException().getClass());
            return exceptionHandleMethod.handlingException(commandRequest.getRequest());
        }

        Map<Class<? extends Exception>, ExceptionHandleMethod> globalExceptionHandler = exceptionHandlerMap.get(DssRestActorService.class);

        if (containMethod(globalExceptionHandler, commandRequest)) {
            ExceptionHandleMethod exceptionHandleMethod = globalExceptionHandler.get(commandRequest.getException().getClass());
            return exceptionHandleMethod.handlingException(commandRequest.getRequest());
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

    private boolean containMethod(Map<Class<? extends Exception>, ExceptionHandleMethod> exceptionHandler, DssRestExceptionHandlerCommandRequest commandRequest) {
        return Objects.nonNull(exceptionHandler) && exceptionHandler.containsKey(commandRequest.getException().getClass());
    }
}
