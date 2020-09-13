package io.github.ztkmkoo.dss.core.network.websocket.property;

import io.github.ztkmkoo.dss.core.network.DssChannelProperty;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssLogLevel;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Getter
public class DssWebSocketChannelProperty implements Serializable, DssChannelProperty {

    private static final long serialVersionUID = -2235406911031269400L;

    private final String host;
    private final int port;
    private final DssLogLevel dssLogLevel;

    @Builder
    public DssWebSocketChannelProperty(String host, int port, DssLogLevel dssLogLevel) {
        this.host = host;
        this.port = port;
        this.dssLogLevel = Objects.nonNull(dssLogLevel) ? dssLogLevel : DssLogLevel.DEBUG;
    }
}
