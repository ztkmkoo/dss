package com.ztkmkoo.dss.core.actor.rest.service;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
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
    DssRestContentInfo getConsume();
    DssRestContentInfo getProduce();

    DssRestServiceResponse handling(DssRestServiceActorCommandRequest commandRequest);
}
