package com.ztkmkoo.dss.server.network.core.service;

import com.ztkmkoo.dss.server.network.core.entity.DssRequest;
import com.ztkmkoo.dss.server.network.core.entity.DssResponse;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:23
 */
public interface DssService<T extends DssRequest, U extends DssResponse> {

    String path();
    U onReceive(T req);
}
