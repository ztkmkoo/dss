package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 01:56
 */
public class DssHttpClientServiceActor<R extends DssBlockingRestCommand.HttpRequest> {

    public static <R extends DssBlockingRestCommand.HttpRequest> Behavior<DssBlockingRestCommand> create(DssHttpClientService<R> service) {
        return Behaviors.setup(context -> new DssHttpClientServiceActor<>(context, service).httpClientServiceActor());
    }

    private final ActorContext<DssBlockingRestCommand> context;
    private final DssHttpClientService<R> service;

    private DssHttpClientServiceActor(ActorContext<DssBlockingRestCommand> context, DssHttpClientService<R> service) {
        this.context = context;
        this.service = service;
    }

    private Behavior<DssBlockingRestCommand> httpClientServiceActor() {
        return Behaviors
                .receive(DssBlockingRestCommand.class)
                .onMessage(service.getCommandClassType(), this::handlingHttpRequest)
                .build();
    }

    private Behavior<DssBlockingRestCommand> handlingHttpRequest(R msg) {
        service.httpRequest(msg, context.getSelf());
        return Behaviors.same();
    }
}
