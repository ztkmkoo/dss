package com.ztkmkoo.dss.server.network.rest.service;

import com.ztkmkoo.dss.server.network.rest.entity.DssRestRequest;
import com.ztkmkoo.dss.server.network.rest.entity.DssRestResponse;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 2. 23. 오후 11:13
 */
public interface DssResHandlerService {

    DssRestResponse handle(DssRestRequest request);
}
