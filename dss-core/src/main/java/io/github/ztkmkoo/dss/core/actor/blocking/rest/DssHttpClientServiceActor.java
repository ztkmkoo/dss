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

    public static <R extends DssBlockingRestCommand.HttpRequest> Behavior<DssBlockingRestCommand> create(DssHttpClientService<R> service, Class<R> requestClass) {
        return Behaviors.setup(context -> new DssHttpClientServiceActor<>(context, service, requestClass).httpClientServiceActor());
    }

    private final ActorContext<DssBlockingRestCommand> context;
    private final DssHttpClientService<R> service;
    private final Class<R> requestClass;

    private DssHttpClientServiceActor(ActorContext<DssBlockingRestCommand> context, DssHttpClientService<R> service, Class<R> requestClass) {
        this.context = context;
        this.service = service;
        this.requestClass = requestClass;
    }

    private Behavior<DssBlockingRestCommand> httpClientServiceActor() {
        return Behaviors
                .receive(DssBlockingRestCommand.class)
                .onMessage(requestClass, this::handlingHttpRequest)
                .build();
    }

    private Behavior<DssBlockingRestCommand> handlingHttpRequest(R msg) {
        service.httpRequest(msg, context.getSelf());
        return Behaviors.same();
    }
}
