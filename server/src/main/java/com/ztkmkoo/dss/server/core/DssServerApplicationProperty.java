package com.ztkmkoo.dss.server.core;

import com.ztkmkoo.dss.server.actor.core.DssServerActorProperty;
import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import com.ztkmkoo.dss.server.network.core.DssNetworkChannelProperty;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 6:45
 */
@Getter
public class DssServerApplicationProperty {

    private final DssNetworkType networkType;
    private final DssNetworkChannelProperty networkProperty;
    private final DssServerActorProperty dssServerActorProperty;

    private DssServerApplicationProperty(Builder builder) {
        this.networkType = builder.networkType;
        this.networkProperty = builder.networkProperty;
        this.dssServerActorProperty = builder.dssServerActorProperty;
    }

    public static Builder builder(DssNetworkType networkType) {
        return new Builder(networkType);
    }

    public static class Builder {

        private final DssNetworkType networkType;
        private DssNetworkChannelProperty networkProperty;
        private DssServerActorProperty dssServerActorProperty;

        private Builder(DssNetworkType networkType) {
            this.networkType = networkType;
        }

        public Builder networkProperty(DssNetworkChannelProperty networkProperty) {
            this.networkProperty = networkProperty;
            return this;
        }

        public Builder dssServerActorProperty(DssServerActorProperty dssServerActorProperty) {
            this.dssServerActorProperty = dssServerActorProperty;
            return this;
        }

        public DssServerApplicationProperty build() {
            return new DssServerApplicationProperty(this);
        }
    }
}
