package io.github.ztkmkoo.dss.core.message;

import io.github.ztkmkoo.dss.core.common.CommonStaticContents;
import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import io.github.ztkmkoo.dss.core.util.ObjectUtils;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 00:40
 */
public interface DssNetworkCommand extends DssCommand {

    @Getter
    class Bind implements DssNetworkCommand, DssBindCommand {
        private static final long serialVersionUID = -4274539067197850682L;

        private final String host;
        private final int port;
        private final DssLogLevel logLevel;

        protected Bind(Builder builder) {
            this.host = ObjectUtils.defaultValueIfNull(builder.host, CommonStaticContents.getDefaultHost(), CommonStaticContents.getTextHost());
            this.port = ObjectUtils.defaultValueIfNull(builder.port, CommonStaticContents.getDefaultPort(), CommonStaticContents.getTextPort());
            this.logLevel = ObjectUtils.defaultValueIfNull(builder.logLevel, CommonStaticContents.getDefaultLogLevel(), CommonStaticContents.getTextLogLevel());
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

            public Builder bind(DssMasterCommand.Bind bind) {
                if (Objects.nonNull(bind)) {
                    this.host = bind.getHost();
                    this.port = bind.getPort();
                    this.logLevel = bind.getLogLevel();
                }
                return this;
            }

            public Bind build() {
                return new Bind(this);
            }
        }
    }
}
