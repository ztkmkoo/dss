package io.github.ztkmkoo.dss.core.message;

import akka.actor.typed.ActorRef;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 00:40
 */
public interface DssNetworkCommand extends DssCommand {

    @Getter
    class Bind extends DssBindCommand implements DssNetworkCommand {
        private static final long serialVersionUID = -4274539067197850682L;

        protected Bind(Builder builder) {
            super(builder);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder extends DssBindCommand.Builder<Bind> {

            public Builder bind(DssMasterCommand.Bind bind) {
                if (Objects.nonNull(bind)) {
                    this
                            .host(bind.getHost())
                            .port(bind.getPort())
                            .logLevel(bind.getLogLevel());
                }
                return this;
            }

            @Override
            public Bind build() {
                return new Bind(this);
            }
        }
    }

    class Close implements DssNetworkCommand {
        private static final long serialVersionUID = 952560633714242274L;

        public static final Close INST = new Close();
    }

    @Getter
    class ConfigMasterActor implements DssNetworkCommand {
        private static final long serialVersionUID = -8292622262021867200L;

        private final ActorRef<DssMasterCommand> masterActor;

        @Builder
        public ConfigMasterActor(ActorRef<DssMasterCommand> masterActor) {
            this.masterActor = masterActor;
        }
    }

    @Getter
    class ConfigResolverActor implements DssNetworkCommand {
        private static final long serialVersionUID = -5773181846667820027L;

        private final ActorRef<DssResolverCommand> resolverActor;

        @Builder
        public ConfigResolverActor(ActorRef<DssResolverCommand> resolverActor) {
            this.resolverActor = resolverActor;
        }
    }
}
