package com.ztkmkoo.server.http.actor.system.message;

import akka.http.javadsl.model.HttpRequest;
import com.ztkmkoo.server.http.actor.message.BaseMessage;
import lombok.Getter;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Getter
public class SystemHttpRequestMessage extends BaseMessage {

    private static final AtomicLong idGenerator = new AtomicLong(0);

    private final long id;
    private final HttpRequest httpRequest;

    private SystemHttpRequestMessage(final Builder builder) {
        this.id = idGenerator.addAndGet(1);
        this.httpRequest = builder.httpRequest;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private HttpRequest httpRequest;

        private Builder() {}

        public Builder httpRequest(final HttpRequest httpRequest) {
            this.httpRequest = httpRequest;
            return this;
        }

        public SystemHttpRequestMessage build() {

            Objects.requireNonNull(httpRequest);

            return new SystemHttpRequestMessage(this);
        }
    }
}
