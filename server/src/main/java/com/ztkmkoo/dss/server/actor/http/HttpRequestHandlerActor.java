package com.ztkmkoo.dss.server.actor.http;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.ztkmkoo.dss.server.message.HttpMessages;

import java.util.*;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 3:27
 */
public class HttpRequestHandlerActor extends AbstractBehavior<HttpMessages.Request> {

    private final Map<String, ActorRef<HttpMessages.Request>> businessActorPathMap = new HashMap<>();
    private final List<ActorRef<HttpMessages.Request>> filterList = new ArrayList<>();
    private final ActorRef<HttpMessages.Response> responseActor;

    public static Behavior<HttpMessages.Request> create(ActorRef<HttpMessages.Response> responseActor) {
        return Behaviors.setup(context -> new HttpRequestHandlerActor(context, responseActor));
    }

    private HttpRequestHandlerActor(
            ActorContext<HttpMessages.Request> context,
            ActorRef<HttpMessages.Response> responseActor) {
        super(context);
        this.responseActor = responseActor;
    }

    @Override
    public Receive<HttpMessages.Request> createReceive() {
        return newReceiveBuilder().onMessage(HttpMessages.Request.class, this::onRequest).build();
    }

    private Behavior<HttpMessages.Request> onRequest(HttpMessages.Request request) {

        getContext().getLog().info("on HttpMessages.Request: {}", request);

        if (!businessActorPathMap.containsKey(request.getPath())) {
            responseActor.tell(new HttpMessages.Response(request));
            return this;
        }

        final ActorRef<HttpMessages.Request> businessActor = businessActorPathMap.get(request.getPath());

        if (filterList.isEmpty()) {
            businessActor.tell(new HttpMessages.Request(request, responseActor, businessActor));
        } else {
            final HttpMessages.Request req = new HttpMessages.Request(request, responseActor, new LinkedList<>(filterList), businessActor);
            final ActorRef<HttpMessages.Request> target = req.getFilterQueue().poll();
            Objects.requireNonNull(target);
            target.tell(req);
        }

        return this;
    }
}
