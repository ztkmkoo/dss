package com.ztkmkoo.dss.server.network.enumeration;

import com.ztkmkoo.dss.server.network.core.creator.DssServerCreator;
import com.ztkmkoo.dss.server.network.http.DssHttpServerImpl;
import lombok.Getter;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 9. 오전 12:52
 */
public enum DssNetworkType {

    HTTP(),
    ;

    @Getter
    private final DssServerCreator creator;

    DssNetworkType() {

        this.creator = DssHttpServerImpl::new;
    }
}
