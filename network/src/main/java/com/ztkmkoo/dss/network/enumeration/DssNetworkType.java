package com.ztkmkoo.dss.network.enumeration;

import com.ztkmkoo.dss.network.common.creator.DssServerCreator;
import com.ztkmkoo.dss.network.http.DssHttpServerImpl;
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
