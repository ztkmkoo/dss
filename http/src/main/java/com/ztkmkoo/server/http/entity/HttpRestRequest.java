package com.ztkmkoo.server.http.entity;

import akka.actor.typed.ActorRef;
import lombok.Getter;

import java.util.Objects;

@Getter
public class HttpRestRequest {

    private final ActorRef<HttpRestResponse> replyTo;
    private final String path;

    private HttpRestRequest(final Builder builder) {
        this.replyTo = builder.replyTo;
        this.path = builder.path;
    }

    public static Builder builder(final ActorRef<HttpRestResponse> replyTo) {
        return new Builder(replyTo);
    }

    public static class Builder {

        private final ActorRef<HttpRestResponse> replyTo;
        private String path;

        private Builder(final ActorRef<HttpRestResponse> replyTo) {
            Objects.requireNonNull(replyTo);
            this.replyTo = replyTo;
        }

        public Builder path(final String path) {

            this.path = path;
            return this;
        }

        public HttpRestRequest build() {
            return new HttpRestRequest(this);
        }
    }
}
