package io.github.ztkmkoo.dss.core.network.websocket.property;

import io.github.ztkmkoo.dss.core.network.DssChannelProperty;
import lombok.Builder;

public class DssWebSocketChannelProperty extends DssChannelProperty {

    @Builder
    private DssWebSocketChannelProperty(String host, int port) {
        super(host, port);
    }
}
