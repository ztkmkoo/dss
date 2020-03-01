package com.ztkmkoo.dss.core.network.rest.property;

import lombok.Builder;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 2. 오전 12:44
 */
@Getter
public class DssRestChannelProperty {

    private final String host;
    private final int port;

    @Builder
    private DssRestChannelProperty(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
