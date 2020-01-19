package com.ztkmkoo.server.http.config;

import lombok.Getter;

import java.util.Objects;

@Getter
public class HttpConfig {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    private final String host;
    private final int port;

    private HttpConfig(final Builder builder) {
        this.host = getValue(builder.host, DEFAULT_HOST);
        this.port = getValue(builder.port, DEFAULT_PORT);
    }

    public static Builder builder() {
        return new Builder();
    }

    private static <T> T getValue(final T nullable, final T defaultValue) {

        if (Objects.nonNull(nullable)) {
            return nullable;
        }

        Objects.requireNonNull(defaultValue);
        return defaultValue;
    }

    public static class Builder {

        private Builder() {}

        private String host;
        private Integer port;

        public Builder host(final String host) {

            this.host = host;
            return this;
        }

        public Builder port(final int port) {

            this.port = port;
            return this;
        }

        public HttpConfig build() {
            return new HttpConfig(this);
        }
    }
}
