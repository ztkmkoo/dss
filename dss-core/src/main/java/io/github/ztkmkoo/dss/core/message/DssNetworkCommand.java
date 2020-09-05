package io.github.ztkmkoo.dss.core.message;

import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import io.github.ztkmkoo.dss.core.util.ObjectUtils;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 00:40
 */
public interface DssNetworkCommand extends DssCommand {

    @Getter
    class Bind implements DssNetworkCommand {
        private static final long serialVersionUID = -4274539067197850682L;

        private static final String DEFAULT_HOST = "0.0.0.0";
        private static final int DEFAULT_PORT = 8080;
        private static final DssLogLevel DEFAULT_LOG_LEVEL = DssLogLevel.DEBUG;

        private final String host;
        private final int port;
        private final DssLogLevel logLevel;

        protected Bind(Builder builder) {
            this.host = ObjectUtils.defaultValueIfNull(builder.host, DEFAULT_HOST, "host");
            this.port = ObjectUtils.defaultValueIfNull(builder.port, DEFAULT_PORT, "port");
            this.logLevel = ObjectUtils.defaultValueIfNull(builder.logLevel, DEFAULT_LOG_LEVEL, "logLevel");
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String host;
            private Integer port;
            private DssLogLevel logLevel;

            public Builder host(String host) {
                this.host = host;
                return this;
            }

            public Builder port(Integer port) {
                this.port = port;
                return this;
            }

            public Builder logLevel(DssLogLevel logLevel) {
                this.logLevel = logLevel;
                return this;
            }

            public Bind build() {
                return new Bind(this);
            }
        }
    }
}
