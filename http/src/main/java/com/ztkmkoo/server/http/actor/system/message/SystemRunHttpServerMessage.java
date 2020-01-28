package com.ztkmkoo.server.http.actor.system.message;

import com.ztkmkoo.server.http.actor.message.BaseMessage;
import lombok.Getter;

@Getter
public class SystemRunHttpServerMessage extends BaseMessage {

    private final String host;
    private final int port;
    private final long requestTimeoutMillis;

    private SystemRunHttpServerMessage(final Builder builder) {
        this.host = getNotNullableValue(builder.host, Builder.DEFAULT_HOST);
        this.port = getNotNullableValue(builder.port, Builder.DEFAULT_PORT);
        this.requestTimeoutMillis = getNotNullableValue(builder.requestTimeoutMillis, Builder.DEFAULT_REQUEST_TIMEOUT);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static final String DEFAULT_HOST = "0.0.0.0";
        private static final int DEFAULT_PORT = 8080;
        private static final long DEFAULT_REQUEST_TIMEOUT = 1000;

        private String host;
        private Integer port;
        private Long requestTimeoutMillis;

        private Builder() {}

        public Builder host(final String host) {
            this.host = host;
            return this;
        }

        public Builder port(final int port) {
            this.port = port;
            return this;
        }

        public Builder requestTimeoutMillis(final long requestTimeoutMillis) {
            this.requestTimeoutMillis = requestTimeoutMillis;
            return this;
        }

        public SystemRunHttpServerMessage build() {
            return new SystemRunHttpServerMessage(this);
        }
    }
}
