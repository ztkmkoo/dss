package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.exception.handler.DssRestExceptionHandler;
import io.github.ztkmkoo.dss.core.message.rest.*;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.List;
import java.util.Optional;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:06
 */
public class DssRestMasterActor {

    public static Behavior<DssRestMasterActorCommand> create(List<DssRestActorService> serviceList) {
        return Behaviors.setup(context -> new DssRestMasterActor(context, serviceList).dssRestMasterActor());
    }

    private final ActorContext<DssRestMasterActorCommand> context;
    private final DssRestPathResolver dssRestPathResolver;
    private final ActorRef<DssRestExceptionHandlerCommand> exceptionHandler;

    private DssRestMasterActor(ActorContext<DssRestMasterActorCommand> context, List<DssRestActorService> serviceList) {
        this.context = context;
        this.exceptionHandler = context.spawn(DssRestExceptionHandlerActor.create(DssRestExceptionHandler.getInstance().getExceptionHandlerMap()), "exception-handler");
        this.dssRestPathResolver = dssRestPathResolver(serviceList);
    }

    private DssRestPathResolver dssRestPathResolver(List<DssRestActorService> serviceList) {
        if (serviceList.isEmpty()) {
            context.getLog().warn("Service list is empty");
        }

        final DssRestPathResolver.Builder builder = DssRestPathResolver.builder();
        serviceList.forEach(service -> {
            final ActorRef<DssRestServiceActorCommand> serviceActor = context.spawn(DssRestServiceActor.create(service, exceptionHandler), service.getName());
            builder.addServiceActor(service.getMethodType(), service.getPath(), serviceActor);
        });
        return builder.build();
    }

    private Behavior<DssRestMasterActorCommand> dssRestMasterActor() {
        return Behaviors
                .receive(DssRestMasterActorCommand.class)
                .onMessage(DssRestMasterActorCommandRequest.class, this::handlingDssRestMasterActorCommandRequest)
                .build();
    }

    private Behavior<DssRestMasterActorCommand> handlingDssRestMasterActorCommandRequest(DssRestMasterActorCommandRequest request) {

        context.getLog().info("DssRestMasterActorCommandRequest: {}", request);

        final Optional<ActorRef<DssRestServiceActorCommand>> optional = dssRestPathResolver.getStaticServiceActor(request.getMethodType(), request.getPath());
        if (optional.isPresent()) {
            optional.get().tell(new DssRestServiceActorCommandRequest(request));
        } else {
            request
                    .getSender()
                    .tell(DssRestChannelHandlerCommandResponse
                            .builder()
                            .channelId(request.getChannelId())
                            .status(HttpResponseStatus.BAD_REQUEST.code())
                            .build()
                    );
        }

        return Behaviors.same();
    }
}
