package com.ztkmkoo.dss.network.enumeration;

import com.ztkmkoo.dss.network.common.creator.DssServerCreator;
import com.ztkmkoo.dss.network.http.DssHttpServerImpl;
import lombok.Getter;

public enum DssNetworkType {

    HTTP(),
    ;

    @Getter
    private final DssServerCreator creator;

    DssNetworkType() {

        this.creator = DssHttpServerImpl::new;
    }
}
