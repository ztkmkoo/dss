package com.ztkmkoo.server.http.core.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import com.ztkmkoo.server.common.util.MapUtils;
import com.ztkmkoo.server.common.util.StringUtils;
import com.ztkmkoo.server.http.entity.HttpRestRequest;
import com.ztkmkoo.server.http.entity.HttpRestResponse;

import java.util.Collections;
import java.util.Map;

public class RestControllerActor extends AbstractBehavior<HttpRestRequest> {

    private final Map<String, ActorRef<HttpRestRequest>> controllerMap;

    public static Behavior<HttpRestRequest> create(final Map<String, ActorRef<HttpRestRequest>> controllerMap) {
        return Behaviors.setup(context -> new RestControllerActor(context, controllerMap));
    }

    private RestControllerActor(final ActorContext<HttpRestRequest> context, final Map<String, ActorRef<HttpRestRequest>> controllerMap) {
        super(context);

        if (MapUtils.isEmpty(controllerMap)) {
            this.controllerMap = Collections.unmodifiableMap(Collections.emptyMap());
        } else {
            this.controllerMap = Collections.unmodifiableMap(controllerMap);
        }
    }

    @Override
    public Receive<HttpRestRequest> createReceive() {
        return newReceiveBuilder()
                .onMessage(HttpRestRequest.class, this::onHttpRestRequest)
                .build();
    }

    private Behavior<HttpRestRequest> onHttpRestRequest(final HttpRestRequest request) {

        final String path = request.getPath();
        if (StringUtils.isEmpty(path)) {
            // return message immediately
            request.getReplyTo().tell(new HttpRestResponse());
        } else {
            if (controllerMap.containsKey(path)) {
                final ActorRef<HttpRestRequest> handler = controllerMap.get(path);
                request.getReplyTo().tell(new HttpRestResponse());
            }
        }

        return this;
    }
}
