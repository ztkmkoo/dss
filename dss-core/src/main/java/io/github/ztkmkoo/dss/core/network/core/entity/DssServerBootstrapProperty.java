package io.github.ztkmkoo.dss.core.network.core.entity;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-07-17 00:16
 */
@Getter @ToString
public class DssServerBootstrapProperty {

    public static final DssServerBootstrapProperty DEFAULT_PROPERTY = DssServerBootstrapProperty
            .builder()
            .host("0.0.0.0")
            .port(8080)
            .build();

    private final String host;
    private final int port;

    private DssServerBootstrapProperty(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String host;
        private Integer port;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public DssServerBootstrapProperty build() {
            return new DssServerBootstrapProperty(this);
        }
    }
}
