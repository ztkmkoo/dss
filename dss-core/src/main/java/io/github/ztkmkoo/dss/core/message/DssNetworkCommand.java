package io.github.ztkmkoo.dss.core.message;

import io.github.ztkmkoo.dss.core.network.DssChannelProperty;
import lombok.Getter;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-08-23 00:40
 */
public interface DssNetworkCommand extends DssCommand {

    @Getter
    class Bind<T extends DssChannelProperty> implements DssNetworkCommand {
        private static final long serialVersionUID = -4274539067197850682L;

        private final T property;

        protected Bind(Builder<T> builder) {
            this.property = builder.property;
        }

        public static <T extends DssChannelProperty> Builder<T> builder() {
            return new Builder<>();
        }

        public static class Builder<T extends DssChannelProperty> {
            private T property;

            public Builder<T> property(T property) {
                this.property = property;
                return this;
            }

            public Bind<T> build() {
                return new Bind<>(this);
            }
        }
    }
}
