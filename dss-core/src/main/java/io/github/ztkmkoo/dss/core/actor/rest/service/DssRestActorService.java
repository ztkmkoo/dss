package io.github.ztkmkoo.dss.core.actor.rest.service;

import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import io.github.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import io.github.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

import java.io.IOException;

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

    DssRestServiceResponse handling(DssRestServiceActorCommandRequest commandRequest) throws IOException;
}
