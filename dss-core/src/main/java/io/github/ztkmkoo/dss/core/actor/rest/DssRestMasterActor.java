package io.github.ztkmkoo.dss.core.actor.rest;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.actor.blocking.DssBlockingServiceResolver;
import io.github.ztkmkoo.dss.core.actor.blocking.DssBlockingServiceResolverImpl;
import io.github.ztkmkoo.dss.core.actor.blocking.rest.DssHttpClientServiceActor;
import io.github.ztkmkoo.dss.core.actor.rest.property.DssRestActorProperty;
import io.github.ztkmkoo.dss.core.actor.rest.service.DssRestActorService;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;
import io.github.ztkmkoo.dss.core.message.rest.*;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 3. 오후 10:06
 */
public class DssRestMasterActor {

    public static Behavior<DssRestMasterActorCommand> create(DssRestActorProperty property) {
        return Behaviors.setup(context -> new DssRestMasterActor(context, property).dssRestMasterActor());
    }

    /**
     * @deprecated use create(DssRestActorProperty property) instead
     */
    @Deprecated
    public static Behavior<DssRestMasterActorCommand> create(List<DssRestActorService> serviceList) {
        return Behaviors.setup(context -> new DssRestMasterActor(context, serviceList).dssRestMasterActor());
    }

    private final ActorContext<DssRestMasterActorCommand> context;
    private final DssRestPathResolver dssRestPathResolver;
    private final DssBlockingServiceResolver dssBlockingServiceResolver;

    private DssRestMasterActor(ActorContext<DssRestMasterActorCommand> context, DssRestActorProperty property) {
        Objects.requireNonNull(property);

        this.context = context;
        this.dssRestPathResolver = dssRestPathResolver(property.getServiceList());
        this.dssBlockingServiceResolver = dssBlockingServiceResolver(property);
    }

    /**
     * @deprecated use create(DssRestActorProperty property) instead
     */
    @Deprecated
    private DssRestMasterActor(ActorContext<DssRestMasterActorCommand> context, List<DssRestActorService> serviceList) {
        this.context = context;
        this.dssRestPathResolver = dssRestPathResolver(serviceList);
        this.dssBlockingServiceResolver = new DssBlockingServiceResolverImpl();
    }

    private DssRestPathResolver dssRestPathResolver(List<DssRestActorService> serviceList) {
        if (serviceList.isEmpty()) {
            context.getLog().warn("Service list is empty");
        }

        Objects.requireNonNull(dssBlockingServiceResolver);

        final DssRestPathResolver.Builder builder = DssRestPathResolver.builder();
        serviceList.forEach(service -> {
            service.setBlockingServiceResolver(dssBlockingServiceResolver);
            final ActorRef<DssRestServiceActorCommand> serviceActor = context.spawn(DssRestServiceActor.create(service), service.getName());
            builder.addServiceActor(service.getMethodType(), service.getPath(), serviceActor);
        });
        return builder.build();
    }

    private DssBlockingServiceResolver dssBlockingServiceResolver(DssRestActorProperty property) {
        final DssBlockingServiceResolver resolver = new DssBlockingServiceResolverImpl();

        if (!property.getHttpClientServiceList().isEmpty()) {
            property.getHttpClientServiceList().forEach(service -> {
                final ActorRef<DssBlockingRestCommand> actorRef = context.spawn(DssHttpClientServiceActor.create(service), service.getName());
                resolver.addDssHttpClientService(service.getName(), actorRef);
            });
        }

        return resolver;
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
