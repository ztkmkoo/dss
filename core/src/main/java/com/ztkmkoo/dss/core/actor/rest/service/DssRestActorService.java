package com.ztkmkoo.dss.core.actor.rest.service;

import com.ztkmkoo.dss.core.actor.rest.entity.DssRestContentInfo;
import com.ztkmkoo.dss.core.actor.rest.entity.DssRestServiceResponse;
import com.ztkmkoo.dss.core.exception.DssRestInvalidContentTypeMappingException;
import com.ztkmkoo.dss.core.message.rest.DssRestServiceActorCommandRequest;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import com.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;

import java.util.Objects;

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

    default void validContentType(DssRestServiceActorCommandRequest commandRequest) {
        final DssRestContentType requiredContentType = getConsume().getContentType();
        Objects.requireNonNull(requiredContentType);

        final DssRestContentType userContentType = commandRequest.getContentType();

        if (Objects.isNull(userContentType)) {
            return;
        }

        if (!requiredContentType.equals(userContentType)) {
            throw new DssRestInvalidContentTypeMappingException("Required " + requiredContentType + " but " + userContentType);
        }
    }
}
