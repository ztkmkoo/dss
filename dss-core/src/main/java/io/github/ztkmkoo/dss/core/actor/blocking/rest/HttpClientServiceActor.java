package io.github.ztkmkoo.dss.core.actor.blocking.rest;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import io.github.ztkmkoo.dss.core.message.blocking.DssBlockingRestCommand;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-13 01:56
 */
public class HttpClientServiceActor<S extends Serializable> {

    public static <S extends Serializable> Behavior<DssBlockingRestCommand> create(HttpClientService<S> service) {
        return Behaviors.setup(context -> new HttpClientServiceActor<>(context, service).httpClientServiceActor());
    }

    private final ActorContext<DssBlockingRestCommand> context;
    private final HttpClientService<S> service;

    private HttpClientServiceActor(ActorContext<DssBlockingRestCommand> context, HttpClientService<S> service) {
        this.context = context;
        this.service = service;
    }

    private Behavior<DssBlockingRestCommand> httpClientServiceActor() {
        return Behaviors
                .receive(DssBlockingRestCommand.class)
                .onMessage(DssBlockingRestCommand.HttpGetRequest.class, this::handlingHttpGetRequest)
                .build();
    }

    private Behavior<DssBlockingRestCommand> handlingHttpGetRequest(DssBlockingRestCommand.HttpGetRequest msg) {
        final DssBlockingRestCommand.DssHttpResponseCommand<S> response = service.getRequest(msg);
        Objects.requireNonNull(response);

        if (Objects.nonNull(msg.getSender())) {
            msg.getSender().tell(DssBlockingRestCommand.HttpResponse
                    .builder(msg.getSeq(), context.getSelf(), response.getCode())
                    .message(response.getMessage())
                    .body(response.getBody())
                    .build());
        } else {
            context.getLog().error("Http Request sender is empty: {}", msg);
        }

        return Behaviors.same();
    }
}
