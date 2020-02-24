package com.ztkmkoo.dss.server.actor.http;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.ztkmkoo.dss.server.message.HttpMessages;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 7:57
 *
 * Manager total http master actor
 * Request handler actor and Response handler actor are its child actor
 */
@Deprecated
public class HttpMasterActor extends AbstractBehavior<HttpMessages.Request> {

    private final ActorRef<HttpMessages.Request> requestActor;
    private final ActorRef<HttpMessages.Response> responseActor;

    public static Behavior<HttpMessages.Request> create() {
        return Behaviors.setup(HttpMasterActor::new);
    }

    private HttpMasterActor(ActorContext<HttpMessages.Request> context) {
        super(context);
        this.responseActor = newHttpResponseActor();
        this.requestActor = newHttpRequestActor(responseActor);
    }

    @Override
    public Receive createReceive() {
        return newReceiveBuilder().onMessage(HttpMessages.Request.class, this::onRequest).build();
    }

    private Behavior<HttpMessages.Request> onRequest(HttpMessages.Request msg) {
        getContext().getLog().info("Request: {}", msg);
        requestActor.tell(msg);
        return this;
    }

    private ActorRef<HttpMessages.Response> newHttpResponseActor() {
        return getContext().spawn(HttpResponseHandlerActor.create(), "http-response-actor");
    }

    private ActorRef<HttpMessages.Request> newHttpRequestActor(ActorRef<HttpMessages.Response> responseActor) {
        return getContext().spawn(HttpRequestHandlerActor.create(responseActor), "http-request-actor");
    }
}
