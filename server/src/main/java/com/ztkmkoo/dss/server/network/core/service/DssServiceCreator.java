package com.ztkmkoo.dss.server.network.core.service;

import com.ztkmkoo.dss.server.network.core.entity.DssRequest;
import com.ztkmkoo.dss.server.network.core.entity.DssResponse;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 17. 오후 10:28
 */
public interface DssServiceCreator <T extends DssRequest, U extends DssResponse, V extends DssService<T, U>> {
    V create();
}
