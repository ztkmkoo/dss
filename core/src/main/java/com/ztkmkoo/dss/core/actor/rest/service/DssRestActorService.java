package com.ztkmkoo.dss.core.actor.rest.service;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceRequest;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

/**
 * Project: dss
 * Created by: @ztkmkoo(ztkmkoo@gmail.com)
 * Date: 20. 3. 10. 오전 1:51
 */
public interface DssRestActorService {

    String getName();
    String getPath();
    DssRestMethodType getMethodType();

    DssRestServiceResponse handling(DssRestServiceRequest request);

    default DssRestContentInfo consume() {
        return DssRestContentInfo.APPLICATION_JSON_UTF8;
    }

    default DssRestContentInfo produce() {
        return DssRestContentInfo.APPLICATION_JSON_UTF8;
    }
}
