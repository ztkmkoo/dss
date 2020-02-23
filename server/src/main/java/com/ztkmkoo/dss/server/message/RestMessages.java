package com.ztkmkoo.dss.server.message;

import akka.actor.typed.ActorRef;
import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 24. 오전 12:48
 */
public class RestMessages {

    private RestMessages() {}

    public static class Request extends ServerMessages.Req {

        private static final long serialVersionUID = -5018351808316127595L;
        private static final LinkedList<ActorRef<RestMessages.Request>> emptyQueue = new LinkedList<>();
        private static final AtomicLong randomId = new AtomicLong(0);

        @Getter
        private final long id = randomId.addAndGet(1);
        @Getter
        private final transient ChannelHandlerContext ctx;
        @Getter
        private final String path;
        @Getter
        private final String content;
        @Getter
        private final ActorRef<RestMessages.Response> restResponseHandlerActor;
        @Getter
        private final Queue<ActorRef<RestMessages.Request>> filterQueue;
        @Getter
        private final ActorRef<RestMessages.Request> businessActor;

        public Request(
                ChannelHandlerContext ctx,
                String path,
                String content,
                ActorRef<RestMessages.Response> httpResponseHandlerActor,
                List<ActorRef<RestMessages.Request>> filterQueue,
                ActorRef<RestMessages.Request> businessActor) {
            this.ctx = ctx;
            this.path = path;
            this.content = content;
            this.restResponseHandlerActor = httpResponseHandlerActor;
            this.filterQueue = new LinkedList<>(filterQueue);
            this.businessActor = businessActor;
        }

        public Request(
                ChannelHandlerContext ctx,
                String path,
                String content) {
            this(ctx, path, content, null, emptyQueue, null);
        }

        public Request(
                RestMessages.Request request,
                ActorRef<RestMessages.Response> httpResponseHandlerActor,
                ActorRef<RestMessages.Request> businessActor
        ) {
            this(request.ctx, request.path, request.content, httpResponseHandlerActor, emptyQueue, businessActor);
        }

        public Request(
                RestMessages.Request request,
                ActorRef<RestMessages.Response> httpResponseHandlerActor,
                List<ActorRef<RestMessages.Request>> filterQueue,
                ActorRef<RestMessages.Request> businessActor) {
            this(request.ctx, request.path, request.content, httpResponseHandlerActor, new LinkedList<>(filterQueue), businessActor);
        }

        @Override
        public String toString() {
            return RestMessages.Request.class.getSimpleName() + "@" + hashCode() + ":{"
                    + "\npath: " + path
                    + ",\ncontent: "+ content + "\n}";
        }
    }

    public static class Response implements Serializable {

        private static final long serialVersionUID = 8444183972980055157L;
        @Getter
        private final long id;
        @Getter
        private final transient ChannelHandlerContext ctx;
        @Getter
        private final String path;
        @Getter
        private final String content;

        public Response(RestMessages.Request request) {
            this.id = request.id;
            this.ctx = request.ctx;
            this.path = request.path;
            this.content = request.content;
        }
    }
}
