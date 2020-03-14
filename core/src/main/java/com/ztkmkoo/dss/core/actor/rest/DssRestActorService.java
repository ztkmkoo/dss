package com.ztkmkoo.dss.core.actor.rest;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 10. 오전 1:51
 */
public interface DssRestActorService<R extends DssRestServiceRequest, S extends DssRestServiceResponse> {

    String getName();
    String getPath();
    DssRestMethodType getMethodType();

    S handling(R request);
}
