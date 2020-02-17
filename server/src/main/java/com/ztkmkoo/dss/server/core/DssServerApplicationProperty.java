package com.ztkmkoo.dss.server.core;

import com.ztkmkoo.dss.server.network.core.DssServerProperty;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오후 6:45
 */
@Getter
public class DssServerApplicationProperty {

    private final DssServerProperty networkProperty;

    private DssServerApplicationProperty(Builder builder) {
        this.networkProperty = builder.networkProperty;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private DssServerProperty networkProperty;

        private Builder() {}

        public Builder networkProperty(DssServerProperty networkProperty) {
            this.networkProperty = networkProperty;
            return this;
        }

        public DssServerApplicationProperty build() {
            return new DssServerApplicationProperty(this);
        }
    }
}
