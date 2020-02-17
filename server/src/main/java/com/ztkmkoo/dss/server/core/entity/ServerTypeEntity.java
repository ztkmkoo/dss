package com.ztkmkoo.dss.server.core.entity;

import com.ztkmkoo.dss.server.enumeration.DssNetworkType;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 11. 오전 12:46
 */
@Getter
public class ServerTypeEntity {

    private final DssNetworkType networkType;
    private final int port;

    private ServerTypeEntity(DssNetworkType networkType, int port) {
        this.networkType = networkType;
        this.port = port;
    }

    public static ServerTypeEntity of(DssNetworkType networkType, int port) {
        return new ServerTypeEntity(networkType, port);
    }
}
