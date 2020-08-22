package io.github.ztkmkoo.dss.core.network.tcp.property;

import java.io.*;

import io.github.ztkmkoo.dss.core.network.*;
import lombok.*;

@Getter
public class DssTcpChannelProperty implements Serializable, DssChannelProperty {

    private static final long serialVersionUID = -6904414304979344025L;
    private final String host;
    private final int port;

    @Builder
    public DssTcpChannelProperty(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
