package io.github.ztkmkoo.dss.core.service.rest;

import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestContentType;
import io.github.ztkmkoo.dss.core.network.rest.enumeration.DssRestMethodType;
import io.github.ztkmkoo.dss.core.service.DssService;
import io.github.ztkmkoo.dss.core.service.DssServiceRequest;
import io.github.ztkmkoo.dss.core.service.DssServiceResponse;

/**
 * @author Kebron ztkmkoo@gmail.com
 * @create 2020-12-06 04:01
 */
public interface DssRestService<R extends DssServiceRequest, S extends DssServiceResponse> extends DssService<R, S> {

    String getName();
    String getPath();
    DssRestMethodType getMethodType();
    DssRestContentType getConsumeType();
    DssRestContentType getProduceType();
}
