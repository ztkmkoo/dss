package com.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import com.ztkmkoo.dss.core.message.rest.*;

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

    private DssRestMasterActor(ActorContext<DssRestMasterActorCommand> context, List<DssRestActorService> serviceList) {
        this.context = context;
        this.dssRestPathResolver = dssRestPathResolver(serviceList);
    }

    private DssRestPathResolver dssRestPathResolver(List<DssRestActorService> serviceList) {
        if (serviceList.isEmpty()) {
            context.getLog().warn("Service list is empty");
        }

        final DssRestPathResolver.Builder builder = DssRestPathResolver.builder();
        serviceList.forEach(service -> {
            final ActorRef<DssRestServiceActorCommand> serviceActor = context.spawn(DssRestServiceActor.create(service), service.getName());
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

        final Optional<ActorRef<DssRestServiceActorCommand>> optional = dssRestPathResolver.getStaticServiceActorByPath(request.getPath());
        if (optional.isPresent()) {
            optional.get().tell(new DssRestServiceActorCommandRequest(request));
        } else {
            request.getSender().tell(DssRestChannelHandlerCommandInvalidUriResponse.builder().channelId(request.getChannelId()).build());
        }

        return Behaviors.same();
    }
}
