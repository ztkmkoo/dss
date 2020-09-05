package io.github.ztkmkoo.dss.core.message;

import akka.actor.typed.ActorRef;
import io.github.ztkmkoo.dss.core.actor.enumeration.DssMasterActorStatus;
import io.github.ztkmkoo.dss.core.common.CommonStaticContents;
import io.github.ztkmkoo.dss.core.common.logging.DssLogLevel;
import io.github.ztkmkoo.dss.core.util.ObjectUtils;
import lombok.Builder;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 00:32
 */
public interface DssMasterCommand extends DssCommand {

    @Getter
    class Bind implements DssMasterCommand, DssBindCommand{
        private static final long serialVersionUID = -6291619473907870504L;

        private final String host;
        private final int port;
        private final DssLogLevel logLevel;

        public Bind(Builder builder) {
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

            public Bind build() {
                return new Bind(this);
            }
        }
    }

    class Shutdown implements DssMasterCommand {
        private static final long serialVersionUID = 8437169186189342859L;
    }

    @Getter
    class StatusRequest implements DssMasterCommand {
        private static final long serialVersionUID = 8724292690016254956L;

        private final ActorRef<DssCommand> sender;

        @Builder
        public StatusRequest(ActorRef<DssCommand> sender) {
            this.sender = sender;
        }
    }

    @Getter
    class StatusResponse implements DssMasterCommand {
        private static final long serialVersionUID = 7231536850003256590L;

        private final DssMasterActorStatus status;

        @Builder
        public StatusResponse(DssMasterActorStatus status) {
            this.status = status;
        }
    }

    @Getter
    class StatusUpdate implements DssMasterCommand {
        private static final long serialVersionUID = 9124412765371994928L;

        private final DssMasterActorStatus status;

        @Builder
        public StatusUpdate(DssMasterActorStatus status) {
            this.status = status;
        }
    }
}
