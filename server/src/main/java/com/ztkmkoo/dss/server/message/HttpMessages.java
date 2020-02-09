package com.ztkmkoo.dss.server.message;

import akka.actor.typed.ActorRef;
import lombok.Getter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 6:07
 */
public class HttpMessages {

    private HttpMessages() {}

    public static class Request implements Serializable {

        private static final LinkedList<ActorRef<Request>> emptyQueue = new LinkedList<>();
        private static final long serialVersionUID = -5018351808316127595L;

        @Getter
        private final String path;
        @Getter
        private final String content;
        @Getter
        private final ActorRef<HttpMessages.Response> httpResponseHandlerActor;
        @Getter
        private final Queue<ActorRef<Request>> filterQueue;
        @Getter
        private final ActorRef<HttpMessages.Request> businessActor;

        public Request(
                String path,
                String content,
                ActorRef<HttpMessages.Response> httpResponseHandlerActor,
                List<ActorRef<Request>> filterQueue,
                ActorRef<HttpMessages.Request> businessActor) {
            this.path = path;
            this.content = content;
            this.httpResponseHandlerActor = httpResponseHandlerActor;
            this.filterQueue = new LinkedList<>(filterQueue);
            this.businessActor = businessActor;
        }

        public Request(
                String path,
                String content) {
            this(path, content, null, emptyQueue, null);
        }

        public Request(
                HttpMessages.Request request,
                ActorRef<HttpMessages.Response> httpResponseHandlerActor,
                ActorRef<HttpMessages.Request> businessActor
        ) {
            this(request.path, request.content, httpResponseHandlerActor, emptyQueue, businessActor);
        }

        public Request(
                HttpMessages.Request request,
                ActorRef<HttpMessages.Response> httpResponseHandlerActor,
                List<ActorRef<Request>> filterQueue,
                ActorRef<HttpMessages.Request> businessActor) {
            this(request.path, request.content, httpResponseHandlerActor, new LinkedList<>(filterQueue), businessActor);
        }
    }

    public static class Response implements Serializable {
    }
}
