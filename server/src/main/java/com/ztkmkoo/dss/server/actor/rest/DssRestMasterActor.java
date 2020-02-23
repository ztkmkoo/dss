package com.ztkmkoo.dss.server.actor.rest;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import com.ztkmkoo.dss.server.message.RestMessages;
import com.ztkmkoo.dss.server.message.ServerMessages;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 24. 오전 1:40
 */
public class DssRestMasterActor extends AbstractBehavior<ServerMessages.Req> {

    public static Behavior<ServerMessages.Req> create() {
        return Behaviors.setup(DssRestMasterActor::new);
    }

    private DssRestMasterActor(ActorContext<ServerMessages.Req> context) {
        super(context);
    }

    @Override
    public Receive<ServerMessages.Req> createReceive() {
        return newReceiveBuilder()
                .onMessage(ServerMessages.Req.class, this::onHandlingServerMessagesRequest)
                .build();
    }

    private Behavior<ServerMessages.Req> onHandlingServerMessagesRequest(ServerMessages.Req request) {

        if (request instanceof RestMessages.Request) {
            return onHandlingRestMessagesRequest((RestMessages.Request) request);
        } else {
            // do sth
        }
        return this;
    }

    private Behavior<ServerMessages.Req> onHandlingRestMessagesRequest(RestMessages.Request request) {

        getContext().getLog().info("onHandlingRestMessagesRequest: {}", request);
        return this;
    }
}
