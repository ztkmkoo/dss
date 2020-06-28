package io.github.ztkmkoo.dss.core.network;

import lombok.Getter;

import java.io.Serializable;

@Getter
public abstract class DssChannelProperty implements Serializable {

    private final String host;
    private final int port;

    protected DssChannelProperty(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
